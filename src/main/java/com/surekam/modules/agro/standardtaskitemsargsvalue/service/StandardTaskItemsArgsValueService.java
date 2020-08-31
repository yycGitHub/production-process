package com.surekam.modules.agro.standardtaskitemsargsvalue.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.standarditemargs.dao.StandardItemArgsDao;
import com.surekam.modules.agro.standarditemargs.entity.StandardItemArgs;
import com.surekam.modules.agro.standarditemargsvalue.dao.StandardItemArgsValueDao;
import com.surekam.modules.agro.standarditemargsvalue.entity.StandardItemArgsValue;
import com.surekam.modules.agro.standardtaskitemsargsvalue.dao.StandardTaskItemsArgsValueDao;
import com.surekam.modules.agro.standardtaskitemsargsvalue.entity.StandardTaskItemsArgsValue;
import com.surekam.modules.sys.entity.User;

/**
 * 标准作业执行记录表Service
 * 
 * @author liwei
 * @version 2019-04-23
 */
@Component
@Transactional(readOnly = true)
public class StandardTaskItemsArgsValueService extends BaseService {

	@Autowired
	private StandardTaskItemsArgsValueDao standardTaskItemsArgsValueDao;
	
	@Autowired
	private StandardItemArgsDao standardItemArgsDao;
	
	@Autowired
	private StandardItemArgsValueDao standardItemArgsValueDao;

	/**
	 * Title: getStandardItemArgsValueList Description: 基地确认采收列表
	 * 
	 * @param user
	 *            用户信息
	 * @param pageno
	 *            页数
	 * @param pagesize
	 *            条数
	 * @param batchId
	 *            批次ID
	 * @return
	 */
	public Page<Map<String, String>> StandardItemArgsValueList(Integer pageno, Integer pagesize, String batchId) {
		int no = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int size = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Map<String, String>> pageByRegionId = standardTaskItemsArgsValueDao.pageByRegionId(batchId, "采收类型", no, size);
		List<Map<String, String>> list = pageByRegionId.getList();
		for (Map<String, String> map : list) {
			String taskListId = "";
			String confirmStates = "";
			String ids = "";
			String taskItemArgsValueName ="";
			String taskItemArgsId = "";

			for (String key : map.keySet()) {
				if ("taskListId".equals(key)) {
					taskListId = map.get(key);
				}
				if ("confirmStates".equals(key)) {
					confirmStates = map.get(key);
				}
				if ("ids".equals(key)) {
					ids = map.get(key);
				}
				if ("taskItemArgsValueName".equals(key)) {
					taskItemArgsValueName = map.get(key);
				}
				if ("taskItemArgsId".equals(key)) {
					taskItemArgsId = map.get(key);
				}
			}
			StandardTaskItemsArgsValue taskListIdAndArgsName = standardTaskItemsArgsValueDao.getTaskListIdAndArgsName(taskListId, "采收量");
			if (null != taskListIdAndArgsName) {
				map.put("taskItemArgsValue", taskListIdAndArgsName.getTaskItemArgsValue());
				map.put("argsUnit", taskListIdAndArgsName.getArgsUnit());
				map.put("id", taskListIdAndArgsName.getId());
			} else {
				map.put("taskItemArgsValue", "0");
				map.put("argsUnit", "");
				map.put("id", "");
			}
			if("1".equals(confirmStates)) {
				map.put("status", "已确认");
			}else {
				map.put("status", "未确认");
			}
		}
		return pageByRegionId;
	}
	
	/**
	 * Title: updateTaskItemArgsValue Description: 修改采收量
	 * 
	 * @param user
	 *            用户
	 * @param id
	 *            主键
	 * @param taskItemArgsValue
	 *            参数值
	 */
	public void updateTaskItemArgsValue(User user, String id, String taskItemArgsValue, String taskItemArgsValueName) {
		StandardTaskItemsArgsValue taskListIdAndArgsName = standardTaskItemsArgsValueDao.get(id);
		taskListIdAndArgsName.setTaskItemArgsValue(taskItemArgsValue);
		taskListIdAndArgsName.setUpdateUserId(user.getId());
		taskListIdAndArgsName.setUpdateTime(new Date());
		standardTaskItemsArgsValueDao.save(taskListIdAndArgsName);
		
		StandardTaskItemsArgsValue stavPojo = standardTaskItemsArgsValueDao.getTaskListIdAndArgsName(taskListIdAndArgsName.getTaskListId(), "采收类型");
		
		StandardItemArgsValue standardItemArgsValue = standardItemArgsValueDao.get(taskItemArgsValueName);
		stavPojo.setTaskItemArgsValue(standardItemArgsValue.getName());
		standardTaskItemsArgsValueDao.save(stavPojo);
	}
	
	/**
	 * Title: getStandardItemArgsValueList Description: 获取采收类型
	 * 
	 * @param id
	 *            标准作业执行记录Id
	 * @return
	 */
	public Map<String, Object> getStandardItemArgsValueList(String id) {
		Map<String, Object> mapList = new HashMap<String, Object>();
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		
		StandardTaskItemsArgsValue taskListIdAndArgsName = standardTaskItemsArgsValueDao.get(id);
		if (null != taskListIdAndArgsName) {
			StandardItemArgs standardItemArgs = standardItemArgsDao.get(taskListIdAndArgsName.getTaskItemArgsId());
			if (null != standardItemArgs) {
				List<StandardItemArgsValue> findByItemArgsId = standardItemArgsValueDao.findByItemArgsId(standardItemArgs.getId());
				for (StandardItemArgsValue standardItemArgsValue : findByItemArgsId) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", standardItemArgsValue.getId());
					map.put("value", standardItemArgsValue.getName());
					listMap.add(map);
				}
				mapList.put("listMap1", listMap);
			}
			
			List<StandardItemArgsValue> findByItemArgsIdAndName = standardItemArgsValueDao.findByItemArgsIdAndName(standardItemArgs.getId(), taskListIdAndArgsName.getTaskItemArgsValue());
			for (StandardItemArgsValue standardItemArgsValue : findByItemArgsIdAndName) {
				mapList.put("listMap2", standardItemArgsValue.getId());
			}
		}
		return mapList;
	}

	public StandardTaskItemsArgsValue getStandardTaskItemsArgsValue(String batchId, String standardItemArgsId) {
		String sql = "select d.*" + "  from" + "  t_agro_product_batch_task a,"
				+ "  t_agro_product_batch_task_resolve b, " + "  t_agro_standard_task_list c, "
				+ "  t_agro_standard_task_items_args_value d " + " where a.id = b.task_id "
				+ "  and b.id = c.task_items_id " + "  and c.id = d.task_list_id " + "  and a.states <> 'D' "
				+ "  and b.states <> 'D' " + "  and c.states <> 'D' " + "  and d.states <> 'D' ";
		if (StringUtils.isNotBlank(batchId)) {
			sql += " and a.region_id = :p1";
		}
		if (StringUtils.isNotBlank(standardItemArgsId)) {
			sql += " and d.task_item_args_id = :p2";
		}
		sql += " order by d.create_time desc limit 1";
		List<StandardTaskItemsArgsValue> list = standardTaskItemsArgsValueDao.findBySql(sql,
				new Parameter(batchId, standardItemArgsId), StandardTaskItemsArgsValue.class);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public StandardTaskItemsArgsValue get(String id) {
		return standardTaskItemsArgsValueDao.get(id);
	}

	public Page<StandardTaskItemsArgsValue> find(Page<StandardTaskItemsArgsValue> page, StandardTaskItemsArgsValue standardTaskItemsArgsValue) {
		DetachedCriteria dc = standardTaskItemsArgsValueDao.createDetachedCriteria();
		dc.add(Restrictions.eq(StandardTaskItemsArgsValue.FIELD_DEL_FLAG, StandardTaskItemsArgsValue.DEL_FLAG_NORMAL));
		return standardTaskItemsArgsValueDao.find(page, dc);
	}

	public List<StandardTaskItemsArgsValue> getStandardTaskItemsArgsValueList(String taskListId) {
		DetachedCriteria dc = standardTaskItemsArgsValueDao.createDetachedCriteria();
		dc.add(Restrictions.ne(StandardTaskItemsArgsValue.FIELD_DEL_FLAG_XGXT, StandardTaskItemsArgsValue.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("taskListId", taskListId));
		dc.addOrder(Order.asc("sort"));
		return standardTaskItemsArgsValueDao.find(dc);
	}

	@Transactional(readOnly = false)
	public void save(StandardTaskItemsArgsValue standardTaskItemsArgsValue) {
		standardTaskItemsArgsValueDao.save(standardTaskItemsArgsValue);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		standardTaskItemsArgsValueDao.deleteById(id);
	}
}
