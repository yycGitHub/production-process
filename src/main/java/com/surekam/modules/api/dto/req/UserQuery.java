package com.surekam.modules.api.dto.req;

import java.io.Serializable;

public class UserQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 登录名
	 */
	private String loginName;
	private String companyId;
	private String officeId;
	private String delFlag;
	private String orderFields;
	private Integer pageno;
	private Integer pagesize;


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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getOrderFields() {
		return orderFields;
	}

	public void setOrderFields(String orderFields) {
		this.orderFields = orderFields;
	}

	public Integer getPageno() {
		return pageno;
	}

	public void setPageno(Integer pageno) {
		this.pageno = pageno;
	}

	public Integer getPagesize() {
		return pagesize;
	}

	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}


}
