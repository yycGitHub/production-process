package com.surekam.modules.standard.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.standard.entity.ExpandPropertyList;

@Repository
public class ExpandPropertyListDao extends BaseDao<ExpandPropertyList> {

	public List<ExpandPropertyList> findAllList(){
		return find("from ExpandPropertyList where delFlag=:p1 order by createDate", new Parameter(ExpandPropertyList.DEL_FLAG_NORMAL));
	}
}
