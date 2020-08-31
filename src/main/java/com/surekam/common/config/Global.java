/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.common.config;

import java.util.Map;

import org.springframework.util.Assert;

import com.google.common.collect.Maps;
import com.surekam.common.utils.PropertiesLoader;

/**
 * 全局配置类
 * @author sureserve
 * @version 2013-03-23
 */
public class Global {
	
    /**
     * 默认分页查询第一页
     */
    public static int DEFAULT_PAGENO = 1;
    /**
     * 默认每页20条记录
     */
    public static int DEFAULT_PAGESIZE = 20;
    
	/**
	 * 溯源系统总公司ID
	 */
	public static final String CONTROLLING_OFFICE_ID = "2";
	/**
	 * 产品模块定义 主模块中固定的字段编码   身份编号
	 */
	public static final String TRACE_SHOW_PROPERTY_CODE_SFBH = "identityCode";
	/**
	 * 产品模块定义 主模块中固定的字段编码   查询次数   已在批次加了个字段维护查询次数 此属性将被废弃
	 */
	public static final String TRACE_SHOW_PROPERTY_CODE_CXCS = "queryCount";
	/**
	 * 产品模块定义 主模块中固定的字段编码  生产日期
	 */
	public static final String TRACE_SHOW_PROPERTY_CODE_SCRQ = "dateOfManufacture";
	/**
	 * 产品模块定义 主模块中固定的字段编码   保质期
	 */
	public static final String TRACE_SHOW_PROPERTY_CODE_BZQ = "qualityGuaranteePeriod";
	/**
	 * 产品模块定义 主模块中固定的字段编码   生产场地
	 */
	public static final String TRACE_SHOW_PROPERTY_CODE_SCCD = "producePlace";
	
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();
	
	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader propertiesLoader = new PropertiesLoader("surekam.properties");
	
	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = propertiesLoader.getProperty(key);
			map.put(key, value);
		}
		return value;
	}

	/////////////////////////////////////////////////////////
	
	/**
	 * 获取管理端根路径
	 */
	public static String getAdminPath() {
		return getConfig("adminPath");
	}
	
	/**
	 * 获取前端根路径
	 */
	public static String getFrontPath() {
		return getConfig("frontPath");
	}
	
	/**
	 * 获取URL后缀
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}
	
	/**
	 * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
	 */
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}
	
	/**
	 * 在修改系统用户和角色时是否同步到Activiti
	 */
	public static Boolean isSynActivitiIndetity() {
		String dm = getConfig("activiti.isSynActivitiIndetity");
		return "true".equals(dm) || "1".equals(dm);
	}

	/**
	 * 获取CKFinder上传文件的根目录
	 * @return
	 */
	public static String getCkBaseDir() {
		String dir = getConfig("userfiles.basedir");
		Assert.hasText(dir, "配置文件里没有配置userfiles.basedir属性");
		if(!dir.endsWith("/")) {
			dir += "/";
		}
		return dir;
	}
	
}
