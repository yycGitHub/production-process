package com.surekam.modules.agro.application.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.application.dao.OperationalRecordsDao;
import com.surekam.modules.agro.application.entity.OperationalRecords;

@Component
@Transactional(readOnly = true)
public class OperationalRecordsService extends BaseService{
	
	@Autowired
	private OperationalRecordsDao operationalRecordsDao;
	
	@Transactional(readOnly = false)
	public void save(OperationalRecords operationalRecords) {
		operationalRecordsDao.save(operationalRecords);
	}
	
	
	public List<OperationalRecords> getList(String id) {
		DetachedCriteria dc = operationalRecordsDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(id)) {
			dc.add(Restrictions.eq("arid", id));
		}
		dc.addOrder(Order.asc("createTime"));
		return operationalRecordsDao.find(dc);
	}

}
