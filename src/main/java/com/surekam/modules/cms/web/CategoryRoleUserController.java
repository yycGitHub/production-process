package com.surekam.modules.cms.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.cms.entity.CategoryRoleUser;
import com.surekam.modules.cms.service.CategoryRoleUserService;
import com.surekam.modules.sys.entity.Role;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.SystemService;

/**
 * 栏目管理员Controller
 * @author liuyi
 * @version 2018-01-29
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/categoryRoleUser")
public class CategoryRoleUserController extends BaseController {

	@Autowired
	private CategoryRoleUserService categoryRoleUserService;
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public CategoryRoleUser get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return categoryRoleUserService.get(id);
		}else{
			return new CategoryRoleUser();
		}
	}
	
	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = {"list", ""})
	public String list(CategoryRoleUser categoryRoleUser, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<CategoryRoleUser> page = categoryRoleUserService.find(new Page<CategoryRoleUser>(request, response), categoryRoleUser); 
        model.addAttribute("page", page);
		return "modules/" + "cms/categoryRoleUserList";
	}

	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "form")
	public String form(CategoryRoleUser categoryRoleUser, Model model) {
		model.addAttribute("categoryRoleUser", categoryRoleUser);
		return "modules/" + "cms/categoryRoleUserForm";
	}
	
	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "information")
	public String information(CategoryRoleUser categoryRoleUser, Model model) {
		model.addAttribute("categoryRoleUser", categoryRoleUser);
		return "modules/" + "cms/categoryRoleUserInformation";
	}

	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "save")
	public String save(CategoryRoleUser categoryRoleUser, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, categoryRoleUser)){
			return form(categoryRoleUser, model);
		}
		categoryRoleUserService.save(categoryRoleUser);
		addMessage(redirectAttributes, "保存栏目管理员成功");
		return "redirect:"+Global.getAdminPath()+"/cms/categoryRoleUser/?repage";
	}
	
	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		//categoryRoleUserService.delete(id);
		addMessage(redirectAttributes, "删除栏目管理员成功");
		return "redirect:"+Global.getAdminPath()+"/cms/categoryRoleUser/?repage";
	}
	
	 /**
     * 跳转到添加栏目管理员
     * 
     * */
    @RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "toPlateAdmin")
	public String toPlateAdmin(String id,User user, Model model,HttpServletRequest request, HttpServletResponse response) {
    	CategoryRoleUser categoryRoleUser =new CategoryRoleUser();
    	//categoryRoleUser.setCategoryId(Integer.parseInt(id));
    	categoryRoleUser = categoryRoleUserService.findObjByCategoryId(Integer.parseInt(id));
    	//根据categoryId查询,获取用户列表，其中被设置为某个栏目版主的用户将排列在最前面
    	List<User>  list = categoryRoleUserService.findListByCategoryId(Integer.parseInt(id),user);
    	//User currentUser = UserUtils.getUser();
    	//Page<User> page = systemService.findUser(new Page<User>(request, response), new User()); 
    	//设置分页
    	Page<User> page = categoryRoleUserService.setPageList(new Page<User>(request, response),request,response,list);
    	
    	//if(categoryRoleUser==null){
    		//categoryRoleUser = new CategoryRoleUser();
    	//}
    	//categoryRoleUser.setCategoryId(Integer.parseInt(id));
        model.addAttribute("page", page);
    	model.addAttribute("user", user);
    	model.addAttribute("categoryId", id);
    	model.addAttribute("categoryRoleUser", categoryRoleUser);
    	//model.addAttribute("allRoles", systemService.findAllRole());
		return "modules/cms/categoryUserForm";
	}

    
    
    
    
    /**
     * 绑定角色
     * 
     * */
    @RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "addPlateAdminRole")
	public String addPlateAdminRole(CategoryRoleUser categoryRoleUser, User user, Model model,HttpServletRequest request, HttpServletResponse response) {
    	CategoryRoleUser category =new CategoryRoleUser();
    	category = categoryRoleUserService.findObjByCategoryId(categoryRoleUser.getCategoryId());
    	List<Role> roleIdList = categoryRoleUser.getRoleList();
		String roleStr="";
		if(roleIdList!=null && roleIdList.size()>0){
    		for (Role role : roleIdList) {
    			roleStr+=role.getId()+",";
			}
    		roleStr = roleStr.substring(0, roleStr.length()-1);
		}
		
		
    	//如果还未设置版主角色则保存categoryRoleUser，如果已经设置了版主角色则更新categoryRoleUser
    	if(category!=null){
    		categoryRoleUserService.updateRole(roleStr,category);
    	}else{
    		categoryRoleUser.setRoleId(roleStr);
    		categoryRoleUserService.save(categoryRoleUser);
    	}
    	
    	//category = categoryRoleUserService.findObjByCategoryId(categoryRoleUser.getCategoryId());
    	//categoryRoleUser.setCategoryId(Integer.parseInt(id));
    	
    	//根据categoryId查询
    	List<User>  list = categoryRoleUserService.findListByCategoryId(categoryRoleUser.getCategoryId(),user);
    	//设置分页
    	Page<User> page = categoryRoleUserService.setPageList(new Page<User>(request, response),request,response,list);
    	model.addAttribute("page", page);
     	model.addAttribute("user", user);
    	model.addAttribute("categoryRoleUser", categoryRoleUser);
    	//model.addAttribute("allRoles", systemService.findAllRole());
    	addMessage(model, "绑定成功！");
		return "modules/cms/categoryUserForm";
	}
    
    /**
     * 指定版主
     * 
     * */
    @RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "addPlateAdmin")
	public String addPlateAdmin(String userId,String exUserIds,String add,String categoryId,User user, Model model,HttpServletRequest request, HttpServletResponse response) {
    	CategoryRoleUser category =new CategoryRoleUser();
    	category = categoryRoleUserService.findObjByCategoryId(Integer.parseInt(categoryId));
    	
    	
    	if(category.getCategoryId()==null){
    		category.setCategoryId(Integer.parseInt(categoryId));
    		categoryRoleUserService.save(category);
    		//addMessage(model, "该栏目还未绑定版主角色，请先绑定角色！");
    		//return toPlateAdmin(categoryId, user,  model, request,  response);
    	}
    	String UserIds="";
    	//增加版主
    	if("1".equals(add)){
    		if("".equals(exUserIds)){
    			UserIds = userId;
    		}else{
    			UserIds = exUserIds+","+userId;
    		}
    	}else{//删除版主
    		UserIds = (exUserIds+",").replace((userId+","), "");
    		if(UserIds.endsWith(",")){
    			UserIds = UserIds.substring(0, UserIds.length()-1);
    		}
    		if(UserIds.startsWith(",")){
    			UserIds = UserIds.substring(1, UserIds.length());
    		}
    	}
    	
    	category.setUserId(UserIds);
    	categoryRoleUserService.updatePlate(category);
    	
    	//categoryRoleUser.setCategoryId(Integer.parseInt(id));
    	
    	//根据categoryId查询
    	List<User>  list = categoryRoleUserService.findListByCategoryId(category.getCategoryId(),user);
    	//设置分页
    	Page<User> page = categoryRoleUserService.setPageList(new Page<User>(request, response),request,response,list);
    	model.addAttribute("page", page);
     	model.addAttribute("user", user);
     	model.addAttribute("categoryId", categoryId);
    	model.addAttribute("categoryRoleUser", category);
    	//model.addAttribute("allRoles", systemService.findAllRole());
    	if("1".equals(add)){
    		addMessage(model, "添加版主成功！");
    	}else{
    		addMessage(model, "取消版主成功！");
    	}
		return "modules/cms/categoryUserForm";
	}
    
    
	
	
}
