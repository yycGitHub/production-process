package com.surekam.modules.api.dto.req;

import java.io.Serializable;

/**
 * 新增或者修改批次信息请求参数
 * 
 * @author dell
 *
 */
public class AgroProductionBatchReq implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 主生产计划ID
	 */
	private String mainProductionPlanId;
	/**
	 * 品种ID
	 */
	private String productId;
	/**
	 * 标准库ID
	 */
	private String standardId;
	/**
	 * 基地ID
	 */
	private String baseId;
	/**
	 * 功能区ID
	 */
	private String functionalAreaId;
	/**
	 * 区域ID
	 */
	private String areaId;
	/**
	 * 批次编号
	 */
	private String batchCode;
	/**
	 * 批次开始日期
	 */
	private String batchStartDate;

	/**
	 * 预计采收日期
	 */
	private String batchPlanHarvestDate;

	/**
	 * 种养殖类型
	 */
	private String productType;
	/**
	 * 农户ID
	 */
	private String userId;
	/**
	 * 各阶段的状态
	 */
	private String status;
	/**
	 * 批次数量
	 */
	private String batchNumber;
	/**
	 * 批次单位
	 */
	private String batchUnit;
	/**
	 * 采收状态0-不需要采收确认 1-需采收确认
	 */
	private String harvestStatus;
	/**
	 * 提前天数
	 */
	private String advanceDays;
	/**
	 * 发送时间
	 */
	private String sendTime;
	/**
	 * 邮件状态
	 */
	private String mailStatus;
	/**
	 * 短信状态
	 */
	private String smsStatus;
	/**
	 * 分类ID
	 */
	private String classificationId;

	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 电话
	 */
	private String phone;
	private String confirmationInformationPath; // 认定信息图片路径（,号分割多张）
	private String confirmationInformationName; // 认定信息图片名称（,号分割多张）
	private String qualityTestPath; // 质检认证图片路径（,号分割多张）
	private String qualityTestName; // 质检认证图片名称（,号分割多张）
	private String batchPlanId; // 批次计划id

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

	public String getBatchPlanHarvestDate() {
		return batchPlanHarvestDate;
	}

	public void setBatchPlanHarvestDate(String batchPlanHarvestDate) {
		this.batchPlanHarvestDate = batchPlanHarvestDate;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(String classificationId) {
		this.classificationId = classificationId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getConfirmationInformationPath() {
		return confirmationInformationPath;
	}

	public void setConfirmationInformationPath(String confirmationInformationPath) {
		this.confirmationInformationPath = confirmationInformationPath;
	}

	public String getConfirmationInformationName() {
		return confirmationInformationName;
	}

	public void setConfirmationInformationName(String confirmationInformationName) {
		this.confirmationInformationName = confirmationInformationName;
	}

	public String getQualityTestPath() {
		return qualityTestPath;
	}

	public void setQualityTestPath(String qualityTestPath) {
		this.qualityTestPath = qualityTestPath;
	}

	public String getQualityTestName() {
		return qualityTestName;
	}

	public void setQualityTestName(String qualityTestName) {
		this.qualityTestName = qualityTestName;
	}

	public String getBatchPlanId() {
		return batchPlanId;
	}

	public void setBatchPlanId(String batchPlanId) {
		this.batchPlanId = batchPlanId;
	}
	
	

}
