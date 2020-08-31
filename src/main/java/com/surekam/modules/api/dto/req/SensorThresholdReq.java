package com.surekam.modules.api.dto.req;

import java.io.Serializable;

/**
 * 传感器阈值表请求参数
 * 
 * @author tangjun
 * @version 2019-05-10
 */
public class SensorThresholdReq implements Serializable {

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
