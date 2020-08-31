/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.modules.sys.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.surekam.common.persistence.DataEntity;
import com.surekam.common.utils.Collections3;
import com.surekam.common.utils.excel.annotation.ExcelField;
import com.surekam.common.utils.excel.fieldtype.RoleListType;

/**
 * 用户Entity
 * 
 * @author sureserve
 * @version 2013-5-15
 */
@Entity
@Table(name = "SYS_USER")
@DynamicInsert
@DynamicUpdate
public class User extends DataEntity<User> {

	private static final long serialVersionUID = 1L;
	private Office company; // 归属公司
	private Office office; // 归属部门
	private String loginName;// 登录名
	private String password;// 密码
	private String no; // 工号
	private String name; // 姓名
	private String email; // 邮箱
	private String phone; // 电话
	private String userType;// 用户类型
	private String loginIp; // 最后登陆IP
	private Date loginDate; // 最后登陆日期
	private String openId; // 微信openId
	private String unionId;// 微信unionId
	private String userImg;

	private String cropName;
	private String cropPostion;
	private String cropAddress;
	private String loginCode; // COOKIE身份验证值

	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	// private List<Menu> menuList = Lists.newArrayList(); // 拥有资源列表
	private List<Integer> categoryIdList = Lists.newArrayList();// 拥有栏目列表
	private List<Group> groupList = Lists.newArrayList();// 拥有组列表

	private boolean isDisable = false;// 是否不可选中
	private boolean isChecked = false;// 是否选中
	private String checkType;// 选择类型 assignee单选 assignees多选

	private boolean isPlateAdmin = false;// 是否版主
	private boolean isMember = false;// 是否组员
	private boolean isMaster = false;// 是否组长
	private String groupRoleDesc;// 组内角色
	private String validationCode;// 验证码临时存储
	private String userToken;// 用户通讯token

	private String roleName;// 角色名称
	private String baseNum;// 基地数量
	private String batchNum;// 批次数量
	private String companyNum;// 公司数量
	private String companyName;// 公司名称
	private String expertTitle;
	private String expertDescription;// 专家描述
	private String expertSex;// 性别
	private String expertBirthDate;// 出生时间

	public User() {
		super();
	}

	public User(String id) {
		this();
		this.id = id;
	}

	public User(String id, String loginName) {
		this();
		this.loginName = loginName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull(message = "归属公司不能为空")
	@ExcelField(title = "归属公司", align = 2, sort = 20)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull(message = "归属部门不能为空")
	@ExcelField(title = "归属部门", align = 2, sort = 25)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Length(min = 1, max = 100)
	@ExcelField(title = "登录名", align = 2, sort = 30)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonIgnore
	@Length(min = 1, max = 100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(min = 1, max = 100)
	@ExcelField(title = "姓名", align = 2, sort = 40)
	public String getName() {
		return name;
	}

	@ExcelField(title = "工号", align = 2, sort = 45)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ExcelField(title = "电话", align = 2, sort = 60)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Transient
	@ExcelField(title = "备注", align = 1, sort = 900)
	public String getRemarks() {
		return remarks;
	}

	@JsonIgnore
	@ExcelField(title = "用户类型", align = 2, sort = 80, dictType = "sys_user_type")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Transient
	@ExcelField(title = "创建时间", type = 0, align = 1, sort = 90)
	public Date getCreateDate() {
		return createDate;
	}

	@ExcelField(title = "最后登录IP", type = 1, align = 1, sort = 100)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title = "最后登录日期", type = 1, align = 1, sort = 110)
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	@Where(clause = "del_flag='" + DEL_FLAG_NORMAL + "'")
	@OrderBy("id")
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@ExcelField(title = "拥有角色", align = 1, sort = 800, fieldType = RoleListType.class)
	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@Transient
	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	@Transient
	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}

	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	@Transient
	@JsonIgnore
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "name", ", ");
	}

	@Transient
	public boolean isAdmin() {
		return isAdmin(this.id);
	}

	@Transient
	public static boolean isAdmin(String id) {
		return id != null && id.equals("1");
	}

	@Transient
	public String getActivitiGroupName() {
		return ObjectUtils.toString(getId());
	}

	@Transient
	public boolean getIsDisable() {
		return isDisable;
	}

	@Transient
	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}

	@Transient
	public boolean getIsChecked() {
		return isChecked;
	}

	@Transient
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Transient
	public boolean getIsPlateAdmin() {
		return isPlateAdmin;
	}

	@Transient
	public void setPlateAdmin(boolean isPlateAdmin) {
		this.isPlateAdmin = isPlateAdmin;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getCropName() {
		return cropName;
	}

	public void setCropName(String cropName) {
		this.cropName = cropName;
	}

	public String getCropPostion() {
		return cropPostion;
	}

	public void setCropPostion(String cropPostion) {
		this.cropPostion = cropPostion;
	}

	public String getCropAddress() {
		return cropAddress;
	}

	public void setCropAddress(String cropAddress) {
		this.cropAddress = cropAddress;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	@Transient
	public String getCheckType() {
		return checkType;
	}

	@Transient
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}

	/**
	 * @return the categoryIdList
	 */
	@Transient
	public List<Integer> getCategoryIdList() {
		return categoryIdList;
	}

	/**
	 * @param categoryIdList
	 *            the categoryIdList to set
	 */
	@Transient
	public void setCategoryIdList(List<Integer> categoryIdList) {
		this.categoryIdList = categoryIdList;
	}

	/**
	 * @return the groupList
	 */
	@Transient
	public List<Group> getGroupList() {
		return groupList;
	}

	/**
	 * @param groupList
	 *            the groupList to set
	 */
	@Transient
	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	/**
	 * @return the isMember
	 */
	@Transient
	public boolean getIsMember() {
		return isMember;
	}

	/**
	 * @param isMember
	 *            the isMember to set
	 */
	@Transient
	public void setMember(boolean isMember) {
		this.isMember = isMember;
	}

	/**
	 * @return the isMaster
	 */
	@Transient
	public boolean getIsMaster() {
		return isMaster;
	}

	/**
	 * @param isMaster
	 *            the isMaster to set
	 */
	@Transient
	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	/**
	 * @return the groupRoleDesc
	 */
	@Transient
	public String getGroupRoleDesc() {
		if (this.isMaster) {
			return "组长";
		} else {
			if (this.isMember) {
				return "成员";
			} else {
				return "无";
			}
		}
	}

	/**
	 * @param groupRoleDesc
	 *            the groupRoleDesc to set
	 */
	@Transient
	public void setGroupRoleDesc(String groupRoleDesc) {
		this.groupRoleDesc = groupRoleDesc;
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}

	@Transient
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Transient
	public String getBaseNum() {
		return baseNum;
	}

	public void setBaseNum(String baseNum) {
		this.baseNum = baseNum;
	}

	@Transient
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Transient
	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	@Transient
	public String getExpertTitle() {
		return expertTitle;
	}

	public void setExpertTitle(String expertTitle) {
		this.expertTitle = expertTitle;
	}

	@Transient
	public String getExpertDescription() {
		return expertDescription;
	}

	public void setExpertDescription(String expertDescription) {
		this.expertDescription = expertDescription;
	}

	@Transient
	public String getExpertSex() {
		return expertSex;
	}

	public void setExpertSex(String expertSex) {
		this.expertSex = expertSex;
	}

	@Transient
	public String getExpertBirthDate() {
		return expertBirthDate;
	}

	public void setExpertBirthDate(String expertBirthDate) {
		this.expertBirthDate = expertBirthDate;
	}

	@Transient
	public String getCompanyNum() {
		return companyNum;
	}

	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}

}