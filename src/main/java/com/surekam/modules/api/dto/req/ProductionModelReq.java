/**
 * 
 */
package com.surekam.modules.api.dto.req;

import java.io.Serializable;

/**
 * Title: ProductionModelReq Description:生产模式请求参数
 * 
 * @author tangjun
 * @date 2019年5月27日
 */
public class ProductionModelReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 品种ID
	 */
	private String productId;
	/**
	 * 生产名称
	 */
	private String productionName;
	/**
	 * 生产编码
	 */
	private String productionCode;
	/**
	 * 生产模式信息
	 */
	private String productionModelInfo;

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

	public String getProductionName() {
		return productionName;
	}

	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}

	public String getProductionCode() {
		return productionCode;
	}

	public void setProductionCode(String productionCode) {
		this.productionCode = productionCode;
	}

	public String getProductionModelInfo() {
		return productionModelInfo;
	}

	public void setProductionModelInfo(String productionModelInfo) {
		this.productionModelInfo = productionModelInfo;
	}

}
