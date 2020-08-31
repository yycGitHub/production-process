package com.surekam.modules.sys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.security.Digests;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.Encodes;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.sys.dao.AreaDao;
import com.surekam.modules.sys.dao.LoginTokenDao;
import com.surekam.modules.sys.dao.MenuDao;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.dao.RoleDao;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.dao.UserRoleDao;
import com.surekam.modules.sys.entity.Area;
import com.surekam.modules.sys.entity.CommonUser;
import com.surekam.modules.sys.entity.LoginToken;
import com.surekam.modules.sys.entity.Menu;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.Role;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.OfficeVo;
import com.surekam.modules.sys.security.SystemAuthorizingRealm;
import com.surekam.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 */
@Service
@Transactional(readOnly = true)
public class ApiSystemService extends BaseService {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private LoginTokenDao loginTokenDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private SystemAuthorizingRealm systemRealm;

	// ############################角色################################################//
	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public List<Role> findAllRole(User user) {
		DetachedCriteria dc = roleDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.createAlias("userList", "userList", JoinType.LEFT_OUTER_JOIN);
		dc.add(dataScopeFilter(user, "office", "userList"));
		dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Role.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("office.code")).addOrder(Order.asc("name"));
		List<Role> list = roleDao.find(dc);
		return list;
	}

	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		roleDao.clear();
		roleDao.save(role);
	}

	@Transactional(readOnly = false)
	public void deleteRole(String id) {
		roleDao.deleteById(id);
	}

	public Page<Role> findRole(Page<Role> page, Role role, User user) {
		DetachedCriteria dc = roleDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(role.getName())) {
			dc.add(Restrictions.like("name", role.getName(), MatchMode.ANYWHERE));
		}
		dc.createAlias("office", "office");
		// 注释数据权限过滤，用不上
		// if(!user.isAdmin()){
		// dc.createAlias("userList", "userList", JoinType.LEFT_OUTER_JOIN);
		// dc.add(dataScopeFilter(user, "office", "userList"));
		// }
		if (StringUtils.isNotBlank(role.getDelFlag())) {
			dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, role.getDelFlag()));
		} else {
			dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Menu.DEL_FLAG_NORMAL));
		}
		dc.addOrder(Order.asc("office.code")).addOrder(Order.asc("name"));
		return roleDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void roleChangeState(String id, String delFlag) {
		roleDao.updateDelFlag(id, delFlag);
	}

	// ############################菜单################################################//
	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu() {
		return UserUtils.getMenuList();
	}

	public List<Menu> findAllMenu(User user, String parentId, boolean onlyNext) {
		DetachedCriteria dc = menuDao.createDetachedCriteria();
		if (onlyNext) {
			if (StringUtils.isNotBlank(parentId)) {
				dc.createAlias("parent", "menu");
				dc.add(Restrictions.eq("parent.id", parentId));
			} else {
				dc.add(Restrictions.eq("id", "1"));
			}
		} else {
			if (StringUtils.isNotBlank(parentId)) {
				Menu menun = menuDao.get(parentId);
				dc.createAlias("parent", "menu");
				dc.add(Restrictions.like("parentIds", menun.getParentIds() + parentId + ",", MatchMode.START));
			}
		}
		dc.add(dataScopeFilter(user, dc.getAlias(), ""));
		dc.add(Restrictions.eq(Menu.FIELD_DEL_FLAG, Menu.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("sort"));
		menuDao.clear();
		return menuDao.find(dc);
	}

	public List<Menu> findAllMenu(String userid) {
		return menuDao.findByUserId(userid);
	}

	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		menu.setParent(this.getMenu(menu.getParent().getId()));
		String oldParentIds = menu.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");
		menuDao.clear();
		menuDao.save(menu);
		// 更新子节点 parentIds
		List<Menu> list = menuDao.findByParentIdsLike("%," + menu.getId() + ",%");
		for (Menu e : list) {
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
		}
		menuDao.save(list);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(String id) {
		menuDao.deleteById(id, "%," + id + ",%");
	}

	public Menu getMenuByAppId(String id) {
		return menuDao.getByHql("from Menu where appId=:p1", new Parameter(id));
	}

	public List<Menu> findZtreeMenus(User user, String itemId) {
		DetachedCriteria dc = menuDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(itemId)) {
			dc.createAlias("parent", "menu");
			dc.add(Restrictions.eq("parent.id", itemId));
		} else {
			dc.add(Restrictions.eq("id", "1"));
		}
		dc.add(dataScopeFilter(user, dc.getAlias(), ""));
		dc.add(Restrictions.eq(Menu.FIELD_DEL_FLAG, Menu.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("sort"));
		menuDao.clear();
		return menuDao.find(dc);
	}

	public Page<Menu> findPageMenus(Page<Menu> page, User currentUser, String name, String parentId, String delFlag,
			boolean b) {
		DetachedCriteria dc = menuDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(name)) {
			dc.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(parentId)) {
			dc.createAlias("parent", "menu");
			dc.add(Restrictions.like("parent.id", parentId, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(delFlag)) {
			dc.add(Restrictions.eq(Menu.FIELD_DEL_FLAG, delFlag));
		} else {
			dc.add(Restrictions.eq(Menu.FIELD_DEL_FLAG, Menu.DEL_FLAG_NORMAL));
		}
		dc.add(dataScopeFilter(currentUser, "menu", ""));
		dc.addOrder(Order.desc("createDate"));
		Page<Menu> menus = menuDao.find(page, dc);
		return menus;
	}

	@Transactional(readOnly = false)
	public void menuChangeState(String id, String delFlag) {
		menuDao.updateDelFlag(id, delFlag);
	}

	// ############################用户################################################//
	/**
	 * 通过token获取用户信息
	 * 
	 * @param token
	 * @return
	 */
	public User findUserByToken(String token) {
		LoginToken loginToken = loginTokenDao.findByToken(token);
		if (StringUtils.isNotBlank(loginToken.getUserId())) {
			User user = userDao.get(loginToken.getUserId());
			return user;
		} else {
			return new User();
		}
	}

	/**
	 * 通过token获取用户信息
	 * 
	 * @param token
	 * @return
	 */
	public CommonUser findCommonUserByToken(String token) {
		LoginToken loginToken = loginTokenDao.findByToken(token);
		if (StringUtils.isNotBlank(loginToken.getUserId())) {
			User user = userDao.get(loginToken.getUserId());
			CommonUser cuser = new CommonUser();
			cuser.setId(user.getId());
			cuser.setLoginName(user.getLoginName());
			cuser.setName(user.getName());
			List<String> roleNameList = new ArrayList<String>();
			List<String> roleIdList = userRoleDao.findByUserId(loginToken.getUserId());
			for (int i = 0; i < roleIdList.size(); i++) {
				Role role = roleDao.get(roleIdList.get(i).toString());
				roleNameList.add(role.getName());
			}
			cuser.setRoleIdList(roleIdList);
			cuser.setRoleNameList(roleNameList);
			List<Menu> menuList = menuDao.findAllList(roleIdList);
			cuser.setMenuList(menuList);
			return cuser;
		} else {
			return new CommonUser();
		}
	}

	public User getUser(String id) {
		return userDao.get(id);
	}

	public Page<User> findUser(Page<User> page, User user, User currentUser) {
		DetachedCriteria dc = userDao.createDetachedCriteria();
		if (user.getCompany() != null && StringUtils.isNotBlank(user.getCompany().getId())) {
			dc.createAlias("company", "company");
			dc.add(Restrictions.or(Restrictions.eq("company.id", user.getCompany().getId()),
					Restrictions.like("company.parentIds", "%," + user.getCompany().getId() + ",%")));
		}
		// 删除了公司的用户，不在这里体现。
		dc.add(Restrictions.eq("company.delFlag", "0"));
		// 如果不是超级管理员，则不显示超级管理员用户
		if (!currentUser.isAdmin()) {
			dc.add(Restrictions.ne("id", "1"));
		}
		dc.add(dataScopeFilter(currentUser, "office", ""));
		if (StringUtils.isNotEmpty(user.getLoginName())) {
			dc.add(Restrictions.like("loginName", "%" + user.getLoginName() + "%"));
		}
		if (StringUtils.isNotEmpty(user.getName())) {
			dc.add(Restrictions.like("name", "%" + user.getName() + "%"));
		}
		if (StringUtils.isNotEmpty(user.getDelFlag())) {
			dc.add(Restrictions.eq("delFlag", user.getDelFlag()));
		}
		Page<User> users = userDao.find(page, dc);
		return users;
	}

	// 取用户的数据范围
	public String getDataScope(User user) {
		return dataScopeFilterString(user, "office", "");
	}

	@Transactional(readOnly = false)
	public void saveUser(User user) {
		userDao.save(user);
	}

	@Transactional(readOnly = false)
	public void userChangeState(String id, String delFlag) {
		userDao.updateDelFlag(id, delFlag);
	}

	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		userDao.updatePasswordById(entryptPassword(newPassword), id);
		systemRealm.clearCachedAuthorizationInfo(loginName);
	}

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
	}

	/**
	 * 验证密码
	 * 
	 * @param plainPassword
	 *            明文密码
	 * @param password
	 *            密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0, 16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
	}

	// #############################机构###############################################//

	public Office getOfficeById(String id) {
		return officeDao.get(id);
	}

	public List<Map> getOfficeByOfficeId(String officeId) {
		return officeDao.findBySql("select a.* from sys_office a where a.id=:p1 and a.del_flag='0'",
				new Parameter(officeId), Map.class);
	}

	public List<Office> findOffices(User currentUser, String officeId, boolean onlyNext) {
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(officeId)) {
			dc.createAlias("parent", "office");
			if (onlyNext) {
				dc.add(Restrictions.eq("parent.id", officeId));
			} else {
				Office office = officeDao.get(officeId);
				dc.add(Restrictions.like("parentIds", office.getParentIds() + officeId + ",", MatchMode.START));
			}
		}
		if (!currentUser.isAdmin()) {
			dc.add(dataScopeFilter(currentUser, dc.getAlias(), ""));
		}
		dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
		return officeDao.find(dc);
	}

	public Page<Office> findPageOffices(Page<Office> page, User currentUser, String name, String parentId,
			String delFlag, boolean onlyNext) {
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(name)) {
			dc.add(Restrictions.like("name", name, MatchMode.START));
		}
		if (StringUtils.isNotBlank(parentId)) {
			dc.createAlias("parent", "office");
			if (onlyNext) {
				dc.add(Restrictions.eq("parent.id", parentId));
			} else {
				Office office = officeDao.get(parentId);
				dc.add(Restrictions.like("parentIds", office.getParentIds() + parentId + ",", MatchMode.START));
			}
		}
		if (!currentUser.isAdmin()) {
			dc.add(dataScopeFilter(currentUser, dc.getAlias(), ""));
		}
		if (StringUtils.isNotBlank(delFlag)) {
			dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, delFlag));
		} else {
			dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
		}
		dc.addOrder(Order.asc("code"));
		return officeDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void saveOffice(Office office) {
		officeDao.save(office);
	}

	@Transactional(readOnly = false)
	public void saveOfficeVo(OfficeVo officeVo) {
		Office office = officeDao.get(officeVo.getId());
		office.setAddress(officeVo.getAddress());
		office.setEmail(officeVo.getEmail());
		office.setOfficeLogo(officeVo.getOfficeLogo());
		office.setOperatorName(officeVo.getOperatorName());
		office.setOperatorIdnumber(officeVo.getOperatorIdnumber());
		office.setName(officeVo.getName());
		office.setZipCode(officeVo.getZipCode());
		officeDao.save(office);
	}

	@Transactional(readOnly = false)
	public void officeChangeState(String id, String delFlag) {
		officeDao.updateDelFlag(id, delFlag);
		userDao.updateDelFlagForeignKey(id, delFlag);
	}

	// ############################################区域################################################//

	public Area getArea(String id) {
		return areaDao.get(id);
	}

	public List<Area> findAreas(User currentUser, String areaId, boolean onlyNext) {
		DetachedCriteria dc = areaDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(areaId)) {
			dc.createAlias("parent", "area");
			if (onlyNext) {
				dc.add(Restrictions.eq("parent.id", areaId));
			} else {
				Area area = areaDao.get(areaId);
				dc.add(Restrictions.like("parentIds", area.getParentIds() + areaId + ",", MatchMode.START));
			}
		}
		if (!currentUser.isAdmin()) {
			dc.add(dataScopeFilter(currentUser, dc.getAlias(), ""));
		}
		dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
		return areaDao.find(dc);
	}

	public Page<Area> findPageAreas(Page<Area> page, User currentUser, String name, String parentId, String delFlag,
			boolean onlyNext) {
		DetachedCriteria dc = areaDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(name)) {
			dc.add(Restrictions.like("name", name, MatchMode.START));
		}
		if (StringUtils.isNotBlank(parentId)) {
			dc.createAlias("parent", "area");
			if (onlyNext) {
				dc.add(Restrictions.eq("parent.id", parentId));
			} else {
				Area area = areaDao.get(parentId);
				dc.add(Restrictions.like("parentIds", area.getParentIds() + parentId + ",", MatchMode.START));
			}
		}
		if (!currentUser.isAdmin()) {
			dc.add(dataScopeFilter(currentUser, dc.getAlias(), ""));
		}
		if (StringUtils.isNotBlank(delFlag)) {
			dc.add(Restrictions.eq(Area.FIELD_DEL_FLAG, delFlag));
		} else {
			dc.add(Restrictions.eq(Area.FIELD_DEL_FLAG, Area.DEL_FLAG_NORMAL));
		}
		dc.addOrder(Order.asc("code"));
		return areaDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void saveArea(Area area) {
		areaDao.save(area);
	}

	@Transactional(readOnly = false)
	public void areaChangeState(String id, String delFlag) {
		areaDao.updateDelFlag(id, delFlag);
	}

	public boolean existSameCompanyName(OfficeVo officeVo) {
		List<Office> list = new ArrayList<Office>();
		// 如果id为空，则代表是新增，where条件不带id
		if (StringUtils.isBlank(officeVo.getId())) {
			list = officeDao.findBySql("select a.* from sys_office a where a.name=:p1 and a.del_flag='0'",
					new Parameter(officeVo.getName()));
		} else {
			list = officeDao.findBySql("select a.* from sys_office a where a.id<>:p1 and a.name=:p2 and a.del_flag='0'",
					new Parameter(officeVo.getId(), officeVo.getName()));
		}
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean existLoop(OfficeVo officeVo) {
		List<Office> list = new ArrayList<Office>();
		String id = "," + officeVo.getId() + ",";
		list = officeDao.findBySql("select a.* from sys_office a where a.id='" + officeVo.getParentId()
				+ "' and a.PARENT_IDS like '%" + id + "%' and a.del_flag='0'");
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean existUserName(User user) {
		List<User> list = new ArrayList<User>();
		list = officeDao.findBySql("select a.* from sys_user a where a.LOGIN_NAME=:p1 and a.del_flag='0'",
				new Parameter(user.getLoginName()));
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<Menu> getTwoLevelMenuList(String userId, String parentId) {
		return menuDao.findBySql(getTwoLevelMenuSQL(userId, parentId), new Parameter(userId, parentId), Menu.class);
	}

	public String getTwoLevelMenuSQL(String userId, String parentId) {
		StringBuffer sql = new StringBuffer(300);
		sql.append(
				"SELECT m.* FROM sys_user u INNER JOIN sys_user_role ur on u.id = ur.USER_ID INNER JOIN sys_role r on ur.ROLE_ID = r.id INNER JOIN sys_role_menu rm on r.id =rm.role_id INNER JOIN sys_menu m on rm.MENU_ID = m.ID WHERE u.id =:p1 and m.DEL_FLAG = 0 and m.PARENT_ID =:p2");
		return sql.toString();
	}

	public List<Menu> getFirstClassMenuList(String userId) {
		return menuDao.findBySql(getFirstClassMenuSQL(userId), new Parameter(userId), Menu.class);
	}

	public String getFirstClassMenuSQL(String userId) {
		StringBuffer sql = new StringBuffer(300);
		sql.append(
				"SELECT * FROM sys_menu WHERE id in (SELECT m.PARENT_ID FROM sys_user u INNER JOIN sys_user_role ur on u.id = ur.USER_ID\n"
						+ "INNER JOIN sys_role_menu rm on ur.ROLE_ID =rm.role_id\n"
						+ "INNER JOIN sys_menu m on rm.MENU_ID = m.id\n" + "WHERE u.id =:p1)");
		return sql.toString();
	}

	public List<Role> getRoleByUserId(String userId) {
		StringBuffer sql = new StringBuffer(300);
		sql.append(
				" SELECT sr.* FROM sys_role sr LEFT JOIN sys_user_role ur on sr.id = ur.role_id WHERE  ur.USER_ID = '"
						+ userId + "' ");
		return roleDao.findBySql2(sql.toString(), null, Role.class);
	}

	/**  
	 * Title: getCurrentCompanyAndSubordinateCompany
	 * Description: 
	 * @param user
	 * @return  
	 */  
	public List<Office> getCurrentCompanyAndSubordinateCompany(User user) {
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(user.getOffice().getId())) {
			dc.createAlias("parent", "office");
			Office office = officeDao.get(user.getOffice().getId());
			dc.add(Restrictions.or(Restrictions.eq("id", user.getOffice().getId()), Restrictions.like("parentIds", office.getParentIds() + user.getOffice().getId() + ",", MatchMode.START)));
		}
		dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return officeDao.find(dc);
	}

}
