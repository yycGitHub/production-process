package com.surekam.modules.api.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.standarditemcategory.service.StandardItemCategoryService;
import com.surekam.modules.api.dto.resp.StandardItemCategoryResp;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 作业项类别表(包括 施肥 投料等 )Controller
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Api
@Controller
@RequestMapping(value = "api/standardItemCategory")
public class StandardItemCategoryApiController extends BaseController {

	@Autowired
	private StandardItemCategoryService standardItemCategoryService;

	@Autowired
	private ApiUserService apiUserService;
	
	@ResponseBody
	@RequestMapping(value = "/getStandardItemCategoryList")
	@ApiOperation(value = "获取作业项类别", httpMethod = "POST", notes = "获取作业项类别", consumes = "application/x-www-form-urlencoded")
	public String getStandardItemCategoryList(HttpServletRequest request) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			List<StandardItemCategoryResp> standardItemCategoryList = standardItemCategoryService.getStandardItemCategoryList();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(standardItemCategoryList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/savaStandardItemCategory")
	@ApiOperation(value = "保存作业项类别", httpMethod = "POST", notes = "保存作业项类别", consumes = "application/x-www-form-urlencoded")
	public String savaStandardItemCategoryService(HttpServletRequest request,
			@RequestParam String taskItemCategoryName) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			ResultBean<String> savaStandardItemCategory = standardItemCategoryService.savaStandardItemCategory(user, taskItemCategoryName);
			return JsonMapper.nonDefaultMapper().toJson(savaStandardItemCategory);
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

}
