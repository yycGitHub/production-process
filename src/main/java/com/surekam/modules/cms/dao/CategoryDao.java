package com.surekam.modules.cms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.cms.entity.Category;

/**
 * 栏目DAO接口
 */
@Repository
public class CategoryDao extends BaseDao<Category> {
	
	public List<Category> findByParentIdsLike(String parentIds){
		return find("from Category where parentIds like :p1", new Parameter(parentIds));
	}

	public List<Category> findByModule(String module){
		return find("from Category where delFlag=:p1 and (module='' or module=:p2) order by sort", 
				new Parameter(Category.DEL_FLAG_NORMAL, module));
	}
	
	public List<Category> findByParentId(Integer parentId, String isMenu){
		return find("from Category where delFlag=:p1 and parent.id=:p2 and inMenu=:p3 order by sort", 
				new Parameter(Category.DEL_FLAG_NORMAL, parentId, isMenu));
	}

	public List<Category> findByParentIdAndSiteId(Integer parentId, String siteId){
		return find("from Category where delFlag=:p1 and parent.id=:p2  order by sort", 
				new Parameter(Category.DEL_FLAG_NORMAL, parentId));
	}
	
	public List<Category> findByParentId(Integer parentId){
		return find("from Category where delFlag=:p1 and parent.id=:p2 order by sort", 
				new Parameter(Category.DEL_FLAG_NORMAL, parentId));
	}
	
	public List<Category> findByIdIn(Integer[] ids){
		return find("from Category where id in (:p1)", new Parameter(new Object[]{ids}));
	}
}
