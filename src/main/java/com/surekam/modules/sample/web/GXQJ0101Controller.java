package com.surekam.modules.sample.web;

import java.io.IOException;
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
import com.surekam.modules.common.entity.FileInfo;
import com.surekam.modules.common.service.FileInfoService;
import com.surekam.modules.sample.entity.GXQJ0101;
import com.surekam.modules.sample.service.GXQJ0101Service;

/**
 * 请假单Controller
 * @author ligm
 * @version 2017-11-20
 */
@Controller
@RequestMapping(value = "${adminPath}/sample/gXQJ0101")
public class GXQJ0101Controller extends BaseController {

	@Autowired
	private GXQJ0101Service gXQJ0101Service;
	
	@Autowired
	private FileInfoService fileInfoService;
	
	@ModelAttribute
	public GXQJ0101 get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return gXQJ0101Service.get(id);
		}else{
			return new GXQJ0101();
		}
	}
	
	@RequiresPermissions("sample:gXQJ0101:view")
	@RequestMapping(value = {"list", ""})
	public String list(GXQJ0101 gXQJ0101, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<GXQJ0101> page = gXQJ0101Service.find(new Page<GXQJ0101>(request, response), gXQJ0101); 
        model.addAttribute("page", page);
		return "modules/" + "sample/gXQJ0101List";
	}

	@RequiresPermissions("sample:gXQJ0101:view")
	@RequestMapping(value = "form")
	public String form(GXQJ0101 gXQJ0101, Model model) {
		model.addAttribute("gXQJ0101", gXQJ0101);
		List<FileInfo> fileList = fileInfoService.findAllfilesByYwb(gXQJ0101.getGxqj0101Id(), FileInfo.YWZB_TYPE_QJD,null);
		model.addAttribute("fileList", fileList);
		return "modules/" + "sample/gXQJ0101Form";
	}
	
	@RequiresPermissions("sample:gXQJ0101:view")
	@RequestMapping(value = "information")
	public String information(GXQJ0101 gXQJ0101, Model model) {
		model.addAttribute("gXQJ0101", gXQJ0101);
		
		List<FileInfo> fileList = fileInfoService.findAllfilesByYwb(gXQJ0101.getGxqj0101Id(), FileInfo.YWZB_TYPE_QJD,null);
		model.addAttribute("fileList", fileList);
		return "modules/" + "sample/gXQJ0101Information";
	}

	@RequiresPermissions("sample:gXQJ0101:edit")
	@RequestMapping(value = "save")
	public String save(GXQJ0101 gXQJ0101, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request,String[] fileIds)throws IllegalStateException, IOException {
		if (!beanValidator(model, gXQJ0101)){
			return form(gXQJ0101, model);
		}
		gXQJ0101Service.saveGXQJ0101AndFileInfos(gXQJ0101,fileIds);
		addMessage(redirectAttributes, "保存请假单成功");
		return "redirect:"+Global.getAdminPath()+"/sample/gXQJ0101/?repage";
	}
	
	@RequiresPermissions("sample:gXQJ0101:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		gXQJ0101Service.delete(id);
		addMessage(redirectAttributes, "删除请假单成功");
		return "redirect:"+Global.getAdminPath()+"/sample/gXQJ0101/?repage";
	}

}
