package com.surekam.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

import java.lang.String;

/**
 * 系统图标表Entity
 * @author luoxw
 * @version 2019-05-16
 */
@Entity
@Table(name = "sys_icon")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysIcon extends XGXTEntity<SysIcon> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String iconUrl;//图标地址
	private String iconDescription;//图标描述

	public SysIcon() {
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
	
	public String getIconUrl() {
		return this.iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public String getIconDescription() {
		return this.iconDescription;
	}
	public void setIconDescription(String iconDescription) {
		this.iconDescription = iconDescription;
	}
	
}


