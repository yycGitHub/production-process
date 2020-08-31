package com.surekam.modules.api.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.surekam.modules.agro.productionmodel.entity.ProductionModel;
import com.surekam.modules.agro.productionmodel.service.ProductionModelService;
import com.surekam.modules.api.dto.req.ProductionModelReq;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 生产模式ApiController
 * 
 * @author tangjun
 * @version 2019-05-27
 */
@Api
@Controller
@RequestMapping(value = "api/productionModel")
public class ProductionModelApiController extends BaseController {

	@Autowired
	private ProductionModelService productionModelService;

	@Autowired
	private ApiUserService apiUserService;

	@ResponseBody
	@RequestMapping(value = "/savaProductionModel")
	@ApiOperation(value = "新增或修改生产模式", httpMethod = "POST", notes = "新增或修改生产模式", consumes = "application/x-www-form-urlencoded")
	public String savaProductionModel(HttpServletRequest request,
			@RequestBody @ApiParam(name = "新增或修改生产模式请求参数", value = "传入json格式", required = true) ProductionModelReq req) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			ResultBean<String> state = productionModelService.savaProductionModel(req, user);
			return JsonMapper.nonDefaultMapper().toJson(state);
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getProductionModelList")
	@ApiOperation(value = "获取品种下的生产模式", httpMethod = "POST", notes = "获取品种下的生产模式", consumes = "application/x-www-form-urlencoded")
	public String findBySensorSetupList(HttpServletRequest request, @RequestParam String productId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			List<ProductionModel> list = productionModelService.getProductionModelList(productId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@RequestMapping(value = "/delete")
	@ResponseBody
	@ApiOperation(value = "删除模式", httpMethod = "POST", notes = "删除模式", consumes = "application/x-www-form-urlencoded")
	public String delete(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = true) String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			boolean falg = productionModelService.delete(id, user);
			if(!falg){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.OPERATION_FAILED.getCode(), ResultEnum.OPERATION_FAILED.getMessage()));
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.PRODUCT_CATEGORY_ERROR.getCode(), ResultEnum.PRODUCT_CATEGORY_ERROR.getMessage()));
		}
	}
}
