package com.surekam.modules.sys.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.dao.UserDao;

/**
 * 用户信息Service
 * @author xy
 * @version 2019-04-09
 */
@Component
@Transactional(readOnly = true)
public class UserService extends BaseService {

	@Autowired
	private UserDao userDao;
	
	public User get(String id) {
		return userDao.get(id);
	}
	
	public Page<User> find(Page<User> page, User user) {
		DetachedCriteria dc = userDao.createDetachedCriteria();
		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		return userDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(User user) {
		userDao.save(user);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		userDao.deleteById(id);
	}
	
	public List<User> getUserList(){
		StringBuffer sql = new StringBuffer(200);
		sql.append(" SELECT su.* FROM sys_user su LEFT JOIN sys_user_role ur on su.id = ur.USER_ID WHERE ur.ROLE_ID = '1fe2c8e247744ff184e8c162eff44f12' ");
		sql.append(" UNION  ALL SELECT su.* FROM sys_user su LEFT JOIN t_agro_experts e on su.id = e.user_id WHERE e.leader = '1' ");
		return userDao.findBySql2(sql.toString(), new Parameter(), User.class);
	}
	
	public List<User> getUserList2(String baseId){
		StringBuffer sql = new StringBuffer(200);
		sql.append(" SELECT * FROM sys_user su LEFT JOIN  t_agro_base_manager bm on su.id = bm.user_id ");
		sql.append(" WHERE role_id = '391192d4f9634982858634f12de44275' ");
		sql.append(" and t_base_id ='"+ baseId +"' ");
		return userDao.findBySql2(sql.toString(), new Parameter(), User.class);
	}
	
}
