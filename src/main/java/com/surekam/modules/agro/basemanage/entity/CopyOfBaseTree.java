package com.surekam.modules.agro.basemanage.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.google.common.collect.Lists;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

/**
 * 基地管理Entity
 * 
 * @author tangjun
 * @version 2019-04-10
 */
@Entity
@Table(name = "t_agro_base_tree")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CopyOfBaseTree extends XGXTEntity<CopyOfBaseTree> {

	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private CopyOfBaseTree parent; // 父级编号
	private String parentIds;// 所有上级ID，用,隔开
	private String name;// 名称
	private String officeId;// 公司ID
	private String sort;// 排序
	private String longitude;// 经度
	private String latitude;// 纬度
	private String province;// 省
	private String city;// 市
	private String area;// 区县
	private String address;// 详细地址
	private String acreage;// 面积
	private String isOffice; // 是否是公司还是基地 0：为是 1：基地
	private String showUrl; // 展示URL
	private String baseImg; // 基地图片地址
	private String code; // 编码
	private String remarks; // 功能介绍
	private String baseCode; // 基地编码
	/**
	 * 养殖能力
	 */
	private String cultivationAbility;
	/**
	 * 养殖单位
	 */
	private String cultivationUnit;

	public CopyOfBaseTree() {
		super();
	}

	@PrePersist
	public void prePersist() {
		this.id = IdGen.uuid();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public CopyOfBaseTree getParent() {
		return parent;
	}

	public void setParent(CopyOfBaseTree parent) {
		this.parent = parent;
	}
	@Id
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "parent_ids")
	public String getParentIds() {
		return this.parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getSort() {
		return this.sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAcreage() {
		return acreage;
	}

	public void setAcreage(String acreage) {
		this.acreage = acreage;
	}

	@Transient
	public String getIsOffice() {
		return isOffice;
	}

	public void setIsOffice(String isOffice) {
		this.isOffice = isOffice;
	}

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

}
