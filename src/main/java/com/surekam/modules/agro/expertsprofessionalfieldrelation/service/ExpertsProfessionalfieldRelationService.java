package com.surekam.modules.agro.expertsprofessionalfieldrelation.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.expertsprofessionalfieldrelation.dao.ExpertsProfessionalfieldRelationDao;
import com.surekam.modules.agro.expertsprofessionalfieldrelation.entity.ExpertsProfessionalfieldRelation;

/**
 * 专家与专业领域关系表Service
 * @author tangjun
 * @version 2019-04-19
 */
@Component
@Transactional(readOnly = true)
public class ExpertsProfessionalfieldRelationService extends BaseService {

	@Autowired
	private ExpertsProfessionalfieldRelationDao agroExpertsProfessionalfieldRelationDao;
	
	public ExpertsProfessionalfieldRelation get(String id) {
		return agroExpertsProfessionalfieldRelationDao.get(id);
	}
	
	public Page<ExpertsProfessionalfieldRelation> find(Page<ExpertsProfessionalfieldRelation> page, ExpertsProfessionalfieldRelation agroExpertsProfessionalfieldRelation) {
		DetachedCriteria dc = agroExpertsProfessionalfieldRelationDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ExpertsProfessionalfieldRelation.FIELD_DEL_FLAG, ExpertsProfessionalfieldRelation.DEL_FLAG_NORMAL));
		return agroExpertsProfessionalfieldRelationDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ExpertsProfessionalfieldRelation agroExpertsProfessionalfieldRelation) {
		agroExpertsProfessionalfieldRelationDao.save(agroExpertsProfessionalfieldRelation);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		agroExpertsProfessionalfieldRelationDao.deleteById(id);
	}
	
}
