package com.surekam.modules.agro.application.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.application.dao.DelegationRecordDao;
import com.surekam.modules.agro.application.entity.DelegationRecord;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.sys.entity.User;

/**
 * 平台委派记录表Service
 * @author xy
 * @version 2019-06-25
 */
@Component
@Transactional(readOnly = true)
public class DelegationRecordService extends BaseService {

	@Autowired
	private DelegationRecordDao delegationRecordDao;
	
	public DelegationRecord get(String id) {
		return delegationRecordDao.get(id);
	}
	
	public Page<DelegationRecord> find(Page<DelegationRecord> page, String applicationId) {
		DetachedCriteria dc = delegationRecordDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(applicationId)) {
			dc.add(Restrictions.eq("applicationId", applicationId));
		}
		dc.addOrder(Order.desc("createTime"));
		return delegationRecordDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(DelegationRecord delegationRecord) {
		delegationRecordDao.flush();
		delegationRecordDao.save(delegationRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		delegationRecordDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void deleteByXGXTId(String id) {
		delegationRecordDao.deleteByXGXTId(id);
	}
	
	public String getAllCount(String taskStatus,Experts expert) {
		StringBuffer sql = new StringBuffer(800);
		sql.append("select * from t_agro_delegation_record WHERE states<>'D' and task_status ='"+ taskStatus +"' and expert_id = '"+ expert.getId() +"' ");
		List<DelegationRecord> list = delegationRecordDao.findBySql2(sql.toString(), new Parameter(), DelegationRecord.class);
		return String.valueOf(list.size()) ;
	}
	
}
