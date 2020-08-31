package com.surekam.modules.api.dto.resp;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

/**
 * 传感器阈值表返回参数
 * 
 * @author tangjun
 * @version 2019-05-10
 */
@Repository
public class SensorThresholdRresp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 指标ID
	 */
	private String targetId;
	/**
	 * 生长周期ID
	 */
	private String growthCycleId;
	/**
	 * 最大值
	 */
	private String maxValue;
	/**
	 * 最小值
	 */
	private String minValue;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getGrowthCycleId() {
		return growthCycleId;
	}

	public void setGrowthCycleId(String growthCycleId) {
		this.growthCycleId = growthCycleId;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

}
