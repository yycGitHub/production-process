/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.sys.entity.CopyMenu;
import com.surekam.modules.sys.entity.Dict;
import com.surekam.modules.sys.entity.Menu;

/**
 * 菜单DAO接口
 * 
 * @author sureserve
 * @version 2013-8-23
 */
@Repository
public class MenuDao extends BaseDao<Menu> {

	public List<Menu> findAllActivitiList() {
		return find(
				"from Menu where delFlag=:p1 and isActiviti = :p2 order by sort",
				new Parameter(Dict.DEL_FLAG_NORMAL, Menu.YES));
	}

	public List<Menu> findByParentIdsLike(String parentIds) {
		return find("from Menu where parentIds like :p1", new Parameter(
				parentIds));
	}

	public List<Menu> findAllList() {
		return find("from Menu where delFlag=:p1 order by sort", new Parameter(
				Dict.DEL_FLAG_NORMAL));
	}

	public List<Menu> findAllList(List<String> roleList) {
		return findBySql(
				"select distinct m.* from sys_menu m,sys_role_menu r where m.id=r.menu_id and m.del_flag=:p1 and r.role_id in(:p2) and m.is_show='1' order by m.sort",
				new Parameter(Dict.DEL_FLAG_NORMAL, roleList.toArray()),
				Menu.class);
	}

	public List<CopyMenu> findAllListCopy(List<String> roleList) {
		return findBySql(
				"select distinct m.* from sys_menu m,sys_role_menu r where m.id=r.menu_id and m.del_flag=:p1 and r.role_id in(:p2) and m.is_show='1' order by m.sort",
				new Parameter(Dict.DEL_FLAG_NORMAL, roleList.toArray()),
				CopyMenu.class);
	}


	public List<Menu> findAllAdminList() {
		return findBySql(
				"select distinct m.* from sys_menu m where m.del_flag=:p1 and m.is_show='1' order by m.sort",
				new Parameter(Dict.DEL_FLAG_NORMAL),
				Menu.class);
	}

	public List<Menu> findAllButtonList(List<String> roleList) {
		return findBySql(
				"select distinct m.* from sys_menu m,sys_role_menu r where m.id=r.menu_id and m.del_flag=:p1 and r.role_id in(:p2) and m.is_show='0' order by m.sort",
				new Parameter(Dict.DEL_FLAG_NORMAL, roleList.toArray()),
				Menu.class);
	}

	public List<Menu> findAllAdminButtonList() {
		return findBySql(
				"select distinct m.* from sys_menu m where  m.del_flag=:p1 and m.is_show='0' order by m.sort",
				new Parameter(Dict.DEL_FLAG_NORMAL), Menu.class);
	}

	public List<CopyMenu> findAllAdminButtonListCopy() {
		return findBySql(
				"select distinct m.* from sys_menu m where  m.del_flag=:p1 and m.is_show='0' order by m.sort",
				new Parameter(Dict.DEL_FLAG_NORMAL), CopyMenu.class);
	}
	public List<Menu> findByUserId(String userId) {
		List<Menu> menus = find(
				"select distinct m from Menu m, Role r, User u where m in elements (r.menuList) and r in elements (u.roleList)"
						+ " and m.delFlag=:p1 and r.delFlag=:p1 and u.delFlag=:p1 and u.id=:p2"
						+ // or (m.user.id=:p2 and m.delFlag=:p1)" +
						" order by m.sort", new Parameter(Menu.DEL_FLAG_NORMAL,
						userId));
		return menus;
	}

	public List<Menu> findAllActivitiList(String userId) {
		return find(
				"select distinct m from Menu m, Role r, User u where m in elements (r.menuList) and r in elements (u.roleList)"
						+ " and m.delFlag=:p1 and r.delFlag=:p1 and u.delFlag=:p1 and m.isActiviti=:p2 and u.id=:p3 order by m.sort",
				new Parameter(Menu.DEL_FLAG_NORMAL, Menu.YES, userId));
	}

	public Menu getByKuid(String kuid) {
		return getByHql("from Menu where kuid =:p1", new Parameter(kuid));
	}

	public List<CopyMenu> findAllAdminListCopy() {
		return findBySql(
				"select distinct m.* from sys_menu m where m.del_flag=:p1 and m.is_show='1' order by m.sort",
				new Parameter(Dict.DEL_FLAG_NORMAL),
				CopyMenu.class);
	}

	public List<CopyMenu> findAllButtonListCopy(List<String> roleList) {
		return findBySql(
				"select distinct m.* from sys_menu m,sys_role_menu r where m.id=r.menu_id and m.del_flag=:p1 and r.role_id in(:p2) and m.is_show='0' order by m.sort",
				new Parameter(Dict.DEL_FLAG_NORMAL, roleList.toArray()),
				CopyMenu.class);
	}
}
