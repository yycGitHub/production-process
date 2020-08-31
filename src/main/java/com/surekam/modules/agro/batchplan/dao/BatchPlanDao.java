package com.surekam.modules.agro.batchplan.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.BaseEntity;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.batchplan.entity.BatchPlan;

/**
 * 批次计划表DAO接口
 * @author luoxw
 * @version 2019-10-16
 */
@Repository
public class BatchPlanDao extends BaseDao<BatchPlan> {

	/**
	 * 逻辑删除
	 * @param planId
	 * @param likeParentIds
	 * @return
	 */
	public int deleteByPlanId(Serializable planId){
		return update("update BatchPlan set states = 'D' where planId = :p1",
				new Parameter(planId));
	}
}
