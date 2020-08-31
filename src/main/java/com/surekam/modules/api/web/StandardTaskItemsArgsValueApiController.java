package com.surekam.modules.api.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.standardtaskitemsargsvalue.service.StandardTaskItemsArgsValueService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 标准作业执行记录表Controller
 * 
 * @author liwei
 * @version 2019-04-23
 */
@Api
@Controller
@RequestMapping(value = "api/standardTaskItemsArgsValue")
public class StandardTaskItemsArgsValueApiController extends BaseController {

	@Autowired
	private StandardTaskItemsArgsValueService standardTaskItemsArgsValueService;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@ResponseBody
	@RequestMapping(value = "/getStandardTaskItemsArgsValueList")
	@ApiOperation(value = "基地确认采收列表", httpMethod = "POST", notes = "基地确认采收列表表", consumes = "application/x-www-form-urlencoded")
	public String getStandardItemArgsList(HttpServletRequest request, @RequestParam Integer pageno, @RequestParam Integer pagesize, @RequestParam String batchId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			Page<Map<String, String>> page = standardTaskItemsArgsValueService.StandardItemArgsValueList(pageno, pagesize, batchId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		} 
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateTaskItemArgsValue")
	@ApiOperation(value = "修改采收量", httpMethod = "POST", notes = "修改采收量", consumes = "application/x-www-form-urlencoded")
	public String updateTaskItemArgsValue(HttpServletRequest request, 
			@RequestParam String id,
			@RequestParam String taskItemArgsValue,
			@RequestParam String taskItemArgsValueName) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			standardTaskItemsArgsValueService.updateTaskItemArgsValue(user, id, taskItemArgsValue, taskItemArgsValueName);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(""));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getStandardItemArgsValueList")
	@ApiOperation(value = "获取采收类型", httpMethod = "POST", notes = "获取采收类型", consumes = "application/x-www-form-urlencoded")
	public String getStandardItemArgsValueList(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			Map<String, Object> mapList = standardTaskItemsArgsValueService.getStandardItemArgsValueList(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(mapList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
}
