/**
 * 
 */
package com.surekam.modules.api.dto.resp;

import java.io.Serializable;

/**
 * Title: BaseTreeListResp Description:
 * 
 * @author tangjun
 * @date 2019年5月6日
 */
public class BaseTreeListResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 是否是公司还是基地 0：为是 1：基地
	 */
	private String isOffice;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsOffice() {
		return isOffice;
	}

	public void setIsOffice(String isOffice) {
		this.isOffice = isOffice;
	}

}
