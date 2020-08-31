package com.surekam.modules.agro.productlibraryquick.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 品种快捷值表用于向溯源推送Entity
 * @author luoxw
 * @version 2019-07-24
 */
@Entity
@Table(name = "t_agro_product_library_quick_value")
@DynamicInsert 
@DynamicUpdate
public class ProductLibraryQuickValue extends XGXTEntity<ProductLibraryQuickValue> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String productQuickId;//品种快捷ID
	private String productName;//品种名称
	private String qualityGuaranteePeriod;//保质期(天)
	private String productPropagandaImgUrl;//品种宣传图片
	private String productPropagandaDescription;//品种宣传描述
	public ProductLibraryQuickValue() {
		super();
	}
	
	@PrePersist
	public void prePersist(){
		this.id = IdGen.uuid();
	}
		
	@Id
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getProductQuickId() {
		return this.productQuickId;
	}
	public void setProductQuickId(String productQuickId) {
		this.productQuickId = productQuickId;
	}
	
	public String getProductName() {
		return this.productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getQualityGuaranteePeriod() {
		return this.qualityGuaranteePeriod;
	}
	public void setQualityGuaranteePeriod(String qualityGuaranteePeriod) {
		this.qualityGuaranteePeriod = qualityGuaranteePeriod;
	}
	
	public String getProductPropagandaImgUrl() {
		return this.productPropagandaImgUrl;
	}
	public void setProductPropagandaImgUrl(String productPropagandaImgUrl) {
		this.productPropagandaImgUrl = productPropagandaImgUrl;
	}
	
	public String getProductPropagandaDescription() {
		return this.productPropagandaDescription;
	}
	public void setProductPropagandaDescription(String productPropagandaDescription) {
		this.productPropagandaDescription = productPropagandaDescription;
	}
	
}


