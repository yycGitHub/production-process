package com.surekam.modules.agro.product.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.BeanUtils;
import com.google.common.collect.Lists;

/**
 * 品种库
 * 
 * @author luoxw
 * @version 2019-09-03
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductTreeList implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;// ID
	private String label;// 名称
	private String pid;// 父节点id
	private String isProductCategory;// 类别
	private Boolean disabled;// 是否可选

	private List<ProductTreeList> children = Lists.newArrayList();// 拥有下级列表

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<ProductTreeList> getChildren() {
		return children;
	}

	public void setChildren(List<ProductTreeList> children) {
		if (children != null && children.size() > 0) {
			for (ProductTreeList vo : children) {
				ProductTreeList agroProductLibraryTreeVo = new ProductTreeList();
				BeanUtils.copyProperties(vo, agroProductLibraryTreeVo);
				this.children.add(agroProductLibraryTreeVo);
			}
		} else {
			this.children = new ArrayList<ProductTreeList>();
		}
		this.children = children;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getIsProductCategory() {
		return isProductCategory;
	}

	public void setIsProductCategory(String isProductCategory) {
		this.isProductCategory = isProductCategory;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	
	
}
