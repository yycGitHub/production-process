package com.surekam.modules.agro.productbatchtask.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 批次对应的详细计划Entity
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Entity
@Table(name = "t_agro_product_batch_task")
@DynamicInsert
@DynamicUpdate
public class ProductBatchTask extends XGXTEntity<ProductBatchTask> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String regionType;// 任务对象类型，1代表基地 ，2代表批次，3代表人员
	private String regionId;// 任务对象ID
	private String regionName;// 任务对象名称
	private String standardItemId;// 标准库ID
	private String standardItemName;// 批次标准作业项名称
	private String standardItemCategoryId;// 批次标准作业项类别
	private String growthCycleId;// 所属生长周期阶段ID
	private String growthCycleName;// 所属生长周期阶段名称
	private String startDateNumber;// 作业项起始日
	private String endDateNumber;// 作业项结束日
	private Date startDate;// 作业项开始日期
	private Date endDate;// 作业项结束日期
	private String workEstimation;// 工作量预估
	private String standardsItemDescription;// 作业项描述
	private String isProductDemands;// 是否为生产资料需求
	private String singleOperationIntervalDay;// 间隔天数
	private String frequencyDuringTime;// 时间段完成频次
	private String needToDoTimes;// 需要完成的次数
	private String finishedTimes;// 时间段内已完成次数

	private String officeId;// 公司ID

	public static String TASK_BASE = "1";
	public static String TASK_BATCH = "2";
	public static String TASK_PEOPLE = "3";

	public ProductBatchTask() {
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

	public String getRegionType() {
		return regionType;
	}

	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getStandardItemId() {
		return this.standardItemId;
	}

	public void setStandardItemId(String standardItemId) {
		this.standardItemId = standardItemId;
	}

	public String getStandardItemName() {
		return this.standardItemName;
	}

	public void setStandardItemName(String standardItemName) {
		this.standardItemName = standardItemName;
	}

	public String getStandardItemCategoryId() {
		return this.standardItemCategoryId;
	}

	public void setStandardItemCategoryId(String standardItemCategoryId) {
		this.standardItemCategoryId = standardItemCategoryId;
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

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public String getFrequencyDuringTime() {
		return this.frequencyDuringTime;
	}

	public void setFrequencyDuringTime(String frequencyDuringTime) {
		this.frequencyDuringTime = frequencyDuringTime;
	}

	public String getFinishedTimes() {
		return this.finishedTimes;
	}

	public void setFinishedTimes(String finishedTimes) {
		this.finishedTimes = finishedTimes;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getSingleOperationIntervalDay() {
		return singleOperationIntervalDay;
	}

	public void setSingleOperationIntervalDay(String singleOperationIntervalDay) {
		this.singleOperationIntervalDay = singleOperationIntervalDay;
	}

	public String getNeedToDoTimes() {
		return needToDoTimes;
	}

	public void setNeedToDoTimes(String needToDoTimes) {
		this.needToDoTimes = needToDoTimes;
	}
}
