package com.surekam.modules.agro.application.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 现场指导操作记录
 * @author xy
 * @version 2019-08-16
 */
@Entity
@Table(name = "t_agro_operational_records")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OperationalRecords extends XGXTEntity<OperationalRecords>{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String type;//类型
	private String ywid;//业务表id
	private String arid;//申请记录id
	private String handle;//操作
	
	private ApplicationRecord applicationRecord;//申请记录
	private DelegationRecord delegationRecord;//委派记录
	private GuidanceGecords guidanceGecords;//指导记录
	private Feedback feedback;//反馈记录
	
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getYwid() {
		return ywid;
	}
	public void setYwid(String ywid) {
		this.ywid = ywid;
	}
	public String getArid() {
		return arid;
	}
	public void setArid(String arid) {
		this.arid = arid;
	}
	
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	@Transient
	public ApplicationRecord getApplicationRecord() {
		return applicationRecord;
	}
	public void setApplicationRecord(ApplicationRecord applicationRecord) {
		this.applicationRecord = applicationRecord;
	}
	@Transient
	public DelegationRecord getDelegationRecord() {
		return delegationRecord;
	}
	public void setDelegationRecord(DelegationRecord delegationRecord) {
		this.delegationRecord = delegationRecord;
	}
	@Transient
	public GuidanceGecords getGuidanceGecords() {
		return guidanceGecords;
	}
	public void setGuidanceGecords(GuidanceGecords guidanceGecords) {
		this.guidanceGecords = guidanceGecords;
	}
	@Transient
	public Feedback getFeedback() {
		return feedback;
	}
	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}
}
