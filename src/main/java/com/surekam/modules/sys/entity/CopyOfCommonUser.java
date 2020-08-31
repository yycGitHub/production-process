package com.surekam.modules.sys.entity;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

/**
 * 用户Entity
 * @author sureserve
 * @version 2013-5-15
 */
public class CopyOfCommonUser {
	private String id;// 登录ID
	private String name;// 用户姓名
	private String loginName;// 登录名
	private List<String> roleIdList = Lists.newArrayList(); // 拥有角色ID列表
	private List<String> roleNameList = Lists.newArrayList(); // 拥有角色名称列表
	private List<CopyMenu> menuList = Lists.newArrayList(); // 拥有角色列表

	private String phone;// 电话号
	private String officeId;
	private String officeLog;// 公司logo
	private String officeName;// 公司名称
	private Map<String,Boolean> buttonList;// 按钮权限

	public Map<String, Boolean> getButtonList() {
		return buttonList;
	}

	public void setButtonList(Map<String, Boolean> buttonList) {
		this.buttonList = buttonList;
	}

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

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public List<String> getRoleNameList() {
		return roleNameList;
	}

	public void setRoleNameList(List<String> roleNameList) {
		this.roleNameList = roleNameList;
	}

	public List<CopyMenu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<CopyMenu> menuList) {
		this.menuList = menuList;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOfficeLog() {
		return officeLog;
	}

	public void setOfficeLog(String officeLog) {
		this.officeLog = officeLog;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
}