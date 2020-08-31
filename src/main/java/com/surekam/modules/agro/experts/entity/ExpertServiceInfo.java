package com.surekam.modules.agro.experts.entity;

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
 * 专家服务信息Entity
 * @author xy
 * @version 2019-04-28
 */
@Entity
@Table(name = "t_agro_experts_service")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExpertServiceInfo extends XGXTEntity<ExpertServiceInfo> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String serviceCompanyId;//服务公司
	private String serviceBaseId;//服务基地
	private String serviceTime;//服务时间
	private String serviceType;//服务类型（1收集样本、2咨询服务、3技术支持、4例行服务、5其他）
	private String serviceAddress;//服务地址
	private String serviceDetails;//服务详情
	private String expertId;//专家id

	private String baseName;//基地名称
	private String companyName;//公司名称
	private String servicePeopleName;//公司名称
	private List<AgroFileInfo> fileList;//照片附件list
	private List<AgroFileInfo> auditList;//录音list
	private String strDate;//日期 （“yyyy-MM-dd”）
	private String strtype;//类型
	private String expertName;//专家id
	
	private String personName;//基地负责人姓名
	private String personPhone;//基地负责人电话

	public ExpertServiceInfo() {
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
	
	public String getServiceBaseId() {
		return serviceBaseId;
	}

	public void setServiceBaseId(String serviceBaseId) {
		this.serviceBaseId = serviceBaseId;
	}

	public String getServiceCompanyId() {
		return this.serviceCompanyId;
	}
	public void setServiceCompanyId(String serviceCompanyId) {
		this.serviceCompanyId = serviceCompanyId;
	}
	
	public String getServiceTime() {
		return this.serviceTime;
	}
	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}
	
	public String getServiceType() {
		return this.serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public String getServiceAddress() {
		return this.serviceAddress;
	}
	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}
	
	public String getServiceDetails() {
		return this.serviceDetails;
	}
	public void setServiceDetails(String serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	@Transient
	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	@Transient
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Transient
	public List<AgroFileInfo> getFileList() {
		return fileList;
	}

	public void setFileList(List<AgroFileInfo> fileList) {
		this.fileList = fileList;
	}

	public String getExpertId() {
		return expertId;
	}

	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}

	@Transient
	public String getServicePeopleName() {
		return servicePeopleName;
	}

	public void setServicePeopleName(String servicePeopleName) {
		this.servicePeopleName = servicePeopleName;
	}

	@Transient
	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	@Transient
	public List<AgroFileInfo> getAuditList() {
		return auditList;
	}

	public void setAuditList(List<AgroFileInfo> auditList) {
		this.auditList = auditList;
	}

	@Transient
	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	@Transient
	public String getStrtype() {
		return strtype;
	}

	public void setStrtype(String strtype) {
		this.strtype = strtype;
	}

	@Transient
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	@Transient
	public String getPersonPhone() {
		return personPhone;
	}

	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}	
	
	
	
}


