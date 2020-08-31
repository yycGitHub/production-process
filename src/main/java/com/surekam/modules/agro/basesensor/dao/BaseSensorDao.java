package com.surekam.modules.agro.basesensor.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.basesensor.entity.BaseSensor;

/**
 * 基地传感器表DAO接口
 * 
 * @author tangjun
 * @version 2019-06-12
 */
@Repository
public class BaseSensorDao extends BaseDao<BaseSensor> {

	public List<BaseSensor> findByBaseIdAndOfficeId(String baseId, String officeId) {
		String sql = "select * from t_agro_base_sensor a where a.base_id =:p1 and a.office_id =:p2";
		List<BaseSensor> findBySql = findBySql(sql, new Parameter(baseId, officeId), BaseSensor.class);
		return findBySql;
	}

	public List<BaseSensor> findByBaseId(String baseId) {
		String sql = "select * from t_agro_base_sensor a where a.base_id =:p1";
		List<BaseSensor> findBySql = findBySql(sql, new Parameter(baseId), BaseSensor.class);
		return findBySql;
	}

	public int deleteBaseSensor(String baseId, String officeId) {
		return updateBySql("delete from t_agro_base_sensor where base_id =:p1 and office_id =:p2", new Parameter(baseId, officeId));
	}

}
