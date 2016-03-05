package com.example.service.test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import jssc.SerialPort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.domain.Beacon;
import com.example.domain.Measurement;
import com.example.repository.LocationRepository;
import com.example.service.STEventListener;

@Component
public class MockSTConnector {

	@Autowired
	private LocationRepository locationRepository;
	
	@PostConstruct
	public void init(){
		System.out.println("Simulating connection to 3 devices ..");
		connectToThreeDevices();
		
	}
	
	public Beacon convertStringToBeacon(String parts[], String id){
		
		if(parts.length !=4){
			throw new RuntimeException("Incorrect number of parts");
		}
		
		Beacon beacon = new Beacon();
		beacon.setDeviceId(parts[1]);
		
		double longitude = Double.parseDouble(parts[2]);
		double lat = Double.parseDouble(parts[3]);
		
		Measurement m = new Measurement();
		m.setAnotherValue(longitude);
		m.setSomeValue(lat);
		
		beacon.setIdPlaca(id);
		beacon.setMeasurement(m);
		
		return beacon;
	}
	
	public  Runnable simulatePlaca(String idPlaca, String deviceId){
		return () -> {
			
			while(true){
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String value1 = String.valueOf(new Random().nextDouble());
				String value2 = String.valueOf(new Random().nextDouble());
				
				
				String[] parts = new String[]{"", deviceId , value1, value2};
				
		
				
				Beacon beacon = convertStringToBeacon(parts, idPlaca);
				beacon.setReceivedDate(LocalDateTime.now());
				locationRepository.save(beacon);
				LocalDateTime currentDate = LocalDateTime.now();
				List<Beacon> beacons = locationRepository.findByDeviceId(deviceId);
				for(Beacon enreg : beacons)
				{
					long seconds = ChronoUnit.SECONDS.between(enreg.getReceivedDate(), currentDate);
					if(seconds > 20)
					{
						locationRepository.delete(enreg);
					}
			}
			}
			
			
		};
	}
	
	public boolean connectToThreeDevices(){
		Thread placa1 = new Thread(simulatePlaca("COM1", "123"));
		Thread placa2 = new Thread(simulatePlaca("COM2", "123"));
		Thread placa3 = new Thread(simulatePlaca("COM3", "123"));
		
		placa1.start();
		placa2.start();
		placa3.start();
		
		return true;
	}
	

}
