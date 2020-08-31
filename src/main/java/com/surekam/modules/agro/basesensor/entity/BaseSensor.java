package com.surekam.modules.agro.basesensor.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.utils.IdGen;

/**
 * 基地传感器表Entity
 * 
 * @author tangjun
 * @version 2019-06-12
 */
@Entity
@Table(name = "t_agro_base_sensor")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BaseSensor implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String baseId;// 基地ID
	private String officeId;// 公司ID
	private String sensorId; // 传感器ID

	public BaseSensor() {
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

	public String getBaseId() {
		return this.baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}

	public String getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

}
