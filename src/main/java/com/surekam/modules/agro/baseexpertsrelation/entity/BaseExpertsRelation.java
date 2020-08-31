package com.surekam.modules.agro.baseexpertsrelation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

import java.lang.String;

/**
 * 基地专家关联表Entity
 * @author luoxw
 * @version 2019-10-29
 */
@Entity
@Table(name = "t_agro_base_experts_relation")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BaseExpertsRelation extends XGXTEntity<BaseExpertsRelation> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String baseId;//基地ID
	private String officeId;//公司ID
	private String expertId;//专家ID
	private String leader;//是否负责专家；1是，0不是

	public BaseExpertsRelation() {
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
	
	public String getBaseId() {
		return this.baseId;
	}
	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
	public String getOfficeId() {
		return this.officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	
	public String getExpertId() {
		return this.expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	
	public String getLeader() {
		return this.leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	
}


