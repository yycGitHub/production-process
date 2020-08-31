package com.surekam.modules.agro.baseexpertsrelation.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.baseexpertsrelation.entity.BaseExpertsRelation;
import com.surekam.modules.agro.baseexpertsrelation.dao.BaseExpertsRelationDao;
import com.surekam.modules.agro.experts.entity.Experts;

/**
 * 基地专家关联表Service
 * @author luoxw
 * @version 2019-10-29
 */
@Component
@Transactional(readOnly = true)
public class BaseExpertsRelationService extends BaseService {

	@Autowired
	private BaseExpertsRelationDao baseExpertsRelationDao;
	
	public BaseExpertsRelation get(String id) {
		return baseExpertsRelationDao.get(id);
	}
	
	public Page<BaseExpertsRelation> find(Page<BaseExpertsRelation> page, BaseExpertsRelation baseExpertsRelation) {
		DetachedCriteria dc = baseExpertsRelationDao.createDetachedCriteria();
		dc.add(Restrictions.eq(BaseExpertsRelation.FIELD_DEL_FLAG, BaseExpertsRelation.DEL_FLAG_NORMAL));
		return baseExpertsRelationDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(BaseExpertsRelation baseExpertsRelation) {
		baseExpertsRelationDao.save(baseExpertsRelation);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		baseExpertsRelationDao.deleteById(id);
	}
}
