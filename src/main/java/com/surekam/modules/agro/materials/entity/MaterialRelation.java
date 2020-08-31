package com.surekam.modules.agro.materials.entity;

import java.io.Serializable;

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
 * 农资与农产品类型关系表Entity
 * 
 * @author tangjun
 * @version 2019-04-22
 */
@Entity
@Table(name = "t_agro_material_relation")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MaterialRelation implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String materialId;// 农事Id
	private String productLibraryId;// 农产品类型id关联的是种养殖种类树形表)
	private String sysDict;// 农资类型

	public MaterialRelation() {
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

	public String getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public String getProductLibraryId() {
		return this.productLibraryId;
	}

	public void setProductLibraryId(String productLibraryId) {
		this.productLibraryId = productLibraryId;
	}

	public String getSysDict() {
		return this.sysDict;
	}

	public void setSysDict(String sysDict) {
		this.sysDict = sysDict;
	}

}
