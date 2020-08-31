package com.surekam.modules.agro.productgrowthcycle.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 生长周期阶段表Entity
 * 
 * @author tangjun
 * @version 2019-04-26
 */
@Entity
@Table(name = "t_agro_product_growth_cycle")
@DynamicInsert
@DynamicUpdate
public class ProductGrowthCycle extends XGXTEntity<ProductGrowthCycle> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String sysEntStandardId;// 品种主键
	private String cycleName;// 生长周期名称
	private String beginDay;// 开始天数
	private String endDay;// 结束天数

	public ProductGrowthCycle() {
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

	public String getSysEntStandardId() {
		return sysEntStandardId;
	}

	public void setSysEntStandardId(String sysEntStandardId) {
		this.sysEntStandardId = sysEntStandardId;
	}

	public String getCycleName() {
		return this.cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getBeginDay() {
		return this.beginDay;
	}

	public void setBeginDay(String beginDay) {
		this.beginDay = beginDay;
	}

	public String getEndDay() {
		return this.endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

}
