package com.surekam.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surekam.common.config.Global;
import com.surekam.common.utils.CookieUtils;
import com.surekam.common.utils.DateUtils;
import com.surekam.common.utils.Encodes;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.utils.UserUtils;

@Service
public class SystemLogoutFilter extends LogoutFilter {
	
	@Autowired
	private SystemService systemService;

	@Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		//在这里执行退出系统前需要清空的数据
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		//CookieUtils.delCookie(httpRequest, httpResponse, "loginCode");
		//CookieUtils.getCookie(httpRequest, httpResponse, "loginCode", true);
		//登录成功后，记录cookie值
		String loginCode = CookieUtils.getCookie(httpRequest, "loginCode");
		User user = UserUtils.getUser();
		if(null == loginCode || (null != loginCode && !loginCode.equals(user.getLoginCode()))){
			String time = DateUtils.getDate("yyyyMMddHHmmss");
			String loginCodeUpdate = Encodes.encodeBase62((user.getLoginCode() + time).getBytes());
			user.setLoginCode(loginCodeUpdate);
			CookieUtils.setCookie(httpResponse, "loginCode", loginCodeUpdate);
			systemService.saveUser(user);
		}
		
		Subject subject = getSubject(request, response);
        try {
            subject.logout();
        } catch (SessionException ise) {
           ise.printStackTrace();
        }
        
        String webUrl = Global.getConfig("webUrl");
        String casUrl = Global.getConfig("casUrl");       
        issueRedirect(request, response, casUrl+"/logout?service="+webUrl);
         //返回false表示不执行后续的过滤器，直接返回跳转到登录页面
        return false;
    }
}
