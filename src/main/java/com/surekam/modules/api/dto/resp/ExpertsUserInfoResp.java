package com.surekam.modules.api.dto.resp;

import java.io.Serializable;

public class ExpertsUserInfoResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 登陆名
	 */
	private String loginName;
	/**
	 * 头像
	 */
	private String userImg;

	/**
	 * 状态
	 */
	private String delFlag;

	/**
	 * 手机号码
	 */
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

}
