package com.surekam.modules.tracewxtoken.service;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.tracewxtoken.entity.TraceWxToken;
import com.surekam.modules.tracewxtoken.dao.TraceWxTokenDao;

/**
 * 保存微信接口凭证信息Service
 * @author liw
 * @version 2019-02-21
 */
@Component
@Transactional(readOnly = true)
public class TraceWxTokenService extends BaseService {

	@Autowired
	private TraceWxTokenDao traceWxTokenDao;
	
	public TraceWxToken get(String id) {
		return traceWxTokenDao.get(id);
	}
	
	public TraceWxToken findTraceWxToken(String openId) {
		DetachedCriteria dc = traceWxTokenDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(openId)){
			dc.add(Restrictions.eq("openid", openId));
		}
		List<TraceWxToken> list = traceWxTokenDao.find(dc);
		if(list.size()>0){
			return list.get(0);
		}else{
			return new TraceWxToken();
		}
	}
	
	@Transactional(readOnly = false)
	public void save(TraceWxToken traceWxToken) {
		traceWxTokenDao.save(traceWxToken);
	}
	
}
