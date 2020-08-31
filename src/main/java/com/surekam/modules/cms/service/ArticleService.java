package com.surekam.modules.cms.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.shiro.SecurityUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.CacheUtils;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.cms.dao.ArticleDao;
import com.surekam.modules.cms.dao.CategoryDao;
import com.surekam.modules.cms.entity.Article;
import com.surekam.modules.cms.entity.Category;
import com.surekam.modules.common.dao.FileInfoDao;
import com.surekam.modules.common.entity.FileInfo;
import com.surekam.modules.standard.service.StandardService;
import com.surekam.modules.sys.utils.UserUtils;

/**
 * 文章Service
 */
@Service
@Transactional(readOnly = true)
public class ArticleService extends BaseService {

	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private FileInfoDao fileInfoDao;
	@Autowired
	private StandardService standardService;
	
	public Article get(Integer id) {
		return articleDao.get(id);
	}
	
	@Transactional(readOnly = false)
	public Page<Article> find(Page<Article> page, Article article, boolean isDataScopeFilter) {
		// 更新过期的权重，间隔为“6”个小时
		Date updateExpiredWeightDate =  (Date)CacheUtils.get("updateExpiredWeightDateByArticle");
		if (updateExpiredWeightDate == null || (updateExpiredWeightDate != null 
				&& updateExpiredWeightDate.getTime() < new Date().getTime())){
			articleDao.updateExpiredWeight();
			CacheUtils.put("updateExpiredWeightDateByArticle", DateUtils.addHours(new Date(), 6));
		}
		DetachedCriteria dc = articleDao.createDetachedCriteria();
		dc.createAlias("category", "category");
		if (article.getCategory()!=null && article.getCategory().getId() != null && !Category.isRoot(article.getCategory().getId())){
			Category category = categoryDao.get(article.getCategory().getId());
			if (category!=null){
				dc.add(Restrictions.or(
						Restrictions.eq("category.id", category.getId()),
						Restrictions.like("category.parentIds", "%,"+category.getId()+",%")));
				article.setCategory(category);
			}
		}
		if (StringUtils.isNotEmpty(article.getTitle())){
			dc.add(Restrictions.like("title", "%"+article.getTitle()+"%"));
		}
		if (StringUtils.isNotEmpty(article.getPosid())){
			dc.add(Restrictions.like("posid", "%,"+article.getPosid()+",%"));
		}
		if (StringUtils.isNotEmpty(article.getImage())&&Article.YES.equals(article.getImage())){
			dc.add(Restrictions.and(Restrictions.isNotNull("image"),Restrictions.ne("image","")));
		}
		if (article.getCreateBy()!=null && StringUtils.isNotBlank(article.getCreateBy().getId())){
			dc.add(Restrictions.eq("createBy.id", article.getCreateBy().getId()));
		}
		if (isDataScopeFilter){
			dc.createAlias("category.office", "categoryOffice").createAlias("createBy", "createBy");
			dc.add(dataScopeFilter(UserUtils.getUser(), "categoryOffice", "createBy"));
		}
		dc.add(Restrictions.eq(Article.FIELD_DEL_FLAG, article.getDelFlag()));
		if (StringUtils.isBlank(page.getOrderBy())){
			dc.addOrder(Order.desc("weight"));
			dc.addOrder(Order.desc("updateDate"));
		}
		return articleDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Article article,String[] fileIds,HttpServletRequest request) {
		if (article.getContent()!=null){
			article.setContent(StringEscapeUtils.unescapeHtml4(article.getContent()));
		}
		// 如果没有审核权限，则将当前内容改为待审核状态
		if (!SecurityUtils.getSubject().isPermitted("cms:article:audit")){
			article.setDelFlag(Article.DEL_FLAG_AUDIT);
		}
		// 如果栏目不需要审核，则将该内容设为发布状态
		if (article.getCategory()!=null&& article.getCategory().getId() != null){
			Category category = categoryDao.get(article.getCategory().getId());
			if (!Article.YES.equals(category.getIsAudit())){
				article.setDelFlag(Article.DEL_FLAG_NORMAL);
			}
		}
		article.setUpdateBy(UserUtils.getUser());
		article.setUpdateDate(new Date());
        if (StringUtils.isNotBlank(article.getViewConfig())){
            article.setViewConfig(StringEscapeUtils.unescapeHtml4(article.getViewConfig()));
        }
		articleDao.clear();
		articleDao.save(article);
		
		//更新文件表关联主表业务
		if(fileIds !=null && fileIds.length>0){
			String hql = "update FileInfo SET ywzbId='"+article.getId()+"',ywzbType='"
					+FileInfo.CMS_ACTICLE_TYPE+"' where id in(:p1)";
			fileInfoDao.update(hql,new Parameter(new Object[]{fileIds}));
		}
		//保存扩展字段值
		standardService.saveExpandPropertyList(article.getId()+"", "article", article.getCategory().getId()+"", request);
	}
	
	@Transactional(readOnly = false)
	public void delete(Integer[] ids, Boolean isRe) {
		// 使用下面方法，以便更新索引。
		for (int i = 0; i < ids.length; i++) {
			Article article = articleDao.get(ids[i]);
			article.setDelFlag(isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
			articleDao.save(article);
		}
	}
	
	/**
	 * 通过编号获取内容标题
	 * @return new Object[]{栏目Id,文章Id,文章标题}
	 */
	public List<Object[]> findByIds(String ids) {
		List<Object[]> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids,",");
		if (idss.length>0){
			Integer[] idInts = new Integer[idss.length];
			for (int i=0;i<idss.length;i++){
				idInts[i] = Integer.valueOf(idss[i]);
			}
			List<Article> l = articleDao.findByIdIn(idInts);
			for (Article e : l){
				list.add(new Object[]{e.getCategory().getId(),e.getId(),StringUtils.abbr(e.getTitle(),50)});
			}
		}
		return list;
	}
	
	/**
	 * 点击数加一
	 */
	@Transactional(readOnly = false)
	public void updateHitsAddOne(Integer id) {
		articleDao.updateHitsAddOne(id);
	}
	
	/**
	 * 更新索引
	 */
	public void createIndex(){
		articleDao.createIndex();
	}
	
	/**
	 * 全文检索
	 */
	public Page<Article> search(Page<Article> page, String q, String categoryId, String beginDate, String endDate){
		
		// 设置查询条件
		BooleanQuery query = articleDao.getFullTextQuery(q, "title","keywords","description","content");
		
		// 设置过滤条件
		List<BooleanClause> bcList = Lists.newArrayList();

		bcList.add(new BooleanClause(new TermQuery(new Term(Article.FIELD_DEL_FLAG, Article.DEL_FLAG_NORMAL)), Occur.MUST));
		if (StringUtils.isNotBlank(categoryId)){
			String[] ids = categoryId.split(",");
			for(String id:ids){
				bcList.add(new BooleanClause(new TermQuery(new Term("category.ids", id+"")), Occur.MUST));
			}
		}
		
		if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {   
			bcList.add(new BooleanClause(new TermRangeQuery("updateDate", beginDate.replaceAll("-", ""),
					endDate.replaceAll("-", ""), true, true), Occur.MUST));
		}   
		
		BooleanQuery queryFilter = articleDao.getFullTextQuery((BooleanClause[])bcList.toArray(new BooleanClause[bcList.size()]));

		// 设置排序（默认相识度排序）
		Sort sort = null;//new Sort(new SortField("updateDate", SortField.DOC, true));
		// 全文检索
		articleDao.search(page, query, queryFilter, sort);
		articleDao.clear();//防止修改后自动保存
		// 关键字高亮
		articleDao.keywordsHighlight(query, page.getList(), 30, "title");
		articleDao.keywordsHighlight(query, page.getList(), 130, "description","content");
		
		return page;
	}
		
		/**
		 * 通过栏目id获取文章
		 */	
		public List<Article> getArticle(Integer categoryid){
			DetachedCriteria dc = articleDao.createDetachedCriteria();
			dc.add(Restrictions.eq("category.id", categoryid));
			dc.add(Restrictions.eq(Article.FIELD_DEL_FLAG, Article.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("weight"));
			return articleDao.find(dc);
		}
		
		public Article findByIdIn(Integer ids){
			return articleDao.findByIdIn(ids);
		}

		public List<Article> getArticleByMeetingId(String meetingId) {
			DetachedCriteria dc = articleDao.createDetachedCriteria();
			if (StringUtils.isNotBlank(meetingId)) {
				dc.add(Restrictions.eq("meeting.id", meetingId));
			}
			dc.add(Restrictions.eq(Article.FIELD_DEL_FLAG, Article.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("weight"));
			return articleDao.find(dc);
		}
}
