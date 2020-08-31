package com.surekam.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.surekam.common.config.Global;
import com.surekam.common.utils.CookieUtils;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.security.CasToken;

@Controller
public class CasLogin {

	@Autowired
	private ApiUserService apiUserService;

	/**
	 * 单点登录
	 */
	@RequestMapping(value = "/caslogin", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		String redirectUrl = request.getParameter("redirectUrl");
		Subject subject = SecurityUtils.getSubject();
		Object object = subject.getSession().getAttribute("_const_cas_assertion_");
		if (object != null) {
			if (subject.isAuthenticated() == false) {
				Assertion assertion = (Assertion) object;
				String loginName = assertion.getPrincipal().getName();
				CasToken token = new CasToken();
				token.setUsername(loginName);
				SecurityUtils.getSubject().login(token);
				String cookieToken = apiUserService.casLogin(loginName);
				CookieUtils.setCookieAndPath(response, "Agro-Token", cookieToken, -1, "/");
				Session session = SecurityUtils.getSubject().getSession();
				session.setAttribute("Agro-Token", cookieToken);
			} else if (CookieUtils.getCookie(request, "Agro-Token") == null) {
				Session session = SecurityUtils.getSubject().getSession();
				String cookieToken = (String) session.getAttribute("Agro-Token");
				CookieUtils.setCookieAndPath(response, "Agro-Token", cookieToken, -1, "/");
			}
		}
		if (StringUtils.isNotBlank(redirectUrl)) {
			return "redirect:" + redirectUrl;
		} else {
			String webUrl = Global.getConfig("webUrl");
			// 登录成功，跳转到VUE前端地址
			return "redirect:" + webUrl;
		}
	}

	@RequestMapping(value = "/caslogout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated() == true) {
			subject.logout();
		}
		String webUrl = Global.getConfig("webUrl");
		return "redirect:" + webUrl;
	}
}
