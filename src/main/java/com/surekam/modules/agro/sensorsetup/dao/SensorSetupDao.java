package com.surekam.modules.agro.sensorsetup.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.sensorsetup.entity.SensorSetup;
import com.surekam.modules.api.entity.BatchGatewayModle;
import com.surekam.modules.sys.entity.User;

/**
 * 传感器管理DAO接口
 * 
 * @author luoxw
 * @version 2019-04-22
 */
@Repository
public class SensorSetupDao extends BaseDao<SensorSetup> {

	public List<SensorSetup> findByOfficeId(String officeId) {
		String sql = "select * from t_agro_sensor_setup a where a.office_id =:p1 and a.states <> 'D' ";
		List<SensorSetup> find = findBySql(sql, new Parameter(officeId), SensorSetup.class);
		return find;
	}

	public SensorSetup findBySubId(String subId) {
		String sql = "select * from t_agro_sensor_setup a where a.sub_id =:p1 ";
		List<SensorSetup> find = findBySql(sql, new Parameter(subId), SensorSetup.class);
		if (!find.isEmpty()) {
			return find.get(0);
		}
		return null;
	}

	public List<String> findByGatewayIdList(String batchId, String leaseTime) {
		String sql = "select distinct d.gateway_id as gatewayId from t_agro_base_tree a "
				+ "inner join sys_office b on a.office_id = b.id "
				+ "inner join t_agro_base_sensor c on a.id = c.base_id and b.id = c.office_id "
				+ "inner join t_agro_sensor_setup d on c.sensor_id = d.id "
				+ "where a.states <> 'D' and b.DEL_FLAG = '0' and d.states <> 'D' and a.id =:p1 "
				+ "and ((d.lease_status ='1' and d.lease_time >:p2) OR d.lease_status ='0')";
		List<String> stringList = findBySql(sql, new Parameter(batchId, leaseTime));
		return stringList;
	}
	public List<BatchGatewayModle> findGatewayIdList(String leaseTime) {
		String sql = "select "
				+ "batch.id as id, "
				+ "batch.id as batch_id, "
				+ "batch.product_id as product_id, "
				+ "product.product_category_name as product_name, "
				+ "batch.standard_id as standard_id, "
				+ "batch.base_id as base_id, "
				+ "a.name as base_name, "
				+ "a.office_id as office_id, "
				+ "b.name as office_name, "
				+ "batch.batch_code as batch_code, "
				+ "batch.sms_status as sms_status, "
				+ "batch.mail_status as mail_status, "
				+ "group_concat(d.gateway_id) as gateway_id "
				+ "from t_agro_production_batch batch "
				+ "inner join t_agro_product_library_tree product on batch.product_id = product.id "
				+ "inner join t_agro_base_tree a on a.id = batch.base_id "
				+ "inner join sys_office b on a.office_id = b.id "
				+ "inner join t_agro_base_sensor c on a.id = c.base_id and b.id = c.office_id "
				+ "inner join t_agro_sensor_setup d on c.sensor_id = d.id "
				+ "where a.states <> 'D' and b.DEL_FLAG = '0' and d.states <> 'D' and batch.status='1' "
				+ "and ((d.lease_status ='1' and d.lease_time >:p1) OR d.lease_status ='0') GROUP BY batch.id,b.name";
		List<BatchGatewayModle> stringList = findBySql(sql, new Parameter(leaseTime),BatchGatewayModle.class);
		return stringList;
	}
	
	public Page<SensorSetup> find(int pageNo, int pageSize, String sensorName, String officeId, User user) {
		Page<SensorSetup> page = new Page<SensorSetup>(pageNo, pageSize);
		String sql = "select a.* from t_agro_sensor_setup a inner join sys_office b on a.office_id = b.id where a.states <> 'D' and b.DEL_FLAG = '0' ";
		if (StringUtils.isNotBlank(sensorName)) {
			sql += "and a.sensor_name like '%" + sensorName + "%' ";
		}
		if (StringUtils.isNotBlank(officeId)) {
			sql += "and (b.id ='" + officeId + "' or b.parent_ids like '%," + officeId + ",%' ) ";
		} else {
			sql += "and (b.id ='" + user.getOffice().getId() + "' or b.parent_ids like '%," + user.getOffice().getId() + ",%' ) ";
		}
		sql += "order by create_time desc ";
		Page<SensorSetup> findPage = findBySql(page, sql, SensorSetup.class);
		return findPage;
	}
}
