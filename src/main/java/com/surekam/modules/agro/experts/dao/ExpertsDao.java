package com.surekam.modules.agro.experts.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.experts.entity.Experts;

/**
 * 专家信息DAO接口
 * 
 * @author xy
 * @version 2019-04-09
 */
@Repository
public class ExpertsDao extends BaseDao<Experts> {
	
	public Experts findById(String id) {
		String sql = "select * from t_agro_experts a where a.id =:p1";
		List<Experts> Experts = findBySql(sql, new Parameter(id), Experts.class);
		if (!Experts.isEmpty()) {
			return Experts.get(0);
		} else {
			return null;
		}
	}
}
