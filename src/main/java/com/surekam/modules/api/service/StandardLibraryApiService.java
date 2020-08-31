package com.surekam.modules.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.product.dao.ProductLibraryTreeDao;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.agro.productgrowthcycle.dao.ProductGrowthCycleDao;
import com.surekam.modules.agro.productgrowthcycle.entity.ProductGrowthCycle;
import com.surekam.modules.agro.productionmodel.dao.ProductionModelDao;
import com.surekam.modules.agro.productionmodel.entity.ProductionModel;
import com.surekam.modules.agro.productlibraryrelation.dao.ProductLibraryRelationDao;
import com.surekam.modules.agro.productlibraryrelation.entity.ProductLibraryRelation;
import com.surekam.modules.agro.sensorthreshold.dao.SensorThresholdDao;
import com.surekam.modules.agro.sensorthreshold.entity.SensorThreshold;
import com.surekam.modules.agro.sensorthreshold.service.SensorThresholdService;
import com.surekam.modules.agro.standarditemargs.dao.StandardItemArgsDao;
import com.surekam.modules.agro.standarditemargs.entity.StandardItemArgs;
import com.surekam.modules.agro.standarditemargsvalue.dao.StandardItemArgsValueDao;
import com.surekam.modules.agro.standarditemargsvalue.entity.StandardItemArgsValue;
import com.surekam.modules.agro.standarditems.dao.StandardItemsDao;
import com.surekam.modules.agro.standarditems.entity.StandardItems;
import com.surekam.modules.agro.standarditems.entity.vo.CopyStandardItemsVo;
import com.surekam.modules.agro.standarditems.entity.vo.StandardItemsVo;
import com.surekam.modules.agro.systementerprisestandards.dao.SystemEnterpriseStandardsDao;
import com.surekam.modules.agro.systementerprisestandards.entity.SystemEnterpriseStandards;
import com.surekam.modules.api.dto.resp.ProductGrowthCycleTreeResp;
import com.surekam.modules.api.dto.resp.SystemEnterpriseStandardsResp;
import com.surekam.modules.sys.dao.DictDao;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.Dict;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;

/**
 * 标准库Service
 * 
 * @author tangjun
 * @version 2019-04-23
 */
@Component
public class StandardLibraryApiService extends BaseService {

	/**
	 * 品种分类
	 */
	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;

	/**
	 * 品种分类
	 */
	@Autowired
	private ProductLibraryTreeDao productLibraryTreeDao;

	/**
	 * 获取种养殖种类
	 */
	@Autowired
	private SpeciesManageApiService speciesManageApiService;

	/**
	 * 传感器阈值表
	 */
	@Autowired
	private SensorThresholdService sensorThresholdService;

	/**
	 * 传感器阈值表
	 */
	@Autowired
	private SensorThresholdDao sensorThresholdDao;

	/**
	 * 标准库
	 */
	@Autowired
	private SystemEnterpriseStandardsDao systemEnterpriseStandardsDao;

	/**
	 * 生长周期阶段表
	 */
	@Autowired
	private ProductGrowthCycleDao productGrowthCycleDao;

	/**
	 * 标准作业项表
	 */
	@Autowired
	private StandardItemsDao standardItemsDao;

	/**
	 * 标准作业详细参数表
	 */
	@Autowired
	private StandardItemArgsDao standardItemArgsDao;

	/**
	 * 标准作业参数多项值表
	 */
	@Autowired
	private StandardItemArgsValueDao standardItemArgsValueDao;

	/**
	 * 机构
	 */
	@Autowired
	private OfficeDao officeDao;

	/**
	 * 字典
	 */
	@Autowired
	private DictDao dictDao;
	
	/**
	 * 生产模式
	 */
	@Autowired
	private ProductionModelDao productionModelDao;
	
	@Autowired
	private ProductLibraryRelationDao productLibraryRelationDao;

	/**
	 * 
	 * Title: getStandardLibraryTreeList Description: 获取标准库树状
	 * 
	 * @param user
	 *            用戶
	 * @param standardId
	 * @return
	 */
	public List<SystemEnterpriseStandardsResp> getStandardLibraryTreeList(User user, String standardId) {
		// 标准对象
		List<SystemEnterpriseStandardsResp> stsRespList = new ArrayList<SystemEnterpriseStandardsResp>();
		// 生命周期对象
		List<ProductGrowthCycleTreeResp> pgcRespList = new ArrayList<ProductGrowthCycleTreeResp>();

		// 查询标准库数据
		SystemEnterpriseStandards sesPojo = systemEnterpriseStandardsDao.get(standardId);
		if (sesPojo != null) {
			SystemEnterpriseStandardsResp sesResp = new SystemEnterpriseStandardsResp();
			BeanUtils.copyProperties(sesPojo, sesResp);
			String standardName = sesPojo.getStandardName() + "_" + sesPojo.getVersionName();
			sesResp.setName(standardName);
			sesResp.setStandardsType("1");
			stsRespList.add(sesResp);

			// 查詢生命周期表
			List<ProductGrowthCycle> pgcList = productGrowthCycleDao.getProductGrowthCycleList(sesPojo.getId());
			for (ProductGrowthCycle pgcPojo : pgcList) {

				ProductGrowthCycleTreeResp pgcResp = new ProductGrowthCycleTreeResp();
				BeanUtils.copyProperties(pgcPojo, pgcResp);
				if (StringUtils.isNotBlank(pgcPojo.getBeginDay()) && StringUtils.isNotBlank(pgcPojo.getEndDay())) {
					pgcResp.setName(
							pgcPojo.getCycleName() + "【" + pgcPojo.getBeginDay() + "—" + pgcPojo.getEndDay() + "】");
				} else {
					pgcResp.setName(pgcPojo.getCycleName());
				}
				pgcResp.setStandardsType("2");
				pgcRespList.add(pgcResp);
				sesResp.setChildList(pgcRespList);

				// 标准作业项表
				List<StandardItems> findByGrowthCycleIdList = standardItemsDao
						.findByGrowthCycleIdAscList(pgcPojo.getId());

				List<StandardItemsVo> stVo = new ArrayList<StandardItemsVo>();
				for (StandardItems standardItems : findByGrowthCycleIdList) {

					List<StandardItems> findAgroBaseTrees = findAgroBaseTrees(user, standardItems);
					StandardItemsVo treeList = treeList(findAgroBaseTrees, standardItems);
					if ((treeList != null) && (findAgroBaseTrees != null && findAgroBaseTrees.size() > 0)) {
						stVo.add(treeList);
						pgcResp.setChildList(stVo);
					} else {
						StandardItemsVo vo = new StandardItemsVo();
						BeanUtils.copyProperties(standardItems, vo);

						if ("1".equals(standardItems.getOperationType())) {
							vo.setStandardsType("3");
						} else if ("2".equals(standardItems.getOperationType())) {
							vo.setStandardsType("4");
						}
						vo.setId(standardItems.getId());
						vo.setName(standardItems.getItemName());
						stVo.add(vo);
						pgcResp.setChildList(stVo);
					}

				}

			}
		}
		return stsRespList;
	}

	/**
	 * 查询标准作业项表
	 * 
	 * @param user
	 *            用户
	 * @param id
	 *            主键
	 * @return
	 */
	public List<StandardItems> findAgroBaseTrees(User user, StandardItems standardItems) {
		DetachedCriteria dc = standardItemsDao.createDetachedCriteria();
		if (standardItems != null) {
			dc.createAlias("parent", "id");
			dc.add(Restrictions.like("parentIds", standardItems.getParentIds() + standardItems.getId() + ",",
					MatchMode.START));
		}
		if (!user.isAdmin()) {
			dc.add(dataScopeFilter(user, dc.getAlias(), ""));
		}
		dc.add(Restrictions.ne("states", StandardItems.STATE_FLAG_DEL));
		dc.addOrder(Order.asc("sort"));
		return standardItemsDao.find(dc);
	}

	public StandardItemsVo treeList(List<StandardItems> list, StandardItems standardItems) {
		StandardItemsVo standardItemsVo = new StandardItemsVo();
		BeanUtils.copyProperties(standardItems, standardItemsVo, new String[] { "childList" });

		standardItemsVo.setId(standardItems.getId());
		standardItemsVo.setName(standardItems.getItemName());
		if ("1".equals(standardItems.getOperationType())) {
			standardItemsVo.setStandardsType("3");
		} else if ("2".equals(standardItems.getOperationType())) {
			standardItemsVo.setStandardsType("4");
		}

		for (StandardItems standardItemsTemp : list) {

			if (standardItems.getId().equals(standardItemsTemp.getParent().getId())) {
				StandardItemsVo standardItemsVoTemp = new StandardItemsVo();
				BeanUtils.copyProperties(standardItemsTemp, standardItemsVoTemp, new String[] { "childList" });

				standardItemsVoTemp.setId(standardItemsTemp.getId());
				standardItemsVoTemp.setName(standardItemsTemp.getItemName());
				if ("1".equals(standardItemsTemp.getOperationType())) {
					standardItemsVoTemp.setStandardsType("3");
				} else if ("2".equals(standardItemsTemp.getOperationType())) {
					standardItemsVoTemp.setStandardsType("4");
				}

				if (standardItemsTemp.getChildList().size() > 0) {
					standardItemsVoTemp = treeList(list, standardItemsTemp);
				}
				standardItemsVo.getChildList().add(standardItemsVoTemp);
			}
		}
		return standardItemsVo;
	}

	public CopyStandardItemsVo CopyTreeList(List<StandardItems> list, StandardItems standardItems) {
		CopyStandardItemsVo standardItemsVo = new CopyStandardItemsVo();
		BeanUtils.copyProperties(standardItems, standardItemsVo, new String[] { "childList" });
		for (StandardItems standardItemsTemp : list) {
			if (standardItems.getId().equals(standardItemsTemp.getParent().getId())) {
				CopyStandardItemsVo standardItemsVoTemp = new CopyStandardItemsVo();
				BeanUtils.copyProperties(standardItemsTemp, standardItemsVoTemp, new String[] { "childList" });
				if (standardItemsTemp.getChildList().size() > 0) {
					standardItemsVoTemp = CopyTreeList(list, standardItemsTemp);
				}
				standardItemsVo.getChildList().add(standardItemsVoTemp);
			}
		}
		return standardItemsVo;
	}

	/**
	 * 保存标准信息
	 * 
	 * @param standardItems
	 * @param user
	 * @return
	 */
	@Transactional(readOnly = false)
	public ResultBean<String> savaSystemEnterpriseStandards(SystemEnterpriseStandards sesPojo, User user) {
		// 查询选择的是否是品种的分类
		ProductLibraryTree productLibraryTree = productLibraryTreeService.get(sesPojo.getProductId());
		if (productLibraryTree == null) {
			return ResultUtil.error(ResultEnum.PRODUCT_CATEGORY_NULL.getCode(),
					ResultEnum.PRODUCT_CATEGORY_NULL.getMessage());
		}

		if (productLibraryTree != null && productLibraryTree.getIsProductCategory().equals("1")) {
			return ResultUtil.error(ResultEnum.PRODUCT_CATEGORY_CODE.getCode(),
					ResultEnum.PRODUCT_CATEGORY_CODE.getMessage());
		}
		// 新增或者修改
		if (StringUtils.isBlank(sesPojo.getId())) {
			// 校验标准名称和版本号是否存在
			List<SystemEnterpriseStandards> sysArrayList = systemEnterpriseStandardsDao
					.findByStandardNameAndVersionName(sesPojo.getStandardName(), sesPojo.getVersionName());
			if (sysArrayList != null && sysArrayList.size() > 0) {
				return ResultUtil.error(ResultEnum.TASK_DATA_NAME.getCode(), ResultEnum.TASK_DATA_NAME.getMessage());
			}
			sesPojo.setCreateTime(new Date());
			sesPojo.setCreateUserId(user.getId());
			sesPojo.setStates(SystemEnterpriseStandards.STATE_FLAG_ADD);
			systemEnterpriseStandardsDao.save(sesPojo);

			// 复制标准数据
			if (StringUtils.isNotBlank(sesPojo.getCopyFromSystemStandardsId())) {
				List<ProductGrowthCycle> productGrowthCycleList = productGrowthCycleDao
						.getProductGrowthCycleList(sesPojo.getCopyFromSystemStandardsId());
				// 生长周期
				for (ProductGrowthCycle pgcList : productGrowthCycleList) {
					ProductGrowthCycle pgcPojo = new ProductGrowthCycle();
					pgcPojo.setSysEntStandardId(sesPojo.getId());
					pgcPojo.setCycleName(pgcList.getCycleName());
					pgcPojo.setBeginDay(pgcList.getBeginDay());
					pgcPojo.setEndDay(pgcList.getEndDay());
					pgcPojo.setCreateTime(new Date());
					pgcPojo.setCreateUserId(user.getId());
					pgcPojo.setStates(ProductGrowthCycle.STATE_FLAG_ADD);
					productGrowthCycleDao.flush();
					productGrowthCycleDao.save(pgcPojo);

					// 查詢传感器信息
					List<SensorThreshold> findByGrowthCycleId = sensorThresholdService
							.findByGrowthCycleId(pgcList.getId());
					for (SensorThreshold sensorThreshold : findByGrowthCycleId) {
						SensorThreshold stPojo = new SensorThreshold();
						stPojo.setTargetId(sensorThreshold.getTargetId());
						stPojo.setGrowthCycleId(pgcPojo.getId());
						stPojo.setMaxValue(sensorThreshold.getMaxValue());
						stPojo.setMinValue(sensorThreshold.getMinValue());
						stPojo.setCreateTime(new Date());
						stPojo.setCreateUserId(user.getId());
						stPojo.setStates(SensorThreshold.STATE_FLAG_ADD);
						sensorThresholdDao.save(stPojo);
					}

					// 标准作业项表 查询标准父节点
					List<StandardItems> findByGrowthCycleIdList = standardItemsDao
							.findByGrowthCycleIdAscList(pgcList.getId());
					for (int i = 0; i < findByGrowthCycleIdList.size(); i++) {
						StandardItems standardItems = findByGrowthCycleIdList.get(i);

						List<StandardItems> findAgroBaseTrees = findAgroBaseTrees(user, standardItems);
						CopyStandardItemsVo treeList = CopyTreeList(findAgroBaseTrees, standardItems);

						StandardItems siPojo = new StandardItems();
						StandardItems parent = new StandardItems();
						parent.setId("0");
						siPojo.setCreateTime(new Date());
						siPojo.setParent(parent); // 父节点ID
						siPojo.setParentIds(treeList.getParentIds()); // 父节点IDS,用逗号隔开
						siPojo.setSystemStandardsId(sesPojo.getId()); // 标准库ID
						siPojo.setItemName(treeList.getItemName()); // 作业项名称
						siPojo.setItemCategoryId(treeList.getItemCategoryId()); // 作业项类别
						siPojo.setGrowthCycleId(pgcPojo.getId()); // 所属生长周期阶段ID
						siPojo.setGrowthCycleName(pgcPojo.getCycleName()); // 所属生长周期阶段名称
						siPojo.setStartDateNumber(treeList.getStartDateNumber()); // 作业项起始日
						siPojo.setEndDateNumber(treeList.getEndDateNumber()); // 作业项结束日
						siPojo.setWorkEstimation(treeList.getWorkEstimation()); // 工作量预估
						siPojo.setStandardsItemDescription(treeList.getStandardsItemDescription()); // 作业项描述
						siPojo.setIsProductDemands(treeList.getIsProductDemands()); // 是否为生产资料需求
						siPojo.setSingleOperationIntervalDay(treeList.getSingleOperationIntervalDay()); // 单次作业间隔天数
						siPojo.setFrequencyDuringTime(treeList.getFrequencyDuringTime()); // 时间段内完成频次
						siPojo.setTaskId(treeList.getTaskId()); // 任务ID
						siPojo.setBatchId(treeList.getBatchId()); // 批次ID
						siPojo.setSort(treeList.getSort()); // 排序
						siPojo.setOperationType(treeList.getOperationType()); // 作业类型 1：目录 2：表单
						siPojo.setCreateUserId(user.getId());
						siPojo.setStates(StandardItems.STATE_FLAG_ADD);
						standardItemsDao.flush();
						standardItemsDao.save(siPojo);
						standardItemsDao.flush();
						if ("2".equals(treeList.getOperationType())) {
							// 保存准作业详细参数表
							List<StandardItemArgs> findByItemsIdList = standardItemArgsDao
									.findByItemsIdList(treeList.getId());
							for (StandardItemArgs siaList : findByItemsIdList) {
								StandardItemArgs siaPojo = new StandardItemArgs();
								siaPojo.setItemsId(siPojo.getId());// 作业项ID
								siaPojo.setArgsName(siaList.getArgsName());// 参数名称
								siaPojo.setMaxValue(siaList.getMaxValue());// 最小值
								siaPojo.setMinValue(siaList.getMinValue());// 最大值
								siaPojo.setArgsValue(siaList.getArgsValue());// 参数值
								siaPojo.setArgsUnit(siaList.getArgsUnit());// 参数单位
								siaPojo.setArgsType(siaList.getArgsType());// 参数类型
								siaPojo.setArgsTypeName(siaList.getArgsTypeName());// 参数类型名稱
								siaPojo.setIsRequired(siaList.getIsRequired());// 是否必填
								siaPojo.setArgsDescription(siaList.getArgsDescription());// 参数描述
								siaPojo.setSort(siaList.getSort());// 排序
								siaPojo.setCreateTime(new Date());
								siaPojo.setCreateUserId(user.getId());
								siaPojo.setStates(StandardItemArgs.STATE_FLAG_ADD);
								standardItemArgsDao.flush();
								standardItemArgsDao.save(siaPojo);
								standardItemArgsDao.flush();

								// 保存参数列表信息
								List<StandardItemArgsValue> findByItemArgsId = standardItemArgsValueDao
										.findByItemArgsId(siaList.getId());
								if (findByItemArgsId != null && findByItemArgsId.size() > 0) {
									for (StandardItemArgsValue siavList : findByItemArgsId) {
										StandardItemArgsValue siavPojo = new StandardItemArgsValue();
										siavPojo.setItemArgsId(siaPojo.getId());
										siavPojo.setName(siavList.getName());
										siavPojo.setValue(siavList.getValue());
										siavPojo.setSort(siavList.getSort());
										siavPojo.setCreateTime(new Date());
										siavPojo.setCreateUserId(user.getId());
										siavPojo.setStates(StandardItemArgsValue.STATE_FLAG_ADD);
										standardItemArgsValueDao.flush();
										standardItemArgsValueDao.save(siavPojo);
										standardItemArgsValueDao.flush();
									}
								}
							}
						}

						if (treeList.getChildList() != null && treeList.getChildList().size() > 0) {
							this.backQueryStandardItemsTree(treeList.getChildList(), user, siPojo.getId(),
									sesPojo.getId(), pgcPojo);
						}
					}
				}
			}
		}

		if (StringUtils.isNotBlank(sesPojo.getId())) {
			SystemEnterpriseStandards pojo = systemEnterpriseStandardsDao.get(sesPojo.getId());
			if (!pojo.getStandardName().equals(sesPojo.getStandardName())
					|| !pojo.getVersionName().equals(sesPojo.getVersionName())) {
				List<SystemEnterpriseStandards> sysArrayList = systemEnterpriseStandardsDao
						.findByStandardNameAndVersionName(sesPojo.getStandardName(), sesPojo.getVersionName());
				if (sysArrayList != null && sysArrayList.size() > 0) {
					return ResultUtil.error(ResultEnum.TASK_DATA_NAME.getCode(),
							ResultEnum.TASK_DATA_NAME.getMessage());
				}
			}
			sesPojo.setCreateUserId(pojo.getId());
			sesPojo.setUpdateTime(new Date());
			sesPojo.setUpdateUserId(user.getId());
			sesPojo.setStates(SystemEnterpriseStandards.STATE_FLAG_UPDATE);
			systemEnterpriseStandardsDao.clear();
			systemEnterpriseStandardsDao.flush();
			systemEnterpriseStandardsDao.save(sesPojo);
		}
		return ResultUtil.success("Success");
	}

	/**
	 * 
	 * Title: backQueryStandardItemsTree Description: 反查询作业项信息
	 * 
	 * @param list
	 *            作业项
	 * @param user
	 *            用户
	 * @param parentNode
	 *            父级
	 * @param sesId
	 *            标准id
	 * @param pgcPojo
	 *            生长周期id
	 */
	public void backQueryStandardItemsTree(List<CopyStandardItemsVo> list, User user, String parentNode, String sesId,
			ProductGrowthCycle pgcPojo) {
		for (CopyStandardItemsVo csiVoPojo : list) {
			StandardItems standardItems = standardItemsDao.get(parentNode);
			StandardItems siPojo = new StandardItems();

			String parentIds = standardItems.getParentIds() + standardItems.getId() + ",";
			siPojo.setParentIds(parentIds); // 父节点IDS,用逗号隔开
			siPojo.setParent(standardItems); // 父节点ID
			siPojo.setSystemStandardsId(sesId); // 标准库ID
			siPojo.setItemName(csiVoPojo.getItemName()); // 作业项名称
			siPojo.setItemCategoryId(csiVoPojo.getItemCategoryId()); // 作业项类别
			siPojo.setGrowthCycleId(pgcPojo.getId()); // 所属生长周期阶段ID
			siPojo.setGrowthCycleName(pgcPojo.getCycleName()); // 所属生长周期阶段名称
			siPojo.setStartDateNumber(csiVoPojo.getStartDateNumber()); // 作业项起始日
			siPojo.setEndDateNumber(csiVoPojo.getEndDateNumber()); // 作业项结束日
			siPojo.setWorkEstimation(csiVoPojo.getWorkEstimation()); // 工作量预估
			siPojo.setStandardsItemDescription(csiVoPojo.getStandardsItemDescription()); // 作业项描述
			siPojo.setIsProductDemands(csiVoPojo.getIsProductDemands()); // 是否为生产资料需求
			siPojo.setSingleOperationIntervalDay(csiVoPojo.getSingleOperationIntervalDay()); // 单次作业间隔天数
			siPojo.setFrequencyDuringTime(csiVoPojo.getFrequencyDuringTime()); // 时间段内完成频次
			siPojo.setTaskId(csiVoPojo.getTaskId()); // 任务ID
			siPojo.setBatchId(csiVoPojo.getBatchId()); // 批次ID
			siPojo.setSort(csiVoPojo.getSort()); // 排序
			siPojo.setOperationType(csiVoPojo.getOperationType()); // 作业类型 1：目录 2：表单
			siPojo.setCreateTime(new Date());
			siPojo.setCreateUserId(user.getId());
			siPojo.setStates(StandardItems.STATE_FLAG_ADD);
			standardItemsDao.flush();
			standardItemsDao.save(siPojo);
			standardItemsDao.flush();

			if ("2".equals(csiVoPojo.getOperationType())) {
				// 保存准作业详细参数表
				List<StandardItemArgs> findByItemsIdList = standardItemArgsDao.findByItemsIdList(csiVoPojo.getId());
				for (StandardItemArgs siaList : findByItemsIdList) {
					StandardItemArgs siaPojo = new StandardItemArgs();
					siaPojo.setItemsId(siPojo.getId());// 作业项ID
					siaPojo.setArgsName(siaList.getArgsName());// 参数名称
					siaPojo.setMaxValue(siaList.getMaxValue());// 最小值
					siaPojo.setMinValue(siaList.getMinValue());// 最大值
					siaPojo.setArgsValue(siaList.getArgsValue());// 参数值
					siaPojo.setArgsUnit(siaList.getArgsUnit());// 参数单位
					siaPojo.setArgsType(siaList.getArgsType());// 参数类型
					siaPojo.setArgsTypeName(siaList.getArgsTypeName());// 参数类型名稱
					siaPojo.setIsRequired(siaList.getIsRequired());// 是否必填
					siaPojo.setArgsDescription(siaList.getArgsDescription());// 参数描述
					siaPojo.setSort(siaList.getSort());// 排序
					siaPojo.setCreateTime(new Date());
					siaPojo.setCreateUserId(user.getId());
					siaPojo.setStates(StandardItemArgs.STATE_FLAG_ADD);
					standardItemArgsDao.flush();
					standardItemArgsDao.save(siaPojo);
					standardItemArgsDao.flush();

					// 保存参数列表信息
					List<StandardItemArgsValue> findByItemArgsId = standardItemArgsValueDao
							.findByItemArgsId(siaList.getId());
					if (findByItemArgsId != null && findByItemArgsId.size() > 0) {
						for (StandardItemArgsValue siavList : findByItemArgsId) {
							StandardItemArgsValue siavPojo = new StandardItemArgsValue();
							siavPojo.setItemArgsId(siaPojo.getId());
							siavPojo.setName(siavList.getName());
							siavPojo.setValue(siavList.getValue());
							siavPojo.setSort(siavList.getSort());
							siavPojo.setCreateTime(new Date());
							siavPojo.setCreateUserId(user.getId());
							siavPojo.setStates(StandardItemArgsValue.STATE_FLAG_ADD);
							standardItemArgsValueDao.flush();
							standardItemArgsValueDao.save(siavPojo);
							standardItemArgsValueDao.flush();
						}
					}
				}
			}

			if (csiVoPojo.getChildList() != null && csiVoPojo.getChildList().size() > 0) {
				backQueryStandardItemsTree(csiVoPojo.getChildList(), user, siPojo.getId(), sesId, pgcPojo);
			}
		}
	}

	/**
	 * 删除标准信息
	 * 
	 * @param id
	 *            主键
	 * @param user
	 *            用户信息
	 * @return
	 */
	@Transactional(readOnly = false)
	public ResultBean<String> delSystemEnterpriseStandards(String id, User user) {
		// 逻辑删除标准库
		SystemEnterpriseStandards sesPojo = systemEnterpriseStandardsDao.get(id);
		sesPojo.setUpdateTime(new Date());
		sesPojo.setUpdateUserId(user.getId());
		sesPojo.setStates(SystemEnterpriseStandards.STATE_FLAG_DEL);
		systemEnterpriseStandardsDao.save(sesPojo);

		// 逻辑删除生长周期
		List<ProductGrowthCycle> findBySysEntStandardIdAndStates = productGrowthCycleDao.findBySysEntStandardIdAndStates(sesPojo.getId(), "1");
		for (ProductGrowthCycle productGrowthCycle : findBySysEntStandardIdAndStates) {
			productGrowthCycle.setUpdateTime(new Date());
			productGrowthCycle.setUpdateUserId(user.getId());
			productGrowthCycle.setStates(ProductGrowthCycle.STATE_FLAG_DEL);
			productGrowthCycleDao.save(productGrowthCycle);

			// 逻辑删除传感器指标表
			sensorThresholdService.delete(productGrowthCycle.getId(), user);

			// 逻辑删除标准作业项表
			List<StandardItems> findBySystemStandardsIdAndGrowthCycleIdAndStates = standardItemsDao.findBySystemStandardsIdAndGrowthCycleIdAndStates(sesPojo.getId(), productGrowthCycle.getId());
			for (StandardItems standardItems : findBySystemStandardsIdAndGrowthCycleIdAndStates) {
				standardItems.setUpdateTime(new Date());
				standardItems.setUpdateUserId(user.getId());
				standardItems.setStates(StandardItems.STATE_FLAG_DEL);
				standardItemsDao.save(standardItems);

				// 逻辑删除标准作业详细参数表
				List<StandardItemArgs> findByItemsIdList = standardItemArgsDao.findByItemsIdList(standardItems.getId());
				for (StandardItemArgs standardItemArgs : findByItemsIdList) {
					standardItemArgs.setUpdateTime(new Date());
					standardItemArgs.setUpdateUserId(user.getId());
					standardItemArgs.setStates(StandardItemArgs.STATE_FLAG_DEL);
					standardItemArgsDao.save(standardItemArgs);

					// 逻辑删除作业项表列表值
					List<StandardItemArgsValue> findByItemArgsId = standardItemArgsValueDao.findByItemArgsId(standardItemArgs.getId());
					for (StandardItemArgsValue standardItemArgsValue : findByItemArgsId) {
						standardItemArgsValue.setUpdateTime(new Date());
						standardItemArgsValue.setUpdateUserId(user.getId());
						standardItemArgsValue.setStates(StandardItemArgsValue.STATE_FLAG_DEL);
						standardItemArgsValueDao.save(standardItemArgsValue);
					}
				}
			}
		}
		return ResultUtil.success("Success");
	}

	/**
	 * H获取标准库分页信息
	 * 
	 * @param user
	 *            用户
	 * @param pageNo
	 *            页面
	 * @param pageSize
	 *            条数
	 * @param productId
	 *            品种ID
	 * @return
	 */
	public Page<SystemEnterpriseStandards> getStandardLibraryList(User user, Integer pageNo, Integer pageSize, String productId) {
		int no = pageNo == null ? Global.DEFAULT_PAGENO : pageNo;
		int size = pageSize == null ? Global.DEFAULT_PAGESIZE : pageSize;
		Page<SystemEnterpriseStandards> page = new Page<SystemEnterpriseStandards>(no, size);
		Page<SystemEnterpriseStandards> findPage = findPage(page, productId, user);
		List<SystemEnterpriseStandards> list = findPage.getList();
		for (SystemEnterpriseStandards sesPojo : list) {
			ProductLibraryTree pojo = productLibraryTreeService.get(sesPojo.getProductId());
			// 上级的分类
			ProductLibraryTree mainPojo = productLibraryTreeService.get(pojo.getParent().getId());
			if (pojo != null && mainPojo != null) {
				sesPojo.setProductName(mainPojo.getProductCategoryName() + "—" + pojo.getProductCategoryName());
			} else {
				sesPojo.setProductName("");
			}
			// 查询公司
			Office office = officeDao.get(sesPojo.getOfficeId());
			if (office != null) {
				sesPojo.setOfficeName(office.getName());
			} else {
				sesPojo.setOfficeName("");
			}
			// 查詢生长周期
			List<ProductGrowthCycle> productGrowthCycleList = productGrowthCycleDao.getProductGrowthCycleList(sesPojo.getId());
			if (!productGrowthCycleList.isEmpty()) {
				// 生长周期阶段表
				ProductGrowthCycle productGrowthCycle = productGrowthCycleList.get(0);
				// 查询传感器
				List<SensorThreshold> findByGrowthCycleId = sensorThresholdDao.findByGrowthCycleId(productGrowthCycle.getId());
				if (!findByGrowthCycleId.isEmpty()) {
					String str = "";
					for (SensorThreshold sensorThreshold : findByGrowthCycleId) {
						Dict dict = dictDao.get(sensorThreshold.getTargetId());
						String label = dict.getLabel();
						String minValue = sensorThreshold.getMinValue() == null ? "无" : sensorThreshold.getMinValue();
						String maxValue = sensorThreshold.getMaxValue() == null ? "无" : sensorThreshold.getMaxValue();
						String description = dict.getDescription();
						str += label + ":" + minValue + "—" + maxValue + description + ",";
						System.out.println(str);
					}
					String substring = str.substring(0, str.length() - 1);
					sesPojo.setLabel(substring);
				}
			}
			if (StringUtils.isNotBlank(sesPojo.getProductionModelId())) {
				ProductionModel productionModel = productionModelDao.get(sesPojo.getProductionModelId());
				sesPojo.setProductionName(productionModel.getProductionName());
			}
		}
		return findPage;
	}

	private Page<SystemEnterpriseStandards> findPage(Page<SystemEnterpriseStandards> page, String productId, User user) {
		String sql = "select * from t_agro_system_enterprise_standards a inner join t_agro_product_library_tree b on a.product_id = b.id  where a.states <> 'D' and b.states <> 'D' ";
		if (!"1".equals(user.getOffice().getId())) {
			Office office = officeDao.get(user.getOffice().getId());
			List<ProductLibraryRelation> findByLikeOfficeId = productLibraryRelationDao.distinct(office.getId(), office.getId());
			String parentIdStr = "";
			for (ProductLibraryRelation productLibraryRelation : findByLikeOfficeId) {
				parentIdStr += "'" + productLibraryRelation.getParentId() + "'" + ",";
			}
			if (StringUtils.isNotBlank(parentIdStr)) {
				parentIdStr = parentIdStr.substring(0, parentIdStr.length() - 1);
			}
			if (StringUtils.isBlank(parentIdStr)) {
				parentIdStr = "''";
			}
			sql += "and a.product_id in (" + parentIdStr + ")";
		}
		if (StringUtils.isNotBlank(productId)) {
			String str = "";
			ProductLibraryTree pojo = productLibraryTreeService.get(productId);
			List<ProductLibraryTree> arrayList = speciesManageApiService.findProductLibraryTree(pojo, "1", false);
			if (arrayList != null && arrayList.size() > 0) {
				for (ProductLibraryTree plt : arrayList) {
					str += "'" + plt.getId() + "'" + ",";
				}
			} else if (pojo != null) {
				str += "'" + pojo.getId() + "'" + ",";
			}
			if (StringUtils.isNotBlank(str)) {
				str = str.substring(0, str.length() - 1);
			} else {
				str = "''";
			}
			sql += "and a.product_id in (" + str + ")";
		}
		sql += "order by a.create_time desc";
		Page<SystemEnterpriseStandards> find = systemEnterpriseStandardsDao.findBySql(page, sql, SystemEnterpriseStandards.class);
		return find;
	}

	public SystemEnterpriseStandards getStandardLibrary(String id) {
		SystemEnterpriseStandards sesPojo = systemEnterpriseStandardsDao.get(id);
		if(StringUtils.isNotBlank(sesPojo.getProductionModelId())) {
			ProductionModel productionModel = productionModelDao.get(sesPojo.getProductionModelId());
			sesPojo.setProductionName(productionModel.getProductionName());
		}
		return sesPojo;
	}

	/**
	 * Title: getVersionName Description: 获取版本信息
	 * 
	 * @param standardName
	 *            标准名称
	 * @return
	 */
	public String getVersionName(String standardName) {
		String versionName = "1.0";
		List<SystemEnterpriseStandards> findByVersionName = systemEnterpriseStandardsDao.findByStandardName(standardName);
		if (findByVersionName != null && findByVersionName.size() > 0) {
			SystemEnterpriseStandards systemEnterpriseStandards = findByVersionName.get(0);
			String version = systemEnterpriseStandards.getVersionName();
			String str = version.substring(0, version.indexOf("."));// 截取@之前的字符串
			Integer intStr = Integer.valueOf(str);
			return intStr + 1 + ".0";
		}
		return versionName;
	}

	/**
	 * 
	 * Title: getVersion Description: 获取标准库版本
	 * 
	 * @param productId
	 *            品种ID
	 * @return
	 */
	public List<Map<String, String>> getVersion(String productId, User user) {
		String id = "";
		
		List<Map<String, String>> addArrayList = new ArrayList<Map<String, String>>();
		ProductLibraryTree productLibraryTree = productLibraryTreeService.get(productId);
		if ("1".equals(productLibraryTree.getIsProductCategory())) {
			id = productLibraryTree.getId();
		}
		if ("2".equals(productLibraryTree.getIsProductCategory()) && productLibraryTree.getParent() != null) {
			ProductLibraryTree pltPojo = productLibraryTreeService.get(productLibraryTree.getParent().getId());
			id = pltPojo.getId();
		}
		List<SystemEnterpriseStandards> findAll = systemEnterpriseStandardsDao.findByProductIdUnionAll(id, user.getOffice().getId());
		for (SystemEnterpriseStandards pojo : findAll) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", pojo.getId());
			map.put("standardName", pojo.getStandardName());
			addArrayList.add(map);
		}
		return addArrayList;
	}

	/**
	 * H获取标准库分页信息
	 * 
	 * @param user
	 *            用户
	 * @param pageNo
	 *            页面
	 * @param pageSize
	 *            条数
	 * @param productId
	 *            品种ID
	 * @return
	 */
	public List<SystemEnterpriseStandards> getStandardList(User user) {
		Page<SystemEnterpriseStandards> page = new Page<SystemEnterpriseStandards>(1, -1);
		Page<SystemEnterpriseStandards> findPage = findPage(page, null, user);
		List<SystemEnterpriseStandards> list = findPage.getList();
		for (SystemEnterpriseStandards sesPojo : list) {
			ProductLibraryTree pojo = productLibraryTreeService.get(sesPojo.getProductId());
			if (pojo!=null) {
				sesPojo.setProductName(pojo.getProductCategoryName());
			} else {
				sesPojo.setProductName("");
			}
			if (StringUtils.isNotBlank(sesPojo.getProductionModelId())) {
				ProductionModel productionModel = productionModelDao.get(sesPojo.getProductionModelId());
				sesPojo.setProductionName(productionModel.getProductionName());
			}
		}
		return findPage.getList();
	}
}
