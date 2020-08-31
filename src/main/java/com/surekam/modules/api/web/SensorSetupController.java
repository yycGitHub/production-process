package com.surekam.modules.api.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.sensorsetup.entity.SensorSetup;
import com.surekam.modules.agro.sensorsetup.service.SensorSetupService;
import com.surekam.modules.api.dto.req.SensorSetupReq;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 传感器管理Controller
 * 
 * @author luoxw
 * @version 2019-04-22
 */
@Api
@Controller
@RequestMapping(value = "api/sensorsetup")
public class SensorSetupController extends BaseController {

	@Autowired
	private SensorSetupService sensorSetupService;
	@Autowired
	private ApiUserService apiUserService;

	@ResponseBody
	@RequestMapping(value = "/getSensorSetupList")
	@ApiOperation(value = "获取传感器列表", httpMethod = "POST", notes = "获取传感器列表", consumes = "application/x-www-form-urlencoded")
	public String getBatchManageList(HttpServletRequest request, 
			@RequestParam(required = false) String sensorName,
			@RequestParam(required = false) String isOffice, 
			@RequestParam(required = false) String officeId,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson( ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			// 根据token获取用户信息
			User user = apiUserService.getUserByToken(token);
			Page<SensorSetup> page = sensorSetupService.find(pageno, pagesize, sensorName, isOffice, officeId, user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("获取list异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/savaSensorSetup")
	@ApiOperation(value = "新增或者修改传感器", httpMethod = "POST", notes = "新增或者修改传感器", consumes = "application/x-www-form-urlencoded")
	public String savaBatchManage(HttpServletRequest request,
			@RequestBody @ApiParam(name = "新增传感器信息", value = "传入json格式", required = true) SensorSetup sensorSetupVo) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			SensorSetup sensorSetup = new SensorSetup();
			User user = apiUserService.getUserByToken(token);
			if (sensorSetupService.existName(sensorSetupVo)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.SENSOR_NAME_EXIST.getCode(), ResultEnum.SENSOR_NAME_EXIST.getMessage()));
			}
			BeanUtils.copyProperties(sensorSetupVo, sensorSetup);
			if (StringUtils.isBlank(sensorSetupVo.getId())) {
				sensorSetup.setCreateTime(new Date());
				sensorSetup.setCreateUserId(user.getId());
				sensorSetup.setStates("A");
			} else {
				sensorSetup.setUpdateTime(new Date());
				sensorSetup.setStates("U");
				sensorSetup.setUpdateUserId(user.getId());
				sensorSetup.setId(sensorSetupVo.getId());
			}
			sensorSetup.setOfficeId(sensorSetupVo.getOfficeId());
			sensorSetup.setSensorName(sensorSetupVo.getSensorName());
			sensorSetup.setSensorSerialNumber(sensorSetupVo.getSensorSerialNumber());
			sensorSetup.setWebsite(sensorSetupVo.getWebsite());
			sensorSetup.setSensorDiscription(sensorSetupVo.getSensorDiscription());
			sensorSetupService.save(sensorSetup);
			if(StringUtils.isBlank(sensorSetup.getSubId())) {
				SensorSetup pojo = sensorSetupService.get(sensorSetup.getId());
				pojo.setSubId(sensorSetup.getId());
				sensorSetupService.save(pojo);
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/deleteSensorSetup")
	@ApiOperation(value = "删除传感器", httpMethod = "POST", notes = "删除传感器", consumes = "application/x-www-form-urlencoded")
	public String deleteBatchManage(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			sensorSetupService.delete(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getSensorSetupById")
	@ApiOperation(value = "根据ID查询传感器信息", httpMethod = "POST", notes = "根据ID查询传感器信息", consumes = "application/x-www-form-urlencoded")
	public String getBatchManage(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			SensorSetup sensorSetup = sensorSetupService.get(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(sensorSetup));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/findBySensorSetupList")
	@ApiOperation(value = "获取公司下的传感器", httpMethod = "POST", notes = "根据ID查询传感器信息", consumes = "application/x-www-form-urlencoded")
	public String findBySensorSetupList(HttpServletRequest request, @RequestParam String officeId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			List<SensorSetup> lsit = sensorSetupService.findBySensorSetupList(officeId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(lsit));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/savaUnifiedAuthenticationSensor")
	@ApiOperation(value = "统一认证传感器新增或修改接口", httpMethod = "POST", notes = "统一认证传感器新增或修改接口", consumes = "application/x-www-form-urlencoded")
	public String savaUnifiedAuthenticationSensor(HttpServletRequest request,
			@RequestBody @ApiParam(name = "新增传感器信息", value = "传入json格式", required = true) SensorSetupReq req) {
		try {
			String state = sensorSetupService.savaUnifiedAuthenticationSensor(req);
			if("notData".equals(state)){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), "没有匹配的公司"));
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(state));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/delUnifiedAuthenticationSensor")
	@ApiOperation(value = "统一认证传感器删除接口", httpMethod = "POST", notes = "统一认证传感器删除接口", consumes = "application/x-www-form-urlencoded")
	public String delUnifiedAuthenticationSensor(HttpServletRequest request, @RequestParam String subId) {
		try {
			String state = sensorSetupService.delUnifiedAuthenticationSensor(subId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(state));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

}
