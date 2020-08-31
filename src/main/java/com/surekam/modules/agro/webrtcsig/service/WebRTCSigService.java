package com.surekam.modules.agro.webrtcsig.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.webrtcsig.dao.WebRTCSigDao;
import com.surekam.modules.agro.webrtcsig.entity.WebRTCSig;

/**
 * 视频聊天人员信息Service
 * @author xy
 * @version 2019-06-04
 */
@Component
@Transactional(readOnly = true)
public class WebRTCSigService extends BaseService {

	@Autowired
	private WebRTCSigDao webRTCSigDao;
	
	public WebRTCSig get(String id) {
		return webRTCSigDao.get(id);
	}
	

	public Page<WebRTCSig> findUser(Page<WebRTCSig> page,String  loginName,String name,String companyId) {
		String sql = "SELECT * FROM v_agro_webRTCSig WHERE 1 = 1 ";
		if (companyId != null && StringUtils.isNotBlank(companyId)){
			sql += " and (officeId = '"+ companyId +"' or parentId = '"+ companyId +"' or parentIds like '%,"+ companyId +",%') ";
		}
		if (StringUtils.isNotEmpty(loginName)){
			sql += " and loginName like '%"+ loginName +"%' ";
		}
		if (StringUtils.isNotEmpty(name)){
			sql += " and name like '%"+ name +"%' ";
		}	
        Page<WebRTCSig> users = webRTCSigDao.findBySql(page, sql,WebRTCSig.class);
		return users;
	}
	
	
	
	
	
	
	
	
	
	public Page<WebRTCSig> find(Page<WebRTCSig> page, WebRTCSig webRTCSig) {
		DetachedCriteria dc = webRTCSigDao.createDetachedCriteria();
		dc.add(Restrictions.eq(WebRTCSig.FIELD_DEL_FLAG, WebRTCSig.DEL_FLAG_NORMAL));
		return webRTCSigDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(WebRTCSig webRTCSig) {
		webRTCSigDao.save(webRTCSig);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		webRTCSigDao.deleteById(id);
	}
	
}
