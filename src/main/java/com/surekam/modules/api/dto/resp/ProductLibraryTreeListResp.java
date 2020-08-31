package com.surekam.modules.api.dto.resp;

import java.io.Serializable;

public class ProductLibraryTreeListResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 品种ID
	 */
	private String id;
	/**
	 * 品种名称
	 */
	private String name;

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

}
