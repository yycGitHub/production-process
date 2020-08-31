/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.surekam.common.config.Global;
import com.surekam.common.utils.CookieUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.sys.security.Principal;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.utils.UserUtils;

/**
 * 退出登录Controller
 * @author ligm
 * @version 2018-01-22
 */
@Controller
public class LogoutController extends BaseController{
	
	@Autowired
	private SystemService systemService;
	
	/**
	 * 退出登录
	 */
	@RequestMapping(value = "${adminPath}/logout", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		CookieUtils.delCookie(request, response, "loginCode");
		Principal principal = UserUtils.getPrincipal();  
	    // 如果已经登录，则跳转到管理首页  
	    if(principal != null){  
	    	SecurityUtils.getSubject().logout(); 
	    }  
	    return "redirect:" + Global.getAdminPath()+"/login";
	}
	
}
