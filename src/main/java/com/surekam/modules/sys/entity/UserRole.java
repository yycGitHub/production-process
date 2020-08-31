/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.DataEntity;

/**
 * 用户Entity
 * @author liuyi
 * @version 2018-1-29
 */

public class UserRole extends DataEntity<UserRole> {

	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String roleId;
	
	

	public UserRole() {
		super();
	}



	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}



	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}



	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}



	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	

	
	
	
	
}