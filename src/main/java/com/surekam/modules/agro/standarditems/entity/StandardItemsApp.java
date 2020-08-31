package com.surekam.modules.agro.standarditems.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import com.surekam.modules.agro.standarditemargs.entity.StandardItemArgs;

/**
 * 标准作业项表Entity
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Entity
@Table(name = "t_agro_standard_items")
@DynamicInsert
@DynamicUpdate
public class StandardItemsApp extends XGXTEntity<StandardItemsApp> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
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
	private List<StandardItemArgs> standardItemArgsList = new ArrayList<StandardItemArgs>();
	private String taskId;// 任务ID
	private String batchId;// 批次ID
	private String sort;// 排序
	private String operationType;// 作业类型  1：目录 2：表单
	private String iconUrl; // 图标URL

	public StandardItemsApp() {
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

	public String getSystemStandardsId() {
		return this.systemStandardsId;
	}

	public void setSystemStandardsId(String systemStandardsId) {
		this.systemStandardsId = systemStandardsId;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCategoryId() {
		return this.itemCategoryId;
	}

	public void setItemCategoryId(String itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}

	public String getGrowthCycleId() {
		return this.growthCycleId;
	}

	public void setGrowthCycleId(String growthCycleId) {
		this.growthCycleId = growthCycleId;
	}

	public String getGrowthCycleName() {
		return this.growthCycleName;
	}

	public void setGrowthCycleName(String growthCycleName) {
		this.growthCycleName = growthCycleName;
	}

	public String getStartDateNumber() {
		return this.startDateNumber;
	}

	public void setStartDateNumber(String startDateNumber) {
		this.startDateNumber = startDateNumber;
	}

	public String getEndDateNumber() {
		return this.endDateNumber;
	}

	public void setEndDateNumber(String endDateNumber) {
		this.endDateNumber = endDateNumber;
	}

	public String getWorkEstimation() {
		return this.workEstimation;
	}

	public void setWorkEstimation(String workEstimation) {
		this.workEstimation = workEstimation;
	}

	public String getStandardsItemDescription() {
		return this.standardsItemDescription;
	}

	public void setStandardsItemDescription(String standardsItemDescription) {
		this.standardsItemDescription = standardsItemDescription;
	}

	public String getIsProductDemands() {
		return this.isProductDemands;
	}

	public void setIsProductDemands(String isProductDemands) {
		this.isProductDemands = isProductDemands;
	}

	public String getSingleOperationIntervalDay() {
		return this.singleOperationIntervalDay;
	}

	public void setSingleOperationIntervalDay(String singleOperationIntervalDay) {
		this.singleOperationIntervalDay = singleOperationIntervalDay;
	}

	public String getFrequencyDuringTime() {
		return this.frequencyDuringTime;
	}

	public void setFrequencyDuringTime(String frequencyDuringTime) {
		this.frequencyDuringTime = frequencyDuringTime;
	}

	public String getSort() {
		return this.sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Transient
	public List<StandardItemArgs> getStandardItemArgsList() {
		return standardItemArgsList;
	}

	public void setStandardItemArgsList(List<StandardItemArgs> standardItemArgsList) {
		this.standardItemArgsList = standardItemArgsList;
	}

	@Transient
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Transient
	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
}
