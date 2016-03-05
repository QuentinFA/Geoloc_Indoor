package com.example.controller;

import org.geojson.GeoJsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Beacon;
import com.example.domain.Measurement;
import com.example.repository.LocationRepository;
import com.example.service.LocationService;

@RestController
@RequestMapping("devices")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@Autowired
	private LocationRepository locationRepository;

	@RequestMapping(value = "/{deviceId}/location")
	public GeoJsonObject getDevicePosition(@PathVariable String deviceId) {

		return locationService.getDevicePosition(deviceId);

	}

	@RequestMapping("test")
	public boolean test() {

		System.out.println("test ..");
		Beacon beacon = new Beacon();

		Measurement measurment = new Measurement();
		
		measurment.setSomeValue(1.11);
		measurment.setAnotherValue(1.23);
		
		beacon.setDeviceId("device1");
		beacon.setIdPlaca("placa1");
		beacon.setMeasurement(measurment);

		locationRepository.save(beacon);

		return true;
	}

}
