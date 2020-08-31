package com.surekam.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.sys.entity.GroupUser;

/**
 * 项目组DAO接口
 * @author liuyi
 * @version 2018-03-23
 */
@Repository
public class GroupUserDao extends BaseDao<GroupUser> {
	
	
	
	public List<GroupUser> findListByGroupId(String id) {
		return find("from GroupUser where  groupId =:p1 order by userId", new Parameter(id));
		// findBySql("select * from sys_group_user where group_id = :p1 order by user_id",new Parameter(id),List.class);
	}
	
	public List<GroupUser> findMasterByGroupId(String id,String master) {
		return find("from GroupUser where  groupId =:p1 and master =:p2 order by userId", new Parameter(id,master));
		// findBySql("select * from sys_group_user where group_id = :p1 order by user_id",new Parameter(id),List.class);
	}
	
	/**
	 * 删除
	 * @param groupId
	 * @param userId
	 * @return
	 */
	public int deleteDataById(String groupId,String userId){
		//return update("delete GroupUser where groupId = :p1 and userId = :p2", new Parameter(groupId,userId));
		return updateBySql("delete from sys_group_user where group_id =:p1 and user_id=:p2",new Parameter(groupId,userId));
	}
	
	public int updateGroupMaster(String master,String groupId,String userId){
		//return update("delete GroupUser where groupId = :p1 and userId = :p2", new Parameter(groupId,userId));
		return updateBySql("update sys_group_user  set master =:p1 where group_id =:p2 and user_id=:p3",new Parameter(master,groupId,userId));
	}
	
	
	/**插入
	 * @param groupUser
	 * @return
	 */
//	public int insert(GroupUser groupUser){
//		return updateBySql(" insert into sys_group_user(group_id,user_id,master) value(:p1,:p2,:p3) ",new Parameter(groupUser.getGroupId(), groupUser.getUserId(),groupUser.getMaster()));
//	}
	
	
}
