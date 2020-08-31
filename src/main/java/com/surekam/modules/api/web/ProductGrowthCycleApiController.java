package com.surekam.modules.api.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.productgrowthcycle.service.ProductGrowthCycleService;
import com.surekam.modules.api.dto.req.ProductGrowthCycleReq;
import com.surekam.modules.api.dto.resp.ProductGrowthCycleResp;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 生长周期API
 * 
 * @author tangjun
 * @version 2019-04-23
 */
@Api
@Controller
@RequestMapping(value = "api/productGrowthCycle")
public class ProductGrowthCycleApiController {

	@Autowired
	private ProductGrowthCycleService productGrowthCycleService;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@ResponseBody
	@RequestMapping(value = "/savaProductGrowthCycle")
	@ApiOperation(value = "新增或修改生长周期", httpMethod = "POST", notes = "新增或修改生长周期", consumes = "application/x-www-form-urlencoded")
	public String savaProductGrowthCycle(HttpServletRequest request,
			@RequestBody @ApiParam(name = "新增或修改生长周期参数", value = "传入json格式", required = true) ProductGrowthCycleReq req) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			ResultBean<String> state = productGrowthCycleService.savaProductGrowthCycle(req, user);
			return JsonMapper.nonDefaultMapper().toJson(state);
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/delProductGrowthCycle")
	@ApiOperation(value = "获取标准库树状结构", httpMethod = "POST", notes = "获取标准库树状结构", consumes = "application/x-www-form-urlencoded")
	public String delProductGrowthCycle(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			ResultBean<String> delProductGrowthCycle = productGrowthCycleService.delProductGrowthCycle(user, id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(delProductGrowthCycle));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getBeginDate")
	@ApiOperation(value = "获取标准库树状结构", httpMethod = "POST", notes = "获取标准库树状结构", consumes = "application/x-www-form-urlencoded")
	public String getBeginDate(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			String state = productGrowthCycleService.getBeginDate(user, id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(state));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getProductGrowthCycle")
	@ApiOperation(value = "修改时获取生长周期对象信息", httpMethod = "POST", notes = "修改时获取生长周期对象信息", consumes = "application/x-www-form-urlencoded")
	public String getProductGrowthCycle(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			ProductGrowthCycleResp productGrowthCycleResp = productGrowthCycleService.getProductGrowthCycleResp(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(productGrowthCycleResp));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
}
