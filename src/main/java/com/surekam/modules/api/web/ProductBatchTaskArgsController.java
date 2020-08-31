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
import com.surekam.modules.agro.productbatchtaskargs.entity.ProductBatchTaskArgs;
import com.surekam.modules.agro.productbatchtaskargs.service.ProductBatchTaskArgsService;

/**
 * 批次标准作业详细参数表Controller
 * @author liwei
 * @version 2019-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/productbatchtaskargs/productBatchTaskArgs")
public class ProductBatchTaskArgsController extends BaseController {

	@Autowired
	private ProductBatchTaskArgsService productBatchTaskArgsService;
	
	@ModelAttribute
	public ProductBatchTaskArgs get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return productBatchTaskArgsService.get(id);
		}else{
			return new ProductBatchTaskArgs();
		}
	}
	
	@RequiresPermissions("productbatchtaskargs:productBatchTaskArgs:view")
	@RequestMapping(value = {"list", ""})
	public String list(ProductBatchTaskArgs productBatchTaskArgs, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ProductBatchTaskArgs> page = productBatchTaskArgsService.find(new Page<ProductBatchTaskArgs>(request, response), productBatchTaskArgs); 
        model.addAttribute("page", page);
		return "modules/" + "productbatchtaskargs/productBatchTaskArgsList";
	}

	@RequiresPermissions("productbatchtaskargs:productBatchTaskArgs:view")
	@RequestMapping(value = "form")
	public String form(ProductBatchTaskArgs productBatchTaskArgs, Model model) {
		model.addAttribute("productBatchTaskArgs", productBatchTaskArgs);
		return "modules/" + "productbatchtaskargs/productBatchTaskArgsForm";
	}
	
	@RequiresPermissions("productbatchtaskargs:productBatchTaskArgs:view")
	@RequestMapping(value = "information")
	public String information(ProductBatchTaskArgs productBatchTaskArgs, Model model) {
		model.addAttribute("productBatchTaskArgs", productBatchTaskArgs);
		return "modules/" + "productbatchtaskargs/productBatchTaskArgsInformation";
	}

	@RequiresPermissions("productbatchtaskargs:productBatchTaskArgs:edit")
	@RequestMapping(value = "save")
	public String save(ProductBatchTaskArgs productBatchTaskArgs, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, productBatchTaskArgs)){
			return form(productBatchTaskArgs, model);
		}
		productBatchTaskArgsService.save(productBatchTaskArgs);
		addMessage(redirectAttributes, "保存批次标准作业详细参数表成功");
		return "redirect:"+Global.getAdminPath()+"/productbatchtaskargs/productBatchTaskArgs/?repage";
	}
	
	@RequiresPermissions("productbatchtaskargs:productBatchTaskArgs:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		productBatchTaskArgsService.delete(id);
		addMessage(redirectAttributes, "删除批次标准作业详细参数表成功");
		return "redirect:"+Global.getAdminPath()+"/productbatchtaskargs/productBatchTaskArgs/?repage";
	}

}
