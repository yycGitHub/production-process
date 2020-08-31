package com.surekam.modules.agro.standardtasklist.service;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.standardtasklist.entity.StandardTaskList;
import com.surekam.modules.agro.standardtasklist.dao.StandardTaskListDao;

/**
 * 作业记录主数据表Service
 * @author liwei
 * @version 2019-04-23
 */
@Component
@Transactional(readOnly = true)
public class StandardTaskListService extends BaseService {

	@Autowired
	private StandardTaskListDao standardTaskListDao;
	
	public StandardTaskList get(String id) {
		return standardTaskListDao.get(id);
	}
	
	public Page<StandardTaskList> find(Page<StandardTaskList> page, StandardTaskList standardTaskList) {
		DetachedCriteria dc = standardTaskListDao.createDetachedCriteria();
		dc.add(Restrictions.eq(StandardTaskList.FIELD_DEL_FLAG, StandardTaskList.DEL_FLAG_NORMAL));
		return standardTaskListDao.find(page, dc);
	}
	
	public StandardTaskList getStandardTaskList(String taskId) {
		DetachedCriteria dc = standardTaskListDao.createDetachedCriteria();
		dc.add(Restrictions.ne(StandardTaskList.FIELD_DEL_FLAG_XGXT, StandardTaskList.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("taskItemsId", taskId));
		List<StandardTaskList> list = standardTaskListDao.find(dc);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Transactional(readOnly = false)
	public void save(StandardTaskList standardTaskList) {
		standardTaskListDao.save(standardTaskList);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		standardTaskListDao.deleteById(id);
	}
	
}
