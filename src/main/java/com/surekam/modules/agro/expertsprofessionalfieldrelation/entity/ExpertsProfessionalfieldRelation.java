package com.surekam.modules.agro.expertsprofessionalfieldrelation.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 专家与专业领域关系表Entity
 * 
 * @author tangjun
 * @version 2019-04-19
 */
@Entity
@Table(name = "t_agro_experts_professionalfield_relation")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExpertsProfessionalfieldRelation extends XGXTEntity<ExpertsProfessionalfieldRelation> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String expertsId;// 专家id
	private String productLibraryId;// 专业领域id(关联的是种养殖种类树形表)

	public ExpertsProfessionalfieldRelation() {
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

	public String getExpertsId() {
		return this.expertsId;
	}

	public void setExpertsId(String expertsId) {
		this.expertsId = expertsId;
	}

	public String getProductLibraryId() {
		return this.productLibraryId;
	}

	public void setProductLibraryId(String productLibraryId) {
		this.productLibraryId = productLibraryId;
	}

}
