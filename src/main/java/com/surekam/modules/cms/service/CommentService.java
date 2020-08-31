package com.surekam.modules.cms.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.cms.dao.CommentDao;
import com.surekam.modules.cms.entity.Comment;

/**
 * 评论Service
 */
@Service
@Transactional(readOnly = true)
public class CommentService extends BaseService {

	@Autowired
	private CommentDao commentDao;
	
	public Comment get(Integer id) {
		return commentDao.get(id);
	}
	
	public Page<Comment> find(Page<Comment> page, Comment comment) {
		DetachedCriteria dc = commentDao.createDetachedCriteria();
		if (comment.getContentId() != null){
			dc.add(Restrictions.eq("contentId", comment.getContentId()));
		}
		if (StringUtils.isNotEmpty(comment.getTitle())){
			dc.add(Restrictions.like("title", comment.getTitle(),MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(comment.getDelFlag())){
			dc.add(Restrictions.eq(Comment.FIELD_DEL_FLAG, comment.getDelFlag()));
		}
		if(comment.getCategory()!=null && comment.getCategory().getId()!=null && comment.getCategory().getId() != 1){
			dc.createAlias("category", "category");
			dc.add(Restrictions.or(
					Restrictions.eq("category.id",comment.getCategory().getId())
					,Restrictions.like("category.parentIds", ","+comment.getCategory().getId()+",",MatchMode.ANYWHERE)));
		}
		dc.addOrder(Order.desc("id"));
		return commentDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Comment comment) {
		commentDao.save(comment);
	}
	
	@Transactional(readOnly = false)
	public void delete(Integer[] ids, Boolean isRe) {
		if(ids!=null && ids.length>0){
			for(Integer id:ids){
				commentDao.updateDelFlag(id, isRe!=null&&isRe?Comment.DEL_FLAG_AUDIT:Comment.DEL_FLAG_DELETE);
			}
		}
	}
	
}
