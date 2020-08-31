package com.surekam.modules.agro.basemanage.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 基地用户管理Entity
 * 
 * @author tangjun
 * @version 2019-04-09
 */
@Entity
@Table(name = "t_agro_base_manager")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BaseManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private String tBaseId;// 基地树_主键
	private String userId;// 管理员ID
	private String roleId; // 角色ID

	public BaseManager() {
		super();
	}

	@Id
	@Column(name = "t_base_id")
	public String getTBaseId() {
		return this.tBaseId;
	}

	public void setTBaseId(String tBaseId) {
		this.tBaseId = tBaseId;
	}

	@Id
	@Column(name = "user_id")
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Id
	@Column(name = "role_id")
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
