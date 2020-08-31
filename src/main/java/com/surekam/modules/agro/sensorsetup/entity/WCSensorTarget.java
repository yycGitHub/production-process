package com.surekam.modules.agro.sensorsetup.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WCSensorTarget {

	@JsonProperty("SensorSn")
	private String sensorSn;
	@JsonProperty("SensorSite")
	private String sensorSite;
	@JsonProperty("TargetUnit")
	private String targetUnit;
	@JsonProperty("TargetName")
	private String targetName;
	
	public String getSensorSn() {
		return sensorSn;
	}
	public void setSensorSn(String sensorSn) {
		this.sensorSn = sensorSn;
	}
	public String getSensorSite() {
		return sensorSite;
	}
	public void setSensorSite(String sensorSite) {
		this.sensorSite = sensorSite;
	}
	public String getTargetUnit() {
		return targetUnit;
	}
	public void setTargetUnit(String targetUnit) {
		this.targetUnit = targetUnit;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
}
