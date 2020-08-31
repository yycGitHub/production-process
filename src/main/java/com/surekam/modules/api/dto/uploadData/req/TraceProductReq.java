/**
 * 
 */
package com.surekam.modules.api.dto.uploadData.req;

import java.io.Serializable;
import java.util.List;

/**
 * Title: TraceProductReq Description: 产品信息
 * 
 * @author tangjun
 * @date 2019年7月16日
 */
public class TraceProductReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 产品名称
	 */
	private String productName;
	/**
	 * 产品简介
	 */
	private String productDiscription;
	/**
	 * 主题ID
	 */
	private String themeId;
	/**
	 * 产品图片，base64位格式
	 */
	private String productPic;

	/**
	 * 产品模型列表
	 */
	private List<TraceModelListReq> traceModelList;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDiscription() {
		return productDiscription;
	}

	public void setProductDiscription(String productDiscription) {
		this.productDiscription = productDiscription;
	}

	public String getThemeId() {
		return themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public String getProductPic() {
		return productPic;
	}

	public void setProductPic(String productPic) {
		this.productPic = productPic;
	}

	public List<TraceModelListReq> getTraceModelList() {
		return traceModelList;
	}

	public void setTraceModelList(List<TraceModelListReq> traceModelList) {
		this.traceModelList = traceModelList;
	}

}
