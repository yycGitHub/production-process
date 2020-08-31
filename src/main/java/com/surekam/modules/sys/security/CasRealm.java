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
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.context.annotation.DependsOn;

import com.surekam.common.utils.SpringContextHolder;
import com.surekam.modules.sys.entity.Menu;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.utils.UserUtils;

/**
 * 实现用户与权限查询.
 */
@DependsOn({"userDao","roleDao","menuDao"})
public class CasRealm extends AuthorizingRealm {
	
	private SystemService systemService;

    public CasRealm() {
        //直接比对密文
    	setCredentialsMatcher(new AllowAllCredentialsMatcher());
        //使用自定义Token类
        setAuthenticationTokenClass(CasToken.class);
    }
    
	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        if(authcToken instanceof CasToken){
        	CasToken token = (CasToken) authcToken;
            User loginUser = getSystemService().getUserByLoginName(token.getUsername());
    		if (loginUser == null){
    		    User user = new User();
    		    user.setLoginName(token.getUsername());
    		    user.setName(token.getUsername());
    		    user.setPassword(SystemService.entryptPassword("ht123456"));
    		    user.setDelFlag(User.DEL_FLAG_NORMAL);
    		    user.setCompany(new Office("4095166d3c864f37866114e4dfc5daf5"));
    		    user.setOffice(new Office("4095166d3c864f37866114e4dfc5daf5"));
    		    try {
    		    	systemService.saveUser(user);
				} catch (Exception e) {
					e.printStackTrace();
				}
    		    loginUser = user;
    	    }
            if ("0".equals(loginUser.getDelFlag())==false){
                return null;
            }
    		return new SimpleAuthenticationInfo(new Principal(loginUser),loginUser.getPassword(),getName());
        }
        return null;
	}
	
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
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
			// 更新登录IP和时间
			getSystemService().updateUserLoginInfo(user.getId());
			return info;
		} else {
			return null;
		}
	}
	
	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清空所有关联认证
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	public SystemService getSystemService() {
		if (systemService == null){
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}
}
