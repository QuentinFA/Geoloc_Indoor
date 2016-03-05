package com.example.service;

import java.util.List;

import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.domain.Beacon;
import com.example.repository.LocationRepository;

@Component
public class LocationService {

	@Autowired
	private LocationRepository locationRepository;

	
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
		
		return object;
		
	}
}
