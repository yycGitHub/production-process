package com.surekam.modules.agro.file.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.file.dao.AgroFileInfoDao;
import com.surekam.modules.agro.file.entity.AgroFileInfo;

/**
 * 附件信息Service
 * @author xy
 * @version 2019-04-24
 */
@Component
@Transactional(readOnly = true)
public class AgroFileInfoService extends BaseService {

	@Autowired
	private AgroFileInfoDao agroFileInfoDao;
	
	public AgroFileInfo get(String id) {
		return agroFileInfoDao.get(id);
	}
	
	public Page<AgroFileInfo> find(Page<AgroFileInfo> page, AgroFileInfo agroFileInfo) {
		DetachedCriteria dc = agroFileInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq(AgroFileInfo.FIELD_DEL_FLAG, AgroFileInfo.DEL_FLAG_NORMAL));
		return agroFileInfoDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(AgroFileInfo agroFileInfo) {
		agroFileInfoDao.save(agroFileInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		agroFileInfoDao.deleteById(id);
	}
	
	public List<AgroFileInfo> find(String ywzbId,String type) {
		DetachedCriteria dc = agroFileInfoDao.createDetachedCriteria();
		dc.add(Restrictions.ne(AgroFileInfo.FIELD_DEL_FLAG_XGXT, AgroFileInfo.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("ywzbId", ywzbId));
		if(StringUtils.isNotBlank(type)) {
			dc.add(Restrictions.eq("type", type));
		}
		return agroFileInfoDao.find(dc);
	}
	
}
