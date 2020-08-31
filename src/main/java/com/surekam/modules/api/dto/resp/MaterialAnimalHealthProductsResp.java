package com.surekam.modules.api.dto.resp;

import java.io.Serializable;
import java.util.List;

public class MaterialAnimalHealthProductsResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主鍵
	 */
	private String id;
	/**
	 * 公司id
	 */
	private String officeId;
	/**
	 * 农药名称
	 */
	private String pesticideName;
	/**
	 * 登记号
	 */
	private String registrationNumber;
	/**
	 * 规格
	 */
	private String specifications;
	/**
	 * 制造商
	 */
	private String manufacturer;
	/**
	 * 有效期
	 */
	private String valid;
	/**
	 * 成分和含量
	 */
	private String compositionAndContent;
	/**
	 * 用法用量
	 */
	private String usageAndDosage;
	/**
	 * 储存
	 */
	private String storage;
	/**
	 * 不良反应
	 */
	private String sideEffects;
	/**
	 * 休药期
	 */
	private String withdrawalPeriod;
	/**
	 * 作用
	 */
	private String effect;
	/**
	 * 备注信息
	 */
	private String remarks;
	/**
	 * 种类表信息
	 */
	private List<ProductLibraryTreeListResp> pltResp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getPesticideName() {
		return pesticideName;
	}

	public void setPesticideName(String pesticideName) {
		this.pesticideName = pesticideName;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getCompositionAndContent() {
		return compositionAndContent;
	}

	public void setCompositionAndContent(String compositionAndContent) {
		this.compositionAndContent = compositionAndContent;
	}

	public String getUsageAndDosage() {
		return usageAndDosage;
	}

	public void setUsageAndDosage(String usageAndDosage) {
		this.usageAndDosage = usageAndDosage;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getSideEffects() {
		return sideEffects;
	}

	public void setSideEffects(String sideEffects) {
		this.sideEffects = sideEffects;
	}

	public String getWithdrawalPeriod() {
		return withdrawalPeriod;
	}

	public void setWithdrawalPeriod(String withdrawalPeriod) {
		this.withdrawalPeriod = withdrawalPeriod;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<ProductLibraryTreeListResp> getPltResp() {
		return pltResp;
	}

	public void setPltResp(List<ProductLibraryTreeListResp> pltResp) {
		this.pltResp = pltResp;
	}

}
