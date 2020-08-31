package com.surekam.modules.agro.materials.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.materials.dao.MaterialAnimalHealthProductsDao;
import com.surekam.modules.agro.materials.entity.MaterialAnimalHealthProducts;

/**
 * 动保产品Service
 * 
 * @author tangjun
 * @version 2019-04-22
 */
@Component
@Transactional(readOnly = true)
public class MaterialAnimalHealthProductsService extends BaseService {

	@Autowired
	private MaterialAnimalHealthProductsDao agroMaterialAnimalHealthProductsDao;

	public MaterialAnimalHealthProducts get(String id) {
		return agroMaterialAnimalHealthProductsDao.get(id);
	}

	public Page<MaterialAnimalHealthProducts> find(Page<MaterialAnimalHealthProducts> page,
			MaterialAnimalHealthProducts agroMaterialAnimalHealthProducts) {
		DetachedCriteria dc = agroMaterialAnimalHealthProductsDao.createDetachedCriteria();
//		dc.add(Restrictions.eq(MaterialAnimalHealthProducts.FIELD_DEL_FLAG, MaterialAnimalHealthProducts.DEL_FLAG_NORMAL));
		return agroMaterialAnimalHealthProductsDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(MaterialAnimalHealthProducts agroMaterialAnimalHealthProducts) {
		agroMaterialAnimalHealthProductsDao.save(agroMaterialAnimalHealthProducts);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		agroMaterialAnimalHealthProductsDao.deleteById(id);
	}

}
