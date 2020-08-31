package com.surekam.modules.standard.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.standard.entity.Standard;

@Repository
public class StandardDao extends BaseDao<Standard> {
	
	public List<Standard> findByParentIdsLike(String parentIds){
		return find("from Standard where parentIds like :p1  and delFlag=:p2", new Parameter(parentIds,Standard.DEL_FLAG_NORMAL));
	}

}
