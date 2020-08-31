package com.surekam.modules.agro.experts.entity;

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
 * 专家擅长问题Entity
 * 
 * @author xy
 * @version 2019-04-16
 */
@Entity
@Table(name = "t_agro_experts_goodproblem")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExpertsGoodproblem extends XGXTEntity<ExpertsGoodproblem> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String expertId;// 专家ID
	private String goodProblem;// 擅长问题

	public ExpertsGoodproblem() {
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

	public String getExpertId() {
		return this.expertId;
	}

	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}

	public String getGoodProblem() {
		return this.goodProblem;
	}

	public void setGoodProblem(String goodProblem) {
		this.goodProblem = goodProblem;
	}

}
