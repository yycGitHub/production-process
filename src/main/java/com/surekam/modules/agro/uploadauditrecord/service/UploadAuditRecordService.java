package com.surekam.modules.agro.uploadauditrecord.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.uploadauditrecord.dao.UploadAuditRecordDao;
import com.surekam.modules.agro.uploadauditrecord.entity.UploadAuditRecord;

/**
 * 上传审核记录表Service
 * 
 * @author tangjun
 * @version 2019-07-12
 */
@Component
@Transactional(readOnly = true)
public class UploadAuditRecordService extends BaseService {

	@Autowired
	private UploadAuditRecordDao uploadAuditRecordDao;

	public UploadAuditRecord get(String id) {
		return uploadAuditRecordDao.get(id);
	}

	@Transactional(readOnly = false)
	public void save(UploadAuditRecord uploadAuditRecord) {
		uploadAuditRecordDao.save(uploadAuditRecord);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		uploadAuditRecordDao.deleteById(id);
	}
	
	public List<UploadAuditRecord> findByStandardTaskItemsArgsValueId(String id) {
		List<UploadAuditRecord> findByStandardTaskItemsArgsValueId = uploadAuditRecordDao.findByStandardTaskItemsArgsValueId(id);
		return findByStandardTaskItemsArgsValueId;
	}

}
