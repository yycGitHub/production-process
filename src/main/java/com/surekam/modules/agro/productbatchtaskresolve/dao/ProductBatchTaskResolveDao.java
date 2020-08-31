package com.surekam.modules.agro.productbatchtaskresolve.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.productbatchtaskresolve.entity.ProductBatchTaskResolve;

/**
 * 分解任务表DAO接口
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Repository
public class ProductBatchTaskResolveDao extends BaseDao<ProductBatchTaskResolve> {

	public List<ProductBatchTaskResolve> findByTaskId(String taskId) {
		String sql = "select * from t_agro_product_batch_task_resolve a where a.task_id =:p1 and a.states <> 'D'";
		List<ProductBatchTaskResolve> findBySql = findBySql(sql, new Parameter(taskId), ProductBatchTaskResolve.class);
		return findBySql;
	}

	public List<ProductBatchTaskResolve> findBySendDate(String sendDate, String taskId) {
		String sql = "select * from t_agro_product_batch_task_resolve a where str_to_date(a.send_date, '%Y%m%d%H%i%s') <=:p1 and  a.task_id =:p2 and a.states <> 'D'";
		List<ProductBatchTaskResolve> findBySql = findBySql(sql, new Parameter(sendDate, taskId), ProductBatchTaskResolve.class);
		return findBySql;
	}
	
	
	public Integer count(String sendDate, String taskId, String sendStates) {
		String sql = "select count(*) from t_agro_product_batch_task_resolve a where str_to_date(a.send_date, '%Y%m%d%H%i%s') <=:p1 and a.task_id =:p2 and a.send_states =:p3 and a.states <> 'D'";
		List<BigInteger> findBySql = findBySql(sql, new Parameter(sendDate, taskId, sendStates));
		Integer count = 0;
		for (int i = 0; i < findBySql.size(); i++) {
			String string = findBySql.get(i).toString();
			count = +Integer.valueOf(string);
		}
		return count;
	}

	public List<String> findBySendDateGroupBy(String sendDate, String taskId, String sendStates) {
		String sql = "select a.dispatch_time from t_agro_product_batch_task_resolve a where str_to_date(a.send_date, '%Y%m%d%H%i%s') <=:p1 and  a.task_id =:p2 and a.send_states =:p3 and a.states <> 'D' group by a.dispatch_time order by a.dispatch_time desc ";
		List<String> findBySql = findBySql(sql, new Parameter(sendDate, taskId, sendStates));
		return findBySql;
	}

	public List<ProductBatchTaskResolve> findBySendDateAndTaskIdAndSendStates(String sendDate, String taskId, String sendStates) {
		String sql = "select a.* from t_agro_product_batch_task_resolve a where str_to_date(a.send_date, '%Y%m%d%H%i%s') <=:p1 and a.task_id =:p2 and a.send_states =:p3 and a.states <> 'D' ";
		List<ProductBatchTaskResolve> findBySql = findBySql(sql, new Parameter(sendDate, taskId, sendStates), ProductBatchTaskResolve.class);
		return findBySql;
	}

	public int deleteTaskList(String taskResoleId) {
		return updateBySql("update t_agro_standard_task_list set states='D' where task_items_id =:p1", new Parameter(taskResoleId));
	}
	public int deleteTaskvalue(String taskResoleId) {
		return updateBySql("update t_agro_standard_task_items_args_value a, t_agro_standard_task_list b set a.states='D'"
				+ " where a.task_list_id=b.id and b.task_items_id =:p1", new Parameter(taskResoleId));
	}
}
