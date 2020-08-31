package com.surekam.modules.standard.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.google.common.collect.Lists;
import com.surekam.common.persistence.DataEntity;
import com.surekam.common.utils.DateUtils;

@Entity
@Table(name = "sys_expand_property_list")
@DynamicInsert 
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExpandPropertyList extends DataEntity<ExpandPropertyList> {

	private static final long serialVersionUID = 1L;
	private String fromTableId;
	private String fromTableName;
	private String mainStandardId;
	
	private String synTime = DateUtils.getDateTime();//同步时间
	private String synState;//同步标志
	
	private List<ApplicationItemValue> appItemValues = Lists.newArrayList(); // 拥有资源列表
	
	private List<ApplicationItemValue> appItemValuesOfPhoto = Lists.newArrayList(); // 拥有资源列表

	@OneToMany(mappedBy = "expandPropertyList", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="itemSort,id")
	@Fetch(FetchMode.SUBSELECT)
	public List<ApplicationItemValue> getAppItemValues() {
		return appItemValues;
	}

	public void setAppItemValues(List<ApplicationItemValue> appItemValues) {
		this.appItemValues = appItemValues;
	}

	public String getSynTime() {
		return synTime;
	}

	public void setSynTime(String synTime) {
		this.synTime = synTime;
	}
	
	public ExpandPropertyList(){
		super();
	}
	
	public ExpandPropertyList(String id){
		this();
		this.id = id;
	}

	public String getFromTableId() {
		return fromTableId;
	}

	public void setFromTableId(String fromTableId) {
		this.fromTableId = fromTableId;
	}

	public String getFromTableName() {
		return fromTableName;
	}

	public void setFromTableName(String fromTableName) {
		this.fromTableName = fromTableName;
	}

	public String getMainStandardId() {
		return mainStandardId;
	}

	public void setMainStandardId(String mainStandardId) {
		this.mainStandardId = mainStandardId;
	}

	public String getSynState() {
		return synState;
	}

	public void setSynState(String synState) {
		this.synState = synState;
	}

	@OneToMany(mappedBy = "expandPropertyList", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"' and standard_item_type in ('2', '6') and item_value != ''")
	@OrderBy(value="id")
	@Fetch(FetchMode.SUBSELECT)
	public List<ApplicationItemValue> getAppItemValuesOfPhoto() {
		return appItemValuesOfPhoto;
	}

	public void setAppItemValuesOfPhoto(
			List<ApplicationItemValue> appItemValuesOfPhoto) {
		this.appItemValuesOfPhoto = appItemValuesOfPhoto;
	}
}