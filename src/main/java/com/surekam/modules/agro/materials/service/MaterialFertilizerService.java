package com.surekam.modules.agro.materials.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.materials.dao.MaterialFertilizerDao;
import com.surekam.modules.agro.materials.entity.MaterialFertilizer;

/**
 * 化肥表Service
 * 
 * @author tangjun
 * @version 2019-04-22
 */
@Component
@Transactional(readOnly = true)
public class MaterialFertilizerService extends BaseService {

	@Autowired
	private MaterialFertilizerDao agroMaterialFertilizerDao;

	public MaterialFertilizer get(String id) {
		return agroMaterialFertilizerDao.get(id);
	}

	public Page<MaterialFertilizer> find(Page<MaterialFertilizer> page,
			MaterialFertilizer agroMaterialFertilizer) {
		DetachedCriteria dc = agroMaterialFertilizerDao.createDetachedCriteria();
//		dc.add(Restrictions.eq(MaterialFertilizer.FIELD_DEL_FLAG, MaterialFertilizer.DEL_FLAG_NORMAL));
		return agroMaterialFertilizerDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(MaterialFertilizer agroMaterialFertilizer) {
		agroMaterialFertilizerDao.save(agroMaterialFertilizer);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		agroMaterialFertilizerDao.deleteById(id);
	}

}
