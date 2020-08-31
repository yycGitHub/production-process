package com.surekam.modules.sys.entity.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.sys.entity.Menu;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.Role;

public class UserVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	@ApiModelProperty(hidden = true)
	private OfficeVo company = new OfficeVo();
	@ApiModelProperty(hidden = true)
	private OfficeVo office = new OfficeVo();
	private String companyId;
	private String officeId;
	private String loginName;// 登录名
	private String password;
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
	private List<String> roleListIds = new ArrayList<String>();
	@ApiModelProperty(hidden = true)
	private List<RoleVo> roleList = Lists.newArrayList(); // 拥有角色列表
	@ApiModelProperty(hidden = true)
	private List<MenuVo> menuList = Lists.newArrayList(); // 拥有资源列表
	private Experts experts; // 专家）
	private String remarks; // 备注
	private Date createDate = new Date();// 创建日期
	private Date updateDate;// 更新日期
	private String delFlag; // 删除标记（0：正常；1：删除；）
	private boolean isDisable = false;// 是否不可选中
	private boolean isChecked = false;// 是否选中

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName.trim();
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
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

	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public boolean isDisable() {
		return isDisable;
	}

	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public OfficeVo getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		if (company != null) {
			BeanUtils.copyProperties(company, this.company);
		} else {
			this.company = new OfficeVo();
		}
	}

	public OfficeVo getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		if (office != null) {
			BeanUtils.copyProperties(office, this.office);
		} else {
			this.office = new OfficeVo();
		}
	}

	public List<RoleVo> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		if (roleList != null && roleList.size() > 0) {
			for (Role role : roleList) {
				RoleVo roleVo = new RoleVo();
				BeanUtils.copyProperties(role, roleVo);
				roleVo.setMenuList(null);
				this.roleList.add(roleVo);
			}
		} else {
			this.roleList = new ArrayList<RoleVo>();
		}
	}

	public List<MenuVo> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		if (menuList != null && menuList.size() > 0) {
			for (Menu menu : menuList) {
				MenuVo menuVo = new MenuVo();
				BeanUtils.copyProperties(menu, menuVo);
				menuVo.setChildList(null);
				this.menuList.add(menuVo);
			}
		} else {
			this.menuList = new ArrayList<MenuVo>();
		}
	}

	public List<String> getRoleListIds() {
		return roleListIds;
	}

	public void setRoleListIds(List<String> roleListIds) {
		this.roleListIds = roleListIds;
	}

	@Transient
	public Experts getExperts() {
		return experts;
	}

	public void setExperts(Experts experts) {
		this.experts = experts;
	}

}