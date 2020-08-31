package com.surekam.modules.agro.product.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.surekam.modules.api.dto.req.ProductionModelReq;
import com.surekam.modules.api.dto.resp.ProductionModelResp;

/**
 * 品种详情Entity
 * 
 * @author lb
 * @version 2019-04-10
 */
@Entity
@Table(name = "t_agro_product_library_detail")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductLibraryDetail extends XGXTEntity<ProductLibraryDetail> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String productLibraryId;// 种类ID
	private String productCode;// 品种编号
	private String productName;// 品种名称
	private Integer growthYears;// 生长年限
	private String species;// 种名
	private String family;// 科名
	private String genus;// 属名
	private String academicName;// 学名
	private String alias;// 别名
	private String englishName;// 英文名
	private String productImagUrl;// 图片
	private String originDistribution;// 产地及分布
	private String ecologicalHabits;// 生态习性
	private String morphologicalCharacter;// 形态特征
	private String description;// 综合描述
	private String plantingSuggestion;// 种养植建议
	private String enterpriseId;// 创建企业的主键

	private String parentId;
	private String parentName;
	private List<ProductionModelReq> pmReq = new ArrayList<ProductionModelReq>();
	private List<ProductionModelResp> pmResp = new ArrayList<ProductionModelResp>();
	private List<Map<String, String>> fieldTags = new ArrayList<Map<String, String>>();

	public ProductLibraryDetail() {
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

	public String getProductLibraryId() {
		return productLibraryId;
	}

	public void setProductLibraryId(String productLibraryId) {
		this.productLibraryId = productLibraryId;
	}

	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getGrowthYears() {
		return growthYears;
	}

	public void setGrowthYears(Integer growthYears) {
		this.growthYears = growthYears;
	}

	public String getSpecies() {
		return this.species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getFamily() {
		return this.family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getGenus() {
		return this.genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	public String getAcademicName() {
		return this.academicName;
	}

	public void setAcademicName(String academicName) {
		this.academicName = academicName;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getEnglishName() {
		return this.englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getProductImagUrl() {
		return this.productImagUrl;
	}

	public void setProductImagUrl(String productImagUrl) {
		this.productImagUrl = productImagUrl;
	}

	public String getOriginDistribution() {
		return this.originDistribution;
	}

	public void setOriginDistribution(String originDistribution) {
		this.originDistribution = originDistribution;
	}

	public String getEcologicalHabits() {
		return this.ecologicalHabits;
	}

	public void setEcologicalHabits(String ecologicalHabits) {
		this.ecologicalHabits = ecologicalHabits;
	}

	public String getMorphologicalCharacter() {
		return this.morphologicalCharacter;
	}

	public void setMorphologicalCharacter(String morphologicalCharacter) {
		this.morphologicalCharacter = morphologicalCharacter;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlantingSuggestion() {
		return this.plantingSuggestion;
	}

	public void setPlantingSuggestion(String plantingSuggestion) {
		this.plantingSuggestion = plantingSuggestion;
	}

	public String getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	@Transient
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Transient
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	@Transient
	public List<ProductionModelReq> getPmReq() {
		return pmReq;
	}

	public void setPmReq(List<ProductionModelReq> pmReq) {
		this.pmReq = pmReq;
	}

	@Transient
	public List<ProductionModelResp> getPmResp() {
		return pmResp;
	}

	public void setPmResp(List<ProductionModelResp> pmResp) {
		this.pmResp = pmResp;
	}

	@Transient
	public List<Map<String, String>> getFieldTags() {
		return fieldTags;
	}

	public void setFieldTags(List<Map<String, String>> fieldTags) {
		this.fieldTags = fieldTags;
	}

}
