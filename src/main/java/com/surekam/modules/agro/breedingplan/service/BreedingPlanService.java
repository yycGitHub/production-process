package com.surekam.modules.agro.breedingplan.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import com.surekam.common.utils.DateUtils;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.batchplan.dao.BatchPlanDao;
import com.surekam.modules.agro.batchplan.entity.BatchPlan;
import com.surekam.modules.agro.breedingplan.entity.BreedingPlan;
import com.surekam.modules.agro.breedingplan.dao.BreedingPlanDao;
import com.surekam.modules.agro.productgrowthcycle.dao.ProductGrowthCycleDao;
import com.surekam.modules.agro.productgrowthcycle.entity.ProductGrowthCycle;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.sys.entity.User;

/**
 * 种植计划表Service
 * @author luoxw
 * @version 2019-10-16
 */
@Component
@Transactional(readOnly = true)
public class BreedingPlanService extends BaseService {

	@Autowired
	private BreedingPlanDao breedingPlanDao;
	@Autowired
	private BatchPlanDao batchPlanDao;
	@Autowired
	private ProductGrowthCycleDao productGrowthCycleDao;
	
	public BreedingPlan get(String id) {
		return breedingPlanDao.get(id);
	}

	public Page<BreedingPlan> find(Page<BreedingPlan> page, String baseId,String isOffice,String officeId,String year,User user) {
		DetachedCriteria dc = breedingPlanDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.createAlias("baseTree", "baseTree");
		if("0".equals(isOffice)&&StringUtils.isNotBlank(officeId)){
			dc.add(Restrictions.or(
				// 本企业的信息
				Restrictions.eq("office.id", officeId),
				// 所有下级企业的信息
				Restrictions.like("office.parentIds", "%," + officeId + ",%")
			));
		}
		if("1".equals(isOffice)&&StringUtils.isNotBlank(baseId)){
			dc.add(Restrictions.or(
					// 本企业的信息
					Restrictions.eq("baseTree.id", baseId),
					// 所有下级企业的信息
					Restrictions.like("baseTree.parentIds", "%," + baseId + ",%")
				));
		}
		if(StringUtils.isNotBlank(year)){
			dc.add(Restrictions.eq("planYear", year));
		}
		dc.addOrder(Order.desc("office.id"));
		dc.addOrder(Order.desc("baseTree.id"));
		dc.addOrder(Order.desc("planYear"));
		dc.add(Restrictions.ne(ProductionBatch.FIELD_DEL_FLAG_XGXT, ProductionBatch.STATE_FLAG_DEL));
		return breedingPlanDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(BreedingPlan breedingPlan) throws Exception {
		breedingPlanDao.save(breedingPlan);
		double batchNumber = Math.floor(Integer.parseInt(breedingPlan.getPlanTotal())/Integer.parseInt(breedingPlan.getBatchNumber()));
		for(int i=0;i<Integer.parseInt(breedingPlan.getBatchNumber());i++){
			BatchPlan batchPlan = new BatchPlan();
			batchPlan.setPlanId(breedingPlan.getId());
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sfString = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			c.setTime(sf.parse(breedingPlan.getStartDate()));
			c.add(Calendar.DAY_OF_MONTH, i*breedingPlan.getIntervalDays());
			batchPlan.setBatchDate(sf.format(c.getTime()));
			batchPlan.setBatchPlanNo(sfString.format(c.getTime()));
			batchPlan.setBatchNumber(String.valueOf(batchNumber));
			batchPlan.setCreateTime(new Date());
			batchPlan.setCreateUserId(breedingPlan.getCreateUserId());
			batchPlan.setStates("A");
			batchPlan.setStandardId(breedingPlan.getStandardId());
			batchPlan.setBatchUnit(breedingPlan.getPlanUnit());
			batchPlan.setBatchEndDate(getEndDate(breedingPlan.getStandardId(),batchPlan.getBatchDate()));
			batchPlanDao.save(batchPlan);
		}
	}

	public String getEndDate(String standardId, String beginDay) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		String sql = "SELECT * FROM t_agro_product_growth_cycle a WHERE a.sys_ent_standard_id= :p1 AND a.states<>'D'";
		List<ProductGrowthCycle> list = productGrowthCycleDao.findBySql(sql, new Parameter(standardId), ProductGrowthCycle.class);
		if(list!=null && list.size()>0){
			ProductGrowthCycle productGrowthCycle = list.get(0);
			String bDay = productGrowthCycle.getBeginDay();
			String eDay = productGrowthCycle.getEndDay();
			if(StringUtils.isNotBlank(bDay) && StringUtils.isNotBlank(eDay)){
				if(Integer.parseInt(bDay)<=Integer.parseInt(eDay)){
					Date date = sdf.parse(beginDay);
				    Calendar calendar = Calendar.getInstance();  
				    calendar.setTime(date);  
				    calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(eDay)-Integer.parseInt(bDay));  
				    date = calendar.getTime();  
				    return sdf.format(date); 
				}else{
					return beginDay;
				}
			}else{
				return beginDay;
			}
		}else{
			return beginDay;
		}
	}
	@Transactional(readOnly = false)
	public void delete(String id) {
		breedingPlanDao.deleteByXGXTId(id);
		batchPlanDao.deleteByPlanId(id);
	}

	public List<BreedingPlan> findList(String baseId) {
		DetachedCriteria dc = breedingPlanDao.createDetachedCriteria();
		dc.createAlias("baseTree", "baseTree");
		dc.add(Restrictions.eq("baseTree.id", baseId));
		dc.add(Restrictions.gt("planYear", String.valueOf(new Date().getYear())));
		dc.addOrder(Order.desc("planYear"));
		dc.add(Restrictions.ne(ProductionBatch.FIELD_DEL_FLAG_XGXT, ProductionBatch.STATE_FLAG_DEL));
		return breedingPlanDao.find(dc);
	}
	public List<BreedingPlan> findList(String baseId,String year) {
		DetachedCriteria dc = breedingPlanDao.createDetachedCriteria();
		dc.createAlias("baseTree", "baseTree");
		dc.add(Restrictions.eq("baseTree.id", baseId));
		dc.add(Restrictions.eq("planYear", year));
		dc.add(Restrictions.ne(ProductionBatch.FIELD_DEL_FLAG_XGXT, ProductionBatch.STATE_FLAG_DEL));
		return breedingPlanDao.find(dc);
	}
}
