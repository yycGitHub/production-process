package com.surekam.modules.agro.product.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 品种分类Entity
 * 
 * @author lb
 * @version 2019-04-10
 */
@Entity
@Table(name = "t_agro_product_library_tree")
@DynamicInsert
@DynamicUpdate
public class ProductLibraryTree extends XGXTEntity<ProductLibraryTree> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private ProductLibraryTree parent;
	private String productCategoryCode;// 种类编号
	private String productCategoryName;// 种类名称
	private String parentsIds;// 完整父级ID路径
	private String productCategoryImgUrl;// 品种图片
	private String isProductCategory;// 是否为种类,1是种类，2是品种
	private String sort;// 排序

	List<ProductLibraryTree> childList = new ArrayList<ProductLibraryTree>();

	private String parentId;

	public ProductLibraryTree() {
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public ProductLibraryTree getParent() {
		return parent;
	}

	public void setParent(ProductLibraryTree parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@Where(clause = "states <>'" + STATE_FLAG_DEL + "'")
	@OrderBy(value = "sort")
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public List<ProductLibraryTree> getChildList() {
		return childList;
	}

	public void setChildList(List<ProductLibraryTree> childList) {
		this.childList = childList;
	}

	public String getProductCategoryCode() {
		return this.productCategoryCode;
	}

	public void setProductCategoryCode(String productCategoryCode) {
		this.productCategoryCode = productCategoryCode;
	}

	public String getProductCategoryName() {
		return this.productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public String getParentsIds() {
		return this.parentsIds;
	}

	public void setParentsIds(String parentsIds) {
		this.parentsIds = parentsIds;
	}

	public String getProductCategoryImgUrl() {
		return this.productCategoryImgUrl;
	}

	public void setProductCategoryImgUrl(String productCategoryImgUrl) {
		this.productCategoryImgUrl = productCategoryImgUrl;
	}

	public String getIsProductCategory() {
		return this.isProductCategory;
	}

	public void setIsProductCategory(String isProductCategory) {
		this.isProductCategory = isProductCategory;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Transient
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
