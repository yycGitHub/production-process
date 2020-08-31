/**
 * 
 */
package com.surekam.modules.api.dto.req;

import java.io.Serializable;

/**
 * Title: UploadAuditRecordReq Description: 上传审核记录表请求参数
 * 
 * @author tangjun
 * @date 2019年7月15日
 */
public class UploadAuditRecordReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 溯源批次号
	 */
	private String sybatchCode;
	/**
	 * 公司ID
	 */
	private String officeId;
	/**
	 * 审核状态 1-未审 2-已审未通过 3-已审通过
	 */
	private String auditStatus;
	/**
	 * 审核时间
	 */
	private String auditTime;
	/**
	 * audit_opinions 原因
	 */
	private String auditOpinions;
	/**
	 * 登录名
	 */
	private String loginName;
	/**
	 * 姓名
	 */
	private String userNmae;

	public String getSybatchCode() {
		return sybatchCode;
	}

	public void setSybatchCode(String sybatchCode) {
		this.sybatchCode = sybatchCode;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditOpinions() {
		return auditOpinions;
	}

	public void setAuditOpinions(String auditOpinions) {
		this.auditOpinions = auditOpinions;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserNmae() {
		return userNmae;
	}

	public void setUserNmae(String userNmae) {
		this.userNmae = userNmae;
	}

}
