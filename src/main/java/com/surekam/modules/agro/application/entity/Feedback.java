package com.surekam.modules.agro.application.entity;

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
 * 反馈记录表Entity
 * @author xy
 * @version 2019-06-25
 */
@Entity
@Table(name = "t_agro_feedback")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Feedback extends XGXTEntity<Feedback> {
	
	private static final long serialVersionUID = 1L;
	private String id;//反馈id
	private String applicationId;//农户申请记录id
	private String guidanceId;//专家指导记录id
	private String farmerId;//农户id
	private String expertId;//专家id
	private String feedbackOpinion;//反馈意见
	private String feedbackState;//反馈状态 0：已反馈
	private String continueAppoint;//是否继续委派  0:是，1否
	
	private List<AgroFileInfo> fileList;//图片list
	private List<AgroFileInfo> auditList;//录音list
	private List<String> fileUrlList;//图片list
	private String expertName;//专家姓名
	private String userName;//农户姓名

	public Feedback() {
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
	
	public String getApplicationId() {
		return this.applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	
	public String getGuidanceId() {
		return this.guidanceId;
	}
	public void setGuidanceId(String guidanceId) {
		this.guidanceId = guidanceId;
	}
	
	public String getFarmerId() {
		return this.farmerId;
	}
	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
	
	public String getExpertId() {
		return this.expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	
	public String getFeedbackOpinion() {
		return this.feedbackOpinion;
	}
	public void setFeedbackOpinion(String feedbackOpinion) {
		this.feedbackOpinion = feedbackOpinion;
	}
	
	public String getFeedbackState() {
		return this.feedbackState;
	}
	public void setFeedbackState(String feedbackState) {
		this.feedbackState = feedbackState;
	}
	
	public String getContinueAppoint() {
		return this.continueAppoint;
	}
	public void setContinueAppoint(String continueAppoint) {
		this.continueAppoint = continueAppoint;
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
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Transient
	public List<String> getFileUrlList() {
		return fileUrlList;
	}

	public void setFileUrlList(List<String> fileUrlList) {
		this.fileUrlList = fileUrlList;
	}
	
	
}


