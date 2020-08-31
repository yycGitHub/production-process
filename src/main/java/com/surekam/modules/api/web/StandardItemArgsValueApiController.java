package com.surekam.modules.api.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.standarditemargsvalue.entity.StandardItemArgsValue;
import com.surekam.modules.agro.standarditemargsvalue.service.StandardItemArgsValueService;
import com.surekam.modules.api.dto.req.StandardItemArgsValueReq;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 标准作业参数多项值表Controller
 * 
 * @author liwei
 * @version 2019-04-26
 */

@Api
@Controller
@RequestMapping(value = "api/standardItemArgsValue")
public class StandardItemArgsValueApiController extends BaseController {

	@Autowired
	private StandardItemArgsValueService standardItemArgsValueService;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@ResponseBody
	@RequestMapping(value = "/getStandardItemArgsValueList")
	@ApiOperation(value = "获取标准作业参数多项 列表值", httpMethod = "POST", notes = "获取标准作业参数多项 列表值", consumes = "application/x-www-form-urlencoded")
	public String getStandardItemArgsValueList(HttpServletRequest request, 
			@RequestParam Integer pageno,
			@RequestParam Integer pagesize, 
			@RequestParam String itemArgsId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}

			Page<StandardItemArgsValue> page = standardItemArgsValueService.getStandardItemArgsValueList(pageno, pagesize, itemArgsId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getStandardItemArgsValue")
	@ApiOperation(value = "获取标准作业参数多项值", httpMethod = "POST", notes = "获取标准作业参数多项值", consumes = "application/x-www-form-urlencoded")
	public String getStandardItemArgsValue(HttpServletRequest request, 
			@RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			StandardItemArgsValue pojo = standardItemArgsValueService.getStandardItemArgsValue(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(pojo));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/delStandardItemArgsValue")
	@ApiOperation(value = "删除标准作业参数多项值", httpMethod = "POST", notes = "删除标准作业参数多项值", consumes = "application/x-www-form-urlencoded")
	public String delStandardItemArgsValue(HttpServletRequest request, 
			@RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			String state = standardItemArgsValueService.delStandardItemArgsValue(user, id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(state));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/savaStandardItemArgsValue")
	@ApiOperation(value = "新增或修改标准作业参数多项值", httpMethod = "POST", notes = "新增或修改标准作业参数多项值", consumes = "application/x-www-form-urlencoded")
	public String savaStandardItemArgsValue(HttpServletRequest request, 
			@RequestBody @ApiParam(name = "新增或修改标准作业参数多项值参数", value = "传入json格式", required = true) StandardItemArgsValueReq req) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			ResultBean<String> savaStandardItemArgsValue = standardItemArgsValueService.savaStandardItemArgsValue(user, req);
			return JsonMapper.nonDefaultMapper().toJson(savaStandardItemArgsValue);
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/findBystandardsId")
	@ApiOperation(value = "获取标准的采收类型", httpMethod = "POST", notes = "获取标准的采收类型", consumes = "application/x-www-form-urlencoded")
	public String findBystandardsId(HttpServletRequest request, 
			@RequestParam String standardsId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			List<Object> pojo = standardItemArgsValueService.findBystandardsId(standardsId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(pojo));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

}
