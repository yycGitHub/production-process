package com.surekam.modules.agro.basemanage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.basemanage.dao.BaseManagerDao;
import com.surekam.modules.agro.basemanage.entity.BaseManager;

/**
 * 基地管理Service
 * 
 * @author tangjun
 * @version 2019-04-09
 */
@Component
@Transactional(readOnly = true)
public class BaseManagerService extends BaseService {

	@Autowired
	private BaseManagerDao baseManagerDao;

	public BaseManager get(String id) {
		return baseManagerDao.get(id);
	}

	@Transactional(readOnly = false)
	public void save(BaseManager agroBaseManager) {
		baseManagerDao.save(agroBaseManager);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		baseManagerDao.deleteById(id);
	}

}
