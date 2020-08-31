package com.surekam.modules.sys.entity.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.surekam.modules.sys.entity.Menu;
import com.surekam.modules.sys.entity.Office;


public class RoleVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String id;
	
	private OfficeVo office = new OfficeVo();	// 归属机构
	private String name; 	// 角色名称
	private String dataScope; // 数据范围
	private String delFlag; // 删除标记（0：正常；1：删除；）
	private String bookName;//通讯录字段

	private List<MenuVo> menuList = Lists.newArrayList(); // 拥有菜单列表
	private List<OfficeVo> officeList = Lists.newArrayList(); // 按明细设置数据范围
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataScope() {
		return dataScope;
	}
	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public OfficeVo getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		BeanUtils.copyProperties(office, this.office);
	}
	public List<MenuVo> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<Menu> menuList) {
		if(menuList != null && menuList.size()>0){
			for(Menu menu:menuList){
				MenuVo menuVo = new MenuVo();
				BeanUtils.copyProperties(menu, menuVo,new String[]{"childList"});
				if(menu.getChildList()!=null && menu.getChildList().size()>0){
					menuVo.setIsLeaf(false);
				}else{
					menuVo.setIsLeaf(true);
				}
				this.menuList.add(menuVo);
			}
		}else{
			this.menuList = new ArrayList<MenuVo>();
		}
	}
	public List<OfficeVo> getOfficeList() {
		return officeList;
	}
	public void setOfficeList(List<Office> officeList) {
		if(officeList != null && officeList.size()>0){
			for(Office office:officeList){
				OfficeVo officeVo = new OfficeVo();
				BeanUtils.copyProperties(office, officeVo,new String[]{"childList"});
				if(office.getChildList()!=null && office.getChildList().size()>0){
					officeVo.setIsLeaf(false);
				}else{
					officeVo.setIsLeaf(true);
				}
				this.officeList.add(officeVo);
			}
		}else{
			this.officeList = new ArrayList<OfficeVo>();
		}
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	
	
}
