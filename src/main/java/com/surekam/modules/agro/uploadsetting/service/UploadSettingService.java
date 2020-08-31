package com.surekam.modules.agro.uploadsetting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.uploadsetting.dao.UploadSettingDao;
import com.surekam.modules.agro.uploadsetting.entity.UploadSetting;

/**
 * 上传审核记录表Service
 * 
 * @author tangjun
 * @version 2019-07-12
 */
@Component
@Transactional(readOnly = true)
public class UploadSettingService extends BaseService {

	@Autowired
	private UploadSettingDao uploadSettingDao;

	public UploadSetting get(String id) {
		return uploadSettingDao.get(id);
	}

	@Transactional(readOnly = false)
	public void save(UploadSetting uploadSetting) {
		uploadSettingDao.save(uploadSetting);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		uploadSettingDao.deleteById(id);
	}

}
