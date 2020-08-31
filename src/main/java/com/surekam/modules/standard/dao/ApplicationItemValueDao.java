package com.surekam.modules.standard.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.standard.entity.ApplicationItemValue;

@Repository
public class ApplicationItemValueDao extends BaseDao<ApplicationItemValue> {

	public List<ApplicationItemValue> findAllList(){
		return find("from ApplicationItemValue where delFlag=:p1 order by createDate", new Parameter(ApplicationItemValue.DEL_FLAG_NORMAL));
	}

	public List<ApplicationItemValue> findByUserId(String id) {
		return find("from ApplicationItemValue where delFlag=:p1 and createBy.id=:p2 order by createDate", 
				new Parameter(ApplicationItemValue.DEL_FLAG_NORMAL,id));
	}
}
