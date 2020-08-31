package com.surekam.modules.api.dto.req;

import java.io.Serializable;
import java.util.List;

public class ExpertsReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 专家名称
	 */
	private String expertName;
	/**
	 * 用戶iD
	 */
	private String userId;
	/**
	 * 专家描述
	 */
	private String expertDescription;
	/**
	 * 性别
	 */
	private String expertSex;
	/**
	 * 出生时间
	 */
	private String expertBirthDate;
	/**
	 * 职称
	 */
	private String expertTitle;
	/**
	 * 专业领域
	 */
	private String professionalField;
	/**
	 * 从业开始时间
	 */
	private String workingStartTime;
	/**
	 * 操作状态，数据字典：A增加； U更新；D删除
	 */
	protected String states;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 创建人
	 */
	private String createUserId;
	/**
	 * 最后修改时间
	 */
	private String updateTime;
	/**
	 * 更新人
	 */
	private String updateUserId;
	/**
	 * 擅长问题
	 */
	private String goodProblem;
	/**
	 * 用户信息
	 */
	private UserReq userReq;
	
	/**
	 * 所属平台
	 */
	private String[] platform;
	
	/**
	 * 团队负责人
	 */
	private String leader;

	private List<ProductLibraryTreeListReq> pltListReq;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getExpertDescription() {
		return expertDescription;
	}

	public void setExpertDescription(String expertDescription) {
		this.expertDescription = expertDescription;
	}

	public String getExpertSex() {
		return expertSex;
	}

	public void setExpertSex(String expertSex) {
		this.expertSex = expertSex;
	}

	public String getExpertBirthDate() {
		return expertBirthDate;
	}

	public void setExpertBirthDate(String expertBirthDate) {
		this.expertBirthDate = expertBirthDate;
	}

	public String getExpertTitle() {
		return expertTitle;
	}

	public void setExpertTitle(String expertTitle) {
		this.expertTitle = expertTitle;
	}

	public String getProfessionalField() {
		return professionalField;
	}

	public void setProfessionalField(String professionalField) {
		this.professionalField = professionalField;
	}

	public String getWorkingStartTime() {
		return workingStartTime;
	}

	public void setWorkingStartTime(String workingStartTime) {
		this.workingStartTime = workingStartTime;
	}

	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
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

	public UserReq getUserReq() {
		return userReq;
	}

	public void setUserReq(UserReq userReq) {
		this.userReq = userReq;
	}

	public List<ProductLibraryTreeListReq> getPltListReq() {
		return pltListReq;
	}

	public void setPltListReq(List<ProductLibraryTreeListReq> pltListReq) {
		this.pltListReq = pltListReq;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getGoodProblem() {
		return goodProblem;
	}

	public void setGoodProblem(String goodProblem) {
		this.goodProblem = goodProblem;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String[] getPlatform() {
		return platform;
	}

	public void setPlatform(String[] platform) {
		this.platform = platform;
	}
	

}
