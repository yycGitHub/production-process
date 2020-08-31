package com.surekam.modules.common.web;

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
import com.surekam.modules.common.entity.FileInfo;
import com.surekam.modules.common.service.FileInfoService;

/**
 * 文件信息Controller
 * @author l
 * @version 2017-09-30
 */
@Controller
@RequestMapping(value = "${adminPath}/common/fileInfo")
public class FileInfoController extends BaseController {

	@Autowired
	private FileInfoService fileInfoService;
	
	@ModelAttribute
	public FileInfo get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return fileInfoService.get(id);
		}else{
			return new FileInfo();
		}
	}
	
	@RequiresPermissions("common:fileInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(FileInfo fileInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<FileInfo> page = fileInfoService.find(new Page<FileInfo>(request, response), fileInfo); 
        model.addAttribute("page", page);
		return "modules/" + "common/fileInfoList";
	}

	@RequiresPermissions("common:fileInfo:view")
	@RequestMapping(value = "form")
	public String form(FileInfo fileInfo, Model model) {
		model.addAttribute("fileInfo", fileInfo);
		return "modules/" + "common/fileInfoForm";
	}

	@RequiresPermissions("common:fileInfo:edit")
	@RequestMapping(value = "save")
	public String save(FileInfo fileInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, fileInfo)){
			return form(fileInfo, model);
		}
		fileInfoService.save(fileInfo);
		addMessage(redirectAttributes, "保存文件信息成功");
		return "redirect:"+Global.getAdminPath()+"/common/fileInfo/?repage";
	}
	
	@RequiresPermissions("common:fileInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(HttpServletRequest request,String id, RedirectAttributes redirectAttributes) {
		String root = request.getSession().getServletContext().getRealPath("..");
		fileInfoService.delete(root,id);
		addMessage(redirectAttributes, "删除文件信息成功");
		return "redirect:"+Global.getAdminPath()+"/common/fileInfo/?repage";
	}

}
