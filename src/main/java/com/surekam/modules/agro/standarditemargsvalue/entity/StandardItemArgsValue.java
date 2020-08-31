package com.surekam.modules.agro.standarditemargsvalue.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 标准作业参数多项值表Entity
 * @author liwei
 * @version 2019-04-26
 */
@Entity
@Table(name = "t_agro_standard_item_args_value")
@DynamicInsert 
@DynamicUpdate
public class StandardItemArgsValue extends XGXTEntity<StandardItemArgsValue> {
	
	private static final long serialVersionUID = 1L;
	private String id;//
	private String itemArgsId;//标准参数ID
	private String name;//标准参数的值的展示内容
	private String value;//标准参数的值
	private String sort;//排序

	public StandardItemArgsValue() {
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
	
	public String getItemArgsId() {
		return this.itemArgsId;
	}
	public void setItemArgsId(String itemArgsId) {
		this.itemArgsId = itemArgsId;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getSort() {
		return this.sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
}


