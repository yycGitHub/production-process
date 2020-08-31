package com.surekam.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "sys_login_token")
@DynamicInsert 
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LoginToken implements Serializable {
	
	private static final long serialVersionUID = -7555256399637594398L;
	protected String id;
	private String userId;
	private String userName;
	private String token;
	private Date createTime = new Date();
	private Date failTime;
	
	public LoginToken() {
		super();
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getFailTime() {
		return failTime;
	}

	public void setFailTime(Date failTime) {
		this.failTime = failTime;
	}

}
