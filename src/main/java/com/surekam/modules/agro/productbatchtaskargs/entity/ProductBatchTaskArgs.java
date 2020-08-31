package com.surekam.modules.agro.productbatchtaskargs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

import java.lang.String;

/**
 * 批次标准作业详细参数表Entity
 * @author liwei
 * @version 2019-04-25
 */
@Entity
@Table(name = "t_agro_product_batch_task_args")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductBatchTaskArgs extends XGXTEntity<ProductBatchTaskArgs> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String batchTaskId;//批次标准作业项ID
	private String argsName;//参数名称
	private String maxValue;//最小值
	private String minValue;//最大值
	private String argsValue;//参数值
	private String argsUnit;//参数单位
	private String argsType;//参数类型
	private String isRequired;//是否必填
	private String argsDescription;//参数描述

	public ProductBatchTaskArgs() {
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
	
	public String getBatchTaskId() {
		return this.batchTaskId;
	}
	public void setBatchTaskId(String batchTaskId) {
		this.batchTaskId = batchTaskId;
	}
	
	public String getArgsName() {
		return this.argsName;
	}
	public void setArgsName(String argsName) {
		this.argsName = argsName;
	}
	
	public String getMaxValue() {
		return this.maxValue;
	}
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	
	public String getMinValue() {
		return this.minValue;
	}
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}
	
	public String getArgsValue() {
		return this.argsValue;
	}
	public void setArgsValue(String argsValue) {
		this.argsValue = argsValue;
	}
	
	public String getArgsUnit() {
		return this.argsUnit;
	}
	public void setArgsUnit(String argsUnit) {
		this.argsUnit = argsUnit;
	}
	
	public String getArgsType() {
		return this.argsType;
	}
	public void setArgsType(String argsType) {
		this.argsType = argsType;
	}
	
	public String getIsRequired() {
		return this.isRequired;
	}
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}
	
	public String getArgsDescription() {
		return this.argsDescription;
	}
	public void setArgsDescription(String argsDescription) {
		this.argsDescription = argsDescription;
	}
	
}


