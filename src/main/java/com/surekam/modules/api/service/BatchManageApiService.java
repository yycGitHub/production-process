package com.surekam.modules.api.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.utils.Client;
import com.surekam.common.utils.DateUtil;
import com.surekam.common.utils.DateUtils;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.basemanage.dao.BaseManagerDao;
import com.surekam.modules.agro.basemanage.dao.BaseTreeDao;
import com.surekam.modules.agro.basemanage.entity.BaseManager;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
import com.surekam.modules.agro.basemanage.entity.CopyOfBaseManager;
import com.surekam.modules.agro.batchplan.dao.BatchPlanDao;
import com.surekam.modules.agro.batchplan.entity.BatchPlan;
import com.surekam.modules.agro.breedingplan.dao.BreedingPlanDao;
import com.surekam.modules.agro.breedingplan.entity.BreedingPlan;
import com.surekam.modules.agro.product.dao.ProductLibraryTreeDao;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.productbatchtask.dao.ProductBatchTaskDao;
import com.surekam.modules.agro.productbatchtask.entity.ProductBatchTask;
import com.surekam.modules.agro.productbatchtask.service.ProductBatchTaskService;
import com.surekam.modules.agro.productbatchtaskresolve.dao.ProductBatchTaskResolveDao;
import com.surekam.modules.agro.productbatchtaskresolve.entity.ProductBatchTaskResolve;
import com.surekam.modules.agro.productbatchtaskresolve.service.ProductBatchTaskResolveService;
import com.surekam.modules.agro.productgrowthcycle.dao.ProductGrowthCycleDao;
import com.surekam.modules.agro.productgrowthcycle.entity.ProductGrowthCycle;
import com.surekam.modules.agro.productionbatch.dao.ProductionBatchDao;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatchReport;
import com.surekam.modules.agro.productlibraryrelation.dao.ProductLibraryRelationDao;
import com.surekam.modules.agro.productlibraryrelation.entity.ProductLibraryRelation;
import com.surekam.modules.agro.sendingrecords.dao.SendingRecordsDao;
import com.surekam.modules.agro.sendingrecords.entity.SendingRecords;
import com.surekam.modules.agro.sensorsetup.dao.SensorSetupDao;
import com.surekam.modules.agro.sensorthreshold.dao.SensorThresholdDao;
import com.surekam.modules.agro.sensorthreshold.entity.CopyOfSensorThreshold;
import com.surekam.modules.agro.standardtaskitemsargsvalue.dao.StandardTaskItemsArgsValueDao;
import com.surekam.modules.agro.standardtaskitemsargsvalue.entity.StandardTaskItemsArgsValue;
import com.surekam.modules.agro.systementerprisestandards.dao.SystemEnterpriseStandardsDao;
import com.surekam.modules.agro.systementerprisestandards.entity.SystemEnterpriseStandards;
import com.surekam.modules.api.dto.req.AgroProductionBatchReq;
import com.surekam.modules.api.dto.resp.BatchCropInfoResp;
import com.surekam.modules.api.dto.resp.BatchHarvestDateResp;
import com.surekam.modules.api.entity.BatchGatewayModle;
import com.surekam.modules.api.utils.Send;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;


/**
 * 批次管理serivce
 * 
 * @author tangjun
 * @version 2019-04-15
 */

@Component
@EnableScheduling
@Transactional(readOnly = true)
public class BatchManageApiService {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserDao userDao;

	@Autowired
	private OfficeDao officeDao;
	
	@Autowired
	private BaseTreeDao baseTreeDao;
	
	@Autowired
	private SensorSetupDao sensorSetupDao;
	
	@Autowired
	private BaseManagerDao baseManagerDao;
	
	@Autowired
	private SensorThresholdDao sensorThresholdDao;
	
	@Autowired
	private ProductionBatchDao productionBatchDao;

	@Autowired
	private ProductBatchTaskDao productBatchTaskDao;
	
	@Autowired
	private ProductLibraryTreeDao productLibraryTreeDao;
	
	@Autowired
	private ProductBatchTaskService productBatchTaskService;
	
	@Autowired
	private ProductBatchTaskResolveDao productBatchTaskResolveDao;

	@Autowired
	private SystemEnterpriseStandardsDao systemEnterpriseStandardsDao;

	@Autowired
	private StandardTaskItemsArgsValueDao standardTaskItemsArgsValueDao;

	@Autowired
	private ProductBatchTaskResolveService productBatchTaskResolveService;

	@Autowired
	private SendingRecordsDao sendingRecordsDao;
	
	@Autowired
	private ProductGrowthCycleDao productGrowthCycleDao;
	
	@Autowired
	private ProductLibraryRelationDao productLibraryRelationDao;

	@Autowired
	private BreedingPlanDao breedingPlanDao;

	@Autowired
	private BatchPlanDao batchPlanDao;
	
	/**
	 * 
	 * Title: getBatchManageList Description: 查询批次信息
	 * 
	 * @param baseId
	 *            基地ID
	 * @param pageNo
	 *            条数
	 * @param pageSize
	 *            页数
	 * @param operType
	 *            类型
	 * @return
	 */
	public Page<ProductionBatch> getBatchManageList(String baseId, Integer pageNo, Integer pageSize, String operType) {
		int no = pageNo == null ? Global.DEFAULT_PAGENO : pageNo;
		int size = pageSize == null ? Global.DEFAULT_PAGESIZE : pageSize;
		Page<ProductionBatch> page = new Page<ProductionBatch>(no, size);
		page = findPage(page, baseId, operType);
		return page;
	}

	public Page<ProductionBatch> findPage(Page<ProductionBatch> page, String baseId, String type) {
		DetachedCriteria dc = productionBatchDao.createDetachedCriteria();
		if ("1".equals(type)) {
			dc.add(Restrictions.ne(ProductionBatch.FIELD_DEL_FLAG_XGXT, ProductionBatch.STATE_FLAG_DEL));
		} else if ("2".equals(type)) {
			dc.add(Restrictions.eq(ProductionBatch.FIELD_DEL_FLAG_XGXT, ProductionBatch.STATE_FLAG_DEL));
		}
		dc.add(Restrictions.eq("baseId", baseId));
		dc.addOrder(Order.desc("createTime"));
		Page<ProductionBatch> agroProductionBatchPage = productionBatchDao.find(page, dc);
		List<ProductionBatch> batchArrayList = agroProductionBatchPage.getList();

		for (ProductionBatch productionBatch : batchArrayList) {
			User user = userDao.get(productionBatch.getUserId());
			productionBatch.setUserName(user.getName());

			BaseTree agroBaseTree = baseTreeDao.get(productionBatch.getBaseId());
			productionBatch.setBaseName(agroBaseTree.getName());

			ProductLibraryTree agroProductLibraryTree = productLibraryTreeDao.get(productionBatch.getProductId());
			productionBatch.setProductName(agroProductLibraryTree.getProductCategoryName());

			SystemEnterpriseStandards systemEnterpriseStandards = systemEnterpriseStandardsDao.get(productionBatch.getStandardId());
			productionBatch.setStandardName(systemEnterpriseStandards.getStandardName());
		}
		return agroProductionBatchPage;
	}

	/**
	 * 新增或修改批次信息
	 * 
	 * @param user
	 *            用户信息
	 * @param agroProductionBatchReq
	 *            请求参数
	 * @return
	 * @throws ParseException
	 */
	@Transactional(readOnly = false)
	public ResultBean<String> savaBatchManage(User user, AgroProductionBatchReq req) throws Exception {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isBlank(req.getUserId())) {
			req.setUserId(user.getId());
		}
		ProductionBatch agroProductionBatch = new ProductionBatch();
		// 复制页面属性信息到基地实体对象内
		BeanUtils.copyProperties(req, agroProductionBatch);
		if (StringUtils.isNotBlank(req.getSendTime())) {
			String sendTime = req.getSendTime();
			int i = sendTime.indexOf(":");
			sendTime = sendTime.substring(0, i) + sendTime.substring(i + 1);
			agroProductionBatch.setSendTime(sendTime);
		}
		// 判断新怎还是修改
		if (StringUtils.isNotBlank(req.getId())) {
			ProductionBatch batch = productionBatchDao.get(req.getId());
			if (!agroProductionBatch.getBatchCode().equals(batch.getBatchCode())) {
				BaseTree agroBaseTree = baseTreeDao.get(batch.getBaseId());
				List<BaseTree> queryOfficeList = baseTreeDao.queryOfficeList(agroBaseTree.getOfficeId());
				for (BaseTree tree : queryOfficeList) {
					List<ProductionBatch> list = productionBatchDao.findByBaseIdAndCreateTimeAndBatchCode(tree.getId(), DateUtils.getDate(), agroProductionBatch.getBatchCode());
					if (!list.isEmpty()) {
						return ResultUtil.error(ResultEnum.BATCH_CODE_EXIST.getCode(), ResultEnum.BATCH_CODE_EXIST.getMessage());
					}
				}
			}
			if ((!agroProductionBatch.getAdvanceDays().equals(batch.getAdvanceDays())) || (!agroProductionBatch.getSendTime().equals(batch.getSendTime()))) {
				List<ProductBatchTask> findByRegionId = productBatchTaskDao.findByRegionId(batch.getId());
				for (ProductBatchTask productBatchTask : findByRegionId) {
					List<ProductBatchTaskResolve> findByTaskId = productBatchTaskResolveDao .findByTaskId(productBatchTask.getId());
					for (ProductBatchTaskResolve productBatchTaskResolve : findByTaskId) {
						Date date = format.parse(productBatchTaskResolve.getDispatchTime());
						Date advanceDays = DateUtil.advanceDays(date, -Integer.valueOf(agroProductionBatch.getAdvanceDays()));
						productBatchTaskResolve.setSendDate((DateUtil.formatDate4(advanceDays) + agroProductionBatch.getSendTime()));
						productBatchTaskResolveDao.save(productBatchTaskResolve);
					}
				}
			}
			// 保存
			agroProductionBatch.setStatus(batch.getStatus());
			agroProductionBatch.setUpdateTime(new Date());
			agroProductionBatch.setUpdateUserId(user.getId());
			agroProductionBatch.setStates(ProductionBatch.STATE_FLAG_UPDATE);
			baseTreeDao.flush();
			baseTreeDao.clear();
			productionBatchDao.save(agroProductionBatch);
			// 修改农户的手机号码和邮箱
			BaseManager baseManager = baseManagerDao.getUserId(agroProductionBatch.getBaseId(), "1852c8e247744ff184e8c162eff44f4c");
			if (null != baseManager) {
				User userInfo = userDao.get(baseManager.getUserId());
				if (null != userInfo) {
					if (StringUtils.isNotBlank(agroProductionBatch.getPhone())) {
						userInfo.setPhone(agroProductionBatch.getPhone());
					}
					if (StringUtils.isNotBlank(agroProductionBatch.getEmail())) {
						userInfo.setEmail(agroProductionBatch.getEmail());
					}
					userDao.save(userInfo);
				}
			}
		} else {
			BaseTree baseTree = baseTreeDao.get(req.getBaseId());
			String treeStr = baseTreeDao.queryLikeOffice(baseTree.getOfficeId());
			if(StringUtils.isNotBlank(treeStr)) {
				List<ProductionBatch> list = productionBatchDao.findByInBaseIdAndCreateTimeAndBatchCode(treeStr, DateUtils.getDate(), req.getBatchCode());
				if (!list.isEmpty()) {
					return ResultUtil.error(ResultEnum.BATCH_CODE_EXIST.getCode(), ResultEnum.BATCH_CODE_EXIST.getMessage());
				}
			}
			//判断公司是否存在批次号
			if(productionBatchDao.isExistBatchCode(baseTree.getOfficeId(), req.getBatchCode())){
				return ResultUtil.error(ResultEnum.BATCH_CODE_EXIST.getCode(), ResultEnum.BATCH_CODE_EXIST.getMessage());
			}
			// 查询基地农户信息
			BaseManager userId = baseManagerDao.getUserId(baseTree.getId(), "1852c8e247744ff184e8c162eff44f4c");
			// 保存
			agroProductionBatch.setStatus("1");
			agroProductionBatch.setUserId(userId.getUserId());
			agroProductionBatch.setCreateTime(new Date());
			agroProductionBatch.setCreateUserId(user.getId());
			agroProductionBatch.setStates(ProductionBatch.STATE_FLAG_ADD);
			productionBatchDao.save(agroProductionBatch);
			// 锁住养殖年度计划不让删除
			if (StringUtils.isNotBlank(agroProductionBatch.getBatchPlanId())) {
				BatchPlan batchPlan = batchPlanDao.get(agroProductionBatch.getBatchPlanId());
				if (batchPlan!=null) {
					BreedingPlan breedingPlan = breedingPlanDao.get(batchPlan.getPlanId());
					breedingPlan.setIsLock("1");
					breedingPlan.setUpdateTime(new Date());
					breedingPlan.setStates("U");
					breedingPlan.setUpdateUserId(user.getId());
					breedingPlanDao.save(breedingPlan);
				}
			}
			final ProductionBatch batch = agroProductionBatch;
			final User u = user;
			System.out.println("start-----");
			ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	        cachedThreadPool.execute(new Runnable() {
	             @Override
	             public void run() {
	            	 System.out.println("task start-----");
	            	 productBatchTaskService.createFixedTask(batch, u);
	            	 System.out.println("task end-----");
	             }
	        });
	        System.out.println("end-----");
			// 结算任务表的发送时间段
			List<ProductBatchTask> findByRegionId = productBatchTaskDao.findByRegionId(agroProductionBatch.getId());
			for (ProductBatchTask productBatchTask : findByRegionId) {
				List<ProductBatchTaskResolve> findByTaskId = productBatchTaskResolveDao.findByTaskId(productBatchTask.getId());
				for (ProductBatchTaskResolve productBatchTaskResolve : findByTaskId) {
					Date date = format.parse(productBatchTaskResolve.getDispatchTime());
					Date advanceDays = DateUtil.advanceDays(date, -Integer.valueOf(agroProductionBatch.getAdvanceDays()==null?"0":agroProductionBatch.getAdvanceDays()));
					productBatchTaskResolve.setSendDate((DateUtil.formatDate4(advanceDays) + agroProductionBatch.getSendTime()));
					productBatchTaskResolveDao.save(productBatchTaskResolve);
				}
			}
			// 修改农户的手机号码和邮箱
			BaseManager baseManager = baseManagerDao.getUserId(agroProductionBatch.getBaseId(), "1852c8e247744ff184e8c162eff44f4c");
			if (null != baseManager) {
				User userInfo = userDao.get(baseManager.getUserId());
				if (StringUtils.isNotBlank(agroProductionBatch.getPhone())) {
					userInfo.setPhone(agroProductionBatch.getPhone());
				}
				if (StringUtils.isNotBlank(agroProductionBatch.getEmail())) {
					userInfo.setEmail(agroProductionBatch.getEmail());
				}
				userDao.save(userInfo);
			}
			
			List<ProductLibraryRelation> findByOfficeIdAndProductId = productLibraryRelationDao.findByOfficeIdAndProductId(baseTree.getOfficeId(), req.getProductId());
			if(findByOfficeIdAndProductId.isEmpty()) {
				ProductLibraryTree agroProductLibraryTree = productLibraryTreeDao.get(req.getProductId());
				Office office = officeDao.get(baseTree.getOfficeId());
				
				ProductLibraryRelation pojo = new ProductLibraryRelation();
				pojo.setOfficeId(office.getId());
				pojo.setOfficeIds(office.getParentIds());
				pojo.setParentId(agroProductLibraryTree.getId());
				pojo.setParentsIds(agroProductLibraryTree.getParentsIds());
				productLibraryRelationDao.save(pojo);
			}
		}
		return ResultUtil.success("Success");
	}

	/**
	 * 删除批次信息
	 * 
	 * @param user
	 *            用户信息
	 * @param id
	 *            主键ID
	 * @return
	 */
	@Transactional(readOnly = false)
	public String deleteBatchManage(User user, String id) {
		String hql = " select pbtr.id from ProductBatchTask pbtk,ProductBatchTaskResolve pbtr  where pbtk.id=pbtr.taskId and pbtk.regionId=:p1 and pbtr.executionStatus is not null ";
		List<String> ids = productionBatchDao.find(hql, new Parameter(id));
		// 如果存在已执行的任务，则不能删除批次
		if (ids != null && ids.size() > 0) {
			return "Fail";
		} else {
			// 如果不存在已执行的任务，则把批次和任务都物理删掉
			// 1删除任务计划
			String resolveDeleteHql = "delete from ProductBatchTaskResolve pbtr where pbtr.taskId in (select pbt.id from ProductBatchTask pbt" + " where pbt.regionId=:p1)";
			productionBatchDao.createQuery(resolveDeleteHql, new Parameter(id)).executeUpdate();
			// 2删除任务
			String batchTaskDeleteHql = "delete from ProductBatchTask" + " where regionId=:p1";
			productionBatchDao.createQuery(batchTaskDeleteHql, new Parameter(id)).executeUpdate();
			// 3删除批次
			String batchDeleteHql = "delete from ProductionBatch where id=:p1";
			productionBatchDao.createQuery(batchDeleteHql, new Parameter(id)).executeUpdate();
			return "Success";
		}
	}

	/**
	 * 获取作物标准信息
	 * 
	 * @param id
	 *            品种ID
	 * @return
	 */
	public List<BatchCropInfoResp> getCropStandardInfoList(String id) {
		List<BatchCropInfoResp> resp = new ArrayList<BatchCropInfoResp>();
		List<SystemEnterpriseStandards> findByProductId = systemEnterpriseStandardsDao.findByProductId(id);
		if (!findByProductId.isEmpty()) {
			for (SystemEnterpriseStandards sesList : findByProductId) {
				BatchCropInfoResp batchCropInfoResp = new BatchCropInfoResp();
				batchCropInfoResp.setId(sesList.getId());
				batchCropInfoResp.setCropName(sesList.getStandardName());
				resp.add(batchCropInfoResp);
			}
		}
		return resp;
	}

	/**
	 * 获取作物标准信息
	 * 
	 * @param id
	 *            生产模式ID
	 * @return
	 */
	public List<BatchCropInfoResp> getStandardInfoList(String id) {
		List<BatchCropInfoResp> resp = new ArrayList<BatchCropInfoResp>();
		List<SystemEnterpriseStandards> findByProductId = systemEnterpriseStandardsDao.findByModelId(id);
		if (!findByProductId.isEmpty()) {
			for (SystemEnterpriseStandards sesList : findByProductId) {
				BatchCropInfoResp batchCropInfoResp = new BatchCropInfoResp();
				batchCropInfoResp.setId(sesList.getId());
				batchCropInfoResp.setCropName(sesList.getStandardName());
				resp.add(batchCropInfoResp);
			}
		}
		return resp;
	}

	/**
	 * 根据作物标准Id查询详细信息
	 * 
	 * @param office
	 *            公司ID
	 * @param id
	 *            作物标准ID
	 * @return
	 * @throws Exception 
	 */
	public BatchHarvestDateResp getStandardIdQueryInfo(String officeId, String id) throws Exception {
		int size = 1;
		List<BaseTree> queryOfficeList = baseTreeDao.queryOfficeList(officeId);
		for (BaseTree agroBaseTree : queryOfficeList) {
			List<ProductionBatch> list = productionBatchDao.findByBaseIdAndCreateTime(agroBaseTree.getId(), DateUtils.getDate());
			for (int i = 0; i < list.size(); i++) {
				size += list.size();
			}
		}
		String length = String.valueOf(size);
		if (length.length() == 1) {
			length = "00" + size;
		} else if (length.length() == 2) {
			length = "0" + size;
		}
		BatchHarvestDateResp resp = new BatchHarvestDateResp();
		resp.setBatchCode(DateUtils.getDateYYYYMMDD() + length);
		resp.setBatchStartDate(DateUtils.getDate());
		resp.setBatchPlanHarvestDate(getEndDate(id, DateUtils.getDate()));
		return resp;
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
					return DateUtils.getDate();
				}
			}else{
				return DateUtils.getDate();
			}
		}else{
			return DateUtils.getDate();
		}
	}
	
	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		String beginDay = "2019-08-06";
		Date date=sdf.parse(beginDay);
	    Calendar calendar = Calendar.getInstance();  
	    calendar.setTime(date);  
	    calendar.add(Calendar.DAY_OF_MONTH, 2);  
	    date = calendar.getTime(); 
	    System.out.println(sdf.format(date));
	}
	
	/**
	 * 根据ID查询批次信息
	 * 
	 * @param id
	 *            批次ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public ProductionBatch getBatchManage(String id) {
		ProductionBatch productionBatch = productionBatchDao.get(id);
		productionBatchDao.clear();
		productionBatchDao.flush();
		if(StringUtils.isNotBlank(productionBatch.getSendTime())) {
			productionBatch.setSendTime(productionBatch.getSendTime().substring(0, 2) + ":" + productionBatch.getSendTime().substring(2, 4));
		}
		BaseManager baseManager = baseManagerDao.getUserId(productionBatch.getBaseId(), "1852c8e247744ff184e8c162eff44f4c");
		if (null != baseManager) {
			User user = userDao.get(baseManager.getUserId());
			if (null != user) {
				userDao.clear();
				userDao.flush();
				if (StringUtils.isNotBlank(user.getPhone())) {
					productionBatch.setPhone(user.getPhone());
				}
				if (StringUtils.isNotBlank(user.getEmail())) {
					productionBatch.setEmail(user.getEmail());
				}
			}
		}
		BaseTree agroBaseTree = baseTreeDao.get(productionBatch.getBaseId());
		productionBatch.setBaseName(agroBaseTree.getName());

		ProductLibraryTree agroProductLibraryTree = productLibraryTreeDao.get(productionBatch.getProductId());
		productionBatch.setCategoryId(agroProductLibraryTree.getParent().getId());

		productionBatch.setProductName(agroProductLibraryTree.getProductCategoryName());
		productionBatch.setProductImg(agroProductLibraryTree.getProductCategoryImgUrl());

		SystemEnterpriseStandards systemEnterpriseStandards = systemEnterpriseStandardsDao.get(productionBatch.getStandardId());
		productionBatch.setStandardName(systemEnterpriseStandards.getStandardName());
		return productionBatch;
	}

	public Page<ProductionBatch> getNHBatchList(Page<ProductionBatch> page, User user, String officeId) {
//		DetachedCriteria dc = productionBatchDao.createDetachedCriteria();
//		dc.add(Restrictions.ne(ProductionBatch.FIELD_DEL_FLAG_XGXT, ProductionBatch.STATE_FLAG_DEL));
//		if(StringUtils.isNotBlank(officeId)){
//			dc.add(Restrictions.in("baseId", "select"));
//		}else{
//			dc.add(Restrictions.eq("userId", user.getId()));
//		}
//		dc.addOrder(Order.asc("status")).addOrder(Order.desc("createTime"));
		String sql = "select a.* from t_agro_production_batch a where a.states<>'D'";
		if(StringUtils.isNotBlank(officeId)){
			sql+= " and a.base_id IN (SELECT b.id FROM t_agro_base_tree b WHERE b.office_id='" + officeId + "' AND b.states<>'D')";
		}else{
			sql+= " and user_id = '" + user.getId() + "'";
		}
		Page<ProductionBatch> productionBatchPage = productionBatchDao.findBySql(page, sql, new Parameter(), ProductionBatch.class);
		List<ProductionBatch> batchArrayList = productionBatchPage.getList();
		List<ProductionBatch> list = new ArrayList<ProductionBatch>();

		for (ProductionBatch productionBatch : batchArrayList) {
			ProductLibraryTree productLibraryTree = productLibraryTreeDao.get(productionBatch.getProductId());
			productionBatch.setProductName(productLibraryTree.getProductCategoryName());
			productionBatch.setProductImg(productLibraryTree.getProductCategoryImgUrl());
			// if (StringUtils.isBlank(productionBatch.getGrowthCycleName())) {
			if ("998".equals(productionBatch.getStatus())) {
				productionBatch.setGrowthCycleName("非正常完成");
			} else if ("999".equals(productionBatch.getStatus())) {
				productionBatch.setGrowthCycleName("正常完成");
			} else {
				productionBatch.setGrowthCycleName(productBatchTaskService.getGrowthCycleNameByBatchId(productionBatch.getId()));
			}
			productionBatch.setWwcTaskCount(productBatchTaskResolveService.getBatchCount(productionBatch.getId(), "0"));
			list.add(productionBatch);
		}
		productionBatchPage.setList(list);
		return productionBatchPage;
	}

	/**
	 * 
	 * Title: getOfficeQueryBatchInfoList Description: 根据公司或基地查询当前基地以及下级的批次信息
	 * 
	 * @param officeId
	 *            公司ID
	 * @param baseId
	 *            基地ID
	 * @param operType
	 *            状态类型
	 * @param isOffice
	 *            0：为是公司 1：基地
	 * @param pageno
	 *            页数
	 * @param pagesize
	 *            条数
	 * @return
	 */
	public Page<ProductionBatch> getOfficeQueryBatchInfoList(User user, String officeId, String baseId, String operType, String isOffice, Integer pageno, Integer pagesize) {
		int no = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int size = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<ProductionBatch> findPage = findOfficeQueryBatchInfoPage(user, no, size, officeId, baseId, operType, isOffice);
		return findPage;
	}

	/**
	 * 
	 * Title: getOfficeQueryBatchInfoList Description: 批次报表
	 * 
	 * @param officeId
	 *            公司ID
	 * @param baseId
	 *            基地ID
	 * @param operType
	 *            状态类型
	 * @param isOffice
	 *            0：为是公司 1：基地
	 * @param pageno
	 *            页数
	 * @param pagesize
	 *            条数
	 * @return
	 */
	public Page<ProductionBatchReport> getBatchReport(String officeId, String baseId,String batchCode, String operType, String isOffice, Integer pageno, Integer pagesize) {
		int no = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int size = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<ProductionBatchReport> findPage = getBatchReportPage(no, size, officeId, baseId,batchCode, operType, isOffice);
		return findPage;
	}
	/**
	 * Title: findOfficeQueryBatchInfoPage Description: 组装公司或基地查询当前基地以及下级的批次信息
	 * 
	 * @param size
	 *            条数
	 * @param no
	 *            页数
	 * @param page
	 *            分页
	 * @param officeId
	 *            公司
	 * @param baseId
	 *            基地
	 * @param operType
	 *            类型
	 * @param isOffice
	 *            0：为是公司 1：基地
	 * @return
	 */
	private Page<ProductionBatch> findOfficeQueryBatchInfoPage(User user ,int no, int size, String officeId, String baseId, String operType, String isOffice) {
		String str = "";
		if (StringUtils.isNotBlank(officeId) && "0".equals(isOffice)) {
			List<Office> findAllOfficesAndId = officeDao.findAllOfficesAndId(officeId, officeId);
			for (Office officePojo : findAllOfficesAndId) {
				str += "'" + officePojo.getId() + "'" + ",";
			}
			boolean flg = true;
			List<String> roleIdList = user.getRoleIdList();
			for (String string : roleIdList) {
				if ("1".equals(string) || "1fe2c8e247744ff184e8c162eff44f12".equals(string)) {
					flg = true;
					break;
				}
				if ("391192d4f9634982858634f12de44275".equals(string) || "1852c8e247744ff184e8c162eff44f4c".equals(string)) {
					flg = false;
				}
			}
			List<BaseTree> parentArrayList = null;
			if(flg == true) {
				parentArrayList = baseTreeDao.findByInOfficeId("(" + str.substring(0, str.length() - 1) + ")");
			}else {
				parentArrayList = baseTreeDao.getByOfficeIdAndUserId(user.getOffice().getId(), user.getId());
			}
			for (BaseTree baseTreePjo : parentArrayList) {
				str += "'" + baseTreePjo.getId() + "'" + ",";
			}
			str = str.substring(0, str.length() - 1);
			str = "(" + str + ")";
		} else if (StringUtils.isNotBlank(baseId) && "1".equals(isOffice)) {
			List<BaseTree> baseTreeArrayList = new ArrayList<BaseTree>();
			BaseTree pojo = baseTreeDao.get(baseId);
			baseTreeArrayList.add(pojo);
			List<BaseTree> findAgroBaseTrees = findAgroBaseTrees(pojo.getId());
			for (BaseTree baseTree : findAgroBaseTrees) {
				baseTreeArrayList.add(baseTree);
			}
			for (BaseTree baseTreePjo : baseTreeArrayList) {
				str += "'" + baseTreePjo.getId() + "'" + ",";
			}
			str = str.substring(0, str.length() - 1);
			str = "(" + str + ")";
		}
		Page<ProductionBatch> findByInBaseId = productionBatchDao.findByInBaseId(str, operType, no, size);
		List<ProductionBatch> list = findByInBaseId.getList(); 
		for (int i = 0; i < list.size(); i++) {
			ProductionBatch productionBatch = list.get(i);
			// 查询是否有农事记录，0没有，可以删除批次，1有，不能
			productionBatch.setIsDelete(productionBatchDao.isDeleteBatch(productionBatch.getId()));
			User use = userDao.get(productionBatch.getUserId());
			productionBatch.setUserName(use.getName());

			BaseTree agroBaseTree = baseTreeDao.get(productionBatch.getBaseId());
			productionBatch.setBaseName(agroBaseTree.getName());

			ProductLibraryTree agroProductLibraryTree = productLibraryTreeDao.get(productionBatch.getProductId());
			productionBatch.setProductName(agroProductLibraryTree.getProductCategoryName());

			SystemEnterpriseStandards systemEnterpriseStandards = systemEnterpriseStandardsDao.get(productionBatch.getStandardId());
			productionBatch.setStandardName(systemEnterpriseStandards.getStandardName());
			
			List<Map<String, String>> listByRegionId = standardTaskItemsArgsValueDao.listByRegionId(productionBatch.getId(), "采收类型");
			if (!listByRegionId.isEmpty()) {
				productionBatch.setRecoveryRecord("0");
			} else {
				productionBatch.setRecoveryRecord("1");
			}
		}
		return findByInBaseId;
	}
	
	public List<Office> findOffices(String officeId) {
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(officeId)) {
			dc.createAlias("parent", "office");
			Office office = officeDao.get(officeId);
			dc.add(Restrictions.like("parentIds", office.getParentIds() + officeId + ",", MatchMode.START));
		}
		dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
		return officeDao.find(dc);
	}

	public List<BaseTree> findAgroBaseTrees(String id) {
		DetachedCriteria dc = baseTreeDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(id)) {
			dc.createAlias("parent", "office");
			BaseTree agroBaseTree = baseTreeDao.get(id);
			dc.add(Restrictions.like("parentIds", agroBaseTree.getParentIds() + agroBaseTree.getId() + ",", MatchMode.START));
		}
		dc.addOrder(Order.asc("sort"));
		return baseTreeDao.find(dc);
	}
	


	@Transactional(readOnly = false)
	public void finishedBatch(String batchId, String remark, String finishStatus, String finishTime, User user) throws ParseException {
		// 如果结束日期为空，则默认为当前日期
		if (StringUtils.isBlank(finishTime)) {
			finishTime = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date parse = sdf.parse(finishTime);
			finishTime = DateUtil.formatDate(parse);
		}
		// 修改当前批次的状态
		ProductionBatch productionBatch = productionBatchDao.get(batchId);
		// 修改当前批次的任务的状态
		productionBatch.setRemark(remark);
		productionBatch.setBatchEndDate(finishTime);
		productionBatch.setStatus(finishStatus);
		productionBatch.setUpdateTime(new Date());
		productionBatch.setUpdateUserId(user.getId());
		productionBatchDao.save(productionBatch);
		// 物理删除结束日期之后未完成的执行计划
		String hql = " delete from ProductBatchTaskResolve pbtr" + " where pbtr.dispatchTime>:p1"
				+ " and pbtr.executionStatus is null " + " and pbtr.taskId in("
				+ " select pbtk.id from ProductBatchTask pbtk" + " where pbtk.regionId =:p2)";
		productionBatchDao.createQuery(hql, new Parameter(finishTime + " 23:59:59", batchId)).executeUpdate();
		// 物理删除结束日期之后的未执行过得任务
		String deleteResolveHql = "delete from ProductBatchTask pbt where pbt.startDate >:p1 and pbt.regionId =:p2 "
				+ "and not exists(select 1 from ProductBatchTaskResolve pbtr where pbtr.taskId = pbt.id "
				+ "and pbtr.executionStatus is not null)";
		productionBatchDao.createQuery(deleteResolveHql, new Parameter(DateUtils.parseDate(finishTime + " 23:59:59"), batchId)).executeUpdate();
	}

	/**
	 * Title: findByUserInfo Description: 根据基地查询农户信息
	 * 
	 * @param baseId
	 *            基地ID
	 * @param operType
	 *            操作类型 1 校验手机号 2 校验邮箱
	 * @return
	 */
	public String findByUserInfo(String baseId, String operType) {
		BaseManager baseManager = baseManagerDao.getUserId(baseId, "1852c8e247744ff184e8c162eff44f4c");
		if (baseManager == null) {
			return "3";
		}
		User user = userDao.get(baseManager.getUserId());
		if ("1".equals(operType)) {
			if (StringUtils.isNotBlank(user.getPhone())) {
				return "0";
			} else {
				return "1";
			}
		} else {
			if (StringUtils.isNotBlank(user.getEmail())) {
				return "0";
			} else {
				return "2";
			}
		}
	}
	
	public List<ProductionBatch> getHasTaskListBatchList(String officeId, String baseId, String operType, String isOffice, String startDate, String endDate) {
		List<String> baseIds = new ArrayList<String>();
		if ("0".equals(isOffice)) {// 公司
			// 查询公司下所有的基地
			List<BaseTree> baseTrees = baseTreeDao.queryOfficeList(officeId);
			if (baseTrees != null && baseTrees.size() > 0) {
				for (BaseTree baseTree : baseTrees) {
					baseIds.add(baseTree.getId());
				}
			}
			if (baseIds.size()<=0) {
				return null;
			}
		} else {// 基地
			baseIds.add(baseId);
		}
		// 查询有作业记录的批次
		String hql = "select pb "
				+ " from ProductionBatch pb , ProductBatchTask pbt,ProductBatchTaskResolve pbtr" 
				+ " where pb.id = pbt.regionId "
				+ " and pbt.id=pbtr.taskId "
				+ " and pb.states <> 'D'" 
				+ " and pbt.states <> 'D'" 
				+ " and pbtr.states <> 'D'" 
				+ " and pb.baseId in (:p1)";
		if (StringUtils.isNotBlank(startDate)) {
			hql += " and pbtr.finishTime >= '" + startDate.substring(0, 10) + "'";
		}
		if (StringUtils.isNotBlank(endDate)) {
			hql += " and pbtr.finishTime <= '" + endDate + "'";
		}
		hql += " order by pbtr.finishTime asc";
		List<ProductionBatch> batchs = productBatchTaskDao.find(hql, new Parameter(new Object[] { baseIds.toArray() }));
		return batchs;
	}

	
	/**
	 * Title: consoleInfo Description: 定时任务提醒
	 */
	@Transactional(readOnly = false)
//	@Scheduled(cron = "0 */1 * * * ?")
	public void consoleInfo() {
		String format = DateUtils.getDateTime();
		List<BaseTree> all = baseTreeDao.all();
		for (BaseTree baseTree : all) {
			List<ProductionBatch> findByBaseId = productionBatchDao.findByBaseId(baseTree.getId());
			for (ProductionBatch productionBatch : findByBaseId) {
				if ("2".equals(productionBatch.getSmsStatus()) || "2".equals(productionBatch.getMailStatus())) {
					List<ProductBatchTask> findByRegionId = productBatchTaskDao.findByRegionId(productionBatch.getId());
					Integer size = 0;
					String standardItemName = "";
					String officeNmae = "";
					String date = "";
					for (ProductBatchTask productBatchTask : findByRegionId) {
						size = size + productBatchTaskResolveDao.count(format, productBatchTask.getId(), "0");
						List<String> findBySendDateGroupBy = productBatchTaskResolveDao.findBySendDateGroupBy(format, productBatchTask.getId(), "0");
						List<ProductBatchTaskResolve> findBySendDateAndTaskId = productBatchTaskResolveDao.findBySendDateAndTaskIdAndSendStates(format, productBatchTask.getId(), "0");
						for (ProductBatchTaskResolve productBatchTaskResolve : findBySendDateAndTaskId) {
							productBatchTaskResolve.setSendStates("1");
							productBatchTaskResolveDao.save(productBatchTaskResolve);
						}
						if (!findBySendDateGroupBy.isEmpty()) {
							date = findBySendDateGroupBy.get(0);
						}
						standardItemName += productBatchTask.getStandardItemName() + "、";
					}
					if (size != 0 && StringUtils.isNotBlank(standardItemName)) {
						BaseManager userId = baseManagerDao.getUserId(baseTree.getId(), "1852c8e247744ff184e8c162eff44f4c");
						standardItemName = standardItemName.substring(0, standardItemName.length() - 1);
						List<Office> findByParentId = officeDao.findByParentId("1");
						for (Office office : findByParentId) {
							List<Office> findByParentIdAndLikeParentIds = officeDao.findByParentIdAndLikeParentIds(baseTree.getOfficeId(), office.getId());
							if (!findByParentIdAndLikeParentIds.isEmpty()) {
								officeNmae = findByParentIdAndLikeParentIds.get(0).getName();
							}
						}
						if (StringUtils.isNotBlank(productionBatch.getAdvanceDays())) {
							int day = Integer.valueOf(productionBatch.getAdvanceDays());
							if (day == 0) {
								date = "今天";
							}
							if (day == 1) {
								date = "明天";
							}
							if (day == 2) {
								date = "后天";
							}
						}
						User user = userDao.get(userId.getUserId());
						String str = "尊敬的" + user.getName() + "，请注意，您有" + size + "条农事任务：" + standardItemName + "，需要在" + date + "执行，请登陆APP查看任务详情，并记录执行情况。批次编号：" + productionBatch.getBatchCode();
						if ("2".equals(productionBatch.getSmsStatus()) && StringUtils.isNotBlank(user.getPhone())) {
							Send.sendMessage(user.getPhone(), str, officeNmae);
						}
						if ("2".equals(productionBatch.getMailStatus()) && StringUtils.isNotBlank(user.getEmail())) {
							try {
								Send.sendEmail(user.getEmail(), officeNmae, str);
							} catch (MessagingException e) {
								logger.error("定时器执行错误" + e.toString());
							}
						}
					}
				}
			}
		}
		System.out.println(format);
	}

	/**
	 * 
	 * Title: timingSensor Description: 定时监控传感器指标30s检测一次
	 */
	@Transactional(readOnly = false)
//	@Scheduled(cron = "*/30 * * * * ?")
	public void timingSensor() {
		List<SendingRecords> listSend = new ArrayList<SendingRecords>();
		List<BatchGatewayModle> findByGatewayIdList = sensorSetupDao.findGatewayIdList(DateUtil.getCurrDateTime2());
		List<CopyOfSensorThreshold> findBySensorThreshold = sensorThresholdDao.findBySensorThreshold();
		for (int j = 0; j < findByGatewayIdList.size(); j++) {
			BatchGatewayModle objGatewayId = findByGatewayIdList.get(j);
			Map<String, String> params = new HashMap<String, String>();
			params.put("gids", objGatewayId.getGatewayId());
			String postRequest = Client.fromHttp("http://sensor.sureserve.cn/interfaces/gateway_handler.ashx?action=gateway_targets", params);
			if(StringUtils.isBlank(postRequest)){
				continue;
			}
			JSONObject obj = new JSONObject(postRequest);
			if ("0".equals(obj.get("result").toString())) {
				JSONArray jsonArray = obj.getJSONArray("data");
				if (jsonArray.length() == 0) {
					continue;
				}
				// 获取公司
				String officeName = objGatewayId.getOfficeName();
				List<CopyOfBaseManager> baseManagerList = baseManagerDao.getCopyBaseManagerList(objGatewayId.getBaseId());
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					// 指标名称
					String targetName = jsonObject.get("TargetName").toString();
					// 实时指标值
					String measureData = jsonObject.get("MeasureData").toString();
					if (StringUtils.isNotBlank(measureData) && !"--".equals(measureData)) {
						for (CopyOfSensorThreshold sensorThreshold : findBySensorThreshold) {
							if (!targetName.equals(sensorThreshold.getLabel()) || !objGatewayId.getStandardId().equals(sensorThreshold.getSysEntStandardId())) {
								continue;
							}
							BigDecimal realTimeValue = new BigDecimal(measureData);
							if (StringUtils.isNotBlank(sensorThreshold.getMaxValue())) {
								BigDecimal max = new BigDecimal(sensorThreshold.getMaxValue());
								if (realTimeValue.compareTo(max) == 1) {
									for (CopyOfBaseManager baseManager : baseManagerList) {
										// 基地负责人
										if("391192d4f9634982858634f12de44275".equals(baseManager.getRoleId())) {
											User user = baseManager.getUser();
											if (StringUtils.isNotBlank(user.getPhone())&&findSendingRecords(user.getPhone(),"短信",objGatewayId.getBatchCode()+targetName)) {
												String str = "尊敬的：" + user.getName() + "，请注意，区域【" + objGatewayId.getBaseName() + "】，" + "批次号【" + objGatewayId.getBatchCode() + "】的农产品【" + objGatewayId.getProductName() + "】" + "，现场" + targetName + " 现在为" + measureData + " ，超过（低于）阈值" + max;
												SendingRecords sendingRecords = new SendingRecords();
												sendingRecords.setSendUserId(user.getId());
												sendingRecords.setSendAddress(user.getPhone());
												sendingRecords.setSendType("短信");
												sendingRecords.setSendMessage(str);
												sendingRecords.setMessageType(objGatewayId.getBatchCode() + targetName);
												// 防止重复发送
												if (findSendingRecords(listSend, sendingRecords)) {
													continue;
												}
												String resultMessage = Send.sendMessage(user.getPhone(), str, officeName);
												sendingRecords.setResult(resultMessage);
												sendingRecords.setCreateTime(new Date());
												sendingRecordsDao.save(sendingRecords);
												listSend.add(sendingRecords);
											}
											if (StringUtils.isNotBlank(user.getEmail())&&findSendingRecords(user.getEmail(),"邮箱",objGatewayId.getBatchCode()+targetName)) {
												String str = "尊敬的：" + user.getName() + "，请注意，区域【" + objGatewayId.getBaseName() + "】，" + "批次号【" + objGatewayId.getBatchCode() + "】的农产品【" + objGatewayId.getProductName() + "】" + "，现场" + targetName + " 现在为" + measureData + " ，超过（低于）阈值" + max;
												try {
													SendingRecords sendingRecords = new SendingRecords();
													sendingRecords.setSendUserId(user.getId());
													sendingRecords.setSendAddress(user.getEmail());
													sendingRecords.setSendType("邮箱");
													sendingRecords.setSendMessage(str);
													sendingRecords.setMessageType(objGatewayId.getBatchCode()+targetName);
													// 防止重复发送
													if(findSendingRecords(listSend,sendingRecords)){
														continue;
													}
													Send.sendEmail(user.getEmail(), officeName, str);
													sendingRecords.setCreateTime(new Date());
													sendingRecordsDao.save(sendingRecords);
													listSend.add(sendingRecords);
													
												} catch (MessagingException e) {
													logger.error("定时器发送邮件错误" + e.toString());
												}
											}
											
										}else if("1852c8e247744ff184e8c162eff44f4c".equals(baseManager.getRoleId())) {
											
											User user = baseManager.getUser();
											if ("2".equals(objGatewayId.getSmsStatus()) && StringUtils.isNotBlank(user.getPhone())&&findSendingRecords(user.getPhone(),"短信",objGatewayId.getBatchCode()+targetName)) {
												String str = "尊敬的：" + user.getName() + "，请注意，区域【" + objGatewayId.getBaseName() + "】，" + "批次号【" + objGatewayId.getBatchCode() + "】的农产品【" + objGatewayId.getProductName() + "】" + "，现场" + targetName + " 现在为" + measureData + " ，超过（低于）阈值" + max;
												SendingRecords sendingRecords = new SendingRecords();
												sendingRecords.setSendUserId(user.getId());
												sendingRecords.setSendAddress(user.getPhone());
												sendingRecords.setSendType("短信");
												sendingRecords.setSendMessage(str);
												sendingRecords.setMessageType(objGatewayId.getBatchCode()+targetName);
												// 防止重复发送
												if(findSendingRecords(listSend,sendingRecords)){
													continue;
												}
												String resultMessage = Send.sendMessage(user.getPhone(), str, officeName);
												sendingRecords.setResult(resultMessage);
												sendingRecords.setCreateTime(new Date());
												sendingRecordsDao.save(sendingRecords);
												listSend.add(sendingRecords);
											}
											if ("2".equals(objGatewayId.getMailStatus()) && StringUtils.isNotBlank(user.getEmail())&&findSendingRecords(user.getEmail(),"邮箱",objGatewayId.getBatchCode()+targetName)) {
												String str = "尊敬的：" + user.getName() + "，请注意，区域【" + objGatewayId.getBaseName() + "】，" + "批次号【" + objGatewayId.getBatchCode() + "】的农产品【" + objGatewayId.getProductName() + "】" + "，现场" + targetName + " 现在为" + measureData + " ，超过（低于）阈值" + max;
												try {
													SendingRecords sendingRecords = new SendingRecords();
													sendingRecords.setSendUserId(user.getId());
													sendingRecords.setSendAddress(user.getEmail());
													sendingRecords.setSendType("邮箱");
													sendingRecords.setSendMessage(str);
													sendingRecords.setMessageType(objGatewayId.getBatchCode()+targetName);
													// 防止重复发送
													if(findSendingRecords(listSend,sendingRecords)){
														continue;
													}
													Send.sendEmail(user.getEmail(), officeName, str);
													sendingRecords.setCreateTime(new Date());
													sendingRecordsDao.save(sendingRecords);
													listSend.add(sendingRecords);
												} catch (MessagingException e) {
													logger.error("定时器发送邮件错误" + e.toString());
												}
											}
											
										}
									}
								}
							}
							
							if(StringUtils.isNotBlank(sensorThreshold.getMinValue())) {
								BigDecimal min = new BigDecimal(sensorThreshold.getMinValue());
								if (realTimeValue.compareTo(min)==-1) {
									for (CopyOfBaseManager baseManager : baseManagerList) {
										// 基地负责人
										if("391192d4f9634982858634f12de44275".equals(baseManager.getRoleId())) {
											
											User user = baseManager.getUser();
											if (StringUtils.isNotBlank(user.getPhone())&&findSendingRecords(user.getPhone(),"短信",objGatewayId.getBatchCode()+targetName)) {
												String str = "尊敬的：" + user.getName() + "，请注意，区域【" + objGatewayId.getBaseName() + "】，" + "批次号【" + objGatewayId.getBatchCode() + "】的农产品【" + objGatewayId.getProductName() + "】" + "，现场" + targetName + " 现在为" + measureData + " ，超过（低于）阈值" + min;
												SendingRecords sendingRecords = new SendingRecords();
												sendingRecords.setSendUserId(user.getId());
												sendingRecords.setSendAddress(user.getPhone());
												sendingRecords.setSendType("短信");
												sendingRecords.setSendMessage(str);
												sendingRecords.setMessageType(objGatewayId.getBatchCode()+targetName);
												// 防止重复发送
												if(findSendingRecords(listSend,sendingRecords)){
													continue;
												}
												String resultMessage = Send.sendMessage(user.getPhone(), str, officeName);
												sendingRecords.setResult(resultMessage);
												sendingRecords.setCreateTime(new Date());
												sendingRecordsDao.save(sendingRecords);
												listSend.add(sendingRecords);
											}
											if (StringUtils.isNotBlank(user.getEmail())&&findSendingRecords(user.getEmail(),"邮箱",objGatewayId.getBatchCode()+targetName)) {
												String str = "尊敬的：" + user.getName() + "，请注意，区域【" + objGatewayId.getBaseName() + "】，" + "批次号【" + objGatewayId.getBatchCode() + "】的农产品【" + objGatewayId.getProductName() + "】" + "，现场" + targetName + " 现在为" + measureData + " ，超过（低于）阈值" + min;
												try {
													SendingRecords sendingRecords = new SendingRecords();
													sendingRecords.setSendUserId(user.getId());
													sendingRecords.setSendAddress(user.getEmail());
													sendingRecords.setSendType("邮箱");
													sendingRecords.setSendMessage(str);
													sendingRecords.setMessageType(objGatewayId.getBatchCode()+targetName);
													// 防止重复发送
													if(findSendingRecords(listSend,sendingRecords)){
														continue;
													}
													Send.sendEmail(user.getEmail(), officeName, str);
													sendingRecords.setCreateTime(new Date());
													sendingRecordsDao.save(sendingRecords);
													listSend.add(sendingRecords);
												} catch (MessagingException e) {
													logger.error("定时器发送邮件错误" + e.toString());
												}
											}
											
										}else if("1852c8e247744ff184e8c162eff44f4c".equals(baseManager.getRoleId())) {
											
											User user = baseManager.getUser();
											if ("2".equals(objGatewayId.getSmsStatus()) && StringUtils.isNotBlank(user.getPhone())&&findSendingRecords(user.getPhone(),"短信",objGatewayId.getBatchCode()+targetName)) {
												String str = "尊敬的：" + user.getName() + "，请注意，区域【" + objGatewayId.getBaseName() + "】，" + "批次号【" + objGatewayId.getBatchCode() + "】的农产品【" + objGatewayId.getProductName() + "】" + "，现场" + targetName + " 现在为" + measureData + " ，超过（低于）阈值" + min;
												SendingRecords sendingRecords = new SendingRecords();
												sendingRecords.setSendUserId(user.getId());
												sendingRecords.setSendAddress(user.getPhone());
												sendingRecords.setSendType("短信");
												sendingRecords.setSendMessage(str);
												sendingRecords.setMessageType(objGatewayId.getBatchCode()+targetName);
												// 防止重复发送
												if(findSendingRecords(listSend,sendingRecords)){
													continue;
												}
												String resultMessage = Send.sendMessage(user.getPhone(), str, officeName);
												sendingRecords.setResult(resultMessage);
												sendingRecords.setCreateTime(new Date());
												sendingRecordsDao.save(sendingRecords);
												listSend.add(sendingRecords);
											}
											if ("2".equals(objGatewayId.getMailStatus()) && StringUtils.isNotBlank(user.getEmail())&&findSendingRecords(user.getEmail(),"邮箱",objGatewayId.getBatchCode()+targetName)) {
												String str = "尊敬的：" + user.getName() + "，请注意，区域【" + objGatewayId.getBaseName() + "】，" + "批次号【" + objGatewayId.getBatchCode() + "】的农产品【" + objGatewayId.getProductName() + "】" + "，现场" + targetName + " 现在为" + measureData + " ，超过（低于）阈值" + min;
												try {
													SendingRecords sendingRecords = new SendingRecords();
													sendingRecords.setSendUserId(user.getId());
													sendingRecords.setSendAddress(user.getEmail());
													sendingRecords.setSendType("邮箱");
													sendingRecords.setSendMessage(str);
													sendingRecords.setMessageType(objGatewayId.getBatchCode()+targetName);
													// 防止重复发送
													if(findSendingRecords(listSend,sendingRecords)){
														continue;
													}
													Send.sendEmail(user.getEmail(), officeName, str);
													sendingRecords.setCreateTime(new Date());
													sendingRecordsDao.save(sendingRecords);
													listSend.add(sendingRecords);
												} catch (MessagingException e) {
													logger.error("定时器发送邮件错误" + e.toString());
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Title: findByOfficeAndBatchCode Description:提供加工系统的批次验证
	 * 
	 * @param batchCode
	 *            批次编码
	 * @param officeId 
	 *            公司ID
	 * @return
	 */
	public boolean findByOfficeAndBatchCode(String batchCode, String kuId) {
		if (StringUtils.isBlank(batchCode) || StringUtils.isBlank(kuId)) {
			return false;
		}
		List<Object> batchTaskList = productBatchTaskResolveService.getBatchCode(batchCode);
		if (batchTaskList.size()==0) {
			return false;
		}
		Object[] batchTaskObj = (Object[]) batchTaskList.get(0);
		List<ProductionBatch> findByBatchCodeAndOfficeId = productionBatchDao.findByBatchCodeAndKuId(batchTaskObj[0].toString(), kuId);
		if (!findByBatchCodeAndOfficeId.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean findSendingRecords(String sendAddress,String sendType,String messageType) {
		DetachedCriteria dc = sendingRecordsDao.createDetachedCriteria();
		dc.add(Restrictions.eq("sendAddress", sendAddress));
		dc.add(Restrictions.eq("sendType", sendType));
		dc.add(Restrictions.eq("messageType", messageType));
		Date day = new Date();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(day);
		// 将时分秒,毫秒域清零
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		dc.add(Restrictions.ge("createTime", cal1.getTime()));
		return sendingRecordsDao.find(dc).size()==0;
	}
	public boolean findSendingRecords(List<SendingRecords> listSend, SendingRecords sendingRecords){
		for(SendingRecords se:listSend){
			if(se.getSendType().equals(sendingRecords.getSendType())){
				if(se.getSendAddress().equals(sendingRecords.getSendAddress())){
					if(se.getMessageType().equals(sendingRecords.getMessageType())){
						return true;
					}
				}
			}
		}
		return false;
	}
	@Transactional(readOnly = false)
	public void save(User user,AgroProductionBatchReq req) {
		ProductionBatch productionBatch = productionBatchDao.get(req.getId());
		productionBatch.setQualityTestPath(req.getQualityTestPath());
		productionBatch.setQualityTestName(req.getQualityTestName());
		productionBatch.setConfirmationInformationPath(req.getConfirmationInformationPath());
		productionBatch.setConfirmationInformationName(req.getConfirmationInformationName());
		productionBatch.setUpdateTime(new Date());
		productionBatch.setUpdateUserId(user.getId());
		productionBatch.setStates(ProductionBatch.STATE_FLAG_UPDATE);
		productionBatchDao.save(productionBatch);
	}

	/**
	 * Title: findOfficeQueryBatchInfoPage Description: 批次报表
	 * 
	 * @param size
	 *            条数
	 * @param no
	 *            页数
	 * @param page
	 *            分页
	 * @param officeId
	 *            公司
	 * @param baseId
	 *            基地
	 * @param operType
	 *            类型
	 * @param isOffice
	 *            0：为是公司 1：基地
	 * @return
	 */
	private Page<ProductionBatchReport> getBatchReportPage(int no, int size, String officeId, String baseId,String batchCode, String operType, String isOffice) {
		String str = "";
		if (StringUtils.isNotBlank(officeId) && "0".equals(isOffice)) {
			List<Office> officeArraylist = new ArrayList<Office>();
			Office office = officeDao.get(officeId);
			officeArraylist.add(office);
			List<Office> list = findOffices(office.getId());
			for (Office officeList : list) {
				officeArraylist.add(officeList);
			}
			for (Office officePojo : officeArraylist) {
				str += "'" + officePojo.getId() + "'" + ",";
			}
			List<BaseTree> findByInOfficeId = baseTreeDao.findByInOfficeId("(" + str.substring(0, str.length() - 1) + ")");
			for (BaseTree baseTreePjo : findByInOfficeId) {
				str += "'" + baseTreePjo.getId() + "'" + ",";
			}
			str = str.substring(0, str.length() - 1);
			str = "(" + str + ")";
		} else if (StringUtils.isNotBlank(baseId) && "1".equals(isOffice)) {
			List<BaseTree> baseTreeArrayList = new ArrayList<BaseTree>();
			BaseTree pojo = baseTreeDao.get(baseId);
			baseTreeArrayList.add(pojo);
			List<BaseTree> findAgroBaseTrees = findAgroBaseTrees(pojo.getId());
			for (BaseTree baseTree : findAgroBaseTrees) {
				baseTreeArrayList.add(baseTree);
			}
			for (BaseTree baseTreePjo : baseTreeArrayList) {
				str += "'" + baseTreePjo.getId() + "'" + ",";
			}
			str = str.substring(0, str.length() - 1);
			str = "(" + str + ")";
		}
		Page<ProductionBatchReport> findByInBaseId = productionBatchDao.findBatchReport(str,batchCode, operType, no, size);
		List<ProductionBatchReport> list = findByInBaseId.getList(); 
		for (int i = 0; i < list.size(); i++) {
			ProductionBatchReport productionBatch = list.get(i);
			
			User user = userDao.get(productionBatch.getUserId());
			productionBatch.setUserName(user.getName());

			BaseTree agroBaseTree = baseTreeDao.get(productionBatch.getBaseId());
			productionBatch.setBaseName(agroBaseTree.getName());
			productionBatch.setBaseAcreage(agroBaseTree.getAcreage());
			Office officeName = officeDao.get(agroBaseTree.getOfficeId());
			productionBatch.setOfficeName(officeName.getName());
			ProductLibraryTree agroProductLibraryTree = productLibraryTreeDao.get(productionBatch.getProductId());
			productionBatch.setProductName(agroProductLibraryTree.getProductCategoryName());
			productionBatch.setProductImg(agroProductLibraryTree.getProductCategoryImgUrl());
			SystemEnterpriseStandards systemEnterpriseStandards = systemEnterpriseStandardsDao.get(productionBatch.getStandardId());
			productionBatch.setStandardName(systemEnterpriseStandards.getStandardName());
			List<StandardTaskItemsArgsValue> stiavList = standardTaskItemsArgsValueDao.findByRegionIdAndStates(productionBatch.getId(), "采收类型", "2");
			
			if(!stiavList.isEmpty()) {
				List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
				for (StandardTaskItemsArgsValue standardTaskItemsArgsValue : stiavList) {
					Map<String, Object> map = new HashMap<String, Object>();
					StandardTaskItemsArgsValue taskListIdAndArgsName = standardTaskItemsArgsValueDao.getTaskListIdAndArgsName(standardTaskItemsArgsValue.getTaskListId(), "采收量");
					map.put("id", standardTaskItemsArgsValue.getTaskItemArgsValue());
					map.put("value1", taskListIdAndArgsName.getTaskItemArgsValue());
					map.put("value2", taskListIdAndArgsName.getArgsUnit());
					listMap.add(map);
				}
				
				Map<String, Object> map2 = new HashMap<String, Object>();
				for (Map<String, Object> map1 : listMap) {
					String id = (String)map1.get("id");
					if(StringUtils.isNotBlank(id)){
						if (map2.containsKey(id)) {
							String value = (String) map2.get(id);
							String[] str1 = value.split("_");
							map2.put(id, str1[0] + "_" + (Integer.parseInt(map1.get("value1").toString()) + Integer.parseInt(str1[1])) + "_" + str1[2]);
						} else {
							map2.put(id, id + "_" + map1.get("value1").toString() + "_" + map1.get("value2").toString());
						}
					}
				}
				String result = "";
				for (String in : map2.keySet()) {
					String strs = (String) map2.get(in);
					result += strs.replaceAll("_", "") + ",";
				}
				if (StringUtils.isNotBlank(result)) {
					result = result.substring(0, result.length() - 1);
				}
				productionBatch.setRecoveryAmount(result);
			}
		}
		return findByInBaseId;
	}
	
	public static void longTimeMethod(String str){
		for(int i=0;i<100000;i++){
			System.out.println(str+i);
		}
	}
	
}