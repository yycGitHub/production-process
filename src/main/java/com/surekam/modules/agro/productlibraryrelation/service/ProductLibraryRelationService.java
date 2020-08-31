package com.surekam.modules.agro.productlibraryrelation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.productlibraryrelation.dao.ProductLibraryRelationDao;
import com.surekam.modules.agro.productlibraryrelation.entity.ProductLibraryRelation;

/**
 * 种养殖种类树形关系表Service
 * 
 * @author tangjun
 * @version 2019-08-28
 */
@Component
@Transactional(readOnly = true)
public class ProductLibraryRelationService extends BaseService {

	@Autowired
	private ProductLibraryRelationDao productLibraryRelationDao;

	public ProductLibraryRelation get(String id) {
		return productLibraryRelationDao.get(id);
	}

	@Transactional(readOnly = false)
	public void save(ProductLibraryRelation productLibraryRelation) {
		productLibraryRelationDao.save(productLibraryRelation);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		productLibraryRelationDao.deleteById(id);
	}

}
