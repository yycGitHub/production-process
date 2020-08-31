/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.modules.sys.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.sys.entity.CommonUser;
import com.surekam.modules.sys.entity.CopyMenu;
import com.surekam.modules.sys.entity.CopyOfCommonUser;
import com.surekam.modules.sys.entity.LoginToken;
import com.surekam.modules.sys.entity.Menu;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.Role;
import com.surekam.modules.sys.entity.User;

/**
 * 用户DAO接口
 * 
 * @author sureserve
 * @version 2013-8-23
 */
@Repository
public class UserDao extends BaseDao<User> {

	@Autowired
	private LoginTokenDao loginTokenDao;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private OfficeDao officeDao;

	@Autowired
	private MenuDao menuDao;

	public List<User> findAllList() {
		return find("from User where delFlag=:p1 order by id", new Parameter(User.DEL_FLAG_NORMAL));
	}

	public User findByLoginName(String loginName) {
		return getByHql("from User where loginName = :p1 and delFlag = :p2",
				new Parameter(loginName, User.DEL_FLAG_NORMAL));
	}

	public User findByOpenId(String openId) {
		return getByHql("from User where openId = :p1 and delFlag = :p2", new Parameter(openId, User.DEL_FLAG_NORMAL));
	}

	public User getUserByLoginCode(String loginCode) {
		return getByHql("from User where loginCode = :p1 and delFlag = :p2",
				new Parameter(loginCode, User.DEL_FLAG_NORMAL));
	}

	public int updatePasswordById(String newPassword, String id) {
		return update("update User set password=:p1 where id = :p2", new Parameter(newPassword, id));
	}

	public int updateLoginInfo(String loginIp, Date loginDate, String id) {
		return update("update User set loginIp=:p1, loginDate=:p2 where id = :p3",
				new Parameter(loginIp, loginDate, id));
	}

	public User findByUnionId(String unionId) {
		return getByHql("from User where unionId = :p1 and delFlag = :p2",
				new Parameter(unionId, User.DEL_FLAG_NORMAL));
	}

	public List<User> getRoleList(String office, String roleId) {
		String sql = "select " + "* from sys_user a inner join sys_user_role b on a.ID = b.USER_ID and a.OFFICE_ID = '"
				+ office + "'" + "inner join sys_role c on b.ROLE_ID = c.ID " + "where b.ROLE_ID = '" + roleId
				+ "' and a.del_flag = '0'";
		return findBySql(sql, null, User.class);
	}

	public CommonUser findByToken(String token) {
		LoginToken loginToken = loginTokenDao.findByToken(token);
		if (StringUtils.isNotBlank(loginToken.getUserId())) {
			User user = get(loginToken.getUserId());
			CommonUser cuser = new CommonUser();
			if (user.getOffice() != null) {
				Office office = officeDao.get(user.getOffice().getId());
				if (office.getId().equals("1") || StringUtils.isBlank(office.getName())) {
					cuser.setOfficeName("农科院信息所农事标准化管理平台");
				} else {
					cuser.setOfficeName(office.getName());
				}
				cuser.setOfficeLog(office.getOfficeLogo());
				cuser.setOfficeId(office.getId());
			}
			cuser.setId(user.getId());
			cuser.setLoginName(user.getLoginName());
			cuser.setName(user.getName());
			cuser.setPhone(user.getPhone());
			List<String> roleNameList = new ArrayList<String>();
			List<String> roleIdList = userRoleDao.findByUserId(loginToken.getUserId());
			for (int i = 0; i < roleIdList.size(); i++) {
				Role role = roleDao.get(roleIdList.get(i).toString());
				roleNameList.add(role.getName());
			}
			cuser.setRoleIdList(roleIdList);
			cuser.setRoleNameList(roleNameList);
			List<Menu> menuList = new ArrayList<Menu>();
			List<Menu> ButtonList = new ArrayList<Menu>();
			// 濡傛灉鐧婚檰璐﹀彿涓鸿秴绾х郴缁熺鐞嗗憳璐﹀彿鍒欒繑鍥炴墍鏈夋寜閽垪琛紝鍚﹀垯灏辨牴鎹鑹茶繑鍥炲搴旂殑鎸夐挳鏉冮檺鍒楄〃
			if (user.isAdmin()) {
				menuList = menuDao.findAllAdminList();
				ButtonList = menuDao.findAllAdminButtonList();
			} else {
				menuList = menuDao.findAllList(roleIdList);
				ButtonList = menuDao.findAllButtonList(roleIdList);
			}
			Map<String, Boolean> map = new HashMap();
			for (Menu strList : ButtonList) {
				map.put(strList.getPermission(), true);
			}
			cuser.setButtonList(map);
			cuser.setMenuList(menuList);
			return cuser;
		} else {
			return new CommonUser();
		}
	}
	
	public List<User> findUserByIds(String[] ids, User user) {
		StringBuffer sql = new StringBuffer();
		sql.append("from User where delFlag=:p1 and id in (:p2) ");
		if (StringUtils.isNotBlank(user.getLoginName())) {
			sql.append(" and loginName like " + "'%" + user.getLoginName() + "%'");
		}
		if (StringUtils.isNotBlank(user.getName())) {
			sql.append(" and name like " + "'%" + user.getName() + "%'");
		}
		if (user.getOffice() != null && StringUtils.isNotBlank(user.getOffice().getId())) {
			sql.append(" and office.id = '" + user.getOffice().getId() + "'");
		}
		sql.append(" order by id");

		return find(sql.toString(), new Parameter(User.DEL_FLAG_NORMAL, ids));
	}

	public List<User> findUserListByCondition(User user) {
		StringBuffer sql = new StringBuffer();
		sql.append("from User where delFlag=:p1");

		if (StringUtils.isNotBlank(user.getLoginName())) {
			sql.append(" and loginName like " + "'%" + user.getLoginName() + "%'");
		}
		if (StringUtils.isNotBlank(user.getName())) {
			sql.append(" and name like " + "'%" + user.getName() + "%'");
		}
		if (user.getOffice() != null && StringUtils.isNotBlank(user.getOffice().getId())) {
			sql.append(" and office.id = '" + user.getOffice().getId() + "'");
		}
		sql.append(" order by id");
		Parameter param = new Parameter(User.DEL_FLAG_NORMAL);

		return find(sql.toString(), param);
	}

	public List<User> findByNameAndLoginName(String nameAndloginName) {
		String sql = "select * from sys_user a where (a.name ='" + nameAndloginName + "' or a.login_name ='"
				+ nameAndloginName + "'";
		List<User> findBySql = findBySql(sql, null, User.class);
		return findBySql;
	}

	public User findByLoginNameQuery(String loginName) {
		String sql = "select * from sys_user a  where a.login_name ='" + loginName + "'";
		List<User> findBySql = findBySql(sql, null, User.class);
		for (int i = 0; i < findBySql.size(); i++) {
			return findBySql.get(0);
		}
		return null;
	}
	
	public List<User> findUserList() {
		String sql = "SELECT t.* FROM sys_user t WHERE t.`DEL_FLAG` = '0' "
				+" AND t.office_id IN (SELECT b.id FROM sys_office b WHERE b.`PARENT_IDS` LIKE '%a2bfcffa042646a98360ef86343de977,%' OR b.id='a2bfcffa042646a98360ef86343de977')";
		List<User> list = findBySql(sql, null, User.class);
		return list;
	}

	public CopyOfCommonUser findByTokenCopy(String token) {
		LoginToken loginToken = loginTokenDao.findByToken(token);
		if (StringUtils.isNotBlank(loginToken.getUserId())) {
			User user = get(loginToken.getUserId());
			CopyOfCommonUser cuser = new CopyOfCommonUser();
			if (user.getOffice() != null) {
				Office office = officeDao.get(user.getOffice().getId());
				if (office.getId().equals("1") || StringUtils.isBlank(office.getName())) {
					cuser.setOfficeName("农科院信息所农事标准化管理平台");
				} else {
					cuser.setOfficeName(office.getName());
				}
				cuser.setOfficeLog(office.getOfficeLogo());
				cuser.setOfficeId(office.getId());
			}
			cuser.setId(user.getId());
			cuser.setLoginName(user.getLoginName());
			cuser.setName(user.getName());
			cuser.setPhone(user.getPhone());
			List<String> roleNameList = new ArrayList<String>();
			List<String> roleIdList = userRoleDao.findByUserId(loginToken.getUserId());
			for (int i = 0; i < roleIdList.size(); i++) {
				Role role = roleDao.get(roleIdList.get(i).toString());
				roleNameList.add(role.getName());
			}
			cuser.setRoleIdList(roleIdList);
			cuser.setRoleNameList(roleNameList);
			List<CopyMenu> menuList = new ArrayList<CopyMenu>();
			List<CopyMenu> ButtonList = new ArrayList<CopyMenu>();
			// 如果登陆账号为超级系统管理员账号则返回所有按钮列表，否则就根据角色返回对应的按钮权限列表
			if (user.isAdmin()) {
				menuList = menuDao.findAllAdminListCopy();
				ButtonList = menuDao.findAllAdminButtonListCopy();
			} else if (roleIdList.size()>0) {
				menuList = menuDao.findAllListCopy(roleIdList);
				ButtonList = menuDao.findAllButtonListCopy(roleIdList);
			}
			Map<String, Boolean> map = new HashMap();
			for (CopyMenu strList : ButtonList) {
				map.put(strList.getPermission(), true);
			}
			cuser.setButtonList(map);
			cuser.setMenuList(menuList);
			return cuser;
		} else {
			return new CopyOfCommonUser();
		}
	}
	
	
	public List<User> getUserList(String officeId, String roleId) {
		String sql = "select " + "* from sys_user a inner join sys_user_role b on a.ID = b.USER_ID and "
				+ "a.OFFICE_ID in (SELECT id FROM sys_office WHERE PARENT_IDS LIKE '%," + officeId + ",%' or id ='" + officeId + "') "
						+ "inner join sys_role c on b.ROLE_ID = c.ID " + "where b.ROLE_ID = '" + roleId + "' and a.del_flag = '0'";
		return findBySql(sql, null, User.class);
	}
}
