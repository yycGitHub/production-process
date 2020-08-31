package com.surekam.modules.agro.sensorsetup.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SensorData {

	@JsonProperty("Id")
	private String id;
	@JsonProperty("PathName")
	private String pathName;
	@JsonProperty("TargetName")
	private String targetName;
	@JsonProperty("MeasureMaxData")
	private String measureMaxData;
	@JsonProperty("MeasureMinData")
	private String measureMinData;
	@JsonProperty("MeasureData")
	private String measureData;
	@JsonProperty("MeasureTime")
	private String measureTime;
	@JsonProperty("TargetIcon")
	private String targetIcon;
	@JsonProperty("TargetUnit")
	private String targetUnit;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMeasureMaxData() {
		return measureMaxData;
	}
	public void setMeasureMaxData(String measureMaxData) {
		this.measureMaxData = measureMaxData;
	}
	public String getMeasureMinData() {
		return measureMinData;
	}
	public void setMeasureMinData(String measureMinData) {
		this.measureMinData = measureMinData;
	}
	public String getMeasureData() {
		return measureData;
	}
	public void setMeasureData(String measureData) {
		this.measureData = measureData;
	}
	public String getMeasureTime() {
		return measureTime;
	}
	public void setMeasureTime(String measureTime) {
		this.measureTime = measureTime;
	}
	public String getTargetIcon() {
		return targetIcon;
	}
	public void setTargetIcon(String targetIcon) {
		this.targetIcon = targetIcon;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getTargetUnit() {
		return targetUnit;
	}
	public void setTargetUnit(String targetUnit) {
		this.targetUnit = targetUnit;
	}
	public String getPathName() {
		return pathName;
	}
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
}
