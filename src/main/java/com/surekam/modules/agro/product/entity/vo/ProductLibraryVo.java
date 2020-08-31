package com.surekam.modules.agro.product.entity.vo;

import java.util.ArrayList;
import java.util.List;

public class ProductLibraryVo {

	private String id;// 主键
	private String parentId;
	private String parentName;
	private String productCategoryCode;// 种类编号
	private String productCategoryName;// 种类名称
	private String parentsIds;// 完整父级ID路径
	private String parentsNames;// 完整父级名称路径
	private String productCategoryImgUrl;// 品种图片
	private String isProductCategory;// 是否为种类
	private String sort;// 排序
	private String states;
	private String officeStr;

	List<ProductLibraryVo> childList = new ArrayList<ProductLibraryVo>();

	public ProductLibraryVo() {
		super();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getProductCategoryCode() {
		return productCategoryCode;
	}

	public void setProductCategoryCode(String productCategoryCode) {
		this.productCategoryCode = productCategoryCode;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public String getParentsIds() {
		return parentsIds;
	}

	public void setParentsIds(String parentsIds) {
		this.parentsIds = parentsIds;
	}

	public String getParentsNames() {
		return parentsNames;
	}

	public void setParentsNames(String parentsNames) {
		this.parentsNames = parentsNames;
	}

	public String getProductCategoryImgUrl() {
		return productCategoryImgUrl;
	}

	public void setProductCategoryImgUrl(String productCategoryImgUrl) {
		this.productCategoryImgUrl = productCategoryImgUrl;
	}

	public String getIsProductCategory() {
		return isProductCategory;
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

	public List<ProductLibraryVo> getChildList() {
		return childList;
	}

	public void setChildList(List<ProductLibraryVo> childList) {
		this.childList = childList;
	}

	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
	}

	public String getOfficeStr() {
		return officeStr;
	}

	public void setOfficeStr(String officeStr) {
		this.officeStr = officeStr;
	}

}
