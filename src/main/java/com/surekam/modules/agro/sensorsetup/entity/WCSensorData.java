package com.surekam.modules.agro.sensorsetup.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WCSensorData {

	@JsonProperty("SensorSn")
	private String sensorSn;
	@JsonProperty("MeasureData")
	private Double measureData;
	@JsonProperty("MeasureTime")
	private String measureTime;
	
	public String getSensorSn() {
		return sensorSn;
	}
	public void setSensorSn(String sensorSn) {
		this.sensorSn = sensorSn;
	}
	public Double getMeasureData() {
		return measureData;
	}
	public void setMeasureData(Double measureData) {
		this.measureData = measureData;
	}
	public String getMeasureTime() {
		return measureTime;
	}
	public void setMeasureTime(String measureTime) {
		this.measureTime = measureTime;
	}
}
