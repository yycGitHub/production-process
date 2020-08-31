package com.surekam.modules.agro.standarditems.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.standarditems.entity.StandardItemsApp;

/**
 * 标准作业项表DAO接口
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Repository
public class StandardItemsAppDao extends BaseDao<StandardItemsApp> {
	public List<StandardItemsApp> getStandardTypeList(String standardId) {
		String sql = "SELECT a.* FROM"
			+"  t_agro_standard_items a,"
			+"  t_agro_product_growth_cycle c "
			+" WHERE a.growth_cycle_id = c.id "
			+"  AND a.system_standards_id = :p1 "
			+"  AND a.states <> 'D' "
			+"  AND c.states <> 'D' "
			+"  AND a.id IN "
			+"  (SELECT "
			+"    b.items_id"
			+"  FROM"
			+"    t_agro_standard_item_args b "
			+"  WHERE b.states <> 'D') "
			+"ORDER BY c.begin_day,a.sort";
		return findBySql(sql, new Parameter(standardId),StandardItemsApp.class);
	}
	

}
