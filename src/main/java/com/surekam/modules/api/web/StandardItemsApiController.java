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
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.standarditems.service.StandardItemsService;
import com.surekam.modules.api.dto.req.StandardItemsReq;
import com.surekam.modules.api.dto.resp.SavaStandardItemsInfoResp;
import com.surekam.modules.api.dto.resp.StandardItemsResp;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 标准作业项表Controller
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Api
@Controller
@RequestMapping(value = "api/standardItems")
public class StandardItemsApiController extends BaseController {

	@Autowired
	private StandardItemsService standardItemsService;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@ResponseBody
	@RequestMapping(value = "/savaStandardItems")
	@ApiOperation(value = "新增或修改作业项信息", httpMethod = "POST", notes = "新增或修改作业项信息", consumes = "application/x-www-form-urlencoded")
	public String savaStandardItems(HttpServletRequest request,
			@RequestBody @ApiParam(name = "新增或修改作业项参数", value = "传入json格式", required = true) StandardItemsReq req) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			ResultBean<String> savaStandardItems = standardItemsService.savaStandardItems(req, user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(savaStandardItems));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/delStandardItems")
	@ApiOperation(value = "删除作业项信息", httpMethod = "POST", notes = "删除作业项信息", consumes = "application/x-www-form-urlencoded")
	public String delStandardItems(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			String delStandardItems = standardItemsService.delStandardItems(id, user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(delStandardItems));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getStandardItems")
	@ApiOperation(value = "修改时获取作业项信息", httpMethod = "POST", notes = "修改时获取作业项信息", consumes = "application/x-www-form-urlencoded")
	public String getStandardItems(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			StandardItemsResp resp = standardItemsService.getStandardItems(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(resp));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getStandardItemsSavaInfo")
	@ApiOperation(value = "新增作业项时获取相关信息", httpMethod = "POST", notes = "新增作业项时获取相关信息", consumes = "application/x-www-form-urlencoded")
	public String getStandardItemsBeginDate(HttpServletRequest request,
			@RequestParam(required = false) String productGrowthCycleId,
			@RequestParam(required = false) String standardItemsId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			SavaStandardItemsInfoResp standardItemsSavaInfo = standardItemsService.getStandardItemsSavaInfo(productGrowthCycleId, standardItemsId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(standardItemsSavaInfo));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	} 
	
}
