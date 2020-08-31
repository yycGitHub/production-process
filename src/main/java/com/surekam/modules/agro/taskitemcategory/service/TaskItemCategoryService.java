package com.surekam.modules.agro.taskitemcategory.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.taskitemcategory.entity.TaskItemCategory;
import com.surekam.modules.agro.taskitemcategory.dao.TaskItemCategoryDao;

/**
 * 作业项类别表(包括 施肥 投料等 )Service
 * @author liwei
 * @version 2019-04-23
 */
@Component
@Transactional(readOnly = true)
public class TaskItemCategoryService extends BaseService {

	@Autowired
	private TaskItemCategoryDao taskItemCategoryDao;
	
	public TaskItemCategory get(String id) {
		return taskItemCategoryDao.get(id);
	}
	
	public Page<TaskItemCategory> find(Page<TaskItemCategory> page, TaskItemCategory taskItemCategory) {
		DetachedCriteria dc = taskItemCategoryDao.createDetachedCriteria();
		dc.add(Restrictions.eq(TaskItemCategory.FIELD_DEL_FLAG, TaskItemCategory.DEL_FLAG_NORMAL));
		return taskItemCategoryDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(TaskItemCategory taskItemCategory) {
		taskItemCategoryDao.save(taskItemCategory);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		taskItemCategoryDao.deleteById(id);
	}
	
}
