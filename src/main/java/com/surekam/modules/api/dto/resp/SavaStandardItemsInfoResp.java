/**
 * 
 */
package com.surekam.modules.api.dto.resp;

import java.io.Serializable;

/**
 * Title: SavaStandardItemsInfo Description: 新增作业项时返回对象参数
 * 
 * @author tangjun
 * @date 2019年5月5日
 */
public class SavaStandardItemsInfoResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 生长周期ID
	 */
	private String id;
	/**
	 * 生长周期名称
	 */
	private String cycleName;
	/**
	 * 排序
	 */
	private String sort;
	/**
	 * 开始天数
	 */
	private String beginDay;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getBeginDay() {
		return beginDay;
	}

	public void setBeginDay(String beginDay) {
		this.beginDay = beginDay;
	}

}
