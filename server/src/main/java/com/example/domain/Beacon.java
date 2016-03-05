package com.example.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Class which represents a Beacon, which is inplemented by a STM32 or an
 * Arduino
 */
@Entity
public class Beacon {
	/**
	 * Idenetification of the beacon
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String deviceId;

	private String idPlaca;
	
	private LocalDateTime receivedDate;
	
	public LocalDateTime getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(LocalDateTime receivedDate) {
		this.receivedDate = receivedDate;
	}

	/**
	 * Position of the beacon TODO : Which kind of object ?
	 */
	private Measurement measurement;

	/*
	 * Connection with the beacon
	 */
	private boolean connection;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public boolean isConnection() {
		return connection;
	}

	public void setConnection(boolean connection) {
		this.connection = connection;
	}

	public String getIdPlaca() {
		return idPlaca;
	}

	public void setIdPlaca(String idPlaca) {
		this.idPlaca = idPlaca;
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	
	

}