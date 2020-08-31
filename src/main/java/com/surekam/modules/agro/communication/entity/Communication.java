package com.surekam.modules.agro.communication.entity;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.DateUtils;
import com.surekam.common.utils.IdGen;
import com.surekam.modules.agro.file.entity.AgroFileInfo;

/**
 * 问题信息Entity
 * @author xy
 * @version 2019-04-09
 */
@Entity
@Table(name = "t_agro_communication")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Communication extends XGXTEntity<Communication> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String communicationTypeId;//问题类别
	private String baseId;//基地表ID
	private String productionBatchId;//批次主键
	private String communicationTitle;//问题标题
	private String communicationDescription;//问题描述
	private String communicationStatus;//问题状态 1-未解决，0-已解决
	private String communicationBoutique;//精品问题标识 0-加精品
	private String communicationTop;//置顶问题标识 0-置顶
	private BigInteger clickNum;//点击量
	private String sort;//排序权重
	private String expertId;//专家id
	
	private String batchCode;//批次号
	private String expertName;//解答专家
	private String baseName;//基地名称
	private String questioner;//提问人
	private String replyNum;//回复数量
	private String strDate;//日期 （“yyyy-MM-dd”）
	private List<AgroFileInfo> fileList;//图片附件list
	private List<AgroFileInfo> auditList;//录音list

	public Communication() {
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
	
	public String getCommunicationTypeId() {
		return this.communicationTypeId;
	}
	public void setCommunicationTypeId(String communicationTypeId) {
		this.communicationTypeId = communicationTypeId;
	}
	
	public String getBaseId() {
		return this.baseId;
	}
	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
	public String getProductionBatchId() {
		return this.productionBatchId;
	}
	public void setProductionBatchId(String productionBatchId) {
		this.productionBatchId = productionBatchId;
	}
	
	public String getCommunicationTitle() {
		return this.communicationTitle;
	}
	public void setCommunicationTitle(String communicationTitle) {
		this.communicationTitle = communicationTitle;
	}
	
	public String getCommunicationDescription() {
		return this.communicationDescription;
	}
	public void setCommunicationDescription(String communicationDescription) {
		this.communicationDescription = communicationDescription;
	}
	
	public String getCommunicationStatus() {
		return this.communicationStatus;
	}
	public void setCommunicationStatus(String communicationStatus) {
		this.communicationStatus = communicationStatus;
	}

	public String getCommunicationBoutique() {
		return communicationBoutique;
	}

	public void setCommunicationBoutique(String communicationBoutique) {
		this.communicationBoutique = communicationBoutique;
	}

	public String getCommunicationTop() {
		return communicationTop;
	}

	public void setCommunicationTop(String communicationTop) {
		this.communicationTop = communicationTop;
	}

	public BigInteger getClickNum() {
		return clickNum;
	}

	public void setClickNum(BigInteger clickNum) {
		this.clickNum = clickNum;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getExpertId() {
		return expertId;
	}

	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}

	@Transient
	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	@Transient
	public String getQuestioner() {
		return questioner;
	}

	public void setQuestioner(String questioner) {
		this.questioner = questioner;
	}

	@Transient
	public List<AgroFileInfo> getFileList() {
		return fileList;
	}

	public void setFileList(List<AgroFileInfo> fileList) {
		this.fileList = fileList;
	}

	@Transient
	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	@Transient
	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	@Transient
	public List<AgroFileInfo> getAuditList() {
		return auditList;
	}

	public void setAuditList(List<AgroFileInfo> auditList) {
		this.auditList = auditList;
	}

	@Transient
	public String getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(String replyNum) {
		this.replyNum = replyNum;
	}

	@Transient
	public String getStrDate() {
		strDate = DateUtils.formatDate(this.getCreateTime());
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}
	
	
}


