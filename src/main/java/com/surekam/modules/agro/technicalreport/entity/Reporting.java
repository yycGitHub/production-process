package com.surekam.modules.agro.technicalreport.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import com.surekam.modules.agro.file.entity.AgroFileInfo;

import java.lang.String;
import java.util.List;

/**
 * 汇报信息Entity
 * @author xy
 * @version 2019-07-09
 */
@Entity
@Table(name = "t_agro_reporting")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Reporting extends XGXTEntity<Reporting> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键id
	private String workDone;//完成的工作
	private String plannedWork;//计划的工作
	private String otherMatters;//其他事项
	private String reportUserId;//汇报给谁
	private String reportDate;//汇报日期
	private String address;//提交地点
	private String longitude;//经度
	private String latitude;//纬度
	private String officeId;//公司id
	private String baseId;//基地id
	private String reportingType;//汇报类型 1:日报，2：周报，3：月报，4：总结
	private String attachments;//是否有附件0:有，1：无
	private String auditStatus;//审核状态；0：未审核，1：合格，2：不合格
	
	private List<AgroFileInfo> fileList;//文件list
	private List<AgroFileInfo> photoList;//图片list
	private String createUserName;// 创建人

	public Reporting() {
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
	
	public String getWorkDone() {
		return this.workDone;
	}
	public void setWorkDone(String workDone) {
		this.workDone = workDone;
	}
	
	public String getPlannedWork() {
		return this.plannedWork;
	}
	public void setPlannedWork(String plannedWork) {
		this.plannedWork = plannedWork;
	}
	
	public String getOtherMatters() {
		return this.otherMatters;
	}
	public void setOtherMatters(String otherMatters) {
		this.otherMatters = otherMatters;
	}
	
	public String getReportUserId() {
		return this.reportUserId;
	}
	public void setReportUserId(String reportUserId) {
		this.reportUserId = reportUserId;
	}
	
	public String getReportDate() {
		return this.reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	
	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getLongitude() {
		return this.longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getLatitude() {
		return this.latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getOfficeId() {
		return this.officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	
	public String getBaseId() {
		return this.baseId;
	}
	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
	public String getReportingType() {
		return this.reportingType;
	}
	public void setReportingType(String reportingType) {
		this.reportingType = reportingType;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	@Transient
	public List<AgroFileInfo> getFileList() {
		return fileList;
	}

	public void setFileList(List<AgroFileInfo> fileList) {
		this.fileList = fileList;
	}

	@Transient
	public List<AgroFileInfo> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<AgroFileInfo> photoList) {
		this.photoList = photoList;
	}

	@Transient
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	
	
	
}


