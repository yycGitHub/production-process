package com.surekam.modules.sys.security;

public class CookieLoginToken extends UsernamePasswordToken {
	
	private static final long serialVersionUID = 1L;
	
	private String loginCode;

	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}
	
	

}
