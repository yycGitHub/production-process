package com.surekam.modules.agro.productlibraryrelation.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.productlibraryrelation.entity.ProductLibraryRelation;

/**
 * 种养殖种类树形关系表DAO接口
 * 
 * @author tangjun
 * @version 2019-08-28
 */
@Repository
public class ProductLibraryRelationDao extends BaseDao<ProductLibraryRelation> {

	public int delete(String parentId) {
		return updateBySql("delete from t_agro_product_library_relation where parent_id =:p1", new Parameter(parentId));
	}

	public int deleteByOfficeId(String officeId, String productId) {
		return updateBySql("delete from t_agro_product_library_relation where office_id =:p1 and parent_id = :p2", new Parameter(officeId, productId));
	}

	public List<ProductLibraryRelation> findByOfficeIdAndProductId(String officeId, String productId) {
		String sql = "select * from t_agro_product_library_relation a where a.office_id =:p1 and a.parent_id = :p2";
		List<ProductLibraryRelation> findBySql = findBySql(sql, new Parameter(officeId, productId), ProductLibraryRelation.class);
		return findBySql;
	}

	public List<Map<String, String>> findByParentId(String parentId) {
		String sql = "select b.id as id, b.name as name from t_agro_product_library_relation a inner join sys_office b on a.office_id = b.id where a.parent_id =:p1";
		List<Map<String, String>> findBySql = findBySql(sql, new Parameter(parentId), Map.class);
		return findBySql;
	}

	public List<ProductLibraryRelation> findByLikeOfficeId(String officeId, String officeStr) {
		String sql = "select * from t_agro_product_library_relation a where (a.office_id ='" + officeId + "' or a.office_ids like '%," + officeStr + ",%' )";
		List<ProductLibraryRelation> findBySql = findBySql(sql, null, ProductLibraryRelation.class);
		return findBySql;
	}
	
	public List<ProductLibraryRelation> distinct(String officeId, String officeStr) {
		String sql = "select distinct a.* from t_agro_product_library_relation a where (a.office_id ='" + officeId + "' or a.office_ids like '%," + officeStr + ",%' )";
		List<ProductLibraryRelation> findBySql = findBySql(sql, null, ProductLibraryRelation.class);
		return findBySql;
	}

	public List<ProductLibraryRelation> findByLikeOfficeId(String officeId) {
		String sql = "select * from t_agro_product_library_relation a where a.office_id ='" + officeId + "'";
		List<ProductLibraryRelation> findBySql = findBySql(sql, null, ProductLibraryRelation.class);
		return findBySql;
	}

	public List<ProductLibraryRelation> findByInOfficeId(String officeId) {
		String sql = "select * from t_agro_product_library_relation a where a.office_id in (" + officeId + ")";
		List<ProductLibraryRelation> findBySql = findBySql(sql, null, ProductLibraryRelation.class);
		return findBySql;
	}

	public List<ProductLibraryRelation> findByLikeOffice(String officeId, String officeStr) {
		String sql = "select * from t_agro_product_library_relation a inner join t_agro_product_library_tree b on a.parent_id = b.id where (a.office_id ='" + officeId + "' or a.office_ids like '%," + officeStr + ",%' ) and b.states <> 'D' ";
		List<ProductLibraryRelation> findBySql = findBySql(sql, null, ProductLibraryRelation.class);
		return findBySql;
	}

	public List<ProductLibraryRelation> findByParentid(String ParentId) {
		String sql = "select * from t_agro_product_library_relation a where a.parent_id =:p1 ";
		List<ProductLibraryRelation> findBySql = findBySql(sql, new Parameter(ParentId), ProductLibraryRelation.class);
		return findBySql;
	}
}
