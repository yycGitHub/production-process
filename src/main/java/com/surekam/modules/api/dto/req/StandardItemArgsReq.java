/**
 * 
 */
package com.surekam.modules.api.dto.req;

import java.io.Serializable;

/**
 * Title: StandardItemArgsReq Description:
 * 
 * @author tangjun
 * @date 2019年5月6日
 */
public class StandardItemArgsReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 作业项ID
	 */
	private String itemsId;
	/**
	 * 参数名称
	 */
	private String argsName;
	/**
	 * 最小值
	 */
	private String maxValue;
	/**
	 * 最大值
	 */
	private String minValue;
	/**
	 * 参数值
	 */
	private String argsValue;
	/**
	 * 参数单位
	 */
	private String argsUnit;
	/**
	 * 参数类型
	 */
	private String argsType;
	/**
	 * 参数类型名稱
	 */
	private String argsTypeName;
	/**
	 * 是否必填
	 */
	private String isRequired;
	/**
	 * 参数描述
	 */
	private String argsDescription;
	/**
	 * 排序
	 */
	private String sort;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemsId() {
		return itemsId;
	}

	public void setItemsId(String itemsId) {
		this.itemsId = itemsId;
	}

	public String getArgsName() {
		return argsName;
	}

	public void setArgsName(String argsName) {
		this.argsName = argsName;
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

	public String getArgsValue() {
		return argsValue;
	}

	public void setArgsValue(String argsValue) {
		this.argsValue = argsValue;
	}

	public String getArgsUnit() {
		return argsUnit;
	}

	public void setArgsUnit(String argsUnit) {
		this.argsUnit = argsUnit;
	}

	public String getArgsType() {
		return argsType;
	}

	public void setArgsType(String argsType) {
		this.argsType = argsType;
	}

	public String getArgsTypeName() {
		return argsTypeName;
	}

	public void setArgsTypeName(String argsTypeName) {
		this.argsTypeName = argsTypeName;
	}

	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	public String getArgsDescription() {
		return argsDescription;
	}

	public void setArgsDescription(String argsDescription) {
		this.argsDescription = argsDescription;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
