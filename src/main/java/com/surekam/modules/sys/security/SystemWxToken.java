package com.surekam.modules.sys.security;


public class SystemWxToken extends UsernamePasswordToken {
	private static final long serialVersionUID = 1L;
	
	private String openId;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

}
