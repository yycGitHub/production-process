package com.surekam.modules.api.web;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.SpringContextHolder;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.sys.dao.LoginTokenDao;
import com.surekam.modules.sys.entity.LoginToken;

public class APIInterceptor extends HandlerInterceptorAdapter {

	private static LoginTokenDao  loginTokenDao = SpringContextHolder.getBean(LoginTokenDao.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String token = request.getHeader("X-Token");
		if(StringUtils.isBlank(token)){
			sendJsonMessage(response,JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage())));
			return false;
		}
		token = URLEncoder.encode(token, "UTF-8");
		String hql = "from LoginToken where token=:p1";
		LoginToken loginToken = loginTokenDao.getByHql(hql,new Parameter(token));
		if(loginToken !=null){
			if(new Date().compareTo(loginToken.getFailTime())<0){
				return true;
			}else{
				sendJsonMessage(response,JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_EXPIRED.getCode(), ResultEnum.TOKEN_IS_EXPIRED.getMessage())));
				return false;
			}
		}else{
			sendJsonMessage(response,JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_INVALID.getCode(), ResultEnum.TOKEN_IS_INVALID.getMessage())));
			return false;
		}
	}
	
	public static void sendJsonMessage(HttpServletResponse response, Object obj) throws Exception {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(obj);
        writer.close();
        response.flushBuffer();
    }
	
}
