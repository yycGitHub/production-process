package com.surekam.modules.agro.productionmodel.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 生产模式Entity
 * 
 * @author tangjun
 * @version 2019-05-27
 */
@Entity
@Table(name = "t_agro_production_model")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductionModel extends XGXTEntity<ProductionModel> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String productId;// 品种ID
	private String productionName;// 生产名称
	private String productionCode;// 生产编码
	private String productionModelInfo;// 生产模式信息

	public ProductionModel() {
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

	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductionName() {
		return this.productionName;
	}

	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}

	public String getProductionCode() {
		return this.productionCode;
	}

	public void setProductionCode(String productionCode) {
		this.productionCode = productionCode;
	}

	public String getProductionModelInfo() {
		return this.productionModelInfo;
	}

	public void setProductionModelInfo(String productionModelInfo) {
		this.productionModelInfo = productionModelInfo;
	}

}
