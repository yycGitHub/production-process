package com.surekam.modules.api.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.DataEntity;

/**
 * 接口调用方注册Entity
 * @author lb
 * @version 2018-03-09
 */
@Entity
@Table(name = "api_user")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApiUser extends DataEntity<ApiUser> {
	
	private static final long serialVersionUID = 1L;
	private String name;//
	private String appId;//
	private String secretCode;//

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getSecretCode() {
		return secretCode;
	}
	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}
}


