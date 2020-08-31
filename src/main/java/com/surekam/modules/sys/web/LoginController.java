/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.modules.sys.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Maps;
import com.surekam.common.config.Global;
import com.surekam.common.utils.CacheUtils;
import com.surekam.common.utils.CookieUtils;
import com.surekam.common.utils.DateUtils;
import com.surekam.common.utils.Encodes;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.security.CookieLoginToken;
import com.surekam.modules.sys.security.SystemWxToken;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.utils.Sign;
import com.surekam.modules.sys.utils.UserUtils;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * 登录Controller
 * @author sureserve
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController{
	
	@Autowired
	private SystemService systemService;
	
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
//		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		String redirectUrl = request.getParameter("redirectUrl");
//		model.addAttribute("redirectUrl",redirectUrl);
//		if(DeviceType.MOBILE.equals(userAgent.getOperatingSystem().getDeviceType())){
//			if(user.getId() != null){
//				return "redirect:"+redirectUrl;
//			}
//			String code = request.getParameter("code");
//			if(code != null) {
//				String openId = new Sign().getOpenId(code);
//				User loginUser = UserUtils.getUserByOpenId(openId);
//				if(loginUser != null) {
//					SystemWxToken token = new SystemWxToken();
//					token.setOpenId(openId);
//					try {
//						SecurityUtils.getSubject().login(token);
//						return "redirect:"+redirectUrl;
//					} catch (AuthenticationException e) {
//						model.addAttribute("openId", openId);
//						model.addAttribute("erro", "用户名或者密码不正确！");
//						return "modules/front/weixinLogin"; 
//					}
//				} else {
//					model.addAttribute("openId", openId);
//					return "modules/front/weixinLogin"; 
//				}
//			}
//			model.addAttribute("appid", Sign.appid);
//			return "modules/front/wxCodeGet";
//		}else{
			if(user.getId() != null){
				return "redirect:"+Global.getAdminPath();
			}else if(null != CookieUtils.getCookie(request, "loginCode")){
				User loginUser = UserUtils.getUserByLoginCode(CookieUtils.getCookie(request, "loginCode"));
				//检查是否过期
				try {
					if(null != loginUser && null != loginUser.getLoginDate() && !"".equals(loginUser.getLoginDate())){
						long days = DateUtils.calcDays(loginUser.getLoginDate(), new Date());
						if(days <= 30){
							CookieLoginToken token = new CookieLoginToken();
							token.setLoginCode(CookieUtils.getCookie(request, "loginCode"));
							SecurityUtils.getSubject().login(token);
							return "redirect:"+Global.getAdminPath();
						}else{
							model.addAttribute("error", "登录已经过期，请重新登录！");
							return "modules/sys/common/sysLogin";
						}
						
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					model.addAttribute("error", "用户验证失败，请重新登录！");
					return "modules/sys/common/sysLogin";
				}
				
			}
			return "modules/sys/common/sysLogin";
//		}
	}
	
	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String login(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String username, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		// 如果已经登录，则跳转到管理首页
		if(user.getId() != null){
			return "redirect:"+Global.getAdminPath();
		}
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
		return "modules/sys/common/sysLogin";
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresUser
	@RequestMapping(value = "${adminPath}")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		User user = UserUtils.getUser();
		// 未登录，则跳转到登录页
		if(user.getId() == null){
			return "redirect:"+Global.getAdminPath()+"/login";
		}
		//登录成功后，记录cookie值
		String loginCode = CookieUtils.getCookie(request, "loginCode");
		if(null == loginCode || (null != loginCode && !loginCode.equals(user.getLoginCode()))){
			String time = DateUtils.getDate("yyyyMMddHHmmss");
			String loginCodeUpdate = Encodes.encodeBase62((user.getLoginName() + time).getBytes());
			user.setLoginCode(loginCodeUpdate);
			CookieUtils.setCookie(response, "loginCode", loginCodeUpdate);
			systemService.saveUser(user);
		}
		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(user.getLoginName(), false, true);
		return "modules/sys/common/sysIndex";
	}
	
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isNotBlank(theme)){
			CookieUtils.setCookie(response, "theme", theme);
		}else{
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:"+request.getParameter("url");
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
	

	@SuppressWarnings("resource")
	@RequestMapping("${adminPath}/download")
	public String download(@RequestParam String filePath,HttpServletResponse response) {
		File file = new File(filePath);
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(filePath);
			response.reset();
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			OutputStream outputStream = new BufferedOutputStream(
					response.getOutputStream());
			byte data[] = new byte[1024];
			while (inputStream.read(data, 0, 1024) >= 0) {
				outputStream.write(data);
			}
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
