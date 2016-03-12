package com.example.domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class Measurement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	private double someValue;
	
	@NotNull
	private double anotherValue;

	public double getSomeValue() {
		return someValue;
	}

	public void setSomeValue(double someValue) {
		this.someValue = someValue;
	}

	public double getAnotherValue() {
		return anotherValue;
	}

	public void setAnotherValue(double anotherValue) {
		this.anotherValue = anotherValue;
	}
	
	
}
