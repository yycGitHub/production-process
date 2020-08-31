/**
 * 
 */
package com.surekam.modules.api.dto.resp;

import java.io.Serializable;

/**
 * Title: a Description:
 * 
 * @author tangjun
 * @date 2019年5月17日
 */
public class StatisticsBatchResq implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 采收量
	 */
	public String taskItemArgsValue;

	/**
	 * 采收量单位
	 */
	public String taskItemArgsValueUnit;
	/**
	 * 投入量
	 */
	public String batchNumber;
	/**
	 * 投入量量单位
	 */
	public String batchNumberUnit;

	/**
	 * 采收品种
	 */
	public String argsName;
	/**
	 * 品种名称
	 */
	public String productName;

	public String getTaskItemArgsValue() {
		return taskItemArgsValue;
	}

	public void setTaskItemArgsValue(String taskItemArgsValue) {
		this.taskItemArgsValue = taskItemArgsValue;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getTaskItemArgsValueUnit() {
		return taskItemArgsValueUnit;
	}

	public void setTaskItemArgsValueUnit(String taskItemArgsValueUnit) {
		this.taskItemArgsValueUnit = taskItemArgsValueUnit;
	}

	public String getBatchNumberUnit() {
		return batchNumberUnit;
	}

	public void setBatchNumberUnit(String batchNumberUnit) {
		this.batchNumberUnit = batchNumberUnit;
	}

	public String getArgsName() {
		return argsName;
	}

	public void setArgsName(String argsName) {
		this.argsName = argsName;
	}

}
