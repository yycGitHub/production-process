/**
 * 
 */
package com.surekam.modules.api.dto.resp;

import java.io.Serializable;

/**
 * Title: DictResp Description: 字典返回参数
 * 
 * @author tangjun
 * @date 2019年5月5日
 */
public class DictResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 字典ID
	 */
	private String id;
	/**
	 * 标签名
	 */
	private String label;

	/**
	 * 字典描述
	 */
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
