package com.surekam.modules.agro.expertsprofessionalfieldrelation.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.expertsprofessionalfieldrelation.entity.ExpertsProfessionalfieldRelation;

/**
 * 专家与专业领域关系表DAO接口
 * 
 * @author tangjun
 * @version 2019-04-19
 */
@Repository
public class ExpertsProfessionalfieldRelationDao extends BaseDao<ExpertsProfessionalfieldRelation> {

	public List<ExpertsProfessionalfieldRelation> getExpertsProfessionalfieldRelation(String expertId) {
		String sql = "select * from t_agro_experts_professionalfield_relation a where a.experts_id = '" + expertId + "'";
		List<ExpertsProfessionalfieldRelation> findBySql = findBySql(sql, null, ExpertsProfessionalfieldRelation.class);
		return findBySql;
	}
	
	public List<ExpertsProfessionalfieldRelation> findByProductLibraryid(String productLibraryid) {
		String sql = "select * from t_agro_experts_professionalfield_relation a where a.product_library_id = '" + productLibraryid + "'";
		List<ExpertsProfessionalfieldRelation> findBySql = findBySql(sql, null, ExpertsProfessionalfieldRelation.class);
		return findBySql;
	}

	public int delete(String expertId) {
		return updateBySql("delete from t_agro_experts_professionalfield_relation where experts_id =:p1", new Parameter(expertId));
	}
	
	public List<ExpertsProfessionalfieldRelation> findByLikeProductLibraryId(String productLibraryid) {
		String sql = "select * from t_agro_experts_professionalfield_relation a where a.product_library_id in " + productLibraryid + " and a.states <> 'D'";
		List<ExpertsProfessionalfieldRelation> findBySql = findBySql(sql, null, ExpertsProfessionalfieldRelation.class);
		return findBySql;
	}
}
