package com.surekam.modules.agro.standarditemcategory.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.standarditemcategory.entity.StandardItemCategory;

/**
 * 作业项类别表(包括 施肥 投料等 )DAO接口
 * @author liwei
 * @version 2019-04-25
 */
@Repository
public class StandardItemCategoryDao extends BaseDao<StandardItemCategory> {
	
	public List<StandardItemCategory> findByTaskItemCategoryNameList(String taskItemCategoryName) {
		String sql = "select * from t_agro_standard_item_category a where a.task_item_category_name =:p1 ";
		List<StandardItemCategory> findBySql = findBySql(sql,new Parameter(taskItemCategoryName));
		return findBySql;
	}
	
	public List<StandardItemCategory> findBy() {
		String sql = "select * from t_agro_standard_item_category a order by a.sort desc";
		List<StandardItemCategory> findBySql = findBySql(sql, null, StandardItemCategory.class);
		return findBySql;
	}
	
	public List<StandardItemCategory> findByTaskItemCategoryName(String taskItemCategoryName) {
		String sql = "select * from t_agro_standard_item_category a where a.task_item_category_name =:p1 ";
		List<StandardItemCategory> findBySql = findBySql(sql, new Parameter(taskItemCategoryName), StandardItemCategory.class);
		return findBySql;
	}
}
