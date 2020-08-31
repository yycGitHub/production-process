package com.surekam.modules.standard.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.web.BaseController;
import com.surekam.modules.standard.entity.Standard;
import com.surekam.modules.standard.entity.StandardItem;
import com.surekam.modules.standard.service.StandardService;

@Controller
@RequestMapping(value = "api/standard/")
public class StandardAPIService extends BaseController {

	@Autowired
	private StandardService standardService;
	
	@RequestMapping(value = "findStandardList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Standard> findStandardList(Standard standard, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Standard> standards = standardService.findAllStandard(new Standard());
		return standards;
	}

	@RequestMapping(value = "findStandardItems", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<StandardItem> findStandardItems(String targetId, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<StandardItem> standardItems = standardService.findStandardItems(targetId);
		return standardItems;
	}
	
	@RequestMapping(value = "getStandardItem", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public StandardItem getStandardItem(String standardItemId, HttpServletRequest request, HttpServletResponse response, Model model) {
		StandardItem standardItem = standardService.getStandardItem(standardItemId);
		return standardItem;
	}
}
