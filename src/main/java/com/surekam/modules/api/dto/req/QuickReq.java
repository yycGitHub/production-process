/**
 * 
 */
package com.surekam.modules.api.dto.req;

import java.io.Serializable;

/**
 * Title: QuickReq Description:
 * 
 * @author tangjun
 * @date 2019年7月19日
 */
public class QuickReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
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
