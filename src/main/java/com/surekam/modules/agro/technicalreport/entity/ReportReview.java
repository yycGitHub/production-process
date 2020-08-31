package com.surekam.modules.agro.technicalreport.entity;

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

import java.lang.String;
import java.util.List;

/**
 * 汇报评审信息类Entity
 * @author xy
 * @version 2019-07-08
 */
@Entity
@Table(name = "t_agro_report_review")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReportReview extends XGXTEntity<ReportReview> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String officeId;//公司id
	private String baseId;//基地id
	private String reportId;//汇报表id
	private String reportType;//汇报表类型
	private String score;//评分
	private String review;//评审
	private String reviewTime;//评审时间
	private String reviewUserId;//评审人
	private String reviewStatus;//评审状态
	private String attachments;//是否有附件
	
	private List<String> rIds;

	public ReportReview() {
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
	
	public String getReportId() {
		return this.reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	public String getReportType() {
		return this.reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	public String getScore() {
		return this.score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
	public String getReview() {
		return this.review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	
	public String getReviewTime() {
		return this.reviewTime;
	}
	public void setReviewTime(String reviewTime) {
		this.reviewTime = reviewTime;
	}
	
	public String getReviewUserId() {
		return this.reviewUserId;
	}
	public void setReviewUserId(String reviewUserId) {
		this.reviewUserId = reviewUserId;
	}
	
	public String getReviewStatus() {
		return this.reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	
	public String getAttachments() {
		return this.attachments;
	}
	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	@Transient
	public List<String> getrIds() {
		return rIds;
	}

	public void setrIds(List<String> rIds) {
		this.rIds = rIds;
	}	
	
}


