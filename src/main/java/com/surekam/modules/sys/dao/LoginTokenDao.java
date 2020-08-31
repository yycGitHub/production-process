package com.surekam.modules.sys.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.sys.entity.LoginToken;

@Repository
public class LoginTokenDao extends BaseDao<LoginToken> {
	public LoginToken findByToken(String token) {
		List<LoginToken> tokenList = find("from LoginToken a where a.token=:p1", new Parameter(token));
		if (tokenList.size() > 0) {
			return tokenList.get(0);
		} else {
			return new LoginToken();
		}
	}

	public void deleteLoginTokenByUserId(String userId) {
		this.update("delete LoginToken a where a.userId =:p1", new Parameter(userId));
	}
	
	public void deleteLoginTokenByToken(String token) {
		this.update("delete LoginToken a where a.token =:p1", new Parameter(token));
	}
}
