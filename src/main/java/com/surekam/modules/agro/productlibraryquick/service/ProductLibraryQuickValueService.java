package com.surekam.modules.agro.productlibraryquick.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
import com.surekam.modules.agro.productlibraryquick.entity.ProductLibraryQuick;
import com.surekam.modules.agro.productlibraryquick.entity.ProductLibraryQuickValue;
import com.surekam.modules.agro.productlibraryquick.dao.ProductLibraryQuickValueDao;

/**
 * 品种快捷值表用于向溯源推送Service
 * @author luoxw
 * @version 2019-07-24
 */
@Component
@Transactional(readOnly = true)
public class ProductLibraryQuickValueService extends BaseService {

	@Autowired
	private ProductLibraryQuickValueDao productLibraryQuickValueDao;
	
	public ProductLibraryQuickValue get(String id) {
		return productLibraryQuickValueDao.get(id);
	}

	public Page<ProductLibraryQuickValue> find(Page<ProductLibraryQuickValue> page, ProductLibraryQuickValue productLibraryQuickValue) {
		DetachedCriteria dc = productLibraryQuickValueDao.createDetachedCriteria();
		dc.add(Restrictions.ne(ProductLibraryQuickValue.FIELD_DEL_FLAG_XGXT, ProductLibraryQuickValue.STATE_FLAG_DEL));
		return productLibraryQuickValueDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ProductLibraryQuickValue productLibraryQuickValue) {
		productLibraryQuickValueDao.save(productLibraryQuickValue);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		productLibraryQuickValueDao.deleteById(id);
	}
	
}
