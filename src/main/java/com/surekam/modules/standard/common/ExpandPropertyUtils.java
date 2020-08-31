package com.surekam.modules.standard.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.surekam.common.service.BaseService;
import com.surekam.common.utils.SpringContextHolder;
import com.surekam.modules.common.dao.FileInfoDao;
import com.surekam.modules.common.entity.FileInfo;
import com.surekam.modules.standard.dao.ExpandPropertyListDao;
import com.surekam.modules.standard.dao.StandardItemDao;
import com.surekam.modules.standard.entity.ApplicationItemValue;
import com.surekam.modules.standard.entity.ExpandPropertyList;
import com.surekam.modules.standard.entity.StandardItem;

public class ExpandPropertyUtils extends BaseService{
	private static StandardItemDao standardItemDao = SpringContextHolder.getBean(StandardItemDao.class);
	private static ExpandPropertyListDao expandPropertyListDao = SpringContextHolder.getBean(ExpandPropertyListDao.class);
	private static FileInfoDao fileInfoDao = SpringContextHolder.getBean(FileInfoDao.class);
	
	public static List<StandardItem> getExpandProperty(String targetId){
		DetachedCriteria dc = standardItemDao.createDetachedCriteria();
		dc.add(Restrictions.eq("targetId", targetId));
		dc.add(Restrictions.eq(StandardItem.FIELD_DEL_FLAG, StandardItem.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("sort"));
		return standardItemDao.find(dc);
	}
	
	public static Map<String,Object> getItemValues(String fromTableId,String fromTableName){
		DetachedCriteria dc = expandPropertyListDao.createDetachedCriteria();
		dc.add(Restrictions.eq("fromTableId", fromTableId));
		dc.add(Restrictions.eq("fromTableName", fromTableName));
		dc.add(Restrictions.eq(ExpandPropertyList.FIELD_DEL_FLAG, ExpandPropertyList.DEL_FLAG_NORMAL));
		List<ExpandPropertyList> lists = expandPropertyListDao.find(dc);
	    if(lists!=null && lists.size()>0){
	    	ExpandPropertyList expandPropertyList = lists.get(0);
	    	List<ApplicationItemValue> propertys = expandPropertyList.getAppItemValues();
	    	Map<String,Object> map = new HashMap<String, Object>();
	    	for(ApplicationItemValue itemValue:propertys){
	    		map.put(itemValue.getStandardItemId(), itemValue.getItemValue());
	    	}
	    	map.put("epListId", expandPropertyList.getId());
	    	return map;
	    }else{
	    	return null;
	    }
	}
	
	public static List<FileInfo> getFiles(String ywzbId,String ywzbType,String fieldMark){
		DetachedCriteria dc = fileInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("ywzbId", ywzbId));
		dc.add(Restrictions.eq("ywzbType", ywzbType));
		if(StringUtils.isNotBlank(fieldMark)){
			dc.add(Restrictions.eq("fieldMark", fieldMark));
		}
		dc.add(Restrictions.eq(FileInfo.FIELD_DEL_FLAG, FileInfo.DEL_FLAG_NORMAL));
		List<FileInfo> lists = fileInfoDao.find(dc);
		return lists;
	}
}
