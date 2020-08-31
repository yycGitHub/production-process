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
import com.surekam.modules.agro.systementerprisestandards.entity.SystemEnterpriseStandards;
import com.surekam.modules.api.dto.resp.SystemEnterpriseStandardsResp;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.api.service.StandardLibraryApiService;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 标准库API
 * 
 * @author tangjun
 * @version 2019-04-23
 */
@Api
@Controller
@RequestMapping(value = "api/standardLibrary")
public class StandardLibraryApiController extends BaseController {

	@Autowired
	private StandardLibraryApiService standardLibraryApiService;

	@Autowired
	private ApiUserService apiUserService;

	@ResponseBody
	@RequestMapping(value = "/savaSystemEnterpriseStandards")
	@ApiOperation(value = "新增或修改标准信息", httpMethod = "POST", notes = "新增或修改标准信息", consumes = "application/x-www-form-urlencoded")
	public String savaSystemEnterpriseStandards(HttpServletRequest request,
			@RequestBody @ApiParam(name = "新增或修改标准信息", value = "传入json格式", required = true) SystemEnterpriseStandards sesReq) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			ResultBean<String> savaSystemEnterpriseStandards = standardLibraryApiService.savaSystemEnterpriseStandards(sesReq, user);
			return JsonMapper.nonDefaultMapper().toJson(savaSystemEnterpriseStandards);
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delSystemEnterpriseStandards")
	@ApiOperation(value = "删除标准信息", httpMethod = "POST", notes = "删除标准信息", consumes = "application/x-www-form-urlencoded")
	public String delSystemEnterpriseStandards(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}

			User user = apiUserService.getUserByToken(token);
			ResultBean<String> delSystemEnterpriseStandards = standardLibraryApiService.delSystemEnterpriseStandards(id, user);
			return JsonMapper.nonDefaultMapper().toJson(delSystemEnterpriseStandards);
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getStandardLibraryTreeList")
	@ApiOperation(value = "获取标准库树状结构", httpMethod = "POST", notes = "获取标准库树状结构", consumes = "application/x-www-form-urlencoded")
	public String getStandardLibraryTreeList(HttpServletRequest request, @RequestParam String standardId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}

			User user = apiUserService.getUserByToken(token);
			List<SystemEnterpriseStandardsResp> standardLibraryList = standardLibraryApiService.getStandardLibraryTreeList(user, standardId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(standardLibraryList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getStandardLibraryList")
	@ApiOperation(value = "获取标准库分页信息", httpMethod = "POST", notes = "获取标准库分页信息", consumes = "application/x-www-form-urlencoded")
	public String getStandardLibraryList(HttpServletRequest request, @RequestParam Integer pageno,
			@RequestParam Integer pagesize, @RequestParam(required = false) String productId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}

			User user = apiUserService.getUserByToken(token);
			Page<SystemEnterpriseStandards> page = standardLibraryApiService.getStandardLibraryList(user, pageno, pagesize, productId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getStandardLibrary")
	@ApiOperation(value = "获取标准库信息", httpMethod = "POST", notes = "获取标准库信息", consumes = "application/x-www-form-urlencoded")
	public String getStandardLibrary(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}

			SystemEnterpriseStandards sesPojo = standardLibraryApiService.getStandardLibrary(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(sesPojo));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getVersion")
	@ApiOperation(value = "获取标准库版本", httpMethod = "POST", notes = "获取标准库版本", consumes = "application/x-www-form-urlencoded")
	public String getVersion(HttpServletRequest request, @RequestParam String productId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}

			User user = apiUserService.getUserByToken(token);
			List<Map<String, String>> sesList = standardLibraryApiService.getVersion(productId, user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(sesList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getVersionName")
	@ApiOperation(value = "获取版本信息", httpMethod = "POST", notes = "获取版本信息", consumes = "application/x-www-form-urlencoded")
	public String getVersionName(HttpServletRequest request, @RequestParam String standardName) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			String getVersionName = standardLibraryApiService.getVersionName(standardName);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(getVersionName));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getStandardList")
	@ApiOperation(value = "获取标准库分页信息", httpMethod = "POST", notes = "获取标准库分页信息", consumes = "application/x-www-form-urlencoded")
	public String getStandardLibraryList(HttpServletRequest request) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}

			User user = apiUserService.getUserByToken(token);
			List<SystemEnterpriseStandards> list = standardLibraryApiService.getStandardList(user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
}
