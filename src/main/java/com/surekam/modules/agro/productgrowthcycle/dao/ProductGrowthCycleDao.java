package com.surekam.modules.agro.productgrowthcycle.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.productgrowthcycle.entity.ProductGrowthCycle;

/**
 * 生长周期阶段表DAO接口
 * 
 * @author tangjun
 * @version 2019-04-26
 */
@Repository
public class ProductGrowthCycleDao extends BaseDao<ProductGrowthCycle> {

	public List<ProductGrowthCycle> getProductGrowthCycleList(String sysEntStandardId) {
		String sql = "select * from t_agro_product_growth_cycle a where a.sys_ent_standard_id = '" + sysEntStandardId + "' and a.states <> 'D' ORDER BY CONVERT(a.begin_day,SIGNED) ASC";
		return findBySql(sql, null, ProductGrowthCycle.class);
	}

	public String findBySysEntStandardIdMaxDate(String sysEntStandardId) {
		String sql = "select (ifnull(max(a.end_day),0)+1) as end_day from t_agro_product_growth_cycle a where a.sys_ent_standard_id =:p1 and a.states <> 'D'";
		List<Integer> list = findBySql(sql, new Parameter(sysEntStandardId));
		String intValue = String.valueOf(list.get(0));
		return Double.valueOf(intValue).intValue() + "";
	}
	
	public List<ProductGrowthCycle> findBySysEntStandardIdAndCycleName(String sysEntStandardId, String cycleName) {
		String sql = "select * from t_agro_product_growth_cycle a where a.sys_ent_standard_id = '" + sysEntStandardId + "' and a.cycle_name = '" + cycleName + "' and a.states <> 'D' ";
		return findBySql(sql, null, ProductGrowthCycle.class);
	}

	public List<ProductGrowthCycle> findBySysEntStandardIdAndStates(String sysEntStandardId, String states) {
		String sql = "select * from t_agro_product_growth_cycle a where a.sys_ent_standard_id = '" + sysEntStandardId + "' ";
		if ("1".equals(states)) {
			sql += "and a.states <> 'D'";
		} else if ("2".equals(states)) {
			sql += "and a.states = 'D'";
		}
		return findBySql(sql, null, ProductGrowthCycle.class);
	}
}
