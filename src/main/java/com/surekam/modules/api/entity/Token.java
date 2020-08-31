package com.surekam.modules.api.entity;

import java.io.Serializable;

/**
 * 
 * @author wangyuewen
 */
public class Token implements Serializable{
	private static final long serialVersionUID = 1L;
	private String token;
	
	public Token() {
		super();
	}
	
	public Token(String token) {
		super();
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}

