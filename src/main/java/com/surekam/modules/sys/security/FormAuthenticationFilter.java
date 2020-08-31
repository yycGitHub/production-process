/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

/**
 * 表单验证（包含验证码）过滤类
 * @author sureserve
 * @version 2013-5-19
 */
@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;
	
	public static final String DEFAULT_REDIRECTURL_PARAM = "redirectUrl";
	
	public static final String DEFAULT_WX_USER_ID_PARAM = "wxUserId";

	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		if (password==null){
			password = "";
		}
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		String captcha = getCaptcha(request);
		return new UsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha);
	}
	
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		String redirectUrl = getRedirectUrl(request);
		if (StringUtils.isNotBlank(redirectUrl)) {
			WebUtils.redirectToSavedRequest(request, response,redirectUrl);
		} else {
			WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
		}
	}
	
	protected String getRedirectUrl(ServletRequest request) {
		return WebUtils.getCleanParam(request, DEFAULT_REDIRECTURL_PARAM);
	}
	
	protected String getWxUserId(ServletRequest request) {
		return WebUtils.getCleanParam(request, DEFAULT_WX_USER_ID_PARAM);
	}

}