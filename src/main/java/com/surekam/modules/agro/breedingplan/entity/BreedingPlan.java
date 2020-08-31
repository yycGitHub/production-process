package com.surekam.modules.agro.breedingplan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
import com.surekam.modules.sys.entity.Office;

import java.lang.String;

/**
 * 种植计划表Entity
 * @author luoxw
 * @version 2019-10-16
 */
@Entity
@Table(name = "t_agro_breeding_plan")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BreedingPlan extends XGXTEntity<BreedingPlan> {
	
	private static final long serialVersionUID = 1L;
	private String id;//种植计划ID
	private String officeId;//公司ID-非数据库字段
	private String baseId;//基地ID-非数据库字段
	private String officeName;//公司名字-非数据库字段
	private String baseName;//基地名字-非数据库字段
	private Office office;//公司
	private BaseTree baseTree;//基地
	private String planYear;//计划年份
	private String startDate;//计划开始日期
	private int intervalDays;//间隔天数
	private String batchNumber;//批次数
	private String planTotal;//计划总数
	private String planUnit;//计划总数单位
	private String isLock;//是否锁(0-否，1-是)
	private String remark;//备注
	private String standardId;//标准ID-非数据库字段

	public BreedingPlan() {
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

	@Transient
	public String getOfficeId() {
		if(StringUtils.isBlank(this.officeId) && office != null){
			officeId = office.getId();
		}
		return this.officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	@Transient
	public String getBaseId() {
		if(StringUtils.isBlank(this.baseId) && baseTree != null){
			baseId = baseTree.getId();
		}
		return this.baseId;
	}
	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}

	@Transient
	public String getOfficeName() {
		if(StringUtils.isBlank(this.officeName) && office != null){
			officeName = office.getName();
		}
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	@Transient
	public String getBaseName() {
		if(StringUtils.isBlank(this.baseName) && baseTree != null){
			baseName = baseTree.getName();
		}
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	@ManyToOne(targetEntity=Office.class,fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@JoinColumn(name="office_id")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@ManyToOne(targetEntity=BaseTree.class,fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@JoinColumn(name="base_id")
	public BaseTree getBaseTree() {
		return baseTree;
	}

	public void setBaseTree(BaseTree baseTree) {
		this.baseTree = baseTree;
	}

	public String getPlanYear() {
		return this.planYear;
	}
	public void setPlanYear(String planYear) {
		this.planYear = planYear;
	}

	@Transient
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Transient
	public int getIntervalDays() {
		return intervalDays;
	}

	public void setIntervalDays(int intervalDays) {
		this.intervalDays = intervalDays;
	}

	public String getBatchNumber() {
		return this.batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	
	public String getPlanTotal() {
		return this.planTotal;
	}
	public void setPlanTotal(String planTotal) {
		this.planTotal = planTotal;
	}
	
	public String getPlanUnit() {
		return this.planUnit;
	}
	public void setPlanUnit(String planUnit) {
		this.planUnit = planUnit;
	}
	
	public String getIsLock() {
		return this.isLock;
	}
	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}
	
	public String getRemark() {
		return this.remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Transient
	public String getStandardId() {
		return standardId;
	}

	public void setStandardId(String standardId) {
		this.standardId = standardId;
	}
	
}


