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
import com.surekam.modules.agro.taskitemcategory.entity.TaskItemCategory;
import com.surekam.modules.agro.taskitemcategory.service.TaskItemCategoryService;

/**
 * 作业项类别表(包括 施肥 投料等 )Controller
 * @author liwei
 * @version 2019-04-23
 */
@Controller
@RequestMapping(value = "${adminPath}/taskitemcategory/taskItemCategory")
public class TaskItemCategoryController extends BaseController {

	@Autowired
	private TaskItemCategoryService taskItemCategoryService;
	
	@ModelAttribute
	public TaskItemCategory get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return taskItemCategoryService.get(id);
		}else{
			return new TaskItemCategory();
		}
	}
	
	@RequiresPermissions("taskitemcategory:taskItemCategory:view")
	@RequestMapping(value = {"list", ""})
	public String list(TaskItemCategory taskItemCategory, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TaskItemCategory> page = taskItemCategoryService.find(new Page<TaskItemCategory>(request, response), taskItemCategory); 
        model.addAttribute("page", page);
		return "modules/" + "taskitemcategory/taskItemCategoryList";
	}

	@RequiresPermissions("taskitemcategory:taskItemCategory:view")
	@RequestMapping(value = "form")
	public String form(TaskItemCategory taskItemCategory, Model model) {
		model.addAttribute("taskItemCategory", taskItemCategory);
		return "modules/" + "taskitemcategory/taskItemCategoryForm";
	}
	
	@RequiresPermissions("taskitemcategory:taskItemCategory:view")
	@RequestMapping(value = "information")
	public String information(TaskItemCategory taskItemCategory, Model model) {
		model.addAttribute("taskItemCategory", taskItemCategory);
		return "modules/" + "taskitemcategory/taskItemCategoryInformation";
	}

	@RequiresPermissions("taskitemcategory:taskItemCategory:edit")
	@RequestMapping(value = "save")
	public String save(TaskItemCategory taskItemCategory, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, taskItemCategory)){
			return form(taskItemCategory, model);
		}
		taskItemCategoryService.save(taskItemCategory);
		addMessage(redirectAttributes, "保存作业项类别表(包括 施肥 投料等 )成功");
		return "redirect:"+Global.getAdminPath()+"/taskitemcategory/taskItemCategory/?repage";
	}
	
	@RequiresPermissions("taskitemcategory:taskItemCategory:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		taskItemCategoryService.delete(id);
		addMessage(redirectAttributes, "删除作业项类别表(包括 施肥 投料等 )成功");
		return "redirect:"+Global.getAdminPath()+"/taskitemcategory/taskItemCategory/?repage";
	}

}
