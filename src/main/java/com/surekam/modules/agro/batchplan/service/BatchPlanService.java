package com.surekam.modules.agro.batchplan.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.batchplan.entity.BatchPlan;
import com.surekam.modules.agro.batchplan.dao.BatchPlanDao;
import com.surekam.modules.agro.breedingplan.entity.BreedingPlan;
import com.surekam.modules.agro.productgrowthcycle.entity.ProductGrowthCycle;
import com.surekam.modules.agro.productionbatch.dao.ProductionBatchDao;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.sys.entity.User;

/**
 * 批次计划表Service
 * @author luoxw
 * @version 2019-10-16
 */
@Component
@Transactional(readOnly = true)
public class BatchPlanService extends BaseService {

	@Autowired
	private BatchPlanDao batchPlanDao;

	@Autowired
	private ProductionBatchDao productionBatchDao;
	
	public BatchPlan get(String id) {
		return batchPlanDao.get(id);
	}
	
	public Page<BatchPlan> find(Page<BatchPlan> page, String planId,User user) {
		DetachedCriteria dc = batchPlanDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(planId)){
			dc.add(Restrictions.eq("planId", planId));
		}
		dc.addOrder(Order.asc("standardId"));
		dc.addOrder(Order.asc("batchDate"));
		dc.addOrder(Order.asc("batchPlanNo"));
		dc.add(Restrictions.ne(ProductionBatch.FIELD_DEL_FLAG_XGXT, ProductionBatch.STATE_FLAG_DEL));
		return batchPlanDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(BatchPlan batchPlan) {
		batchPlanDao.save(batchPlan);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		batchPlanDao.deleteByXGXTId(id);
	}

	public List<BatchPlan> getListByBaseId(String baseId) {
		String sql = "SELECT a.* FROM t_agro_batch_plan a LEFT JOIN t_agro_breeding_plan p ON a.plan_id=p.id"
				+ " WHERE a.states <> 'D' AND p.states <> 'D' AND p.base_id = :p1"
				+ " ORDER BY a.standard_id,a.batch_date,a.batch_number";
		return batchPlanDao.findBySql(sql, new Parameter(baseId), BatchPlan.class);
	}
	public List<BatchPlan> findList(BatchPlan batchPlan) {
		DetachedCriteria dc = batchPlanDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(batchPlan.getId())){
			dc.add(Restrictions.ne("id", batchPlan.getId()));
		}
		dc.add(Restrictions.eq("planId", batchPlan.getPlanId()));
		dc.add(Restrictions.eq("batchPlanNo", batchPlan.getBatchPlanNo()));
		dc.add(Restrictions.ne(ProductionBatch.FIELD_DEL_FLAG_XGXT, ProductionBatch.STATE_FLAG_DEL));
		return batchPlanDao.find(dc);
	}

	public List<ProductionBatch> findListByBatchPlanId(String batchPlanId) {
		DetachedCriteria dc = productionBatchDao.createDetachedCriteria();
		dc.add(Restrictions.eq("batchPlanId", batchPlanId));
		dc.add(Restrictions.ne(ProductionBatch.FIELD_DEL_FLAG_XGXT, ProductionBatch.STATE_FLAG_DEL));
		return productionBatchDao.find(dc);
	}

	public List<BatchPlan> findList(String planId) {
		DetachedCriteria dc = batchPlanDao.createDetachedCriteria();
		dc.add(Restrictions.eq("planId", planId));
		dc.add(Restrictions.ne(ProductionBatch.FIELD_DEL_FLAG_XGXT, ProductionBatch.STATE_FLAG_DEL));
		return batchPlanDao.find(dc);
	}
}
