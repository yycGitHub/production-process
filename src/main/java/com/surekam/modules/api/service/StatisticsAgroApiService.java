package com.surekam.modules.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.basemanage.dao.BaseManagerDao;
import com.surekam.modules.agro.basemanage.dao.BaseTreeDao;
import com.surekam.modules.agro.basemanage.entity.BaseManager;
import com.surekam.modules.agro.product.dao.ProductLibraryTreeDao;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.productbatchtask.dao.ProductBatchTaskDao;
import com.surekam.modules.agro.productionbatch.dao.ProductionBatchDao;
import com.surekam.modules.agro.productionbatch.dao.StatisticsProductionBatcDao;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.agro.productionbatch.entity.StatisticsProductionBatch;
import com.surekam.modules.agro.standardtaskitemsargsvalue.dao.StandardTaskItemsArgsValueDao;
import com.surekam.modules.agro.standardtaskitemsargsvalue.entity.StandardTaskItemsArgsValue;
import com.surekam.modules.agro.systementerprisestandards.dao.SystemEnterpriseStandardsDao;
import com.surekam.modules.agro.systementerprisestandards.entity.SystemEnterpriseStandards;
import com.surekam.modules.api.dto.resp.StatisticsBatchResq;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;

/**
 * Title: StatisticsAgroApiService Description: 统计农事接口Service
 * 
 * @author tangjun
 * @date 2019年5月11日
 */
@Component
@Transactional(readOnly = true)
public class StatisticsAgroApiService {

	@Autowired
	private OfficeDao officeDao;

	@Autowired
	private StatisticsProductionBatcDao statisticsProductionBatcDao;

	@Autowired
	private BaseManagerDao baseManagerDao;

	@Autowired
	private BaseTreeDao baseTreeDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ProductLibraryTreeDao productLibraryTreeDao;

	@Autowired
	private SystemEnterpriseStandardsDao systemEnterpriseStandardsDao;

	@Autowired
	private StandardTaskItemsArgsValueDao standardTaskItemsArgsValueDao;

	@Autowired
	private ProductBatchTaskDao productBatchTaskDao;

	@Autowired
	private ProductionBatchDao productionBatchDao;

	/**
	 * Title: statisticsBatch Description: 统计批次信息
	 * 
	 * @param user
	 *            用户信息
	 * @param officeId
	 *            公司id
	 * @param productId
	 *            品种id
	 * @param particularYear
	 *            年份
	 * @param operType
	 *            执行状态
	 * @param pageno
	 *            页数
	 * @param pagesize
	 *            条数
	 */
	public Map<String, Object> statisticsBatch(User user, String officeId, String productId, String particularYear, String operType, Integer pageno, Integer pagesize) {
		int no = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int size = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		// 查询当前公司
		Office of = officeDao.get(officeId);
		if (of == null) {
			return null;
		}
		List<Office> officeArrayList = new ArrayList<Office>();
		officeArrayList.add(of);
		// 查询公司下架的公司
		List<Office> officeLike = officeDao.findByParentIdsLike(of.getParentIds() + of.getId() + ",%");
		for (Office officeList : officeLike) {
			officeArrayList.add(officeList);
		}
		String str = "";
		for (Office office : officeArrayList) {
			str += "'" + office.getId() + "'" + ",";
		}
		String officeStr = str.substring(0, str.length() - 1);
		// 查询品种
		String plt = "";
		String pltStr = "";
		List<ProductLibraryTree> productLibraryTreeLike = null;
		if (StringUtils.isNotBlank(productId)) {
			ProductLibraryTree pojo = productLibraryTreeDao.get(productId);
			if (pojo != null) {
				if("1".equals(officeId)) {
					productLibraryTreeLike = productLibraryTreeDao.findByParentIdsLike("%" + pojo.getId() + ",%");
				} else {
					productLibraryTreeLike = productLibraryTreeDao.findByParent(pojo.getId(), pojo.getId(), officeId, officeId);
				}
				if (!productLibraryTreeLike.isEmpty()) {
					for (ProductLibraryTree pltPojo : productLibraryTreeLike) {
						plt += "'" + pltPojo.getId() + "'" + ",";
					}
				} else {
					plt += "'" + pojo.getId() + "'" + ",";
				}
				pltStr = plt.substring(0, plt.length() - 1);
			}
		} else {
			if("1".equals(officeId)) {
				productLibraryTreeLike = productLibraryTreeDao.findAll();
			} else {
				productLibraryTreeLike = productLibraryTreeDao.findByParent(officeId, officeId);
			}
			if (!productLibraryTreeLike.isEmpty()) {
				for (ProductLibraryTree pltPojo : productLibraryTreeLike) {
					plt += "'" + pltPojo.getId() + "'" + ",";
				}
				pltStr = plt.substring(0, plt.length() - 1);
			}
		}
		Page<StatisticsProductionBatch> findByBaseIdAndStates = statisticsProductionBatcDao.pageBaseIdAndStates(officeStr, pltStr, operType, particularYear, no, size);
		List<StatisticsProductionBatch> list = findByBaseIdAndStates.getList();
		for (StatisticsProductionBatch statisticsProductionBatch : list) {
			Office office = officeDao.get(statisticsProductionBatch.getOfficeId());
			// 公司名称
			statisticsProductionBatch.setOfficeName(office.getName());
			// 查询农户
			BaseManager roleNong = baseManagerDao.getUserId(statisticsProductionBatch.getBaseId(), "1852c8e247744ff184e8c162eff44f4c");
			User userNong = userDao.get(roleNong.getUserId());
			statisticsProductionBatch.setUserName(userNong.getName());
			// 查询专家
			BaseManager roleZhuan = baseManagerDao.getUserId(statisticsProductionBatch.getBaseId(), "391192d4f9634982858634f12de44275");
			User userZhuan = userDao.get(roleZhuan.getUserId());
			statisticsProductionBatch.setRoleName(userZhuan.getName());
			// 品种
			ProductLibraryTree productLibraryTree = productLibraryTreeDao.get(statisticsProductionBatch.getProductId());
			statisticsProductionBatch.setProductName(productLibraryTree.getProductCategoryName());
			// 标准
			SystemEnterpriseStandards systemEnterpriseStandards = systemEnterpriseStandardsDao.get(statisticsProductionBatch.getStandardId());
			statisticsProductionBatch.setStandardName(systemEnterpriseStandards.getStandardName());
			// 批次数量
			statisticsProductionBatch.setBatchNumberAndUnit(statisticsProductionBatch.getBatchNumber() + statisticsProductionBatch.getBatchUnit());

			List<StandardTaskItemsArgsValue> staValueList = standardTaskItemsArgsValueDao.findByRegionIdAndStates(statisticsProductionBatch.getId(), "采收类型", "2");
			if (!staValueList.isEmpty()) {
				String strName = "";
				for (StandardTaskItemsArgsValue standardTaskItemsArgsValue : staValueList) {
					List<Object> findByTaskListIdAndArgsName = standardTaskItemsArgsValueDao.findByTaskListIdAndArgsName(standardTaskItemsArgsValue.getTaskListId(), "采收量");
					if (!findByTaskListIdAndArgsName.isEmpty()) {
						String value = standardTaskItemsArgsValue.getTaskItemArgsValue();
						for (int i = 0; i < findByTaskListIdAndArgsName.size(); i++) {
							Object[] object = (Object[]) findByTaskListIdAndArgsName.get(i);
							String taskItemArgsValue = String.valueOf(object[1]);
							String argsUnit = (String) object[2];
							strName += value + taskItemArgsValue + argsUnit + ",";
						}
					} else {
						strName += standardTaskItemsArgsValue + "0" + "kg" + ",";
					}
				}
				if (StringUtils.isNotBlank(strName)) {
					strName = strName.substring(0, strName.length() - 1);
				}
				if("1".equals(statisticsProductionBatch.getStatus())) {
					statisticsProductionBatch.setIsRecovery("执行中");
				}else if("998".equals(statisticsProductionBatch.getStatus()) || "999".equals(statisticsProductionBatch.getStatus())) {
					statisticsProductionBatch.setIsRecovery("已完成");
				}
				statisticsProductionBatch.setRecoveryAmount(strName);
			} else {
				List<StandardTaskItemsArgsValue> findByRegionIdAndStates = standardTaskItemsArgsValueDao.findByRegionIdAndStates(statisticsProductionBatch.getId(), "采收量", "2");
				if (!findByRegionIdAndStates.isEmpty()) {
					String strName = "";
					for (StandardTaskItemsArgsValue standardTaskItemsArgsValue : findByRegionIdAndStates) {
						List<Object> findByTaskListIdAndArgsName = standardTaskItemsArgsValueDao.findByTaskListIdAndArgsName(standardTaskItemsArgsValue.getTaskListId(), "采收量");
						if (!findByTaskListIdAndArgsName.isEmpty()) {
							for (int i = 0; i < findByTaskListIdAndArgsName.size(); i++) {
								Object[] object = (Object[]) findByTaskListIdAndArgsName.get(i);
								String taskItemArgsValue = String.valueOf(object[1]);
								String argsUnit = (String) object[2];
								strName += taskItemArgsValue + argsUnit + ",";
							}
						} else {
							strName += "0kg" + ",";
						}
					}
					if (StringUtils.isNotBlank(strName)) {
						strName = strName.substring(0, strName.length() - 1);
					}
					if("1".equals(statisticsProductionBatch.getStatus())) {
						statisticsProductionBatch.setIsRecovery("执行中");
					}else if("998".equals(statisticsProductionBatch.getStatus()) || "999".equals(statisticsProductionBatch.getStatus())) {
						statisticsProductionBatch.setIsRecovery("已完成");
					}
					statisticsProductionBatch.setRecoveryAmount(strName);
				} else {
					if("1".equals(statisticsProductionBatch.getStatus())) {
						statisticsProductionBatch.setIsRecovery("执行中");
					}else if("998".equals(statisticsProductionBatch.getStatus()) || "999".equals(statisticsProductionBatch.getStatus())) {
						statisticsProductionBatch.setIsRecovery("已完成");
					}
					statisticsProductionBatch.setRecoveryAmount("0kg");
				}
			}
		}

		List<StatisticsProductionBatch> list2 = statisticsProductionBatcDao.findByBaseIdAndStates(officeStr, pltStr, operType, particularYear);
		List<Map<String, StatisticsBatchResq>> listMap = new ArrayList<Map<String, StatisticsBatchResq>>();
		for (StatisticsProductionBatch statisticsProductionBatch : list2) {
			Map<String, StatisticsBatchResq> map = new HashMap<String, StatisticsBatchResq>();
			// 品种
			ProductLibraryTree productLibraryTree = productLibraryTreeDao.get(statisticsProductionBatch.getProductId());

			List<StandardTaskItemsArgsValue> staValueList = standardTaskItemsArgsValueDao.findByRegionIdAndStates(statisticsProductionBatch.getId(), "采收类型", "2");
			if (!staValueList.isEmpty()) {
				for (StandardTaskItemsArgsValue standardTaskItemsArgsValue : staValueList) {
					List<Object> findByTaskListIdAndArgsName = standardTaskItemsArgsValueDao.findByTaskListIdAndArgsName(standardTaskItemsArgsValue.getTaskListId(), "采收量");
					StatisticsBatchResq resq = new StatisticsBatchResq();
					if (!findByTaskListIdAndArgsName.isEmpty()) {
						for (int i = 0; i < findByTaskListIdAndArgsName.size(); i++) {
							Object[] object = (Object[]) findByTaskListIdAndArgsName.get(i);
							// String argsName = (String) object[0];
							String taskItemArgsValue = String.valueOf(object[1]);
							resq.setTaskItemArgsValue(taskItemArgsValue);
							resq.setTaskItemArgsValueUnit(object[2] + "");
							resq.setBatchNumber(statisticsProductionBatch.getBatchNumber());
							resq.setBatchNumberUnit(statisticsProductionBatch.getBatchUnit());
							resq.setProductName(productLibraryTree.getProductCategoryName());
							resq.setArgsName(object[0] + "");
						}
					} else {
						resq.setTaskItemArgsValue("0");
						resq.setTaskItemArgsValueUnit("kg");
						resq.setBatchNumber(statisticsProductionBatch.getBatchNumber());
						resq.setBatchNumberUnit(statisticsProductionBatch.getBatchUnit());
						resq.setProductName(productLibraryTree.getProductCategoryName());
						resq.setArgsName(productLibraryTree.getProductCategoryName());
					}
					map.put(standardTaskItemsArgsValue.getTaskItemArgsValue(), resq);
				}
			} else {
				List<StandardTaskItemsArgsValue> findByRegionIdAndStates = standardTaskItemsArgsValueDao.findByRegionIdAndStates(statisticsProductionBatch.getId(), "采收量", operType);
				if (!findByRegionIdAndStates.isEmpty()) {
					for (StandardTaskItemsArgsValue standardTaskItemsArgsValue : findByRegionIdAndStates) {
						List<Object> findByTaskListIdAndArgsName = standardTaskItemsArgsValueDao.findByTaskListIdAndArgsName(standardTaskItemsArgsValue.getTaskListId(), "采收量");
						StatisticsBatchResq resq = new StatisticsBatchResq();
						if (!findByTaskListIdAndArgsName.isEmpty()) {
							for (int i = 0; i < findByTaskListIdAndArgsName.size(); i++) {
								Object[] object = (Object[]) findByTaskListIdAndArgsName.get(i);
								String taskItemArgsValue = String.valueOf(object[1]);
								resq.setTaskItemArgsValue(taskItemArgsValue);
								resq.setTaskItemArgsValueUnit(object[2] + "");
								resq.setBatchNumber(statisticsProductionBatch.getBatchNumber());
								resq.setBatchNumberUnit(statisticsProductionBatch.getBatchUnit());
								resq.setProductName(productLibraryTree.getProductCategoryName());
								resq.setArgsName(productLibraryTree.getProductCategoryName());
								map.put(productLibraryTree.getProductCategoryName(), resq);
							}
						} else {
							resq.setTaskItemArgsValue("0");
							resq.setTaskItemArgsValueUnit("kg");
							resq.setBatchNumber(statisticsProductionBatch.getBatchNumber());
							resq.setBatchNumberUnit(statisticsProductionBatch.getBatchUnit());
							resq.setProductName(productLibraryTree.getProductCategoryName());
							resq.setArgsName(productLibraryTree.getProductCategoryName());
							map.put(productLibraryTree.getProductCategoryName(), resq);
						}
					}
				} else {
					StatisticsBatchResq resq = new StatisticsBatchResq();
					resq.setTaskItemArgsValue("0");
					resq.setTaskItemArgsValueUnit("kg");
					resq.setBatchNumber(statisticsProductionBatch.getBatchNumber());
					resq.setBatchNumberUnit(statisticsProductionBatch.getBatchUnit());
					resq.setProductName(productLibraryTree.getProductCategoryName());
					resq.setArgsName(productLibraryTree.getProductCategoryName());
					map.put(productLibraryTree.getProductCategoryName(), resq);
				}
			}
			listMap.add(map);
		}
		Map<String, StatisticsBatchResq> result = new HashMap<String, StatisticsBatchResq>();
		for (Map<String, StatisticsBatchResq> map : listMap) {
			for (Entry<String, StatisticsBatchResq> entry : map.entrySet()) {
				String id = entry.getKey();
				StatisticsBatchResq value2 = entry.getValue();
				StatisticsBatchResq statisticsBatchResq = result.get(id);
				if (statisticsBatchResq != null) {
					Double batchNum1 = 0d;
					try {
						batchNum1 = Double.valueOf(value2.getBatchNumber());
					} catch (Exception e) {
					}
					Double batchNum2 = 0d;
					try {
						batchNum2 = Double.valueOf(statisticsBatchResq.getBatchNumber());
					} catch (Exception e) {
					}
					statisticsBatchResq.setBatchNumber((batchNum1 + batchNum2) + "");
					Double tia1 = Double.valueOf(value2.getTaskItemArgsValue());
					Double tia2 = Double.valueOf(statisticsBatchResq.getTaskItemArgsValue());
					statisticsBatchResq.setTaskItemArgsValue((tia1 + tia2) + "");
				} else {
					statisticsBatchResq = new StatisticsBatchResq();
					try {
						Double batchNum1 = Double.valueOf(value2.getBatchNumber());
						statisticsBatchResq.setBatchNumber((batchNum1) + "");
					} catch (Exception e) {
					}
					try {
						Double tia1 = Double.valueOf(value2.getTaskItemArgsValue());
						statisticsBatchResq.setTaskItemArgsValue((tia1) + "");
					} catch (Exception e) {
					}
				}
				statisticsBatchResq.setArgsName(id);
				statisticsBatchResq.setTaskItemArgsValueUnit(value2.getTaskItemArgsValueUnit());
				statisticsBatchResq.setBatchNumberUnit(value2.getBatchNumberUnit());
				result.put(id, statisticsBatchResq);
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result.values());
		resultMap.put("page", findByBaseIdAndStates);
		return resultMap;
	}

	/**
	 * Title: statisticsTask Description: 统计任务接口
	 * 
	 * @param user
	 *            用户
	 * @param officeId
	 *            公司
	 * @param particularYear
	 *            年月
	 * @param pageno
	 *            页数
	 * @param pagesize
	 *            条数
	 * @return
	 */
	public Page<Map<String, String>> statisticsTask(User user, String officeId, String particularYear, Integer pageno,
			Integer pagesize) {
		int no = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int size = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		// 查询当前公司
		Office of = officeDao.get(officeId);
		List<Office> officeArrayList = new ArrayList<Office>();
		officeArrayList.add(of);
		// 查询公司下架的公司
		List<Office> officeLike = officeDao.findByParentIdsLike(of.getParentIds() + of.getId() + ",%");
		for (Office officeList : officeLike) {
			officeArrayList.add(officeList);
		}
		String str = "";
		for (Office office : officeArrayList) {
			str += "'" + office.getId() + "'" + ",";
		}
		String officeStr = str.substring(0, str.length() - 1);
		Page<Map<String, String>> findByOfficeId = baseTreeDao.findByOfficeId("(" + officeStr + ")", no, size);
		List<Map<String, String>> mapArrayList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list = findByOfficeId.getList();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> listMap = new HashMap<String, String>();
			Map<String, String> map = list.get(i);
			Set<Entry<String, String>> entrySet = map.entrySet();

			String baseId = null;
			String baseName = null;
			String office = null;

			for (Entry<String, String> entry : entrySet) {
				if ("baseId".equals(entry.getKey())) {
					baseId = entry.getValue();
				}

				if ("baseName".equals(entry.getKey())) {
					baseName = entry.getValue();
				}

				if ("officeId".equals(entry.getKey())) {
					office = entry.getValue();
				}
			}
			// 查询农户
			BaseManager roleNong = baseManagerDao.getUserId(baseId, "1852c8e247744ff184e8c162eff44f4c");
			User userNong = userDao.get(roleNong.getUserId());
			listMap.put("userNong", userNong.getName());
			// 查询专家
			BaseManager roleZhuan = baseManagerDao.getUserId(baseId, "391192d4f9634982858634f12de44275");
			User userZhuan = userDao.get(roleZhuan.getUserId());
			listMap.put("userFu", userZhuan.getName());

			Office officeName = officeDao.get(office);
			listMap.put("officeName", officeName.getName());
			listMap.put("baseName", baseName);

			List<ProductionBatch> findByBaseId = productionBatchDao.findByBaseId(baseId);
			for (int j = 1; j <= 12; j++) {
				Integer countTask = 0;
				String length = String.valueOf(j);
				if (length.length() == 1) {
					length = "0" + length;
				}
				for (ProductionBatch productionBatch : findByBaseId) {
					countTask += productBatchTaskDao.countTask(productionBatch.getId(), particularYear, length);
				}
				listMap.put("month" + j, countTask.toString());
			}
			mapArrayList.add(listMap);
		}
		findByOfficeId.setList(mapArrayList);
		return findByOfficeId;
	}

}
