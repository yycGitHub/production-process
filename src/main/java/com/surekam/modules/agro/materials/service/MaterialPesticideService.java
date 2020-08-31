package com.surekam.modules.agro.materials.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.materials.dao.MaterialPesticideDao;
import com.surekam.modules.agro.materials.entity.MaterialPesticide;

/**
 * 农药表Service
 * 
 * @author tangjun
 * @version 2019-04-22
 */
@Component
@Transactional(readOnly = true)
public class MaterialPesticideService extends BaseService {

	@Autowired
	private MaterialPesticideDao agroMaterialPesticideDao;

	public MaterialPesticide get(String id) {
		return agroMaterialPesticideDao.get(id);
	}

	public Page<MaterialPesticide> find(Page<MaterialPesticide> page,
			MaterialPesticide agroMaterialPesticide) {
		DetachedCriteria dc = agroMaterialPesticideDao.createDetachedCriteria();
//		dc.add(Restrictions.eq(MaterialPesticide.FIELD_DEL_FLAG, MaterialPesticide.DEL_FLAG_NORMAL));
		return agroMaterialPesticideDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(MaterialPesticide agroMaterialPesticide) {
		agroMaterialPesticideDao.save(agroMaterialPesticide);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		agroMaterialPesticideDao.deleteById(id);
	}

}
