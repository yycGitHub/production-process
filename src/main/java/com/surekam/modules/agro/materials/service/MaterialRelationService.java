package com.surekam.modules.agro.materials.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.materials.dao.MaterialRelationDao;
import com.surekam.modules.agro.materials.entity.MaterialRelation;

/**
 * 农资与农产品类型关系表Service
 * 
 * @author tangjun
 * @version 2019-04-22
 */
@Component
@Transactional(readOnly = true)
public class MaterialRelationService extends BaseService {

	@Autowired
	private MaterialRelationDao materialRelationDao;

	public MaterialRelation get(String id) {
		return materialRelationDao.get(id);
	}

	public Page<MaterialRelation> find(Page<MaterialRelation> page, MaterialRelation materialRelation) {
		DetachedCriteria dc = materialRelationDao.createDetachedCriteria();
//		dc.add(Restrictions.eq(MaterialRelation.FIELD_DEL_FLAG, MaterialRelation.DEL_FLAG_NORMAL));
		return materialRelationDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(MaterialRelation materialRelation) {
		materialRelationDao.save(materialRelation);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		materialRelationDao.deleteById(id);
	}

}
