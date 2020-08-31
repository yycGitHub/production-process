package com.surekam.modules.sys.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.sys.dao.BusinessLicenseDao;
import com.surekam.modules.sys.entity.BusinessLicense;

/**
 * 
 * @author wangyuewen
 *
 */
@Service
@Transactional(readOnly = true)
public class BusinessLicenseService extends BaseService {

	@Autowired
	private BusinessLicenseDao businessLicenseDao;
	
	public BusinessLicense get(String id) {
		return businessLicenseDao.get(id);
	}
	
	public List<BusinessLicense> findBusinessLicense(String officeId) {
		DetachedCriteria dc = businessLicenseDao.createDetachedCriteria();		
		if(StringUtils.isNotBlank(officeId)){
			dc.add(Restrictions.eq("officeId", officeId));
		}
		dc.add(Restrictions.eq(BusinessLicense.FIELD_DEL_FLAG, BusinessLicense.DEL_FLAG_NORMAL));
		return businessLicenseDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(BusinessLicense businessLicense) {
		businessLicenseDao.save(businessLicense);
	}
}
