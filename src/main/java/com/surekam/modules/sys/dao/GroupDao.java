package com.surekam.modules.sys.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.BaseEntity;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.cms.entity.Category;
import com.surekam.modules.cms.entity.CategoryRoleUser;
import com.surekam.modules.sys.entity.Group;
import com.surekam.modules.sys.entity.GroupUser;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;

/**
 * 项目组DAO接口
 * @author liuyi
 * @version 2018-03-23
 */
@Repository
public class GroupDao extends BaseDao<Group> {
	
	public List<Group> findAllList() {
		return find("from Group where delFlag=:p1 order by groupId", new Parameter(User.DEL_FLAG_NORMAL));
	}
	
	
	public List<Group> findByParentId(String parentId){
		return find("from Group where delFlag=:p1 and parent.groupId=:p2 ", 
				new Parameter(Group.DEL_FLAG_NORMAL, parentId));
	}

	public List<Group> findByParentIdsLike(String parentIds){
		return find("from Group where parentIds like :p1", new Parameter(parentIds));
	}

	/**
	 * 逻辑删除
	 * @param id
	 * @return
	 */
	public int deleteByGroupId(String id){
		return update("update Group set delFlag='" + Group.DEL_FLAG_DELETE + "' where groupId = :p1", 
				new Parameter(id));
	}
	
	/**
	 * 逻辑删除
	 * @param id
	 * @param likeParentIds
	 * @return
	 */
	public int deleteByGroupId(Serializable id, String likeParentIds){
		return update("update Group set delFlag = '" + Group.DEL_FLAG_DELETE + "' where groupId = :p1 or parentIds like :p2",
				new Parameter(id, likeParentIds));
	}
	
	
}
