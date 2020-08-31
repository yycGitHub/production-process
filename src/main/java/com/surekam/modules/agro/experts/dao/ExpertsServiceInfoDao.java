package com.surekam.modules.agro.experts.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.modules.agro.experts.entity.ExpertServiceInfo;

/**
 * 专家服务信息DAO接口
 * @author xy
 * @version 2019-04-28
 */
@Repository
public class ExpertsServiceInfoDao extends BaseDao<ExpertServiceInfo> {
	//根据月份分组获取数据
	public List<Map<String,Object>> getMonthCount(String expertId, String time ,String companyId) {
		String sql = "SELECT DATE_FORMAT(service_time,'%m') as 'month', COUNT(*) as 'count' FROM t_agro_experts_service "
				+ "WHERE states != 'D' "
				+ "AND expert_id LIKE '"+expertId+"' ";
		if(StringUtils.isNotBlank(time)){
			sql += "AND DATE_FORMAT(service_time,'%Y') LIKE '"+time+"' ";
		}
		if(StringUtils.isNotBlank(companyId)){
			sql += "AND service_company_id in (SELECT t.id FROM sys_office t WHERE (t.PARENT_IDS like '%,"+companyId+",%' OR t.id like '"+companyId+"')) ";
			
		}
		sql+="GROUP BY DATE_FORMAT(service_time,'%m')";
		return findBySql(sql,null,Map.class);
	}
	
}
