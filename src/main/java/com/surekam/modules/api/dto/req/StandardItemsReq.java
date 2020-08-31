/**
 * 
 */
package com.surekam.modules.api.dto.req;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Title: StandardItemsReq Description: 作业项请求参数
 * 
 * @author tangjun
 * @date 2019年4月30日
 */
public class StandardItemsReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 父节点ID
	 */
	private String parentId;
	/**
	 * 父节点IDS,用逗号隔开
	 */
	private String parentIds;
	/**
	 * 标准库ID
	 */
	private String systemStandardsId;
	/**
	 * 作业项名称
	 */
	private String itemName;
	/**
	 * 作业项类别
	 */
	private String itemCategoryId;
	/**
	 * 所属生长周期阶段ID
	 */
	private String growthCycleId;
	/**
	 * 所属生长周期阶段名称
	 */
	private String growthCycleName;
	/**
	 * 作业项起始日
	 */
	private String startDateNumber;
	/**
	 * 作业项结束日
	 */
	private String endDateNumber;
	/**
	 * 工作量预估
	 */
	private String workEstimation;
	/**
	 * 作业项描述
	 */
	private String standardsItemDescription;
	/**
	 * 是否为生产资料需求
	 */
	private String isProductDemands;
	/**
	 * 单次作业间隔天数
	 */
	private String singleOperationIntervalDay;
	/**
	 * 时间段内完成频次
	 */
	private String frequencyDuringTime;
	/**
	 * 任务ID
	 */
	private String taskId;
	/**
	 * 批次ID
	 */
	private String batchId;
	/**
	 * 排序
	 */
	private String sort;
	/**
	 * 作业类型 1：目录 2：表单
	 */
	private String operationType;
	/**
	 * 图标URL
	 */
	private String iconUrl;
	/**
	 * 页面默认参数
	 */
	private List<String> parameterList = new ArrayList<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getSystemStandardsId() {
		return systemStandardsId;
	}

	public void setSystemStandardsId(String systemStandardsId) {
		this.systemStandardsId = systemStandardsId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCategoryId() {
		return itemCategoryId;
	}

	public void setItemCategoryId(String itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}

	public String getGrowthCycleId() {
		return growthCycleId;
	}

	public void setGrowthCycleId(String growthCycleId) {
		this.growthCycleId = growthCycleId;
	}

	public String getGrowthCycleName() {
		return growthCycleName;
	}

	public void setGrowthCycleName(String growthCycleName) {
		this.growthCycleName = growthCycleName;
	}

	public String getStartDateNumber() {
		return startDateNumber;
	}

	public void setStartDateNumber(String startDateNumber) {
		this.startDateNumber = startDateNumber;
	}

	public String getEndDateNumber() {
		return endDateNumber;
	}

	public void setEndDateNumber(String endDateNumber) {
		this.endDateNumber = endDateNumber;
	}

	public String getWorkEstimation() {
		return workEstimation;
	}

	public void setWorkEstimation(String workEstimation) {
		this.workEstimation = workEstimation;
	}

	public String getStandardsItemDescription() {
		return standardsItemDescription;
	}

	public void setStandardsItemDescription(String standardsItemDescription) {
		this.standardsItemDescription = standardsItemDescription;
	}

	public String getIsProductDemands() {
		return isProductDemands;
	}

	public void setIsProductDemands(String isProductDemands) {
		this.isProductDemands = isProductDemands;
	}

	public String getSingleOperationIntervalDay() {
		return singleOperationIntervalDay;
	}

	public void setSingleOperationIntervalDay(String singleOperationIntervalDay) {
		this.singleOperationIntervalDay = singleOperationIntervalDay;
	}

	public String getFrequencyDuringTime() {
		return frequencyDuringTime;
	}

	public void setFrequencyDuringTime(String frequencyDuringTime) {
		this.frequencyDuringTime = frequencyDuringTime;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public List<String> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<String> parameterList) {
		this.parameterList = parameterList;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

}
