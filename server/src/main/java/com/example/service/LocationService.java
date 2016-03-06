package com.example.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.example.domain.Beacon;
import com.example.domain.LocationHistory;
import com.example.repository.LocationHistoryRepository;
import com.example.repository.LocationRepository;

@Component
public class LocationService {

	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private LocationHistoryRepository locationRepositoryHistory;

	
	public GeoJsonObject getDevicePosition(String deviceId){
		
		List<Beacon> beaconList = locationRepository.findByDeviceId(deviceId);
		
	
		double longitude = 0;
		double latitude = 0;
		//calcul
		for(Beacon beacon : beaconList){
			// if beacon.isValid ==> calcul //// if currentTime - beacon.timeOfReceive < Limit
			longitude += beacon.getMeasurement().getSomeValue();
			latitude += beacon.getMeasurement().getAnotherValue();
			// else nothing
		}
		
		// IL FAUT AU MOINS 3 BALISES AYANT DETECTE
		
		
		LngLatAlt devicePosition = new LngLatAlt(longitude, latitude);
		GeoJsonObject object = new Point(devicePosition);
		LocationHistory locationHistory = new LocationHistory();
		locationHistory.setDeviceId(deviceId);
		locationHistory.setDate(LocalDateTime.now());
		locationHistory.setLatitude(latitude);
		locationHistory.setLongitude(longitude);
		locationRepositoryHistory.save(locationHistory);
		System.out.println("SAVED !\n");
		return object;
		
	}
	public List<LocationHistory> getDeviceHistory(String deviceId,int page, int size){
		final PageRequest page1 = new PageRequest(
				page, size, Direction.DESC, "date");
		return locationRepositoryHistory.findByDeviceId(deviceId, page1);

	}
}
