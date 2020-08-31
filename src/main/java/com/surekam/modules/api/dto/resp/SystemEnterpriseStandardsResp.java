package com.surekam.modules.api.dto.resp;

import java.io.Serializable;
import java.util.List;

/**
 * Title: SystemEnterpriseStandardsResp Description: 标准库树状返回对象
 * 
 * @author tangjun
 * @date 2019年5月10日
 */
public class SystemEnterpriseStandardsResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 品种主键
	 */
	private String productId;
	/**
	 * 复制来源标准库ID
	 */
	private String copyFromSystemStandardsId;
	/**
	 * 标准库名称
	 */
	private String name;
	/**
	 * 企业主键
	 */
	private String officeId;
	/**
	 * 标准类型(作业标准与环境标准)
	 */
	private String standardsType;
	/**
	 * 版本库描述
	 */
	private String standardsDescription;
	/**
	 * 标准类型
	 */
	private String standardType;

	private List<ProductGrowthCycleTreeResp> childList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCopyFromSystemStandardsId() {
		return copyFromSystemStandardsId;
	}

	public void setCopyFromSystemStandardsId(String copyFromSystemStandardsId) {
		this.copyFromSystemStandardsId = copyFromSystemStandardsId;
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

	public String getStandardsType() {
		return standardsType;
	}

	public void setStandardsType(String standardsType) {
		this.standardsType = standardsType;
	}

	public String getStandardsDescription() {
		return standardsDescription;
	}

	public void setStandardsDescription(String standardsDescription) {
		this.standardsDescription = standardsDescription;
	}

	public String getStandardType() {
		return standardType;
	}

	public void setStandardType(String standardType) {
		this.standardType = standardType;
	}

	public List<ProductGrowthCycleTreeResp> getChildList() {
		return childList;
	}

	public void setChildList(List<ProductGrowthCycleTreeResp> childList) {
		this.childList = childList;
	}

}
