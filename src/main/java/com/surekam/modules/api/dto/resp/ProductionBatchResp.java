package com.surekam.modules.api.dto.resp;

import java.io.Serializable;

public class ProductionBatchResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 种类名称
	 */
	private String productCategoryName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

}
