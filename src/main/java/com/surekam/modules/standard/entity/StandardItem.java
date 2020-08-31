package com.surekam.modules.standard.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.surekam.common.persistence.DataEntity;
import com.surekam.common.utils.DateUtils;

@Entity
@Table(name = "sys_standard_item")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StandardItem extends DataEntity<StandardItem> {

	private static final long serialVersionUID = 1L;
	private String targetId;
	private String name; 	// 名称
	private String keyName; //字段名称
	private Integer sort; 	// 排序
	private String itemType;  //类型
	private String minValue;//最小值
	private String maxValue;//最大值
	private String itemUnit;//单位
	private String itemLimitType;
	
	private Boolean isRequired;//0,非必填;1,必填
	
	private String synTime = DateUtils.getDateTime();//同步时间
	private List<StandardItemValue> standardItemValues = Lists.newArrayList();
	
	@OneToMany(mappedBy = "standardItem", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="id")
	@Fetch(FetchMode.SUBSELECT)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<StandardItemValue> getStandardItemValues() {
		return standardItemValues;
	}

	public void setStandardItemValues(List<StandardItemValue> standardItemValues) {
		this.standardItemValues = standardItemValues;
	}

	public String getSynTime() {
		return synTime;
	}

	public void setSynTime(String synTime) {
		this.synTime = synTime;
	}
	
	public StandardItem(){
		super();
	}
	
	public StandardItem(String id){
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
	
	@NotNull
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getItemLimitType() {
		return itemLimitType;
	}

	public void setItemLimitType(String itemLimitType) {
		this.itemLimitType = itemLimitType;
	}

	public Boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}
}