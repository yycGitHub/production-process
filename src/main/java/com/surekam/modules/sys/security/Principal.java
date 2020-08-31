package com.surekam.modules.sys.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.surekam.modules.sys.entity.User;

/**
 * 授权用户信息
 */
public class Principal implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String loginName;
	private String name;
	private Map<String, Object> cacheMap;

	public Principal(User user) {
		this.id = user.getId();
		this.loginName = user.getLoginName();
		this.name = user.getName();
	}

	public String getId() {
		return id;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getName() {
		return name;
	}

	public Map<String, Object> getCacheMap() {
		if (cacheMap==null){
			cacheMap = new HashMap<String, Object>();
		}
		return cacheMap;
	}

}