package com.surekam.modules.agro.productbatchtask.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.DateUtil;
import com.surekam.common.utils.DateUtils;
import com.surekam.modules.agro.basemanage.dao.BaseTreeDao;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
import com.surekam.modules.agro.productbatchtask.dao.ProductBatchTaskDao;
import com.surekam.modules.agro.productbatchtask.entity.ProductBatchTask;
import com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskPage;
import com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskValueVo;
import com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskVo;
import com.surekam.modules.agro.productbatchtaskresolve.dao.ProductBatchTaskResolveDao;
import com.surekam.modules.agro.productbatchtaskresolve.entity.ProductBatchTaskResolve;
import com.surekam.modules.agro.productionbatch.dao.ProductionBatchDao;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.agro.standarditems.dao.StandardItemsDao;
import com.surekam.modules.agro.standarditems.entity.StandardItems;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;

/**
 * 批次对应的详细计划Service
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Component
@Transactional(readOnly = true)
public class ProductBatchTaskService extends BaseService {

	@Autowired
	private ProductBatchTaskDao productBatchTaskDao;

	@Autowired
	private ProductionBatchDao productionBatchDao;

	@Autowired
	private StandardItemsDao standardItemsDao;

	@Autowired
	private ProductBatchTaskResolveDao batchTaskResolveDao;

	@Autowired
	private BaseTreeDao baseTreeDao;

	@Autowired
	private OfficeDao officeDao;
	
	@Autowired
	private UserDao userDao;

	public ProductBatchTask get(String id) {
		return productBatchTaskDao.get(id);
	}

	/**
	 * 查询基地，批次，人员任务
	 * 
	 * @param page
	 * @param currentUser
	 * @param name
	 * @param states
	 * @param regionType
	 *            任务对象类型，1代表基地 ，2代表批次，3代表人员
	 * @return
	 */
	public Page<ProductBatchTask> findTasks(Page<ProductBatchTask> page, User currentUser, String name, String states, String regionType, String regionId) {
		DetachedCriteria dc = productBatchTaskDao.createDetachedCriteria();
		dc.add(Restrictions.ne(ProductBatchTask.FIELD_DEL_FLAG_XGXT, ProductBatchTask.STATE_FLAG_DEL));
		if (StringUtils.isNotBlank(regionType)) {
			dc.add(Restrictions.eq("regionType", regionType));
		}
		if (StringUtils.isNotBlank(regionId)) {
			dc.add(Restrictions.eq("regionId", regionId));
		}
		if (StringUtils.isNotBlank(name)) {
			dc.add(Restrictions.like("standardItemName", name, MatchMode.ANYWHERE));
		}
		if (!currentUser.isAdmin()) {
			// 查询出当前公司的数据，包含子公司
			List<String> ids = new ArrayList<String>();
			List<Office> offices = officeDao.findAllOffices(currentUser.getCompany().getId());
			if (offices != null && offices.size() > 0) {
				for (Office office : offices) {
					ids.add(office.getId());
				}
				ids.add(currentUser.getCompany().getId());
				dc.add(Restrictions.in("officeId", ids));
			} else {
				if(regionType.equals("3")) {
					ids.add(currentUser.getCompany().getId());
					dc.add(Restrictions.in("officeId", ids));
				}else {
					dc.add(Restrictions.eq("officeId", null));
				}
			}
		}
		dc.addOrder(Order.desc("startDate"));
		return productBatchTaskDao.find(page, dc);
	}

	public List<ResolveAndTaskVo> fetchDailyTaskList(String tagetId, String isOffice, String startDate, String endDate, String taskType, String batchId) {
		List<String> ids = new ArrayList<String>();
		if (StringUtils.isNotBlank(batchId)) {
			ids.add(batchId);
		} else {
			this.getIds(isOffice, tagetId, ids);
		}
		if (ids != null && ids.size() > 0) {
			// 查询基地和批次作业记录
			String hql = "select new com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskVo(pbt.id,pbtr.id,pbt.standardItemName,pbtr.dispatchTime,pbtr.finishTime,pbt.standardItemId,pbtr.nonexecutionReason)"
					+ " from ProductBatchTask pbt,ProductBatchTaskResolve pbtr" + " where pbt.id=pbtr.taskId "
					+ " and pbt.states <> 'D'" + " and pbtr.states <> 'D'" + " and pbt.regionId in (:p1)";
			if ("dispatch".equals(taskType)) {
				if (StringUtils.isNotBlank(startDate)) {
					hql += " and pbtr.dispatchTime >= '" + startDate.substring(0, 10) + "'";
				}
				if (StringUtils.isNotBlank(endDate)) {
					hql += " and pbtr.dispatchTime <= '" + endDate.substring(0, 10) + "'";
				}
				hql += " order by pbtr.dispatchTime asc";
			} else {
				if (StringUtils.isNotBlank(startDate)) {
					hql += " and pbtr.finishTime >= '" + startDate.substring(0, 10) + "'";
				}
				if (StringUtils.isNotBlank(endDate)) {
					hql += " and pbtr.finishTime <= '" + endDate + "'";
				}
				hql += " order by pbtr.finishTime asc";
			}
			List<ResolveAndTaskVo> tasks = productBatchTaskDao.find(hql, new Parameter(new Object[] { ids.toArray() }));
			// 查询任务的详细数据
			if ("finish".equals(taskType)) {
				if (tasks != null && tasks.size() > 0) {
					List<String> taskResoleIds = new ArrayList<String>();
					for (int i = 0; i < tasks.size(); i++) {
						taskResoleIds.add(tasks.get(i).getTaskResoleId());
					}
					String taskValueHql = "select new com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskValueVo(stiav.id,pbtr.id,stiav.taskListId,stiav.taskItemArgsId,stiav.argsName,"
							+ "stiav.taskItemArgsValue,stiav.argsUnit,stiav.argsType,stiav.argsValueDescription,stiav.sort)"
							+ " from ProductBatchTaskResolve pbtr ,StandardTaskList stl,StandardTaskItemsArgsValue stiav"
							+ " where pbtr.id=stl.taskItemsId and stl.id=stiav.taskListId"
							+ " and stl.taskItemsId in (:p1)" + " order by stiav.sort";
					List<ResolveAndTaskValueVo> taskResoles = productBatchTaskDao.find(taskValueHql, new Parameter(new Object[] { taskResoleIds.toArray() }));
					if (taskResoles != null && taskResoles.size() > 0) {
						for (int i = 0; i < tasks.size(); i++) {
							ResolveAndTaskVo resolveAndTaskVo = tasks.get(i);
							for (int j = 0; j < taskResoles.size(); j++) {
								ResolveAndTaskValueVo resolveAndTaskValueVo = taskResoles.get(j);
								if (resolveAndTaskVo.getTaskResoleId().equals(resolveAndTaskValueVo.getTaskResolveId())) {
									resolveAndTaskVo.getTaskValueList().add(resolveAndTaskValueVo);
								}
							}
						}
					}
				}
			}
			return tasks;
		} else {
			return null;
		}

	}

	public void getIds(String isOffice, String tagetId, List<String> ids) {
		List<String> baseIds = new ArrayList<String>();
		if ("0".equals(isOffice)) {// 公司
			// 查询公司下所有的基地
			List<BaseTree> baseTrees = baseTreeDao.queryOfficeList(tagetId);
			if (baseTrees != null && baseTrees.size() > 0) {
				for (BaseTree baseTree : baseTrees) {
					baseIds.add(baseTree.getId());
					ids.add(baseTree.getId());
				}
			}
		} else {// 基地
			baseIds.add(tagetId);
			ids.add(tagetId);
		}
		if (baseIds != null && baseIds.size() > 0) {
			// 查询基地下批次
			DetachedCriteria dc = productionBatchDao.createDetachedCriteria();
			dc.add(Restrictions.in("baseId", baseIds.toArray()));
			List<ProductionBatch> batchs = productionBatchDao.find(dc);
			if (batchs != null && batchs.size() > 0) {
				for (int i = 0; i < batchs.size(); i++) {
					ids.add(batchs.get(i).getId());
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public void save(ProductBatchTask productBatchTask) throws ParseException {
		productBatchTask.setStandardItemId("1");
		if(productBatchTask.getRegionType().equals("3")) {
			User user = userDao.get(productBatchTask.getRegionId());
			productBatchTask.setOfficeId(user.getCompany().getId());
		}
		productBatchTaskDao.save(productBatchTask);
		// 清除未完成的执行计划
		String hql = "delete from ProductBatchTaskResolve" + " where executionStatus is null  " + " and taskId =:p1";
		batchTaskResolveDao.createQuery(hql, new Parameter(productBatchTask.getId())).executeUpdate();
		String frequencyDuringTime = productBatchTask.getFrequencyDuringTime() == null ? "0" : productBatchTask.getFrequencyDuringTime();
		String singleOperationIntervalDay = productBatchTask.getSingleOperationIntervalDay() == null ? "0" : productBatchTask.getSingleOperationIntervalDay();
		// 次数
		Integer cnum = Integer.valueOf(frequencyDuringTime);
		// 天数
		Integer tnum = Integer.valueOf(singleOperationIntervalDay);
		// 查询已完成的计划数量
		int finishNum = 0;
		String finishedHql = "from ProductBatchTaskResolve " + " where executionStatus is not null" + " and taskId =:p1";
		List<ProductBatchTaskResolve> productBatchTaskResolves = batchTaskResolveDao.find(finishedHql, new Parameter(productBatchTask.getId()));
		if (productBatchTaskResolves != null && productBatchTaskResolves.size() > 0) {
			finishNum = productBatchTaskResolves.size();
		}
		ProductionBatch productionBatch = productionBatchDao.get(productBatchTask.getRegionId());
		Integer needToDoTimes = this.createTaskCarryList(productBatchTask, cnum, tnum, finishNum, productionBatch);
		productBatchTask.setNeedToDoTimes(needToDoTimes.toString());
	}

	@Transactional(readOnly = false)
	public String delete(String id) {
		String finishedHql = "from ProductBatchTaskResolve " + " where executionStatus is not null" + " and taskId =:p1";
		List<ProductBatchTaskResolve> productBatchTaskResolves = batchTaskResolveDao.find(finishedHql, new Parameter(id));
		if (productBatchTaskResolves != null && productBatchTaskResolves.size() > 0) {
			return "1";
		} else {
			productBatchTaskDao.deleteByXGXTId(id);
			String deleteTaskCarryHal = "update ProductBatchTaskResolve " + " set states=:p1" + " where taskId =:p2";
			batchTaskResolveDao.update(deleteTaskCarryHal, new Parameter(ProductBatchTaskResolve.STATE_FLAG_DEL, id));
			return "0";
		}
	}

	@Transactional(readOnly = false)
	public void createFixedTask(ProductionBatch productionBatch, User currentUser) {
		//System.out.println(batchId+"        "+currentUser.getLoginName());
		// 获取批次信息
		if(StringUtils.isBlank(productionBatch.getId())){
			productionBatchDao.save(productionBatch);
		}
		
		//ProductionBatch productionBatch = productionBatchDao.get(batchId);
		if(productionBatch!=null){
			System.out.println("batch------------"+productionBatch.getBatchCode());
			// 获取对应的标准树
			String hql = "select si from ProductGrowthCycle pgc,StandardItems si "
					+ " where pgc.sysEntStandardId=:p1 and pgc.id=si.growthCycleId" + " and si.operationType ='2'"
					+ " and pgc.states<>'D'" + " and si.states<>'D'";
			List<StandardItems> standardItems = standardItemsDao.find(hql, new Parameter(productionBatch.getStandardId()));
			// 遍历标准生成固定任务
			if (standardItems != null && standardItems.size() > 0) {
				for (int i = 0; i < standardItems.size(); i++) {
					// 生成固定任务
					StandardItems item = standardItems.get(i);
					String startDateNumber = item.getStartDateNumber() == null ? "0" : item.getStartDateNumber();
					String endDateNumber = item.getEndDateNumber() == null ? "0" : item.getEndDateNumber();
					Date startDate = DateUtils.addDays(DateUtils.parseDate(productionBatch.getBatchStartDate()), Integer.valueOf(startDateNumber));
					Date endDate = DateUtils.addDays(DateUtils.parseDate(productionBatch.getBatchStartDate()), Integer.valueOf(endDateNumber));
					// cnum
					String frequencyDuringTime = item.getFrequencyDuringTime() == null ? "0" : item.getFrequencyDuringTime();
					// tnum
					String singleOperationIntervalDay = item.getSingleOperationIntervalDay() == null ? "0" : item.getSingleOperationIntervalDay();
					Integer cnum = Integer.valueOf(frequencyDuringTime);
					Integer tnum = Integer.valueOf(singleOperationIntervalDay);
					ProductBatchTask productBatchTask = new ProductBatchTask();
					productBatchTask.setRegionType("2");
					productBatchTask.setRegionId(productionBatch.getId());
					productBatchTask.setRegionName(productionBatch.getBatchCode());
					productBatchTask.setStandardItemId(item.getId());
					productBatchTask.setStandardItemName(item.getItemName());
					productBatchTask.setStandardItemCategoryId(item.getItemCategoryId());
					productBatchTask.setGrowthCycleId(item.getGrowthCycleId());
					productBatchTask.setGrowthCycleName(item.getGrowthCycleName());
					productBatchTask.setStandardsItemDescription(item.getStandardsItemDescription());
					productBatchTask.setStartDateNumber(startDateNumber);
					productBatchTask.setEndDateNumber(endDateNumber);
					productBatchTask.setStartDate(startDate);
					productBatchTask.setEndDate(endDate);
					productBatchTask.setFrequencyDuringTime(frequencyDuringTime);
					productBatchTask.setFinishedTimes("0");
					productBatchTask.setOfficeId(currentUser.getCompany().getId());
					productBatchTask.setCreateUserId(currentUser.getId());
					productBatchTaskDao.flush();
					productBatchTaskDao.clear();
					productBatchTaskDao.save(productBatchTask);

					Integer needToDoTimes = this.createTaskCarryList(productBatchTask, cnum, tnum, 0, productionBatch);
					productBatchTask.setNeedToDoTimes(needToDoTimes.toString());
				}
			}
		}
		
	}

	public Page<ProductBatchTaskResolve> findTaskCarryList(Page<ProductBatchTaskResolve> page, String taskId, String states) {
		DetachedCriteria dc = batchTaskResolveDao.createDetachedCriteria();
		dc.add(Restrictions.ne(ProductBatchTaskResolve.FIELD_DEL_FLAG_XGXT, ProductBatchTaskResolve.STATE_FLAG_DEL));
		if (StringUtils.isNotBlank(taskId)) {
			dc.add(Restrictions.eq("taskId", taskId));
		}
		if (StringUtils.isNotBlank(states)) {
			dc.add(Restrictions.eq("states", states));
		}
		dc.addOrder(Order.asc("dispatchTime"));
		return batchTaskResolveDao.find(page, dc);
	}
	
	public static void main(String[] args) throws ParseException {
		Date stDate = new Date();
		Date advanceDays = DateUtil.advanceDays(stDate, -0);
		
		 SimpleDateFormat sj = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sj.format(advanceDays));
	}

	/**
	 * 生成任务执行计划
	 * 
	 * @param productBatchTask
	 *            任务
	 * @param cnum
	 *            次数
	 * @param tnum
	 *            天数
	 * @param productionBatch
	 *            批次表
	 */
	public Integer createTaskCarryList(ProductBatchTask productBatchTask, Integer cnum, Integer tnum, Integer finishNum, ProductionBatch productionBatch) {
		try{
		// 1.次数为0，不生成执行计划
		if (cnum > 0) {
			if (tnum == 0) {// 2.次数不为0，天数为0,则默认生成按一天一次的计划任务
				Date stDate = new Date();
				if (productBatchTask.getStartDate() != null) {
					stDate = productBatchTask.getStartDate();
				}
				for (int m = 1; m <= cnum; m++) {
					if (m > finishNum) {
						ProductBatchTaskResolve batchTaskResolve = new ProductBatchTaskResolve();
						batchTaskResolve.setTaskId(productBatchTask.getId());
						batchTaskResolve.setDispatchTime(DateUtils.formatDate(stDate));
						batchTaskResolve.setCreateUserId(productBatchTask.getCreateUserId());
						Date advanceDays = DateUtil.advanceDays(stDate, -Integer.valueOf(productionBatch.getAdvanceDays()==null?"0":productionBatch.getAdvanceDays()));
						batchTaskResolve.setSendDate(DateUtil.formatDate4(advanceDays) + productionBatch.getSendTime());
						batchTaskResolveDao.save(batchTaskResolve);
					}
					stDate = DateUtils.addDays(stDate, 1);
				}
				return cnum;
			} else {// 3.次数不为0，天数也不为0
				int cal = (tnum * 86400000) / cnum;// 每隔多少豪秒执行一次
				Integer i = 1;
				if (productBatchTask.getStartDate() != null && productBatchTask.getEndDate() != null) {// 3.1设置了时间，生成时间之内的任务
					Date stDate = productBatchTask.getStartDate();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(productBatchTask.getEndDate());
					calendar.set(Calendar.HOUR_OF_DAY, 23);
					calendar.set(Calendar.MINUTE, 59);
					calendar.set(Calendar.SECOND, 59);
					calendar.set(Calendar.MILLISECOND, 999);
					Date edDate = calendar.getTime();// 11点59分59秒999
					while (true) {
						ProductBatchTaskResolve batchTaskResolve = new ProductBatchTaskResolve();
						batchTaskResolve.setTaskId(productBatchTask.getId());
						batchTaskResolve.setCreateUserId(productBatchTask.getCreateUserId());
						batchTaskResolve.setDispatchTime(DateUtils.formatDate(stDate));
						if ((stDate.getTime() + cnum - 1) > edDate.getTime()) {
							break;
						}
						if (i > finishNum) {// 已完成了的次数抵消掉
							batchTaskResolveDao.save(batchTaskResolve);
						}
						stDate = DateUtils.addMilliseconds(stDate, cal);
						i++;
					}
				} else {
					Calendar calendar1 = Calendar.getInstance();
					calendar1.setTime(new Date());
					calendar1.set(Calendar.HOUR_OF_DAY, 0);
					calendar1.set(Calendar.MINUTE, 0);
					calendar1.set(Calendar.SECOND, 0);
					calendar1.set(Calendar.MILLISECOND, 0);
					Date circulateStartDate = calendar1.getTime();// 今日0点
					Date circulateEndDate = DateUtils.addYears(circulateStartDate, 1);// 循环执行一年
					while (true) {
						ProductBatchTaskResolve batchTaskResolve = new ProductBatchTaskResolve();
						batchTaskResolve.setTaskId(productBatchTask.getId());
						batchTaskResolve.setCreateUserId(productBatchTask.getCreateUserId());
						batchTaskResolve.setDispatchTime(DateUtils.formatDate(circulateStartDate));
						if ((circulateStartDate.getTime() + cnum - 1) >= circulateEndDate.getTime()) {
							break;
						}
						if (i > finishNum) {// 已完成了的次数抵消掉
							batchTaskResolveDao.save(batchTaskResolve);
						}
						circulateStartDate = DateUtils.addMilliseconds(circulateStartDate, cal);
						i++;
					}
				}
				return i - 1;
			}
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	public String getGrowthCycleNameByBatchId(String batchId) {
		String sql = "SELECT aa.cycle_name FROM(SELECT b.* FROM t_agro_product_batch_task a, t_agro_product_growth_cycle b WHERE a.growth_cycle_id=b.id AND a.states<>'D' AND b.states<>'D' AND a.region_id=:p1 ORDER BY b.begin_day DESC) aa LIMIT 1 ";
		List<String> list = productBatchTaskDao.findBySql(sql, new Parameter(batchId));
		if (list.size() > 0) {
			return list.get(0) + "";
		} else {
			return "";
		}
	}

	/**
	 * Title: findByBatchIdQueryTask Description: 根据批次ID查询相关任务
	 * 
	 * @param baseId
	 *            批次id
	 * @return 
	 */
	public Map<String, Object> findByBatchIdQueryTask(String batchId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		ProductionBatch productionBatch = productionBatchDao.get(batchId);
		if(productionBatch == null) {
			return map;
		}
		List<ProductBatchTask> findByRegionId = productBatchTaskDao.findByRegionId(productionBatch.getId());
		for (ProductBatchTask productBatchTask : findByRegionId) {
			List<ProductBatchTaskResolve> pbtrList = new ArrayList<ProductBatchTaskResolve>();
			List<ProductBatchTaskResolve> findByTaskId = batchTaskResolveDao.findByTaskId(productBatchTask.getId());
			if(!findByTaskId.isEmpty()) {
				for (ProductBatchTaskResolve productBatchTaskResolve : findByTaskId) {
					pbtrList.add(productBatchTaskResolve);
				}
				Map<String, Object> pbtmap = new HashMap<String, Object>();
				pbtmap.put("pbtrDate", new java.sql.Date(productBatchTask.getStartDate().getTime()));
				pbtmap.put("pbtrList", pbtrList);
				listMap.add(pbtmap);
			}
			
		}
		map.put("productionBatch", productionBatch);
		map.put("map", listMap);
		return map;
	}
	

	public Page<ResolveAndTaskPage> fetchDailyTaskPage(Page<ResolveAndTaskPage> page,String tagetId, String isOffice, String startDate, String endDate, String taskType, String batchId) {
		List<String> ids = new ArrayList<String>();
		if (StringUtils.isNotBlank(batchId)) {
			ids.add(batchId);
		} else {
			this.getIds(isOffice, tagetId, ids);
		}
		if (ids != null && ids.size() > 0) {
			// 查询基地和批次作业记录
			String hql = "select new com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskPage(pbt.id,pbtr.id,pbt.standardItemName,pbtr.dispatchTime,pbtr.finishTime,pbt.standardItemId,pbt.regionId)"
					+ " from ProductBatchTask pbt,ProductBatchTaskResolve pbtr" + " where pbt.id=pbtr.taskId "
					+ " and pbt.states <> 'D'" + " and pbtr.states <> 'D'" + " and pbt.regionId in (:p1)";
			if ("dispatch".equals(taskType)) {
				if (StringUtils.isNotBlank(startDate)) {
					hql += " and pbtr.dispatchTime >= '" + startDate.substring(0, 10) + "'";
				}
				if (StringUtils.isNotBlank(endDate)) {
					hql += " and pbtr.dispatchTime <= '" + endDate.substring(0, 10) + "'";
				}
				hql += " order by pbtr.dispatchTime desc";
			} else {
				if (StringUtils.isNotBlank(startDate)) {
					hql += " and pbtr.finishTime >= '" + startDate.substring(0, 10) + "'";
				}
				if (StringUtils.isNotBlank(endDate)) {
					hql += " and pbtr.finishTime <= '" + endDate + "'";
				}
				hql += " order by pbtr.finishTime desc";
			}
			Page<ResolveAndTaskPage> pageResolveAndTaskVo = productBatchTaskDao.find(page,hql, new Parameter(new Object[] { ids.toArray() }));
			List<ResolveAndTaskPage> tasks= pageResolveAndTaskVo.getList();
			// 查询任务的详细数据
			if ("finish".equals(taskType)) {
				if (tasks != null && tasks.size() > 0) {
					List<String> taskResoleIds = new ArrayList<String>();
					// 保存根据批次查询公司名称，基地名称，对于品种名称，以免多次重复查询
					Map<String, Map<String, String>> mapObject = new HashMap<String, Map<String, String>>();
					for (int i = 0; i < tasks.size(); i++) {
						ResolveAndTaskPage resolveAndTaskVo = tasks.get(i);
						taskResoleIds.add(resolveAndTaskVo.getTaskResoleId());
						if (!mapObject.containsKey(resolveAndTaskVo.getBatchId())) {
							List<Map<String, String>> mapList= productionBatchDao.findNameByBacthId(resolveAndTaskVo.getBatchId());
							if (mapList.size()>0) {
								Map<String, String> map = mapList.get(0);
								resolveAndTaskVo.setOfficeName(map.get("officeName"));
								resolveAndTaskVo.setBaseName(map.get("baseName"));
								resolveAndTaskVo.setProductName(map.get("productName"));
								resolveAndTaskVo.setBatchCode(map.get("batchCode"));
								mapObject.put(resolveAndTaskVo.getBatchId(), map);
							}
						} else {
							Map<String, String> map = mapObject.get(resolveAndTaskVo.getBatchId());
							resolveAndTaskVo.setOfficeName(map.get("officeName"));
							resolveAndTaskVo.setBaseName(map.get("baseName"));
							resolveAndTaskVo.setProductName(map.get("productName"));
							resolveAndTaskVo.setBatchCode(map.get("batchCode"));
						}
					}
					String taskValueHql = "select new com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskValueVo(stiav.id,pbtr.id,stiav.taskListId,stiav.taskItemArgsId,stiav.argsName,"
							+ "stiav.taskItemArgsValue,stiav.argsUnit,stiav.argsType,stiav.argsValueDescription,stiav.sort)"
							+ " from ProductBatchTaskResolve pbtr ,StandardTaskList stl,StandardTaskItemsArgsValue stiav"
							+ " where pbtr.id=stl.taskItemsId and stl.id=stiav.taskListId"
							+ " and stl.taskItemsId in (:p1)" + " order by stiav.sort";
					List<ResolveAndTaskValueVo> taskResoles = productBatchTaskDao.find(taskValueHql, new Parameter(new Object[] { taskResoleIds.toArray() }));
					if (taskResoles != null && taskResoles.size() > 0) {
						for (int i = 0; i < tasks.size(); i++) {
							ResolveAndTaskPage resolveAndTaskVo = tasks.get(i);
							for (int j = 0; j < taskResoles.size(); j++) {
								ResolveAndTaskValueVo resolveAndTaskValueVo = taskResoles.get(j);
								if (resolveAndTaskVo.getTaskResoleId().equals(resolveAndTaskValueVo.getTaskResolveId())) {
									resolveAndTaskVo.getTaskValueList().add(resolveAndTaskValueVo);
								}
							}
						}
					}
				}
			}
			return pageResolveAndTaskVo;
		} else {
			return page;
		}

	}

	public List<ResolveAndTaskVo> fetchDailyTaskList(String tagetId, String isOffice, String batchId) {
		List<String> ids = new ArrayList<String>();
		if (StringUtils.isNotBlank(batchId)) {
			ids.add(batchId);
		} else {
			this.getIds(isOffice, tagetId, ids);
		}
		if (ids != null && ids.size() > 0) {
			// 查询基地和批次作业记录
			String hql = "select new com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskVo(pbt.id,pbtr.id,pbt.standardItemName,pbtr.dispatchTime,pbtr.finishTime,pbt.standardItemId,pbtr.nonexecutionReason)"
					+ " from ProductBatchTask pbt,ProductBatchTaskResolve pbtr" + " where pbt.id=pbtr.taskId "
					+ " and pbt.states <> 'D'" + " and pbtr.states <> 'D'" + " and pbt.regionId in (:p1)";
			hql+= " and REPLACE(pbtr.dispatchTime, '-', '') <= date_format(sysdate(), '%Y%m%d')";
			hql += " order by pbtr.dispatchTime,pbtr.finishTime desc";
			List<ResolveAndTaskVo> tasks = productBatchTaskDao.find(hql, new Parameter(new Object[] { ids.toArray() }));
			// 查询任务的详细数据
			if (tasks != null && tasks.size() > 0) {
				List<String> taskResoleIds = new ArrayList<String>();
				for (int i = 0; i < tasks.size(); i++) {
					taskResoleIds.add(tasks.get(i).getTaskResoleId());
				}
				String taskValueHql = "select new com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskValueVo(stiav.id,pbtr.id,stiav.taskListId,stiav.taskItemArgsId,stiav.argsName,"
						+ "stiav.taskItemArgsValue,stiav.argsUnit,stiav.argsType,stiav.argsValueDescription,stiav.sort)"
						+ " from ProductBatchTaskResolve pbtr ,StandardTaskList stl,StandardTaskItemsArgsValue stiav"
						+ " where pbtr.id=stl.taskItemsId and stl.id=stiav.taskListId"
						+ " and stl.taskItemsId in (:p1)" + " order by stiav.sort";
				List<ResolveAndTaskValueVo> taskResoles = productBatchTaskDao.find(taskValueHql, new Parameter(new Object[] { taskResoleIds.toArray() }));
				if (taskResoles != null && taskResoles.size() > 0) {
					for (int i = 0; i < tasks.size(); i++) {
						ResolveAndTaskVo resolveAndTaskVo = tasks.get(i);
						for (int j = 0; j < taskResoles.size(); j++) {
							ResolveAndTaskValueVo resolveAndTaskValueVo = taskResoles.get(j);
							if (resolveAndTaskVo.getTaskResoleId().equals(resolveAndTaskValueVo.getTaskResolveId())) {
								resolveAndTaskVo.getTaskValueList().add(resolveAndTaskValueVo);
							}
						}
					}
				}
			}
			return tasks;
		} else {
			return null;
		}

	}

	@Transactional(readOnly = false)
	public void deleteTask(String taskResoleId) throws ParseException {
		batchTaskResolveDao.deleteByXGXTId(taskResoleId);
		batchTaskResolveDao.deleteTaskList(taskResoleId);
		batchTaskResolveDao.deleteTaskvalue(taskResoleId);
	}

	
}
