package com.surekam.modules.agro.materials.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.materials.entity.MaterialRelation;

/**
 * 农资与农产品类型关系表DAO接口
 * 
 * @author tangjun
 * @version 2019-04-22
 */
@Repository
public class MaterialRelationDao extends BaseDao<MaterialRelation> {

	public int delete(String materialId) {
		return updateBySql("delete from t_agro_material_relation where material_id =:p1", new Parameter(materialId));
	}

	public List<MaterialRelation> getMaterialRelationList(String materialId) {
		String sql = "select * from t_agro_material_relation a where a.material_id ='" + materialId + "'";
		List<MaterialRelation> list = findBySql(sql, null, MaterialRelation.class);
		return list;
	}

}
