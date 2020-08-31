package com.surekam.modules.agro.videopeopleinvolvement.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.PrePersist;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import java.lang.String;

/**
 * 实时音视频参与人员表Entity
 * @author liwei
 * @version 2019-07-16
 */
@Entity
@Table(name = "t_agro_video_people_involvement")
@DynamicInsert @DynamicUpdate
public class VideoPeopleInvolvement extends XGXTEntity<VideoPeopleInvolvement> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String mainId;//主表ID
	private String userCode;//参与人账号
	private String userName;//参与人姓名
	private String taskId;//任务ID
	private String fileId;//文件ID
	private String userId;//用户ID
	private String userType;//用户ID
	private String companyName;//公司名称
	private String thumbnail;//缩略图（来源于腾讯云平台）
	private String productData;//专家领域信息
	private String goodproblemData;//专家擅长问题信息
	private String expertDescription;//简介

	public VideoPeopleInvolvement() {
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
	
	public String getMainId() {
		return this.mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	
	public String getUserCode() {
		return this.userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	public String getUserName() {
		return this.userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getThumbnail() {
		return this.thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Transient
	public String getProductData() {
		return productData;
	}

	public void setProductData(String productData) {
		this.productData = productData;
	}

	@Transient
	public String getGoodproblemData() {
		return goodproblemData;
	}

	public void setGoodproblemData(String goodproblemData) {
		this.goodproblemData = goodproblemData;
	}

	@Transient
	public String getExpertDescription() {
		return expertDescription;
	}

	public void setExpertDescription(String expertDescription) {
		this.expertDescription = expertDescription;
	}
	
}


