package com.surekam.modules.agro.standarditems.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.standarditems.entity.StandardItems;

/**
 * 标准作业项表DAO接口
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Repository
public class StandardItemsDao extends BaseDao<StandardItems> {

	public StandardItems findByGrowthCycleId(String growthCycleId) {
		String sql = "select * from t_agro_standard_items a where a.growth_cycle_id = '" + growthCycleId + "' and a.states <> 'D' and a.parent_id ='0'";
		List<StandardItems> findBySql = findBySql(sql, null, StandardItems.class);
		if (findBySql != null && findBySql.size() > 0) {
			return findBySql.get(0);
		} else {
			return null;
		}
	}
	
	public List<StandardItems> findByGrowthCycleIdDescList(String growthCycleId) {
		String sql = "select * from t_agro_standard_items a where a.growth_cycle_id = '" + growthCycleId + "' and a.states <> 'D' and a.parent_id ='0' order by a.sort desc";
		List<StandardItems> findBySql = findBySql(sql, null, StandardItems.class);
		return findBySql;
	}
	
	public List<StandardItems> findByGrowthCycleIdAscList(String growthCycleId) {
		String sql = "select * from t_agro_standard_items a where a.growth_cycle_id = '" + growthCycleId + "' and a.states <> 'D' and a.parent_id ='0' order by a.sort asc";
		List<StandardItems> findBySql = findBySql(sql, null, StandardItems.class);
		return findBySql;
	}
	
	public List<StandardItems> findBySystemStandardsIdAndItemName(String systemStandardsId, String itemName) {
		String sql = "select * from t_agro_standard_items a where a.system_standards_id = '" + systemStandardsId + "'and a.item_name = '" + itemName + "' and a.states <> 'D'";
		return findBySql(sql, null, StandardItems.class);
	}
	
	public String getSystemStandardsIdMaxDate(String growthCycleId) {
		String sql = "SELECT (ifnull(max(a.END_DATE_NUMBER),0)+1) as end_date_number from t_agro_standard_items a where a.growth_cycle_id =:p1 and a.states <> 'D' ORDER BY CONVERT(a.end_date_number,SIGNED) ASC";
		List<Integer> list = findBySql(sql,new Parameter(growthCycleId));
		String intValue = String.valueOf(list.get(0));
		return Double.valueOf(intValue).intValue() + "";
	}
	
	public List<StandardItems> getStandardItemsAndOperationType(String growthCycleId, String operationType) {
		String sql = "select * from t_agro_standard_items a where a.growth_cycle_id = '" + growthCycleId + "' and a.operation_type = '" + operationType + "'and a.states <> 'D' ORDER BY CONVERT(a.end_date_number,SIGNED) ASC";
		return findBySql(sql, null, StandardItems.class);
	}
	
	public List<StandardItems> getStandardItemsList(String growthCycleId) {
		String sql = "select * from t_agro_standard_items a where a.growth_cycle_id = '" + growthCycleId + "' and a.states <> 'D'";
		return findBySql(sql, null, StandardItems.class);
	}
	
	public String findByParentIdQueryMaxSort(String parentId) {
		String sql = "select (ifnull(max(a.sort),0)+1) as sort from t_agro_standard_items a where a.parent_id = '" + parentId + "' and a.states <> 'D' ";
		List<BigInteger> findBySql = findBySql(sql);
		BigInteger bigInteger = findBySql.get(0);
		String string = String.valueOf(bigInteger);
		return string;
	}
	
	public List<StandardItems> findBySystemStandardsIdAndItemCategoryIdAndStates(String systemStandardsId, String itemCategoryId) {
		String sql = "select * from t_agro_standard_items a where a.system_standards_id = '" + systemStandardsId + "' and a.item_category_id = '" + itemCategoryId + "' and a.states <> 'D'";
		return findBySql(sql, null, StandardItems.class);
	}
	
	public List<StandardItems> findBySystemStandardsIdAndGrowthCycleIdAndStates(String systemStandardsId, String growthCycleId) {
		String sql = "select * from t_agro_standard_items a where a.system_standards_id =:p1 and a.growth_cycle_id =:p2 and a.states <> 'D'";
		return findBySql(sql, new Parameter(systemStandardsId, growthCycleId), StandardItems.class);
	}
	

}
