package com.surekam.modules.cms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.cms.entity.CategoryRoleUser;

/**
 * 栏目管理员DAO接口
 * @author liuyi
 * @version 2018-01-29
 */
@Repository
public class CategoryRoleUserDao extends BaseDao<CategoryRoleUser> {
	
	
	public List<CategoryRoleUser> findAllList() {
		return find("from CategoryRoleUser where delFlag=:p1  order by id", new Parameter(CategoryRoleUser.DEL_FLAG_NORMAL));
	}
	
	public List<CategoryRoleUser> findListByCategoryId(Integer id) {
		return find("from CategoryRoleUser where delFlag=:p1 and categoryId =:p2 order by id", new Parameter(CategoryRoleUser.DEL_FLAG_NORMAL,id));
	}
	
	public int updateRole (CategoryRoleUser categoryRoleUser){
		return this.updateBySql("UPDATE cms_category_role_user SET ROLE_ID = :p1 WHERE CATEGORY_ID = :p2 AND DEL_FLAG = :p3"
				,new Parameter(categoryRoleUser.getRoleId(),categoryRoleUser.getCategoryId(),CategoryRoleUser.DEL_FLAG_NORMAL));
	}
	
	public int updatePlate (CategoryRoleUser categoryRoleUser){
		return this.updateBySql("UPDATE cms_category_role_user SET USER_ID = :p1 WHERE CATEGORY_ID = :p2 AND DEL_FLAG = :p3"
				,new Parameter(categoryRoleUser.getUserId(),categoryRoleUser.getCategoryId(),CategoryRoleUser.DEL_FLAG_NORMAL));
	}
	
	public int deleteBycategoryId(Integer categoryId){
		return update("update CategoryRoleUser set delFlag='" + CategoryRoleUser.DEL_FLAG_DELETE + "' where categoryId = :p1", 
				new Parameter(categoryId));
		
	}
	
}
