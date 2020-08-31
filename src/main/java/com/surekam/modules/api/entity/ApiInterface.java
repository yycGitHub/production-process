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
@Table(name = "api_interface")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApiInterface extends DataEntity<ApiInterface> {
	
	private static final long serialVersionUID = 1L;
	private String name;//接口名称
	private String url;//接口URL
	private String status;//接口状态

	public ApiInterface() {
		super();
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}


