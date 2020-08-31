package com.surekam.modules.agro.experts.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.experts.entity.ExpertsGoodproblem;

/**
 * 专家擅长问题DAO接口
 * 
 * @author xy
 * @version 2019-04-16
 */
@Repository
public class ExpertsGoodproblemDao extends BaseDao<ExpertsGoodproblem> {

	public List<ExpertsGoodproblem> getExpertsGoodproblemList(String expertId) {
		String sql = "select * from t_agro_experts_goodproblem a where a.expert_id = '" + expertId + "'";
		List<ExpertsGoodproblem> findBySql = findBySql(sql, null, ExpertsGoodproblem.class);
		return findBySql;
	}

	public int delete(String expertId) {
		return updateBySql("delete from t_agro_experts_goodproblem where expert_id =:p1", new Parameter(expertId));
	}

	public List<ExpertsGoodproblem> findByGoodProblem(String goodProblem) {
		String sql = "select * from t_agro_experts_goodproblem a where a.good_problem = '" + goodProblem + "'";
		List<ExpertsGoodproblem> findBySql = findBySql(sql, null, ExpertsGoodproblem.class);
		return findBySql;
	}
	
	public List<ExpertsGoodproblem> findByLikeGoodProblem(String goodProblem) {
		String sql = "select * from t_agro_experts_goodproblem a where a.good_problem like '%" + goodProblem + "%'";
		List<ExpertsGoodproblem> findBySql = findBySql(sql, null, ExpertsGoodproblem.class);
		return findBySql;
	}
}
