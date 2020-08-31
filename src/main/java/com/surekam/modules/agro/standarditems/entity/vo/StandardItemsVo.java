package com.surekam.modules.agro.standarditems.entity.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.surekam.common.persistence.XGXTEntity;

/**
 * 标准作业项表Entity
 * 
 * @author liwei
 * @version 2019-04-25
 */

public class StandardItemsVo extends XGXTEntity<StandardItemsVo> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String name;// 作业项名称
	private String standardsType; // 标准类型
	private String operationType;// 作业类型 1：条目 2：集合

	private List<StandardItemsVo> childList = Lists.newArrayList();// 拥有子机构列表

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<StandardItemsVo> getChildList() {
		return childList;
	}

	public void setChildList(List<StandardItemsVo> childList) {
		if (childList != null && childList.size() > 0) {
			for (StandardItemsVo vo : childList) {
				StandardItemsVo agroProductLibraryTreeVo = new StandardItemsVo();
				BeanUtils.copyProperties(vo, agroProductLibraryTreeVo);
				this.childList.add(agroProductLibraryTreeVo);
			}
		} else {
			this.childList = new ArrayList<StandardItemsVo>();
		}
		this.childList = childList;
	}

	public String getStandardsType() {
		return standardsType;
	}

	public void setStandardsType(String standardsType) {
		this.standardsType = standardsType;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

}
