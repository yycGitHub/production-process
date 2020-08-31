package com.surekam.modules.sys.security;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.surekam.common.utils.SpringContextHolder;
import com.surekam.modules.sys.entity.Menu;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.utils.UserUtils;

public class CookieLoginRealm  extends AuthorizingRealm{
	
	private SystemService systemService;
	
	public CookieLoginRealm(){
		//直接比对密文
    	setCredentialsMatcher(new AllowAllCredentialsMatcher());
		 //使用自定义Token类
        setAuthenticationTokenClass(CookieLoginToken.class);
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		User user = getSystemService().getUserByLoginName(principal.getLoginName());
		if (user != null) {
			UserUtils.putCache("user", user);
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<Menu> list = UserUtils.getMenuList();
			for (Menu menu : list){
				if (StringUtils.isNotBlank(menu.getPermission())){
					// 添加基于Permission的权限信息
					for (String permission : StringUtils.split(menu.getPermission(),",")){
						info.addStringPermission(permission);
					}
				}
			}
			return info;
		} else {
			return null;
		}
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		if(authcToken instanceof CookieLoginToken){
			CookieLoginToken token = (CookieLoginToken) authcToken;
            if (StringUtils.isBlank(token.getLoginCode())){
                return null;
            }
            User loginUser = UserUtils.getUserByLoginCode(token.getLoginCode());
    		if (loginUser == null || !User.DEL_FLAG_NORMAL.equals(loginUser.getDelFlag())){
    		    return null;
    	    }
    		return new SimpleAuthenticationInfo(new Principal(loginUser),loginUser.getPassword(),getName());
        }
        return null;
	}
	
	public SystemService getSystemService() {
		if (systemService == null){
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}

}
