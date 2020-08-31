package com.surekam.modules.api.dto.resp;

import java.io.Serializable;

public class BatchHarvestDateResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 批次编码
	 */
	private String batchCode;

	/**
	 * 开始时间
	 */
	private String batchStartDate;

	/**
	 * 预计采收时间
	 */
	private String batchPlanHarvestDate;

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getBatchStartDate() {
		return batchStartDate;
	}

	public void setBatchStartDate(String batchStartDate) {
		this.batchStartDate = batchStartDate;
	}

	public String getBatchPlanHarvestDate() {
		return batchPlanHarvestDate;
	}

	public void setBatchPlanHarvestDate(String batchPlanHarvestDate) {
		this.batchPlanHarvestDate = batchPlanHarvestDate;
	}

}
