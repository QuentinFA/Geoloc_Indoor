package com.example.service;

import javax.annotation.PostConstruct;

import jssc.SerialPort;
import jssc.SerialPortException;

import org.springframework.beans.factory.annotation.Autowired;

//TODO Uncomment @Component when using real devices
//@Component
public class STConnector {

	@Autowired
	private AmazonService amazonService;
	

	@PostConstruct
	public void init(){
		connect("COM6");
		
	}
	
	public boolean connect(String portName) {

		SerialPort port = SerialConnector.connect(portName);
		STEventListener listener = new STEventListener(port, amazonService);

		try {
			port.addEventListener(listener);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return true;

	}
}
