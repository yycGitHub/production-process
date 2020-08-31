package com.surekam.modules.api.web;

import java.util.List;
import java.util.Map;

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
import com.surekam.modules.agro.standarditemargs.entity.StandardItemArgs;
import com.surekam.modules.agro.standarditemargs.service.StandardItemArgsService;
import com.surekam.modules.api.dto.req.StandardItemArgsReq;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * Title: StandardItemArgsController Description: 标准作业详细参数表API
 * 
 * @author tangjun
 * @date 2019年5月5日
 */
@Api
@Controller
@RequestMapping(value = "api/standardItemArgs")
public class StandardItemArgsApiController extends BaseController {

	@Autowired
	private StandardItemArgsService standardItemArgsService;

	@Autowired
	private ApiUserService apiUserService;

	@ResponseBody
	@RequestMapping(value = "/getStandardItemArgsList")
	@ApiOperation(value = "获取标准库分页信息", httpMethod = "POST", notes = "获取标准库分页信息", consumes = "application/x-www-form-urlencoded")
	public String getStandardItemArgsList(HttpServletRequest request, 
			@RequestParam Integer pageno,
			@RequestParam Integer pagesize, 
			@RequestParam String itemsId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			Page<StandardItemArgs> page = standardItemArgsService.getStandardItemArgsList(user, pageno, pagesize, itemsId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getDeWeighting")
	@ApiOperation(value = "获取复制参数列表", httpMethod = "POST", notes = "获取复制参数列表", consumes = "application/x-www-form-urlencoded")
	public String getDeWeighting(HttpServletRequest request) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			List<Map<String, Object>> deWeighting = standardItemArgsService.getDeWeighting();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(deWeighting));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delStandardItemArgs")
	@ApiOperation(value = "删除参数列表", httpMethod = "POST", notes = "删除参数列表", consumes = "application/x-www-form-urlencoded")
	public String delStandardItemArgs(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			String state = standardItemArgsService.delStandardItemArgs(user, id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(state));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getStandardItemArgs")
	@ApiOperation(value = "获取参数信息", httpMethod = "POST", notes = "获取参数信息", consumes = "application/x-www-form-urlencoded")
	public String getStandardItemArgs(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			StandardItemArgs standardItemArgs = standardItemArgsService.getStandardItemArgs(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(standardItemArgs));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/savaStandardItemArgs")
	@ApiOperation(value = "新增或修改参数信息", httpMethod = "POST", notes = "新增或修改参数信息", consumes = "application/x-www-form-urlencoded")
	public String savaStandardItemArgs(HttpServletRequest request, 
			@RequestBody @ApiParam(name = "新增或修改参数", value = "传入json格式", required = true) StandardItemArgsReq req) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			ResultBean<String> savaStandardItemArgs = standardItemArgsService.savaStandardItemArgs(user, req);
			return JsonMapper.nonDefaultMapper().toJson(savaStandardItemArgs);
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getStandardItemArgsSort")
	@ApiOperation(value = "获取标准库分页信息排序", httpMethod = "POST", notes = "获取标准库分页信息排序", consumes = "application/x-www-form-urlencoded")
	public String getStandardItemArgsSort(HttpServletRequest request, 
			@RequestParam String itemsId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			int sort = standardItemArgsService.getStandardItemArgsSort(itemsId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(sort));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
}
