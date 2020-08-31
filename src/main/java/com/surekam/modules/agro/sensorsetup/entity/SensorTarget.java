package com.surekam.modules.agro.sensorsetup.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SensorTarget {

	@JsonProperty("Id")
	private String id;
	@JsonProperty("TargetName")
	private String targetName;
	@JsonProperty("TargetUnit")
	private String targetUnit;
	@JsonProperty("TargetId")
	private String targetId;
	@JsonProperty("SensorId")
	private String sensorId;
	@JsonProperty("GateWayId")
	private String gateWayId;
	@JsonProperty("TargetIcon")
	private String targetIcon;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getSensorId() {
		return sensorId;
	}
	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}
	public String getGateWayId() {
		return gateWayId;
	}
	public void setGateWayId(String gateWayId) {
		this.gateWayId = gateWayId;
	}
	public String getTargetIcon() {
		return targetIcon;
	}
	public void setTargetIcon(String targetIcon) {
		this.targetIcon = targetIcon;
	}
}
