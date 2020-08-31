package com.surekam.modules.agro.baseexpertsrelation.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.baseexpertsrelation.entity.BaseExpertsRelation;

/**
 * 基地专家关联表DAO接口
 * @author luoxw
 * @version 2019-10-29
 */
@Repository
public class BaseExpertsRelationDao extends BaseDao<BaseExpertsRelation> {


	/**
	 * 逻辑删除
	 * @param planId
	 * @param likeParentIds
	 * @return
	 */
	public int deleteByBaseId(Serializable baseId){
		return update("delete from BaseExpertsRelation where baseId = :p1",
				new Parameter(baseId));
	}
}
