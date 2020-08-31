package com.surekam.modules.sys.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.sys.dao.BusinessLicenseDao;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.dao.ProductionCertificateDao;
import com.surekam.modules.sys.entity.Area;
import com.surekam.modules.sys.entity.BusinessLicense;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.ProductionCertificate;
import com.surekam.modules.sys.entity.vo.OfficeVo;

/**
 * 
 * @author wangyuewen
 *
 */
@Service
@Transactional(readOnly = true)
public class ProductionCertificateService extends BaseService {

	@Autowired
	private ProductionCertificateDao productionCertificateDao;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private BusinessLicenseDao businessLicenseDao;
	
	public ProductionCertificate get(String id) {
		return productionCertificateDao.get(id);
	}
	
	public List<ProductionCertificate> findProductionCertificate(String officeId) {
		DetachedCriteria dc = productionCertificateDao.createDetachedCriteria();		
		if(StringUtils.isNotBlank(officeId)){
			dc.add(Restrictions.eq("officeId", officeId));
		}
		dc.add(Restrictions.eq(ProductionCertificate.FIELD_DEL_FLAG, ProductionCertificate.DEL_FLAG_NORMAL));
		return productionCertificateDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ProductionCertificate ProductionCertificate) {
		productionCertificateDao.save(ProductionCertificate);
	}
	
	
	@Transactional(readOnly = false)
	public void saveEnterprise(OfficeVo officeVo, ProductionCertificate productionCertificate,BusinessLicense businessLicense) {
		Office office = StringUtils.isNotBlank(officeVo.getId())?officeDao.get(officeVo.getId()):new Office();
		if(StringUtils.isBlank(officeVo.getId())){
			//新增时，默认区域为中国
			BeanUtils.copyProperties(officeVo,office,new String[]{"id","loginIp","loginDate","createDate","updateDate","delFlag"});
			office.setArea(new Area("2"));
			office.setType("1");
			office.setGrade("1");
			office.setCode(officeVo.getCode());
		}
		office.setAddress(officeVo.getAddress());
		office.setEmail(officeVo.getEmail());
		office.setOfficeLogo(officeVo.getOfficeLogo());
		office.setOperatorName(officeVo.getOperatorName());
		office.setOperatorIdnumber(officeVo.getOperatorIdnumber());
		office.setName(officeVo.getName());
		office.setOfficeHomePage(officeVo.getOfficeHomePage());
		office.setProductionFunction(officeVo.getProductionFunction());
		office.setZipCode(officeVo.getZipCode());
		if(!officeVo.getParentId().equals(office.getParent()==null?"":office.getParent().getId()) && StringUtils.isNotBlank(officeVo.getParentId())){
			Office officePrenat = officeDao.get(officeVo.getParentId());
			office.setParent(officePrenat);
			office.setParentIds(officePrenat.getParentIds()+officePrenat.getId()+",");
			// 修改时，对应下级一起变动
			if(StringUtils.isNotBlank(officeVo.getId())){
				StringBuffer id=new StringBuffer(","+officeVo.getId()+",");
				officeDao.updateBySql("update sys_office set "
						+ "PARENT_IDS=concat('"+office.getParentIds()+"',substring(PARENT_IDS,locate(',"+office.getId()+",',PARENT_IDS)+1)) "
						+ "where DEL_FLAG='0' and PARENT_IDS like '%"+id+"%'");
			}
		}
		officeDao.save(office);
		businessLicense.setOfficeId(office.getId());
		productionCertificate.setOfficeId(office.getId());	
		businessLicenseDao.save(businessLicense);
		productionCertificateDao.save(productionCertificate);
	}
}
