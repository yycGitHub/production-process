package com.surekam.modules.api.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.auditrecord.entity.AuditRecord;
import com.surekam.modules.agro.auditrecord.service.AuditRecordService;
import com.surekam.modules.agro.basemanage.service.BaseTreeService;
import com.surekam.modules.agro.experts.entity.ExpertServiceInfo;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.file.service.AgroFileInfoService;
import com.surekam.modules.agro.label.entity.Label;
import com.surekam.modules.agro.label.service.LabelService;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.agro.productbatchtask.entity.ProductBatchTask;
import com.surekam.modules.agro.productbatchtask.service.ProductBatchTaskService;
import com.surekam.modules.agro.productbatchtaskresolve.entity.ProductBatchTaskResolve;
import com.surekam.modules.agro.productbatchtaskresolve.service.ProductBatchTaskResolveService;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.agro.productionbatch.serivce.ProductionBatchService;
import com.surekam.modules.agro.standarditemargs.entity.StandardItemArgs;
import com.surekam.modules.agro.standarditemargs.service.StandardItemArgsService;
import com.surekam.modules.agro.standarditemargsvalue.entity.StandardItemArgsValue;
import com.surekam.modules.agro.standarditemargsvalue.service.StandardItemArgsValueService;
import com.surekam.modules.agro.standarditems.dao.StandardItemsAppDao;
import com.surekam.modules.agro.standarditems.entity.StandardItemsApp;
import com.surekam.modules.agro.standarditems.service.StandardItemsService;
import com.surekam.modules.agro.standardtaskitemsargsvalue.entity.StandardTaskItemsArgsValue;
import com.surekam.modules.agro.standardtaskitemsargsvalue.service.StandardTaskItemsArgsValueService;
import com.surekam.modules.agro.standardtasklist.entity.StandardTaskList;
import com.surekam.modules.agro.standardtasklist.service.StandardTaskListService;
import com.surekam.modules.agro.video.dao.VideoDao;
import com.surekam.modules.agro.video.entity.Video;
import com.surekam.modules.agro.video.service.VideoService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.api.service.BaseManageApiService;
import com.surekam.modules.api.service.BatchManageApiService;
import com.surekam.modules.sys.entity.Dict;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.utils.DictUtils;
import com.surekam.modules.sys.utils.StaticStringUtils;

/**
 * 专家
 * 
 * @author
 *
 */
@Api
@Controller
@RequestMapping(value = "api/home_nh")
public class HomeNhController {
	
	@Autowired
	private ApiUserService apiUserService;
	
	@Autowired
	private ProductionBatchService productionBatchService;
	
	@Autowired
	private ExpertsService expertsService;

	@Autowired
	private LabelService labelService;
	
	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;
	
	@Autowired
	private ProductBatchTaskService productBatchTaskService;
	
	@Autowired
	private ProductBatchTaskResolveService productBatchTaskResolveService;
	
	@Autowired
	private StandardTaskListService standardTaskListService;

	@Autowired
	private AgroFileInfoService fileInfoService;
	
	@Autowired
	private StandardItemArgsService standardItemArgsService;
	
	@Autowired
	private StandardItemArgsValueService standardItemArgsValueService;
	
	@Autowired
	private StandardTaskItemsArgsValueService standardTaskItemsArgsValueService;
	
	@Autowired
	private StandardItemsAppDao standardItemsAppDao;
	
	@Autowired
	private BatchManageApiService batchManageApiService;
	
	@Autowired
	private BaseManageApiService baseManageApiService;
	
	@Autowired
	private AuditRecordService auditRecordService;
	
	@Autowired
	private BaseTreeService baseTreeService;
	
	@Autowired
	private VideoDao videoDao;
	
	/**
	 * 获取未完成批次任务列表
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @param batchId
	 * @return
	 */
	@RequestMapping(value = "getBatchNotTaskList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取未完成批次任务列表", httpMethod = "GET", notes = "获取未完成批次任务列表", consumes = "application/x-www-form-urlencoded")
	public String getBatchNotTaskList(HttpServletRequest request, HttpServletResponse response,  @RequestParam Integer pageno,
			@RequestParam Integer pagesize, @RequestParam String batchId) {
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Object> page = new Page<Object>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			page = productBatchTaskResolveService.getBatchNotTaskList(page, batchId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 获取已完成批次任务列表
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @param batchId
	 * @return
	 */
	@RequestMapping(value = "getBatchTaskList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取已完成批次任务列表", httpMethod = "GET", notes = "获取已完成批次任务列表", consumes = "application/x-www-form-urlencoded")
	public String getBatchTaskList(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer pageno,
			@RequestParam Integer pagesize, @RequestParam String batchId) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Object> page = new Page<Object>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			page = productBatchTaskResolveService.getBatchTaskList(page, batchId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 获取未完成任务列表
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getAllNotTaskList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取未完成任务列表", httpMethod = "GET", notes = "获取未完成任务列表", consumes = "application/x-www-form-urlencoded")
	public String getAllNotTaskList(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer pageno,@RequestParam Integer pagesize, @RequestParam(required = false) String baseId) {
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Object> page = new Page<Object>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			page = productBatchTaskResolveService.getAllNotTaskListNew(page,user,baseId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 获取已完成任务列表
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getAllTaskList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取已完成批次任务列表", httpMethod = "GET", notes = "获取已完成批次任务列表", consumes = "application/x-www-form-urlencoded")
	public String getAllTaskList(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer pageno,@RequestParam Integer pagesize, @RequestParam(required = false) String baseId) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Object> page = new Page<Object>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			page = productBatchTaskResolveService.getAllTaskList(page,user,baseId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@RequestMapping(value = "getBatchCount", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取批次任务数量", httpMethod = "GET", notes = "获取批次任务数量", consumes = "application/x-www-form-urlencoded")
	public String getBatchCount(HttpServletRequest request, HttpServletResponse response, @RequestParam String batchId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			String finishCount = productBatchTaskResolveService.getBatchCount(batchId,"1");
			String notFinishCount = productBatchTaskResolveService.getBatchCount(batchId,"0");
			map.put("finishCount", finishCount);
			map.put("notFinishCount", notFinishCount);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@RequestMapping(value = "getAllCount", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取任务数量", httpMethod = "GET", notes = "获取任务数量", consumes = "application/x-www-form-urlencoded")
	public String getAllCount(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String baseId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			String finishCount = productBatchTaskResolveService.getAllTaskListCount(user,baseId);
			String notFinishCount = productBatchTaskResolveService.getAllCountnew("0",user,baseId);
			map.put("finishCount", finishCount);
			map.put("notFinishCount", notFinishCount);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@RequestMapping(value = "getWWCCount", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取未完成任务数量", httpMethod = "GET", notes = "获取未完成任务数量", consumes = "application/x-www-form-urlencoded")
	public String getWWCCount(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String officeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			String notFinishCount = productBatchTaskResolveService.getAllCount("0",user,officeId);
			map.put("notFinishCount", notFinishCount);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@RequestMapping(value = "getStandardTypeList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取标准类型", httpMethod = "GET", notes = "获取标准类型", consumes = "application/x-www-form-urlencoded")
	public String getStandardTypeList(HttpServletRequest request, HttpServletResponse response, @RequestParam String batchId) {
		String token = request.getHeader("X-Token");
		if (token != null) {
			ProductionBatch productionBatch = productionBatchService.get(batchId);
			String standardId = productionBatch.getStandardId();
			List<StandardItemsApp> list = standardItemsAppDao.getStandardTypeList(standardId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@RequestMapping(value = "getStandardItemsInfo", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取标准参数信息", httpMethod = "GET", notes = "获取标准参数信息", consumes = "application/x-www-form-urlencoded")
	public String getStandardItemsInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam String itemId, @RequestParam(required = false) String taskId, @RequestParam String batchId) {
		String token = request.getHeader("X-Token");
		if (token != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			StandardItemsApp standardItems = standardItemsAppDao.get(itemId);
			standardItems.setTaskId(taskId);
			standardItems.setBatchId(batchId);
			List<StandardItemArgs> standardItemArgsList = standardItemArgsService.getStandardItemArgsInfo(itemId);
			List<StandardItemArgs> resultList = new ArrayList<StandardItemArgs>();
			for (StandardItemArgs standardItemArgs : standardItemArgsList) {
				StandardTaskItemsArgsValue standardTaskItemsArgsValue = standardTaskItemsArgsValueService.getStandardTaskItemsArgsValue(batchId,standardItemArgs.getId());
				if(standardTaskItemsArgsValue!=null){
					if(standardTaskItemsArgsValue.getArgsType().equals("0") || standardTaskItemsArgsValue.getArgsType().equals("1") || standardTaskItemsArgsValue.getArgsType().equals("13")
							 || standardTaskItemsArgsValue.getArgsType().equals("7") || standardTaskItemsArgsValue.getArgsType().equals("9")){
						standardItemArgs.setDefaultValue(standardTaskItemsArgsValue.getTaskItemArgsValue());
					}
				}
				List<StandardItemArgsValue> standardItemArgsValueList = standardItemArgsValueService.getStandardItemArgsValueList(standardItemArgs.getId());
				if(standardItemArgsValueList.size()>0){
					standardItemArgs.setStandardItemArgsValueList(standardItemArgsValueList);
				}else{
					standardItemArgs.setStandardItemArgsValueList(new ArrayList<StandardItemArgsValue>());
				}
				if(standardItemArgs.getArgsType().equals("3")){
					standardItemArgs.setDefaultValue(sdf.format(new Date()));
				}
				resultList.add(standardItemArgs);
			}
			standardItems.setStandardItemArgsList(standardItemArgsList);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(standardItems));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@RequestMapping(value = "getProductBatchTaskResolveInfo", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取子任务信息", httpMethod = "GET", notes = "获取子任务信息", consumes = "application/x-www-form-urlencoded")
	public String getProductBatchTaskResolveInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam String taskId) {
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			ProductBatchTaskResolve productBatchTaskResolve = productBatchTaskResolveService.get(taskId);
			List<Label> labelList = labelService.getLabelList(user);
			productBatchTaskResolve.setLabelList(labelList);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(productBatchTaskResolve));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * @author 批次完成操作
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "saveBatchStatusData", produces = "text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "批次完成操作", httpMethod = "POST", notes = "批次完成操作", consumes = "application/json")
	public String saveBatchStatusData(HttpServletRequest request,@RequestParam String id,@RequestParam String status,@RequestParam String finishTime,@RequestParam(required = false) String remark) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User user = apiUserService.getUserByToken(token);
			productionBatchService.saveExecutionTaskData(id,status,user,finishTime,remark);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * @author 保存任务作业数据(执行)
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "saveExecutionTaskData", produces = "text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "保存任务作业数据(执行)", httpMethod = "POST", notes = "保存任务作业数据(执行)", consumes = "application/json")
	public String saveExecutionTaskData(HttpServletRequest request,
			@RequestBody @ApiParam(name = "任务作业项数据", value = "传入json格式", required = true) StandardItemsApp standardItems,
			@RequestParam(required = false) String address, @RequestParam(required = false) String latitude, @RequestParam(required = false) String longitude) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User user = apiUserService.getUserByToken(token);
			productBatchTaskResolveService.saveExecutionTaskData(standardItems,user, address, latitude, longitude);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * @author 保存自主任务作业数据(执行)
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "saveZZExecutionTaskData", produces = "text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "保存任务作业数据(执行)", httpMethod = "POST", notes = "保存任务作业数据(执行)", consumes = "application/json")
	public String saveZZExecutionTaskData(HttpServletRequest request,
			@RequestBody @ApiParam(name = "任务作业项数据", value = "传入json格式", required = true) StandardItemsApp standardItems,
			@RequestParam(required = false) String address, @RequestParam(required = false) String latitude, @RequestParam(required = false) String longitude) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User user = apiUserService.getUserByToken(token);
			productBatchTaskResolveService.saveZZExecutionTaskData(standardItems,user, address, latitude, longitude);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * @author 保存任务作业数据(不执行)
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "saveNonExecutionTaskData", produces = "text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "保存任务作业数据(不执行)", httpMethod = "POST", notes = "保存任务作业数据(执行)", consumes = "application/json")
	public String saveNonExecutionTaskData(HttpServletRequest request,
			@RequestBody @ApiParam(name = "任务作业项数据", value = "传入json格式", required = true) ProductBatchTaskResolve productBatchTaskResolve,
			@RequestParam(required = false) String address, @RequestParam(required = false) String latitude, @RequestParam(required = false) String longitude) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User user = apiUserService.getUserByToken(token);
			productBatchTaskResolveService.saveNonExecutionTaskData(productBatchTaskResolve,user, address, latitude, longitude);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@RequestMapping(value = "getProductBatchTaskResolveDetail", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取子任务及参数信息", httpMethod = "GET", notes = "获取子任务及参数信息", consumes = "application/x-www-form-urlencoded")
	public String getProductBatchTaskResolveDetail(HttpServletRequest request, HttpServletResponse response, @RequestParam String taskId) {
		String token = request.getHeader("X-Token");
		if (token != null) {
			ProductBatchTaskResolve productBatchTaskResolve = productBatchTaskResolveService.get(taskId);
			ProductBatchTask productBatchTask = productBatchTaskService.get(productBatchTaskResolve.getTaskId());
			productBatchTaskResolve.setStandardItemName(productBatchTask.getStandardItemName());
			StandardTaskList standardTaskList = standardTaskListService.getStandardTaskList(productBatchTaskResolve.getId());
			if(standardTaskList!=null){
				List<StandardTaskItemsArgsValue> standardTaskItemsArgsValueList = standardTaskItemsArgsValueService.getStandardTaskItemsArgsValueList(standardTaskList.getId());
				List<StandardTaskItemsArgsValue> rList = new ArrayList<StandardTaskItemsArgsValue>();
				for (StandardTaskItemsArgsValue standardTaskItemsArgsValue : standardTaskItemsArgsValueList) {
					if(standardTaskItemsArgsValue.getArgsType().equals("6")){
						String imgsrc = standardTaskItemsArgsValue.getTaskItemArgsValue();
						if(StringUtils.isNotBlank(imgsrc)){
							String[] result = imgsrc.split(",");
							List<String> list = Arrays.asList(result); 
							standardTaskItemsArgsValue.setImageSrc(list);
						}
					}
					rList.add(standardTaskItemsArgsValue);
				}
				standardTaskList.setStandardTaskItemsArgsValueList(rList);
			}
			productBatchTaskResolve.setStandardTaskList(standardTaskList);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(productBatchTaskResolve));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@RequestMapping(value = "getBatchInfo", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取批次信息和专家信息", httpMethod = "GET", notes = "获取批次信息和专家信息", consumes = "application/x-www-form-urlencoded")
	public String getBatchInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam String batchId) {
		String token = request.getHeader("X-Token");
		if (token != null) {
			ProductionBatch productBatch = productionBatchService.get(batchId);
			ProductLibraryTree productLibrary = productLibraryTreeService.get(productBatch.getProductId());
			List<Experts> expertsList = expertsService.getExpertsListByProductId(productLibrary);
			productBatch.setExpertsList(expertsList);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(productBatch));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * @author 删除批次数据
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "deleteBatchData", produces = "text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "保存任务作业数据(不执行)", httpMethod = "POST", notes = "保存任务作业数据(执行)", consumes = "application/json")
	public String deleteBatchData(HttpServletRequest request, @RequestParam String id) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User user = apiUserService.getUserByToken(token);
			String savaBatchManage = batchManageApiService.deleteBatchManage(user, id);
			if("Success".equals(savaBatchManage)){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
			}else{
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.BATCH_DELETE_ERROR.getCode(), ResultEnum.BATCH_DELETE_ERROR.getMessage()));
			}
		}else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@RequestMapping(value = "getUserSSArea", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取用户所属区域", httpMethod = "GET", notes = "获取用户所属区域", consumes = "application/x-www-form-urlencoded")
	public String getUserSSArea(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			List<Map<String,Object>> list = baseManageApiService.getUserSSArea(user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 获取检查记录列表
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @param batchId
	 * @return
	 */
	@RequestMapping(value = "getBaseAuditList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取检查记录列表", httpMethod = "GET", notes = "获取检查记录列表", consumes = "application/x-www-form-urlencoded")
	public String getBaseAuditList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Integer pageno, @RequestParam Integer pagesize, @RequestParam String baseId) {
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<AuditRecord> page = new Page<AuditRecord>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			List<AuditRecord> list = new ArrayList<AuditRecord>();
			page = auditRecordService.getAuditRecordList(page, baseId);
			for (AuditRecord auditRecord : page.getList()) {
				if (StringUtils.isNotBlank(auditRecord.getAuditType())) {
					auditRecord.setAuditTypeName(DictUtils.getDictLabel(auditRecord.getAuditType(), "check", ""));
					list.add(auditRecord);
				}
			}
			page.setList(list);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 获取采收任务列表
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @param baseId
	 * @return
	 */
	@RequestMapping(value = "getBaseCollectionConfirmationList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取采收任务列表", httpMethod = "GET", notes = "获取采收任务列表", consumes = "application/x-www-form-urlencoded")
	public String getBaseCollectionConfirmationList(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer pageno,
			@RequestParam Integer pagesize, @RequestParam String baseId) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Object> page = new Page<Object>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			page = productBatchTaskResolveService.getBatchTaskList(page, baseId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 获取检查类型列表
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @param baseId
	 * @return
	 */
	@RequestMapping(value = "getAuditTypeList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取采收任务列表", httpMethod = "GET", notes = "获取采收任务列表", consumes = "application/x-www-form-urlencoded")
	public String getAuditTypeList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			List<Dict> list = DictUtils.getDictList("check"); 
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * @author 
	 * 检查项目保存
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "saveAuditInfo",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "检查项目保存", httpMethod = "POST", notes = "检查项目保存",	consumes="application/json")
	public String saveAuditInfo(HttpServletRequest request,HttpServletResponse response,String typeId,String baseId,String [] photo,String auditDate,String details,String [] auditUrl) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User user = apiUserService.getUserByToken(token); 
			auditRecordService.saveAuditRecord(user,baseId,auditDate,typeId,details,photo,auditUrl);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 *通过ID获取检查项目详情
	 * @author 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getAuditInfoById",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "通过ID获取检查项目详情", httpMethod = "GET", notes = "通过ID获取检查项目详情",	consumes="application/x-www-form-urlencoded")
	public String getAuditInfoById(HttpServletRequest request,HttpServletResponse response,String id) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			AuditRecord auditRecord = auditRecordService.get(id);
			auditRecord.setFileList(fileInfoService.find(auditRecord.getId(),"1"));
			auditRecord.setAuditList(fileInfoService.find(auditRecord.getId(),"4"));
			auditRecord.setAuditTypeName(DictUtils.getDictLabel(auditRecord.getAuditType(), "check", ""));
			auditRecord.setBaseName(baseTreeService.get(auditRecord.getBaseId()).getName());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(auditRecord));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	/**
	 *获取平台数据
	 * @author 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getPlatformData",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "获取平台数据", httpMethod = "GET", notes = "获取平台数据", consumes="application/x-www-form-urlencoded")
	public String getPlatformData(HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		Map<String,Object> map = new HashMap<String, Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			String code = "0";//平台编码
			String flag = "0";//是否是平台账号
			User user = apiUserService.getUserByToken(token);
			Video video = videoDao.getVideoByUserCode(user.getLoginName());
			if(video!=null && video.getType().equals("专家")){
				String platform = video.getPlatform();
				if(StringUtils.isNotBlank(platform)){
					code = platform.split(",")[0];
					flag = "1";
				}
			}else{
				List<String> list = apiUserService.getChildUsers_new(user);
				String userData = "'" + StringUtils.join(list, "','") + "'";
				map.put("userData", userData);
				
				String parentIds = user.getOffice().getParentIds();
				String officeId = user.getOffice().getId();
				if(parentIds.indexOf(StaticStringUtils.AGRO_TULUFAN)>-1){
					code = "tulufan";
				}else if(parentIds.indexOf(StaticStringUtils.AGRO_WANGCHENG)>-1){
					code = "wangcheng";
				}else if(parentIds.indexOf(StaticStringUtils.AGRO_LIUYANG)>-1){
					code = "rice";
				}else if(officeId.equals(StaticStringUtils.AGRO_TULUFAN)){
					code = "tulufan";
					flag = "1";
				}else if(officeId.equals(StaticStringUtils.AGRO_WANGCHENG)){
					code = "wangcheng";
					flag = "1";
				}else if(officeId.equals(StaticStringUtils.AGRO_LIUYANG)){
					code = "rice";
					flag = "1";
				}
			}
			map.put("platform", code);
			map.put("flag", flag);
			map.put("baseName", user.getOffice().getName());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 获取已完成采收任务列表
	 * @author
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @param baseId
	 * @return
	 */
	@RequestMapping(value = "getCollectionTaskList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取已完成采收任务列表", httpMethod = "GET", notes = "获取已完成采收任务列表", consumes = "application/x-www-form-urlencoded")
	public String getCollectionTaskList(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer pageno,
			@RequestParam Integer pagesize, @RequestParam String baseId) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Object> page = new Page<Object>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			page = productBatchTaskResolveService.getCollectionTaskList(page, baseId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 采收任务确认操作
	 * @author
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @param baseId
	 * @return
	 */
	@RequestMapping(value = "saveTaskConfirmStates", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "采收任务确认操作", httpMethod = "GET", notes = "采收任务确认操作", consumes = "application/x-www-form-urlencoded")
	public String saveTaskConfirmStates(HttpServletRequest request, HttpServletResponse response, @RequestParam String id) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			productBatchTaskResolveService.saveTaskConfirmStates(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
}
