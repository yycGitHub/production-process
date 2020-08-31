/**
 * 
 */
package com.surekam.modules.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.utils.Client;
import com.surekam.common.utils.DateUtil;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.basemanage.dao.BaseManagerDao;
import com.surekam.modules.agro.basemanage.dao.BaseTreeDao;
import com.surekam.modules.agro.basemanage.entity.BaseManager;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
import com.surekam.modules.agro.notuploadrecord.dao.NotUploadRecordDao;
import com.surekam.modules.agro.notuploadrecord.entity.NotUploadRecord;
import com.surekam.modules.agro.product.dao.ProductLibraryDetailDao;
import com.surekam.modules.agro.product.dao.ProductLibraryTreeDao;
import com.surekam.modules.agro.product.entity.ProductLibraryDetail;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.productbatchtask.dao.ProductBatchTaskDao;
import com.surekam.modules.agro.productbatchtask.entity.DataUploadResolveAndTaskVo;
import com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskValueVo;
import com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskVo;
import com.surekam.modules.agro.productbatchtaskresolve.service.ProductBatchTaskResolveService;
import com.surekam.modules.agro.productgrowthcycle.dao.ProductGrowthCycleDao;
import com.surekam.modules.agro.productgrowthcycle.entity.ProductGrowthCycle;
import com.surekam.modules.agro.productionbatch.dao.ProductionBatchDao;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.agro.productionmodel.dao.ProductionModelDao;
import com.surekam.modules.agro.productionmodel.entity.ProductionModel;
import com.surekam.modules.agro.productlibraryquick.dao.ProductLibraryQuickValueDao;
import com.surekam.modules.agro.productlibraryquick.entity.ProductLibraryQuickValue;
import com.surekam.modules.agro.standarditems.dao.StandardItemsDao;
import com.surekam.modules.agro.standarditems.entity.StandardItems;
import com.surekam.modules.agro.standardtaskitemsargsvalue.dao.StandardTaskItemsArgsValueDao;
import com.surekam.modules.agro.standardtaskitemsargsvalue.entity.StandardTaskItemsArgsValue;
import com.surekam.modules.agro.uploadauditrecord.dao.UploadAuditRecordDao;
import com.surekam.modules.agro.uploadauditrecord.entity.UploadAuditRecord;
import com.surekam.modules.agro.uploadsetting.dao.UploadSettingDao;
import com.surekam.modules.agro.uploadsetting.entity.UploadSetting;
import com.surekam.modules.api.dto.req.QuickReq;
import com.surekam.modules.api.dto.req.SavaUploadReq;
import com.surekam.modules.api.dto.req.UploadAuditRecordReq;
import com.surekam.modules.api.dto.uploadData.req.BatchInfoReq;
import com.surekam.modules.api.dto.uploadData.req.TraceModelListReq;
import com.surekam.modules.api.dto.uploadData.req.TraceProductReq;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;

import net.sf.cglib.beans.BeanMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Title: DataUploadManageApiService Description:数据上传管理Service
 * 
 * @author tangjun
 * @date 2019年7月12日
 */
@Component
@Transactional(readOnly = true)
public class DataUploadManageApiService {
	
	private static String syImgUrl = Global.getConfig("sy_img_url");

	private static String syUrl = Global.getConfig("sy_url");

	@Autowired
	private OfficeDao officeDao;
	
	@Autowired
	private BaseTreeDao baseTreeDao;
	
	@Autowired
	private StandardItemsDao standardItemsDao;
	
	@Autowired
	private UploadSettingDao uploadSettingDao;

	@Autowired
	private ProductionBatchDao productionBatchDao;

	@Autowired
	private NotUploadRecordDao notUploadRecordDao;
	
	@Autowired
	private ProductBatchTaskDao productBatchTaskDao;

	@Autowired
	private UploadAuditRecordDao uploadAuditRecordDao;

	@Autowired
	private ProductLibraryTreeDao productLibraryTreeDao;
	
	@Autowired
	private ProductGrowthCycleDao ProductGrowthCycleDao;
	
	@Autowired
	private StandardTaskItemsArgsValueDao standardTaskItemsArgsValueDao;

	@Autowired
	private ProductLibraryQuickValueDao productLibraryQuickValueDao;
	
	@Autowired
	private ProductLibraryDetailDao productLibraryDetailDao;
	
	@Autowired
	private ProductionModelDao productionModelDao;
	
	@Autowired
	private BaseManagerDao baseManagerDao;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProductBatchTaskResolveService productBatchTaskResolveService;
	
	/**
	 * Title: list Description: 获取未上传列表信息
	 * 
	 * @param officeId
	 *            公司ID
	 * @param pageno
	 *            页数
	 * @param pagesize
	 *            条数
	 * @return
	 */
	public Page<Map<String, String>> notUpload(String officeId, Integer pageno, Integer pagesize) {
		int no = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int size = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;

		List<UploadAuditRecord> uploadAuditRecordList = uploadAuditRecordDao.getUploadAuditRecordList();
		String idStr = "";
		for (UploadAuditRecord uploadAuditRecord : uploadAuditRecordList) {
			idStr += "'" + uploadAuditRecord.getStandardTaskItemsArgsValueId() + "'" + ",";
		}
		if (StringUtils.isNotBlank(idStr)) {
			idStr = idStr.substring(0, idStr.length() - 1);
		} else {
			idStr = "''";
		}
		List<Office> findAllOffices = officeDao.findAllOfficesAndId(officeId, officeId);
		String ofStr = "";
		for (Office office : findAllOffices) {
			ofStr += "'" + office.getId() + "'" + ",";
		}
		if (StringUtils.isNotBlank(ofStr)) {
			ofStr = ofStr.substring(0, ofStr.length() - 1);
		}
		if (StringUtils.isBlank(ofStr)) {
			ofStr = "''";
		}
		Page<Map<String, String>> page = standardTaskItemsArgsValueDao.page("采收量", idStr, ofStr, "391192d4f9634982858634f12de44275", "notIn", no, size);
		List<Map<String, String>> list = page.getList();
		for (Map<String, String> map : list) {
			String taskListId = map.get("taskListId");
			if(StringUtils.isNotBlank(taskListId)) {
				StandardTaskItemsArgsValue taskListIdAndArgsName = standardTaskItemsArgsValueDao.getTaskListIdAndArgsName(taskListId, "采收类型");
				if (null != taskListIdAndArgsName) {
					map.put("typeName", taskListIdAndArgsName.getTaskItemArgsValue());
				}
			}
		}
		return page;
	}

	/**
	 * Title: alreadyUpload Description: 获取已上传列表信息
	 * 
	 * @param officeId
	 *            公司ID
	 * @param pageno
	 *            页数
	 * @param pagesize
	 *            条数
	 * @return
	 */
	public Page<Map<String, String>> alreadyUpload(String officeId, Integer pageno, Integer pagesize) {
		int no = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int size = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;

		// 查询
		List<UploadAuditRecord> uploadAuditRecordList = uploadAuditRecordDao.findByAuditStatus("'1','3'");
		String idStr = "";
		for (UploadAuditRecord uploadAuditRecord : uploadAuditRecordList) {
			idStr += "'" + uploadAuditRecord.getStandardTaskItemsArgsValueId() + "'" + ",";
		}
		if (StringUtils.isNotBlank(idStr)) {
			idStr = idStr.substring(0, idStr.length() - 1);
		}else {
			idStr = "''";
		}
		List<Office> findAllOffices = officeDao.findAllOfficesAndId(officeId, officeId);
		String ofStr = "";
		for (Office office : findAllOffices) {
			ofStr += "'" + office.getId() + "'" + ",";
		}
		if (StringUtils.isNotBlank(ofStr)) {
			ofStr = ofStr.substring(0, ofStr.length() - 1);
		}
		if (StringUtils.isBlank(ofStr)) {
			ofStr = "''";
		}
		Page<Map<String, String>> page = standardTaskItemsArgsValueDao.page("采收量", idStr, ofStr, "391192d4f9634982858634f12de44275", "in", no, size);
		List<Map<String, String>> list = page.getList();
		for (Map<String, String> map : list) {
			String taskListId = map.get("taskListId");
			String id = map.get("id");
			if(StringUtils.isNotBlank(taskListId)) {
				StandardTaskItemsArgsValue taskListIdAndArgsName = standardTaskItemsArgsValueDao.getTaskListIdAndArgsName(taskListId, "采收类型");
				if (null != taskListIdAndArgsName) {
					map.put("typeName", taskListIdAndArgsName.getTaskItemArgsValue());
				}
			}
			
			if(StringUtils.isNotBlank(id)) {
				List<UploadAuditRecord> aduditList1 = uploadAuditRecordDao.findByStandardTaskItemsArgsValueIdAndAuditStatus(id, "1");
				if(!aduditList1.isEmpty()) {
					map.put("auditStatus", "1");
					map.put("syBatchCode", aduditList1.get(0).getSyBatchCode());
					
				}
				List<UploadAuditRecord> aduditList2 = uploadAuditRecordDao.findByStandardTaskItemsArgsValueIdAndAuditStatus(id, "3");
				if(!aduditList2.isEmpty()) {
					map.put("auditStatus", "3");
					map.put("syBatchCode", aduditList2.get(0).getSyBatchCode());
				}
			}
		}
		return page;
	}

	/**
	 * Title: auditFailed Description: 获取审核未通过列表信息
	 * 
	 * @param officeId
	 *            公司ID
	 * @param pageno
	 *            页数
	 * @param pagesize
	 *            条数
	 * @return
	 */
	public Page<Map<String, String>> auditFailed(String officeId, Integer pageno, Integer pagesize) {
		int no = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int size = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;

		List<UploadAuditRecord> uploadAuditRecordList = uploadAuditRecordDao.findByAuditStatus("'2'");
		String idStr = "";
		for (UploadAuditRecord uploadAuditRecord : uploadAuditRecordList) {
			idStr += "'" + uploadAuditRecord.getStandardTaskItemsArgsValueId() + "'" + ",";
		}
		if (StringUtils.isNotBlank(idStr)) {
			idStr = idStr.substring(0, idStr.length() - 1);
		}else {
			idStr = "''";
		}
		List<Office> findAllOffices = officeDao.findAllOfficesAndId(officeId, officeId);
		String ofStr = "";
		for (Office office : findAllOffices) {
			ofStr += "'" + office.getId() + "'" + ",";
		}
		if (StringUtils.isNotBlank(ofStr)) {
			ofStr = ofStr.substring(0, ofStr.length() - 1);
		}
		if (StringUtils.isBlank(ofStr)) {
			ofStr = "''";
		}
		Page<Map<String, String>> page = standardTaskItemsArgsValueDao.page("采收量", idStr, ofStr, "391192d4f9634982858634f12de44275", "in", no, size);
		List<Map<String, String>> list = page.getList();
		for (Map<String, String> map : list) {
			String taskListId = map.get("taskListId");
			String id = map.get("id");
			if(StringUtils.isNotBlank(id)) {
				List<UploadAuditRecord> aduditList = uploadAuditRecordDao.findByStandardTaskItemsArgsValueIdAndAuditStatus(id, "2");
				if(!aduditList.isEmpty()) {
					map.put("syBatchCode", aduditList.get(0).getSyBatchCode());
				}
			}
			if(StringUtils.isNotBlank(taskListId)) {
				StandardTaskItemsArgsValue taskListIdAndArgsName = standardTaskItemsArgsValueDao.getTaskListIdAndArgsName(taskListId, "采收类型");
				if (null != taskListIdAndArgsName) {
					map.put("typeName", taskListIdAndArgsName.getTaskItemArgsValue());
				}
			}
		}
		return page;
	}

	/**
	 * 
	 * Title: getBatchData
	 * Description: 上传页面数据
	 * @param id 采收任务ID
	 * @param batchId 批次id
	 * @return
	 */
	public ResultBean<Map<Object, Object>> getBatchData(String id, String batchId) {
		ProductionBatch productionBatch = productionBatchDao.get(batchId);
		if (null == productionBatch) {
			return ResultUtil.error(80001, "未查询到批次信息。");
		}
		BaseTree baseTree = baseTreeDao.get(productionBatch.getBaseId());
		if (null == baseTree) {
			return ResultUtil.error(80002, "未查询到基地信息。");
		}
		StandardTaskItemsArgsValue standardTaskItemsArgsValue = standardTaskItemsArgsValueDao.get(id);
		if (null == standardTaskItemsArgsValue) {
			return ResultUtil.error(80003, "未查询到执行记录信息。");
		}
		Map<Object, Object> map = new HashMap<Object, Object>();

		boolean notUpload = notUploadRecordDao.findByBatchIdAndDataId(productionBatch.getId());
		// 基本信息List
		List<Map<Object, Object>> quickList = new ArrayList<Map<Object, Object>>();
		// 获取采收类型名称
		StandardTaskItemsArgsValue taskListIdAndArgsName = standardTaskItemsArgsValueDao.getTaskListIdAndArgsName(standardTaskItemsArgsValue.getTaskListId(), "采收类型");
		// 获取快捷品种信息
		ProductLibraryQuickValue productLibraryQuickValue = productLibraryQuickValueDao.findByOfficeIdAndProductIdAndProductName(baseTree.getOfficeId(), productionBatch.getProductId() , taskListIdAndArgsName==null?"":taskListIdAndArgsName.getTaskItemArgsValue());
		
		// 产品名称
		if (null != taskListIdAndArgsName && StringUtils.isNotBlank(taskListIdAndArgsName.getTaskItemArgsValue())) {
			Map<Object, Object> quickNameMap = new HashMap<Object, Object>();
			quickNameMap.put("id", "quickName");
			quickNameMap.put("name", taskListIdAndArgsName.getTaskItemArgsValue());
			quickList.add(quickNameMap);
		} else {
			List<ProductLibraryDetail> findById = productLibraryDetailDao.findById(productionBatch.getProductId());
			if (!findById.isEmpty() && StringUtils.isNotBlank(findById.get(0).getProductName())) {
				Map<Object, Object> quickNameMap = new HashMap<Object, Object>();
				quickNameMap.put("id", "quickName");
				quickNameMap.put("name", findById.get(0).getProductName());
				quickList.add(quickNameMap);
			}
		}
		
		// 保质期
		if (null != productLibraryQuickValue && StringUtils.isNotBlank(productLibraryQuickValue.getQualityGuaranteePeriod())) {
			Map<Object, Object> quickDatMap = new HashMap<Object, Object>();
			quickDatMap.put("id", "quickDat");
			quickDatMap.put("name", productLibraryQuickValue.getQualityGuaranteePeriod());
			quickList.add(quickDatMap);
		}
		
		// 品种宣传图片
		if (null != productLibraryQuickValue && StringUtils.isNotBlank(productLibraryQuickValue.getProductPropagandaImgUrl())) {
			Map<Object, Object> quickUrlMap = new HashMap<Object, Object>();
			quickUrlMap.put("id", "quickUrl");
			quickUrlMap.put("name", syImgUrl + productLibraryQuickValue.getProductPropagandaImgUrl());
			quickList.add(quickUrlMap);
		} else {
			List<ProductLibraryDetail> findById = productLibraryDetailDao.findById(productionBatch.getProductId());
			if (!findById.isEmpty() && StringUtils.isNotBlank(findById.get(0).getProductImagUrl())) {
				Map<Object, Object> quickUrlMap = new HashMap<Object, Object>();
				quickUrlMap.put("id", "quickUrl");
				quickUrlMap.put("name", syImgUrl + findById.get(0).getProductImagUrl());
				quickList.add(quickUrlMap);
			}
		}
		
		// 品种宣传描述
		if (null != productLibraryQuickValue && StringUtils.isNotBlank(productLibraryQuickValue.getProductPropagandaDescription())) {
			Map<Object, Object> quickDescriptionMap = new HashMap<Object, Object>();
			quickDescriptionMap.put("id", "quickDescription");
			quickDescriptionMap.put("name", productLibraryQuickValue.getProductPropagandaDescription());
			quickList.add(quickDescriptionMap);
		} else {
			List<ProductLibraryDetail> findById = productLibraryDetailDao.findById(productionBatch.getProductId());
			if (!findById.isEmpty() && StringUtils.isNotBlank(findById.get(0).getDescription())) {
				Map<Object, Object> quickDescriptionMap = new HashMap<Object, Object>();
				quickDescriptionMap.put("id", "quickDescription");
				quickDescriptionMap.put("name", findById.get(0).getDescription());
				quickList.add(quickDescriptionMap);
			}
		}
		
		// 产地
		if (StringUtils.isNotBlank(baseTree.getProvince()) && StringUtils.isNotBlank(baseTree.getCity())) {
			Map<Object, Object> addrMap = new HashMap<Object, Object>();
			addrMap.put("id", "quickAddr");
			String province = baseTree.getProvince() == null ? "" : baseTree.getProvince();
			String city = baseTree.getCity() == null ? "" : baseTree.getCity();
			String area = baseTree.getArea() == null ? "" : baseTree.getArea();
			String address = baseTree.getAddress() == null ? "" : baseTree.getAddress();
			addrMap.put("name", province + city + area + address);
			quickList.add(addrMap);
		}
		
		BaseManager userId = baseManagerDao.getUserId(baseTree.getId(), "391192d4f9634982858634f12de44275");
		if (null != userId) {
			User user = userDao.get(userId.getUserId());
			if (null != user && StringUtils.isNotBlank(user.getName())) {
				// 负责人
				Map<Object, Object> quickUserNameMap = new HashMap<Object, Object>();
				quickUserNameMap.put("id", "quickUserName");
				quickUserNameMap.put("name", user.getName());
				quickList.add(quickUserNameMap);
			}
			if (null != user && StringUtils.isNotBlank(user.getPhone())) {
				// 负责人
				Map<Object, Object> quickUserNameMap = new HashMap<Object, Object>();
				quickUserNameMap.put("id", "quickUserPhone");
				quickUserNameMap.put("name", user.getPhone());
				quickList.add(quickUserNameMap);
			}
		}
		
		Map<Object, Object> quickDateMap = new HashMap<Object, Object>();
		quickDateMap.put("id", "quickDate");
		quickDateMap.put("name", DateUtil.formatSmsDt(standardTaskItemsArgsValue.getCreateTime()));
		quickList.add(quickDateMap);
		
		// 返回页面的Map
		map.put("quickList", quickList);
		
		// 作业项
		map.put("taskList", "");
		List<String> ids = new ArrayList<String>();
		ids.add(batchId);
		if (ids != null && ids.size() > 0) {
			// 查询基地和批次作业记录
			String hqlTasks = "select new com.surekam.modules.agro.productbatchtask.entity.DataUploadResolveAndTaskVo(pbt.id,pbtr.id,pbt.standardItemName,pbtr.dispatchTime,pbtr.finishTime,pbt.standardItemId,pbtr.nonexecutionReason)"
					+ " from ProductBatchTask pbt,ProductBatchTaskResolve pbtr" + " where pbt.id=pbtr.taskId "
					+ " and pbt.states <> 'D'" + " and pbtr.executionStatus like '1' and pbtr.states <> 'D'" + " and pbt.regionId in (:p1)";
			hqlTasks += " order by pbtr.finishTime asc";
			List<DataUploadResolveAndTaskVo> tasks = productBatchTaskDao.find(hqlTasks, new Parameter(new Object[] { ids.toArray() }));
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
						DataUploadResolveAndTaskVo resolveAndTaskVo = tasks.get(i);
						UploadSetting uploadSetting = uploadSettingDao.findByOfficeidAndStandardIdAndStandardIdAndItemsId(baseTree.getOfficeId(), productionBatch.getStandardId(), tasks.get(i).getStandardItemId());
						if (null != uploadSetting) {
							resolveAndTaskVo.setTaskState(false);
						}else{
							resolveAndTaskVo.setTaskState(true);
						}
						if(notUpload){
							NotUploadRecord taskValueVo = notUploadRecordDao.findByBatchIdAndDataId(productionBatch.getId(), resolveAndTaskVo.getTaskResoleId());
							if(null == taskValueVo) {
								resolveAndTaskVo.setTaskState(true);
							}else{
								resolveAndTaskVo.setTaskState(false);
							}
						}
						for (int j = 0; j < taskResoles.size(); j++) {
							ResolveAndTaskValueVo resolveAndTaskValueVo = taskResoles.get(j);
							if (resolveAndTaskVo.getTaskResoleId().equals(resolveAndTaskValueVo.getTaskResolveId())) {
								resolveAndTaskVo.getTaskValueList().add(resolveAndTaskValueVo);
							}
						}
					}
				}
				map.put("taskList", tasks);
			}
		}
		List<Map<Object, Object>> zjList = new ArrayList<Map<Object, Object>>();
		if (StringUtils.isNotBlank(productionBatch.getQualityTestPath())) {
			String[] str = productionBatch.getQualityTestPath().split(",");
			for (int i = 0; i < str.length; ++i) {
				Map<Object, Object> pathMap = new HashMap<Object, Object>();

				NotUploadRecord zybgPojo = notUploadRecordDao.findByBatchIdAndDataId(productionBatch.getId(), str[i]);
				pathMap.put("id", str[i]);
				pathMap.put("value", syImgUrl + str[i]);
				if (null != zybgPojo) {
					pathMap.put("quickState", false);
				} else {
					pathMap.put("quickState", true);
				}
				zjList.add(pathMap);
			}
		}
		map.put("zjList", zjList);

		List<Map<Object, Object>> rzList = new ArrayList<Map<Object, Object>>();
		if (StringUtils.isNotBlank(productionBatch.getConfirmationInformationPath())) {
			String[] str = productionBatch.getConfirmationInformationPath().split(",");
			for (int i = 0; i < str.length; ++i) {
				Map<Object, Object> pathMap = new HashMap<Object, Object>();

				NotUploadRecord rzbgPojo = notUploadRecordDao.findByBatchIdAndDataId(productionBatch.getId(), str[i]);
				pathMap.put("id", str[i]);
				pathMap.put("value", syImgUrl + str[i]);
				if (null != rzbgPojo) {
					pathMap.put("quickState", false);
				} else {
					pathMap.put("quickState", true);
				}
				rzList.add(pathMap);
			}
		}
		map.put("rzList", rzList);
		
		return ResultUtil.success(map);
	}
	
	/**
	 * 
	 * Title: aboutOfficeQueryProductLibrary Description: 通过公司查询品种
	 * 
	 * @param officeId
	 *            公司ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String, String>> aboutOfficeQueryProductLibrary(String officeId) {
		List<Map<String, String>> findByOffice = productionBatchDao.findByOffice(officeId);
		if (!findByOffice.isEmpty()) {
			for (int i = 0; i < findByOffice.size(); i++) {
				Map<String, String> map = findByOffice.get(i);
				String str = map.get("productId");
				if(StringUtils.isNotBlank(str)) {
					ProductLibraryTree productLibraryTree = productLibraryTreeDao.get(str);
					if (null != productLibraryTree) {
						map.put("productName", productLibraryTree.getProductCategoryName());
					}
				}
				
				
			}
		}
		return findByOffice;
	}

	/**
	 * Title: aboutStandardQueryItems Description: 通过标准查询作业项
	 * 
	 * @param standardId
	 *            标准ID
	 * @param standardId2 
	 */
	public List<StandardItems> aboutStandardQueryItems(String officeId, String standardId) {
		List<ProductGrowthCycle> productGrowthCycleList = ProductGrowthCycleDao.getProductGrowthCycleList(standardId);
		if (!productGrowthCycleList.isEmpty()) {
			ProductGrowthCycle productGrowthCycle = productGrowthCycleList.get(0);
			List<StandardItems> findByGrowthCycleIdAscList = standardItemsDao.findByGrowthCycleIdAscList(productGrowthCycle.getId());
			for (StandardItems standardItems : findByGrowthCycleIdAscList) {
				UploadSetting uploadSetting = uploadSettingDao.findByOfficeidAndStandardIdAndStandardIdAndItemsId(officeId, standardId, standardItems.getId());
				if(null == uploadSetting) {
					standardItems.setUploadStates(true);
				} else {
					standardItems.setUploadStates(false);
				}
			}
			return findByGrowthCycleIdAscList;
		}
		return null;
	}

	/**
	 * Title: savaUpload Description: 保存上传
	 * 
	 * @param req
	 * @return
	 */
	@Transactional(readOnly = false)
	public String savaUpload(SavaUploadReq req) {
		ProductionBatch productionBatch = productionBatchDao.get(req.getBatchId());
		if (null == productionBatch) {
			return null;
		}
		BaseTree baseTree = baseTreeDao.findById(productionBatch.getBaseId());
		if (null == baseTree) {
			return null;
		}
		Office office = officeDao.get(baseTree.getOfficeId());
		if (null == office) {
			return null;
		}
		
		notUploadRecordDao.deleteBatchId(req.getBatchId());
		// 基本信息
		if (!req.getQuickList().isEmpty()) {
			List<QuickReq> quickList = req.getQuickList();
			for (QuickReq quickReq : quickList) {
				NotUploadRecord pojo = new NotUploadRecord();
				pojo.setBatchId(req.getBatchId());
				pojo.setModularTypeId(quickReq.getId());
				pojo.setDataId(quickReq.getName());
				notUploadRecordDao.save(pojo);
			}
		}
		
		// 生产过程不为空则存不上传的作业项id，为空就存一个这个批次为空的数据，用来区分没有设置
		if(!req.getTaskList().isEmpty()){
			List<String> taskList = req.getTaskList();
			for (String string : taskList) {
				NotUploadRecord pojo = new NotUploadRecord();
				pojo.setBatchId(req.getBatchId());
				pojo.setModularTypeId("生产过程");
				pojo.setDataId(string);
				notUploadRecordDao.save(pojo);
			}
		}else{
			NotUploadRecord pojo = new NotUploadRecord();
			pojo.setBatchId(req.getBatchId());
			pojo.setModularTypeId("生产过程");
			pojo.setDataId("");
			notUploadRecordDao.save(pojo);
		}
		
		// 认定信息
		if (!req.getRzList().isEmpty()) {
			List<String> rzList = req.getRzList();
			for (String string : rzList) {
				NotUploadRecord pojo = new NotUploadRecord();
				pojo.setBatchId(req.getBatchId());
				pojo.setModularTypeId("认定信息");
				pojo.setDataId(string);
				notUploadRecordDao.save(pojo);

			}
		}
		
		// 质检信息
		if (!req.getZjList().isEmpty()) {
			List<String> zjList = req.getZjList();
			for (String string : zjList) {
				NotUploadRecord pojo = new NotUploadRecord();
				pojo.setBatchId(req.getBatchId());
				pojo.setModularTypeId("质检信息");
				pojo.setDataId(string);
				notUploadRecordDao.save(pojo);
			}
		}
		return "Success";
	}
	
	/**
	 * Title: uploadSetting Description: 保存上传设置
	 * 
	 * @param officeId
	 *            公司Id
	 * @param standardId
	 *            标准
	 * @param uploadSettingIdList
	 *            作业项Id
	 * @return
	 */
	@Transactional(readOnly = false)
	public String uploadSetting(String officeId, String standardId, List<String> uploadSettingIdList) {
		if (!uploadSettingIdList.isEmpty()) {
			// 删除之前设置锅的上传设置
			uploadSettingDao.delete(officeId, standardId);
			// 保存上传设置记录
			for (String string : uploadSettingIdList) {
				UploadSetting pojo = new UploadSetting();
				pojo.setOfficeId(officeId);
				pojo.setStandardId(standardId);
				pojo.setStandardItemsId(string);
				uploadSettingDao.save(pojo);
			}
		}
		return "Success";
	}

	/**
	 * Title: updateAuditStatus Description: 对外审核状态
	 * 
	 * @param req
	 *            请求参数
	 * @return
	 */
	public String updateAuditStatus(UploadAuditRecordReq req) {
		UploadAuditRecord pojo = uploadAuditRecordDao.findyBySyBatchCodeAndOfficeId(req.getSybatchCode(), req.getOfficeId(), "1");
		if (null == pojo) {
			return "fail";
		}
		pojo.setAuditStatus(req.getAuditStatus());
		pojo.setAuditTime(req.getAuditTime());
		pojo.setAuditOpinions(req.getAuditOpinions());
		pojo.setLoginName(req.getLoginName());
		pojo.setUserName(req.getUserNmae());
		uploadAuditRecordDao.save(pojo);
		return "Success";
	}
	
	/**
	 * Title: confirmationUpload Description: 上传
	 * 
	 * @param batchId
	 *            批次号
	 */
	@Transactional(readOnly = false)
	public ResultBean<String> confirmationUpload(String standardTaskItemsArgsValueId) {
		ProductionBatch productionBatch = productionBatchDao.findById(standardTaskItemsArgsValueId);
		if (null == productionBatch) {
			return ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), ResultEnum.DATA_NOT_EXIST.getMessage());
		}
		BaseTree baseTree = baseTreeDao.findById(productionBatch.getBaseId());
		if (null == baseTree) {
			return ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), ResultEnum.DATA_NOT_EXIST.getMessage());
		}
		Office office = officeDao.get(baseTree.getOfficeId());
		if (null == office) {
			return ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), ResultEnum.DATA_NOT_EXIST.getMessage());
		}
		StandardTaskItemsArgsValue standardTaskItemsArgsValue = standardTaskItemsArgsValueDao.get(standardTaskItemsArgsValueId);
		if (null == standardTaskItemsArgsValue) {
			return ResultUtil.error(800008, "执行记录数据错误。");
		}
		if (StringUtils.isBlank(office.getCode())) {
			return ResultUtil.error(800003, "公司编码数据为空！");
		}
		if (StringUtils.isBlank(baseTree.getBaseCode())) {
			return ResultUtil.error(800004, "基地编码数据为空！");
		}
		String str1 = "";
		int length1 = office.getCode().length();
		if (length1 > 3) {
			str1 = office.getCode().substring(length1 - 3, length1);
		} else {
			str1 = office.getCode();
		}
		String str2 = "";
		int length2 = baseTree.getBaseCode().length();
		if (length2 > 3) {
			str2 = baseTree.getBaseCode().substring(length2 - 3, length2);
		} else {
			str2 = baseTree.getBaseCode();
		}
		boolean notUpload = notUploadRecordDao.findByBatchIdAndDataId(productionBatch.getId());
		// 调用溯源编码接口
		Map<String, String> traceCodeMap = new HashMap<String, String>();
		traceCodeMap.put("companyCode", str1);
		traceCodeMap.put("baseCode", str2);
		traceCodeMap.put("productCategoryCode", "1");
		traceCodeMap.put("packingCode", "1");
		traceCodeMap.put("count", "1");
		String postRequest = Client.fromHttp(syUrl + "api/code/getTraceCode?", traceCodeMap);
		if(StringUtils.isBlank(postRequest)) {
			return ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), ResultEnum.DATA_NOT_EXIST.getMessage());
		}
		JSONObject obj = JSONObject.fromObject(postRequest);
		System.out.println("溯源码编码" + obj);
		if(!"0".equals(obj.get("code").toString())) {
			return ResultUtil.error(800002, "加工生产点编号错误");
		}
		JSONArray jsonArray = obj.getJSONArray("bodyData");
		String syCode = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			syCode = jsonArray.get(0).toString();
		}
		// 生成时间错批次号
		String baseCode = "";
		List<UploadAuditRecord> uploadAuditRecordList = uploadAuditRecordDao.findByStandardTaskItemsArgsValueIdAndOfficeIdAndBatchId(office.getId(), productionBatch.getId(), standardTaskItemsArgsValueId);
		if(!uploadAuditRecordList.isEmpty()) {
			baseCode = uploadAuditRecordList.get(0).getSyBatchCode();
		} else {
			baseCode = DateUtil.getCurrDateTime();
		}
		if(StringUtils.isBlank(office.getKuid())) {
			ResultUtil.error(800005, "企业信息错误，清联系管理员");
		}
		// 上传批次信息
		BatchInfoReq batchInfoReq = new BatchInfoReq();
		batchInfoReq.setIsBottomPacking("1");
		batchInfoReq.setBatchCode(baseCode);
		batchInfoReq.setTraceCode(syCode);
		batchInfoReq.setOfficeId(office.getKuid());
		batchInfoReq.setSysId("1");
		batchInfoReq.setPackType("1");
		// 产品信息
		TraceProductReq traceProductReq = new TraceProductReq();
		// 获取采收类型名称
		StandardTaskItemsArgsValue taskListIdAndArgsName = standardTaskItemsArgsValueDao.getTaskListIdAndArgsName(standardTaskItemsArgsValue.getTaskListId(), "采收类型");
		// 获取快捷品种信息
		ProductLibraryQuickValue productLibraryQuickValue = productLibraryQuickValueDao.findByOfficeIdAndProductIdAndProductName(baseTree.getOfficeId(), productionBatch.getProductId() , taskListIdAndArgsName==null?"":taskListIdAndArgsName.getTaskItemArgsValue());
		if (null != productLibraryQuickValue) {
			// 产品名称
			if (StringUtils.isNotBlank(productLibraryQuickValue.getProductName())) {
				traceProductReq.setProductName(productLibraryQuickValue.getProductName());
			} else {
				List<ProductLibraryDetail> findById = productLibraryDetailDao.findById(productionBatch.getProductId());
				if (!findById.isEmpty() && StringUtils.isNotBlank(findById.get(0).getProductName())) {
					traceProductReq.setProductName(findById.get(0).getProductName());
				}
			}
			// 产品简介
			if (StringUtils.isNotBlank(productLibraryQuickValue.getProductPropagandaDescription())) {
				traceProductReq.setProductDiscription(productLibraryQuickValue.getProductPropagandaDescription());
			} else {
				List<ProductLibraryDetail> findById = productLibraryDetailDao.findById(productionBatch.getProductId());
				if (!findById.isEmpty() && StringUtils.isNotBlank(findById.get(0).getDescription())) {
					traceProductReq.setProductDiscription(findById.get(0).getDescription());
				}
			}
			// 产品图片
			if (StringUtils.isNotBlank(productLibraryQuickValue.getProductPropagandaImgUrl())) {
				traceProductReq.setProductPic(syImgUrl + productLibraryQuickValue.getProductPropagandaImgUrl());
			} else {
				List<ProductLibraryDetail> findById = productLibraryDetailDao.findById(productionBatch.getProductId());
				if (!findById.isEmpty() && StringUtils.isNotBlank(findById.get(0).getProductImagUrl())) {
					traceProductReq.setProductPic(syImgUrl + findById.get(0).getProductImagUrl());
				}
			}
		} else {
			// 品种表
			List<ProductLibraryDetail> findById = productLibraryDetailDao.findById(productionBatch.getProductId());
			if (!findById.isEmpty()) {
				ProductLibraryDetail productLibraryDetail = findById.get(0);
				// 产品名称
				if (StringUtils.isNotBlank(productLibraryDetail.getProductName())) {
					traceProductReq.setProductName(productLibraryDetail.getProductName());
				}
				// 产品简介
				if (StringUtils.isNotBlank(productLibraryDetail.getDescription())) {
					traceProductReq.setProductDiscription(productLibraryDetail.getDescription());
				}
				if (StringUtils.isNotBlank(productLibraryDetail.getProductImagUrl())) {
					traceProductReq.setProductPic(syImgUrl + productLibraryDetail.getProductImagUrl());
				}
			} else {
				traceProductReq.setProductName("");
				traceProductReq.setProductPic("");
				traceProductReq.setProductDiscription("");
			}
		}
		
		// 主题ID
		traceProductReq.setThemeId("039db9f681744daf90d1114fc86604de");
		// 主模块
		TraceModelListReq traceModelListReq1 = new TraceModelListReq();
		traceModelListReq1.setModelName("主模块");
		traceModelListReq1.setModelShowType("1");
		// 模块1
		Map<Object, Object> PropertyNewMap1 = new HashMap<Object, Object>();
		PropertyNewMap1.put("propertyType", "5");
		PropertyNewMap1.put("propertyNameZh", "生产日期");
		PropertyNewMap1.put("propertyNameEn", "dateOfManufacture");
		PropertyNewMap1.put("propertyValue", DateUtil.formatSmsDt(standardTaskItemsArgsValue.getCreateTime()));
		// 模块2
		Map<Object, Object> PropertyNewMap2 = new HashMap<Object, Object>();
		PropertyNewMap2.put("propertyType", "1");
		PropertyNewMap2.put("propertyNameZh", "保质期（天）");
		PropertyNewMap2.put("propertyNameEn", "qualityGuaranteePeriod");
		if (null != productLibraryQuickValue) {
			PropertyNewMap2.put("propertyValue", productLibraryQuickValue.getQualityGuaranteePeriod() == null ? "0" : productLibraryQuickValue.getQualityGuaranteePeriod());
		} else {
			PropertyNewMap2.put("propertyValue", "365");
		}
		// 添加模块信息
		List<Map<Object, Object>> tracePropertyNewList1 = new ArrayList<Map<Object, Object>>();
		tracePropertyNewList1.add(PropertyNewMap1);
		tracePropertyNewList1.add(PropertyNewMap2);
		// 增加到模块对象
		traceModelListReq1.setTracePropertyNewList(tracePropertyNewList1);
		
		// 生产过程模块
		TraceModelListReq traceModelListReq2 = new TraceModelListReq();
		traceModelListReq2.setModelName("生产过程");
		traceModelListReq2.setModelShowType("2");
		
		// 基地表信息
		Map<Object, Object> baseTreeMap = new HashMap<Object, Object>();
		baseTreeMap.put("officeId", office.getId());
		baseTreeMap.put("address", baseTree.getAddress());
		
		// 生产过程的批次和基地信息
		Map<Object, Object> PropertyNewMap = new HashMap<Object, Object>();
		PropertyNewMap.put("batchCode", productionBatch.getBatchCode());
		PropertyNewMap.put("baseTree", baseTreeMap);
		
		String hql = "select new com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskVo(pbt.id, pbtr.id, pbt.standardItemName, pbtr.dispatchTime, pbtr.finishTime, pbt.standardItemId, pbtr.nonexecutionReason ) from ProductBatchTask pbt,ProductBatchTaskResolve pbtr where pbt.id= pbtr.taskId and pbt.states <> 'D' and pbtr.executionStatus like '1' and pbtr.states <> 'D' and pbt.regionId =:p1 order by pbtr.finishTime asc";
		List<ResolveAndTaskVo> tasks = productBatchTaskDao.find(hql, new Parameter(productionBatch.getId()));
		// 生产过程信息
		List<Map<Object,Object>> recordList = new ArrayList<Map<Object,Object>>();
		for (int j = 0; j < tasks.size(); j++) {
			ResolveAndTaskVo ratVo = tasks.get(j);
			// 种植记录
			Map<Object, Object> taskVoMap = new HashMap<Object, Object>();
			String taskValueHql = "select new com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskValueVo(stiav.id, pbtr.id, stiav.taskListId, stiav.taskItemArgsId, stiav.argsName, stiav.taskItemArgsValue, stiav.argsUnit, stiav.argsType, stiav.argsValueDescription, stiav.sort) from ProductBatchTaskResolve pbtr, StandardTaskList stl, StandardTaskItemsArgsValue stiav where pbtr.id = stl.taskItemsId and stl.id = stiav.taskListId and stl.taskItemsId =:p1  order by stiav.sort";
			List<ResolveAndTaskValueVo> taskResoles = productBatchTaskDao.find(taskValueHql, new Parameter(ratVo.getTaskResoleId()));

			String propertyValue1 = "";
			String propertyValue2 = "";
			String propertyValue3 = "";
			boolean ste = true;
			UploadSetting uploadSetting = uploadSettingDao.findByOfficeidAndStandardIdAndStandardIdAndItemsId(baseTree.getOfficeId(), productionBatch.getStandardId(), ratVo.getStandardItemId());
			if (null != uploadSetting) {
				ste = false;
			} else {
				ste = true;
			}
			if(notUpload){
				NotUploadRecord taskValueVo = notUploadRecordDao.findByBatchIdAndDataId(productionBatch.getId(), ratVo.getTaskResoleId());
				if (null != taskValueVo) {
					ste = false;
				} else {
					ste = true;
				}
			}
			for (ResolveAndTaskValueVo pojoVo : taskResoles) {
				// 操作时间
				if ("3".equals(pojoVo.getArgsType())) {
					propertyValue1 = pojoVo.getTaskItemArgsValue();
				}

				// 操作图
				if ("6".equals(pojoVo.getArgsType())) {
					if(StringUtils.isNotBlank(pojoVo.getTaskItemArgsValue())){
						String[] img = pojoVo.getTaskItemArgsValue().split(",");
						propertyValue3 = img[0];
					}
				}

				if ("7".equals(pojoVo.getArgsType())) {
					if (StringUtils.isNotBlank(pojoVo.getTaskItemArgsValue())) {
						propertyValue2 += pojoVo.getArgsName() + ":" + pojoVo.getTaskItemArgsValue() + ",";
					}
				}

				if ("9".equals(pojoVo.getArgsType())) {
					if (StringUtils.isNotBlank(pojoVo.getTaskItemArgsValue())) {
						propertyValue2 += pojoVo.getArgsName() + ":" + pojoVo.getTaskItemArgsValue() + ",";
					}
				}

				if ("13".equals(pojoVo.getArgsType())) {
					if (StringUtils.isNotBlank(pojoVo.getTaskItemArgsValue())) {
						String taskItemArgsValue = pojoVo.getTaskItemArgsValue() == null ? "0" : pojoVo.getTaskItemArgsValue();
						String argsUnit = pojoVo.getArgsUnit() == null ? "" : pojoVo.getArgsUnit();
						propertyValue2 += pojoVo.getArgsName() + ":" + taskItemArgsValue + argsUnit + ",";
					}
				}
			}
		
			if (ste) {
				taskVoMap.put("propertyType1", "5");
				taskVoMap.put("propertyNameZh1", "操作时间");
				taskVoMap.put("propertyValue1", propertyValue1);
				if (StringUtils.isNotBlank(propertyValue2)) {
					taskVoMap.put("propertyType2", "1");
					taskVoMap.put("propertyNameZh2", "操作内容");
					taskVoMap.put("propertyValue2", ratVo.getStandardItemName() + "," + propertyValue2.substring(0, propertyValue2.length() - 1));
				} else {
					taskVoMap.put("propertyType2", "1");
					taskVoMap.put("propertyNameZh2", "操作内容");
					taskVoMap.put("propertyValue2", ratVo.getStandardItemName());
				}
				if (StringUtils.isNotBlank(propertyValue3)) {
					taskVoMap.put("propertyType3", "3");
					taskVoMap.put("propertyNameZh3", "操作图");
					taskVoMap.put("propertyValue3", syImgUrl + propertyValue3);
				} else {
					taskVoMap.put("propertyType3", "3");
					taskVoMap.put("propertyNameZh3", "操作图");
					taskVoMap.put("propertyValue3", "");
				}
				recordList.add(taskVoMap);
			}
		}
		// 添加模块信息
		List<Map<Object, Object>> tracePropertyNewList2 = new ArrayList<Map<Object, Object>>();
		tracePropertyNewList2.add(PropertyNewMap);
		// 增加到模块对象
		traceModelListReq2.setTracePropertyNewList(tracePropertyNewList2);
		// 添加生产过程
		PropertyNewMap.put("recordList", recordList);
		
		// 认定图片
		TraceModelListReq traceModelListReq3 = new TraceModelListReq();
		traceModelListReq3.setModelName("认定信息");
		traceModelListReq3.setModelShowType("1");

		if (StringUtils.isNotBlank(productionBatch.getConfirmationInformationPath())) {
			List<Map<Object, Object>> pathList = new ArrayList<Map<Object, Object>>();
			String[] strArr = productionBatch.getConfirmationInformationPath().split(",");
			for (int i = 0; i < strArr.length; ++i) {
				if (pathList.size() <= 3) {
					NotUploadRecord rzbgPojo = notUploadRecordDao.findByBatchIdAndDataId(productionBatch.getId(), strArr.toString());
					if (null == rzbgPojo) {
						Map<Object, Object> pathMap = new HashMap<Object, Object>();
						pathMap.put("propertyType", "3");
						pathMap.put("propertyNameZh", "认定图片");
						pathMap.put("propertyNameEn", "");
						pathMap.put("propertyValue", syImgUrl + strArr[i].toString());
						pathList.add(pathMap);
					}
				}
			}
			traceModelListReq3.setTracePropertyNewList(pathList);
		}
		
		// 质检信息
		TraceModelListReq traceModelListReq4 = new TraceModelListReq();
		traceModelListReq4.setModelName("质检信息");
		traceModelListReq4.setModelShowType("1");
		if (StringUtils.isNotBlank(productionBatch.getQualityTestPath())) {
			List<Map<Object, Object>> pathList = new ArrayList<Map<Object, Object>>();
			String[] strArr = productionBatch.getQualityTestPath().split(",");
			for (int i = 0; i < strArr.length; ++i) {
				if (pathList.size() <= 3) {
					NotUploadRecord rzbgPojo = notUploadRecordDao.findByBatchIdAndDataId(productionBatch.getId(), strArr.toString());
					if (null == rzbgPojo) {
						Map<Object, Object> pathMap = new HashMap<Object, Object>();

						pathMap.put("propertyType", "3");
						pathMap.put("propertyNameZh", "质检图片");
						pathMap.put("propertyNameEn", "");
						pathMap.put("propertyValue", syImgUrl + strArr[i].toString());
						pathList.add(pathMap);
					}
				}
			}
			traceModelListReq4.setTracePropertyNewList(pathList);
		}
		// 基本信息
		TraceModelListReq traceModelListReq5 = new TraceModelListReq();
		traceModelListReq5.setModelName("基本信息");
		traceModelListReq5.setModelShowType("1");
		
		List<Map<Object, Object>> dataInfo = new ArrayList<Map<Object, Object>>();
		// 产品图片
		Map<Object, Object> dataImg = new HashMap<Object, Object>();
		dataImg.put("propertyType", "3");
		dataImg.put("propertyNameZh", "产品图片");
		dataImg.put("propertyNameEn", "productPic");
		dataImg.put("propertyValue", traceProductReq.getProductPic());
		dataInfo.add(dataImg);
		// 产品名称
		Map<Object, Object> data0 = new HashMap<Object, Object>();
		data0.put("propertyType", "1");
		data0.put("propertyNameZh", "产品名称");
		data0.put("propertyNameEn", "productName");
		data0.put("propertyValue", taskListIdAndArgsName.getTaskItemArgsValue());
		dataInfo.add(data0);
		
		Map<Object, Object> data1 = new HashMap<Object, Object>();
		data1.put("propertyType", "1");
		data1.put("propertyNameZh", "产地");
		data1.put("propertyNameEn", "placeOfOrigin");
		if (StringUtils.isNotBlank(baseTree.getProvince())) {
			String province = baseTree.getProvince() == null ? "" : baseTree.getProvince();
			String city = baseTree.getCity() == null ? "" : baseTree.getCity();
			String area = baseTree.getArea() == null ? "" : baseTree.getArea();
			String address = baseTree.getAddress() == null ? "" : baseTree.getAddress();
			data1.put("propertyValue", province + city + area + address);
		} else {
			data1.put("propertyValue", "湖南省长沙市望城区");
		}
		dataInfo.add(data1);
		
		ProductionModel productionModel = productionModelDao.getProductionModel();
		if (null != productionModel) {
			Map<Object, Object> data2 = new HashMap<Object, Object>();
			data2.put("propertyType", "1");
			data2.put("propertyNameZh", "生产方式");
			data2.put("propertyNameEn", "modeOfProduction");
			data2.put("propertyValue", productionModel.getProductionName());
			dataInfo.add(data2);
		}
		
		BaseManager userId = baseManagerDao.getUserId(baseTree.getId(), "391192d4f9634982858634f12de44275");
		User user = userDao.get(userId.getUserId());
		if(null != user) {
			if (StringUtils.isNotBlank(user.getName())) {
				Map<Object, Object> data3 = new HashMap<Object, Object>();
				data3.put("propertyType", "1");
				data3.put("propertyNameZh", "负责人");
				data3.put("propertyNameEn", "personInCharge");
				data3.put("propertyValue", user.getName());
				dataInfo.add(data3);
			} 
			if (StringUtils.isNotBlank(user.getPhone())) {
				Map<Object, Object> data4 = new HashMap<Object, Object>();
				data4.put("propertyType", "1");
				data4.put("propertyNameZh", "联系电话");
				data4.put("propertyNameEn", "contactNumber");
				data4.put("propertyValue", user.getPhone());
				dataInfo.add(data4);
			} 
		}
		
		if (null != productLibraryQuickValue && StringUtils.isNotBlank(productLibraryQuickValue.getProductPropagandaDescription())) {
			Map<Object, Object> data5 = new HashMap<Object, Object>();
			data5.put("propertyType", "1");
			data5.put("propertyNameZh", "产品简介");
			data5.put("propertyNameEn", "productProfile");
			data5.put("propertyValue", productLibraryQuickValue.getProductPropagandaDescription());
			dataInfo.add(data5);
		} else {
			List<ProductLibraryDetail> findById = productLibraryDetailDao.findById(productionBatch.getProductId());
			Map<Object, Object> data5 = new HashMap<Object, Object>();
			if (!findById.isEmpty()) {
				ProductLibraryDetail productLibraryDetail = findById.get(0);
				data5.put("propertyType", "1");
				data5.put("propertyNameZh", "产品简介");
				data5.put("propertyNameEn", "productProfile");
				if(StringUtils.isNotBlank(productLibraryDetail.getDescription())) {
					data5.put("propertyValue", productLibraryDetail.getDescription());
					dataInfo.add(data5);
				}
			}
		}
		traceModelListReq5.setTracePropertyNewList(dataInfo);
		
		// 增加模块lIst
		List<TraceModelListReq> traceModelList = new ArrayList<TraceModelListReq>();
		traceModelList.add(traceModelListReq5);
		traceModelList.add(traceModelListReq1);
		traceModelList.add(traceModelListReq2);
		traceModelList.add(traceModelListReq3);
		traceModelList.add(traceModelListReq4);
		// 模块
		traceProductReq.setTraceModelList(traceModelList);
		// 返回对象
		batchInfoReq.setTraceProduct(traceProductReq);
		try {
			JSONObject json = new JSONObject().fromObject(batchInfoReq);
			System.out.println("上传数据请求JSON==================" + json);
			String accessDataInterfaceReq = Client.post(syUrl + "api/pcIndex/accessDataInterface", json, "UTF-8");
			JSONObject respJson = new JSONObject().fromObject(accessDataInterfaceReq);
			System.out.println("上传接口返回+++++++++++++++++" + respJson);
			if ("0".equals(respJson.get("code").toString())) {
				UploadAuditRecord pojo = new UploadAuditRecord();
				pojo.setOfficeId(office.getKuid());
				pojo.setBatchId(productionBatch.getId());
				pojo.setStandardTaskItemsArgsValueId(standardTaskItemsArgsValueId);
				pojo.setAuditStatus("1");
				pojo.setAuditTime(DateUtil.getCurrDateTime2());
				pojo.setSyBatchCode(baseCode); // 溯源批次号
				pojo.setSyCode(syCode);
				uploadAuditRecordDao.save(pojo);
			} else {
				Integer code = Integer.valueOf(respJson.get("code").toString());
				return ResultUtil.error(code, respJson.get("message").toString());
			}
		} catch (Exception e) {
			return ResultUtil.error(999999, "上传失败");
		}
		return ResultUtil.success("Success");
	}
	
	/**
	 * Title: batchUpload Description: 批量上传
	 * 
	 * @param batchList
	 * @return
	 */
	public ResultBean<String> batchUpload(List<String> standardTaskItemsArgsValueId) {
		for (String id : standardTaskItemsArgsValueId) {
			ResultBean<String> confirmationUpload = this.confirmationUpload(id);
			if (0 != confirmationUpload.getCode()) {
				return confirmationUpload;
			}
		}
		return ResultUtil.success("Success");
	}

	/**
	 * Title: autoUpload Description: 自动上传
	 * 
	 * @param officeId 公司id
	 * 
	 * @return
	 */
	public ResultBean<String> autoUpload(String officeId) {
		List<UploadAuditRecord> uploadAuditRecordList = uploadAuditRecordDao.getUploadAuditRecordList();
		String idStr = "";
		for (UploadAuditRecord uploadAuditRecord : uploadAuditRecordList) {
			idStr += "'" + uploadAuditRecord.getStandardTaskItemsArgsValueId() + "'" + ",";
		}
		if (StringUtils.isNotBlank(idStr)) {
			idStr = idStr.substring(0, idStr.length() - 1);
		}else {
			idStr = "''";
		}
		List<Office> findAllOffices = officeDao.findAllOfficesAndId(officeId, officeId);
		String ofStr = "";
		for (Office office : findAllOffices) {
			ofStr += "'" + office.getId() + "'" + ",";
		}
		if (StringUtils.isNotBlank(ofStr)) {
			ofStr = ofStr.substring(0, ofStr.length() - 1);
		}
		if (StringUtils.isBlank(ofStr)) {
			ofStr = "''";
		}
		List<Map<String, String>> list = standardTaskItemsArgsValueDao.List("采收量", idStr, ofStr, "391192d4f9634982858634f12de44275");
		for (Map<String, String> map : list) {
			String id = "";
			for(String key: map.keySet()){
				if ("id".equals(key)) {
					id = map.get(key);
				}
	        }
			if (StringUtils.isNotBlank(id)) {
				ResultBean<String> confirmationUpload = this.confirmationUpload(id);
				if (0 != confirmationUpload.getCode()) {
					return confirmationUpload;
				}
			}
		}
		return ResultUtil.success("Success");
	}
	
	public <T> Map<String, Object> beanToMap(T bean) {
		Map<String, Object> map = Maps.newHashMap();
		if (bean != null) {
			BeanMap beanMap = BeanMap.create(bean);
			for (Object key : beanMap.keySet()) {
				map.put(key + "", beanMap.get(key));
			}
		}
		return map;
	}

	/**
	 * Title: getAgroProductionInfo Description: 获取农事生产过程信息
	 * 
	 * @param officeId
	 *            公司
	 * @param batchCode
	 *            批次编码
	 * @return
	 */
	public List<Map<Object, Object>> getAgroProductionInfo(String officeId, String batchCode) {
		// 生产过程信息
		List<Map<Object,Object>> recordList = new ArrayList<Map<Object,Object>>();
		List<Object> batchTaskList = productBatchTaskResolveService.getBatchCode(batchCode);
		if (batchTaskList.size()==0) {
			return recordList;
		}
		Object[] batchTaskObj = (Object[]) batchTaskList.get(0);
		List<ProductionBatch> list = productionBatchDao.findByBatchCodeAndKuId(batchTaskObj[0].toString(), officeId);
		if (list.isEmpty()) {
			return recordList;
		}
		ProductionBatch productionBatch = list.get(0);
		
		String hql = "select new com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskVo(pbt.id, pbtr.id, pbt.standardItemName, pbtr.dispatchTime, pbtr.finishTime, pbt.standardItemId, pbtr.nonexecutionReason)"
				+ " from ProductBatchTask pbt,ProductBatchTaskResolve pbtr"
				+ " where pbt.id= pbtr.taskId and pbt.states <> 'D' and pbtr.states <> 'D'"
				+ " and pbtr.executionStatus like '1'"
				+ " and pbt.regionId =:p1 order by pbtr.finishTime asc";
		List<ResolveAndTaskVo> tasks = productBatchTaskDao.find(hql, new Parameter(productionBatch.getId()));
		
		for (int j = 0; j < tasks.size(); j++) {
			ResolveAndTaskVo ratVo = tasks.get(j);
			// 种植记录
			Map<Object, Object> taskVoMap = new HashMap<Object, Object>();
			String taskValueHql = "select new com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskValueVo(stiav.id, pbtr.id, stiav.taskListId, stiav.taskItemArgsId, stiav.argsName, stiav.taskItemArgsValue, stiav.argsUnit, stiav.argsType, stiav.argsValueDescription, stiav.sort) from ProductBatchTaskResolve pbtr, StandardTaskList stl, StandardTaskItemsArgsValue stiav where pbtr.id = stl.taskItemsId and stl.id = stiav.taskListId and stl.taskItemsId =:p1  order by stiav.sort";
			List<ResolveAndTaskValueVo> taskResoles = productBatchTaskDao.find(taskValueHql, new Parameter(ratVo.getTaskResoleId()));
			String propertyValue1 = "";
			String propertyValue2 = "";
			String propertyValue3 = "";
			// 是否添加到返回数据列表
			boolean falg = true;
			for (ResolveAndTaskValueVo pojoVo : taskResoles) {
				// 操作时间
				if ("3".equals(pojoVo.getArgsType())) {
					propertyValue1 = pojoVo.getTaskItemArgsValue();
				}

				// 操作图
				if ("6".equals(pojoVo.getArgsType())) {
					propertyValue3 = pojoVo.getTaskItemArgsValue();
				}

				if ("7".equals(pojoVo.getArgsType())) {
					if (StringUtils.isNotBlank(pojoVo.getTaskItemArgsValue())) {
						propertyValue2 += pojoVo.getArgsName() + ":" + pojoVo.getTaskItemArgsValue() + ",";
					}
				}

				if ("9".equals(pojoVo.getArgsType())) {
					if (StringUtils.isNotBlank(pojoVo.getTaskItemArgsValue())) {
						propertyValue2 += pojoVo.getArgsName() + ":" + pojoVo.getTaskItemArgsValue() + ",";
					}
				}

				if ("13".equals(pojoVo.getArgsType())) {
					if (StringUtils.isNotBlank(pojoVo.getTaskItemArgsValue())) {
						String taskItemArgsValue = pojoVo.getTaskItemArgsValue() == null ? "0" : pojoVo.getTaskItemArgsValue();
						String argsUnit = pojoVo.getArgsUnit() == null ? "" : pojoVo.getArgsUnit();
						propertyValue2 += pojoVo.getArgsName() + ":" + taskItemArgsValue + argsUnit + ",";
					}
				}
				// 不要之前的采收记录
				if ("采收类型".equals(pojoVo.getArgsName())&&!ratVo.getTaskResoleId().equals(batchTaskObj[1].toString())) {
					falg=false;
				}
			}
			taskVoMap.put("propertyType1", "5");
			taskVoMap.put("propertyNameZh1", "操作时间");
			taskVoMap.put("propertyValue1", propertyValue1);
			if (StringUtils.isNotBlank(propertyValue2)) {
				taskVoMap.put("propertyType2", "1");
				taskVoMap.put("propertyNameZh2", "操作内容");
				taskVoMap.put("propertyValue2", ratVo.getStandardItemName() + "," + propertyValue2.substring(0, propertyValue2.length() - 1));
			} else {
				taskVoMap.put("propertyType2", "1");
				taskVoMap.put("propertyNameZh2", "操作内容");
				taskVoMap.put("propertyValue2", ratVo.getStandardItemName());
			}
			if (StringUtils.isNotBlank(propertyValue3)) {
				taskVoMap.put("propertyType3", "3");
				taskVoMap.put("propertyNameZh3", "操作图");
				taskVoMap.put("propertyValue3", syImgUrl + propertyValue3);
			} else {
				taskVoMap.put("propertyType3", "3");
				taskVoMap.put("propertyNameZh3", "操作图");
				taskVoMap.put("propertyValue3", "");
			}
			if(falg){
				recordList.add(taskVoMap);
			}
			if(ratVo.getTaskResoleId().equals(batchTaskObj[1].toString())){
				break;
			}
		}
		return recordList;
	}
}
