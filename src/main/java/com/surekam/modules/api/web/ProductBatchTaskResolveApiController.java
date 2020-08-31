package com.surekam.modules.api.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.productbatchtaskresolve.service.ProductBatchTaskResolveService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 分解任务表Controller
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Api
@Controller
@RequestMapping(value = "api/productBatchTaskResolve")
public class ProductBatchTaskResolveApiController extends BaseController {

	@Autowired
	private ProductBatchTaskResolveService productBatchTaskResolveService;

	@ResponseBody
	@RequestMapping(value = "/updConfirmStates")
	@ApiOperation(value = "修改确认状态", httpMethod = "POST", notes = "修改确认状态", consumes = "application/x-www-form-urlencoded")
	public String updConfirmStates(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			String state = productBatchTaskResolveService.updConfirmStates(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(state));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
}
