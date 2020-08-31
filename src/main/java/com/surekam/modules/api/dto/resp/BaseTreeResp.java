package com.surekam.modules.api.dto.resp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.sensorsetup.entity.SensorSetup;

/**
 * Title: BaseTreeResp Description: 基地返回对象
 * 
 * @author tangjun
 * @date 2019年4月30日
 */
public class BaseTreeResp implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 上级ID
	 */
	private String parentId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 公司ID
	 */
	private String officeId;
	/**
	 * 排序
	 */
	private String sort;
	/**
	 * 经度
	 */
	private String longitude;
	/**
	 * 纬度
	 */
	private String latitude;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 区县
	 */
	private String area;
	/**
	 * 详细地址
	 */
	private String address;
	/**
	 * 管理员
	 */
	private String isAdmin;
	/**
	 * 技术员
	 */
	private String artisan;
	/**
	 * 面积
	 */
	private String acreage;
	/**
	 * 上级名称
	 */
	private String parentName;
	/**
	 * 展示URL
	 */
	private String showUrl;
	/**
	 * 基地图片地址
	 */
	private String baseImg;
	/**
	 * 养殖能力
	 */
	private String cultivationAbility;
	/**
	 * 养殖单位
	 */
	private String cultivationUnit;
	/**
	 * 传感器
	 */
	private List<SensorSetup> ssList = new ArrayList<SensorSetup>();
	private String code; // 编码
	private String remarks; // 功能介绍
	private String baseCode; // 基地编码

	private String parentNames;
	/**
	 * 专家
	 */
	private List<Experts> expertsList = new ArrayList<Experts>();

	public String getShowUrl() {
		return showUrl;
	}

	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}

	public String getBaseImg() {
		return baseImg;
	}

	public void setBaseImg(String baseImg) {
		this.baseImg = baseImg;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getAcreage() {
		return acreage;
	}

	public void setAcreage(String acreage) {
		this.acreage = acreage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getArtisan() {
		return artisan;
	}

	public void setArtisan(String artisan) {
		this.artisan = artisan;
	}

	public String getParentNames() {
		return parentNames;
	}

	public void setParentNames(String parentNames) {
		this.parentNames = parentNames;
	}

	public List<SensorSetup> getSsList() {
		return ssList;
	}

	public void setSsList(List<SensorSetup> ssList) {
		this.ssList = ssList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBaseCode() {
		return baseCode;
	}

	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}

	public String getCultivationAbility() {
		return cultivationAbility;
	}

	public void setCultivationAbility(String cultivationAbility) {
		this.cultivationAbility = cultivationAbility;
	}

	public String getCultivationUnit() {
		return cultivationUnit;
	}

	public void setCultivationUnit(String cultivationUnit) {
		this.cultivationUnit = cultivationUnit;
	}

	public List<Experts> getExpertsList() {
		return expertsList;
	}

	public void setExpertsList(List<Experts> expertsList) {
		this.expertsList = expertsList;
	}

}
