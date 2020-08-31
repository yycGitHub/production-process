/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.modules.sys.web;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.surekam.common.beanvalidator.BeanValidators;
import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.DateUtils;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.utils.excel.ExportExcel;
import com.surekam.common.utils.excel.ImportExcel;
import com.surekam.common.web.BaseController;
import com.surekam.modules.sys.entity.Menu;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.Role;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.utils.UserUtils;

/**
 * 用户Controller
 * @author sureserve
 * @version 2013-5-31
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getUser(id);
		}else{
			return new User();
		}
	}
	
	@RequestMapping("upUserTable")
	public String upUserTable(User user, String selectIds, Model model,HttpServletRequest request, HttpServletResponse response) {
		Page<User> page = systemService.findUser(new Page<User>(request, response),user);
		model.addAttribute("user", user);
		model.addAttribute("selectIds", selectIds);
		model.addAttribute("page", page);
		return "modules/sys/user/upUserTable";
	}
	
	@RequiresPermissions("sys:user:view")
	@RequestMapping({"list", ""})
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<User> page = systemService.findUser(new Page<User>(request, response), user); 
        model.addAttribute("page", page);
		return "modules/sys/user/userList";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping("form")
	public String form(User user, Model model) {
		if (user.getCompany() == null || user.getCompany().getId() == null) {
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice() == null || user.getOffice().getId() == null) {
			user.setOffice(UserUtils.getUser().getOffice());
		}

		// 判断显示的用户是否在授权范围内
		String officeId = user.getOffice().getId();
		User currentUser = UserUtils.getUser();
		if (!currentUser.isAdmin()) {
			String dataScope = systemService.getDataScope(currentUser);
			if (dataScope.indexOf("office.id=") != -1) {
				String AuthorizedOfficeId = dataScope.substring(dataScope.indexOf("office.id=") + 10, dataScope.indexOf(" or"));
				if (!AuthorizedOfficeId.equalsIgnoreCase(officeId)) {
					return "error/403";
				}
			}
		}

		model.addAttribute("user", user);
		model.addAttribute("allRoles", systemService.findAllRole());
		return "modules/sys/user/userForm";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping("save")
	public String save(User user, String oldLoginName, String newPassword, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
		}
		
		user.setCompany(new Office(request.getParameter("company.id")));
		user.setOffice(new Office(request.getParameter("office.id")));
		
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(newPassword)) {
			user.setPassword(SystemService.entryptPassword(newPassword));
		}
		if (!beanValidator(model, user)) {
			return form(user, model);
		}
		if (!"true".equals(checkLoginName(oldLoginName, user.getLoginName()))) {
			addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
			return form(user, model);
		}
		
		
		// 角色数据有效性验证，过滤不在授权内的角色
		List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = user.getRoleIdList();
		for (Role r : systemService.findAllRole()) {
			if (roleIdList.contains(r.getId())) {
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		
		// 保存用户信息
		systemService.saveUser(user);
		
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())) {
			UserUtils.getCacheMap().clear();
		}
		
		addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
	}
	
	/**
	 * 删除用户
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("delete")
	public String delete(String[] ids, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
		}		
		try {
			String message = systemService.deleteUsers(ids);
			addMessage(redirectAttributes,message);
		} catch (Exception e) {
			addMessage(redirectAttributes,e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
	}
	
	/**
	 * 恢复用户
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("rebackUser")
	public String rebackUser(String[] ids, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
		}		
		try {
			systemService.rebackUsers(ids);
			addMessage(redirectAttributes,"恢复用户成功");
		} catch (Exception e) {
			addMessage(redirectAttributes,e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
	}
	
	@RequiresPermissions("sys:user:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx"; 
    		Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user); 
    		new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
    }

	@RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
		}
		
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<User> list = ei.getDataList(User.class);
			for (User user : list){
				try{
					if ("true".equals(checkLoginName("", user.getLoginName()))){
						user.setPassword(SystemService.entryptPassword("123456"));
						BeanValidators.validateWithException(validator, user);
						systemService.saveUser(user);
						successNum++;
					}else{
						failureMsg.append("<br/>登录名 " + user.getLoginName() + " 已存在; ");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：" + ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum+" 条用户" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
    }
	
	@RequiresPermissions("sys:user:view")
    @RequestMapping("import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户数据导入模板.xlsx";
			List<User> list = Lists.newArrayList();
			list.add(UserUtils.getUser());
			new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/sys/user/?repage";
    }

	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName != null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

	@RequiresUser
	@RequestMapping("info")
	public String info(User user, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/user/userInfo";
			}
			currentUser = UserUtils.getUser(true);
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setRemarks(user.getRemarks());
			systemService.saveUser(currentUser);
			model.addAttribute("message", "保存用户信息成功");
		}
		model.addAttribute("user", currentUser);
		return "modules/sys/user/userInfo";
	}

	@RequiresUser
	@RequestMapping("modifyPwd")
	public String modifyPwd(String oldPassword, String newPassword, Model model) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
			if(Global.isDemoMode()){
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/user/userModifyPwd";
			}
			
			if (SystemService.validatePassword(oldPassword, user.getPassword())){
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				model.addAttribute("message", "修改密码成功");
			}else{
				model.addAttribute("message", "修改密码失败，旧密码错误");
			}
		}
		model.addAttribute("user", user);
		return "modules/sys/user/userModifyPwd";
	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "editUserPemissions")
	public String editUserPemissions(String userId, Model model) {
		//获取选中用户所拥有的角色的菜单
		List<Role> roles= systemService.getUser(userId).getRoleList();
		Set<String> rolemenuids = new HashSet<String>();
		if(roles != null && roles.size()>0){
			for(Role role:roles){
				List<Menu> menus = role.getMenuList();
				for(Menu menu:menus){
					rolemenuids.add(menu.getId());
				}
			}
		}
		//获取选择选中用户关联的菜单
		List<Menu> checkedUserMenus = systemService.findAllMenu(userId);
		//合并菜单，角色自带的菜单不能选中，额外增加的菜单可以选中和修改
		if(checkedUserMenus != null && checkedUserMenus.size()>0){
			for(Menu menu:checkedUserMenus){
				for(String id:rolemenuids){
					if(menu.getId().equals(id)){
						menu.setDisable(true);
					}
				}
			}
		}
		//获取登录用户菜单
		List<Menu> loginUserMenus = systemService.findAllMenu();//缓存数据，需要重新创建实体
		List<Menu> viewUserMenus = new ArrayList<Menu>();
		//合并双方的菜单，设置选中，多余部分只保留登录用户菜单
		for(Menu menu:loginUserMenus){
			Menu menuTemp = new Menu();
			BeanUtils.copyProperties(menu, menuTemp);//复制属性
			for(Menu menu2:checkedUserMenus){
				if(menu.getId().equals(menu2.getId())){
					menuTemp.setChecked(true);
					menuTemp.setDisable(menu2.getIsDisable());
				}
			}
			viewUserMenus.add(menuTemp);
		}
		model.addAttribute("menuList",viewUserMenus);
		model.addAttribute("userId",userId);
		return "modules/sys/user/editUserPemissions";
	}
	
//	@ResponseBody
//	@RequiresPermissions("sys:user:edit")
//	@RequestMapping(value = "saveUserPemissions")
//	public String saveUserPemissions(String userId,String menuIds){
//		List<Menu> menuList = Lists.newArrayList();
//		if (menuIds != null){
//			String[] ids = StringUtils.split(menuIds, ",");
//			for (String menuId : ids) {
//				Menu menu = new Menu();
//				menu.setId(menuId);
//				menuList.add(menu);
//			}
//		}
//		try {
//			systemService.saveUserPemissions(userId,menuList);
//			return "1";
//		} catch (Exception e) {
//			return e.getMessage();
//		}
//	}
	
	/**
	 * 返回用户信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "list/{roleId}")
	public List<User> userList(@PathVariable String roleId) {
		Role role = systemService.getRole(roleId);
		if(role != null)
			return role.getUserList();
		return new ArrayList<User>();
	}
}

class MenuComparator implements Comparator<Menu>{
    @Override  
    public int compare(Menu o1, Menu o2) {  
        return  o1.getId().compareTo(o2.getId());
    }  
} 
