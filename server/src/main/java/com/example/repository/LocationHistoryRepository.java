package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.domain.LocationHistory;

public interface LocationHistoryRepository extends PagingAndSortingRepository<LocationHistory, Long> {
	List<LocationHistory> findByDeviceId(String deviceId,Pageable page);
}
