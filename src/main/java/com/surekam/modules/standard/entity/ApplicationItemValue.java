package com.surekam.modules.standard.entity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.DataEntity;
import com.surekam.common.utils.DateUtils;

@Entity
@Table(name = "sys_standard_app_item_value")
@DynamicInsert 
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationItemValue extends DataEntity<ApplicationItemValue> {

	private static final long serialVersionUID = 1L;
	private ExpandPropertyList expandPropertyList;
	private String standardItemId;
	private String standardItemName;
	private String standardItemType;
	private Integer itemSort;
	private String itemValue;
	private String blobValue;
	private Map<String,String> blobValueJsonMap;

	
	private String synTime = DateUtils.getDateTime();//同步时间
	private String synState;//同步标志
	
	private String thumbnailValue;//缩略图
	
	@ManyToOne
	@JoinColumn(name="standard_user_app_list_id")
	@JsonIgnore
	public ExpandPropertyList getExpandPropertyList() {
		return expandPropertyList;
	}

	public void setExpandPropertyList(ExpandPropertyList expandPropertyList) {
		this.expandPropertyList = expandPropertyList;
	}

	public String getSynTime() {
		return synTime;
	}

	public void setSynTime(String synTime) {
		this.synTime = synTime;
	}
	
	public ApplicationItemValue(){
		super();
	}
	
	public ApplicationItemValue(String id){
		this();
		this.id = id;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getBlobValue() {
		return blobValue;
	}

	public void setBlobValue(String blobValue) {
		this.blobValue = blobValue;
	}

	public String getStandardItemId() {
		return standardItemId;
	}

	public void setStandardItemId(String standardItemId) {
		this.standardItemId = standardItemId;
	}

	public String getStandardItemName() {
		return standardItemName;
	}

	public void setStandardItemName(String standardItemName) {
		this.standardItemName = standardItemName;
	}

	public String getStandardItemType() {
		return standardItemType;
	}

	public void setStandardItemType(String standardItemType) {
		this.standardItemType = standardItemType;
	}

	public String getSynState() {
		return synState;
	}

	public void setSynState(String synState) {
		this.synState = synState;
	}

	public Integer getItemSort() {
		return itemSort;
	}

	public void setItemSort(Integer itemSort) {
		this.itemSort = itemSort;
	}

	@Transient
	public String getThumbnailValue() {
		thumbnailValue = this.itemValue.replace("/uploadfiles/", "/uploadfiles/thumbnail/");
		return thumbnailValue;
	}

	@SuppressWarnings("unchecked")
	@Transient
	public Map<String, String> getBlobValueJsonMap() {
		blobValueJsonMap = JsonMapper.nonDefaultMapper().fromJson(blobValue, Map.class);
		return blobValueJsonMap;
	}
}