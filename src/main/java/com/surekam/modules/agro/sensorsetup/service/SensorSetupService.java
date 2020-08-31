package com.surekam.modules.agro.sensorsetup.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.basesensor.dao.BaseSensorDao;
import com.surekam.modules.agro.basesensor.entity.BaseSensor;
import com.surekam.modules.agro.sensorsetup.dao.SensorSetupDao;
import com.surekam.modules.agro.sensorsetup.entity.SensorSetup;
import com.surekam.modules.api.dto.req.SensorSetupReq;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;

/**
 * 传感器管理Service
 * 
 * @author luoxw
 * @version 2019-04-22
 */
@Component
@Transactional(readOnly = true)
public class SensorSetupService extends BaseService {

	@Autowired
	private SensorSetupDao sensorSetupDao;
	
	@Autowired
	private BaseSensorDao baseSensorDao;
	
	@Autowired
	private OfficeDao officeDao;

	public SensorSetup get(String id) {
		SensorSetup sensorSetup = sensorSetupDao.get(id);
		return sensorSetup;
	}
	
	public Page<SensorSetup> fetchFind(Page<SensorSetup> page, String baseId, String isOffice, String officeId, User user) {
		DetachedCriteria dc = sensorSetupDao.createDetachedCriteria();
		// 公司
		if (StringUtils.isNotBlank(isOffice) && "0".equals(isOffice)) {
			dc.add(Restrictions.eq("officeId", officeId));
		}
		// 基地
		if (StringUtils.isNotBlank(isOffice) && "1".equals(isOffice)) {
			List<BaseSensor> findByBaseId = baseSensorDao.findByBaseId(baseId);
			List<String> sensorIdList = new ArrayList<String>();
			if (!findByBaseId.isEmpty()) {
				for (BaseSensor baseSensor : findByBaseId) {
					sensorIdList.add(baseSensor.getSensorId());
				}
			} else {
				sensorIdList.add("查不到");
			}
			dc.add(Restrictions.in("id", sensorIdList));
		}
		dc.add(Restrictions.ne(SensorSetup.FIELD_DEL_FLAG_XGXT, SensorSetup.STATE_FLAG_DEL));
		dc.addOrder(Order.desc("createTime"));
		Page<SensorSetup> pageSensorSetup = sensorSetupDao.find(page, dc);
		List<SensorSetup> list = pageSensorSetup.getList();
		for (SensorSetup sensorSetup : list) {
			Office office = officeDao.get(sensorSetup.getOfficeId());
			sensorSetup.setOfficeName(office.getName());
		}
		return pageSensorSetup;
	}
	
	public Page<SensorSetup> find(Integer pageno, Integer pagesize, String sensorName, String isOffice, String officeId, User user) {
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<SensorSetup> pageSensorSetup = sensorSetupDao.find(pageNo, pageSize, sensorName, officeId, user);
		List<SensorSetup> list = pageSensorSetup.getList();
		for (SensorSetup sensorSetup : list) {
			Office office = officeDao.get(sensorSetup.getOfficeId());
			if (null != office) {
				sensorSetup.setOfficeName(office.getName());
			}
		}
		return pageSensorSetup;
	}

	@Transactional(readOnly = false)
	public void save(SensorSetup sensorSetup) {
		sensorSetupDao.save(sensorSetup);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		sensorSetupDao.deleteByXGXTId(id);
	}

	public boolean existName(SensorSetup sensorSetup) {
		List<Office> list = new ArrayList<Office>();
		// 如果id为空，则代表是新增，where条件不带id
		if (StringUtils.isBlank(sensorSetup.getId())) {
			list = sensorSetupDao.findBySql("select a.* from t_agro_sensor_setup a where a.sensor_name=:p1 and a.office_id=:p2 and a.states!='D' ", new Parameter(sensorSetup.getSensorName(), sensorSetup.getOfficeId()));
		} else {
			list = sensorSetupDao.findBySql("select a.* from t_agro_sensor_setup a where a.id <>:p1 and a.sensor_name=:p2 and a.office_id=:p3 and a.states!='D' ", new Parameter(sensorSetup.getId(), sensorSetup.getSensorName(), sensorSetup.getOfficeId()));
		}
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Title: getSensorSetupList Description: 获取公司下的传感器
	 * 
	 * @param officeId
	 *            公司ID
	 * @return
	 */
	public List<SensorSetup> findBySensorSetupList(String officeId) {
		List<SensorSetup> findByOfficeId = sensorSetupDao.findByOfficeId(officeId);
		return findByOfficeId;
	}

	/**
	 * Title: savaUnifiedAuthenticationSensor Description: 统一认证传感器新怎或修改接口
	 * 
	 * @param req
	 *            请求参数
	 * @return
	 */
	@Transactional(readOnly = false)
	public String savaUnifiedAuthenticationSensor(SensorSetupReq req) {
		SensorSetup findBySubId = sensorSetupDao.findBySubId(req.getSubId());
		Office office = officeDao.getByKuid(req.getOfficeId());
		if(office==null){
			return "notData";
		}
		if (null != findBySubId) {
			// 传感器名称
			if (!req.getSensorName().equals(findBySubId.getSensorName())) {
				findBySubId.setSensorName(req.getSensorName());
			}

			// 传感器设备号
			if (!req.getSensorSerialNumber().equals(findBySubId.getSensorSerialNumber())) {
				findBySubId.setSensorSerialNumber(req.getSensorSerialNumber());
			}

			// 描述
			if (!req.getSensorDiscription().equals(findBySubId.getSensorDiscription())) {
				findBySubId.setSensorDiscription(req.getSensorDiscription());
			}

			// 网址
			if (!req.getWebsite().equals(findBySubId.getWebsite())) {
				findBySubId.setWebsite(req.getWebsite());
			}

			// 传感器网关ID
			if (!req.getGatewayId().equals(findBySubId.getGatewayId())) {
				findBySubId.setGatewayId(req.getGatewayId());
			}

			// 租赁状态 0-长期 1-短期
			if (!req.getLeaseStatus().equals(findBySubId.getLeaseStatus())) {
				findBySubId.setLeaseStatus(req.getLeaseStatus());
			}

			// 租赁时间
			if (StringUtils.isNotBlank(req.getLeaseTime())) {
				findBySubId.setLeaseTime(req.getLeaseTime());
			} else {
				findBySubId.setLeaseTime(null);
			}
			findBySubId.setOfficeId(office.getId());
			findBySubId.setUpdateTime(new Date());
			findBySubId.setUpdateUserId("统一认证网关");
			findBySubId.setStates(SensorSetup.STATE_FLAG_UPDATE);
			sensorSetupDao.save(findBySubId);
		} else {
			SensorSetup sensorSetup = new SensorSetup();
			sensorSetup.setSubId(req.getSubId());
			// 传感器名称
			if (StringUtils.isNotBlank(req.getSensorName())) {
				sensorSetup.setSensorName(req.getSensorName());
			}

			// 传感器设备号
			if (StringUtils.isNotBlank(req.getSensorSerialNumber())) {
				sensorSetup.setSensorSerialNumber(req.getSensorSerialNumber());
			}

			// 描述
			if (StringUtils.isNotBlank(req.getSensorDiscription())) {
				sensorSetup.setSensorDiscription(req.getSensorDiscription());
			}

			// 网址
			if (StringUtils.isNotBlank(req.getWebsite())) {
				sensorSetup.setWebsite(req.getWebsite());
			}

			// 传感器网关ID
			if (StringUtils.isNotBlank(req.getGatewayId())) {
				sensorSetup.setGatewayId(req.getGatewayId());
			}

			// 租赁状态 0-长期 1-短期
			if (StringUtils.isNotBlank(req.getLeaseStatus())) {
				sensorSetup.setLeaseStatus(req.getLeaseStatus());
			}

			// 租赁时间
			if (StringUtils.isNotBlank(req.getLeaseTime())) {
				sensorSetup.setLeaseTime(req.getLeaseTime());
			}
			sensorSetup.setOfficeId(office.getId());
			sensorSetup.setCreateTime(new Date());
			sensorSetup.setCreateUserId("统一认证网关");
			sensorSetup.setStates(SensorSetup.STATE_FLAG_ADD);
			sensorSetupDao.save(sensorSetup);
		}
		return "Success";
	}

	/**
	 * Title: delUnifiedAuthenticationSensor Description: 统一认证传感器删除接口
	 * 
	 * @param subId
	 *            子ID
	 * @return
	 */
	public String delUnifiedAuthenticationSensor(String subId) {
		SensorSetup findBySubId = sensorSetupDao.findBySubId(subId);
		findBySubId.setStates(SensorSetup.STATE_FLAG_DEL);
		findBySubId.setUpdateUserId("统一认证网关");
		findBySubId.setUpdateTime(new Date());
		sensorSetupDao.save(findBySubId);
		return "Success";
	}
}
