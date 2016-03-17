package com.example.controller;

import java.util.List;

import javax.validation.Valid;

import org.geojson.GeoJsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Beacon;
import com.example.domain.LocationHistory;
import com.example.domain.LocationSTm;
import com.example.service.LocationService;

@RestController
@RequestMapping("devices")
public class LocationController {

	@Autowired
	private LocationService locationService;

	/*
	@RequestMapping(value = "/{deviceId}/location", method = RequestMethod.POST)
	public LocationHistory addDeviceLocation(@PathVariable String deviceId,
			@RequestBody @Valid LocationHistory location) {
		return locationService.addDeviceLocation(deviceId, location);
	}
	*/
	
	@RequestMapping(value = "/{deviceId}/location", method = RequestMethod.POST)
	public Beacon addBeaconInformation(@PathVariable String deviceId,
			@RequestBody @Valid Beacon beacon) {
		return locationService.addBeaconInformation(beacon);
	}
	@RequestMapping(value="/locationSTm", method =RequestMethod.POST)
	public LocationSTm addSTmLocation(@RequestBody @Valid LocationSTm locationSTm)
	{
		return locationService.addLocationSTm(locationSTm);
	}
	
	@RequestMapping(value = "/{deviceId}/history",  method = RequestMethod.GET)
	public List<LocationHistory> getDevicePositionHistory(
			@PathVariable String deviceId, 
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "20") int size) {
		return locationService.getDeviceHistory(deviceId, page, size);
	}
	
	@RequestMapping(value = "/{deviceId}/locationFin", method = RequestMethod.GET)
	public GeoJsonObject getSTmLocation(@PathVariable String deviceId)
	{
		GeoJsonObject beacon = locationService.getDevicePosition(deviceId);
		if(beacon !=null)
		{
			return beacon;
		}
		return null;
	}
	

}
