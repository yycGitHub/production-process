package com.surekam.modules.agro.systementerprisestandards.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.modules.agro.systementerprisestandards.entity.SystemEnterpriseStandards;

/**
 * 标准库(系统标准+企业标准)DAO接口
 * 
 * @author liwei
 * @version 2019-04-23
 */
@Repository
public class SystemEnterpriseStandardsDao extends BaseDao<SystemEnterpriseStandards> {

	public List<SystemEnterpriseStandards> findByStandardNameAndVersionName(String standardName, String versionName) {
		String sql = "select * from t_agro_system_enterprise_standards a where a.standard_name = '" + standardName + "' and a.version_name = '" + versionName + "' and a.states <> 'D'";
		return findBySql(sql, null, SystemEnterpriseStandards.class);
	}

	public List<SystemEnterpriseStandards> findByStandardName(String standardName) {
		String sql = "select * from t_agro_system_enterprise_standards a where a.standard_name = '" + standardName + "' and a.states <> 'D' order by a.version_name desc";
		return findBySql(sql, null, SystemEnterpriseStandards.class);
	}

	public List<SystemEnterpriseStandards> findByProductId(String productId) {
		String sql = "select * from t_agro_system_enterprise_standards a where a.product_id = '" + productId + "'  and a.states <> 'D'";
		return findBySql(sql, null, SystemEnterpriseStandards.class);
	}

	public List<SystemEnterpriseStandards> findByModelId(String productionModelId) {
		String sql = "select * from t_agro_system_enterprise_standards a where a.production_model_id = '" + productionModelId + "'  and a.states <> 'D'";
		return findBySql(sql, null, SystemEnterpriseStandards.class);
	}

	public List<SystemEnterpriseStandards> findByProductIdUnionAll(String productId,String officeId ) {
		String sql = "select distinct  a.* from t_agro_system_enterprise_standards a inner join t_agro_product_library_relation b on a.product_id = b.parent_id " + "where (b.parent_id = '" + productId + "' or b.parents_ids like '%," + productId + ",%') and (b.office_id ='" + officeId + "' or b.office_ids like '%," + officeId + ",%') and a.states <> 'D' " 
				+ "union all "
				+ "select distinct  a.* from t_agro_system_enterprise_standards a inner join t_agro_product_library_relation b on a.product_id = b.parent_id " + "where (b.parent_id not in ('" + productId + "') or b.parents_ids not like '%," + productId + ",%') and (b.office_id not in ('" + officeId + "') or b.office_ids not like '%," + officeId + ",%') and a.states <> 'D' ";
		return findBySql(sql, null, SystemEnterpriseStandards.class);
	}
	
}
