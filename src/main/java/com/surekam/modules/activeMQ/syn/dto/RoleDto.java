package com.surekam.modules.activeMQ.syn.dto;

import java.util.List;

import com.google.common.collect.Lists;


public class RoleDto{
	
	private String kuid;
	private String officeId;	// 归属机构
	private String name; 	// 角色名称
	private List<String> menuIds = Lists.newArrayList(); // 拥有菜单列表

	public String getKuid() {
		return kuid;
	}
	public void setKuid(String kuid) {
		this.kuid = kuid;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(List<String> menuIds) {
		this.menuIds = menuIds;
	}
}
