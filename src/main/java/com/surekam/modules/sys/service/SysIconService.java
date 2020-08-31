package com.surekam.modules.sys.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.sys.dao.SysIconDao;
import com.surekam.modules.sys.entity.SysIcon;

/**
 * 系统图标表Service
 * @author luoxw
 * @version 2019-05-16
 */
@Component
@Transactional(readOnly = true)
public class SysIconService extends BaseService {

	@Autowired
	private SysIconDao sysIconDao;
	
	public SysIcon get(String id) {
		return sysIconDao.get(id);
	}
	
	public List<SysIcon> find() {
		DetachedCriteria dc = sysIconDao.createDetachedCriteria();
		dc.add(Restrictions.ne(SysIcon.FIELD_DEL_FLAG_XGXT, SysIcon.STATE_FLAG_DEL));
		return sysIconDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(SysIcon sysIcon) {
		sysIconDao.save(sysIcon);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		sysIconDao.deleteById(id);
	}
	
}
