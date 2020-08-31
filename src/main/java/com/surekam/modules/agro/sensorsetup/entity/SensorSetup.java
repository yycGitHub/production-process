package com.surekam.modules.agro.sensorsetup.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 传感器管理Entity
 * 
 * @author luoxw
 * @version 2019-04-22
 */
@Entity
@Table(name = "t_agro_sensor_setup")
@DynamicInsert
@DynamicUpdate
public class SensorSetup extends XGXTEntity<SensorSetup> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String subId;// 子ID
	private String officeId;// 公司ID
	private String officeName;// 公司名称
	private String sensorName;// 传感器名称
	private String sensorSerialNumber;// 传感器设备号
	private String sensorDiscription;// 描述
	private String website;// 网址
	private String gatewayId;// 传感器网关ID
	private String leaseStatus;// 租赁状态 0-无限 1-短期
	private String leaseTime; // 租赁时间

	public SensorSetup() {
		super();
	}

	@PrePersist
	public void prePersist() {
		this.id = IdGen.uuid();
	}

	@Id
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSensorName() {
		return this.sensorName;
	}

	public void setSensorName(String sensorName) {
		if (StringUtils.isNotBlank(sensorName)) {
			this.sensorName = sensorName.trim();
		} else {
			this.sensorName = sensorName;
		}
	}

	public String getSensorSerialNumber() {
		return this.sensorSerialNumber;
	}

	public void setSensorSerialNumber(String sensorSerialNumber) {
		this.sensorSerialNumber = sensorSerialNumber;
	}

	public String getSensorDiscription() {
		return this.sensorDiscription;
	}

	public void setSensorDiscription(String sensorDiscription) {
		this.sensorDiscription = sensorDiscription;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getGatewayId() {
		return this.gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
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

	@Transient
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

}
