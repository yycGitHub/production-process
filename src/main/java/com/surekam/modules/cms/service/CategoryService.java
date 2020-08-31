package com.surekam.modules.cms.service;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.CacheUtils;
import com.surekam.modules.cms.dao.CategoryDao;
import com.surekam.modules.cms.entity.Category;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.utils.UserUtils;

/**
 * 栏目Service
 */
@Service
@Transactional(readOnly = true)
public class CategoryService extends BaseService {

	public static final String CACHE_CATEGORY_LIST = "categoryList";
	
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private CategoryRoleUserService categoryRoleUserService;
	
	public Category get(Integer id) {
		return categoryDao.get(id);
	}
	
	public List<Category> findByUser(Integer fromId,String module){
		User user = UserUtils.getUser();
		DetachedCriteria dc = categoryDao.createDetachedCriteria();
		dc.createAlias("office", "office").createAlias("createBy", "user");
		if (fromId != null){
			dc.add(Restrictions.like("parentIds", ","+fromId+",",MatchMode.ANYWHERE));
		}
		dc.add(dataScopeFilter(user, "office", "user"));
		dc.add(Restrictions.eq("delFlag", Category.DEL_FLAG_NORMAL));
		List<Category> list = categoryDao.find(dc);
		// 将没有父节点的节点，找到父节点
		Set<Integer> parentIdSet = Sets.newHashSet();
		for (Category e : list){
			if (e.getParent()!=null && e.getParent().getId() != null){
				boolean isExistParent = false;
				for (Category e2 : list){
					if (e.getParent().getId().equals(e2.getId())){
						isExistParent = true;
						break;
					}
				}
				if (!isExistParent){
					parentIdSet.add(e.getParent().getId());
				}
			}
		}
		if (parentIdSet.size() > 0){
			dc = categoryDao.createDetachedCriteria();
			dc.add(Restrictions.in("id", parentIdSet));
			dc.add(Restrictions.eq("delFlag", Category.DEL_FLAG_NORMAL));
			list.addAll(0, categoryDao.find(dc));
		}
		
		List<Category> categoryList = Lists.newArrayList(); 
		for (Category e : list){
			if (StringUtils.isNotEmpty(module)){
				if (module.equals(e.getModule()) || "".equals(e.getModule())){
					categoryList.add(e);
				}
			}else{
				categoryList.add(e);
			}
		}
		return categoryList;
	}

	public List<Category> findByParentId(Integer parentId){
		return categoryDao.findByParentId(parentId);
	}
	
	public Page<Category> find(Page<Category> page, Category category) {
		DetachedCriteria dc = categoryDao.createDetachedCriteria();
		if (category.getParent()!=null && category.getParent().getId()!=null){
			dc.createAlias("parent", "parent");
			dc.add(Restrictions.eq("parent.id", category.getParent().getId()));
		}
		if (StringUtils.isNotBlank(category.getInMenu()) && Category.SHOW.equals(category.getInMenu())){
			dc.add(Restrictions.eq("inMenu", category.getInMenu()));
		}
		dc.add(Restrictions.eq(Category.FIELD_DEL_FLAG, Category.DEL_FLAG_NORMAL));
		return categoryDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(Category category) {
		if("".equals(category.getId())){
			category.setId(null);
		}
		category.setParent(this.get(category.getParent().getId()));
		String oldParentIds = category.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		category.setParentIds(category.getParent().getParentIds()+category.getParent().getId()+",");
        if (StringUtils.isNotBlank(category.getViewConfig())){
            category.setViewConfig(StringEscapeUtils.unescapeHtml4(category.getViewConfig()));
        }
		categoryDao.clear();
        categoryDao.save(category);
		// 更新子节点 parentIds
		List<Category> list = categoryDao.findByParentIdsLike("%,"+category.getId()+",%");
		for (Category e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, category.getParentIds()));
		}
		categoryDao.save(list);
		UserUtils.removeCache(CACHE_CATEGORY_LIST);
		CacheUtils.remove("cmsCache", "mainNavList");
	}
	
	@Transactional(readOnly = false)
	public void delete(Integer id) {
		Category category = get(id);
		if (category!=null){
			categoryDao.deleteById(id, "%,"+id+",%");
			UserUtils.removeCache(CACHE_CATEGORY_LIST);
		}
	}
	
	/**
	 * 通过编号获取栏目列表
	 */
	public List<Category> findByIds(String ids) {
		List<Category> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids,",");
		if (idss.length>0){
			Integer[] idInts = new Integer[idss.length];
			for (int i=0;i<idss.length;i++){
				idInts[i] = Integer.valueOf(idss[i]);
			}
			List<Category> l = categoryDao.findByIdIn(idInts);
			for (String id : idss){
				for (Category e : l){
					if (e.getId().equals(id)){
						list.add(e);
						break;
					}
				}
			}
		}
		return list;
	}

	public List<Category> findByParentIdsLike(String categoryid) {
		DetachedCriteria dc = categoryDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(categoryid)){
			dc.add(Restrictions.like("parentIds", ","+categoryid+",",MatchMode.ANYWHERE));
		}
		dc.add(Restrictions.eq(Category.FIELD_DEL_FLAG, Category.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("sort"));
		return categoryDao.find(dc);
	}

	public List<Category> findAll() {
		DetachedCriteria dc = categoryDao.createDetachedCriteria();
		dc.add(Restrictions.eq(Category.FIELD_DEL_FLAG,Category.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("sort"));
		return categoryDao.find(dc);
	}
	
	
	
	public List<Category> findCategoryWithCategoryRole() {
		List<Integer> categoryIdList = Lists.newArrayList();
		categoryIdList = categoryRoleUserService.findCategoryIdByUserId();
		User user = UserUtils.getUser();
		DetachedCriteria dc = categoryDao.createDetachedCriteria();
		dc.add(Restrictions.eq(Category.FIELD_DEL_FLAG,Category.DEL_FLAG_NORMAL));
		//过滤栏目
		if(!user.isAdmin() && categoryIdList!=null && categoryIdList.size()>0){
			dc.add(Restrictions.in("id",categoryIdList));
		}
		//dc.add(Restrictions.in("id",list));
		//dc.createAlias("category.id", "id").createAlias("createBy", "createBy");
		//dc.add(dataScopeFilter(user,"id",""));
		//dc.add(Restrictions.in("id",categoryIdList));
		dc.addOrder(Order.asc("sort"));
		return categoryDao.find(dc);
	}
	
}
