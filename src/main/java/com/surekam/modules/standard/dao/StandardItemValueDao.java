package com.surekam.modules.standard.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.standard.entity.StandardItemValue;

@Repository
public class StandardItemValueDao extends BaseDao<StandardItemValue> {
	
	public List<StandardItemValue> findAllList(){
		return find("from StandardItemValue where delFlag=:p1 order by sort", new Parameter(StandardItemValue.DEL_FLAG_NORMAL));
	}
}
