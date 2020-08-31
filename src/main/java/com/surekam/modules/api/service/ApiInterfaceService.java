package com.surekam.modules.api.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.api.dao.ApiInterfaceDao;
import com.surekam.modules.api.dao.ApiUserInterfaceDao;
import com.surekam.modules.api.entity.ApiInterface;
import com.surekam.modules.api.entity.ApiUserInterface;

/**
 * 接口管理Service
 * @author lb
 * @version 2018-03-15
 */
@Component
@Transactional(readOnly = true)
public class ApiInterfaceService extends BaseService {

	@Autowired
	private ApiInterfaceDao apiInterfaceDao;
	@Autowired
	private ApiUserInterfaceDao apiUserInterfaceDao;
	
	public ApiInterface get(String id) {
		return apiInterfaceDao.get(id);
	}
	
	public Page<ApiInterface> find(Page<ApiInterface> page, ApiInterface apiInterface) {
		DetachedCriteria dc = apiInterfaceDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(apiInterface.getName())) {
			dc.add(Restrictions.like("name", apiInterface.getName(), MatchMode.ANYWHERE));
		}
		dc.add(Restrictions.eq(ApiInterface.FIELD_DEL_FLAG, ApiInterface.DEL_FLAG_NORMAL));
		return apiInterfaceDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ApiInterface apiInterface) {
		apiInterfaceDao.save(apiInterface);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		apiInterfaceDao.deleteById(id);
	}

	public List<ApiInterface> findAll(ApiInterface apiInterface) {
		DetachedCriteria dc = apiInterfaceDao.createDetachedCriteria();
		if(apiInterface!=null && StringUtils.isNotBlank(apiInterface.getName())){
			dc.add(Restrictions.like("name", apiInterface.getName(),MatchMode.ANYWHERE));
		}
		dc.add(Restrictions.eq(ApiInterface.FIELD_DEL_FLAG, ApiInterface.DEL_FLAG_NORMAL));
		return apiInterfaceDao.find(dc);
	}

	public List<String> find(String appId) {
		String hql = "select interfaceId from ApiUserInterface where delFlag =:p1 and appId=:p2";
		return apiUserInterfaceDao.find(hql,new Parameter(ApiUserInterface.DEL_FLAG_NORMAL,appId));
	}

	@Transactional(readOnly = false)
	public void setInterUserface(String appId, String[] apiInterfaceIds) {
		//删除旧数据
		String hql = "update ApiUserInterface set delFlag =:p1 where appId=:p2";
		apiUserInterfaceDao.update(hql, new Parameter(ApiUserInterface.DEL_FLAG_DELETE,appId));
		if (apiInterfaceIds!=null) {
			for (int i = 0; i < apiInterfaceIds.length; i++) {
				ApiUserInterface apiUserInterface = new ApiUserInterface();
				apiUserInterface.setAppId(appId);
				apiUserInterface.setInterfaceId(apiInterfaceIds[i]);
				apiUserInterfaceDao.save(apiUserInterface);
			}
		}
	}
}
