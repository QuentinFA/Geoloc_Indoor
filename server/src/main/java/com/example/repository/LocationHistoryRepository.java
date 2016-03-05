package com.example.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.domain.LocationHistory;

public interface LocationHistoryRepository extends CrudRepository<LocationHistory, Long> {
	List<LocationHistory> findByDeviceId(String deviceId);
}
