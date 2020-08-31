package com.surekam.modules.agro.uploadauditrecord.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.utils.IdGen;

/**
 * 上传审核记录表Entity
 * 
 * @author tangjun
 * @version 2019-07-12
 */
@Entity
@Table(name = "t_agro_upload_audit_record")
@DynamicInsert
@DynamicUpdate
public class UploadAuditRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String officeId;// 公司iD
	private String standardTaskItemsArgsValueId; // 标准作业执行记录表Id
	private String batchId;// 批次ID
	private String auditStatus;// 审核状态 1-已上传未审 2-已上传已审 未通过 3-已上传已审 通过
	private String auditTime;// 审核时间
	private String auditOpinions;// 审核意见
	private String syBatchCode;// 溯源批次号
	private String syCode;// 溯源码
	private String loginName; // 登录名
	private String userName; // 姓名

	public UploadAuditRecord() {
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

	public String getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getBatchId() {
		return this.batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getAuditStatus() {
		return this.auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditTime() {
		return this.auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditOpinions() {
		return this.auditOpinions;
	}

	public void setAuditOpinions(String auditOpinions) {
		this.auditOpinions = auditOpinions;
	}

	public String getStandardTaskItemsArgsValueId() {
		return standardTaskItemsArgsValueId;
	}

	public void setStandardTaskItemsArgsValueId(String standardTaskItemsArgsValueId) {
		this.standardTaskItemsArgsValueId = standardTaskItemsArgsValueId;
	}

	public String getSyBatchCode() {
		return syBatchCode;
	}

	public void setSyBatchCode(String syBatchCode) {
		this.syBatchCode = syBatchCode;
	}

	public String getSyCode() {
		return syCode;
	}

	public void setSyCode(String syCode) {
		this.syCode = syCode;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
