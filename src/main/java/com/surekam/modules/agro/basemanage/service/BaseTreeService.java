package com.surekam.modules.agro.basemanage.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.basemanage.dao.BaseTreeDao;
import com.surekam.modules.agro.basemanage.entity.BaseTree;

/**
 * 基地管理Service
 * 
 * @author tangjun
 * @version 2019-04-10
 */
@Component
@Transactional(readOnly = true)
public class BaseTreeService extends BaseService {

	@Autowired
	private BaseTreeDao BaseTreeDao;

	public BaseTree get(String id) {
		return BaseTreeDao.get(id);
	}

	public Page<BaseTree> find(Page<BaseTree> page, BaseTree BaseTree) {
		DetachedCriteria dc = BaseTreeDao.createDetachedCriteria();
		dc.add(Restrictions.eq(BaseTree.FIELD_DEL_FLAG, BaseTree.DEL_FLAG_NORMAL));
		return BaseTreeDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(BaseTree BaseTree) {
		BaseTreeDao.save(BaseTree);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		BaseTreeDao.deleteById(id);
	}

}
