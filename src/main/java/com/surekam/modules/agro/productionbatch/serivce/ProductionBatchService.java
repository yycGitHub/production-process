package com.surekam.modules.agro.productionbatch.serivce;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.productbatchtask.dao.ProductBatchTaskDao;
import com.surekam.modules.agro.productbatchtask.entity.ProductBatchTask;
import com.surekam.modules.agro.productbatchtaskresolve.dao.ProductBatchTaskResolveDao;
import com.surekam.modules.agro.productionbatch.dao.ProductionBatchDao;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.OfficeVo;

/**
 * 批次管理Service
 * 
 * @author tangjun
 * @version 2019-04-15
 */
@Component
@Transactional(readOnly = true)
public class ProductionBatchService extends BaseService {

	@Autowired
	private ProductionBatchDao productionBatchDao;
	
	@Autowired
	private ProductBatchTaskDao productBatchTaskDao;
	
	@Autowired
	private ProductBatchTaskResolveDao productBatchTaskResolveDao;
	
	@Autowired
	private OfficeDao officeDao;

	public ProductionBatch get(String id) {
		return productionBatchDao.get(id);
	}

	public Page<ProductionBatch> find(Page<ProductionBatch> page, ProductionBatch agroProductionBatch) {
		DetachedCriteria dc = productionBatchDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ProductionBatch.FIELD_DEL_FLAG, ProductionBatch.DEL_FLAG_NORMAL));
		return productionBatchDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(ProductionBatch agroProductionBatch) {
		productionBatchDao.save(agroProductionBatch);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		productionBatchDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void saveExecutionTaskData(String id,String status,User user,String finishTime,String remark) {
		ProductionBatch batch = productionBatchDao.get(id);
		batch.setUpdateTime(new Date());
		batch.setUpdateUserId(user.getId());
		batch.setStatus(status);
		batch.setBatchEndDate(finishTime);
		batch.setRemark(remark);
		productionBatchDao.save(batch);
		
		//删除当天以后的所有任务
		deleteTaskData(batch);
	}
	
	@Transactional(readOnly = false)
	public void deleteTaskData(ProductionBatch batch) {
		String hql = " from ProductBatchTask a where a.regionId = :p1 and a.states<>'D' ";
		List<ProductBatchTask> taskList = productBatchTaskDao.find(hql,new Parameter(batch.getId()));
		for (ProductBatchTask productBatchTask : taskList) {
			String sql = "UPDATE t_agro_product_batch_task_resolve a SET a.states='D' WHERE a.states <> 'D' AND a.task_id = :p1 AND REPLACE(a.dispatch_time, '-', '')> DATE_FORMAT(SYSDATE(), '%Y%m%d')";
			productBatchTaskResolveDao.updateBySql(sql,new Parameter(productBatchTask.getId()));
			productBatchTaskResolveDao.flush();
		}
		
		String sql = " UPDATE t_agro_product_batch_task a SET a.finished_times=(SELECT COUNT(b.id) FROM t_agro_product_batch_task_resolve b WHERE b.task_id=a.id AND b.states<>'D') WHERE a.states<>'D' AND a.region_id=:p1 ";
		productBatchTaskDao.updateBySql(sql,new Parameter(batch.getId()));
	}

	/**
	 * 获取基地数量
	 * 
	 * @param user
	 * @param officeId
	 * @param onlyNext
	 * @return
	 */
	public String findBatchCount(User user) {
		String sql = "SELECT COUNT(t.id) FROM t_agro_production_batch t WHERE t.user_id = :p1 AND t.states <> 'D'";
		List<Integer> list = productionBatchDao.findBySql(sql, new Parameter(user.getId()));
		if (list.size() > 0) {
			return list.get(0) + "";
		}
		return "0";
	}
	
	/**
	 * 获取公司批次数
	 * 
	 * @param baseId
	 * @return
	 */
	public String findBatchCountByBaseId(String officeId) {
		String sql = "SELECT t.id FROM t_agro_production_batch t WHERE t.base_id IN (SELECT b.id FROM t_agro_base_tree b WHERE b.office_id='" + officeId + "' AND b.states<>'D') AND t.states <> 'D' order by t.create_time";
		List<String> list = productionBatchDao.findBySql(sql);
		if(list!=null && list.size()>0){
			return list.size() + " 批";
		}else{
			return "无";
		}
	}
	
	/**
	 * 获取基地批次
	 * 
	 * @param baseId
	 * @return
	 */
	public String findBatchDataByBaseId(String baseId) {
		String result = "";
		String sql = "SELECT t.batch_code FROM t_agro_production_batch t WHERE t.status='1' and t.base_id = :p1 AND t.states <> 'D' order by t.create_time";
		List<String> list = productionBatchDao.findBySql(sql, new Parameter(baseId));
		for (String str : list) {
			result+= str + "，";
		}
		if(StringUtils.isNotBlank(result)){
			result = "操作批次：" + result.substring(0, result.length()-1);
		}else{
			result = "操作批次：无";
		}
		return result;
	}
	
	/**
	 * app-提问获取批次列表
	 * @param officeId
	 * @return
	 */
	public List<ProductionBatch> getlist(User user) { 
		StringBuffer sql = new StringBuffer(200);
		sql.append("SELECT distinct pb.* FROM t_agro_production_batch pb,t_agro_base_tree bt WHERE pb.base_id = bt.id ");
		sql.append(" and  bt.office_id = '" + user.getCompany().getId() +"'" );
		//sql.append(" and ( pb.create_user_id = '"+user.getId()+"' or pb.update_user_id = '"+user.getId()+"') ");
		return productionBatchDao.findBySql2(sql.toString(), new Parameter(), ProductionBatch.class);	
	}
	
	/**
	 * 获取基地数据结构
	 * 
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> findCompanyList(User user) {
		String sql = "SELECT a.id,a.name,a.office_logo FROM sys_office a WHERE (a.id = '" + user.getOffice().getId() + "' OR a.PARENT_IDS LIKE '%" + user.getOffice().getId() + ",%') AND a.DEL_FLAG = '0' ORDER BY a.CREATE_DATE";
		return officeDao.findBySql(sql,new Parameter(),Map.class);
	}
	
	/**
	 * 获取已完成批次数
	 * 
	 * @param user
	 * @return
	 */
	public String findWCCountByOfficeId(String officeId) {
		String sql = "SELECT a.* FROM t_agro_production_batch a WHERE a.states<>'D' AND a.status in('998','999') AND a.base_id IN (SELECT b.id FROM t_agro_base_tree b WHERE b.office_id='" + officeId + "' AND b.states<>'D')";
		List<ProductionBatch> list = productionBatchDao.findBySql(sql,new Parameter(),ProductionBatch.class);
		if(list!=null && list.size()>0){
			return list.size() + " 批";
		}else{
			return "无";
		}
	}
	
	/**
	 * 获取未完成批次数
	 * 
	 * @param user
	 * @return
	 */
	public String findWWCCountByOfficeId(String officeId) {
		String sql = "SELECT a.* FROM t_agro_production_batch a WHERE a.states<>'D' AND a.status='1' AND a.base_id IN (SELECT b.id FROM t_agro_base_tree b WHERE b.office_id='" + officeId + "' AND b.states<>'D')";
		List<ProductionBatch> list = productionBatchDao.findBySql(sql,new Parameter(),ProductionBatch.class);
		if(list!=null && list.size()>0){
			return list.size() + " 批";
		}else{
			return "无";
		}
	}

}
