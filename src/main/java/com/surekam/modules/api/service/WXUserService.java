package com.surekam.modules.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.modules.api.dao.WXUserDao;
import com.surekam.modules.api.entity.WXUser;

@Component
@Transactional(readOnly = true)
public class WXUserService extends BaseService {
	
	@Autowired
	private WXUserDao wxuserdao;
	
	@Transactional(readOnly = false)
	public void save(WXUser wxuser) {
		wxuserdao.save(wxuser);
	}
	
	public boolean find(String openid){
		List<Object> list = wxuserdao.findBySql("select * from sys_wx_user a where a.OPEN_ID=:p1",new Parameter(openid));
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}

}
