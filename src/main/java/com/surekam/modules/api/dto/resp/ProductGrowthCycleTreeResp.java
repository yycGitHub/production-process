package com.surekam.modules.api.dto.resp;

import java.io.Serializable;
import java.util.List;

import com.surekam.modules.agro.standarditems.entity.vo.StandardItemsVo;

/**
 * Title: ProductGrowthCycleTreeResp Description: 标注树生长周期返回对象
 * 
 * @author tangjun
 * @date 2019年5月10日
 */
public class ProductGrowthCycleTreeResp implements Serializable {

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
	private String name;
	/**
	 * 开始天数
	 */
	private String beginDay;
	/**
	 * 结束天数
	 */
	private String endDay;
	/**
	 * 标准类型
	 */
	private String standardsType;

	private List<StandardItemsVo> childList;

	public List<StandardItemsVo> getChildList() {
		return childList;
	}

	public void setChildList(List<StandardItemsVo> childList) {
		this.childList = childList;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getStandardsType() {
		return standardsType;
	}

	public void setStandardsType(String standardsType) {
		this.standardsType = standardsType;
	}

}
