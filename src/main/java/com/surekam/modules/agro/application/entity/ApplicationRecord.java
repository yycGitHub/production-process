package com.surekam.modules.agro.application.entity;

import javax.persistence.Column;
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
 * 农户申请记录表Entity
 * @author xy
 * @version 2019-06-25
 */
@Entity
@Table(name = "t_agro_application_record")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationRecord extends XGXTEntity<ApplicationRecord> {
	
	private static final long serialVersionUID = 1L;
	private String id;//农户申请记录id
	private String officeId;//公司id
	private String baseId;//基地id
	private String appointmentTime;//预约日期
	private String address;//地址
	private String phoneNumber;//电话号码
	private String contacts;//联系人
	private String productId;//产品品种
	private String detailedQuestions;//问题详细
	private String batchId;//批次id
	private String longitude;//经度
	private String latitude;//纬度

	private List<AgroFileInfo> fileList;//图片list
	private List<AgroFileInfo> auditList;//录音list
	private List<String> fileUrlList;//图片list
	
	private String officeName;//公司名称
	private String baseName;//基地名称
	private String productName;//产品品种
	private String batchCode;//批次id
	
	public ApplicationRecord() {
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
	
	@Column(name="OFFICE_ID")
	public String getOfficeId() {
		return this.officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	
	@Column(name="BASE_ID")
	public String getBaseId() {
		return this.baseId;
	}
	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
	public String getAppointmentTime() {
		return this.appointmentTime;
	}
	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	
	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getContacts() {
		return this.contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	
	public String getProductId() {
		return this.productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getDetailedQuestions() {
		return this.detailedQuestions;
	}
	public void setDetailedQuestions(String detailedQuestions) {
		this.detailedQuestions = detailedQuestions;
	}
	
	public String getBatchId() {
		return this.batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	@Transient
	public List<AgroFileInfo> getFileList() {
		return fileList;
	}

	public void setFileList(List<AgroFileInfo> fileList) {
		this.fileList = fileList;
	}

	@Transient
	public List<AgroFileInfo> getAuditList() {
		return auditList;
	}

	public void setAuditList(List<AgroFileInfo> auditList) {
		this.auditList = auditList;
	}

	@Transient
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	@Transient
	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	@Transient
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Transient
	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	@Transient
	public List<String> getFileUrlList() {
		return fileUrlList;
	}

	public void setFileUrlList(List<String> fileUrlList) {
		this.fileUrlList = fileUrlList;
	}
	
}


