package com.surekam.modules.api.dto.req;

import java.io.Serializable;
import java.util.List;

public class MaterialPesticideReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
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
	 * 稀释倍数
	 */
	private String dilution;
	/**
	 * 使用范围
	 */
	private String scopeOfUse;
	/**
	 * 使用方法
	 */
	private String usageMethod;
	/**
	 * 储存
	 */
	private String storage;
	/**
	 * 作用
	 */
	private String effect;
	/**
	 * 备注信息
	 */
	private String remarks;

	/**
	 * 农产品类型id关联的是种养殖种类树形表)
	 */
	private List<ProductLibraryTreeListReq> pltReq;

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

	public String getDilution() {
		return dilution;
	}

	public void setDilution(String dilution) {
		this.dilution = dilution;
	}

	public String getScopeOfUse() {
		return scopeOfUse;
	}

	public void setScopeOfUse(String scopeOfUse) {
		this.scopeOfUse = scopeOfUse;
	}

	public String getUsageMethod() {
		return usageMethod;
	}

	public void setUsageMethod(String usageMethod) {
		this.usageMethod = usageMethod;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
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

	public List<ProductLibraryTreeListReq> getPltReq() {
		return pltReq;
	}

	public void setPltReq(List<ProductLibraryTreeListReq> pltReq) {
		this.pltReq = pltReq;
	}

}
