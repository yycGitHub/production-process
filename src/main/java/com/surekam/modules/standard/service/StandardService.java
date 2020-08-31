package com.surekam.modules.standard.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.service.ServiceException;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.standard.dao.ApplicationItemValueDao;
import com.surekam.modules.standard.dao.ExpandPropertyListDao;
import com.surekam.modules.standard.dao.StandardDao;
import com.surekam.modules.standard.dao.StandardItemDao;
import com.surekam.modules.standard.dao.StandardItemValueDao;
import com.surekam.modules.standard.entity.ApplicationItemValue;
import com.surekam.modules.standard.entity.ExpandPropertyList;
import com.surekam.modules.standard.entity.Standard;
import com.surekam.modules.standard.entity.StandardItem;
import com.surekam.modules.standard.entity.StandardItemValue;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.security.SystemAuthorizingRealm;
import com.surekam.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class StandardService extends BaseService {
	
	@Autowired
	private StandardDao standardDao;
	@Autowired
	private StandardItemDao standardItemDao;
	@Autowired
	private StandardItemValueDao standardItemValueDao;
	@Autowired
	private ApplicationItemValueDao applicationItemValueDao;
	@Autowired
	private ExpandPropertyListDao expandPropertyListDao;
	@Autowired
	private SystemAuthorizingRealm systemRealm;
	@Autowired
	private OfficeDao officeDao;
	
	public Page<Standard> find(Page<Standard> page, Standard standard, boolean b) {
		DetachedCriteria dc = standardDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(standard.getDelFlag())) {
			dc.add(Restrictions.eq("delFlag",standard.getDelFlag()));		
		} else {
			dc.add(Restrictions.eq("delFlag",Standard.DEL_FLAG_NORMAL));	
		}
		if(StringUtils.isNotBlank(standard.getName())) {
			dc.add(Restrictions.like("name",standard.getName(),MatchMode.ANYWHERE));		
		}
		User currentUser = UserUtils.getUser();
		if(!currentUser.isAdmin()){
			dc.createAlias("office", "office");
			dc.add(dataScopeFilter(currentUser, "office", ""));
		}
		dc.addOrder(Order.asc("sort"));
		page = standardDao.find(page,dc);
		return page;
	}
	
	public List<Standard> findAllStandard(Standard standard) {
		DetachedCriteria dc = standardDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(standard.getDelFlag())) {
			dc.add(Restrictions.eq("delFlag",standard.getDelFlag()));		
		} else {
			dc.add(Restrictions.eq("delFlag",Standard.DEL_FLAG_NORMAL));	
		}
		dc.addOrder(Order.asc("sort"));
		List<Standard> standards= standardDao.find(dc);
		return standards;
	}
	
	public List<Standard> findAllCopyStandard(Standard standard) {
		DetachedCriteria dc = standardDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(standard.getParentIds())) {
			dc.add(Restrictions.or(
					Restrictions.eq("id",standard.getId()),
					Restrictions.like("parentIds", standard.getParentIds()+standard.getId(), MatchMode.START))
				   );		
		}
		dc.add(Restrictions.eq("delFlag",Standard.DEL_FLAG_NORMAL));	
		User currentUser = UserUtils.getUser();
		dc.addOrder(Order.asc("sort"));
		List<Standard> standards= standardDao.find(dc);
		if(!currentUser.isAdmin()){
			String rootHql = "from Standard where id='1' and delFlag ='"+Standard.DEL_FLAG_NORMAL+"'";
			List<Standard> list = standardDao.find(rootHql);
			standards.addAll(list);
		}
		return standards;
	}
	
	public List<Standard> findStandardByParentId(String parentId) {
		DetachedCriteria dc = standardDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag",Standard.DEL_FLAG_NORMAL));		
		User currentUser = UserUtils.getUser();
		if(!currentUser.isAdmin()){
			dc.createAlias("office", "office");
			dc.add(dataScopeFilter(currentUser, "office", ""));
		}
		if (StringUtils.isNotEmpty(parentId)){
			dc.add(Restrictions.eq("parent.id",parentId));
		}
		List<Standard> standards= standardDao.find(dc);
		return standards;
	}

	public Standard getStandard(String id) {
		return standardDao.get(id);
	}

	@Transactional(readOnly = false)
	public void saveStandard(Standard standard) {
		Standard parentStandard = this.getStandard(standard.getParent().getId());
		standard.setParent(parentStandard);
//		String oldParentIds = standard.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		standard.setParentIds(standard.getParent().getParentIds()+standard.getParent().getId()+",");		
		standardDao.save(standard);
//		// 更新子节点 parentIds
//		List<Standard> list = standardDao.findByParentIdsLike("%,"+standard.getId()+",%");
//		for (Standard e : list){
//			e.setParentIds(e.getParentIds().replace(oldParentIds, standard.getParentIds()));
//		}
//		standardDao.save(list);
		systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteStandard(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			standardDao.deleteById(ids[i], "%,"+ids[i]+",%");
		}
		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	public StandardItem findFisrtStandardItemByStandard(String stId) {
		String hql = "from StandardItem where delFlag=:p1 and targetId =:p2 order by createDate";
		List<StandardItem> items = standardItemDao.find(hql,new Parameter(StandardItem.DEL_FLAG_NORMAL,stId));
		if (items != null && items.size()>0) {
			return items.get(0);
		}else{
			return new StandardItem();
		}
	}
	
	public List<StandardItem> findStandardItems(String targetId) {
		String hql = "from StandardItem where delFlag=:p1 and targetId =:p2 order by sort";
		return standardItemDao.find(hql,new Parameter(StandardItem.DEL_FLAG_NORMAL,targetId));
	}
	
	public StandardItem getStandardItem(String standardItemId) {
		// TODO Auto-generated method stub
		return standardItemDao.get(standardItemId);
	}
	
	@Transactional(readOnly = false)
	public void saveStandardItem(StandardItem standardItem) {
		standardItemDao.clear();
		standardItemDao.save(standardItem);
	}
	
	@Transactional(readOnly = false)
	public String deleteStandardItem(String[] ids) {
		try {
			for(String id:ids){
				standardItemDao.deleteById(id);
			}
			return "删除成功";
		} catch (Exception e) {
			throw new ServiceException("删除失败:"+e.getMessage());
		}
	}

	public List<StandardItemValue> findStandardItemValues(String standardItemId) {
		String hql = "from StandardItemValue where delFlag=:p1 and standardItem.id=:p2 order by createDate";
		return standardItemValueDao.find(hql,new Parameter(StandardItemValue.DEL_FLAG_NORMAL,standardItemId));
	}

	public StandardItemValue getStandardItemValue(String standardItemValueId) {
		// TODO Auto-generated method stub
		return standardItemValueDao.get(standardItemValueId);
	}
	
	@Transactional(readOnly = false)
	public void saveStandardItemValue(StandardItemValue standardItemValue) {
		standardItemValueDao.clear();
		standardItemValueDao.save(standardItemValue);
	}
	
	@Transactional(readOnly = false)
	public String deleteStandardItemValue(String[] ids) {
		try {
			for(String id:ids){
				standardItemValueDao.deleteById(id);
			}
			return "删除成功";
		} catch (Exception e) {
			throw new ServiceException("删除失败:"+e.getMessage());
		}
	}

	
	public List<ApplicationItemValue> findApplicationItemValueList(ApplicationItemValue applicationItemValue) {
		DetachedCriteria dc = applicationItemValueDao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(applicationItemValue.getExpandPropertyList().getId())) {
			dc.add(Restrictions.eq("applicationUserItemList.id",applicationItemValue.getExpandPropertyList().getId()));
		}
		dc.add(Restrictions.eq("delFlag",ApplicationItemValue.DEL_FLAG_NORMAL));
		List<String> standardItemTypeList = new ArrayList<String>();
		standardItemTypeList.add("2");
		standardItemTypeList.add("6");
		dc.add(Restrictions.in("standardItemType", standardItemTypeList));
		dc.add(Restrictions.ne("itemValue", ""));
		return applicationItemValueDao.find(dc);
	}
	
	public List<StandardItem> findAppStandardItems(String auiId) {
		String hql = "select sti from StandardItem as sti,ApplicationUserItemList as auil,Standard std"
				   + " where sti.targetId = std.id and auil.mainStandardId = std.id"
				   + " and auil.id=:p1"
				   + " and sti.delFlag='"+StandardItem.DEL_FLAG_NORMAL+"'";
		return applicationItemValueDao.find(hql,new Parameter(auiId));
	}

	public Page<ExpandPropertyList> findAppUserItemValuesList(Page<ExpandPropertyList> page,
			ExpandPropertyList expandPropertyList) {
		DetachedCriteria dc = expandPropertyListDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(expandPropertyList.getFromTableId())){
			dc.add(Restrictions.eq("fromTableId", expandPropertyList.getFromTableId()));
		}
		if (StringUtils.isNotEmpty(expandPropertyList.getDelFlag())){
			dc.add(Restrictions.eq("delFlag", expandPropertyList.getDelFlag()));
		}
		dc.addOrder(Order.desc("createDate"));
		return expandPropertyListDao.find(page,dc);
	}
	

	
	public List<ExpandPropertyList> findAppUserItemValuesList(String getApplicationUserItemListHql, Parameter parameter) {
		List<ExpandPropertyList> applicationUserItemLists = expandPropertyListDao.find(getApplicationUserItemListHql, parameter);
		return applicationUserItemLists;
	}
	
	public List<ExpandPropertyList> findAppUserItemValuesListSql(String getApplicationUserItemListSql, Parameter parameter) {
		List<ExpandPropertyList> applicationUserItemLists = expandPropertyListDao.findBySql(getApplicationUserItemListSql, parameter,ExpandPropertyList.class);
		return applicationUserItemLists;
	}
	
	public void SaveAppUserItemValuesList(ExpandPropertyList applicationUserItemList) {
		expandPropertyListDao.save(applicationUserItemList);
	}

	public List<Standard> findAppStandard(String applicationUserItemListId) {
		ExpandPropertyList itemList = expandPropertyListDao.get(applicationUserItemListId);
		String hql = "from Standard where parentIds like '%"+itemList.getMainStandardId()+"%' or id=:p1";
		List<Standard> list = standardDao.find(hql,new Parameter(itemList.getMainStandardId()));
		return list;
	}

	public List<ApplicationItemValue> showItemValue(String applicationUserItemListId, String valueStandardId) {
		String hql = "select applicationItemValue "
				+ " from StandardItem standardItem,ApplicationItemValue applicationItemValue "
				+ " where "
				+ " standardItem.id = applicationItemValue.standardItemId "
				+ " and applicationItemValue.applicationUserItemList.id=:p1 "
				+ " and standardItem.targetId =:p2";
		return  expandPropertyListDao.find(hql,new Parameter(applicationUserItemListId,valueStandardId));
	}
	
	@Transactional(readOnly = false)
	public void saveAppItemValue(ApplicationItemValue applicationItemValue) {
		String hql = "from ApplicationItemValue where applicationUserItemList.id=:p1 and standardItemId =:p2";
		ApplicationItemValue applicationItemValueTemp = applicationItemValueDao.getByHql(hql, 
				new Parameter(applicationItemValue.getExpandPropertyList().getId(),applicationItemValue.getStandardItemId()));
		if(applicationItemValueTemp == null){
			applicationItemValueTemp = applicationItemValue;
		}
		applicationItemValueTemp.setItemValue(applicationItemValue.getItemValue());
		applicationItemValueTemp.setBlobValue(applicationItemValue.getBlobValue());
		applicationItemValueDao.save(applicationItemValueTemp);
	}

	public List<StandardItem> findStanrdItemsByItemType(String standardId,
			String[] types) {
		DetachedCriteria dc = standardItemDao.createDetachedCriteria();
		if(types!=null && types.length>0){
			dc.add(Restrictions.in("itemType",types));
		}
		if(StringUtils.isNotBlank(standardId)){
			dc.add(Restrictions.eq("targetId",standardId));
		}
		dc.add(Restrictions.eq("delFlag",StandardItem.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		return standardItemDao.find(dc);
	}
	
	/**
	 * 复制树的所有子节点
	 * @param office 所属机构
	 * @param sourcelist 复制目标树
	 * @param curId 开始节点
	 * @param inSertParntStandard 开始节点的父节点
	 */
	@Transient 
	public void copyList(Office office, List<Standard> sourcelist, String curId, Standard inSertParntStandard) {
		for(int i=0; i<sourcelist.size(); i++) {
			Standard e = sourcelist.get(i);
			if(e.getId() != null && e.getId().equals(curId)) {
				for(int j=0; j<sourcelist.size(); j++) {
					Standard child = sourcelist.get(j);
					if(child.getParent() != null && child.getParent().getId() != null && child.getParent().getId().equals(e.getId())) {
						Standard insertStandard = new Standard();
						insertStandard.setParent(inSertParntStandard);
						insertStandard.setName(child.getName());
						insertStandard.setSort(child.getSort());
						saveStandard(insertStandard);
						copyList(office, sourcelist, child.getId(), insertStandard);
					}
				}
			}
		}
	}

	/**
	 * @param fromTableId 扩展目标表数据ID
	 * @param fromTableName 扩展目标表名称
	 * @param targetId 扩展字段目标ID
	 * @param request
	 */
	@Transactional(readOnly = false)
	public void saveExpandPropertyList(String fromTableId,String fromTableName,String targetId,HttpServletRequest request) {
		ExpandPropertyList expandPropertyList = new ExpandPropertyList();
		String epListId = request.getParameter("epListId");
		if(StringUtils.isNotBlank(epListId)){
			expandPropertyList = expandPropertyListDao.get(epListId);
		}
		expandPropertyList.setFromTableId(fromTableId);
		expandPropertyList.setFromTableName(fromTableName);
		expandPropertyListDao.save(expandPropertyList);
		
		String hql = "from StandardItem where targetId=:p1 and delFlag=:p2";
		List<StandardItem> standardItems = standardItemDao.find(hql,new Parameter(targetId,StandardItem.DEL_FLAG_NORMAL));	
		Map<String, String> map = getRequestMap(request, "ep_", false);
		Set<String> keys = map.keySet();  
		Iterator<String> iterator = keys.iterator();
		List<ApplicationItemValue> apItemValues = expandPropertyList.getAppItemValues();
		if(StringUtils.isNotBlank(epListId) && apItemValues!=null && apItemValues.size()>0){
			while (iterator.hasNext()) {//更新
				String key = iterator.next();  
				String value = map.get(key);
				for(StandardItem item:standardItems){
					if(item.getKeyName().equals(key)){
						boolean flag = false;
						for(ApplicationItemValue itemValue:apItemValues){
							//如果是以前就有的值，就更新
							if(item.getId().equals(itemValue.getStandardItemId())){
								flag = true;
								itemValue.setExpandPropertyList(expandPropertyList);
								itemValue.setStandardItemId(item.getId());
								itemValue.setItemValue(value);
								applicationItemValueDao.save(itemValue);
							}
						}
						if(!flag){
							ApplicationItemValue aIValue = new ApplicationItemValue();
							aIValue.setExpandPropertyList(expandPropertyList);
							aIValue.setStandardItemId(item.getId());
							aIValue.setItemValue(value);
							applicationItemValueDao.save(aIValue);
						}
					}
				}
			} 
		}else{
			while (iterator.hasNext()) {//新增
				String key = iterator.next();  
				String value = map.get(key);
				for(StandardItem item:standardItems){
					if(item.getKeyName().equals(key)){
						ApplicationItemValue aIValue = new ApplicationItemValue();
						aIValue.setExpandPropertyList(expandPropertyList);
						aIValue.setStandardItemId(item.getId());
						aIValue.setItemValue(value);
						applicationItemValueDao.save(aIValue);
					}
				}
			} 
		}
	}
	
    @SuppressWarnings("unchecked")
	private static Map<String, String> getRequestMap(HttpServletRequest request, String prefix, boolean nameWithPrefix) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> names = request.getParameterNames();
        String name, key, value;
        while (names.hasMoreElements()) {
            name = names.nextElement();
            if (name.startsWith(prefix)) {
                key = nameWithPrefix ? name : name.substring(prefix.length());
                value = StringUtils.join(request.getParameterValues(name), ',');
                map.put(key, value);
            }
        }
        return map;
    }
}
