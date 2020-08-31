package com.surekam.modules.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.product.entity.ProductLibraryDetail;
import com.surekam.modules.agro.product.service.ProductLibraryDetailService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 品种库Controller
 * @author lb
 * @version 2019-04-10
 */
@Api
@Controller
@RequestMapping(value = "api/productLibraryDetail")
public class ProductLibraryDetailController extends BaseController {

	@Autowired
	private ProductLibraryDetailService productLibraryDetailService;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@RequestMapping(value = "form", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "品种详情", notes = "品种详情", consumes = "application/json")
	public String form(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = true) String libraryId, Model model) {
		response.setContentType("application/json; charset=UTF-8");
		try {
			ProductLibraryDetail pojo = productLibraryDetailService.getLibraryId(libraryId);
			return JsonMapper.allPropertyMapper().toJson(ResultUtil.success(pojo));
		} catch (Exception e) {
			logger.error("品种类型列表查询错误：" + e);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.PRODUCT_CATEGORY_ERROR.getCode(), ResultEnum.PRODUCT_CATEGORY_ERROR.getMessage()));
		}
	}
	
	@RequestMapping(value = "/save")
	@ResponseBody
	@ApiOperation(value = "保存品种", httpMethod = "POST", notes = "品种", consumes = "application/x-www-form-urlencoded")
	public String save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody @ApiParam(name = "保存品种", value = "传入json格式", required = true) ProductLibraryDetail productlibrary) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			boolean falg = productLibraryDetailService.save(productlibrary, user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(falg));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.PRODUCT_CATEGORY_ERROR.getCode(), ResultEnum.PRODUCT_CATEGORY_ERROR.getMessage()));
		}
	}
	
	@RequestMapping(value = "/defaultProductionMode")
	@ResponseBody
	@ApiOperation(value = "生成默认的生产模式", httpMethod = "GET", notes = "生成默认的生产模式", consumes = "application/x-www-form-urlencoded")
	public String defaultProductionMode(HttpServletRequest request, HttpServletResponse response) {
		try {
			String token = request.getHeader("X-Token");
			User user = apiUserService.getUserByToken(token);
			productLibraryDetailService.defaultProductionMode(user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.PRODUCT_CATEGORY_ERROR.getCode(), ResultEnum.PRODUCT_CATEGORY_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		productLibraryDetailService.delete(id);
		addMessage(redirectAttributes, "删除品种库成功");
		return "redirect:" + Global.getAdminPath() + "/product/productlibrary/?repage";
	}
}
