package com.surekam.modules.agro.uploadsetting.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 上传审核记录表Entity
 * 
 * @author tangjun
 * @version 2019-07-12
 */
@Entity
@Table(name = "t_agro_upload_setting")
@DynamicInsert
@DynamicUpdate
public class UploadSetting implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String officeId;// 公司iD
	private String standardId;// 标准库ID
	private String standardItemsId;// 标准作业项ID

	public UploadSetting() {
		super();
	}

	@PrePersist
	public void prePersist() {
		this.id = IdGen.uuid();
	}

	@Id
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getStandardId() {
		return this.standardId;
	}

	public void setStandardId(String standardId) {
		this.standardId = standardId;
	}

	public String getStandardItemsId() {
		return this.standardItemsId;
	}

	public void setStandardItemsId(String standardItemsId) {
		this.standardItemsId = standardItemsId;
	}

}
