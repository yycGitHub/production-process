package com.surekam.modules.sys.entity.vo;

import java.io.Serializable;

import com.surekam.modules.sys.entity.BusinessLicense;
import com.surekam.modules.sys.entity.ProductionCertificate;

/**
 * 接受企业资料栏目下三个模块内容提交
 * @author 腾农科技
 *
 */
public class Enterprise implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private OfficeVo officeVo;
	private BusinessLicense businessLicense;
	private ProductionCertificate productionCertificate;
	public OfficeVo getOfficeVo() {
		return officeVo;
	}
	public void setOfficeVo(OfficeVo officeVo) {
		this.officeVo = officeVo;
	}
	public BusinessLicense getBusinessLicense() {
		return businessLicense;
	}
	public void setBusinessLicense(BusinessLicense businessLicense) {
		this.businessLicense = businessLicense;
	}
	public ProductionCertificate getProductionCertificate() {
		return productionCertificate;
	}
	public void setProductionCertificate(ProductionCertificate productionCertificate) {
		this.productionCertificate = productionCertificate;
	}
}
