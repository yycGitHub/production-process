/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.sys.entity.Dict;

/**
 * 字典DAO接口
 * 
 * @author sureserve
 * @version 2013-8-23
 */
@Repository
public class DictDao extends BaseDao<Dict> {

	public List<Dict> findAllList() {
		return find("from Dict where delFlag=:p1 order by sort", new Parameter(Dict.DEL_FLAG_NORMAL));
	}

	public List<String> findTypeList() {
		return find("select type from Dict where delFlag=:p1 group by type", new Parameter(Dict.DEL_FLAG_NORMAL));
	}

	public Dict findByDescriptionAndValue(String value, String type) {
		String sql = "select * from sys_dict a where a.value = '" + value + "' and a.type = '" + type + "'";
		List<Dict> findBySql = findBySql(sql, null, Dict.class);
		if (findBySql != null && findBySql.size() > 0) {
			return findBySql.get(0);
		} else {
			return null;
		}
	}
	
	public List<Dict> findByType(String type) {
		String sql = "select * from sys_dict a where a.TYPE = '" + type + "' and a.DEL_FLAG = '" + Dict.DEL_FLAG_NORMAL + "' order by a.CREATE_DATE desc";
		List<Dict> findBySql = findBySql(sql, null, Dict.class);
		return findBySql;
	}
	
	public List<Object> findMaxSort(String type) {
		String sql = "select (ifnull(max(a.sort),0)+1) from sys_dict a where a.TYPE='" + type + "' and a.DEL_FLAG = '" + Dict.DEL_FLAG_NORMAL + "'";
		List<Object> findBySql = findBySql(sql);
		return findBySql;
	}
}
