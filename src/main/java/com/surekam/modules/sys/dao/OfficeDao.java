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
import com.surekam.modules.sys.entity.Office;

/**
 * 机构DAO接口
 * 
 * @author sureserve
 * @version 2013-8-23
 */
@Repository
public class OfficeDao extends BaseDao<Office> {

	public List<Office> findByParentIdsLike(String parentIds) {
		return find("from Office where parentIds like :p1", new Parameter(parentIds));
	}

	public List<Object> getOfficeIdList(String officeid) {
		String qlString = "SELECT t.id FROM sys_office t WHERE t.PARENT_IDS like '%," + officeid + ",%' AND t.DEL_FLAG != '1'";
		return findBySql(qlString);
	}

	public List<Office> findAllOffices(String officeid) {
		String qlString = "SELECT t.* FROM sys_office t WHERE t.PARENT_IDS like '%," + officeid + ",%' AND t.DEL_FLAG != '1'";
		return findBySql(qlString, null, Office.class);
	}
	
	public List<Office> findAllOfficesAndId(String officeid, String id) {
		String qlString = "SELECT t.* FROM sys_office t WHERE (t.PARENT_IDS like '%," + officeid + ",%' OR t.id = '" + id + "' ) AND t.DEL_FLAG != '1' ";
		return findBySql(qlString, null, Office.class);
	}
	
	public List<Office> findByParentId(String parentId) {
		String qlString = "SELECT t.* FROM sys_office t WHERE t.PARENT_ID =:p1 AND t.DEL_FLAG != '1'";
		return findBySql(qlString, new Parameter(parentId), Office.class);
	}

	public List<Office> findByParentIdAndLikeParentIds(String id, String officeid) {
		String qlString = "SELECT t.* FROM sys_office t WHERE t.id ='" + id + "' AND t.PARENT_IDS like '%," + officeid + ",%' AND t.DEL_FLAG != '1'";
		return findBySql(qlString, null, Office.class);
	}

	public Office getByKuid(String kuid) {
		return getByHql("from Office where kuid =:p1", new Parameter(kuid));
	}

}
