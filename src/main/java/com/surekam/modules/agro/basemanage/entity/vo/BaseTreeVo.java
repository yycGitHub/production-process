package com.surekam.modules.agro.basemanage.entity.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.PrePersist;

import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.surekam.common.utils.IdGen;
import com.surekam.modules.agro.basemanage.entity.BaseTree;

/**
 * 基地管理Entity
 * 
 * @author tangjun
 * @version 2019-04-10
 */

public class BaseTreeVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String parentId;// 上级ID
	private String parentIds;// 所有上级ID，用,隔开
	private String name;// 名称
	private String officeId;// 公司ID
	private String sort;// 排序
	private String isOffice; // 是否是公司还是基地 0：为是 1：基地
	private String code; // 编码

	private List<BaseTreeVo> childList = Lists.newArrayList();// 拥有子机构列表

	public BaseTreeVo() {
		super();
	}

	@PrePersist
	public void prePersist() {
		this.id = IdGen.uuid();
	}

	public List<BaseTreeVo> getChildList() {
		return childList;
	}

	public void setChildList(List<BaseTree> childList) {
		if (childList != null && childList.size() > 0) {
			for (BaseTree agroBaseTreeArray : childList) {
				BaseTreeVo agroBaseTree = new BaseTreeVo();
				BeanUtils.copyProperties(agroBaseTreeArray, agroBaseTree);
				this.childList.add(agroBaseTree);
			}
		} else {
			this.childList = new ArrayList<BaseTreeVo>();
		}
	}

	@Id
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getIsOffice() {
		return isOffice;
	}

	public void setIsOffice(String isOffice) {
		this.isOffice = isOffice;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
