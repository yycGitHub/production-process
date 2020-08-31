/**
 * 
 */
package com.surekam.modules.api.dto.resp;

import java.io.Serializable;

/**
 * Title: StatisticsBatchResp Description: 统计批次返回对象
 * 
 * @author tangjun
 * @date 2019年5月13日
 */
public class StatisticsBatchResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 批次编码
	 */
	private String batchCode;

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

}
