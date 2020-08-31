package com.surekam.modules.agro.sensorthreshold.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.sensorthreshold.entity.CopyOfSensorThreshold;
import com.surekam.modules.agro.sensorthreshold.entity.SensorThreshold;

/**
 * 传感器阈值表DAO接口
 * 
 * @author tangjun
 * @version 2019-05-10
 */
@Repository
public class SensorThresholdDao extends BaseDao<SensorThreshold> {

	public List<SensorThreshold> findByGrowthCycleId(String growthCycleId) {
		String sql = "select * from t_agro_sensor_threshold a where a.growth_cycle_id = '" + growthCycleId + "' and a.states <> 'D'";
		List<SensorThreshold> find = findBySql(sql, null, SensorThreshold.class);
		return find;
	}
	
	public int delete(String growthCycleId) {
		return updateBySql("delete from t_agro_sensor_threshold where growth_cycle_id =:p1", new Parameter(growthCycleId));
	}

	public List<SensorThreshold> findBySensorThreshold(String label, String sysEntStandardId) {
		String sql = "select * from sys_dict a " + "inner join t_agro_sensor_threshold b on a.id = b.target_id "
				+ "inner join t_agro_product_growth_cycle c on b.growth_cycle_id = c.id "
				+ "where a.del_flag = '0' and a.type = 'sensor' and a.label =:p1 "
				+ "and b.states <> 'D' and c.states <> 'D' and c.sys_ent_standard_id =:p2 ";
		List<SensorThreshold> find = findBySql(sql, new Parameter(label, sysEntStandardId), SensorThreshold.class);
		return find;
	}
	// 阈值预警时用
	public List<CopyOfSensorThreshold> findBySensorThreshold() {
		String sql = "select b.id,c.sys_ent_standard_id,b.target_id,"
				+ "b.growth_cycle_id,a.label,b.max_value,b.min_value,"
				+ "b.create_time,b.create_user_id,b.update_time,b.update_user_id,b.states"
				+ " from sys_dict a " + "inner join t_agro_sensor_threshold b on a.id = b.target_id "
				+ "inner join t_agro_product_growth_cycle c on b.growth_cycle_id = c.id "
				+ "where a.del_flag = '0' and a.type = 'sensor' "
				+ "and b.states <> 'D' and c.states <> 'D'";
		List<CopyOfSensorThreshold> find = findBySql(sql, null, CopyOfSensorThreshold.class);
		return find;
	}
}
