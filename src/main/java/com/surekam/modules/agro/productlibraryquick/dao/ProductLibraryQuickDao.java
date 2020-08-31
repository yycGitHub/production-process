package com.surekam.modules.agro.productlibraryquick.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.productlibraryquick.entity.ProductLibraryQuick;

/**
 * 品种快捷表DAO接口
 * 
 * @author tangjun
 * @version 2019-06-03
 */
@Repository
public class ProductLibraryQuickDao extends BaseDao<ProductLibraryQuick> {

	public List<ProductLibraryQuick> findByOfficeIdAndClassificationIdAndSpeciesId(String officeId, String speciesId) {
		String sql = "select * from t_agro_product_library_quick a where a.office_id =:p1 and a.product_id =:p2 and a.states!='D'";
		List<ProductLibraryQuick> findBySql = findBySql(sql, new Parameter(officeId, speciesId), ProductLibraryQuick.class);
		return findBySql;
	}

	public List<ProductLibraryQuick> findByOfficeIdAndClassificationIdAndSpeciesId(String officeId, String speciesId, String standardsId) {
		String sql = "select * from t_agro_product_library_quick a where a.office_id =:p1 and a.product_id =:p2 and a.system_standards_id =:p3 and a.states!='D'";
		List<ProductLibraryQuick> findBySql = findBySql(sql, new Parameter(officeId, speciesId, standardsId), ProductLibraryQuick.class);
		return findBySql;
	}

	public List<ProductLibraryQuick> findByOfficeId(String officeId) {
		String sql = "select * from t_agro_product_library_quick a where a.office_id =:p1 and a.states!='D'";
		List<ProductLibraryQuick> findBySql = findBySql(sql, new Parameter(officeId), ProductLibraryQuick.class);
		return findBySql;
	}

	public int delete(String id) {
		return updateBySql("delete from t_agro_product_library_quick where id =:p1", new Parameter(id));
	}

	public List<Object> getVarietyDistribution(String officeId) {
		String sql = "SELECT t.product_category_name,IFNULL(COUNT(*),0) FROM t_agro_product_library_quick q"
				+ " LEFT JOIN sys_office o ON q.office_id=o.id"
				+ " LEFT JOIN t_agro_product_library_tree t ON t.id=q.product_id" + " WHERE (o.PARENT_IDS LIKE '%,"
				+ officeId + ",%' or o.id='" + officeId + "')" + " AND q.states <> 'D' GROUP BY t.product_category_name";
		List<Object> findBySql = findBySql(sql, null);
		return findBySql;
	}

}
