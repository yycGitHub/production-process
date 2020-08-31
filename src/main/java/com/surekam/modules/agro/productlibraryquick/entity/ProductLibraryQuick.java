package com.surekam.modules.agro.productlibraryquick.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.systementerprisestandards.entity.SystemEnterpriseStandards;
import com.surekam.modules.sys.entity.Office;

/**
 * 品种快捷表Entity
 * 
 * @author luoxw
 * @version 2019-07-03
 */
@Entity
@Table(name = "t_agro_product_library_quick")
@DynamicInsert
@DynamicUpdate
public class ProductLibraryQuick extends XGXTEntity<ProductLibraryQuick> {
	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private Office office;// 公司ID
	private String officeId;// 公司ID-非数据库字段
	private String officeName;// 公司name-非数据库字段
	private String classificationId;// 分类ID-非数据库字段
	private ProductLibraryTree productLibraryTree;// 品种主键
	private SystemEnterpriseStandards systemEnterpriseStandards;// 标准库主键
	private String standardsName;// 标准库名称-非数据库字段
	private String standardsId;// 标准库id-非数据库字段
	private String productId;// 品种ID-非数据库字段
	private String productName; // 品种名称
	private String qualityGuaranteePeriod; // 保质期(天)
	private String productPropagandaImgUrl; // 品种宣传图片
	private String productPropagandaDescription; // 品种宣传描述
	private List<ProductLibraryQuickValue> list;

	public ProductLibraryQuick() {
		super();
	}

	@PrePersist
	public void prePersist() {
		this.id = IdGen.uuid();
	}

	@Id
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(targetEntity = Office.class, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Transient
	public String getOfficeName() {
		if (StringUtils.isBlank(this.officeName) && office != null) {
			officeName = office.getName();
		}
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	@Transient
	public String getOfficeId() {
		if (StringUtils.isBlank(this.officeId) && office != null) {
			officeId = office.getId();
		}
		return this.officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	@Transient
	public String getClassificationId() {
		if (StringUtils.isBlank(this.classificationId) && productLibraryTree != null) {
			classificationId = productLibraryTree.getParent().getId();
		}
		return this.classificationId;
	}

	public void setClassificationId(String classificationId) {
		this.classificationId = classificationId;
	}

	@ManyToOne(targetEntity = ProductLibraryTree.class, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@JoinColumn(name = "product_id")
	public ProductLibraryTree getProductLibraryTree() {
		return productLibraryTree;
	}

	public void setProductLibraryTree(ProductLibraryTree productLibraryTree) {
		this.productLibraryTree = productLibraryTree;
	}

	@Transient
	public String getProductId() {
		if (StringUtils.isBlank(this.productId) && productLibraryTree != null) {
			productId = productLibraryTree.getId();
		}
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		if (StringUtils.isBlank(this.productName) && productLibraryTree != null) {
			productName = productLibraryTree.getProductCategoryName();
		}
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getQualityGuaranteePeriod() {
		return qualityGuaranteePeriod;
	}

	public void setQualityGuaranteePeriod(String qualityGuaranteePeriod) {
		this.qualityGuaranteePeriod = qualityGuaranteePeriod;
	}

	public String getProductPropagandaImgUrl() {
		return productPropagandaImgUrl;
	}

	public void setProductPropagandaImgUrl(String productPropagandaImgUrl) {
		this.productPropagandaImgUrl = productPropagandaImgUrl;
	}

	public String getProductPropagandaDescription() {
		return productPropagandaDescription;
	}

	public void setProductPropagandaDescription(String productPropagandaDescription) {
		this.productPropagandaDescription = productPropagandaDescription;
	}

	@ManyToOne(targetEntity = SystemEnterpriseStandards.class, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@JoinColumn(name = "system_standards_id")
	public SystemEnterpriseStandards getSystemEnterpriseStandards() {
		return systemEnterpriseStandards;
	}

	public void setSystemEnterpriseStandards(SystemEnterpriseStandards systemEnterpriseStandards) {
		this.systemEnterpriseStandards = systemEnterpriseStandards;
	}

	@Transient
	public String getStandardsName() {
		if (StringUtils.isBlank(this.standardsName) && systemEnterpriseStandards != null) {
			standardsName = systemEnterpriseStandards.getStandardName();
		}
		return standardsName;
	}

	public void setStandardsName(String standardsName) {
		this.standardsName = standardsName;
	}

	@Transient
	public String getStandardsId() {
		if (StringUtils.isBlank(this.standardsId) && systemEnterpriseStandards != null) {
			standardsId = systemEnterpriseStandards.getId();
		}
		return standardsId;
	}

	public void setStandardsId(String standardsId) {
		this.standardsId = standardsId;
	}

	@Transient
	public List<ProductLibraryQuickValue> getList() {
		return list;
	}

	public void setList(List<ProductLibraryQuickValue> list) {
		this.list = list;
	}

}
