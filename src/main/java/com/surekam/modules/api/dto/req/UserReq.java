package com.surekam.modules.api.dto.req;

import java.io.Serializable;

public class UserReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 登录名
	 */
	private String loginName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 用户头像
	 */
	private String userImg;

	/**
	 * 邮箱
	 */
	private String email;

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
