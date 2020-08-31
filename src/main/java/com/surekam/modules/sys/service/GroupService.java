package com.surekam.modules.sys.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.modules.sys.dao.GroupDao;
import com.surekam.modules.sys.dao.GroupUserDao;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.entity.Group;
import com.surekam.modules.sys.entity.GroupUser;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.utils.UserUtils;

/**
 * 项目组Service
 * @author liuyi
 * @version 2018-03-23
 */
@Component
@Transactional(readOnly = true)
public class GroupService extends BaseService {

	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private GroupUserDao groupUserDao;
	
	
	@Autowired
	private UserDao userDao;
	
	public Group get(String id) {
		return groupDao.get(id);
	}
	
	public Page<Group> find(Page<Group> page, Group group) {
		DetachedCriteria dc = groupDao.createDetachedCriteria();
		
			dc.add(Restrictions.eq("parent.groupId",group.getGroupId()));
			dc.add(Restrictions.eq(Group.FIELD_DEL_FLAG, Group.DEL_FLAG_NORMAL));
			//dc.add(Restrictions.ne(Group.GXBZ, Group.GXBZ_D));
			return groupDao.find(page, dc);
		
	}
	
	/*@Transactional(readOnly = false)
	public void save(Group group) {
		groupDao.save(group);
	}*/
	
	@Transactional(readOnly = false)
	public void save(Group group) {
		group.setParent(this.get(group.getParent().getGroupId()));
		//String oldParentIds = group.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		group.setParentIds(group.getParent().getParentIds()+group.getParent().getGroupId()+",");
		User user = UserUtils.getUser();
		if(StringUtils.isBlank(group.getGroupId())){
			group.setCreateBy(user);
		}
		group.setUpdateBy(user);
		groupDao.clear();
		groupDao.save(group);
	}
	
	
	@Transactional(readOnly = false)
	public void updateGroupMaster(GroupUser gorupUser) {
		groupUserDao.updateGroupMaster(gorupUser.getMaster(),gorupUser.getGroupId(),gorupUser.getUserId());
	}
	
	/*@Transactional(readOnly = false)
	public void save(Group group) {
		Group childGroup = new Group();
		childGroup.setName(group.getName());
		group = this.get(group.getGroupId());
		childGroup.setParent(group);
		
		String oldParentIds = group.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		childGroup.setParentIds(oldParentIds+group.getGroupId()+",");
		childGroup.setDelFlag("0");
		
		groupDao.clear();
		groupDao.save(childGroup);
		// 更新子节点 parentIds
		List<Group> list = groupDao.findByParentIdsLike("%,"+group.getGroupId()+",%");
		for (Group e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, group.getParentIds()));
		}
		groupDao.save(list);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}*/
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		//groupDao.deleteByGroupId(id);
		groupDao.deleteById(id, "%,"+id+",%");
	}
	
	@Transactional(readOnly = false)
	public void saveGroupUser(GroupUser groupUser) {
		//groupDao.deleteByGroupId(id);
		groupUserDao.save(groupUser);
	}
	
	@Transactional(readOnly = false)
	public void deleteGroupUser(GroupUser groupUser) {
		//groupDao.deleteByGroupId(id);
		groupUserDao.deleteDataById(groupUser.getGroupId(),groupUser.getUserId());
	}
	
	
	
	//查询过滤当前用户的组数据，如果当前用户用户是组管理员，返回该组下的所有组员数据
	public void queryGroupDataWithFilter(String groupId,List col){
		List<String> groupIdList = Lists.newArrayList();
		User currentUser = UserUtils.getUser();
		//当前组
		List<Group> currentGroup = getGroupById(currentUser,groupId);
		if(currentGroup!=null && currentGroup.size()>0){
			Group group = new Group();
			group = currentGroup.get(0);
			if(group.getMaster()){
				//获取当前组id和其子组id的集合
				groupIdList.add(group.getGroupId());
				findChildGroupId(group.getGroupId(),groupIdList);
			}
		}
		//调用过滤器
		selectData(col,"groupId",groupIdList);
	}
	
	
	/**
	 * 递归寻找子组
	 * */
	public List<String> findChildGroupId(String groupId,List<String> childIdList){
			List<Group> list = Lists.newArrayList();
			list = groupDao.findByParentId(groupId);
			if(list!=null || list.size()>0){
				List<String> childId = Lists.newArrayList();
				for (Group c : list) {
					childId.add(c.getGroupId());
					childIdList.addAll(childId);
					findChildGroupId(c.getGroupId(),childIdList);
				}
				return childIdList;
				
			}else{
				return childIdList;
			}
		
	}
	
	
	/**
	 * 根据groupId获取GroupUser对象
	 * @param id
	 * @return
	 */
	public GroupUser findObjByGroupId(String id) {
		if(id==null){
			return new GroupUser();
		}
		List<GroupUser> list = Lists.newArrayList();
		list = groupUserDao.findListByGroupId(id);
		if(list!=null && list.size() >0){
			return list.get(0);
		}
		return new GroupUser();
	}
	
	/**
	 * 获取用户列表
	 * @param id
	 * @return
	 * @throws Exception 
	 * @throws IllegalAccessException 
	 */
	public List<User> findListByGroupId(String groupId,User user) {
		//排序后的List,设置了版主的用户排在最前面
		List<User> allUserSortList = getAllUserListByCondition(user);
		if(StringUtils.isNotBlank(groupId)){
			//根据组id获取数据
			List<GroupUser>  list =groupUserDao.findListByGroupId(groupId);
			//组员list
			List<User> groupUserList =  Lists.newArrayList();
			//除组员以外的用户list
			List<User> userWithoutPlateAdminList =  Lists.newArrayList();
			//所有用户list
			List<User> allUserList = Lists.newArrayList();
			allUserList = getAllUserListByCondition(user);
			
			//已经设置了组员的情况
			if(list!=null && list.size()>0){
				String[] idArray = getUserArray(list);
				if(idArray!=null && idArray.length>0  ){
					//String[] idArray = userIdList.toArray(new String[userIdList.size()]);
					groupUserList = getGroupUserList(idArray,user,groupId);
					userWithoutPlateAdminList =getUserWithoutGroup(groupId,user);
					groupUserList.addAll(userWithoutPlateAdminList);
					allUserSortList = groupUserList;
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
	
	/**获取组员用户list
	 * @param String[] idArray
	 * @return List<User>
	 */
	public List<User> getGroupUserList( String[] idArray,User user,String groupId){
		//组员用户list
		List<User> groupUserList =  Lists.newArrayList();
		groupUserList = userDao.findUserByIds(idArray,user);
		if(groupUserList!=null && groupUserList.size()>0){
			for(User obj:groupUserList){
				obj.setMember(true);
			}
			List<GroupUser> masterList = Lists.newArrayList();
			masterList = getMaterList(groupId);
			if(masterList!=null && masterList.size()>0){
				for(GroupUser gu:masterList){
					for(User u:groupUserList){
						if(u.getId().equals(gu.getUserId())){
							u.setMaster(true);
						}
					}
				}
			}
		}
		
		return groupUserList;
	}
	
	//获取组长用户list
	public List<GroupUser> getMaterList( String groupId){
		List<GroupUser> list = Lists.newArrayList();
		list = groupUserDao.findMasterByGroupId(groupId, "1");
		return list;
		
	}
	
	
	/**
	 * 获取某个组除组员以外的用户
	 * @param id
	 * @return
	 */
	public List<User> getUserWithoutGroup(String groupId,User user){
		
		//所有用户list
		List<User> allUserList =  Lists.newArrayList();
		allUserList = getAllUserListByCondition(user);
		if(groupId == null){
			return allUserList;
		}
		//除组员以外的用户list
		List<User> userWithoutPlateAdminList =  Lists.newArrayList();
		//组员list
		List<User> categoryUserList =  Lists.newArrayList();
		//根据组id获取数据
		List<GroupUser>  list =groupUserDao.findListByGroupId(groupId);
		
		if(list!=null && list.size()>0){
			//userIdList用于存放userId
			//List<String> userIdList =  Lists.newArrayList();
			String[] idArray = getUserArray(list);
			if(idArray!=null && idArray.length>0){
				//String[] idArray = userIdList.toArray(new String[userIdList.size()]);
				categoryUserList = getGroupUserList(idArray,user,groupId);
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
	 * 获取GroupUser中的userId,以数组返回
	 * @param list
	 * @return
	 */
	public String[] getUserArray(List<GroupUser> list){
		
		String uersIdStr="";
		String[] idArray=null;
		if(list!=null && list.size()>0){
			for(GroupUser c : list){
				if(uersIdStr.equals("") && StringUtils.isNotBlank(c.getUserId())){
					uersIdStr = c.getUserId();
				}else{
					uersIdStr+=","+c.getUserId();
				}
			}
		}
		if(StringUtils.isNotBlank(uersIdStr)){
			idArray = uersIdStr.split(",");
		}
		return idArray;
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
	
	/**根据条件获取用户list
	 * @return List<User>
	 */
	public List<User> getAllUserListByCondition(User user){
		//所有用户list
		List<User> allUserList =  Lists.newArrayList();
		allUserList = userDao.findUserListByCondition(user);
		return allUserList;
	}
	
	/**
	 * 获取所有组数据
	 * @param user
	 * @param groupId
	 * @return
	 */
	public List<Group> getAllGroupList(){
		List<Group> groupList = groupDao.findAllList();
		return groupList;
	}
	
	
	/**
	 * 当前用户的组数据
	 * @param user
	 * @param groupId
	 * @return
	 */
	public List<Group> getGroupById(User user,String groupId){
		List<Group> list = Lists.newArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.*,t1.master FROM sys_group t,sys_group_user t1 WHERE t.GROUP_ID = t1.group_id AND t1.user_id =:userId AND t1.group_id =:groupId");
		Parameter param = new Parameter();
		param.put("userId", user.getId());
		param.put("groupId", groupId);
		list = groupDao.findBySql(sql.toString(),param,List.class);
		return list;
	}
	
	
	public List<Group> findZtreeGroup(User user, String groupId) {
		DetachedCriteria dc = groupDao.createDetachedCriteria();		
		if(StringUtils.isNotBlank(groupId)){
			dc.createAlias("parent", "group");
			dc.add(Restrictions.eq("parent.groupId",groupId));
		}else{
			dc.add(Restrictions.eq("groupId","1"));
		}
		//dc.add(dataScopeFilter(user, dc.getAlias(), ""));
		dc.add(Restrictions.eq(Group.FIELD_DEL_FLAG, Group.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("groupId"));
		groupDao.clear();
		return groupDao.find(dc);
	}
}
