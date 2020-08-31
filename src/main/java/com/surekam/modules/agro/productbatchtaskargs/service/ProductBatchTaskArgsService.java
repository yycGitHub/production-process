package com.surekam.modules.agro.productbatchtaskargs.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.productbatchtaskargs.entity.ProductBatchTaskArgs;
import com.surekam.modules.agro.productbatchtaskargs.dao.ProductBatchTaskArgsDao;

/**
 * 批次标准作业详细参数表Service
 * @author liwei
 * @version 2019-04-25
 */
@Component
@Transactional(readOnly = true)
public class ProductBatchTaskArgsService extends BaseService {

	@Autowired
	private ProductBatchTaskArgsDao productBatchTaskArgsDao;
	
	public ProductBatchTaskArgs get(String id) {
		return productBatchTaskArgsDao.get(id);
	}
	
	public Page<ProductBatchTaskArgs> find(Page<ProductBatchTaskArgs> page, ProductBatchTaskArgs productBatchTaskArgs) {
		DetachedCriteria dc = productBatchTaskArgsDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ProductBatchTaskArgs.FIELD_DEL_FLAG, ProductBatchTaskArgs.DEL_FLAG_NORMAL));
		return productBatchTaskArgsDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ProductBatchTaskArgs productBatchTaskArgs) {
		productBatchTaskArgsDao.save(productBatchTaskArgs);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		productBatchTaskArgsDao.deleteById(id);
	}
	
}
