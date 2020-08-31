package com.surekam.modules.sys.entity.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.surekam.modules.sys.entity.Area;


public class AreaVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	
	private String parentId;
	private String parentName;
	
	private String parentIds; // 所有父级编号
	private String code; 	// 区域编码
	private String name; 	// 区域名称
	private String type; 	// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	
	private String delFlag;
	
	private List<AreaVo> childList = Lists.newArrayList();// 拥有子机构列表
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public List<AreaVo> getChildList() {
		return childList;
	}
	public void setChildList(List<Area> childList) {
		if(childList != null && childList.size()>0){
			for (Area area : childList) {
				AreaVo areaVo = new AreaVo();
				BeanUtils.copyProperties(area, areaVo);
				this.childList.add(areaVo);
			}
		}else{
			this.childList = new ArrayList<AreaVo>();
		}
	}
}