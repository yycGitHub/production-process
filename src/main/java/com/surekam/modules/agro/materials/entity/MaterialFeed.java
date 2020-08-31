package com.surekam.modules.agro.materials.entity;

import java.util.List;

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
import com.surekam.modules.api.dto.resp.ProductLibraryTreeListResp;

/**
 * 饲料表Entity
 * 
 * @author tangjun
 * @version 2019-04-22
 */
@Entity
@Table(name = "t_agro_material_feed")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MaterialFeed extends XGXTEntity<MaterialFeed> {

	private static final long serialVersionUID = 1L;
	private String id;//
	private String officeId;// 公司id
	private String pesticideName;// 农药名称
	private String registrationNumber;// 登记号
	private String specifications;// 规格
	private String manufacturer;// 制造商
	private String valid;// 有效期
	private String compositionAndContent;// 成分和含量
	private String feedingAmount;// 饲喂量
	private String applicationStage;// 适用阶段
	private String expectedWeight;// 预期体重
	private String effect;// 作用
	private String remarks;// 备注信息
	private String officeName;// 公司名称
	private List<ProductLibraryTreeListResp> pltResp; // 种类对象

	public MaterialFeed() {
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

	public String getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getPesticideName() {
		return this.pesticideName;
	}

	public void setPesticideName(String pesticideName) {
		this.pesticideName = pesticideName;
	}

	public String getRegistrationNumber() {
		return this.registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getSpecifications() {
		return this.specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getValid() {
		return this.valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getCompositionAndContent() {
		return this.compositionAndContent;
	}

	public void setCompositionAndContent(String compositionAndContent) {
		this.compositionAndContent = compositionAndContent;
	}

	public String getFeedingAmount() {
		return this.feedingAmount;
	}

	public void setFeedingAmount(String feedingAmount) {
		this.feedingAmount = feedingAmount;
	}

	public String getApplicationStage() {
		return this.applicationStage;
	}

	public void setApplicationStage(String applicationStage) {
		this.applicationStage = applicationStage;
	}

	public String getExpectedWeight() {
		return this.expectedWeight;
	}

	public void setExpectedWeight(String expectedWeight) {
		this.expectedWeight = expectedWeight;
	}

	public String getEffect() {
		return this.effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Transient
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	@Transient
	public List<ProductLibraryTreeListResp> getPltResp() {
		return pltResp;
	}

	public void setPltResp(List<ProductLibraryTreeListResp> pltResp) {
		this.pltResp = pltResp;
	}

}
