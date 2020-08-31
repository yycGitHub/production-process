package com.surekam.modules.agro.sensorthreshold.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 传感器阈值表Entity
 * 
 * @author tangjun
 * @version 2019-05-10
 */
@Entity
@Table(name = "t_agro_sensor_threshold")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SensorThreshold extends XGXTEntity<SensorThreshold> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String targetId;// 指标ID
	private String growthCycleId;// 生长周期ID
	private String minValue;// 最小值
	private String maxValue;// 最大值
	public SensorThreshold() {
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

	public String getTargetId() {
		return this.targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getGrowthCycleId() {
		return this.growthCycleId;
	}

	public void setGrowthCycleId(String growthCycleId) {
		this.growthCycleId = growthCycleId;
	}

	public String getMaxValue() {
		return this.maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getMinValue() {
		return this.minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

}
