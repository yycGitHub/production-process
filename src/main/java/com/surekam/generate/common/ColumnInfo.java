package com.surekam.generate.common;

public class ColumnInfo {

	private String javaClass;
	private String instance;
	private String logicalName;
	private String physicalInstance;
	private String physicalName;
	
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
	public String getPhysicalInstance() {
		return physicalInstance;
	}
	public void setPhysicalInstance(String physicalInstance) {
		this.physicalInstance = physicalInstance;
	}
	public String getPhysicalName() {
		return physicalName;
	}
	public void setPhysicalName(String physicalName) {
		this.physicalName = physicalName;
	}
	public String getJavaClass() {
		return javaClass;
	}
	public void setJavaClass(String javaClass) {
		this.javaClass = javaClass;
	}
	public String getLogicalName() {
		return logicalName;
	}
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
}
