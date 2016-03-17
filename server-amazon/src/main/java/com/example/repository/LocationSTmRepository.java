package com.example.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.domain.LocationSTm;

public interface LocationSTmRepository extends CrudRepository<LocationSTm, Integer> {
	LocationSTm findByidSTm(String idSTm);
	

}
