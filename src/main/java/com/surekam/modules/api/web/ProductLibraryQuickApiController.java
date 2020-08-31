package com.surekam.modules.api.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.productlibraryquick.entity.ProductLibraryQuick;
import com.surekam.modules.agro.productlibraryquick.service.ProductLibraryQuickService;
import com.surekam.modules.agro.systementerprisestandards.entity.SystemEnterpriseStandards;
import com.surekam.modules.api.dto.resp.ProductLibraryTreeResp;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 品种快捷表Controller
 * 
 * @author tangjun
 * @version 2019-06-03
 */
@Api
@Controller
@RequestMapping(value = "api/productLibraryQuick")
public class ProductLibraryQuickApiController extends BaseController {

	@Autowired
	private ProductLibraryQuickService productLibraryQuickService;

	@Autowired
	private ApiUserService apiUserService;
	
	@ResponseBody
	@RequestMapping(value = "/findByOfficeQueryList")
	@ApiOperation(value = "查询公司下的快捷品种", httpMethod = "POST", notes = "查询公司下的快捷品种", consumes = "application/x-www-form-urlencoded")
	public String findByOfficeQueryList(HttpServletRequest request, @RequestParam String baseId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			List<ProductLibraryTreeResp> list = productLibraryQuickService.findByOfficeQueryList(baseId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getList")
	@ApiOperation(value = "获取快捷品种列表", httpMethod = "POST", notes = "获取快捷品种列表", consumes = "application/x-www-form-urlencoded")
	public String getList(HttpServletRequest request,
			@RequestParam String officeId,
			@RequestParam(required = false) Integer pageno, @RequestParam(required = false) Integer pagesize) {
		try {
			// 对分页参数进行处理
			int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
			int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
			// 获取登录用户的token
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			// 根据token获取用户信息
			User user = apiUserService.getUserByToken(token);
			Page<ProductLibraryQuick> page = new Page<ProductLibraryQuick>(pageNo, pageSize);
			page = productLibraryQuickService.find(page, officeId, user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("获取list异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/save")
	@ApiOperation(value = "新增或者修改快捷品种", httpMethod = "POST", notes = "新增或者修改快捷品种", consumes = "application/x-www-form-urlencoded")
	public String save(HttpServletRequest request,
			@RequestBody @ApiParam(name = "新增或者修改快捷品种", value = "传入json格式", required = true) ProductLibraryQuick req) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			if (productLibraryQuickService.exist(req)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success("fail"));
			}
			User user = apiUserService.getUserByToken(token);
			SystemEnterpriseStandards systemEnterpriseStandards = new SystemEnterpriseStandards();
			systemEnterpriseStandards.setId(req.getStandardsId());
			req.setSystemEnterpriseStandards(systemEnterpriseStandards);
			Office office = new Office();
			office.setId(req.getOfficeId());
			req.setOffice(office);
			ProductLibraryTree product = new ProductLibraryTree();
			product.setId(req.getProductId());
			req.setProductLibraryTree(product);
			if (StringUtils.isBlank(req.getId())) {
				req.setCreateTime(new Date());
				req.setCreateUserId(user.getId());
				req.setStates("A");
			} else {
				req.setUpdateTime(new Date());
				req.setStates("U");
				req.setUpdateUserId(user.getId());
			}
			req.setOffice(office);
			productLibraryQuickService.save(req);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	@ApiOperation(value = "删除快捷品种", httpMethod = "POST", notes = "删除快捷品种", consumes = "application/x-www-form-urlencoded")
	public String delete(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			productLibraryQuickService.delete(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getProductLibraryQuick")
	@ApiOperation(value = "根据ID查询传快捷品种信息", httpMethod = "POST", notes = "根据ID查询快捷品种信息", consumes = "application/x-www-form-urlencoded")
	public String getProductLibraryQuick(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			ProductLibraryQuick productLibraryQuick = productLibraryQuickService.get(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(productLibraryQuick));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
}
