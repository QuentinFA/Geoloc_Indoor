package com.example.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	
	private byte[] bufferArray = new byte[1024];
	private int bufferSize;  //contains the remaining characters after last /n
	
	private List<String> stringExtractor(byte[] newArray) throws UnsupportedEncodingException{
		
		
		int i = 0;
		List<String> lineList = new ArrayList<>();
		while(i < newArray.length){
			if(newArray[i] == 10){
				byte[] remainingPart = Arrays.copyOfRange(bufferArray, 0, bufferSize);
				byte[] newPart = Arrays.copyOfRange(newArray, 0, i);
				
				byte[] fullLine = Arrays.copyOf(remainingPart, remainingPart.length + newPart.length);
				  System.arraycopy(newPart, 0, fullLine, remainingPart.length, newPart.length);
				  
				lineList.add(new String(fullLine, "UTF-8"));
				  
				bufferArray = Arrays.copyOfRange(newArray, i, newArray.length);
				bufferSize = newArray.length - i;
				
				
			}
			
			i++;
		}
		
		return lineList;
	}

	public Beacon convertStringToBeacon(String parts[]){
		
		if(parts.length !=4){
			throw new RuntimeException("Incorrect number of parts");
		}
		
		Beacon beacon = new Beacon();
		beacon.setDeviceId(parts[1]);
		
		double longitude = Double.parseDouble(parts[2]);
		double lat = Double.parseDouble(parts[3]);
		
		Measurement m = new Measurement();
		m.setLongitude(longitude);
		m.setLatitude(lat);
		
		beacon.setIdPlaca(port.getPortName());
		beacon.setMeasurement(m);
		
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
