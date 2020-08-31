package com.surekam.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.sys.entity.Group;
import com.surekam.modules.sys.entity.GroupUser;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.GroupService;
import com.surekam.modules.sys.utils.UserUtils;

/**
 * 项目组Controller
 * @author liuyi
 * @version 2018-03-23
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/group")
public class GroupController extends BaseController {

	@Autowired
	private GroupService groupService;
	
	@ModelAttribute
	public Group get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return groupService.get(id);
		}else{
			return new Group();
		}
	}
	
	@RequiresPermissions("sys:group:view")
	@RequestMapping(value = {"list", ""})
	public String list(Group group, HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("group", group);
        Page<Group> page = groupService.find(new Page<Group>(request, response), group); 
        model.addAttribute("page", page);
		return "modules/" + "sys/common/groupList";
	}

	@RequiresPermissions("sys:group:view")
	@RequestMapping(value = "form")
	public String form(Group group, Model model,String rebackId) {
		if (group.getParent() == null || group.getParent().getGroupId() == null) {
			group.setParent(groupService.get("1"));
		}
		group.setParent(groupService.get(group.getParent().getGroupId()));
		model.addAttribute("group", group);
		model.addAttribute("rebackId", rebackId);
		return "modules/" + "sys/common/groupForm";
	}
	
	
	 /**
     * 跳转到添加组成员管理
     * 
     * */
    @RequiresPermissions("sys:group:edit")
	@RequestMapping(value = "memberManage")
	public String toMemberManage(String groupId,User user, Model model,HttpServletRequest request, HttpServletResponse response) {
    	//GroupUser groupUser =new GroupUser();
    	//groupUser = groupService.findObjByGroupId(id);
    	List<User>  list = groupService.findListByGroupId(groupId,user);
    	//设置分页
    	Page<User> page = groupService.setPageList(new Page<User>(request, response),request,response,list);
    	
        model.addAttribute("page", page);
    	model.addAttribute("user", user);
    	model.addAttribute("groupId", groupId);
    	//model.addAttribute("id", id);
    	//model.addAttribute("groupUser", groupUser);
		return "modules/" + "sys/common/memberManage";
	}
	
	@RequiresPermissions("sys:group:view")
	@RequestMapping(value = "information")
	public String information(Group group, Model model) {
		model.addAttribute("group", group);
		return "modules/" + "sys/common/groupInformation";
	}
	
	
	
	
	/**
     * 添加/移除组员
     * 
     * */
    @RequiresPermissions("sys:group:edit")
	@RequestMapping(value = "addMaster")
	public String addMaster(String userId,String add,String groupId,User user, Model model,HttpServletRequest request, HttpServletResponse response) {
    	GroupUser gorupUser =new GroupUser();
    	gorupUser.setGroupId(groupId);
    	gorupUser.setUserId(userId);
    	
    	//添加组长
    	if("1".equals(add)){
    		gorupUser.setMaster("1");
        	groupService.updateGroupMaster(gorupUser);
    	}else{//移除组长
    		gorupUser.setMaster("0");
    		groupService.updateGroupMaster(gorupUser);
    	}
    	
    	//根据groupId查询
    	List<User>  list = groupService.findListByGroupId(groupId,user);
    	//设置分页
    	Page<User> page = groupService.setPageList(new Page<User>(request, response),request,response,list);
    	model.addAttribute("page", page);
     	model.addAttribute("user", user);
     	model.addAttribute("groupId", groupId);
    	//model.addAttribute("categoryRoleUser", category);
    	//model.addAttribute("allRoles", systemService.findAllRole());
    	if("1".equals(add)){
    		addMessage(model, "添加组员成功！");
    	}else{
    		addMessage(model, "移除组员成功！");
    	}
    	return "modules/" + "sys/common/memberManage";
	}
	
	/**
     * 添加/移除组员
     * 
     * */
    @RequiresPermissions("sys:group:edit")
	@RequestMapping(value = "addMember")
	public String addMember(String userId,String add,String groupId,User user, Model model,HttpServletRequest request, HttpServletResponse response) {
    	GroupUser gorupUser =new GroupUser();
    	gorupUser.setGroupId(groupId);
    	gorupUser.setUserId(userId);
    	gorupUser.setMaster("0");
    	//添加组员
    	if("1".equals(add)){
        	groupService.saveGroupUser(gorupUser);
    	}else{//移除组员
    		groupService.deleteGroupUser(gorupUser);
    	}
    	
    	//根据groupId查询
    	List<User>  list = groupService.findListByGroupId(groupId,user);
    	//设置分页
    	Page<User> page = groupService.setPageList(new Page<User>(request, response),request,response,list);
    	model.addAttribute("page", page);
     	model.addAttribute("user", user);
     	model.addAttribute("groupId", groupId);
    	//model.addAttribute("categoryRoleUser", category);
    	//model.addAttribute("allRoles", systemService.findAllRole());
    	if("1".equals(add)){
    		addMessage(model, "添加组员成功！");
    	}else{
    		addMessage(model, "移除组员成功！");
    	}
    	return "modules/" + "sys/common/memberManage";
	}

	
	
	@RequiresPermissions("sys:group:edit")
	@RequestMapping(value = "save")
	public String save(Group group, Model model, RedirectAttributes redirectAttributes,String rebackId) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/group?id="+rebackId;
		}
		
		if (!beanValidator(model, group)) {
			return form(group, model,rebackId);
		}
		groupService.save(group);
		addMessage(redirectAttributes, "保存项目组'"+group.getName()+"'成功");
		return "redirect:"+Global.getAdminPath()+"/sys/group?id="+rebackId;
		
	}
	
	
	@RequiresPermissions("sys:group:edit")
	@RequestMapping("delete")
	public String delete(String groupId, RedirectAttributes redirectAttributes,String rebackId) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/group?id="+rebackId;
		}
		if (Group.isRoot(groupId)) {
			addMessage(redirectAttributes, "删除机构失败, 不允许删除顶级机构或编号空");
		} else {
			groupService.delete(groupId);
			addMessage(redirectAttributes, "删除机构成功");
		}
		return "redirect:" + Global.getAdminPath() + "/sys/group?id="+rebackId;
	}
	
	
	/*@RequiresPermissions("sys:group:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		groupService.delete(id);
		addMessage(redirectAttributes, "删除项目组成功");
		return "redirect:"+Global.getAdminPath()+"/sys/group/?repage";
	}*/
	
	
	@RequiresPermissions("sys:group:view")
	@RequestMapping({"index"})
	public String index(Model model) {
		return "modules/sys/common/groupIndex";
	}
	
	@RequiresPermissions("sys:group:view")
	@RequestMapping({"groupTree"})
	public String officeTree(Model model) {
		model.addAttribute("url", "");
		return "modules/sys/common/groupTree";
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping("ztreeDynamicData")
	public List<Map<String, Object>> ztreeDynamicData(HttpServletResponse response,
			@RequestParam(required = false) String itemId) {		
		response.setContentType("application/json; charset=UTF-8");
		User user = UserUtils.getUser();
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Group> list = groupService.findZtreeGroup(user,itemId);
		for (int i=0; i<list.size(); i++){
			Group e = list.get(i);					
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getGroupId());
			map.put("pId", e.getParent()!=null?e.getParent().getGroupId():0);
			map.put("name", e.getName());
			if(e.getChildList().size()>0){
				map.put("isParent",true);
			}
			//map.put("isParent",true);
			mapList.add(map);
		}
		return mapList;
	}
	
	
	@RequiresUser
	@ResponseBody
	@RequestMapping("treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response) {
		
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Group> list = groupService.getAllGroupList();
		for (int i=0; i<list.size(); i++){
			Group e = list.get(i);		
				
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getGroupId());
				map.put("pId", e.getParent() != null ? e.getParent().getGroupId() : 0);
				map.put("name", e.getName());
				mapList.add(map);
			
		}
		return mapList;
	}


}
