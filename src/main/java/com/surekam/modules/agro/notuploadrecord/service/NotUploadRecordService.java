package com.surekam.modules.agro.notuploadrecord.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.notuploadrecord.entity.NotUploadRecord;
import com.surekam.modules.agro.notuploadrecord.dao.NotUploadRecordDao;

/**
 * 不上传记录表Service
 * 
 * @author tangjun
 * @version 2019-07-12
 */
@Component
@Transactional(readOnly = true)
public class NotUploadRecordService extends BaseService {

	@Autowired
	private NotUploadRecordDao notUploadRecordDao;

	public NotUploadRecord get(String id) {
		return notUploadRecordDao.get(id);
	}

	@Transactional(readOnly = false)
	public void save(NotUploadRecord notUploadRecord) {
		notUploadRecordDao.save(notUploadRecord);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		notUploadRecordDao.deleteById(id);
	}

}
