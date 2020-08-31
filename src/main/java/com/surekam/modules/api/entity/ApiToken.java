package com.surekam.modules.api.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.BaseEntity;
import com.surekam.common.utils.IdGen;

/**
 * 接入方注册Entity
 * @author lb
 * @version 2018-03-09
 */
@Entity
@Table(name = "api_token")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApiToken extends BaseEntity<ApiToken> {
	
	private static final long serialVersionUID = 1L;
	private String id;//
	private String appId;
	private String token;//
	private Date failureTime;//

	public ApiToken() {
		super();
	}
	
	@PrePersist
	public void prePersist(){
		this.id = IdGen.uuid();
	}
		
	@Id
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getFailureTime() {
		return failureTime;
	}

	public void setFailureTime(Date failureTime) {
		this.failureTime = failureTime;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}


