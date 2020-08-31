package com.surekam.modules.agro.productionbatch.entity;

import com.surekam.common.persistence.XGXTEntity;

/**
 * 批次
 * 
 * @author luoxw
 * @version 2019-07-03
 */
public class ProductionBatchModel extends XGXTEntity<ProductionBatchModel> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String mainProductionPlanId;// 主生产计划ID
	private String productId;// 品种ID-非数据库字段
	private String standardId;// 标准库ID-非数据库字段
	private String baseId;// 基地ID-非数据库字段
	private String functionalAreaId;// 功能区ID
	private String areaId;// 区域ID
	private String batchCode;// 批次编号
	private String batchStartDate;// 批次开始日期
	private String batchEndDate;// 批次完成日期
	private String productType;// 种养殖类型
	private String userId; // 农户ID
	private String userName;// 农户姓名
	private String baseName;// 基地名称
	private String productName;// 品种名称
	private String standardName;// 标准库名称
	private String batchPlanHarvestDate;// 预计采收日期
	private String growthCycleId;// 所属生长周期阶段ID
	private String growthCycleName;// 所属生长周期阶段名称
	private String status; // 各阶段的状态（999-完成）
	private String batchUnit; // 批次单位
	private String remark; // 备注
	private String harvestStatus;// 采收状态0-不需要采收确认 1-需采收确认
	private String advanceDays; // 提前天数
	private String sendTime; // 发送时间
	private String mailStatus; // 邮件状态 1 未开通 2 开通
	private String smsStatus; // 短信状态 1 未开通 2 开通
	private String gatewayId; // 网关号
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMainProductionPlanId() {
		return mainProductionPlanId;
	}
	public void setMainProductionPlanId(String mainProductionPlanId) {
		this.mainProductionPlanId = mainProductionPlanId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getStandardId() {
		return standardId;
	}
	public void setStandardId(String standardId) {
		this.standardId = standardId;
	}
	public String getBaseId() {
		return baseId;
	}
	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	public String getFunctionalAreaId() {
		return functionalAreaId;
	}
	public void setFunctionalAreaId(String functionalAreaId) {
		this.functionalAreaId = functionalAreaId;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getBatchCode() {
		return batchCode;
	}
	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}
	public String getBatchStartDate() {
		return batchStartDate;
	}
	public void setBatchStartDate(String batchStartDate) {
		this.batchStartDate = batchStartDate;
	}
	public String getBatchEndDate() {
		return batchEndDate;
	}
	public void setBatchEndDate(String batchEndDate) {
		this.batchEndDate = batchEndDate;
	}
	public String getProductType() {
		return productType;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBaseName() {
		return baseName;
	}
	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getStandardName() {
		return standardName;
	}
	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}
	public String getBatchPlanHarvestDate() {
		return batchPlanHarvestDate;
	}
	public void setBatchPlanHarvestDate(String batchPlanHarvestDate) {
		this.batchPlanHarvestDate = batchPlanHarvestDate;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBatchUnit() {
		return batchUnit;
	}
	public void setBatchUnit(String batchUnit) {
		this.batchUnit = batchUnit;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getGatewayId() {
		return gatewayId;
	}
	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}
	
}
