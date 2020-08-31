package com.surekam.modules.agro.systementerprisestandards.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.systementerprisestandards.entity.SystemEnterpriseStandards;
import com.surekam.modules.agro.systementerprisestandards.dao.SystemEnterpriseStandardsDao;

/**
 * 标准库(系统标准+企业标准)Service
 * 
 * @author liwei
 * @version 2019-04-23
 */
@Component
@Transactional(readOnly = true)
public class SystemEnterpriseStandardsService extends BaseService {

	@Autowired
	private SystemEnterpriseStandardsDao systemEnterpriseStandardsDao;

	public SystemEnterpriseStandards get(String id) {
		return systemEnterpriseStandardsDao.get(id);
	}

	public Page<SystemEnterpriseStandards> find(Page<SystemEnterpriseStandards> page,
			SystemEnterpriseStandards systemEnterpriseStandards) {
		DetachedCriteria dc = systemEnterpriseStandardsDao.createDetachedCriteria();
		dc.add(Restrictions.eq(SystemEnterpriseStandards.FIELD_DEL_FLAG, SystemEnterpriseStandards.DEL_FLAG_NORMAL));
		return systemEnterpriseStandardsDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(SystemEnterpriseStandards systemEnterpriseStandards) {
		systemEnterpriseStandardsDao.save(systemEnterpriseStandards);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		systemEnterpriseStandardsDao.deleteById(id);
	}

}
