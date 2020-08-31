package com.surekam.modules.standard.web;

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
import com.surekam.modules.standard.entity.Standard;
import com.surekam.modules.standard.entity.StandardItem;
import com.surekam.modules.standard.entity.StandardItemValue;
import com.surekam.modules.standard.service.StandardService;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/standard")
public class StandardController extends BaseController{

	@Autowired
	private StandardService standardService;
	
	@ModelAttribute("standard")
	public Standard get(@RequestParam(required=false) String standardId) {
		if (StringUtils.isNotBlank(standardId)){
			return standardService.getStandard(standardId);
		}else{
			Standard standard =new Standard();
			return standard;
		}
	}
	
	@RequestMapping(value = {"list",""})
	public String list(Standard standard, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Standard> page = standardService.find(new Page<Standard>(request, response), standard, true); 
		model.addAttribute("page", page);
		return "modules/standard/standardList";
	}
	
	@RequiresPermissions("sys:standard:view")
	@RequestMapping(value = "form")
	public String form(Standard standard, Model model) {
		if (standard.getParent()==null||standard.getParent().getId()==null){
			standard.setParent(new Standard("1"));
		}
		model.addAttribute("standard", standard);		
		User user = UserUtils.getUser();
		Office company = user.getCompany();
		model.addAttribute("company",company);
		return "modules/standard/standardForm";
	}
	
	@RequiresPermissions("sys:standard:edit")
	@RequestMapping(value = "save")
	public String saveStandard(Standard standard, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作");
			return "redirect:"+Global.getAdminPath()+"/standard/form";
		}
		if (!beanValidator(model, standard)){
			return form(standard, model);
		}
		standardService.saveStandard(standard);
		redirectAttributes.addAttribute("standardId", standard.getId());
		addMessage(redirectAttributes, "保存" + standard.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/standard/list";
	}
	
	@RequiresPermissions("sys:standard:edit")
	@RequestMapping(value = "delete")
	public String delete(String[] ids, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作");
			return "redirect:"+Global.getAdminPath()+"/standard/list";
		}
		standardService.deleteStandard(ids);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:"+Global.getAdminPath()+"/standard/list/?repage";
	}
	
	//********************************标准参数管理**************************************/	
	@RequiresPermissions("sys:standard:expview")
	@RequestMapping(value = "standardItemList")
	public String standardItemList(String stId, Model model, RedirectAttributes redirectAttributes) {
		List<StandardItem> sourcelist = standardService.findStandardItems(stId);
        model.addAttribute("list", sourcelist);
        model.addAttribute("stId", stId);
        User user = UserUtils.getUser();
		Office company = user.getCompany();
		model.addAttribute("company",company);
		return "modules/standard/standardItemList";
	}
	
	@RequiresPermissions("sys:standard:expedit")
	@RequestMapping(value = "standardItem")
	public String standardItemForm(String standardItemId,String stId, Model model) {
		StandardItem standardItem = new StandardItem();
		standardItem.setSort(30);
		if(StringUtils.isNotBlank(standardItemId)){
			 standardItem = standardService.getStandardItem(standardItemId);
		}else{
			standardItem.setTargetId(stId);
		}
		model.addAttribute("standardItem", standardItem);
		
		User user = UserUtils.getUser();
		Office company = user.getCompany();
		model.addAttribute("company",company);
		return "modules/standard/standardItemForm";
	}
	
	@RequiresPermissions("sys:standard:expedit")
	@RequestMapping(value = "saveStandardItem")
	public String saveStandardItem(StandardItem standardItem, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作");
			redirectAttributes.addAttribute("stId", standardItem.getTargetId());
			return "redirect:"+Global.getAdminPath()+"/standard/standardItemList";
		}
		if (!beanValidator(model, standardItem)){
			return standardItemForm(standardItem.getId(),standardItem.getTargetId(), model);
		}
		standardService.saveStandardItem(standardItem);
		redirectAttributes.addAttribute("stId", standardItem.getTargetId());
		addMessage(redirectAttributes, "保存标准项" + standardItem.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/standard/standardItemList";
	}
	
	@RequiresPermissions("sys:standard:expedit")
	@RequestMapping(value = "deleteStandardItem")
	public String deleteStandardItem(String[] ids,String stId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("stId", stId);
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作");
			return "redirect:"+Global.getAdminPath()+"/standard/standardItemList";
		}
		try {
			standardService.deleteStandardItem(ids);
			addMessage(redirectAttributes, "删除成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "删除失败");
		}
		return "redirect:"+Global.getAdminPath()+"/standard/standardItemList";
	}
	
	
	//********************************标准参数值管"*************************************/
	
	@RequiresPermissions("sys:standard:expview")
	@RequestMapping(value = "standardItemValueList")
	public String standardItemValueList(String standardItemId, Model model) {
		StandardItem standardItem =standardService.getStandardItem(standardItemId);
		List<StandardItemValue> sourcelist = standardService.findStandardItemValues(standardItemId);
		model.addAttribute("standardItem", standardItem);
        model.addAttribute("list", sourcelist);
        model.addAttribute("standardItemId", standardItemId);
		return "modules/standard/standardItemValueList";
	}
	
	@RequiresPermissions("sys:standard:expedit")
	@RequestMapping(value = "standardItemValue")
	public String standardItemValueForm(String standardItemValueId,String standardItemId, Model model) {
		StandardItemValue standardItemValue = new StandardItemValue();
		if(StringUtils.isNotBlank(standardItemValueId)){
			standardItemValue = standardService.getStandardItemValue(standardItemValueId);
		}else{
			StandardItem standardItem = standardService.getStandardItem(standardItemId);
			standardItemValue.setStandardItem(standardItem);
		}
		model.addAttribute("standardItemValue", standardItemValue);
		
		User user = UserUtils.getUser();
		Office company = user.getCompany();
		model.addAttribute("company",company);
		return "modules/standard/standardItemValueForm";
	}
	
	@RequiresPermissions("sys:standard:expedit")
	@RequestMapping(value = "saveStandardItemValue")
	public String saveStandardItemValue(StandardItemValue standardItemValue, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作");
			redirectAttributes.addAttribute("standardItemId", standardItemValue.getStandardItem().getId());
			return "redirect:"+Global.getAdminPath()+"/standard/standardItemValueList";
		}
		if (!beanValidator(model, standardItemValue)){
			return standardItemValueForm(standardItemValue.getId(),standardItemValue.getStandardItem().getId(), model);
		}
		standardService.saveStandardItemValue(standardItemValue);
		redirectAttributes.addAttribute("standardItemId", standardItemValue.getStandardItem().getId());
		addMessage(redirectAttributes, "保存'" + standardItemValue.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/standard/standardItemValueList";
	}
	
	@RequiresPermissions("sys:standard:expedit")
	@RequestMapping(value = "deleteStandardItemValue")
	public String deleteStandardItemValue(String[] ids,String standardItemId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("standardItemId", standardItemId);
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作");
			return "redirect:"+Global.getAdminPath()+"/standard/standardItemValueList";
		}
		try {
			standardService.deleteStandardItemValue(ids);
			addMessage(redirectAttributes, "删除成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "删除失败");
		}
		return "redirect:"+Global.getAdminPath()+"/standard/standardItemValueList";
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping("treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response, HttpServletRequest request) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Standard standard = new Standard();
		List<Standard> sourcelist = standardService.findAllStandard(standard);
		for (int i=0; i<sourcelist.size(); i++){
			Standard e = sourcelist.get(i);					
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
			map.put("name", e.getName());
			mapList.add(map);
		}
		return mapList;
	}
	//********************************标准值管"****************************************//
}
