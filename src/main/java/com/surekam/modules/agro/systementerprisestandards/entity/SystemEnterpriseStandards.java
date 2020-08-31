package com.surekam.modules.agro.systementerprisestandards.entity;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 标准库(系统标准+企业标准)Entity
 * 
 * @author liwei
 * @version 2019-04-23
 */
@Entity
@Table(name = "t_agro_system_enterprise_standards")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SystemEnterpriseStandards extends XGXTEntity<SystemEnterpriseStandards> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String productId;// 品种主键
	private String productName;// 品种名称
	private String copyFromSystemStandardsId;// 复制来源标准库ID
	private String standardName;// 标准库名称
	private String versionName;// 版本号
	private String officeId;// 企业主键
	private String officeName;// 企业 名称
	private String standardsType;// 标准类型(作业标准与环境标准)
	private String standardsDescription;// 版本库描述
	private String productionModelId; // 生产模式ID
	private String productionName; // 生产模式名称
	private String label; // 传感器
	@ApiModelProperty(hidden = true)
	private Date startTime;// 标准开始时间
	@ApiModelProperty(hidden = true)
	private Date endTime;// 标准结束时间-没有值代表无限期

	public SystemEnterpriseStandards() {
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

	public String getCopyFromSystemStandardsId() {
		return this.copyFromSystemStandardsId;
	}

	public void setCopyFromSystemStandardsId(String copyFromSystemStandardsId) {
		this.copyFromSystemStandardsId = copyFromSystemStandardsId;
	}

	public String getStandardName() {
		return this.standardName;
	}

	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}

	public String getVersionName() {
		return this.versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStandardsType() {
		return this.standardsType;
	}

	public void setStandardsType(String standardsType) {
		this.standardsType = standardsType;
	}

	public String getStandardsDescription() {
		return this.standardsDescription;
	}

	public void setStandardsDescription(String standardsDescription) {
		this.standardsDescription = standardsDescription;
	}

	@Transient
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Transient
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getProductionModelId() {
		return productionModelId;
	}

	public void setProductionModelId(String productionModelId) {
		this.productionModelId = productionModelId;
	}

	@Transient
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Transient
	public String getProductionName() {
		return productionName;
	}

	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
	@DateBridge(resolution = Resolution.DAY)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
	@DateBridge(resolution = Resolution.DAY)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
