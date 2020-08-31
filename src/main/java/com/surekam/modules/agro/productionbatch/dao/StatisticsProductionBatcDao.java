package com.surekam.modules.agro.productionbatch.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.productionbatch.entity.StatisticsProductionBatch;

/**
 * 批次管理Entity
 * 
 * @author tangjun
 * @version 2019-04-15
 */
@Repository
public class StatisticsProductionBatcDao extends BaseDao<StatisticsProductionBatcDao> {

	public Page<StatisticsProductionBatch> pageBaseIdAndStates(String officeStr, String productIdStr, String operType, String particularYear, int no, int size) {
		Page<StatisticsProductionBatch> page = new Page<StatisticsProductionBatch>(no, size);
		String sql = "select a.*, b.acreage, b.office_id, b.name from t_agro_base_tree b inner join t_agro_production_batch a on a.base_id = b.id where 1=1 ";
		if (StringUtils.isNotBlank(officeStr)) {
			sql += "and b.office_id in (" + officeStr + ")";
		}
		if (StringUtils.isNotBlank(productIdStr)) {
			sql += "and a.product_id in (" + productIdStr + ")";
		}
		if(StringUtils.isNotBlank(particularYear)) {
			sql += "and DATE_FORMAT(a.create_time,'%Y') LIKE '" + particularYear + "' ";
		}
		if (StringUtils.isNotBlank(operType) && "1".equals(operType)) {
			sql += "and a.status = '1' ";
		}
		if (StringUtils.isNotBlank(operType) && "2".equals(operType)) {
			sql += "and (a.status = '998' or a.status = '999') " ;
		}
		sql += "and (a.states <> 'D' or b.states is not null) and b.states <> 'D' ";
		Page<StatisticsProductionBatch> findPage = findBySql(page, sql, StatisticsProductionBatch.class);
		return findPage;
	}
	
	public List<StatisticsProductionBatch> findByBaseIdAndStates(String officeStr, String productIdStr, String operType, String particularYear) {
		String sql = "select a.*, b.acreage, b.office_id, b.name from t_agro_base_tree b inner join t_agro_production_batch a on a.base_id = b.id where 1=1 ";
		if (StringUtils.isNotBlank(officeStr)) {
			sql += "and b.office_id in (" + officeStr + ")";
		}
		if (StringUtils.isNotBlank(productIdStr)) {
			sql += "and a.product_id in (" + productIdStr + ")";
		}
		if(StringUtils.isNotBlank(particularYear)) {
			sql += "and DATE_FORMAT(a.create_time,'%Y') LIKE '" + particularYear + "' ";
		}
		if (StringUtils.isNotBlank(operType) && "1".equals(operType)) {
			sql += "and a.status = '1' ";
		}
		if (StringUtils.isNotBlank(operType) && "2".equals(operType)) {
			sql += "and (a.status = '998' or a.status = '999') " ;
		}
		sql += "and (a.states <> 'D' or b.states is not null) and b.states <> 'D'";
		List<StatisticsProductionBatch> findPage = findBySql(sql, null, StatisticsProductionBatch.class);
		return findPage;
	}
	// 首页统计用

	public List<StatisticsProductionBatch> findByBaseIdAndStates(String officeStr, String productIdStr) {
		String sql = "select a.*, b.acreage, b.office_id, b.name"
				+ " from t_agro_base_tree b"
				+ " inner join t_agro_production_batch a"
				+ " on a.base_id = b.id LEFT JOIN sys_office o on o.id = b.office_id where 1=1"
				+ " AND  (o.id LIKE '"+officeStr+"' OR o.PARENT_IDS LIKE '%,"+officeStr+",%')";
		if (StringUtils.isNotBlank(productIdStr)) {
			sql += "and a.product_id ='" + productIdStr + "'";
		}
		sql += "and (a.states <> 'D' or b.states is not null) and b.states <> 'D'";
		List<StatisticsProductionBatch> findPage = findBySql(sql, null, StatisticsProductionBatch.class);
		return findPage;
	}
}
