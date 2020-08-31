package com.surekam.modules.api.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.product.entity.ProductTreeList;
import com.surekam.modules.agro.product.entity.vo.ProductLibraryTreeVo;
import com.surekam.modules.api.dto.resp.ProductionBatchResp;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.api.service.SpeciesManageApiService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.ProductRelationVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 种类管理接口
 * 
 * @author tangjun
 *
 */

@Api
@Controller
@RequestMapping(value = "api/speciesManage")
public class SpeciesManageApiController extends BaseController {

	@Autowired
	private SpeciesManageApiService speciesManageApiService;
	
	@Autowired
	private ApiUserService apiUserService;

	@ResponseBody
	@RequestMapping(value = "/getProductLibraryTreeList")
	@ApiOperation(value = "获取批次信息", httpMethod = "POST", notes = "获取批次信息", consumes = "application/x-www-form-urlencoded")
	public String getProductLibraryTreeList(HttpServletRequest request, 
			@RequestParam(required = false) String baseId,
			@RequestParam(required = false) String itemId, 
			@RequestParam(required = false) boolean onlyNext) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			List<ProductLibraryTreeVo> productLibraryTreeList = speciesManageApiService.getProductLibraryTreeList(baseId, itemId, onlyNext);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(productLibraryTreeList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getProductLibraryTreeList_mobile")
	@ApiOperation(value = "获取种类品种信息", httpMethod = "POST", notes = "获取种类品种信息", consumes = "application/x-www-form-urlencoded")
	public String getProductLibraryTreeList_mobile(HttpServletRequest request, 
			@RequestParam(required = false) String baseId,
			@RequestParam(required = false) String itemId, 
			@RequestParam(required = false) boolean onlyNext) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			List<ProductLibraryTreeVo> productLibraryTreeList = speciesManageApiService.getProductLibraryTreeList(baseId, itemId, onlyNext);
			for (ProductLibraryTreeVo productLibraryTreeVo : productLibraryTreeList) {
				List<ProductLibraryTreeVo> firstList = productLibraryTreeVo.getChildren();
				for (ProductLibraryTreeVo productLibraryTreeVo2 : firstList) {
					List<ProductLibraryTreeVo> secordList = productLibraryTreeVo2.getChildren();
					for(ProductLibraryTreeVo productLibraryTreeVo3 : secordList){
						String zlId = productLibraryTreeVo3.getId();
						List<ProductionBatchResp> findByIdList = speciesManageApiService.findById(zlId);
						List<ProductLibraryTreeVo> list = new ArrayList<ProductLibraryTreeVo>();
						for (ProductionBatchResp productionBatchResp : findByIdList) {
							ProductLibraryTreeVo productLibrary = new ProductLibraryTreeVo();
							productLibrary.setId(productionBatchResp.getId());
							productLibrary.setLabel(productionBatchResp.getProductCategoryName());
							list.add(productLibrary);
						}
						productLibraryTreeVo3.setChildren(list);
					}
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(productLibraryTreeList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/findById")
	@ApiOperation(value = "根据ID查询", httpMethod = "POST", notes = "根据ID查询", consumes = "application/x-www-form-urlencoded")
	public String findById(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			List<ProductionBatchResp> findByIdList = speciesManageApiService.findById(id);
			return JsonMapper.allPropertyMapper().toJson(ResultUtil.success(findByIdList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getSpeciesManageList")
	@ApiOperation(value = "根据ID查询", httpMethod = "POST", notes = "根据ID查询", consumes = "application/x-www-form-urlencoded")
	public String getSpeciesManageList(HttpServletRequest request) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			List<ProductLibraryTreeVo> findByIdList = speciesManageApiService.getSpeciesManageList();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(findByIdList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getProductLibraryTreeList_mobileTwo")
	@ApiOperation(value = "获取种类品种信息", httpMethod = "POST", notes = "获取种类品种信息", consumes = "application/x-www-form-urlencoded")
	public String getProductLibraryTreeList_mobileTwo(HttpServletRequest request, 
			@RequestParam(required = false) String baseId,
			@RequestParam(required = false) String itemId, 
			@RequestParam(required = false) boolean onlyNext) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			List<ProductLibraryTreeVo> productLibraryTreeList = speciesManageApiService.getProductLibraryTreeList(baseId, itemId, onlyNext);
			for (ProductLibraryTreeVo productLibraryTreeVo : productLibraryTreeList) {
				List<ProductLibraryTreeVo> firstList = productLibraryTreeVo.getChildren();
				for (ProductLibraryTreeVo productLibraryTreeVo2 : firstList) {
					List<ProductLibraryTreeVo> secordList = speciesManageApiService.getlist(productLibraryTreeVo2.getId()); //鱼
					for(ProductLibraryTreeVo productLibraryTreeVo3 : secordList){
						String zlId = productLibraryTreeVo3.getId();
						if(productLibraryTreeVo3.getChildrenCount() == null) {
							List<ProductionBatchResp> findByIdList = speciesManageApiService.findById(zlId);
							List<ProductLibraryTreeVo> list = new ArrayList<ProductLibraryTreeVo>();
							for (ProductionBatchResp productionBatchResp : findByIdList) {
								ProductLibraryTreeVo productLibrary = new ProductLibraryTreeVo();
								productLibrary.setId(productionBatchResp.getId());
								productLibrary.setLabel(productionBatchResp.getProductCategoryName());
								list.add(productLibrary);
							}
							productLibraryTreeVo3.setChildren(list);
						}
					}
					productLibraryTreeVo2.setChildren(secordList);
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(productLibraryTreeList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/findOfficeAndBbaseIdById")
	@ApiOperation(value = "根据公司或基地来查询品种", httpMethod = "POST", notes = "根据公司或基地来查询品种", consumes = "application/x-www-form-urlencoded")
	public String findOfficeAndBbaseIdById(HttpServletRequest request,	@RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			List<ProductionBatchResp> findByIdList = speciesManageApiService.findOfficeAndBbaseIdById(user, id);
			return JsonMapper.allPropertyMapper().toJson(ResultUtil.success(findByIdList));
		} catch (Exception e) {
			logger.info("根据公司或基地来查询品种{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getOfficeProductLibraryTreeList")
	@ApiOperation(value = "获取树状下级异常", httpMethod = "POST", notes = "根据公司查询品种树列表", consumes = "application/x-www-form-urlencoded")
	public String getOfficeProductLibraryTreeList(HttpServletRequest request, @RequestParam(required = false) String baseId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			List<ProductLibraryTreeVo> productLibraryTreeList = speciesManageApiService.getOfficeProductLibraryTreeList(baseId, user.getOffice().getId());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(productLibraryTreeList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getOfficeProductList")
	@ApiOperation(value = "根据公司查询品种库", httpMethod = "POST", notes = "根据公司查询品种库", consumes = "application/x-www-form-urlencoded")
	public String getOfficeProductList(HttpServletRequest request,
			@RequestParam(required = false) String officeId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			Map<String, Object> map = new HashMap<String, Object>();
			Set<String> productTreeList = speciesManageApiService.getofficeProductIdList(officeId);
			map.put("productOfficeList", productTreeList);
			map.put("productList", speciesManageApiService.getofficeProductLibraryTreeList("1", officeId));
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/saveProductRelation")
	@ApiOperation(value = "保存公司品种库", httpMethod = "POST", notes = "保存公司品种库", consumes = "application/x-www-form-urlencoded")
	public String saveProductRelation(HttpServletRequest request,
			@RequestBody @ApiParam(name = "保存公司品种库", value = "传入json格式", required = true) ProductRelationVo productRelationVo) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			boolean falg = speciesManageApiService.saveProductRelation(productRelationVo.getProductIds(), productRelationVo.getOfficeId(), productRelationVo.getType(), user);
			if (falg){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.OPERATION_FAILED.getCode(), ResultEnum.OPERATION_FAILED.getMessage()));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
}
