/**
 * 
 */
package com.surekam.modules.sys.entity.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.surekam.modules.sys.entity.Office;

/**
 * Title: OfficeTreeVo Description:
 * 
 * @author tangjun
 * @date 2019年8月27日
 */
public class OfficeTreeVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id; // 主键
	private String label; // 公司名称
	private List<OfficeTreeVo> children = Lists.newArrayList();// 拥有子机构列表

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

	public List<OfficeTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<Office> children) {
		if (children != null && children.size() > 0) {
			for (Office office : children) {
				OfficeTreeVo officeTreeVo = new OfficeTreeVo();
				BeanUtils.copyProperties(office, officeTreeVo);
				this.children.add(officeTreeVo);
			}
		} else {
			this.children = new ArrayList<OfficeTreeVo>();
		}
	}

}
