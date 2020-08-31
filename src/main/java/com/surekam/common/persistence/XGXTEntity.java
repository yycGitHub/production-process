package com.surekam.common.persistence;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

import com.fasterxml.jackson.annotation.JsonFormat;

@MappedSuperclass
public abstract class XGXTEntity<T> extends BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(hidden = true)
	protected String states; // 操作状态，数据字典：A增加； U更新；D删除
	@ApiModelProperty(hidden = true)
	private Date createTime;// 创建时间
	@ApiModelProperty(hidden = true)
	private String createUserId;// 创建人
	@ApiModelProperty(hidden = true)
	private Date updateTime;// 最后修改时间
	@ApiModelProperty(hidden = true)
	private String updateUserId;// 更新人

	public XGXTEntity() {
		super();
		this.states = STATE_FLAG_ADD;
		this.createTime = new Date();
	}

	@Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
	@DateBridge(resolution = Resolution.DAY)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
	@DateBridge(resolution = Resolution.DAY)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

}
