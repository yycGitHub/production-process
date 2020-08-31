package com.surekam.modules.agro.standardtaskitemsargsvalue.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.standardtaskitemsargsvalue.entity.StandardTaskItemsArgsValue;

/**
 * 标准作业执行记录表DAO接口
 * 
 * @author liwei
 * @version 2019-04-23
 */
@Repository
public class StandardTaskItemsArgsValueDao extends BaseDao<StandardTaskItemsArgsValue> {

	public List<StandardTaskItemsArgsValue> findByRegionIdAndStates(String regionId, String argsName, String operType) {
		String sql = "select d.* from t_agro_product_batch_task a "
				+ "inner join t_agro_product_batch_task_resolve b on a.id = b.task_id  "
				+ "inner join t_agro_standard_task_list c on b.id = c.task_items_id "
				+ "inner join t_agro_standard_task_items_args_value d on c.id = d.task_list_id "
				+ "where a.region_id =:p1 and d.args_name =:p2 ";
		if ("1".equals(operType)) {
			sql += "and b.dispatch_time is null ";
		} else if ("2".equals(operType)) {
			sql += "and b.dispatch_time is not null ";
		}
		sql += "and a.states <> 'D' and b.states <> 'D' and c.states <> 'D' and d.states <> 'D'";
		List<StandardTaskItemsArgsValue> findBySql = findBySql(sql, new Parameter(regionId, argsName), StandardTaskItemsArgsValue.class);
		return findBySql;
	}

	public List<Object> findByTaskListIdAndArgsName(String taskListId, String argsName) {
		String sql = "select a.args_name as argsName, sum(a.task_item_args_value) as taskItemArgsValue, a.args_unit as argsUnit from t_agro_standard_task_items_args_value a where a.task_list_id =:p1 and a.args_name =:p2 and a.states <> 'D' group by a.args_name, a.args_unit"; 
		List<Object> findBySql = findBySql(sql, new Parameter(taskListId, argsName));
		return findBySql;
	}

	public List<Object> getYield(String taskListId, String year) {
		String sql = "SELECT MONTH(b.time),IFNULL(SUM(a.task_item_args_value),0) FROM" + " (" + " SELECT"
				+ " task_list_id," + " task_item_args_value" + " FROM" + " t_agro_standard_task_items_args_value"
				+ " WHERE" + " args_name = '采收量'" + " AND states <> 'D'" + " ) a LEFT JOIN (" + " SELECT"
				+ " task_list_id,"
				+ " str_to_date(REPLACE (REPLACE (REPLACE (task_item_args_value,'年','-'),'月','-'),'日',''),'%Y-%m-%d') 'time'"
				+ " FROM" + " t_agro_standard_task_items_args_value" + " WHERE" + " args_name = '日期'"
				+ " AND states <> 'D'" + " ) b ON a.task_list_id=b.task_list_id WHERE a.task_list_id='" + taskListId
				+ "'";
		if (StringUtils.isNotBlank(year)) {
			sql += " and YEAR(b.time) = '" + year + "'";
		}
		sql += " GROUP BY MONTH(b.time)";
		List<Object> findBySql = findBySql(sql);
		return findBySql;
	}

	public Page<Map<String, String>> pageByRegionId(String regionId, String argsName, int no, int size) {
		Page<Map<String,String>> page = new Page<Map<String,String>>(no, size);
		String sql = "select "
				+ "f.product_category_name as productCategoryName, "
				+ "c.finish_time as finishTime, a.task_item_args_value as taskItemArgsValueName, "
				+ "a.task_list_id as taskListId, e.status as status, a.id as ids, "
				+ "a.task_item_args_id as taskItemArgsId, c.id as taskResolveId, c.confirm_states as confirmStates,c.serial_number as serialNumber "
				+ "from t_agro_standard_task_items_args_value a "
				+ "inner join t_agro_standard_task_list b on a.task_list_id = b.id "
				+ "inner join t_agro_product_batch_task_resolve c on b.task_items_id = c.id  "
				+ "inner join t_agro_product_batch_task d on c.task_id = d.id "
				+ "inner join t_agro_production_batch e on d.region_id = e.id "
				+ "inner join t_agro_product_library_tree f on e.product_id = f.id "
				+ "inner join t_agro_standard_item_args g on a.task_item_args_id = g.id "
				+ "where e.id = '" + regionId + "' and a.args_name ='" + argsName + "' and a.states <> 'D' and b.states <> 'D' and c.states <> 'D' and d.states <> 'D' order by c.confirm_states,c.finish_time asc";
		Page<Map<String,String>> findPage = findBySql(page, sql, Map.class);
		return findPage;
	}
	
	
	public List<Map<String, String>> listByRegionId(String regionId, String argsName) {
		String sql = "select "
				+ "f.product_category_name as productCategoryName, "
				+ "c.finish_time as finishTime, a.task_item_args_value as taskItemArgsValueName, "
				+ "a.task_list_id as taskListId, e.status as status, a.id as ids, "
				+ "a.task_item_args_id as taskItemArgsId, c.id as taskResolveId, c.confirm_states as confirmStates "
				+ "from t_agro_standard_task_items_args_value a "
				+ "inner join t_agro_standard_task_list b on a.task_list_id = b.id "
				+ "inner join t_agro_product_batch_task_resolve c on b.task_items_id = c.id  "
				+ "inner join t_agro_product_batch_task d on c.task_id = d.id "
				+ "inner join t_agro_production_batch e on d.region_id = e.id "
				+ "inner join t_agro_product_library_tree f on e.product_id = f.id "
				+ "inner join t_agro_standard_item_args g on a.task_item_args_id = g.id "
				+ "where e.id =:p1  and a.args_name =:p2 and a.states <> 'D' and b.states <> 'D' and c.states <> 'D' and d.states <> 'D'";
		List<Map<String, String>> findPage = findBySql(sql, new Parameter(regionId, argsName), Map.class);
		return findPage;
	}
	
	
	
	
	public Page<Map<String, String>> page(String argsName, String idStr, String officeIdStr, String roleId, String typeOperation, int no, int size) {
		Page<Map<String, String>> page = new Page<Map<String, String>>(no, size);
		String sql = "select a.id as id, a.task_list_id as taskListId, e.batch_code as batchCode, g.product_category_name as productCategoryName, f.name as baseTreeName, j.name as userName, i.standard_name as standardName, e.batch_start_date as batchStartDate, a.task_item_args_value as taskItemArgsValue, a.args_unit as argsUnit, e.id as batchId, c.finish_time as finishTime "
				+ "from t_agro_standard_task_items_args_value a "
				+ "inner join t_agro_standard_task_list b on a.task_list_id = b.id "
				+ "inner join t_agro_product_batch_task_resolve c on b.task_items_id = c.id "
				+ "inner join t_agro_product_batch_task d on c.task_id = d.id "
				+ "inner join t_agro_production_batch e on d.region_id = e.id "
				+ "inner join t_agro_base_tree f on e.base_id = f.id "
				+ "inner join t_agro_product_library_tree g on e.product_id = g.id "
				+ "inner join t_agro_base_manager h on e.base_id = h.t_base_id and h.role_id ='" + roleId + "' "
				+ "inner join sys_user j on h.user_id = j.id "
				+ "inner join t_agro_system_enterprise_standards i on e.standard_id = i.id "
				+ "where d.standard_item_id in (select a.id from t_agro_standard_items a where a.item_category_id = 'fb08f0fe80474919bf9d7c14c8ff65b2')" 
				+ "and a.args_name = '" + argsName + "' "
				+ "and f.office_id in (" + officeIdStr + ") "
				+ "and a.states <> 'D' and b.states <> 'D' and c.states <> 'D' and d.states <> 'D' and e.states <> 'D' and c.execution_status ='1' AND (c.confirm_states = '1' OR e.harvest_status='0') ";
		if ("notIn".equals(typeOperation)) {
			sql += "and a.id not in (" + idStr + ") ";
		}
		if ("in".equals(typeOperation)) {
			sql += "and a.id in (" + idStr + ") ";
		}
		sql += "order by e.create_time desc ";
		Page<Map<String,String>> findPage = findBySql(page, sql, Map.class);
		return findPage;
	}
	
	public List<Map<String, String>> List(String argsName, String idStr, String officeIdStr, String roleId) {
		String sql = "select "
				+ "a.id as id, "
				+ "e.id as batchId "
				+ "from t_agro_standard_task_items_args_value a "
				+ "inner join t_agro_standard_task_list b on a.task_list_id = b.id "
				+ "inner join t_agro_product_batch_task_resolve c on b.task_items_id = c.id "
				+ "inner join t_agro_product_batch_task d on c.task_id = d.id "
				+ "inner join t_agro_production_batch e on d.region_id = e.id "
				+ "inner join t_agro_base_tree f on e.base_id = f.id "
				+ "inner join t_agro_product_library_tree g on e.product_id = g.id "
				+ "inner join t_agro_base_manager h on e.base_id = h.t_base_id and h.role_id ='" + roleId + "' "
				+ "inner join sys_user j on h.user_id = j.id "
				+ "inner join t_agro_system_enterprise_standards i on e.standard_id = i.id "
				+ "where d.standard_item_id in (select a.id from t_agro_standard_items a where a.item_category_id = 'fb08f0fe80474919bf9d7c14c8ff65b2')" 
				+ "and a.args_name = '" + argsName + "' "
				+ "and a.id not in (" + idStr + ") "
				+ "and f.office_id in (" + officeIdStr + ") "
				+ "and a.states <> 'D' and b.states <> 'D' and c.states <> 'D' and d.states <> 'D' and e.states <> 'D' ";
		sql += "order by e.create_time asc ";
		List<Map<String, String>> findBySql = findBySql(sql, null, Map.class);
		return findBySql;
	}
	
	public StandardTaskItemsArgsValue getTaskListIdAndArgsName(String taskListId, String argsName) {
		String sql = "select * from t_agro_standard_task_items_args_value a where a.task_list_id =:p1 and a.args_name =:p2 and a.states <> 'D' ";
		List<StandardTaskItemsArgsValue> findBySql = findBySql(sql, new Parameter(taskListId, argsName), StandardTaskItemsArgsValue.class);
		if (!findBySql.isEmpty()) {
			StandardTaskItemsArgsValue standardTaskItemsArgsValue = findBySql.get(0);
			return standardTaskItemsArgsValue;
		}
		return null;
	}
	
}
