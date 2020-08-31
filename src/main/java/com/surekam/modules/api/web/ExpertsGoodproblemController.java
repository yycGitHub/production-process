package com.surekam.modules.api.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.experts.entity.ExpertsGoodproblem;
import com.surekam.modules.agro.experts.service.ExpertsGoodproblemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 专家擅长问题Controller
 * 
 * @author xy
 * @version 2019-04-16
 */
@Api
@Controller
@RequestMapping(value = "api/expertsGoodproblem")
public class ExpertsGoodproblemController extends BaseController {

	@Autowired
	private ExpertsGoodproblemService expertsGoodproblemService;

	@ResponseBody
	@RequestMapping(value = "/getExpertsGoodproblemList")
	@ApiOperation(value = "启用禁用专家用户", httpMethod = "POST", notes = "启用禁用专家用户", consumes = "application/x-www-form-urlencoded")
	public String getExpertsGoodproblemList(HttpServletRequest request) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			List<ExpertsGoodproblem> list = expertsGoodproblemService.getList();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}

	}

	@ResponseBody
	@RequestMapping(value = "/expertsGoodAtProblemsList")
	@ApiOperation(value = "获取到专家所有的擅长问题", httpMethod = "POST", notes = "启用禁用专家用户", consumes = "application/x-www-form-urlencoded")
	public String expertsGoodAtProblemsList(HttpServletRequest request) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			List<String> expertsGoodAtProblemsList = expertsGoodproblemService.getExpertsGoodAtProblemsList();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(expertsGoodAtProblemsList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}

	}
	
	/**
	 * app新增保存专家擅长问题
	 * @param request
	 * @param response
	 * @param content
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/savaGoodAtProblems")
	@ApiOperation(value = "新增保存专家擅长问题", httpMethod = "POST", notes = "新增保存专家擅长问题", consumes = "application/x-www-form-urlencoded")
	public String savaGoodAtProblems(HttpServletRequest request, HttpServletResponse response, String content,String  userId) {
		String token = request.getHeader("X-Token");
		if (token != null) {
			try {
				expertsGoodproblemService.savaGoodAtProblems(userId,content);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
			} catch (Exception e) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		
	}
	
	/**
	 * app编辑保存专家擅长问题
	 * @param request
	 * @param response
	 * @param content
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editSavaGoodAtProblems")
	@ApiOperation(value = "编辑保存专家擅长问题", httpMethod = "POST", notes = "编辑保存专家擅长问题", consumes = "application/x-www-form-urlencoded")
	public String editSavaGoodAtProblems(HttpServletRequest request, HttpServletResponse response, String content,String  id) {
		String token = request.getHeader("X-Token");
		if (token != null) {
			try {
				expertsGoodproblemService.editSavaGoodAtProblems(id,content);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
			} catch (Exception e) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		
	}
	
	/**
	 * app删除专家擅长问题
	 * @param request
	 * @param response
	 * @param content
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delGoodAtProblems")
	@ApiOperation(value = " 删除专家擅长问题", httpMethod = "POST", notes = " 删除专家擅长问题", consumes = "application/x-www-form-urlencoded")
	public String delGoodAtProblems(HttpServletRequest request, HttpServletResponse response,String  id) {
		String token = request.getHeader("X-Token");
		if (token != null) {
			try {
				expertsGoodproblemService.delGoodAtProblems(id);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
			} catch (Exception e) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		
	}
	
	
	/**
	 * app专家擅长问题列表
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getListExpertsGoodproblem", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "专家擅长问题列表", httpMethod = "GET", notes = "专家擅长问题列表", consumes = "application/x-www-form-urlencoded")
	public String getListExpertsGoodproblem(HttpServletRequest request, HttpServletResponse response,String userId) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			List<ExpertsGoodproblem> list  = new ArrayList<ExpertsGoodproblem>();
			list  = expertsGoodproblemService.getListExpertsGoodproblem(userId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} else {
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
}
