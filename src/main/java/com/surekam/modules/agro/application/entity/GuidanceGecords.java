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
 * 专家指导记录表Entity
 * @author xy
 * @version 2019-06-25
 */
@Entity
@Table(name = "t_agro_guidance_gecords")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GuidanceGecords extends XGXTEntity<GuidanceGecords> {
	
	private static final long serialVersionUID = 1L;
	private String id;//指导记录id
	private String officeId;//公司ID
	private String baseId;//基地ID
	private String productId;//产品品种
	private String batchId;//批次id
	private String expertId;//专家id
	private String applicationId;//申请记录id
	private String detailedQuestions;//问题详细
	private String address;//位置
	private String longitude;//经度
	private String latitude;//纬度
	private String details;//详细内容
	private String guidanceTime;//指导时间
	private String delegationId;//委派记录id
	
	private List<AgroFileInfo> fileList;//图片list
	private List<AgroFileInfo> auditList;//录音list
	private List<String> fileUrlList;//图片list
	private String expertName;//专家姓名
	private String officeName;//公司名称
	private String baseName;//基地名称

	public GuidanceGecords() {
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
	
	public String getProductId() {
		return this.productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getBatchId() {
		return this.batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	public String getExpertId() {
		return this.expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	
	public String getApplicationId() {
		return this.applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	
	public String getDetailedQuestions() {
		return this.detailedQuestions;
	}
	public void setDetailedQuestions(String detailedQuestions) {
		this.detailedQuestions = detailedQuestions;
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
	
	public String getDetails() {
		return this.details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	
	public String getGuidanceTime() {
		return this.guidanceTime;
	}
	public void setGuidanceTime(String guidanceTime) {
		this.guidanceTime = guidanceTime;
	}

	public String getDelegationId() {
		return delegationId;
	}

	public void setDelegationId(String delegationId) {
		this.delegationId = delegationId;
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
	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
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
	public List<String> getFileUrlList() {
		return fileUrlList;
	}

	public void setFileUrlList(List<String> fileUrlList) {
		this.fileUrlList = fileUrlList;
	}
	
	
	
}


