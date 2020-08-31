package com.surekam.modules.agro.productionbatch.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatchReport;

/**
 * 批次管理DAO接口
 * 
 * @author tangjun
 * @version 2019-04-15
 */
@Repository
public class ProductionBatchDao extends BaseDao<ProductionBatch> {

	public List<ProductionBatch> findByBaseId(String baseId) {
		String sql = "select * from t_agro_production_batch a where a.base_id = '" + baseId + "' and a.states <> 'D'";
		return findBySql(sql, null, ProductionBatch.class);
	}

	public List<ProductionBatch> findByBaseIdNotDel(String baseId) {
		String sql = "select * from t_agro_production_batch a where a.base_id = '" + baseId + "' and a.states != 'D'";
		return findBySql(sql, null, ProductionBatch.class);
	}

	public List<ProductionBatch> findByBaseIdIsDel(String baseId) {
		String sql = "select * from t_agro_production_batch a where a.base_id = '" + baseId + "' and a.states = 'D'";
		return findBySql(sql, null, ProductionBatch.class);
	}

	public List<ProductionBatch> findByBaseIdAndCreateTime(String baseId, String createTime) {
		String sql = "select * from t_agro_production_batch a where a.base_id = '" + baseId + "' and a.create_time like '" + createTime + "%'";
		return findBySql(sql, null, ProductionBatch.class);
	}

	public List<ProductionBatch> findByBaseIdAndCreateTimeAndBatchCode(String baseId, String createTime, String batchCode) {
		String sql = "select * from t_agro_production_batch a where a.base_id = '" + baseId + "' and a.create_time like '" + createTime + "%' and a.batch_Code = '" + batchCode + "'";
		return findBySql(sql, null, ProductionBatch.class);
	}
	
	public List<ProductionBatch> findByInBaseIdAndCreateTimeAndBatchCode(String baseId, String createTime, String batchCode) {
		String sql = "select * from t_agro_production_batch a where a.base_id in (" + baseId + ") and a.create_time like '" + createTime + "%' and a.batch_Code = '" + batchCode + "'";
		return findBySql(sql, null, ProductionBatch.class);
	}
	
	public boolean isExistBatchCode(String officeId, String batchCode) {
		String sql = "SELECT  a.* FROM t_agro_production_batch a, t_agro_base_tree b WHERE a.base_id = b.id AND a.states<>'D' AND b.states<>'D'"
            +" AND b.office_id = :p1 AND a.batch_Code = :p2 ";
		List<ProductionBatch> list = findBySql(sql, new Parameter(officeId, batchCode), ProductionBatch.class);
		if(list!=null && list.size()>0){
			return true;
		}else{
			return false;
		}
	}

	public Page<ProductionBatch> findByInBaseId(String baseId, String operType, int no, int size) {
		Page<ProductionBatch> page = new Page<ProductionBatch>(no, size);
		String sql = "select * from t_agro_production_batch a where a.base_id in " + baseId + "";
		if (StringUtils.isNotBlank(operType)) {
			if("1".equals(operType)){
				sql += "and a.status = '1' ";
			} else if("2".equals(operType)){
				sql += "and a.status = '999' ";
			} else if("3".equals(operType)){
				sql += "and a.status = '998' ";
			}
		}
		sql += "and a.states <> 'D' order by a.create_time desc";
		Page<ProductionBatch> findPage = findBySql(page, sql, ProductionBatch.class);
		return findPage;
	}
	public Page<ProductionBatchReport> findBatchReport(String baseId,String batchCode, String operType, int no, int size) {
		Page<ProductionBatchReport> page = new Page<ProductionBatchReport>(no, size);
		String sql = "select * from t_agro_production_batch a where a.base_id in " + baseId + "";
		if (StringUtils.isNotBlank(operType)) {
			if("1".equals(operType)){
				sql += "and a.status = '1' ";
			} else if("2".equals(operType)){
				sql += "and a.status = '999' ";
			} else if("3".equals(operType)){
				sql += "and a.status = '998' ";
			}
		}
		if (StringUtils.isNotBlank(batchCode)) {
			sql += "and a.batch_code like '"+batchCode+"' ";
		}
		sql += "and a.states <> 'D' order by a.create_time desc";
		Page<ProductionBatchReport> findPage = findBySql(page, sql, ProductionBatchReport.class);
		return findPage;
	}
	
	public List<ProductionBatch> findByBatchCodeAndOfficeId(String batchCode, String officeId) {
		String sql = "select a.* from t_agro_production_batch a " 
				+ "inner join t_agro_base_tree b on a.base_id = b.id "
				+ "inner join sys_office c on b.office_id = c.id "
				+ "where a.batch_code =:p1 and b.office_id =:p2 and a.states <> 'D' and b.states <> 'D' and c.DEL_FLAG = '0' ";
		List<ProductionBatch> findBySql = findBySql(sql, new Parameter(batchCode, officeId), ProductionBatch.class);
		return findBySql;
	}
	
	public List<ProductionBatch> findByBatchCodeAndKuId(String batchCode, String kuId) {
		String sql = "select a.* from t_agro_production_batch a " 
				+ "inner join t_agro_base_tree b on a.base_id = b.id "
				+ "inner join sys_office c on b.office_id = c.id "
				+ "where a.batch_code =:p1 and c.kuid =:p2 and a.states <> 'D' and b.states <> 'D' and c.DEL_FLAG = '0' ";
		List<ProductionBatch> findBySql = findBySql(sql, new Parameter(batchCode, kuId), ProductionBatch.class);
		return findBySql;
	}
	
	public List<ProductionBatch> findByStatus(String status) {
		String sql = "select * from t_agro_production_batch a where a.status =:p1 and a.states <> 'D' ";
		List<ProductionBatch> findBySql = findBySql(sql, new Parameter(status), ProductionBatch.class);
		return findBySql;
	}
	
	public ProductionBatch findByBatchId(String batchId) {
		String sql = "select * from t_agro_production_batch a where a.id =:p1 and a.states <> 'D' ";
		List<ProductionBatch> list = findBySql(sql, new Parameter(batchId), ProductionBatch.class);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public Page<ProductionBatch> findByInId(String id, String officeIdStr, int no, int size) {
		Page<ProductionBatch> page = new Page<ProductionBatch>(no, size);
		String sql = "select * from t_agro_production_batch a inner join t_agro_base_tree b on a.base_id = b.id where a.id in (" + id + ") and b.office_id in (" + officeIdStr + ") and a.states <> 'D' and b.states <> 'D' ";
		Page<ProductionBatch> findPage = findBySql(page, sql, ProductionBatch.class);
		return findPage;
	}

	public Page<ProductionBatch> pageNotInId(String id, String officeIdStr, int no, int size) {
		Page<ProductionBatch> page = new Page<ProductionBatch>(no, size);
		String sql = "select * from t_agro_production_batch a inner join t_agro_base_tree b on a.base_id = b.id where a.id not in (" + id + ") and b.office_id in (" + officeIdStr + ") and a.states <> 'D' and b.states <> 'D' ";
		Page<ProductionBatch> findPage = findBySql(page, sql, ProductionBatch.class);
		return findPage;
	}
	
	public List<Map<String, String>> findByOffice(String officeId) {
		String sql = "select MAX(a.id) as id, MAX(a.product_id) as productId, a.standard_id as standardId from t_agro_production_batch a inner join t_agro_base_tree b on a.base_id = b.id where b.office_id =:p1 GROUP BY a.standard_id";
		List<Map<String, String>> findPage = findBySql(sql, new Parameter(officeId), Map.class);
		return findPage;
	}
	
	public ProductionBatch findById(String id) {
		String sql = "select a.* from t_agro_production_batch a inner join t_agro_product_batch_task b on a.id = b.region_id "
				+ "inner join t_agro_product_batch_task_resolve c on c.task_id = b.id "
				+ "inner join t_agro_standard_task_list d on d.task_items_id = c.id "
				+ "inner join t_agro_standard_task_items_args_value e on e.task_list_id = d.id where e.id =:p1 ";
		List<ProductionBatch> findBySql = findBySql(sql, new Parameter(id), ProductionBatch.class);
		if (!findBySql.isEmpty()) {
			return findBySql.get(0);
		}
		return null;
	}
	

	public String isDeleteBatch(String batchId) {
		String sql = "SELECT"
				+ " COUNT(r.id)"
				+ " FROM"
				+ " t_agro_product_batch_task_resolve r"
				+ " LEFT JOIN t_agro_product_batch_task t ON r.task_id=t.id"
				+ " LEFT JOIN t_agro_production_batch b ON t.region_id = b.id"
				+ " WHERE"
				+ " r.finish_time is NOT null"
				+ " AND t.region_type='2'"
				+ " AND b.id = :p1";
		List<Object> list = findBySql(sql, new Parameter(batchId));
		if(list.get(0).toString().equals("0")){
			return "0";
		}else{
			return "1";
		}
	}
	/*
	 * 根据批次查询公司名称，基地名称，对于品种名称
	 */
	public List<Map<String, String>> findNameByBacthId(String bacthId) {
		String sql = "SELECT"
				+ " bt.`name` baseName,"
				+ " o.`NAME` officeName,"
				+ " b.batch_code batchCode,"
				+ " lt.product_category_name productName"
				+ " FROM"
				+ " t_agro_production_batch b"
				+ " LEFT JOIN t_agro_base_tree bt ON bt.id = b.base_id"
				+ " LEFT JOIN sys_office o ON bt.office_id = o.id"
				+ " LEFT JOIN t_agro_product_library_tree lt ON lt.id=b.product_id"
				+ " WHERE b.id=:p1";
		List<Map<String, String>> findPage = findBySql(sql, new Parameter(bacthId), Map.class);
		return findPage;
	}
}
