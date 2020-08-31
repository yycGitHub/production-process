package com.surekam.modules.agro.sensorthreshold.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.sensorthreshold.dao.SensorThresholdDao;
import com.surekam.modules.agro.sensorthreshold.entity.SensorThreshold;
import com.surekam.modules.agro.systementerprisestandards.entity.SystemEnterpriseStandards;
import com.surekam.modules.api.dto.req.SensorThresholdReq;
import com.surekam.modules.sys.entity.User;

/**
 * 传感器阈值表Service
 * 
 * @author tangjun
 * @version 2019-05-10
 */
@Component
@Transactional(readOnly = true)
public class SensorThresholdService extends BaseService {

	@Autowired
	private SensorThresholdDao sensorThresholdDao;

	public SensorThreshold get(String id) {
		return sensorThresholdDao.get(id);
	}

	public Page<SensorThreshold> find(Page<SensorThreshold> page, SensorThreshold sensorThreshold) {
		DetachedCriteria dc = sensorThresholdDao.createDetachedCriteria();
		dc.add(Restrictions.eq(SensorThreshold.FIELD_DEL_FLAG, SensorThreshold.DEL_FLAG_NORMAL));
		return sensorThresholdDao.find(page, dc);
	}

	/**
	 * 
	 * Title: savaSensorThreshold Description: 新增或修改传感器
	 * 
	 * @param req
	 *            传感器请求参数
	 * @param user
	 *            用户信息
	 * @param growthCycleId
	 *            生长周期ID
	 */
	@Transactional(readOnly = false)
	public void savaSensorThreshold(SensorThresholdReq req, User user, String growthCycleId) {
		sensorThresholdDao.delete(growthCycleId);
		if (!"0".equals(req.getMaxValue()) || !"0".equals(req.getMinValue())) {
			SensorThreshold stPojo = new SensorThreshold();
			stPojo.setTargetId(req.getTargetId());
			stPojo.setGrowthCycleId(growthCycleId);
			stPojo.setMaxValue(req.getMaxValue());
			stPojo.setMinValue(req.getMinValue());
			stPojo.setCreateTime(new Date());
			stPojo.setCreateUserId(user.getId());
			stPojo.setStates(SensorThreshold.STATE_FLAG_ADD);
			sensorThresholdDao.save(stPojo);
		}
	}

	/**
	 * Title: findByGrowthCycleId Description: 根据生长周期ID 查询传感器
	 * 
	 * @param growthCycleId
	 *            生长周期DI
	 * @return
	 */
	public List<SensorThreshold> findByGrowthCycleId(String growthCycleId) {
		List<SensorThreshold> findByGrowthCycleId = sensorThresholdDao.findByGrowthCycleId(growthCycleId);
		return findByGrowthCycleId;
	}

	/**
	 * Title: delete Description: 根据生长周期 删除传感器
	 * 
	 * @param id
	 *            生长周期
	 * @param user 
	 */
	@Transactional(readOnly = false)
	public void delete(String id, User user) {
		List<SensorThreshold> findByGrowthCycleId = sensorThresholdDao.findByGrowthCycleId(id);
		for (SensorThreshold sensorThreshold : findByGrowthCycleId) {
			sensorThreshold.setUpdateTime(new Date());
			sensorThreshold.setUpdateUserId(user.getId());
			sensorThreshold.setStates(SensorThreshold.STATE_FLAG_DEL);
			sensorThresholdDao.save(sensorThreshold);
		}
	}

}
