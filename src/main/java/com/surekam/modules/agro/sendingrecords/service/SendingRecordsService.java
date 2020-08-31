package com.surekam.modules.agro.sendingrecords.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.sendingrecords.entity.SendingRecords;
import com.surekam.modules.agro.sendingrecords.dao.SendingRecordsDao;

/**
 * 发送记录表(短信邮件等)Service
 * @author luoxw
 * @version 2019-07-02
 */
@Component
@Transactional(readOnly = true)
public class SendingRecordsService extends BaseService {

	@Autowired
	private SendingRecordsDao sendingRecordsDao;
	
	public SendingRecords get(String id) {
		return sendingRecordsDao.get(id);
	}
	
	public Page<SendingRecords> find(Page<SendingRecords> page, SendingRecords sendingRecords) {
		DetachedCriteria dc = sendingRecordsDao.createDetachedCriteria();
		dc.add(Restrictions.eq(SendingRecords.FIELD_DEL_FLAG, SendingRecords.DEL_FLAG_NORMAL));
		return sendingRecordsDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(SendingRecords sendingRecords) {
		sendingRecordsDao.save(sendingRecords);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		sendingRecordsDao.deleteById(id);
	}
	
}
