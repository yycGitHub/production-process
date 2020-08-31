package com.surekam.modules.agro.productionmodel.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.productionmodel.entity.ProductionModel;

/**
 * 生产模式DAO接口
 * 
 * @author tangjun
 * @version 2019-05-27
 */
@Repository
public class ProductionModelDao extends BaseDao<ProductionModel> {

	public List<ProductionModel> findByProductIdList(String productId) {
		String sql = "select a.* from t_agro_production_model a where a.product_id =:p1 and a.states <> 'D' order by create_time desc";
		List<ProductionModel> findBySql = findBySql(sql, new Parameter(productId), ProductionModel.class);
		return findBySql;
	}
	
	public List<ProductionModel> findByProductIdAndStates(String productId, String states) {
		String sql = "select a.* from t_agro_production_model a where a.product_id =:p1 and a.states =:p2 ";
		List<ProductionModel> findBySql = findBySql(sql, new Parameter(productId, states), ProductionModel.class);
		return findBySql;
	}

	public int delete(String productId) {
		return updateBySql("delete from t_agro_production_model where product_id =:p1", new Parameter(productId));
	}

	public List<Object> findProductIdList() {
		String sql = "SELECT"
				+ " t.id"
				+ " FROM"
				+ " t_agro_product_library_tree t"
				+ " LEFT JOIN t_agro_production_model m ON t.id = m.product_id"
				+ " WHERE"
				+ " t.is_product_category = '2'"
				+ " AND t.states <> 'D'"
				+ " GROUP BY"
				+ " t.id"
				+ " HAVING"
				+ " COUNT(m.id) < 1";
		List<Object> findBySql = findBySql(sql, null);
		return findBySql;
	}
	
	public ProductionModel getProductionModel() {
		String sql ="select * from t_agro_production_model a inner join t_agro_system_enterprise_standards b on a.id = b.production_model_id where a.states <> 'D' and  b.states <> 'D' ";
		List<ProductionModel> findBySql = findBySql(sql, null, ProductionModel.class);
		if (!findBySql.isEmpty()) {
			return findBySql.get(0);
		} else {
			return null;
		}
		
		
	}
}
