package com.surekam.modules.standard.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.standard.entity.StandardItem;
import com.surekam.modules.sys.entity.Dict;

@Repository
public class StandardItemDao extends BaseDao<StandardItem> {
	
	public List<StandardItem> findAllList(){
		return find("from StandardItem where delFlag=:p1 order by sort", new Parameter(Dict.DEL_FLAG_NORMAL));
	}
}
