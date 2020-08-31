package com.surekam.modules.sys.entity.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.surekam.modules.sys.entity.Menu;

public class MenuVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	
	private String parentId;
	private String parentName;

	private String parentIds; // 所有父级编号
	private String name; 	// 名称
	private String href; 	// 链接
	private String target; 	// 目标（ mainFrame、_blank、_self、_parent、_top）
	private String icon; 	// 图标
	private Integer sort; 	// 排序
	private String isShow; 	// 是否在菜单中显示（1：显示；0：不显示）
	private String isActiviti; 	// 是否同步到工作流（1：同步；0：不同步）
	private String permission; // 权限标识
	
	private boolean isDisable = false;//是否不可选中
	private boolean isChecked = false;//是否选中
	private boolean isLeaf = false;
	private String type;//资源类型：0菜单，1权限资源
	
	private String delFlag;
	
	private List<MenuVo> childList = Lists.newArrayList();// 拥有子菜单列表

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getIsActiviti() {
		return isActiviti;
	}

	public void setIsActiviti(String isActiviti) {
		this.isActiviti = isActiviti;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public boolean isDisable() {
		return isDisable;
	}

	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public List<MenuVo> getChildList() {
		return childList;
	}

	public void setChildList(List<Menu> childList) {
		if(childList != null && childList.size()>0){
			for(Menu menu:childList){
				MenuVo menuVo = new MenuVo();
				BeanUtils.copyProperties(menu, menuVo);
				if(menu.getChildList()!=null && menu.getChildList().size()>0){
					menuVo.setIsLeaf(false);
				}else{
					menuVo.setIsLeaf(true);
				}
				this.childList.add(menuVo);
			}
		}else{
			this.childList = new ArrayList<MenuVo>();
		}
	}

}