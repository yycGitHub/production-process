/**
 * 
 */
package com.surekam.modules.api.dto.req;

import java.io.Serializable;
import java.util.List;

/**
 * Title: ProductGrowthCycleReq Description: 生长周期阶段请求参数
 * 
 * @author tangjun
 * @date 2019年5月10日
 */
public class ProductGrowthCycleReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 品种主键
	 */
	private String sysEntStandardId;
	/**
	 * 生长周期名称
	 */
	private String cycleName;
	/**
	 * 开始天数
	 */
	private String beginDay;
	/**
	 * 结束天数
	 */
	private String endDay;

	private List<SensorThresholdReq> stReq;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSysEntStandardId() {
		return sysEntStandardId;
	}

	public void setSysEntStandardId(String sysEntStandardId) {
		this.sysEntStandardId = sysEntStandardId;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getBeginDay() {
		return beginDay;
	}

	public void setBeginDay(String beginDay) {
		this.beginDay = beginDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

	public List<SensorThresholdReq> getStReq() {
		return stReq;
	}

	public void setStReq(List<SensorThresholdReq> stReq) {
		this.stReq = stReq;
	}

}
