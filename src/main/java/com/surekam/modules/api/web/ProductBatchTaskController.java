package com.surekam.modules.api.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JavaType;
import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.Client;
import com.surekam.common.utils.DateUtils;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.productbatchtask.entity.ProductBatchTask;
import com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskPage;
import com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskVo;
import com.surekam.modules.agro.productbatchtask.service.ProductBatchTaskService;
import com.surekam.modules.agro.productbatchtaskresolve.entity.ProductBatchTaskResolve;
import com.surekam.modules.agro.sensorsetup.entity.SensorData;
import com.surekam.modules.agro.sensorsetup.entity.SensorReturn;
import com.surekam.modules.agro.sensorsetup.entity.SensorTarget;
import com.surekam.modules.agro.sensorsetup.entity.WCSensorData;
import com.surekam.modules.agro.sensorsetup.entity.WCSensorTarget;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.ApiSystemService;

import io.swagger.annotations.ApiOperation;

/**
 * 任务管理Controller
 * 
 * @author lb
 * @version 2019-04-25
 */
@Controller
@RequestMapping(value = "api/productBatchTask")
public class ProductBatchTaskController extends BaseController {

	@Autowired
	private ProductBatchTaskService productBatchTaskService;

	@Autowired
	private ApiSystemService apiSystemService;

	@RequestMapping(value = "list", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "任务列表查询", notes = "任务列表查询", consumes = "application/json")
	public String list(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String regionId, 
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String states, 
			@RequestParam(required = false) Integer pageno,
			@RequestParam(required = false) Integer pagesize, 
			@RequestParam(required = false) String regionType) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		jsonMapper.setDateFormat(fmt);
		try {
			int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
			int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			Page<ProductBatchTask> page = new Page<ProductBatchTask>(pageNo, pageSize);
			page = productBatchTaskService.findTasks(page, currentUser, name, states, regionType, regionId);
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("任务列表查询错误：" + e);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TASK_LIST_ERROR.getCode(), ResultEnum.TASK_LIST_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, ProductBatchTask productBatchTask, Model model) {
		String token = request.getHeader("X-Token");
		//User currentUser = apiSystemService.findUserByToken(token);
		//productBatchTaskService.createFixedTask("8c67498927c74e7f95dc9958f1b0ff5e", currentUser);
		//model.addAttribute("productBatchTask", productBatchTask);
		return "modules/" + "productbatchtask/productBatchTaskForm";
	}

	@RequestMapping(value = "save", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "任务保存和修改", notes = "任务保存和修改", consumes = "application/x-www-form-urlencoded")
	public String save(ProductBatchTask productBatchTask, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		try {
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			productBatchTask.setOfficeId(currentUser.getCompany().getId());
			if (StringUtils.isNotBlank(productBatchTask.getId())) {
				productBatchTask.setUpdateUserId(currentUser.getId());
				productBatchTask.setUpdateTime(new Date());
			} else {
				productBatchTask.setCreateUserId(currentUser.getId());
			}
			productBatchTaskService.save(productBatchTask);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TASK_SAVE_ERROR.getCode(), ResultEnum.TASK_SAVE_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "删除任务", httpMethod = "POST", notes = "删除任务", consumes = "application/x-www-form-urlencoded")
	public String delete(String id, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		try {
			String flag = productBatchTaskService.delete(id);
			if ("0".equals(flag)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
			} else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TASK_DELETE_LIVE_ERROR.getCode(), ResultEnum.TASK_DELETE_LIVE_ERROR.getMessage()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TASK_LIST_ERROR.getCode(), ResultEnum.TASK_LIST_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "findTaskCarryList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "任务执行计划列表查询", notes = "任务执行计划列表查询", consumes = "application/json")
	public String findTaskCarryList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true) String taskId, 
			@RequestParam(required = false) String states,
			@RequestParam(required = false) Integer pageno, 
			@RequestParam(required = false) Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		jsonMapper.setDateFormat(fmt);
		try {
			int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
			int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
			Page<ProductBatchTaskResolve> page = new Page<ProductBatchTaskResolve>(pageNo, pageSize);
			page = productBatchTaskService.findTaskCarryList(page, taskId, states);
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("任务执行计划列表查询错误：" + e);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TASK_LIST_ERROR.getCode(), ResultEnum.TASK_LIST_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "fetchDailyTaskList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "任务列表查询", notes = "任务列表查询", consumes = "application/json")
	public String fetchDailyTaskList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String baseId, 
			@RequestParam(required = false) String isOffice,
			@RequestParam(required = false) String startDate, 
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) String taskType, 
			@RequestParam(required = false) String batchId) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate) && startDate.equals(endDate)) {
			fmt = new SimpleDateFormat("HH:mm:ss");
		}
		jsonMapper.setDateFormat(fmt);
		try {
			if (StringUtils.isNotBlank(startDate)) {
				startDate = startDate + " 00:00:00";
			}
			if (StringUtils.isNotBlank(endDate)) {
				endDate = endDate + " 23:59:59";
			}
			List<ResolveAndTaskVo> batchTasks = productBatchTaskService.fetchDailyTaskList(baseId, isOffice, startDate, endDate, taskType, batchId);
			return jsonMapper.toJson(ResultUtil.success(batchTasks));
		} catch (Exception e) {
			logger.error("任务列表查询错误：" + e);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TASK_LIST_ERROR.getCode(), ResultEnum.TASK_LIST_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "fetchSensorDailyData", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "传感数据查询", notes = "传感数据查询", consumes = "application/json")
	public String fetchSensorDailyData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String gatewayId, 
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
		jsonMapper.setDateFormat(fmt);
		try {
			Date stDate = new Date();
			Date edDate = new Date();
			if (StringUtils.isNotBlank(startDate)) {
				stDate = DateUtils.parseDate(startDate);
			}
			if (StringUtils.isNotBlank(endDate)) {
				edDate = DateUtils.parseDate(endDate);
			}
			List<SensorData> rebackSensorDatas = new ArrayList<SensorData>();
			// 如果是潜彬或者风向数据，调用他们的接口 2000000001:风向 ，2000000002潜彬
			if ("2000000001".equals(gatewayId) || "2000000002".equals(gatewayId)) {
				Map<Long, String> icon = new HashMap<Long, String>();
				icon.put(1l, "http://sensor.sureserve.cn/phone_wc/image/view/ph.png");// PH值传感器
				icon.put(2l, "http://sensor.sureserve.cn/phone_wc/image/view/o2.png");// 溶解氧传感器DO
				icon.put(3l, "http://sensor.sureserve.cn/phone_wc/image/view/nh4.png");// 氨氮传感器NH4
				icon.put(0l, "http://sensor.sureserve.cn/phone_wc/image/view/temp.png");// 水下温度传感器TEMP
				String fxqbTargeturl = "http://220.169.58.105/waterMonitor/data.action?load_sdates_by_asn";
				String fxqbDataurl = "http://220.169.58.105/waterMonitor/data.action?load_hours_data";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("asn", gatewayId));
				String targetResult = Client.post(fxqbTargeturl, params, fxqbTargeturl);
				JavaType targetType = jsonMapper.createCollectionType(List.class, WCSensorTarget.class);
				List<WCSensorTarget> sensorTargetData = jsonMapper.fromJson(targetResult, targetType);
				if (sensorTargetData != null && sensorTargetData.size() > 0) {
					for (int i = 0; i < sensorTargetData.size(); i++) {
						WCSensorTarget wcSensorTarget = sensorTargetData.get(i);
						List<NameValuePair> dataParams = new ArrayList<NameValuePair>();
						dataParams.add(new BasicNameValuePair("ssn", wcSensorTarget.getSensorSn()));
						dataParams.add(new BasicNameValuePair("stime", DateUtils.formatDate(stDate, "yyyyMMdd00")));
						dataParams.add(new BasicNameValuePair("etime", DateUtils.formatDate(edDate, "yyyyMMdd24")));
						String dataResult = Client.post(fxqbDataurl, dataParams, fxqbDataurl);
						JavaType dataType = jsonMapper.createCollectionType(List.class, WCSensorData.class);
						List<WCSensorData> wcSensorDatas = jsonMapper.fromJson(dataResult, dataType);
						SensorData sensorData = new SensorData();
						sensorData.setId(wcSensorTarget.getSensorSn());
						sensorData.setPathName(wcSensorTarget.getSensorSite());
						sensorData.setTargetName(wcSensorTarget.getTargetName().replace("传感器", ""));
						sensorData.setTargetUnit(wcSensorTarget.getTargetUnit());
						Long iconInt = Long.valueOf(wcSensorTarget.getSensorSn()) % 4;
						sensorData.setTargetIcon(icon.get(iconInt));
						// 求最大值，最小值，平均值
						if (wcSensorDatas != null && wcSensorDatas.size() > 0) {
							int size = wcSensorDatas.size();
							Collections.sort(wcSensorDatas, new Comparator<WCSensorData>() {
								@Override
								public int compare(WCSensorData o1, WCSensorData o2) {
									return o1.getMeasureData().compareTo(o2.getMeasureData());
								}
							});
							sensorData.setMeasureMinData(wcSensorDatas.get(0).getMeasureData() + "");
							sensorData.setMeasureMaxData(wcSensorDatas.get(size - 1).getMeasureData() + "");
							// 平均值
							Double countData = 0d;
							for (int j = 0; j < size; j++) {
								countData = countData + wcSensorDatas.get(j).getMeasureData();
							}
							Double measureData = countData / size;
							sensorData.setMeasureData(new BigDecimal(measureData).setScale(2, RoundingMode.HALF_UP) + "");
						}
						rebackSensorDatas.add(sensorData);
					}
				}
			} else {// 其他的为我们的数据
					// 1.获取传感器指标
				String url = "http://sensor.sureserve.cn/interfaces/gateway_handler.ashx?action=gateway_target";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("gid", gatewayId));
				String result = Client.post(url, params, url);
				JavaType javaType = jsonMapper.createCollectionType(SensorReturn.class, SensorTarget.class);
				SensorReturn<SensorTarget> sensorTargetData = jsonMapper.fromJson(result, javaType);
				if ("0".equals(sensorTargetData.getResult())) {
					List<SensorTarget> sensorTargets = sensorTargetData.getData();
					if (sensorTargets != null && sensorTargets.size() > 0) {
						for (int i = 0; i < sensorTargets.size(); i++) {
							SensorTarget sensorTarget = sensorTargets.get(i);
							String dataUrl = "http://sensor.sureserve.cn/interfaces/target_handler.ashx?action=target_day";
							List<NameValuePair> dataParams = new ArrayList<NameValuePair>();
							dataParams.add(new BasicNameValuePair("stid", sensorTarget.getId()));
							dataParams.add(new BasicNameValuePair("tid", sensorTarget.getTargetId()));
							dataParams.add(new BasicNameValuePair("stime", DateUtils.formatDate(stDate, "yyyyMMdd")));
							dataParams.add(new BasicNameValuePair("etime", DateUtils.formatDate(edDate, "yyyyMMdd")));
							String dataResult = Client.post(dataUrl, dataParams, dataUrl);
							JavaType sensorDataType = jsonMapper.createCollectionType(SensorReturn.class, SensorData.class);
							SensorReturn<SensorData> sensorDatas = jsonMapper.fromJson(dataResult, sensorDataType);
							List<SensorData> sensorDatasList = sensorDatas.getData();
							for (int j = 0; j < sensorDatasList.size(); j++) {
								SensorData sensorData = sensorDatasList.get(j);
								sensorData.setTargetName(sensorTarget.getTargetName());
								sensorData.setTargetUnit(sensorTarget.getTargetUnit());
								sensorData.setTargetIcon("http://sensor.sureserve.cn" + sensorTarget.getTargetIcon());
								rebackSensorDatas.add(sensorData);
							}
						}
					}
				}
			}
			return jsonMapper.toJson(ResultUtil.success(rebackSensorDatas));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("传感数据查询：" + e);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TASK_LIST_ERROR.getCode(), ResultEnum.TASK_LIST_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/findByBatchIdQueryTask")
	@ApiOperation(value = "根据批次ID查询相关任务", httpMethod = "POST", notes = "根据批次ID查询相关任务", consumes = "application/x-www-form-urlencoded")
	public String findByBatchIdQueryTask(HttpServletRequest request, @RequestParam String batchId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			Map<String, Object> findByBatchIdQueryTask = productBatchTaskService.findByBatchIdQueryTask(batchId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(findByBatchIdQueryTask));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "fetchDailyTaskPage", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "任务列表查询", notes = "任务列表查询", consumes = "application/json")
	public String fetchDailyTaskPage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String baseId, 
			@RequestParam(required = false) String isOffice,
			@RequestParam(required = false) String startDate, 
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) String taskType, 
			@RequestParam(required = false) String batchId,
			@RequestParam(required = false) Integer pageno, 
			@RequestParam(required = false) Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<ResolveAndTaskPage> page = new Page<ResolveAndTaskPage>(pageNo, pageSize);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate) && startDate.equals(endDate)) {
			fmt = new SimpleDateFormat("HH:mm:ss");
		}
		jsonMapper.setDateFormat(fmt);
		try {
			if (StringUtils.isNotBlank(startDate)) {
				startDate = startDate + " 00:00:00";
			}
			if (StringUtils.isNotBlank(endDate)) {
				endDate = endDate + " 23:59:59";
			}
			page = productBatchTaskService.fetchDailyTaskPage(page,baseId, isOffice, startDate, endDate, taskType, batchId);
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("任务列表查询错误：" + e);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TASK_LIST_ERROR.getCode(), ResultEnum.TASK_LIST_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "taskListPC", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "任务列表查询PC基地", notes = "任务列表查询PC基地", consumes = "application/json")
	public String taskListPC(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String baseId, 
			@RequestParam(required = false) String isOffice,
			@RequestParam(required = false) String batchId) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		List<ResolveAndTaskVo> batchTasks = productBatchTaskService.fetchDailyTaskList(baseId, isOffice, batchId);
		return jsonMapper.toJson(ResultUtil.success(batchTasks));
	}

	@RequestMapping(value = "deleteTask", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "删除操作过的任务", notes = "删除操作过的任务", consumes = "application/json")
	public String deleteTask(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String taskResoleId) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		try {
			productBatchTaskService.deleteTask(taskResoleId);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.OPERATION_FAILED.getCode(), ResultEnum.OPERATION_FAILED.getMessage()));
		}
	}

}
