package com.surekam.modules.api.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.DataEntity;

/**
 * 接口管理Entity
 * @author lb
 * @version 2018-03-15
 */
@Entity
@Table(name = "api_user_interface")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApiUserInterface extends DataEntity<ApiUserInterface> {
	
	private static final long serialVersionUID = 1L;
	private String appId;
	private String interfaceId;

	public ApiUserInterface() {
		super();
	}

	public String getAppId() {
		return appId;
	}


	public void setAppId(String appId) {
		this.appId = appId;
	}


	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}
}


