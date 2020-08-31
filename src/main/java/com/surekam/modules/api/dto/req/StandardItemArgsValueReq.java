/**
 * 
 */
package com.surekam.modules.api.dto.req;

import java.io.Serializable;

/**
 * Title: StandardItemArgsValueReq Description: 标准作业参数多项值表請求参数
 * 
 * @author tangjun
 * @date 2019年5月6日
 */
public class StandardItemArgsValueReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 标准参数ID
	 */
	private String itemArgsId;
	/**
	 * 标准参数的值的展示内容
	 */
	private String name;
	/**
	 * 标准参数的值
	 */
	private String value;
	/**
	 * 排序
	 */
	private String sort;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemArgsId() {
		return itemArgsId;
	}

	public void setItemArgsId(String itemArgsId) {
		this.itemArgsId = itemArgsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
