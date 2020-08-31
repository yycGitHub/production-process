package com.surekam.modules.agro.technicalreport.entity;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.modules.agro.file.entity.AgroFileInfo;
import java.lang.String;
import java.util.List;

/**
 * 汇报评审视图Entity
 * @author xy
 * @version 2019-07-12
 */
public class ReportReviewVoReq extends XGXTEntity<ReportReviewVoReq> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键id
	private String workDone;//完成的工作
	private String plannedWork;//计划的工作
	private String otherMatters;//其他事项
	private String reportUserId;//汇报给谁
	private String reportDate;//汇报日期
	private String officeId;//公司id
	private String baseId;//基地id
	private String reportingType;//汇报类型
	private String attachments;//是否有附件
	private String auditStatus;//审核状态
	private String wstates;//更新标识
	private String wid;//主键
	private String wscore;//评分
	private String wreview;//评审
	private String wreviewTime;//评审时间
	private String wreportUserId;//评审人
	private String wreviewStatus;//评审状态
	private String wofficeId;//公司id
	private String wbaseId;//基地id
	private String createUserName;//姓名
	private String reportUserName;//汇报给谁
	
	private List<AgroFileInfo> fileList;//文件list
	private List<AgroFileInfo> auditList;//录音list
	private List<AgroFileInfo> photoList;//图片list
	private List<AgroFileInfo> videoList;//视频list

	public ReportReviewVoReq() {
		super();
	}
	
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getWorkDone() {
		return this.workDone;
	}
	public void setWorkDone(String workDone) {
		this.workDone = workDone;
	}
	
	public String getPlannedWork() {
		return this.plannedWork;
	}
	public void setPlannedWork(String plannedWork) {
		this.plannedWork = plannedWork;
	}
	
	public String getOtherMatters() {
		return this.otherMatters;
	}
	public void setOtherMatters(String otherMatters) {
		this.otherMatters = otherMatters;
	}
	
	public String getReportUserId() {
		return this.reportUserId;
	}
	public void setReportUserId(String reportUserId) {
		this.reportUserId = reportUserId;
	}
	
	public String getReportDate() {
		return this.reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	
	public String getOfficeId() {
		return this.officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	
	public String getBaseId() {
		return this.baseId;
	}
	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}
	
	public String getReportingType() {
		return this.reportingType;
	}
	public void setReportingType(String reportingType) {
		this.reportingType = reportingType;
	}
	
	public String getAttachments() {
		return this.attachments;
	}
	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}
	
	public String getAuditStatus() {
		return this.auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	public String getWstates() {
		return this.wstates;
	}
	public void setWstates(String wstates) {
		this.wstates = wstates;
	}
	
	public String getWid() {
		return this.wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	
	public String getWscore() {
		return this.wscore;
	}
	public void setWscore(String wscore) {
		this.wscore = wscore;
	}
	
	public String getWreview() {
		return this.wreview;
	}
	public void setWreview(String wreview) {
		this.wreview = wreview;
	}
	
	public String getWreviewTime() {
		return this.wreviewTime;
	}
	public void setWreviewTime(String wreviewTime) {
		this.wreviewTime = wreviewTime;
	}
	
	public String getWreportUserId() {
		return this.wreportUserId;
	}
	public void setWreportUserId(String wreportUserId) {
		this.wreportUserId = wreportUserId;
	}
	
	public String getWreviewStatus() {
		return this.wreviewStatus;
	}
	public void setWreviewStatus(String wreviewStatus) {
		this.wreviewStatus = wreviewStatus;
	}
	
	public String getWofficeId() {
		return this.wofficeId;
	}
	public void setWofficeId(String wofficeId) {
		this.wofficeId = wofficeId;
	}
	
	public String getWbaseId() {
		return this.wbaseId;
	}
	public void setWbaseId(String wbaseId) {
		this.wbaseId = wbaseId;
	}
	
	public String getCreateUserName() {
		return this.createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public List<AgroFileInfo> getFileList() {
		return fileList;
	}

	public void setFileList(List<AgroFileInfo> fileList) {
		this.fileList = fileList;
	}

	public List<AgroFileInfo> getAuditList() {
		return auditList;
	}

	public void setAuditList(List<AgroFileInfo> auditList) {
		this.auditList = auditList;
	}

	public List<AgroFileInfo> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<AgroFileInfo> photoList) {
		this.photoList = photoList;
	}

	public List<AgroFileInfo> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<AgroFileInfo> videoList) {
		this.videoList = videoList;
	}

	public String getReportUserName() {
		return reportUserName;
	}

	public void setReportUserName(String reportUserName) {
		this.reportUserName = reportUserName;
	}
	
}


