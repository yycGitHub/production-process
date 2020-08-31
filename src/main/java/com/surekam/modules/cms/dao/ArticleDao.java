package com.surekam.modules.cms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.cms.entity.Article;

/**
 * 文章DAO接口
 */
@Repository
public class ArticleDao extends BaseDao<Article> {
	
	public List<Article> findByIdIn(Integer[] ids){
		return find("from Article where id in (:p1)", new Parameter(new Object[]{ids}));
	}
	
	public int updateHitsAddOne(Integer id){
		return update("update Article set hits=hits+1 where id = :p1", new Parameter(id));
	}
	
	public int updateExpiredWeight(){
		return update("update Article set weight=0 where weight > 0 and weightDate < current_timestamp()");
	}
	
	public Article findByIdIn(Integer ids){
		return getByHql("from Article where id in (:p1)", new Parameter(ids));
	}
}
