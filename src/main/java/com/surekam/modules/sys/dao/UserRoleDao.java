package com.surekam.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.UserRole;

/**
 * 用户权限DAO接口
 * @author liuyi
 * @version 2018-01-29
 */
@Repository
public class UserRoleDao extends BaseDao<UserRole> {
	
	
	public int deleteById(String userId,String roleId){
		//return update("delete UserRole where userId = :p1 and roleId = :p2", new Parameter(userId,roleId));
		return updateBySql(" delete from sys_user_role where user_id = :p1 and role_id = :p2 ",new Parameter(userId,roleId));
	}
	
	
	public List<String> findByUserId(String userid){
		 //find("from UserRole where userId = :p1", new Parameter(userid));
		return findBySql("select role_id from sys_user_role  where user_id = :p1 ",new Parameter(userid));
	}
	
	public int insert(UserRole userRole){
		return updateBySql(" insert into sys_user_role(user_id,role_id) value(:p1,:p2) ",new Parameter(userRole.getUserId(),userRole.getRoleId()));
	}
	
	
}
