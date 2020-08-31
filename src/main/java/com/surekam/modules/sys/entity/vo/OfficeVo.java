package com.surekam.modules.sys.entity.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.surekam.modules.agro.basemanage.entity.vo.BaseTreeVo;
import com.surekam.modules.sys.entity.Office;

public class OfficeVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;

	private String parentId;
	private String parentName;

	private String areaId;
	private String areaName;

	private String parentIds; // 所有父级编号
	private String code; // 机构编码
	private String name; // 机构名称
	private String type; // 机构类型（1：公司；2：部门；3：小组）
	private String grade; // 机构等级（1：一级；2：二级；3：三级；4：四级）
	private String address; // 联系地址
	private String zipCode; // 邮政编码
	private String master; // 负责人
	private String phone; // 电话
	private String fax; // 传真
	private String email; // 邮箱
	private String officeHomePage; // 企业主页
	private String productionFunction; // 生产功能

	private String delFlag;

	private boolean isLeaf = false;

	// 新加字段
	private String officeLogo; // 企业logo
	private String operatorName; // 运营者姓名
	private String operatorIdnumber; // 运营者身份证号

	private String typeName;// 机构类型（1：公司；2：部门；3：小组） 非数据库字段
	private List<OfficeVo> childList = Lists.newArrayList();// 拥有子机构列表
	private BaseTreeVo btVo;

	public BaseTreeVo getBtVo() {
		return btVo;
	}

	public void setBtVo(BaseTreeVo btVo) {
		this.btVo = btVo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public List<OfficeVo> getChildList() {
		return childList;
	}

	public String getOfficeLogo() {
		return officeLogo;
	}

	public void setOfficeLogo(String officeLogo) {
		this.officeLogo = officeLogo;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getProductionFunction() {
		return productionFunction;
	}

	public void setProductionFunction(String productionFunction) {
		this.productionFunction = productionFunction;
	}

	public String getOperatorIdnumber() {
		return operatorIdnumber;
	}

	public String getOfficeHomePage() {
		return officeHomePage;
	}

	public void setOfficeHomePage(String officeHomePage) {
		this.officeHomePage = officeHomePage;
	}

	public void setOperatorIdnumber(String operatorIdnumber) {
		this.operatorIdnumber = operatorIdnumber;
	}

	public void setChildList(List<Office> childList) {
		if (childList != null && childList.size() > 0) {
			for (Office office : childList) {
				OfficeVo officeVo = new OfficeVo();
				BeanUtils.copyProperties(office, officeVo);
				this.childList.add(officeVo);
			}
		} else {
			this.childList = new ArrayList<OfficeVo>();
		}
	}
}