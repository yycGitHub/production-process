package com.surekam.modules.api.dto.resp;

import java.io.Serializable;
import java.util.List;

public class MaterialFertilizerResp implements Serializable {

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
	 * 肥料名称
	 */
	private String fertilizerName; 
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
	 * 成分
	 */
	private String composition; 
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

	public String getFertilizerName() {
		return fertilizerName;
	}

	public void setFertilizerName(String fertilizerName) {
		this.fertilizerName = fertilizerName;
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

	public String getComposition() {
		return composition;
	}

	public void setComposition(String composition) {
		this.composition = composition;
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
