package com.surekam.modules.cms.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.surekam.common.web.BaseController;
import com.surekam.modules.cms.service.CategoryService;

@Controller
@RequestMapping(value = "${adminPath}/cms")
public class CmsController extends BaseController {

	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping(value = "")
	public String index(Model model,String type) {
		model.addAttribute("type", type);
		return "modules/cms/cmsIndex";
	}
	
	@RequestMapping(value = "tree")
	public String tree(Model model,String type) {
		if("0".equals(type)){
			return "modules/cms/lmCmsTree";
		}else if("1".equals(type)){
			return "modules/cms/nrCmsTree";
		}else{
			return "modules/cms/plCmsTree";
		}
	}
	
	@RequestMapping(value = "none")
	public String none() {
		return "modules/cms/cmsNone";
	}

}
