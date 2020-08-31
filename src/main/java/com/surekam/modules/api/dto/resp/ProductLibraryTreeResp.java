/**
 * 
 */
package com.surekam.modules.api.dto.resp;

import java.io.Serializable;

/**
 * Title: ProductLibraryTreeResp Description: 快捷品种
 * 
 * @author tangjun
 * @date 2019年9月10日
 */
public class ProductLibraryTreeResp implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 分类ID-非数据库字段
	 */
	private String classificationId;
	/**
	 * 公司ID
	 */
	private String officeId;
	/**
	 * 公司名称
	 */
	private String officeName;
	/**
	 * 品种ID
	 */
	private String productId;
	/**
	 * 品种名称
	 */
	private String productName;
	/**
	 * 标准ID
	 */
	private String standardsId;
	/**
	 * 标准名称
	 */
	private String standardsName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(String classificationId) {
		this.classificationId = classificationId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getStandardsId() {
		return standardsId;
	}

	public void setStandardsId(String standardsId) {
		this.standardsId = standardsId;
	}

	public String getStandardsName() {
		return standardsName;
	}

	public void setStandardsName(String standardsName) {
		this.standardsName = standardsName;
	}

}
