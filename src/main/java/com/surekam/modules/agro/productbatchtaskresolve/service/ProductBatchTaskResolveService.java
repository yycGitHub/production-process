package com.surekam.modules.agro.productbatchtaskresolve.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.DateUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.label.entity.Label;
import com.surekam.modules.agro.label.service.LabelService;
import com.surekam.modules.agro.productbatchtask.dao.ProductBatchTaskDao;
import com.surekam.modules.agro.productbatchtask.entity.ProductBatchTask;
import com.surekam.modules.agro.productbatchtaskresolve.dao.ProductBatchTaskResolveDao;
import com.surekam.modules.agro.productbatchtaskresolve.entity.ProductBatchTaskResolve;
import com.surekam.modules.agro.standarditemargs.entity.StandardItemArgs;
import com.surekam.modules.agro.standarditemargsvalue.dao.StandardItemArgsValueDao;
import com.surekam.modules.agro.standarditems.entity.StandardItemsApp;
import com.surekam.modules.agro.standardtaskitemsargsvalue.dao.StandardTaskItemsArgsValueDao;
import com.surekam.modules.agro.standardtaskitemsargsvalue.entity.StandardTaskItemsArgsValue;
import com.surekam.modules.agro.standardtasklist.dao.StandardTaskListDao;
import com.surekam.modules.agro.standardtasklist.entity.StandardTaskList;
import com.surekam.modules.sys.entity.User;

/**
 * 分解任务表Service
 * @author liwei
 * @version 2019-04-25
 */
@Component
@Transactional(readOnly = true)
public class ProductBatchTaskResolveService extends BaseService {
	@Autowired
	private ProductBatchTaskDao productBatchTaskDao;
	
	@Autowired
	private ProductBatchTaskResolveDao productBatchTaskResolveDao;
	
	@Autowired
	private StandardTaskListDao standardTaskListDao;
	
	@Autowired
	private StandardTaskItemsArgsValueDao standardTaskItemsArgsValueDao;
	
	@Autowired
	private LabelService labelService;
	
	@Autowired
	private StandardItemArgsValueDao standardItemArgsValueDao;
	
	public ProductBatchTaskResolve get(String id) {
		return productBatchTaskResolveDao.get(id);
	}
	
	public Page<ProductBatchTaskResolve> find(Page<ProductBatchTaskResolve> page, ProductBatchTaskResolve productBatchTaskResolve) {
		DetachedCriteria dc = productBatchTaskResolveDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ProductBatchTaskResolve.FIELD_DEL_FLAG, ProductBatchTaskResolve.DEL_FLAG_NORMAL));
		return productBatchTaskResolveDao.find(page, dc);
	}
	
	public Page<Object> getBatchNotTaskList(Page<Object> page, String batchId) {
		String sql = "select a.id aid,b.id bid,"
			+"  a.standard_item_id,"
			+"  a.standard_item_name,"
			+"  b.dispatch_time,"
			+"  b.finish_time, "
			+"  c.icon_url "
			+"  from"
			+"  t_agro_product_batch_task a,"
			+"  t_agro_product_batch_task_resolve b, "
			+"  t_agro_standard_items c, "
			+"  t_agro_production_batch d "
			+" where a.id = b.task_id "
			+"  and a.standard_item_id = c.id "
			+"  and a.region_id = d.id "
			+"  and d.status = '1' "
			+"  and a.states <> 'D' "
			+"  and b.states <> 'D' "
			+"  and c.states <> 'D' "
			+"  and d.states <> 'D' ";
		if(StringUtils.isNotBlank(batchId)){
			sql+= " and (a.region_id = '" + batchId + "')";
		}
		sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') and (b.finish_time = '' || b.finish_time is null)"
			+ " order by b.dispatch_time desc";
		return productBatchTaskResolveDao.findBySql(page, sql);
	}
	
	public Page<Object> getBatchTaskList(Page<Object> page, String batchId) {
		String sql = "select a.id aid,b.id bid,"
			+"  a.standard_item_id,"
			+"  a.standard_item_name,"
			+"  b.dispatch_time,"
			+"  b.finish_time, "
			+"  c.icon_url "
			+"  from"
			+"  t_agro_product_batch_task a,"
			+"  t_agro_product_batch_task_resolve b, "
			+"  t_agro_standard_items c "
			+" where a.id = b.task_id "
			+"  and a.standard_item_id = c.id "
			+"  and a.states <> 'D' "
			+"  and b.states <> 'D' "
			+"  and c.states <> 'D' ";
		if(StringUtils.isNotBlank(batchId)){
			sql+= " and a.region_id = '" + batchId + "'";
		}
		sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') "
			+" and (b.finish_time != '' && b.finish_time is not null) order by b.finish_time desc";
		return productBatchTaskResolveDao.findBySql(page, sql);
	}
	
	
	public Page<Object> getAllNotTaskListNew(Page<Object> page, User user, String baseId) {
		
		String sql = "select k.aid aid,k.bid bid,  k.standard_item_id,  k.standard_item_name,  k.dispatch_time,  k.finish_time,   k.icon_url FROM ( ";
		 sql += "select DISTINCT a.id aid,b.id bid,"
			+"  a.standard_item_id,"
			+"  a.standard_item_name,"
			+"  b.dispatch_time,"
			+"  b.finish_time, "
			+"  c.icon_url "
			+"  from"
			+"  t_agro_product_batch_task a,"
			+"  t_agro_product_batch_task_resolve b, "
			+"  t_agro_standard_items c,"
			+"  t_agro_production_batch d "
			+" where a.id = b.task_id "
			+"  and a.standard_item_id = c.id "
			+"  and d.status = '1' "
			+"  and a.states <> 'D' "
			+"  and b.states <> 'D' "
			+"  and c.states <> 'D' "
			+"  and d.states <> 'D' "
			+"  and a.region_id in (select g.id from t_agro_base_tree g,t_agro_base_manager h where g.id=h.t_base_id "
			+"  and h.user_id='"+ user.getId() +"' and g.states<>'d' ) ";
		if(StringUtils.isNotBlank(baseId)){
			sql+=" and d.base_id = '" + baseId + "'";
		}
		sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') and (b.finish_time = '' || b.finish_time is null)";
		
		 sql += " 	union all select a.id aid,b.id bid,"
				+"  a.standard_item_id,"
				+"  a.standard_item_name,"
				+"  b.dispatch_time,"
				+"  b.finish_time, "
				+"  c.icon_url "
				+"  from"
				+"  t_agro_product_batch_task a,"
				+"  t_agro_product_batch_task_resolve b, "
				+"  t_agro_standard_items c,"
				+"  t_agro_production_batch d "
				+" where a.id = b.task_id "
				+"  and a.standard_item_id = c.id "
				+"  and a.region_id = d.id "
				+"  and d.status = '1' "
				+"  and a.states <> 'D' "
				+"  and b.states <> 'D' "
				+"  and c.states <> 'D' "
				+"  and d.states <> 'D' "
				+"  and a.region_id in ("
				+" 	select "
				+" 	  t.id "
				+" 	from"
				+" 	  t_agro_production_batch t "
				+" 	where t.base_id in "
				+" 	  (select "
				+" 	    g.id "
				+" 	  from"
				+" 	    t_agro_base_tree g,"
				+" 	    t_agro_base_manager h "
				+" 	  where g.id = h.t_base_id "
				+" 	    and h.user_id = '"+ user.getId() +"' "
				+"	    and g.states <> 'D')"
				+"	    and t.states<>'D' ) ";
			if(StringUtils.isNotBlank(baseId)){
				sql+=" and d.base_id = '" + baseId + "'";
			}
			sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') and (b.finish_time = '' || b.finish_time is null)";
			
			
			 sql += " union all select a.id aid,b.id bid,"
					+"  a.standard_item_id,"
					+"  a.standard_item_name,"
					+"  b.dispatch_time,"
					+"  b.finish_time, "
					+"  c.icon_url "
					+"  from"
					+"  t_agro_product_batch_task a,"
					+"  t_agro_product_batch_task_resolve b, "
					+"  t_agro_standard_items c"
					+" where a.id = b.task_id "
					+"  and a.standard_item_id = c.id "
					+"  and a.states <> 'D' "
					+"  and b.states <> 'D' "
					+"  and c.states <> 'D' "
					+"  and a.region_id in ("
					+"	select c.id from sys_user c where c.id='"+ user.getId() +"')";
				sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') and (b.finish_time = '' || b.finish_time is null)";
				sql+= " ) k ORDER BY k.dispatch_time desc ";
		return productBatchTaskResolveDao.findBySql(page, sql);
	}
	
	
	public Page<Object> getAllNotTaskList(Page<Object> page, User user, String baseId) {
		String sql = "select a.id aid,b.id bid,"
			+"  a.standard_item_id,"
			+"  a.standard_item_name,"
			+"  b.dispatch_time,"
			+"  b.finish_time, "
			+"  c.icon_url "
			+"  from"
			+"  t_agro_product_batch_task a,"
			+"  t_agro_product_batch_task_resolve b, "
			+"  t_agro_standard_items c,"
			+"  t_agro_production_batch d "
			+" where a.id = b.task_id "
			+"  and a.standard_item_id = c.id "
			+"  and a.region_id = d.id "
			+"  and d.status = '1' "
			+"  and a.states <> 'D' "
			+"  and b.states <> 'D' "
			+"  and c.states <> 'D' "
			+"  and d.states <> 'D' "
			+"  and a.region_id in (select g.id from t_agro_base_tree g,t_agro_base_manager h where g.id=h.t_base_id "
			+"  and h.user_id='"+ user.getId() +"' and g.states<>'d' "
			+" 	union all"
			+" 	select "
			+" 	  t.id "
			+" 	from"
			+" 	  t_agro_production_batch t "
			+" 	where t.base_id in "
			+" 	  (select "
			+" 	    g.id "
			+" 	  from"
			+" 	    t_agro_base_tree g,"
			+" 	    t_agro_base_manager h "
			+" 	  where g.id = h.t_base_id "
			+" 	    and h.user_id = '"+ user.getId() +"' "
			+"	    and g.states <> 'D')"
			+"	    and t.states<>'D'"
			+"	union all"
			+"	select c.id from sys_user c where c.id='"+ user.getId() +"')";
		if(StringUtils.isNotBlank(baseId)){
			sql+=" and d.base_id = '" + baseId + "'";
		}
		sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') and (b.finish_time = '' || b.finish_time is null)"
				+ " order by b.dispatch_time desc";
		return productBatchTaskResolveDao.findBySql(page, sql);
	}
	
	public Page<Object> getAllTaskList(Page<Object> page, User user, String baseId) {
		String sql = "select k.aid aid,k.bid bid,  k.standard_item_id,  k.standard_item_name,  k.dispatch_time,  k.finish_time,   k.icon_url from ( "
				+" select a.id aid,b.id bid,"
				+"  a.standard_item_id,"
				+"  a.standard_item_name,"
				+"  b.dispatch_time,"
				+"  b.finish_time, "
				+"  c.icon_url "
				+"  from"
				+"  t_agro_product_batch_task a,"
				+"  t_agro_product_batch_task_resolve b, "
				+"  t_agro_standard_items c, "
				+"  t_agro_production_batch d"
				+" where a.id = b.task_id "
				+"  and a.standard_item_id = c.id "
				+"  and a.standard_item_id = c.id "
				+"  and a.region_id = d.id "
				+"  and a.states <> 'D' "
				+"  and b.states <> 'D' "
				+"  and c.states <> 'D' "
				+"  and d.states <> 'D' "
				+"  and a.region_id in (select g.id from t_agro_base_tree g,t_agro_base_manager h where g.id=h.t_base_id "
				+"  and h.user_id='"+ user.getId() +"' and g.states<>'d' "
				+" 	union all"
				+" 	select "
				+" 	  t.id "
				+" 	from"
				+" 	  t_agro_production_batch t "
				+" 	where t.base_id in "
				+" 	  (select "
				+" 	    g.id "
				+" 	  from"
				+" 	    t_agro_base_tree g,"
				+" 	    t_agro_base_manager h "
				+" 	  where g.id = h.t_base_id "
				+" 	    and h.user_id = '"+ user.getId() +"' "
				+"	    and g.states <> 'D')"
				+"	    and t.states<>'D'"
				+"	union all"
				+"	select c.id from sys_user c where c.id='"+ user.getId() +"')";
			if(StringUtils.isNotBlank(baseId)){
				sql+=" and d.base_id = '" + baseId + "'";
			}
			sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') and (b.finish_time != '' && b.finish_time is not null) "; 
			sql+= " union all ";
			sql+= " select a.id aid,b.id bid,"
					+"  a.standard_item_id,"
					+"  a.standard_item_name,"
					+"  b.dispatch_time,"
					+"  b.finish_time, "
					+"  c.icon_url "
					+"  from"
					+"  t_agro_product_batch_task a,"
					+"  t_agro_product_batch_task_resolve b, "
					+"  t_agro_standard_items c "
					+" where a.id = b.task_id "
					+"  and a.standard_item_id = c.id "
					+"  and a.states <> 'D' "
					+"  and b.states <> 'D' "
					+"  and c.states <> 'D' "
					+"  and a.region_id in (select g.id from sys_user g where g.id='"+ user.getId() +"') ";
				sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') "
						+ "and (b.finish_time != '' && b.finish_time is not null) ) k order by k.finish_time desc";
		return productBatchTaskResolveDao.findBySql(page, sql);
	}
	
	public String getBatchCount(String batchId, String type) {
		String sql = "select b.id "
			+"  from"
			+"  t_agro_product_batch_task a,"
			+"  t_agro_product_batch_task_resolve b, "
			+"  t_agro_production_batch d "
			+" where a.id = b.task_id "
			+"  and a.region_id = d.id "
			+"  and a.states <> 'D' "
			+"  and b.states <> 'D' "
			+"  and d.states <> 'D' ";
		if(StringUtils.isNotBlank(batchId)){
			sql+= " and (a.region_id = '" + batchId + "')";
		}
		sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') ";
		if("0".equals(type)){
			sql+=" and (b.finish_time = '' || b.finish_time is null)";
			sql+=" and d.status = '1'";
		}else if("1".equals(type)){
			sql+=" and (b.finish_time != '' && b.finish_time is not null)";
		}
		List<String> list = productBatchTaskResolveDao.findBySql(sql);
		return list.size()+"";
	}
	
	public String getAllCountnew(String type, User user, String baseId) {
		String sql = "select k.aid aid,k.bid bid,  k.standard_item_id,  k.standard_item_name,  k.dispatch_time,  k.finish_time,   k.icon_url FROM ( ";
		 sql += "select DISTINCT a.id aid,b.id bid,"
			+"  a.standard_item_id,"
			+"  a.standard_item_name,"
			+"  b.dispatch_time,"
			+"  b.finish_time, "
			+"  c.icon_url "
			+"  from"
			+"  t_agro_product_batch_task a,"
			+"  t_agro_product_batch_task_resolve b, "
			+"  t_agro_standard_items c,"
			+"  t_agro_production_batch d "
			+" where a.id = b.task_id "
			+"  and a.standard_item_id = c.id "
			+"  and d.status = '1' "
			+"  and a.states <> 'D' "
			+"  and b.states <> 'D' "
			+"  and c.states <> 'D' "
			+"  and d.states <> 'D' "
			+"  and a.region_id in (select g.id from t_agro_base_tree g,t_agro_base_manager h where g.id=h.t_base_id "
			+"  and h.user_id='"+ user.getId() +"' and g.states<>'d' ) ";
		if(StringUtils.isNotBlank(baseId)){
			sql+=" and d.base_id = '" + baseId + "'";
		}
		sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') and (b.finish_time = '' || b.finish_time is null)";
		
		 sql += " 	union all select a.id aid,b.id bid,"
				+"  a.standard_item_id,"
				+"  a.standard_item_name,"
				+"  b.dispatch_time,"
				+"  b.finish_time, "
				+"  c.icon_url "
				+"  from"
				+"  t_agro_product_batch_task a,"
				+"  t_agro_product_batch_task_resolve b, "
				+"  t_agro_standard_items c,"
				+"  t_agro_production_batch d "
				+" where a.id = b.task_id "
				+"  and a.standard_item_id = c.id "
				+"  and a.region_id = d.id "
				+"  and d.status = '1' "
				+"  and a.states <> 'D' "
				+"  and b.states <> 'D' "
				+"  and c.states <> 'D' "
				+"  and d.states <> 'D' "
				+"  and a.region_id in ("
				+" 	select "
				+" 	  t.id "
				+" 	from"
				+" 	  t_agro_production_batch t "
				+" 	where t.base_id in "
				+" 	  (select "
				+" 	    g.id "
				+" 	  from"
				+" 	    t_agro_base_tree g,"
				+" 	    t_agro_base_manager h "
				+" 	  where g.id = h.t_base_id "
				+" 	    and h.user_id = '"+ user.getId() +"' "
				+"	    and g.states <> 'D')"
				+"	    and t.states<>'D' ) ";
			if(StringUtils.isNotBlank(baseId)){
				sql+=" and d.base_id = '" + baseId + "'";
			}
			sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') and (b.finish_time = '' || b.finish_time is null)";
			
			
			 sql += " union all select a.id aid,b.id bid,"
					+"  a.standard_item_id,"
					+"  a.standard_item_name,"
					+"  b.dispatch_time,"
					+"  b.finish_time, "
					+"  c.icon_url "
					+"  from"
					+"  t_agro_product_batch_task a,"
					+"  t_agro_product_batch_task_resolve b, "
					+"  t_agro_standard_items c"
					+" where a.id = b.task_id "
					+"  and a.standard_item_id = c.id "
					+"  and a.states <> 'D' "
					+"  and b.states <> 'D' "
					+"  and c.states <> 'D' "
					+"  and a.region_id in ("
					+"	select c.id from sys_user c where c.id='"+ user.getId() +"')";
				sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') and (b.finish_time = '' || b.finish_time is null)";
				sql+= " ) k ORDER BY k.dispatch_time desc ";
				List<String> list = productBatchTaskResolveDao.findBySql(sql);
		return list.size()+"";
	}
	
	public String getAllCount(String type, User user, String officeId) {
		String sql = "select b.id "
			+"  from"
			+"  t_agro_product_batch_task a,"
			+"  t_agro_product_batch_task_resolve b, "
			+"  t_agro_production_batch d "
			+" where a.id = b.task_id "
			+"  and a.region_id = d.id "
			+"  and a.states <> 'D' "
			+"  and b.states <> 'D' "
			+"  and d.states <> 'D' "
			+"  and a.region_id in (select g.id from t_agro_base_tree g,t_agro_base_manager h where g.id=h.t_base_id "
			+"  and h.user_id='"+ user.getId() +"' and g.states<>'D' "
			+" 	union all"
			+" 	select "
			+" 	  t.id "
			+" 	from"
			+" 	  t_agro_production_batch t "
			+" 	where t.base_id in "
			+" 	  (select "
			+" 	    g.id "
			+" 	  from"
			+" 	    t_agro_base_tree g,"
			+" 	    t_agro_base_manager h "
			+" 	  where g.id = h.t_base_id "
			+" 	    and h.user_id = '"+ user.getId() +"' "
			+"	    and g.states <> 'D')"
			+"	    and t.states<>'D'"
			+"	union all"
			+"	select c.id from sys_user c where c.id='"+ user.getId() +"')";
		sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') ";
		/*if("0".equals(type)){
			sql+=" and (b.finish_time = '' || b.finish_time is null)";
			sql+=" and d.status = '1'";
		}else if("1".equals(type)){*/
			sql+=" and (b.finish_time != '' && b.finish_time is not null)";
		//}
		if(StringUtils.isNotBlank(officeId)){
			sql+=" and d.base_id IN (SELECT z.id FROM t_agro_base_tree z WHERE z.office_id='" + officeId + "' AND z.states<>'D')";
		}
		List<String> list = productBatchTaskResolveDao.findBySql(sql);
		return list.size()+"";
	}
	
	@Transactional(readOnly = false)
	public void save(ProductBatchTaskResolve productBatchTaskResolve) {
		productBatchTaskResolveDao.save(productBatchTaskResolve);
	}
	
	@Transactional(readOnly = false)
	public void saveTaskConfirmStates(String id) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ProductBatchTaskResolve productBatchTaskResolve = productBatchTaskResolveDao.get(id);
		productBatchTaskResolve.setConfirmStates("1");
		productBatchTaskResolve.setConfirmTime(sdf.format(new Date()));
		productBatchTaskResolveDao.save(productBatchTaskResolve);
	}
	
	@Transactional(readOnly = false)
	public void saveExecutionTaskData(StandardItemsApp standardItems,User user, String address, String latitude, String longitude) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		//更新子任务表
		ProductBatchTaskResolve productBatchTaskResolve = productBatchTaskResolveDao.get(standardItems.getTaskId());
		productBatchTaskResolve.setStates("U");
		productBatchTaskResolve.setExecutionStatus("1");
		productBatchTaskResolve.setUpdateTime(new Date());
		productBatchTaskResolve.setUpdateUserId(user.getId());
		productBatchTaskResolve.setLongitude(longitude);
		productBatchTaskResolve.setLatitude(latitude);
		productBatchTaskResolve.setAddress(address);
		
		//更新作业记录主表数据
		StandardTaskList standardTaskList = new StandardTaskList();
		standardTaskList.setAreaId("1");
		standardTaskList.setCreateUserId(user.getId());
		standardTaskList.setTaskItemsId(standardItems.getTaskId());
		standardTaskList.setProductionBatch(standardItems.getBatchId());
		standardTaskListDao.save(standardTaskList);
		
		//插入执行记录表数据
		List<StandardItemArgs> standardItemArgsList = standardItems.getStandardItemArgsList();
		for (StandardItemArgs standardItemArgs : standardItemArgsList) {
			StandardTaskItemsArgsValue standardTaskItemsArgsValue = new StandardTaskItemsArgsValue();
			standardTaskItemsArgsValue.setTaskListId(standardTaskList.getId());
			standardTaskItemsArgsValue.setCreateUserId(user.getId());
			standardTaskItemsArgsValue.setTaskItemArgsId(standardItemArgs.getId());
			standardTaskItemsArgsValue.setArgsName(standardItemArgs.getArgsName());
			standardTaskItemsArgsValue.setTaskItemArgsValue(standardItemArgs.getDefaultValue());
			standardTaskItemsArgsValue.setArgsUnit(standardItemArgs.getArgsUnit());
			standardTaskItemsArgsValue.setArgsType(standardItemArgs.getArgsType());
			standardTaskItemsArgsValue.setSort(standardItemArgs.getSort());
			
			standardTaskItemsArgsValueDao.save(standardTaskItemsArgsValue);
			
			if(standardItemArgs.getArgsName().equals("日期") && StringUtils.isNotBlank(standardItemArgs.getDefaultValue())){
				productBatchTaskResolve.setFinishTime(standardItemArgs.getDefaultValue().replace("年", "-").replace("月", "-").replace("日", ""));
			}
			
			if(standardItemArgs.getArgsType().equals("9")){
				if(StringUtils.isNotBlank(standardItemArgs.getDefaultValue())){
					standardItemArgsValueDao.save(standardItemArgs,user);
				}
			}
		}
		
		if(StringUtils.isBlank(productBatchTaskResolve.getFinishTime())){
			productBatchTaskResolve.setFinishTime(sdf.format(new Date()));
		}
		productBatchTaskResolveDao.save(productBatchTaskResolve);
		
		//更新主任务表数据
		ProductBatchTask productBatchTask = productBatchTaskDao.get(productBatchTaskResolve.getTaskId());
		String count = "0";
		if(StringUtils.isNotBlank(productBatchTask.getFinishedTimes())){
			count = productBatchTask.getFinishedTimes();
		}
		productBatchTask.setFinishedTimes((Integer.parseInt(count)+1)+"");
		productBatchTask.setUpdateTime(new Date());
		productBatchTask.setUpdateUserId(user.getId());
		productBatchTaskDao.save(productBatchTask);
	}
	
	@Transactional(readOnly = false)
	public void saveZZExecutionTaskData(StandardItemsApp standardItems,User user, String address, String latitude, String longitude) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat d_sdf = new SimpleDateFormat("dd");
		//添加主任务
		ProductBatchTask productBatchTask = new ProductBatchTask();
		productBatchTask.setRegionType(ProductBatchTask.TASK_BATCH);
		productBatchTask.setRegionId(standardItems.getBatchId());
		productBatchTask.setStandardItemId(standardItems.getId());
		productBatchTask.setStandardItemName(standardItems.getItemName());
		productBatchTask.setGrowthCycleId(standardItems.getGrowthCycleId());
		productBatchTask.setGrowthCycleName(standardItems.getGrowthCycleName());
		productBatchTask.setStartDateNumber(d_sdf.format(new Date()));
		productBatchTask.setEndDateNumber(d_sdf.format(new Date()));
		productBatchTask.setStartDate(new Date());
		productBatchTask.setEndDate(new Date());
		productBatchTask.setWorkEstimation("1");
		productBatchTask.setIsProductDemands("0");
		productBatchTask.setFrequencyDuringTime("1");
		productBatchTask.setFinishedTimes("1");
		productBatchTask.setCreateUserId(user.getId());
		productBatchTaskDao.save(productBatchTask);
		
		//添加子任务表
		ProductBatchTaskResolve productBatchTaskResolve = new ProductBatchTaskResolve();
		productBatchTaskResolve.setTaskId(productBatchTask.getId());
		productBatchTaskResolve.setDispatchTime(sdf.format(new Date()));
		productBatchTaskResolve.setStates("U");
		productBatchTaskResolve.setExecutionStatus("1");
		productBatchTaskResolve.setUpdateTime(new Date());
		productBatchTaskResolve.setUpdateUserId(user.getId());
		productBatchTaskResolve.setCreateUserId(user.getId());
		productBatchTaskResolve.setLongitude(longitude);
		productBatchTaskResolve.setLatitude(latitude);
		productBatchTaskResolve.setAddress(address);
		SimpleDateFormat sdfs=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String newDate=sdfs.format(new Date());
		productBatchTaskResolve.setSerialNumber(newDate);
		productBatchTaskResolveDao.save(productBatchTaskResolve);
		
		//更新作业记录主表数据
		StandardTaskList standardTaskList = new StandardTaskList();
		standardTaskList.setAreaId("1");
		standardTaskList.setCreateUserId(user.getId());
		standardTaskList.setTaskItemsId(productBatchTaskResolve.getId());
		standardTaskList.setProductionBatch(standardItems.getBatchId());
		standardTaskListDao.save(standardTaskList);
		
		//插入执行记录表数据
		List<StandardItemArgs> standardItemArgsList = standardItems.getStandardItemArgsList();
		for (StandardItemArgs standardItemArgs : standardItemArgsList) {
			StandardTaskItemsArgsValue standardTaskItemsArgsValue = new StandardTaskItemsArgsValue();
			standardTaskItemsArgsValue.setTaskListId(standardTaskList.getId());
			standardTaskItemsArgsValue.setCreateUserId(user.getId());
			standardTaskItemsArgsValue.setTaskItemArgsId(standardItemArgs.getId());
			standardTaskItemsArgsValue.setArgsName(standardItemArgs.getArgsName());
			standardTaskItemsArgsValue.setTaskItemArgsValue(standardItemArgs.getDefaultValue());
			standardTaskItemsArgsValue.setArgsUnit(standardItemArgs.getArgsUnit());
			standardTaskItemsArgsValue.setArgsType(standardItemArgs.getArgsType());
			standardTaskItemsArgsValue.setSort(standardItemArgs.getSort());
			standardTaskItemsArgsValueDao.save(standardTaskItemsArgsValue);
			
			if(standardItemArgs.getArgsName().equals("日期") && StringUtils.isNotBlank(standardItemArgs.getDefaultValue())){
				productBatchTaskResolve.setFinishTime(standardItemArgs.getDefaultValue().replace("年", "-").replace("月", "-").replace("日", ""));
			}
			
			if(standardItemArgs.getArgsType().equals("9")){
				if(StringUtils.isNotBlank(standardItemArgs.getDefaultValue())){
					standardItemArgsValueDao.save(standardItemArgs,user);
				}
			}
		}
		productBatchTaskResolveDao.save(productBatchTaskResolve);
	}
	
	@Transactional(readOnly = false)
	public void saveNonExecutionTaskData(ProductBatchTaskResolve productBatchTaskResolve,User user, String address, String latitude, String longitude) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//更新子任务表
		productBatchTaskResolve.setFinishTime(sdf.format(new Date()));
		productBatchTaskResolve.setStates("U");
		productBatchTaskResolve.setExecutionStatus("0");
		productBatchTaskResolve.setUpdateTime(new Date());
		productBatchTaskResolve.setUpdateUserId(user.getId());
		productBatchTaskResolve.setLongitude(longitude);
		productBatchTaskResolve.setLatitude(latitude);
		productBatchTaskResolve.setAddress(address);
		productBatchTaskResolveDao.save(productBatchTaskResolve);
		
		//更新不执行原因标签表
		if(!labelService.exsitLabelContent(productBatchTaskResolve.getNonexecutionReason(),user)){
			Label label = new Label();
			label.setCreateUserId(user.getId());
			label.setLabelType("1");
			label.setLabelContent(productBatchTaskResolve.getNonexecutionReason());
			label.setUserId(user.getId());
			labelService.save(label);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		productBatchTaskResolveDao.deleteById(id);
	}
	
	public Page<Object> getCollectionTaskList(Page<Object> page, String baseId) {
		String sql = "select a.id aid,b.id bid,"
			+"  a.standard_item_id,"
			+"  a.standard_item_name,"
			+"  b.dispatch_time,"
			+"  b.finish_time, "
			+"  c.icon_url, "
			+"  b.confirm_states, "
			+"  b.confirm_time, "
			+"  d.batch_code "
			+"  from"
			+"  t_agro_product_batch_task a,"
			+"  t_agro_product_batch_task_resolve b, "
			+"  t_agro_standard_items c, "
			+"  t_agro_production_batch d, "
			+"  t_agro_standard_task_list e"
			+" where a.id = b.task_id "
			+"  and a.standard_item_id = c.id "
			+"  and a.region_id = d.id "
			+"  AND b.id = e.task_items_id "
			+"  and c.item_category_id = 'fb08f0fe80474919bf9d7c14c8ff65b2'"
			+"  and d.harvest_status = '1'"
			+"  and a.states <> 'D' "
			+"  and b.states <> 'D' "
			+"  and c.states <> 'D' "
			+"  and d.states <> 'D' "
			+"  and e.states <> 'D' ";
		if(StringUtils.isNotBlank(baseId)){
			sql+= " and d.base_id = '" + baseId + "'";
		}
		sql+= " and (b.finish_time != '' && b.finish_time is not null) order by b.confirm_states, b.finish_time desc";
		return productBatchTaskResolveDao.findBySql(page, sql);
	}
	
	public int getCollectionTaskCount(String baseId) {
		String sql = "select a.id aid,b.id bid,"
			+"  a.standard_item_id,"
			+"  a.standard_item_name,"
			+"  b.dispatch_time,"
			+"  b.finish_time, "
			+"  c.icon_url, "
			+"  b.confirm_states, "
			+"  b.confirm_time, "
			+"  d.batch_code "
			+"  from"
			+"  t_agro_product_batch_task a,"
			+"  t_agro_product_batch_task_resolve b, "
			+"  t_agro_standard_items c, "
			+"  t_agro_production_batch d, "
			+"  t_agro_standard_task_list e"
			+" where a.id = b.task_id "
			+"  and a.standard_item_id = c.id "
			+"  and a.region_id = d.id "
			+"  AND b.id = e.task_items_id "
			+"  and c.item_category_id = 'fb08f0fe80474919bf9d7c14c8ff65b2'"
			+"  and d.harvest_status = '1'"
			+"  and a.states <> 'D' "
			+"  and b.states <> 'D' "
			+"  and c.states <> 'D' "
			+"  and d.states <> 'D' "
			+"  and e.states <> 'D' ";
		if(StringUtils.isNotBlank(baseId)){
			sql+= " and d.base_id = '" + baseId + "'";
		}
		sql+= " and (b.finish_time != '' && b.finish_time is not null) order by b.finish_time desc";
		return productBatchTaskResolveDao.findBySql(sql).size();
	}

	//已确认数
	public int getCollectionTaskCount2(String baseId) {
		String sql = "select a.id aid,b.id bid,"
			+"  a.standard_item_id,"
			+"  a.standard_item_name,"
			+"  b.dispatch_time,"
			+"  b.finish_time, "
			+"  c.icon_url, "
			+"  b.confirm_states, "
			+"  b.confirm_time, "
			+"  d.batch_code "
			+"  from"
			+"  t_agro_product_batch_task a,"
			+"  t_agro_product_batch_task_resolve b, "
			+"  t_agro_standard_items c, "
			+"  t_agro_production_batch d, "
			+"  t_agro_standard_task_list e"
			+" where a.id = b.task_id "
			+"  and a.standard_item_id = c.id "
			+"  and a.region_id = d.id "
			+"  AND b.id = e.task_items_id "
			+"  and c.item_category_id = 'fb08f0fe80474919bf9d7c14c8ff65b2'"
			+"  and d.harvest_status = '1'"
			+"  and a.states <> 'D' "
			+"  and b.states <> 'D' "
			+"  and c.states <> 'D' "
			+"  and d.states <> 'D' "
			+"  and e.states <> 'D' ";
		if(StringUtils.isNotBlank(baseId)){
			sql+= " and d.base_id = '" + baseId + "'";
		}
		sql+= " and (b.finish_time != '' && b.finish_time is not null)  and b.confirm_states ='1' order by b.finish_time desc";
		return productBatchTaskResolveDao.findBySql(sql).size();
	}
	//未确认数
	public int getCollectionTaskCount3(String baseId) {
		String sql = "select a.id aid,b.id bid,"
			+"  a.standard_item_id,"
			+"  a.standard_item_name,"
			+"  b.dispatch_time,"
			+"  b.finish_time, "
			+"  c.icon_url, "
			+"  b.confirm_states, "
			+"  b.confirm_time, "
			+"  d.batch_code "
			+"  from"
			+"  t_agro_product_batch_task a,"
			+"  t_agro_product_batch_task_resolve b, "
			+"  t_agro_standard_items c, "
			+"  t_agro_production_batch d, "
			+"  t_agro_standard_task_list e"
			+" where a.id = b.task_id "
			+"  and a.standard_item_id = c.id "
			+"  and a.region_id = d.id "
			+"  AND b.id = e.task_items_id "
			+"  and c.item_category_id = 'fb08f0fe80474919bf9d7c14c8ff65b2'"
			+"  and d.harvest_status = '1'"
			+"  and a.states <> 'D' "
			+"  and b.states <> 'D' "
			+"  and c.states <> 'D' "
			+"  and d.states <> 'D' "
			+"  and e.states <> 'D' ";
		if(StringUtils.isNotBlank(baseId)){
			sql+= " and d.base_id = '" + baseId + "'";
		}
		sql+= " and (b.finish_time != '' && b.finish_time is not null)  and b.confirm_states is null order by b.finish_time desc";
		return productBatchTaskResolveDao.findBySql(sql).size();
	}
	
	
	/**
	 * Title: updConfirmStates Description: 修改确认状态
	 * 
	 * @param id
	 *            主键
	 * @return
	 */
	public String updConfirmStates(String id) {
		ProductBatchTaskResolve productBatchTaskResolve = productBatchTaskResolveDao.get(id);
		productBatchTaskResolveDao.clear();
		productBatchTaskResolveDao.flush();
		// 1为已确认
		productBatchTaskResolve.setConfirmStates("1");
		productBatchTaskResolve.setConfirmTime(DateUtil.getLastDay1());
		productBatchTaskResolveDao.save(productBatchTaskResolve);
		return "Success";
	}
	
	
	public String getAllTaskListCount(User user, String baseId) {
		String sql = "select k.aid aid,k.bid bid,  k.standard_item_id,  k.standard_item_name,  k.dispatch_time,  k.finish_time,   k.icon_url from ( "
			+" select a.id aid,b.id bid,"
			+"  a.standard_item_id,"
			+"  a.standard_item_name,"
			+"  b.dispatch_time,"
			+"  b.finish_time, "
			+"  c.icon_url "
			+"  from"
			+"  t_agro_product_batch_task a,"
			+"  t_agro_product_batch_task_resolve b, "
			+"  t_agro_standard_items c, "
			+"  t_agro_production_batch d"
			+" where a.id = b.task_id "
			+"  and a.standard_item_id = c.id "
			+"  and a.standard_item_id = c.id "
			+"  and a.region_id = d.id "
			+"  and a.states <> 'D' "
			+"  and b.states <> 'D' "
			+"  and c.states <> 'D' "
			+"  and d.states <> 'D' "
			+"  and a.region_id in (select g.id from t_agro_base_tree g,t_agro_base_manager h where g.id=h.t_base_id "
			+"  and h.user_id='"+ user.getId() +"' and g.states<>'d' "
			+" 	union all"
			+" 	select "
			+" 	  t.id "
			+" 	from"
			+" 	  t_agro_production_batch t "
			+" 	where t.base_id in "
			+" 	  (select "
			+" 	    g.id "
			+" 	  from"
			+" 	    t_agro_base_tree g,"
			+" 	    t_agro_base_manager h "
			+" 	  where g.id = h.t_base_id "
			+" 	    and h.user_id = '"+ user.getId() +"' "
			+"	    and g.states <> 'D')"
			+"	    and t.states<>'D'"
			+"	union all"
			+"	select c.id from sys_user c where c.id='"+ user.getId() +"')";
		if(StringUtils.isNotBlank(baseId)){
			sql+=" and d.base_id = '" + baseId + "'";
		}
		sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') and (b.finish_time != '' && b.finish_time is not null) "; 
		sql+= " union all ";
		sql+= " select a.id aid,b.id bid,"
				+"  a.standard_item_id,"
				+"  a.standard_item_name,"
				+"  b.dispatch_time,"
				+"  b.finish_time, "
				+"  c.icon_url "
				+"  from"
				+"  t_agro_product_batch_task a,"
				+"  t_agro_product_batch_task_resolve b, "
				+"  t_agro_standard_items c "
				+" where a.id = b.task_id "
				+"  and a.standard_item_id = c.id "
				+"  and a.states <> 'D' "
				+"  and b.states <> 'D' "
				+"  and c.states <> 'D' "
				+"  and a.region_id in (select g.id from sys_user g where g.id='"+ user.getId() +"') ";
			sql+= " and REPLACE(b.dispatch_time, '-', '') <= date_format(sysdate(), '%Y%m%d') "
					+ "and (b.finish_time != '' && b.finish_time is not null) ) k order by k.finish_time desc";
		int num = productBatchTaskResolveDao.findBySql3(sql).size();
		return String.valueOf(num);
	}

	/**
	 * 根据任务的流水号查询信息
	 * @return
	 */
	public List<Object> getBatchCode(String serialNumber){
		String sql = "SELECT p.batch_code,t.id FROM t_agro_product_batch_task_resolve t"
				+ " LEFT JOIN t_agro_product_batch_task b ON t.task_id=b.id"
				+ " LEFT JOIN t_agro_production_batch p ON p.id=b.region_id"
				+ " WHERE t.serial_number like '"+serialNumber+"'";
		return productBatchTaskResolveDao.findBySql(sql);	
	}
}
