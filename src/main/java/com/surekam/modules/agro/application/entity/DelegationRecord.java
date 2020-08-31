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

import java.lang.String;

/**
 * 平台委派记录表Entity
 * @author xy
 * @version 2019-06-25
 */
@Entity
@Table(name = "t_agro_delegation_record")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DelegationRecord extends XGXTEntity<DelegationRecord> {
	
	private static final long serialVersionUID = 1L;
	private String id;//委派记录id
	private String applicationId;//农户申请id
	private String expertId;//专家id
	private String acceptStatus;//是否接受委派
	private String remark;//委派说明
	private String explainNo;//拒绝原因
	private String taskStatus;//任务状态

	private String expertName;//专家名称
	private String appointmentTime;//预约服务日期
	
	public DelegationRecord() {
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
	
	public String getExpertId() {
		return this.expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	
	public String getAcceptStatus() {
		return this.acceptStatus;
	}
	public void setAcceptStatus(String acceptStatus) {
		this.acceptStatus = acceptStatus;
	}
	
	public String getRemark() {
		return this.remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getTaskStatus() {
		return this.taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getExplainNo() {
		return explainNo;
	}

	public void setExplainNo(String explainNo) {
		this.explainNo = explainNo;
	}

	@Transient
	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	@Transient
	public String getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	
	
}


