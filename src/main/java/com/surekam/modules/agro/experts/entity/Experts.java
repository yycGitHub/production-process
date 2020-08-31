package com.surekam.modules.agro.experts.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
import com.surekam.modules.agro.basemanage.entity.CopyOfBaseTree;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.api.dto.resp.ExpertsUserInfoResp;

/**
 * 专家信息Entity
 * 
 * @author xy
 * @version 2019-04-09
 */
@Entity
@Table(name = "t_agro_experts")
@DynamicInsert
@DynamicUpdate
public class Experts extends XGXTEntity<Experts> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String expertName;// 专家名称
	private String userId;// 用戶iD
	private String expertDescription;// 专家描述
	private String expertSex;// 性别
	private String expertBirthDate;// 出生时间
	private String expertTitle;// 职称
	private String professionalField;// 专业领域
	private String workingStartTime;// 从业开始时间
	private String leader;//是否是团队负责人 1：是，0：否

	private String photo;// 照片
	private List<ProductLibraryTree> productLibraryList;// 专家领域
	private List<ExpertsGoodproblem> expertsGoodproblemList;// 专家擅长问题
	private List<CopyOfBaseTree> baseTree;// 专家服务基地
	private ExpertsUserInfoResp resp; // 专家信息

	private String productData;//给明姐拼接的专家领域信息
	private String goodproblemData;//给明姐拼接的专家擅长问题信息
	private String userCode;//专家登录账号
	private String createRoomNumber;//视频房间号
	private String isInRoom;//是否在房间（1-在房间，0-不在房间）
	private Map<String,String> listMap;// 月份统计
	private String userToken;// 用户token
	private String platform;//所属平台
	private String platformName;//所属平台名称
	private String userImg;
	private String phone;
	
	public Experts() {
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

	public String getExpertName() {
		return this.expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	public String getExpertDescription() {
		return this.expertDescription;
	}

	public void setExpertDescription(String expertDescription) {
		this.expertDescription = expertDescription;
	}

	public String getExpertSex() {
		return this.expertSex;
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
		return this.expertTitle;
	}

	public void setExpertTitle(String expertTitle) {
		this.expertTitle = expertTitle;
	}

	@Transient
	public String getProfessionalField() {
		return this.professionalField;
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

	@Transient
	public List<ProductLibraryTree> getProductLibraryList() {
		return productLibraryList;
	}

	public void setProductLibraryList(List<ProductLibraryTree> productLibraryList) {
		this.productLibraryList = productLibraryList;
	}

	@Transient
	public List<ExpertsGoodproblem> getExpertsGoodproblemList() {
		return expertsGoodproblemList;
	}

	public void setExpertsGoodproblemList(List<ExpertsGoodproblem> expertsGoodproblemList) {
		this.expertsGoodproblemList = expertsGoodproblemList;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Transient
	public ExpertsUserInfoResp getResp() {
		return resp;
	}

	public void setResp(ExpertsUserInfoResp resp) {
		this.resp = resp;
	}

	@Transient
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Transient
	public String getProductData() {
		return productData;
	}

	public void setProductData(String productData) {
		this.productData = productData;
	}

	@Transient
	public String getGoodproblemData() {
		return goodproblemData;
	}

	public void setGoodproblemData(String goodproblemData) {
		this.goodproblemData = goodproblemData;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Transient
	public String getCreateRoomNumber() {
		return createRoomNumber;
	}

	public void setCreateRoomNumber(String createRoomNumber) {
		this.createRoomNumber = createRoomNumber;
	}

	@Transient
	public Map<String, String> getListMap() {
		return listMap;
	}

	public void setListMap(Map<String, String> listMap) {
		this.listMap = listMap;
	}

	@Transient
	public String getIsInRoom() {
		return isInRoom;
	}

	public void setIsInRoom(String isInRoom) {
		this.isInRoom = isInRoom;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	@Transient
	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@Transient
	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Transient
	public List<CopyOfBaseTree> getBaseTree() {
		return baseTree;
	}

	public void setBaseTree(List<CopyOfBaseTree> baseTree) {
		this.baseTree = baseTree;
	}
	
}
