/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.modules.sys.web;

import java.util.ArrayList;
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
import com.surekam.modules.sys.entity.AdditionalParameters;
import com.surekam.modules.sys.entity.Item;
import com.surekam.modules.sys.entity.Menu;
import com.surekam.modules.sys.entity.TreeRespVO;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.utils.UserUtils;

/**
 * 菜单Controller
 * @author sureserve
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/menu")
public class MenuController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@ModelAttribute("menu")
	public Menu get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getMenu(id);
		}else{
			return new Menu();
		}
	}
	
	@RequiresUser
	@RequestMapping(value = {"index", ""})
	public String index() {
		return "modules/sys/menu/menuIndex";
	}
	
	@RequiresUser
	@RequestMapping(value = "showTree")
	public String showTree() {
		return "modules/sys/menu/menuShowTree";
	}

	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = {"list"})
	public String list(HttpServletRequest request, HttpServletResponse response,
			Menu menu,Model model) {
		User user = UserUtils.getUser();
		model.addAttribute("menu", menu);
        Page<Menu> page = systemService.findMenuPage(new Page<Menu>(request, response), menu,user); 
        model.addAttribute("page", page);
		return "modules/sys/menu/menuList";
	}

	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = "form")
	public String form(Menu menu, Model model,String rebackId) {
		if (menu.getParent()==null||menu.getParent().getId()==null){
			menu.setParent(new Menu("1"));
		}
		menu.setParent(systemService.getMenu(menu.getParent().getId()));
		model.addAttribute("menu", menu);
		model.addAttribute("rebackId", rebackId);
		return "modules/sys/menu/menuForm";
	}
	
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "save")
	public String save(Menu menu, Model model, RedirectAttributes redirectAttributes,String rebackId) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/sys/menu/list?id="+rebackId;
		}
		if (!beanValidator(model, menu)){
			return form(menu, model,rebackId);
		}
		systemService.saveMenu(menu);
		addMessage(redirectAttributes, "保存菜单'" + menu.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/sys/menu?id="+rebackId;
	}
	
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes,String rebackId) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/sys/menu/list?id="+rebackId;
		}
		if (Menu.isRoot(id)){
			addMessage(redirectAttributes, "删除菜单失败, 不允许删除顶级菜单或编号为空");
		}else{
			systemService.deleteMenu(id);
			addMessage(redirectAttributes, "删除菜单成功");
		}
		return "redirect:"+Global.getAdminPath()+"/sys/menu?id="+rebackId;
	}

	@RequiresUser
	@RequestMapping(value = "tree")
	public String tree() {
		return "modules/sys/menu/menuTree";
	}
	
	/**
	 * 批量修改菜单排序
	 */
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/sys/menu/list";
		}
    	int len = ids.length;
    	Menu[] menus = new Menu[len];
    	for (int i = 0; i < len; i++) {
    		menus[i] = systemService.getMenu(ids[i]);
    		menus[i].setSort(sorts[i]);
    		systemService.saveMenu(menus[i]);
    	}
    	addMessage(redirectAttributes, "保存菜单排序成功!");
		return "redirect:"+Global.getAdminPath()+"/sys/menu/list";
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Menu> list = systemService.findAllMenu();
		for (int i=0; i<list.size(); i++){
			Menu e = list.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping("ztreeDynamicData")
	public List<Map<String, Object>> ztreeDynamicData(HttpServletResponse response,
			@RequestParam(required = false) String itemId) {		
		response.setContentType("application/json; charset=UTF-8");
		User user = UserUtils.getUser();
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Menu> list = systemService.findZtreeMenus(user,itemId);
		for (int i=0; i<list.size(); i++){
			Menu e = list.get(i);					
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("parent_id", e.getParent() != null ? e.getParent().getId() : 0);
			map.put("text", e.getName());
			if(e.getChildList().size()>0){
				map.put("type","folder");
				Map<String, Object> childMap = Maps.newHashMap();
				childMap.put("children",e.getChildList());
				childMap.put("id",e.getId());
				map.put("additionalParameters",childMap);
			}
			else
			{
				map.put("type","item");
			}
			mapList.add(map);
		}
		return mapList;
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping("menuTreeData")
	public TreeRespVO menuTreeData(HttpServletResponse response,
			@RequestParam(required = false) String itemId) {		
		response.setContentType("application/json; charset=UTF-8");
		User user = UserUtils.getUser();
		List<Menu> list = systemService.findZtreeMenus(user,itemId);
        TreeRespVO vo = new TreeRespVO();
        List<Item> voItemList = new ArrayList<Item>();
        if (null != list && list.size() !=0)
        {
             //Tree和数据库对应的实体bean对象
             for (Menu menu : list )
             {
            	 Item item = new Item();
				 int child_count = menu.getChildList().size();//根据遍历的节点，查询该节点子节点的个数。
				 item .setText(menu.getName());
				 if (child_count > 0)
				 {
					 item.setType("folder" );//有子节点
				     AdditionalParameters adp = new AdditionalParameters();
				     adp.setId(menu.getId());
				     item.setAdditionalParameters(adp);
				     item.setId(menu.getId());
				 }
				 else
				 {
				     AdditionalParameters adp = new AdditionalParameters();
				     adp.setId(menu.getId());
				     adp.setItemSelected( true);
				     item.setAdditionalParameters(adp);
				     item.setType("item" );//无子节点
				     item.setId(menu.getId());
				 }
				 voItemList.add(item );
             }
         }
         vo.setStatus("OK");
         vo.setData(voItemList);
         return vo;
	}
}
