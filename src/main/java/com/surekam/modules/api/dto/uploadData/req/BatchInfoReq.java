/**
 * 
 */
package com.surekam.modules.api.dto.uploadData.req;

import java.io.Serializable;

/**
 * Title: BatchInfoReq Description: 上传批次信息
 * 
 * @author tangjun
 * @date 2019年7月16日
 */
public class BatchInfoReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否底层包装，0-否，1-是
	 */
	private String isBottomPacking;
	/**
	 * 批次号
	 */
	private String batchCode;
	/**
	 * 溯源码
	 */
	private String traceCode;
	/**
	 * 企业ID
	 */
	private String officeId;
	/**
	 * 系统来源（0-本系统，1-农事，2-加工）
	 */
	private String sysId;
	/**
	 * 包装类型（1-底层标签，2-上级标签，3是上上级标签）
	 */
	private String packType;
	/**
	 * 产品信息
	 */
	private TraceProductReq traceProduct;

	public String getIsBottomPacking() {
		return isBottomPacking;
	}

	public void setIsBottomPacking(String isBottomPacking) {
		this.isBottomPacking = isBottomPacking;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getTraceCode() {
		return traceCode;
	}

	public void setTraceCode(String traceCode) {
		this.traceCode = traceCode;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getPackType() {
		return packType;
	}

	public void setPackType(String packType) {
		this.packType = packType;
	}

	public TraceProductReq getTraceProduct() {
		return traceProduct;
	}

	public void setTraceProduct(TraceProductReq traceProduct) {
		this.traceProduct = traceProduct;
	}

}
