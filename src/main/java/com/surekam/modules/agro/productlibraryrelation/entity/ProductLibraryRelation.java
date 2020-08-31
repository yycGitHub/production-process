package com.surekam.modules.agro.productlibraryrelation.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 种养殖种类树形关系表Entity
 * 
 * @author tangjun
 * @version 2019-08-28
 */
@Entity
@Table(name = "t_agro_product_library_relation")
@DynamicInsert
@DynamicUpdate
public class ProductLibraryRelation implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String officeId;// 公司id
	private String officeIds;// 所有父级编号
	private String parentId;// 种养殖种类树Id
	private String parentsIds;// 完整父级ID路径

	public ProductLibraryRelation() {
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

	public String getOfficeIds() {
		return this.officeIds;
	}

	public void setOfficeIds(String officeIds) {
		this.officeIds = officeIds;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentsIds() {
		return this.parentsIds;
	}

	public void setParentsIds(String parentsIds) {
		this.parentsIds = parentsIds;
	}

}
