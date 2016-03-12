package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.example.domain.LocationHistory;
import com.example.repository.LocationHistoryRepository;

@Component
public class LocationService {

	@Autowired
	private LocationHistoryRepository locationRepositoryHistory;

	public LocationHistory addDeviceLocation(String deviceId, LocationHistory location) {
		return locationRepositoryHistory.save(location);
	}
	
	
	public List<LocationHistory> getDeviceHistory(String deviceId,int page, int size){
		final PageRequest page1 = new PageRequest(
				page, size, Direction.DESC, "date");
		return locationRepositoryHistory.findByDeviceId(deviceId, page1);

	}
}
