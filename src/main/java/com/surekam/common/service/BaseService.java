/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.common.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.surekam.modules.sys.entity.Role;
import com.surekam.modules.sys.entity.User;

/**
 * Service基类
 * @author sureserve
 * @version 2013-05-15
 */
public abstract class BaseService {
	
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	
	/**
	 * 项目组数据范围过滤,只有组管理员才需要进行组数据过滤，普通组员
	 * @param user
	 * @param groupId
	 * @param groupAlias
	 * @param userAlias
	 * @return
	 */
	/*protected static String dataGroupScopeFilter(User user,String groupId, String groupAlias, String userAlias) {
		Junction junction = Restrictions.disjunction();
		if (!user.isAdmin()){
			boolean isMaster = false;
			//该用户在当前组是否是组管理
			Group userGroup = new Group();
			//.....
			
			//
			if(isMaster){
				junction.add(Restrictions.eq(groupAlias+".groupId", groupId));
				junction.add(Restrictions.like(groupAlias+".parentIds", user.getOffice().getParentIds()+user.getOffice().getId()+",%"));
			}
			
			List<Group> listGroup = Lists.newArrayList();//拥有组列表
			listGroup = user.getGroupList();
			for (Group group : listGroup) {
				
			}
		}
		Iterator<Criterion> it = junction.conditions().iterator();
		StringBuilder ql = new StringBuilder();
		ql.append(" and (");
		if (it.hasNext()){
			ql.append(it.next());
		}
		String[] strField = {".parentIds like ", ".type="}; // 需要给字段增加“单引号”的字段。
		while (it.hasNext()) {
			ql.append(" or (");
			String s = it.next().toString();
			for(String field : strField){
				s = s.replaceAll(field + "(\\w.*)", field + "'$1'");
			}
			ql.append(s).append(")");
		}
		ql.append(")");
		return ql.toString();
	}*/
	
	/** 
	 * 测试
     * @param args 
     */
	/*  
    public static void main(String[] args) {  
    	
    	List<User> list =  Lists.newArrayList();
    	list = installDataUser();
    	List<String> valueList =  Lists.newArrayList();
    	valueList.add("世企云联");
    	valueList.add("腾龙");
    	List<User> allResList =  Lists.newArrayList();
    	System.out.println("------开始过滤------");
    	long beginTime=System.currentTimeMillis(); 
    	
    	//测试
    	allResList =(List<User>) selectData(list,"cropName",valueList);
    	
    	
    	//for(int i =0;i<allResList.size();i++){
    		//User u = (User)allResList.get(i);
			//System.out.println("------------"+u.getName()); 
    	//}
    	System.out.println("=============处理过滤结束，执行耗时 : "+(System.currentTimeMillis()-beginTime)/1000f+" 秒 "); 
    }  



    *//**
     * 测试数据组装
     *//*
    public static List<User> installDataUser(){
    	List<User> list =  Lists.newArrayList();//拥有组列表
    	User user = new User();  
    	user.setCropName("农科院信息所");
    	user.setName("张三");
        list.add(user);  
        user = new User();  
    	user.setCropName("");
    	user.setName("王五");
        list.add(user);  
        user = new User();  
    	user.setCropName("腾龙");
    	user.setName("刘7");
        list.add(user);  
        user = new User();  
    	user.setCropName("世企云联");
    	user.setName("李四");
        list.add(user);  
        user = new User();  
    	user.setCropName("世企云联");
    	user.setName("赵6");
        list.add(user);  
        user = new User();  
    	user.setCropName("世企云联");
    	user.setName("马8");
        list.add(user);  
        for(int i=0;i<10;i++){
        	User u = new User();
        	u.setCropName("世企云联");
        	u.setName("李四"+i);
        	list.add(u);
        }
        return list;
    }*/
    
	
    /**
     * @param list 传入集合参数
     * @param column 需要过滤的属性名
     * @param valueList 需要过滤的属性值List
     * @return Collection<E>
     */
    protected  static <E> Collection<E> selectData(List<E> list,String column,List<String> valueList){
    	List<E> allResList =  Lists.newArrayList();
    	for (String id : valueList) {
    		List<E> resList =  Lists.newArrayList();
    		resList =  (List<E>) selectDatafilter(list,column,id,true);//当前组
    		allResList.addAll(resList);
		}
    	
    	return allResList;
    }
	
	/**
     * 根据集合中某一列的某个属性值进行数据过滤
     * @param Collection<E> 传入集合参数
     * @param columnName 	需要过滤的属性名
     * @param columnValue	需要过滤的属性值
     * @param select  select为true时:得到只包含该属性值的集合; select为false时:得到去除该属性值的集合;
     * @return Collection<E>
     */
    protected static <E> Collection<E> selectDatafilter(Collection<E> list,  String columnName,String columnValue,boolean select){
    	if(list==null || list.size()==0 || StringUtils.isBlank(columnName) || columnValue==null){
    		return list;
    	}
    	Predicate predicate=new MyPredicate(columnName,columnValue);   
    	Collection<E>  resList = Lists.newArrayList();//返回集合
    	if(select){
    		CollectionUtils.select(list, predicate,resList);  
    	}else{
    		CollectionUtils.selectRejected(list, predicate,resList);
    	}
    	//PredicateUtils.andPredicate(predicate1, predicate2)
        return resList;
    }
    
   
	
	
	
	/**
	 * 数据范围过滤
	 * @param user 当前用户对象，通过“UserUtils.getUser()”获取
	 * @param officeAlias 机构表别名，例如：dc.createAlias("office", "office");
	 * @param userAlias 用户表别名，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	protected static Junction dataScopeFilter(User user, String officeAlias, String userAlias) {

		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();
		Junction junction = Restrictions.disjunction();
		
		// 超级管理员，跳过权限过滤// 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；6：所在栏目及以下数据8：仅本人数据；9：按明细设置）
		if (!user.isAdmin()){
			for (Role r : user.getRoleList()){
				if (!dataScope.contains(r.getDataScope()) && StringUtils.isNotBlank(officeAlias)){
					boolean isDataScopeAll = false;
					if (Role.DATA_SCOPE_ALL.equals(r.getDataScope())){
						isDataScopeAll = true;
					}
					else if (Role.DATA_SCOPE_COMPANY_AND_CHILD.equals(r.getDataScope())){
						junction.add(Restrictions.eq(officeAlias+".id", user.getCompany().getId()));
						junction.add(Restrictions.like(officeAlias+".parentIds", user.getCompany().getParentIds()+user.getCompany().getId()+",%"));
					}
					else if (Role.DATA_SCOPE_COMPANY.equals(r.getDataScope())){
						junction.add(Restrictions.eq(officeAlias+".id", user.getCompany().getId()));
						junction.add(Restrictions.and(Restrictions.eq(officeAlias+".parent.id", user.getCompany().getId()),
								Restrictions.eq(officeAlias+".type", "2"))); // 包括本公司下的部门
					}
					else if (Role.DATA_SCOPE_OFFICE_AND_CHILD.equals(r.getDataScope())){
						junction.add(Restrictions.eq(officeAlias+".id", user.getOffice().getId()));
						junction.add(Restrictions.like(officeAlias+".parentIds", user.getOffice().getParentIds()+user.getOffice().getId()+",%"));
					}
					else if (Role.DATA_SCOPE_OFFICE.equals(r.getDataScope())){
						junction.add(Restrictions.eq(officeAlias+".id", user.getOffice().getId()));
					}
					else if (Role.DATA_SCOPE_CUSTOM.equals(r.getDataScope())){
						if(r.getOfficeIdList() != null && r.getOfficeIdList().size()>0){
							junction.add(Restrictions.in(officeAlias+".id", r.getOfficeIdList()));
						}
					}else if (Role.DATA_SCOPE_SELF.equals(r.getDataScope())){
						junction.add(Restrictions.eq("createBy.id", user.getId()));
					}
					/*else if (Role.DATA_SCOPE_CATEGORY.equals(r.getDataScope())){
						if(r.getCategortIdList() != null && r.getCategortIdList().size()>0){
							junction.add(Restrictions.in(officeAlias, r.getCategortIdList()));
							//junction.add(Restrictions.in("id", r.getCategortIdList()));
						}
					}*/
					if (!isDataScopeAll){
						if (StringUtils.isNotBlank(userAlias)){
							junction.add(Restrictions.eq(userAlias+".id", user.getId()));
						}
//						else {
//							junction.add(Restrictions.isNull(officeAlias+".id"));
//						}
					}else{
						// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
						junction = Restrictions.disjunction();
						break;
					}
					dataScope.add(r.getDataScope());
				}
			}
		}
		return junction;
	}
	
	/**
	 * 数据范围过滤
	 * @param user 当前用户对象，通过“UserUtils.getUser()”获取
	 * @param officeAlias 机构表别名，例如：dc.createAlias("office", "office");
	 * @param userAlias 用户表别名，传递空，忽略此参数
	 * @return ql查询字符串
	 */
	protected static String dataScopeFilterString(User user, String officeAlias, String userAlias) {
		Junction junction = dataScopeFilter(user, officeAlias, userAlias);
		Iterator<Criterion> it = junction.conditions().iterator();
		StringBuilder ql = new StringBuilder();
		ql.append(" and (");
		if (it.hasNext()){
			ql.append(it.next());
		}
		String[] strField = {".parentIds like ", ".type="}; // 需要给字段增加“单引号”的字段。
		while (it.hasNext()) {
			ql.append(" or (");
			String s = it.next().toString();
			for(String field : strField){
				s = s.replaceAll(field + "(\\w.*)", field + "'$1'");
			}
			ql.append(s).append(")");
		}
		ql.append(")");
		return ql.toString();
	}

	protected List<Long> getIdList(String ids) {
		List<Long> idList = Lists.newArrayList();
		if(StringUtils.isNotBlank(ids)) {
			ids = ids.trim().replace("　", ",").replace(" ", ",").replace("，", ",");
			String[] arrId = ids.split(",");
			for(String id:arrId) {
				if(id.matches("\\d*")) {
					idList.add(Long.valueOf(id));
				}
			}
		}
		return idList;
	}
}
