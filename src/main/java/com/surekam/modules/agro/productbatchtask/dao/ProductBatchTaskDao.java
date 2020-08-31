package com.surekam.modules.agro.productbatchtask.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.productbatchtask.entity.ProductBatchTask;

/**
 * 批次对应的详细计划DAO接口
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Repository
public class ProductBatchTaskDao extends BaseDao<ProductBatchTask> {

	public Integer countTask(String regionId, String years,String month) {
		String sql = "select count(1) from t_agro_product_batch_task a inner join t_agro_product_batch_task_resolve b on a.id = b.task_id and b.dispatch_time is not null and a.states <> 'D' and b.states <> 'D' and a.region_id = '" + regionId + "'";
		if(StringUtils.isNotBlank(years)) {
			sql += "AND DATE_FORMAT(b.finish_time,'%Y') LIKE '" + years + "' ";
		}
		sql += "AND DATE_FORMAT(b.finish_time,'%m') LIKE '" + month + "' ";
		List<BigInteger> findBySql = findBySql(sql);
		Integer count = 0;
		for (int i = 0; i < findBySql.size(); i++) {
			String string = findBySql.get(i).toString();
			count = +Integer.valueOf(string);
		}
		return count;
	}
	
	public List<ProductBatchTask> findByRegionId(String regionId) {
		String sql = "select * from t_agro_product_batch_task a where a.region_id =:p1 and a.states <> 'D' order by a.start_date asc";
		List<ProductBatchTask> findBySql = findBySql(sql, new Parameter(regionId), ProductBatchTask.class);
		return findBySql;
	}
}
