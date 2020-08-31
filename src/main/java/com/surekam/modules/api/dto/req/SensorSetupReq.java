/**
 * 
 */
package com.surekam.modules.api.dto.req;

import java.io.Serializable;

/**
 * Title: SensorSetupReq Description: 传感器请求参数
 * 
 * @author tangjun
 * @date 2019年6月19日
 */
public class SensorSetupReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 子ID
	 */
	private String subId;
	/**
	 * 公司ID
	 */
	private String officeId;
	/**
	 * 传感器名称
	 */
	private String sensorName;
	/**
	 * 传感器设备号
	 */
	private String sensorSerialNumber;
	/**
	 * 描述
	 */
	private String sensorDiscription;
	/**
	 * 网址
	 */
	private String website;
	/**
	 * 传感器网关ID
	 */
	private String gatewayId;
	/**
	 * 租赁时间
	 */
	private String leaseTime;
	/**
	 * 租赁状态
	 */
	private String leaseStatus;

	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public String getSensorSerialNumber() {
		return sensorSerialNumber;
	}

	public void setSensorSerialNumber(String sensorSerialNumber) {
		this.sensorSerialNumber = sensorSerialNumber;
	}

	public String getSensorDiscription() {
		return sensorDiscription;
	}

	public void setSensorDiscription(String sensorDiscription) {
		this.sensorDiscription = sensorDiscription;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getLeaseTime() {
		return leaseTime;
	}

	public void setLeaseTime(String leaseTime) {
		this.leaseTime = leaseTime;
	}

	public String getLeaseStatus() {
		return leaseStatus;
	}

	public void setLeaseStatus(String leaseStatus) {
		this.leaseStatus = leaseStatus;
	}

}
