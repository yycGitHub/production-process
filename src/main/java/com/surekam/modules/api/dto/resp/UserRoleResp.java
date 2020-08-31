package com.surekam.modules.api.dto.resp;

import java.io.Serializable;

public class UserRoleResp implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 公司ID
	 */
	private String companyId;
	/**
	 * 登录名
	 */
	private String loginName;
	/**
	 * 行吗
	 */
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "UserRoleResp [id=" + id + ", companyId=" + companyId + ", loginName=" + loginName + ", name=" + name
				+ "]";
	}

}
