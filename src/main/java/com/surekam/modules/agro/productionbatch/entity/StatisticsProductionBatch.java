package com.surekam.modules.agro.productionbatch.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 批次管理Entity
 * 
 * @author tangjun
 * @version 2019-04-15
 */
@Entity
@Table(name = "t_agro_production_batch")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StatisticsProductionBatch extends XGXTEntity<StatisticsProductionBatch> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String mainProductionPlanId;// 主生产计划ID
	private String productId;// 品种ID
	private String productName;// 品种名称
	private String standardId;// 标准库ID
	private String standardName;// 标准库名称
	private String functionalAreaId;// 功能区ID
	private String areaId;// 区域ID
	private String batchCode;// 批次编号
	private String batchStartDate;// 批次开始日期
	private String productType;// 种养殖类型
	private String userId; // 农户ID
	private String userName;// 农户姓名
	private String baseId;// 基地ID
	private String name;// 基地名称
	private String batchPlanHarvestDate;// 预计采收日期
	private String categoryId; // 种类ID
	private String productImg;// 品种图片
	private String growthCycleId;// 所属生长周期阶段ID
	private String growthCycleName;// 所属生长周期阶段名称
	private String status; // 各阶段的状态（999-完成）
	private String officeId;// 公司ID
	private String officeName; // 公司名称
	private String roleName;// 角色
	private String acreage; // 面积
	private String isRecovery; // 是否采收 1 已采收 2： 未采收
	private String recoveryAmount; // 采收量
	private String batchNumber; // 批次数量
	private String batchUnit; // 批次单位
	private String batchNumberAndUnit;
	private String harvestStatus;// 采收状态0-不需要采收确认 1-需采收确认
	private String advanceDays; // 提前天数
	private String sendTime; // 发送时间
	private String mailStatus; // 邮件状态 1 未开通 2 开通
	private String smsStatus; // 短信状态 1 未开通 2 开通

	public StatisticsProductionBatch() {
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

	public String getMainProductionPlanId() {
		return this.mainProductionPlanId;
	}

	public void setMainProductionPlanId(String mainProductionPlanId) {
		this.mainProductionPlanId = mainProductionPlanId;
	}

	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getStandardId() {
		return this.standardId;
	}

	public void setStandardId(String standardId) {
		this.standardId = standardId;
	}

	public String getBaseId() {
		return this.baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}

	public String getFunctionalAreaId() {
		return this.functionalAreaId;
	}

	public void setFunctionalAreaId(String functionalAreaId) {
		this.functionalAreaId = functionalAreaId;
	}

	public String getAreaId() {
		return this.areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getBatchCode() {
		return this.batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Transient
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Transient
	public String getStandardName() {
		return standardName;
	}

	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}

	public String getBatchStartDate() {
		return batchStartDate;
	}

	public void setBatchStartDate(String batchStartDate) {
		this.batchStartDate = batchStartDate;
	}

	public String getBatchPlanHarvestDate() {
		return batchPlanHarvestDate;
	}

	public void setBatchPlanHarvestDate(String batchPlanHarvestDate) {
		this.batchPlanHarvestDate = batchPlanHarvestDate;
	}

	@Transient
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	@Transient
	public String getProductImg() {
		return productImg;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
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

	@Transient
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	@Transient
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getAcreage() {
		return acreage;
	}

	public void setAcreage(String acreage) {
		this.acreage = acreage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	@Transient
	public String getIsRecovery() {
		return isRecovery;
	}

	public void setIsRecovery(String isRecovery) {
		this.isRecovery = isRecovery;
	}

	@Transient
	public String getRecoveryAmount() {
		return recoveryAmount;
	}

	public void setRecoveryAmount(String recoveryAmount) {
		this.recoveryAmount = recoveryAmount;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getBatchUnit() {
		return batchUnit;
	}

	public void setBatchUnit(String batchUnit) {
		this.batchUnit = batchUnit;
	}

	@Transient
	public String getBatchNumberAndUnit() {
		return batchNumberAndUnit;
	}

	public void setBatchNumberAndUnit(String batchNumberAndUnit) {
		this.batchNumberAndUnit = batchNumberAndUnit;
	}

	public String getHarvestStatus() {
		return harvestStatus;
	}

	public void setHarvestStatus(String harvestStatus) {
		this.harvestStatus = harvestStatus;
	}

	public String getAdvanceDays() {
		return advanceDays;
	}

	public void setAdvanceDays(String advanceDays) {
		this.advanceDays = advanceDays;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getMailStatus() {
		return mailStatus;
	}

	public void setMailStatus(String mailStatus) {
		this.mailStatus = mailStatus;
	}

	public String getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}

}
