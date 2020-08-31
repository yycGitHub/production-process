package com.surekam.modules.sample.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.surekam.common.web.BaseController;
import com.surekam.modules.sample.service.DocUtilService;

/**
 * java生成doc文件Controller
 * @author liuyi
 * @version 2018-01-18
 */
@Controller
@RequestMapping(value = "${adminPath}/sample/docutil")
public class DocUtilController extends BaseController {

	@Autowired
	private DocUtilService docUtilService;
	
	@RequestMapping(value = {"createDoc"})
	public String createDoc( HttpServletRequest request, HttpServletResponse response, Model model) {
		docUtilService.createDoc("templateDoc", "D:\\testDoc.doc");
        //返回页面xxx
		return "modules/xxx";
	}

	

}
