package com.example.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.example.domain.Beacon;
import com.example.domain.LocationHistory;
import com.example.domain.LocationSTm;
import com.example.repository.LocationHistoryRepository;
import com.example.repository.LocationRepository;
import com.example.repository.LocationSTmRepository;

@Component
public class LocationService {

	@Autowired
	private LocationHistoryRepository locationRepositoryHistory;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private LocationSTmRepository locationSTmRepository;

	public static final double TIME_LIMIT = 10;
	public LocationHistory addDeviceLocation(String deviceId, LocationHistory location) {
		return locationRepositoryHistory.save(location);
	}
	
	public Beacon addBeaconInformation(Beacon beacon){
		return locationRepository.save(beacon);
	}
	
	public LocationSTm addLocationSTm(LocationSTm locationSTm)
	{
		return locationSTmRepository.save(locationSTm);
	}
	
	
	public List<LocationHistory> getDeviceHistory(String deviceId,int page, int size){
		final PageRequest page1 = new PageRequest(
				page, size, Direction.DESC, "date");
		return locationRepositoryHistory.findByDeviceId(deviceId, page1);

	}
	
	public GeoJsonObject getDevicePosition(String deviceId){
		//prelevation des coordones des balises de la bases de donnees.
		HashMap<Integer, Integer> levels = new HashMap<Integer,Integer>();
		Map<String, LocationSTm> stmLocations = new HashMap<>();
		List<Beacon> beaconList = locationRepository.findByDeviceId(deviceId); //
		List<Beacon> calculBeacons = new ArrayList<Beacon>(); //as
		LocalDateTime currentDate = LocalDateTime.now();
		if(beaconList.size()>0){
			for(Beacon enreg : beaconList){
				long seconds = ChronoUnit.SECONDS.between(enreg.getReceivedDate(),currentDate);
				if(seconds <=TIME_LIMIT)
				{
					calculBeacons.add(enreg);
					System.out.println("ADDED!");
					System.out.println("LATITUDINE_FOR :" +enreg.getMeasurement().getLatitude());
					System.out.println("LONGITUDINE_FOR :"+enreg.getMeasurement().getLongitude());
					
					
				}
				else
				{
					locationRepository.delete(enreg);
				}
			}
		}
		double longitude = 0;
		double latitude = 0;
		if(calculBeacons.size()>=3)
		{
			double xStm=0;
			double yStm=0;
			double coordX=0;
			double coordY=0;
			double sumLatitude =0;
			double sumLongitude = 0;
			for(Beacon beacon : calculBeacons)
			{
				String idSTM = beacon.getIdPlaca();
				LocationSTm locationSTm = locationSTmRepository.findByidSTm(idSTM);
				int level = locationSTm.getLevel();
				stmLocations.put(idSTM, locationSTm);
				levels.put(level, 1);
			}
			
			if(levels.size() == 1) //si toutes les cartes sont sur le meme etage
			{  
				for(Beacon beacon : calculBeacons)
				{
					String idSTM = beacon.getIdPlaca();
					LocationSTm locationSTm = stmLocations.get(idSTM);
					
					xStm =locationSTm.getLocation().getLatitude();
					yStm = locationSTm.getLocation().getLongitude();
					System.out.println("XStm : "+xStm);
					System.out.println("YStm : "+yStm);
					longitude = beacon.getMeasurement().getLongitude();
					sumLongitude +=longitude;
					latitude =  beacon.getMeasurement().getLatitude();
					System.out.println("LATITUDINE :" +latitude);
					System.out.println("LONGITUDINE :"+longitude);
					sumLatitude+=latitude;
					coordX = coordX + (xStm *latitude);
					coordY = coordY + (yStm * longitude);
					System.out.println("COORDONATA X din for :"+coordX);
					System.out.println("COORDONATA Y din for :"+coordY);
					
				}
				coordX = coordX/sumLatitude;
				coordY = coordY/sumLongitude;
				System.out.println("COORDONATA X :"+coordX);
				System.out.println("COORDONATA y :"+coordY);
				
			}
			else
			{
				//TODO
			}
			LngLatAlt devicePosition = new LngLatAlt(coordX,coordY);
			GeoJsonObject object = new Point(devicePosition);
			LocationHistory locationHistory = new LocationHistory();
			locationHistory.setDeviceId(deviceId);
			locationHistory.setDate(LocalDateTime.now());
			locationHistory.setLatitude(latitude);
			locationHistory.setLongitude(longitude);
			locationRepositoryHistory.save(locationHistory);
			System.out.println("SAVED !\n");
			for(Beacon b : calculBeacons){
				
				locationRepository.delete(b);
			}
			
			return object;

			
		}
		return null;
	}
}
