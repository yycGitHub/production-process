package com.surekam.modules.agro.productlibraryquick.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.productlibraryquick.entity.ProductLibraryQuickValue;

/**
 * 品种快捷值表用于向溯源推送DAO接口
 * 
 * @author luoxw
 * @version 2019-07-24
 */
@Repository
public class ProductLibraryQuickValueDao extends BaseDao<ProductLibraryQuickValue> {

	public List<ProductLibraryQuickValue> findByQuickId(String quickId) {
		String sql = "select * from t_agro_product_library_quick_value a where a.product_quick_id =:p1 and a.states!='D'";
		List<ProductLibraryQuickValue> findBySql = findBySql(sql, new Parameter(quickId), ProductLibraryQuickValue.class);
		return findBySql;
	}

	public List<ProductLibraryQuickValue> findByList(String officeId, String productId, String systemStandardsId, String productName) {
		String sql = "select b.* from t_agro_product_library_quick a inner join t_agro_product_library_quick_value b on a.id = b.product_quick_id where a.office_id =:p1 and a.product_id =:p2 and a.system_standards_id =:p3 and b.product_name =:p4 and a.states <> 'D' and b.states <> 'D' ";
		List<ProductLibraryQuickValue> findBySql = findBySql(sql, new Parameter(officeId, productId, systemStandardsId, productName), ProductLibraryQuickValue.class);
		return findBySql;
	}
	
	
	public ProductLibraryQuickValue findByOfficeIdAndProductIdAndProductName(String officeId, String productId, String productName) {
		String sql = "select b.* from t_agro_product_library_quick a inner join t_agro_product_library_quick_value b on a.id = b.product_quick_id where a.office_id =:p1 and a.product_id =:p2 and b.product_name =:p3 and a.states <> 'D' and b.states <> 'D' ";
		List<ProductLibraryQuickValue> findBySql = findBySql(sql, new Parameter(officeId, productId, productName), ProductLibraryQuickValue.class);
		if(!findBySql.isEmpty()) {
			return findBySql.get(0);
		}
		return null;
	}
	
	
	public List<ProductLibraryQuickValue> findByList(String officeId, String productId) {
		String sql = "select b.* from t_agro_product_library_quick a inner join t_agro_product_library_quick_value b on a.id = b.product_quick_id where a.office_id =:p1 and a.product_id =:p2 and a.states <> 'D' and b.states <> 'D' ";
		List<ProductLibraryQuickValue> findBySql = findBySql(sql, new Parameter(officeId, productId), ProductLibraryQuickValue.class);
		return findBySql;
	}

}
