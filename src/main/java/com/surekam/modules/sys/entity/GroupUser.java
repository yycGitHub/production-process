package com.surekam.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.surekam.common.persistence.BaseEntity;
import com.surekam.common.persistence.IdEntity;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import com.surekam.modules.sys.utils.UserUtils;

import java.lang.String;
import java.util.Date;
import java.util.List;

/**
 * 项目组Entity
 * @author liuyi
 * @version 2018-03-23
 */
@Entity
@Table(name = "sys_group_user")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GroupUser extends IdEntity<GroupUser> {
	
	private static final long serialVersionUID = 1L;
	
	private String groupId;//组主键id
	private String userId;//用户主键id
	
	private String master;// 是否是组管理
	
	
	

	public GroupUser() {
		super();
	}
	
	/*@PrePersist
	public void prePersist(){
		this.groupId = IdGen.uuid();
	}*/
		
	
	public String getGroupId() {
		return this.groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
	 * @return the master
	 */
	public String getMaster() {
		return master;
	}

	/**
	 * @param master the master to set
	 */
	public void setMaster(String master) {
		this.master = master;
	}
	
	
	
}


