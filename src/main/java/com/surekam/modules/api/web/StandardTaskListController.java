package com.surekam.modules.api.web;

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
import com.surekam.common.web.BaseController;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.standardtasklist.entity.StandardTaskList;
import com.surekam.modules.agro.standardtasklist.service.StandardTaskListService;

/**
 * 作业记录主数据表Controller
 * @author liwei
 * @version 2019-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/standardtasklist/standardTaskList")
public class StandardTaskListController extends BaseController {

	@Autowired
	private StandardTaskListService standardTaskListService;
	
	@ModelAttribute
	public StandardTaskList get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return standardTaskListService.get(id);
		}else{
			return new StandardTaskList();
		}
	}
	
	@RequiresPermissions("standardtasklist:standardTaskList:view")
	@RequestMapping(value = {"list", ""})
	public String list(StandardTaskList standardTaskList, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<StandardTaskList> page = standardTaskListService.find(new Page<StandardTaskList>(request, response), standardTaskList); 
        model.addAttribute("page", page);
		return "modules/" + "standardtasklist/standardTaskListList";
	}

	@RequiresPermissions("standardtasklist:standardTaskList:view")
	@RequestMapping(value = "form")
	public String form(StandardTaskList standardTaskList, Model model) {
		model.addAttribute("standardTaskList", standardTaskList);
		return "modules/" + "standardtasklist/standardTaskListForm";
	}
	
	@RequiresPermissions("standardtasklist:standardTaskList:view")
	@RequestMapping(value = "information")
	public String information(StandardTaskList standardTaskList, Model model) {
		model.addAttribute("standardTaskList", standardTaskList);
		return "modules/" + "standardtasklist/standardTaskListInformation";
	}

	@RequiresPermissions("standardtasklist:standardTaskList:edit")
	@RequestMapping(value = "save")
	public String save(StandardTaskList standardTaskList, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, standardTaskList)){
			return form(standardTaskList, model);
		}
		standardTaskListService.save(standardTaskList);
		addMessage(redirectAttributes, "保存作业记录主数据表成功");
		return "redirect:"+Global.getAdminPath()+"/standardtasklist/standardTaskList/?repage";
	}
	
	@RequiresPermissions("standardtasklist:standardTaskList:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		standardTaskListService.delete(id);
		addMessage(redirectAttributes, "删除作业记录主数据表成功");
		return "redirect:"+Global.getAdminPath()+"/standardtasklist/standardTaskList/?repage";
	}

}
