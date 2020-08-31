package com.surekam.modules.sys.entity.vo;

import java.io.Serializable;
import java.util.List;

public class ProductRelationVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String officeId;
	private String type;
	private List<String> productIds;
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getProductIds() {
		return productIds;
	}
	public void setProductIds(List<String> productIds) {
		this.productIds = productIds;
	}
	
	
}