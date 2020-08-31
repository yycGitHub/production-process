package com.surekam.modules.agro.notuploadrecord.entity;

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
 * 不上传记录表Entity
 * 
 * @author tangjun
 * @version 2019-07-12
 */
@Entity
@Table(name = "t_agro_not_upload_record")
@DynamicInsert
@DynamicUpdate
public class NotUploadRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String batchId;// 批次id
	private String modularTypeId;// 模块类型ID
	private String dataId;// 数据ID

	public NotUploadRecord() {
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

	public String getBatchId() {
		return this.batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getModularTypeId() {
		return this.modularTypeId;
	}

	public void setModularTypeId(String modularTypeId) {
		this.modularTypeId = modularTypeId;
	}

	public String getDataId() {
		return this.dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

}
