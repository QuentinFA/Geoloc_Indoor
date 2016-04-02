package com.example.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import com.example.domain.Beacon;
import com.example.domain.Measurement;

public class STEventListener implements SerialPortEventListener {

	private SerialPort port;
	private AmazonService amazonService;
	
	
	private boolean receivedMessage = false;
	StringBuilder message = new StringBuilder();

	public STEventListener(SerialPort port, AmazonService amazonService) {
		this.port = port;
		this.amazonService = amazonService;
	}
	

	public Beacon convertStringToBeacon(String parts[]){
		
		if(parts.length != 4){
			throw new RuntimeException("Incorrect number of parts");
		}
		
		Beacon beacon = new Beacon();
		double deviceId = Double.parseDouble(parts[2]);
		beacon.setDeviceId(deviceId);
		
		double signalStrength = Double.parseDouble(parts[3]);
		
		beacon.setIdPlaca(parts[1]);
		beacon.setMeasurement(signalStrength);
		
		beacon.setNameOfDevice( UUID.randomUUID().toString());
		
		return beacon;
	}
	
	public void serialEvent(SerialPortEvent event) {

		if (event.isRXCHAR()) {

			try {
				//System.out.println("received nr bytes : " + event.getEventValue());
				
				byte[] receivedBytes = port.readBytes(event.getEventValue()); //citim exact cate caractere primim
				//System.out.println("Received bytes : " + new String(receivedBytes, "UTF-8")); //se goleste buffer-ul
				for (byte b : receivedBytes)
				{
					if(b == '>')
					{
						receivedMessage = true;
						message.setLength(0);
					}
					else if(receivedMessage == true){
						if(b == '\n')
						{
							receivedMessage = false;
							String messageReceived = message.toString();
							String[] parts = messageReceived.split("#");
							for(String s : parts){
								System.out.println(s);
							}
							
							try{
								Beacon beacon = convertStringToBeacon(parts);
								beacon.setReceivedDate(LocalDateTime.now());
								amazonService.sendBeacon(beacon);
								
								
							} catch(RuntimeException e){
								System.out.println(e);
							}
							
						}
						else{
							message.append((char)b);
						}
					}
				}
				//System.out.println(stringExtractor(receivedBytes));
			
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			//}
		}

		else if (event.isCTS()) {// If CTS line has changed state
			if (event.getEventValue() == 1) {// If line is ON
				System.out.println("CTS - ON");
			} else {
				System.out.println("CTS - OFF");
			}
		} else if (event.isDSR()) {// /If DSR line has changed state
			if (event.getEventValue() == 1) {// If line is ON
				System.out.println("DSR - ON");
			} else {
				System.out.println("DSR - OFF");
			}
		}
	}
}
