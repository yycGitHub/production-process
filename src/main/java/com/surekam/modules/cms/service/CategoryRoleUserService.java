package com.surekam.modules.cms.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.cms.dao.CategoryDao;
import com.surekam.modules.cms.dao.CategoryRoleUserDao;
import com.surekam.modules.cms.entity.Category;
import com.surekam.modules.cms.entity.CategoryRoleUser;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.dao.UserRoleDao;
import com.surekam.modules.sys.entity.Role;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.UserRole;
import com.surekam.modules.sys.utils.UserUtils;

/**
 * 栏目管理员Service
 * @author liuyi
 * @version 2018-01-29
 */
@Component
@Transactional(readOnly = true)
public class CategoryRoleUserService extends BaseService {

	@Autowired
	private CategoryRoleUserDao categoryRoleUserDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private CategoryDao categoryDao;
	
	//private static  List<Integer> childIdList=Lists.newArrayList();
	
	public CategoryRoleUser get(String id) {
		return categoryRoleUserDao.get(id);
	}
	
	public Page<CategoryRoleUser> find(Page<CategoryRoleUser> page, CategoryRoleUser categoryRoleUser) {
		DetachedCriteria dc = categoryRoleUserDao.createDetachedCriteria();
		//dc.add(Restrictions.ne(CategoryRoleUser.GXBZ, CategoryRoleUser.GXBZ_D));
		return categoryRoleUserDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(CategoryRoleUser categoryRoleUser) {
		categoryRoleUserDao.save(categoryRoleUser);
	}
	
	/**
	 * 更新栏目权限
	 * @param categoryRoleUser
	 * @param oldCategoryRoleUser
	 */
	@Transactional(readOnly = false)
	public void updateRole(String roleStr,CategoryRoleUser oldCategoryRoleUser){
		//如果栏目已绑定的角色发生了变化
		if(!roleStr.equals(oldCategoryRoleUser.getRoleId())){
			String userStr  = oldCategoryRoleUser.getUserId();
			String oldroleStr  = oldCategoryRoleUser.getRoleId();
			List<String> oldUserList = Lists.newArrayList();
			List<String> oldRoleList = Lists.newArrayList();
			List<String> newRoleList = Lists.newArrayList();
			oldUserList = StrToList(userStr);
			oldRoleList = StrToList(oldroleStr);
			newRoleList = StrToList(roleStr);
			
			deleteOldUserRole(oldUserList,oldRoleList);
			addNewUserRole(oldUserList,newRoleList);
			
		}
		//更新栏目权限
		oldCategoryRoleUser.setRoleId(roleStr);
		categoryRoleUserDao.updateRole(oldCategoryRoleUser);
	}
	
	@Transactional(readOnly = false)
	public void deleteOldUserRole(List<String> oldUserList,List<String> oldRoleList){
		if(oldUserList!=null && oldUserList.size()>0){
			for (String userId : oldUserList) {
				if(oldRoleList!=null && oldRoleList.size()>0){
					for (String roleId : oldRoleList) {
						//删除原来的用户栏目角色
						userRoleDao.deleteById(userId, roleId);
					}
				}
			}
		}
		
	}
	
	@Transactional(readOnly = false)
	public void addNewUserRole(List<String> oldUserList,List<String> newRoleList){
		if(oldUserList!=null && oldUserList.size()>0){
			for (String userId : oldUserList) {
				if(newRoleList!=null && newRoleList.size()>0){
					for (String roleId : newRoleList) {
						UserRole userRole = new UserRole();
						userRole.setUserId(userId);
						userRole.setRoleId(roleId);
						//新增新的用户栏目角色
						userRoleDao.insert(userRole);
					}
				}
			}
		}
		
	}
	
	
	/**
	 * String 转为List
	 * @param str
	 * @return
	 */
	public List<String> StrToList(String str){
		String [] array = null;
		List<String> resList = Lists.newArrayList();
		if(StringUtils.isNotBlank(str)){
			array = str.split(",");
		}
		if(array!=null && array.length >0){
			for (String s : array) {
				resList.add(s);
			}
		}
		return resList;
	}
	
	@Transactional(readOnly = false)
	public void deleteByCategoryId(Integer categoryId){
		categoryRoleUserDao.deleteBycategoryId(categoryId);
	}
	
	
	/**
	 * 更新版主
	 * @param categoryRoleUser
	 * @param add
	 * @param userId
	 */
	@Transactional(readOnly = false)
	public void updatePlate(CategoryRoleUser categoryRoleUser){
		categoryRoleUserDao.updatePlate(categoryRoleUser);
		/*if("1".equals(add)){
			List<String> rolelist = compareRoles(categoryRoleUser.getCategoryId(), userId);
			if(rolelist!=null && rolelist.size() > 0){
				for (String roleId : rolelist) {
					UserRole obj = new UserRole();
					obj.setUserId(userId);
					obj.setRoleId(roleId);
					userRoleDao.insert(obj);
				}
			}
			
		}else{
			String roleIdStr = categoryRoleUser.getRoleId();
			if(StringUtils.isNotBlank(roleIdStr)){
				String [] roleArr = roleIdStr.split(",");
				for (String roleId : roleArr) {
					userRoleDao.deleteById(userId, roleId);
				}
			}
		}*/
	}
	
	/**
	 * 根据CategoryId获取CategoryRoleUser对象
	 * @param id
	 * @return
	 */
	public CategoryRoleUser findObjByCategoryId(Integer id) {
		if(id==null){
			return new CategoryRoleUser();
		}
		List<CategoryRoleUser> list = Lists.newArrayList();
		list = categoryRoleUserDao.findListByCategoryId(id);
		if(list!=null && list.size() >0){
			return list.get(0);
		}
		return new CategoryRoleUser();
	}
	
	/**根据条件获取用户list
	 * @return List<User>
	 */
	public List<User> getAllUserListByCondition(User user){
		//所有用户list
		List<User> allUserList =  Lists.newArrayList();
		allUserList = userDao.findUserListByCondition(user);
		return allUserList;
	}
	
	
	/**获取版主用户list
	 * @param String[] idArray
	 * @return List<User>
	 */
	public List<User> getCategoryUserList( String[] idArray,User user){
		//版主用户list
		List<User> categoryUserList =  Lists.newArrayList();
		categoryUserList = userDao.findUserByIds(idArray,user);
		if(categoryUserList!=null && categoryUserList.size()>0){
			for(User obj:categoryUserList){
				obj.setPlateAdmin(true);
			}
		}
		return categoryUserList;
	}
	
	/**
	 * 获取CategoryRoleUser中的userId,以数组返回
	 * @param list
	 * @return
	 */
	public String[] getUserArray(List<CategoryRoleUser> list){
		
		String uersIdStr="";
		String[] idArray=null;
		if(list!=null && list.size()>0){
			for(CategoryRoleUser c : list){
				if(uersIdStr.equals("") && StringUtils.isNotBlank(c.getUserId())){
					uersIdStr = c.getUserId();
					idArray = uersIdStr.split(",");
					break;
				}
			}
		}
		return idArray;
	}
	
	
	/**
	 * 获取某个栏目除版主以外的用户
	 * @param id
	 * @return
	 */
	public List<User> getUserWithoutPlateAdmin(Integer id,User user){
		
		//所有用户list
		List<User> allUserList =  Lists.newArrayList();
		allUserList = getAllUserListByCondition(user);
		if(id == null){
			return allUserList;
		}
		//除版主以外的用户list
		List<User> userWithoutPlateAdminList =  Lists.newArrayList();
		//版主list
		List<User> categoryUserList =  Lists.newArrayList();
		//根据栏目id获取数据
		List<CategoryRoleUser>  list =categoryRoleUserDao.findListByCategoryId(id);
		if(list!=null && list.size()>0){
			//userIdList用于存放userId
			//List<String> userIdList =  Lists.newArrayList();
			String[] idArray = getUserArray(list);
			if(idArray!=null && idArray.length>0){
				//String[] idArray = userIdList.toArray(new String[userIdList.size()]);
				categoryUserList = getCategoryUserList(idArray,user);
				final CopyOnWriteArrayList<User> cowList = new CopyOnWriteArrayList<User>(allUserList);
				for(User all:cowList){
					for( User category :categoryUserList){
						if(all.getId().equals(category.getId())){
							cowList.remove(all);
						}
					}
				}
				userWithoutPlateAdminList = cowList;
				return userWithoutPlateAdminList;
			}else{
				return allUserList;
			}
			
		}else{
			return allUserList;
		}
		
	}
	
	
	/**
	 * 获取用户列表，其中被设置为某个栏目版主的用户将排列在最前面
	 * @param id
	 * @return
	 */
	public List<User> findListByCategoryId(Integer id,User user){
		//排序后的List,设置了版主的用户排在最前面
		List<User> allUserSortList = getAllUserListByCondition(user);
		if(id!=null){
			//根据栏目id获取数据
			List<CategoryRoleUser>  list =categoryRoleUserDao.findListByCategoryId(id);
			//版主list
			List<User> categoryUserList =  Lists.newArrayList();
			//除版主以外的用户list
			List<User> userWithoutPlateAdminList =  Lists.newArrayList();
			//所有用户list
			List<User> allUserList = Lists.newArrayList();
			allUserList = getAllUserListByCondition(user);
			
			//已经设置了版主的情况
			if(list!=null && list.size()>0){
				String[] idArray = getUserArray(list);
				if(idArray!=null && idArray.length>0  ){
					//String[] idArray = userIdList.toArray(new String[userIdList.size()]);
					categoryUserList = getCategoryUserList(idArray,user);
					userWithoutPlateAdminList =getUserWithoutPlateAdmin(id,user);
					categoryUserList.addAll(userWithoutPlateAdminList);
					allUserSortList = categoryUserList;
					return allUserSortList;
				}else{//设置版主表中有数据，但还未设置版主
					allUserSortList = allUserList;
				}
			}else{//设置版主表中没有数据
				allUserSortList = allUserList;
			}
		}else{
			return allUserSortList;
		}
		return allUserSortList;
	}
	
	
	public Page<User> setPageList(Page<User> page,HttpServletRequest request, HttpServletResponse response,List<User> list){
		if(list==null || list.size()==0){
			return page;
		}
		String no = request.getParameter("pageNo");
		String size = request.getParameter("pageSize");
		if(StringUtils.isBlank(no)){
			no="1";
		}
		if(StringUtils.isBlank(size)){
			size=Global.getConfig("page.pageSize");
		}
		page.setPageNo(Integer.parseInt(no));
		page.setPageSize(Integer.parseInt(size));
		if (StringUtils.isNumeric(size) && StringUtils.isNumeric(size)){
			Integer max = Integer.parseInt(no)*Integer.parseInt(size);
			Integer min = max - Integer.parseInt(size) + 1;
			if(max > list.size()){
				max = list.size();
			}
			
			List<User> pageUserList = Lists.newArrayList();
			for(int i = 0; i< list.size();i++){
				if(i+1 >= min && i+1 <= max){
					pageUserList.add(list.get(i));
				}
			}
			page.setList(pageUserList);
			page.setCount(list.size());
		}
		return page;
	}
	
	
	/**
	 * 栏目角色和用户角色相比较，筛选出不存在用户角色表中的栏目角色
	 * @param categoryId
	 * @param userId
	 */
	public List<String> compareRoles(Integer categoryId,String userId){
		List<String> categoryRoleList = findCategoryRoleBycategoryId(categoryId);
		List<String> userRoleList = findUserRoleByUserId(userId);
		List<String> resList = Lists.newArrayList();
		if(categoryRoleList!=null && categoryRoleList.size()>0){
			for (String c : categoryRoleList) {
				int i =0;
				for (String u : userRoleList) {
					if(c.equals(u)){
						i++;
					}
				}
				//栏目角色不存在用户角色表之中
				if(i==0){
					resList.add(c);
				}
			}
		}
		return resList;
	}
	
	
	
	/**
	 * 根据栏目Id获取栏目角色
	 * @param id
	 * @return
	 */
	public List<String> findCategoryRoleBycategoryId(Integer categoryId) {
		if(categoryId==null){
			return null;
		}
		List<String> roleList = Lists.newArrayList();
		CategoryRoleUser category = new CategoryRoleUser();
		List<CategoryRoleUser> list = Lists.newArrayList();
		list = categoryRoleUserDao.findListByCategoryId(categoryId);
		if(list!=null && list.size() >0){
			category = list.get(0);
			if(category!=null && StringUtils.isNotBlank(category.getRoleId())){
				String[] idArr = category.getRoleId().split(",");
				for (String s : idArr) {
					roleList.add(s);
				}
			}
		}
		return roleList;
		
	}
	
	/**
	 * 根据userId获取UserRole已有角色
	 * @param userId
	 * @return
	 */
	public List<String> findUserRoleByUserId(String userId){
		List<String> exUserRoleList = Lists.newArrayList();
		exUserRoleList =  userRoleDao.findByUserId(userId);
		/*List<String> roleList = Lists.newArrayList();
		for (UserRole userRole : exUserRoleList) {
			roleList.add(userRole.getRoleId());
		}*/
		return exUserRoleList;
	}
	
	
	/**
	 * 获取全部CategoryRoleUserList
	 * @return
	 */
	public List<CategoryRoleUser> findAllCategoryRoleUser(){
		List<CategoryRoleUser> allList = Lists.newArrayList();
		allList = categoryRoleUserDao.findAllList();
		return allList;
	}
	
	/**根据角色获取板块idList*/
	public List<Integer> getCategoryListByRole(Role role){
		if(role == null){
			return null;
		}
		List<Integer> resList = Lists.newArrayList();
		List<CategoryRoleUser> allList = Lists.newArrayList(); 
		allList = findAllCategoryRoleUser();
		List<Integer> categoryIdList =  Lists.newArrayList(); 
		if(allList!=null && allList.size() > 0){
			for (CategoryRoleUser categoryRoleUser : allList) {
				String roleIdStr = categoryRoleUser.getRoleId();
				Integer categoryId =  categoryRoleUser.getCategoryId();
				if(StringUtils.isNotBlank(roleIdStr)){
					String[] roleIdArray = roleIdStr.split(",");
					for (String roleId : roleIdArray) {
						if(roleId.equals(role.getId())){
							categoryIdList.add(categoryId);
						}
					}
				}
			}
		}
		List<Integer> child = Lists.newArrayList();
		List<Integer> childIdList = Lists.newArrayList();
		for(Integer id:categoryIdList){
			child = findChildCategoryId(id,childIdList);
			if(child!=null && child.size()>0){
				resList.addAll(child);
			}
		}
		resList.addAll(categoryIdList);
		
		return resList;
	}
	
	/**
	 * 递归寻找子栏目
	 * */
	public List<Integer> findChildCategoryId(Integer categoryId,List<Integer> childIdList){
			List<Category> list = Lists.newArrayList();
			list = categoryDao.findByParentId(categoryId);
			if(list!=null || list.size()>0){
				List<Integer> childId = Lists.newArrayList();
				for (Category c : list) {
					childId.add(c.getId());
					childIdList.addAll(childId);
					findChildCategoryId(c.getId(),childIdList);
				}
				return childIdList;
				
			}else{
				return childIdList;
			}
		
	}
	
	/**
	 * 获取当前用户所管理的板块Id
	 * */
	public List<Integer> findCategoryIdByUserId(){
		User user = UserUtils.getUser();
		List<Integer> resList = Lists.newArrayList();
		List<CategoryRoleUser> allList = Lists.newArrayList(); 
		allList = findAllCategoryRoleUser();
		List<Integer> categoryIdList =  Lists.newArrayList(); 
		if(allList!=null && allList.size() > 0){
			for (CategoryRoleUser categoryRoleUser : allList) {
				String userIdStr = categoryRoleUser.getUserId();
				Integer categoryId =  categoryRoleUser.getCategoryId();
				if(StringUtils.isNotBlank(userIdStr)){
					String[] UserArray = userIdStr.split(",");
					for (String userid : UserArray) {
						if(userid.equals(user.getId())){
							categoryIdList.add(categoryId);
						}
					}
				}
			}
		}
		
		List<Integer> child = Lists.newArrayList();
		List<Integer> childIdList = Lists.newArrayList();
		for(Integer id:categoryIdList){
			child = findChildCategoryId(id,childIdList);
			if(child!=null && child.size()>0){
				resList.addAll(child);
			}
		}
		resList.addAll(categoryIdList);
		
		return resList;
		
	}
	
}
