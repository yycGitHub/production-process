package com.surekam.modules.agro.standarditems.entity.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.surekam.common.persistence.XGXTEntity;

/**
 * 标准作业项表Entity
 * 
 * @author liwei
 * @version 2019-04-25
 */

public class CopyStandardItemsVo extends XGXTEntity<CopyStandardItemsVo> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String parentId; // 父节点ID
	private String parentIds;// 父节点IDS,用逗号隔开
	private String systemStandardsId;// 标准库ID
	private String itemName;// 作业项名称
	private String itemCategoryId;// 作业项类别
	private String growthCycleId;// 所属生长周期阶段ID
	private String growthCycleName;// 所属生长周期阶段名称
	private String startDateNumber;// 作业项起始日
	private String endDateNumber;// 作业项结束日
	private String workEstimation;// 工作量预估
	private String standardsItemDescription;// 作业项描述
	private String isProductDemands;// 是否为生产资料需求
	private String singleOperationIntervalDay;// 单次作业间隔天数
	private String frequencyDuringTime;// 时间段内完成频次
	private String taskId;// 任务ID
	private String batchId;// 批次ID
	private String sort;// 排序
	private String operationType;// 作业类型 1：目录 2：表单

	private List<CopyStandardItemsVo> childList = Lists.newArrayList();// 拥有子机构列表

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

	public List<CopyStandardItemsVo> getChildList() {
		return childList;
	}

	public void setChildList(List<CopyStandardItemsVo> childList) {
		if (childList != null && childList.size() > 0) {
			for (CopyStandardItemsVo vo : childList) {
				CopyStandardItemsVo agroProductLibraryTreeVo = new CopyStandardItemsVo();
				BeanUtils.copyProperties(vo, agroProductLibraryTreeVo);
				this.childList.add(agroProductLibraryTreeVo);
			}
		} else {
			this.childList = new ArrayList<CopyStandardItemsVo>();
		}
		this.childList = childList;
	}

}
