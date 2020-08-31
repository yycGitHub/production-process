package com.surekam.modules.agro.materials.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.materials.dao.MaterialFeedDao;
import com.surekam.modules.agro.materials.entity.MaterialFeed;

/**
 * 饲料表Service
 * 
 * @author tangjun
 * @version 2019-04-22
 */
@Component
@Transactional(readOnly = true)
public class MaterialFeedService extends BaseService {

	@Autowired
	private MaterialFeedDao agroMaterialFeedDao;

	public MaterialFeed get(String id) {
		return agroMaterialFeedDao.get(id);
	}

	public Page<MaterialFeed> find(Page<MaterialFeed> page, MaterialFeed agroMaterialFeed) {
		DetachedCriteria dc = agroMaterialFeedDao.createDetachedCriteria();
//		dc.add(Restrictions.eq(MaterialFeed.FIELD_DEL_FLAG, MaterialFeed.DEL_FLAG_NORMAL));
		return agroMaterialFeedDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(MaterialFeed agroMaterialFeed) {
		agroMaterialFeedDao.save(agroMaterialFeed);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		agroMaterialFeedDao.deleteById(id);
	}

}
