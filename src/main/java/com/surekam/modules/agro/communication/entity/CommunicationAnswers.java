package com.surekam.modules.agro.communication.entity;

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
 * 问题解答信息Entity
 * @author xy
 * @version 2019-04-09
 */
@Entity
@Table(name = "t_agro_communication_answers")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommunicationAnswers extends XGXTEntity<CommunicationAnswers> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String communicationId;//问题主键
	private String answerContent;//解决方案
	private String expertId;//回复人id
	private String expertName;//回复人姓名
	private String type;//类型 0:专家回复，1提问人回复
	
	private List<AgroFileInfo> fileList;//图片list
	
	private List<AgroFileInfo> auditList;//图片list
	private String strDate;//日期 （“yyyy-MM-dd”）

	public CommunicationAnswers() {
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
	
	public String getCommunicationId() {
		return this.communicationId;
	}
	public void setCommunicationId(String communicationId) {
		this.communicationId = communicationId;
	}
	
	public String getAnswerContent() {
		return this.answerContent;
	}
	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}
	
	public String getExpertId() {
		return this.expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	
	public String getExpertName() {
		return this.expertName;
	}
	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}
	
}


