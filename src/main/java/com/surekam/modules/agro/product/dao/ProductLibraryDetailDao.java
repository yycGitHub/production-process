package com.surekam.modules.agro.product.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.product.entity.ProductLibraryDetail;

/**
 * 品种库DAO接口
 * 
 * @author lb
 * @version 2019-04-10
 */
@Repository
public class ProductLibraryDetailDao extends BaseDao<ProductLibraryDetail> {

	public List<ProductLibraryDetail> findById(String id) {
		String sql = "select b.* from t_agro_product_library_tree a inner join t_agro_product_library_detail b on a.id = b.product_library_id where a.id=:p1  ";
		List<ProductLibraryDetail> ProductLibraryDetail = findBySql(sql, new Parameter(id), ProductLibraryDetail.class);
		return ProductLibraryDetail;
	}

	public List<ProductLibraryDetail> findByProductLibraryIdAndProductName(String productLibraryId, String productName) {
		String sql = "select * from t_agro_product_library_detail a where a.product_library_id =:p1 and a.product_name =:p2 ";
		List<ProductLibraryDetail> ProductLibraryDetail = findBySql(sql, new Parameter(productLibraryId, productName), ProductLibraryDetail.class);
		return ProductLibraryDetail;
	}
}
