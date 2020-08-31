package com.surekam.modules.agro.auditrecord.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.PrePersist;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import com.surekam.modules.agro.file.entity.AgroFileInfo;

import java.lang.String;
import java.util.List;

/**
 * 巡检记录表Entity
 * @author liwei
 * @version 2019-05-29
 */
@Entity
@Table(name = "t_agro_audit_record")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuditRecord extends XGXTEntity<AuditRecord> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String baseId;//基地
	private String baseName;//基地名称
	private String auditTime;//检查时间
	private String auditType;//检查类型（1-常规检查、2-周检查、3-月检查、4-例行检查、5其他）
	private String auditTypeName;
	private String auditDetails;//检查事项
	private List<AgroFileInfo> fileList;//照片附件list
	private List<AgroFileInfo> auditList;//录音list

	public AuditRecord() {
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
	
	public String getBaseId() {
		return this.baseId;
	}
	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
	public String getAuditTime() {
		return this.auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	
	public String getAuditType() {
		return this.auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	
	public String getAuditDetails() {
		return this.auditDetails;
	}
	public void setAuditDetails(String auditDetails) {
		this.auditDetails = auditDetails;
	}

	@Transient
	public String getAuditTypeName() {
		return auditTypeName;
	}

	public void setAuditTypeName(String auditTypeName) {
		this.auditTypeName = auditTypeName;
	}

	@Transient
	public List<AgroFileInfo> getFileList() {
		return fileList;
	}

	public void setFileList(List<AgroFileInfo> fileList) {
		this.fileList = fileList;
	}

	@Transient
	public List<AgroFileInfo> getAuditList() {
		return auditList;
	}

	public void setAuditList(List<AgroFileInfo> auditList) {
		this.auditList = auditList;
	}

	@Transient
	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
	
}


