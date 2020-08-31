package com.surekam.modules.agro.product.entity.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.BeanUtils;
import com.google.common.collect.Lists;

/**
 * 种类 管理Entity
 * 
 * @author tangjun
 * @version 2019-04-15
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductLibraryTreeVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;// ID
	private String label;// 名称
	private String childrenCount;// 子节点数量

	private List<ProductLibraryTreeVo> children = Lists.newArrayList();// 拥有子机构列表

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

	public List<ProductLibraryTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<ProductLibraryTreeVo> children) {
		if (children != null && children.size() > 0) {
			for (ProductLibraryTreeVo vo : children) {
				ProductLibraryTreeVo agroProductLibraryTreeVo = new ProductLibraryTreeVo();
				BeanUtils.copyProperties(vo, agroProductLibraryTreeVo);
				this.children.add(agroProductLibraryTreeVo);
			}
		} else {
			this.children = new ArrayList<ProductLibraryTreeVo>();
		}
		this.children = children;
	}

	public String getChildrenCount() {
		return childrenCount;
	}

	public void setChildrenCount(String childrenCount) {
		this.childrenCount = childrenCount;
	}
	
}
