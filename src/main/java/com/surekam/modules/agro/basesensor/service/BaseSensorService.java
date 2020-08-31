package com.surekam.modules.agro.basesensor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.basesensor.dao.BaseSensorDao;

/**
 * 基地传感器表Service
 * 
 * @author tangjun
 * @version 2019-06-12
 */
@Component
@Transactional(readOnly = true)
public class BaseSensorService extends BaseService {

	@Autowired
	private BaseSensorDao baseSensorDao;

}
