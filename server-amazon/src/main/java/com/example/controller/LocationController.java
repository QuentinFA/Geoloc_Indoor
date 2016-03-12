package com.example.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.LocationHistory;
import com.example.service.LocationService;

@RestController
@RequestMapping("devices")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@RequestMapping(value = "/{deviceId}/location", method = RequestMethod.POST)
	public LocationHistory addDeviceLocation(@PathVariable String deviceId,
			@RequestBody @Valid LocationHistory location) {
		return locationService.addDeviceLocation(deviceId, location);
	}

	@RequestMapping(value = "/{deviceId}/history",  method = RequestMethod.GET)
	public List<LocationHistory> getDevicePositionHistory(
			@PathVariable String deviceId, 
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "20") int size) {
		return locationService.getDeviceHistory(deviceId, page, size);
	}

}
