package com.surekam.modules.standard.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.surekam.common.persistence.DataEntity;

@Entity
@Table(name = "sys_standard_item_value")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StandardItemValue extends DataEntity<StandardItemValue> {

	private static final long serialVersionUID = 1L;
	private StandardItem standardItem;
	private String name; 	// 名称
	private String value;  //类型
	
	public StandardItemValue(){
		super();
	}
	
	public StandardItemValue(String id){
		this();
		this.id = id;
	}
	
	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne
	@JoinColumn(name="standard_item_id")
	@JsonIgnore
	public StandardItem getStandardItem() {
		return standardItem;
	}

	public void setStandardItem(StandardItem standardItem) {
		this.standardItem = standardItem;
	}
}