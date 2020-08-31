package com.surekam.modules.api.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.materials.entity.MaterialAnimalHealthProducts;
import com.surekam.modules.agro.materials.entity.MaterialFeed;
import com.surekam.modules.agro.materials.entity.MaterialFertilizer;
import com.surekam.modules.agro.materials.entity.MaterialPesticide;
import com.surekam.modules.api.dto.req.MaterialAnimalHealthProductsReq;
import com.surekam.modules.api.dto.req.MaterialFeedReq;
import com.surekam.modules.api.dto.req.MaterialFertilizerReq;
import com.surekam.modules.api.dto.req.MaterialPesticideReq;
import com.surekam.modules.api.dto.resp.MaterialAnimalHealthProductsResp;
import com.surekam.modules.api.dto.resp.MaterialFeedResp;
import com.surekam.modules.api.dto.resp.MaterialFertilizerResp;
import com.surekam.modules.api.dto.resp.MaterialPesticideResp;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.api.service.MaterialsApiService;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 农业农资接口
 * 
 * @author dell
 *
 */

@Api
@Controller
@RequestMapping(value = "api/materials")
public class MaterialsApiController extends BaseController {

	@Autowired
	private MaterialsApiService materialsApiService;

	@Autowired
	private ApiUserService apiUserService;
	

	@ResponseBody
	@RequestMapping(value = "/getMaterialAnimalHealthProductsList")
	@ApiOperation(value = "获取动保产品列表", httpMethod = "POST", notes = "获取动保产品列表", consumes = "application/x-www-form-urlencoded")
	public String getMaterialAnimalHealthProductsList(HttpServletRequest request, 
			@RequestParam Integer pageno,
			@RequestParam Integer pagesize) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			Page<MaterialAnimalHealthProducts> page = materialsApiService.getMaterialAnimalHealthProductsList(user, pageno, pagesize);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteMaterialAnimalHealthProducts")
	@ApiOperation(value = "删除动保产品列表", httpMethod = "POST", notes = "删除动保产品列表", consumes = "application/x-www-form-urlencoded")
	public String deleteMaterialAnimalHealthProducts(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			String operType = materialsApiService.deleteMaterialAnimalHealthProducts(user, id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(operType));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/savaMaterialAnimalHealthProducts")
	@ApiOperation(value = "新增或修改动保产品", httpMethod = "POST", notes = "新增或修改动保产品", consumes = "application/x-www-form-urlencoded")
	public String savaMaterialAnimalHealthProducts(HttpServletRequest request,
			@RequestBody @ApiParam(name = "动保产品参数", value = "传入json格式", required = true) MaterialAnimalHealthProductsReq req) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			String operType = materialsApiService.savaMaterialAnimalHealthProducts(user, req);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(operType));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getMaterialAnimalHealthProducts")
	@ApiOperation(value = "获取动保产品信息", httpMethod = "POST", notes = "获取动保产品信息", consumes = "application/x-www-form-urlencoded")
	public String getMaterialAnimalHealthProducts(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			MaterialAnimalHealthProductsResp resp = materialsApiService.getMaterialAnimalHealthProducts(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(resp));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getMaterialPesticideList")
	@ApiOperation(value = "获取农药列表", httpMethod = "POST", notes = "获取农药列表", consumes = "application/x-www-form-urlencoded")
	public String getMaterialPesticideList(HttpServletRequest request, 
			@RequestParam Integer pageno,
			@RequestParam Integer pagesize) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			Page<MaterialPesticide> page = materialsApiService.getMaterialPesticideList(user, pageno, pagesize);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteMaterialPesticide")
	@ApiOperation(value = "删除农药", httpMethod = "POST", notes = "删除农药", consumes = "application/x-www-form-urlencoded")
	public String deleteMaterialPesticide(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			User user = apiUserService.getUserByToken(token);
			
			String operType = materialsApiService.deleteMaterialPesticide(user, id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(operType));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/savaMaterialPesticide")
	@ApiOperation(value = "新增或修改农药", httpMethod = "POST", notes = "新增或修改农药", consumes = "application/x-www-form-urlencoded")
	public String savaMaterialPesticide(HttpServletRequest request,
			@RequestBody @ApiParam(name = "农药参数", value = "传入json格式", required = true) MaterialPesticideReq req) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			String operType = materialsApiService.savaMaterialPesticide(user, req);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(operType));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getMaterialPesticide")
	@ApiOperation(value = "获取农药信息", httpMethod = "POST", notes = "获取农药信息", consumes = "application/x-www-form-urlencoded")
	public String getMaterialPesticide(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			MaterialPesticideResp resp = materialsApiService.getMaterialPesticide(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(resp));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "/getMaterialFeedList")
	@ApiOperation(value = "获取饲料列表", httpMethod = "POST", notes = "获取饲料列表", consumes = "application/x-www-form-urlencoded")
	public String getMaterialFeedList(HttpServletRequest request, 
			@RequestParam Integer pageno,
			@RequestParam Integer pagesize) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			Page<MaterialFeed> page = materialsApiService.getMaterialFeedList(user, pageno, pagesize);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteMaterialFeed")
	@ApiOperation(value = "删除饲料", httpMethod = "POST", notes = "删除饲料", consumes = "application/x-www-form-urlencoded")
	public String deleteMaterialFeed(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			String operType = materialsApiService.deleteMaterialFeed(user, id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(operType));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/saveMaterialFeed")
	@ApiOperation(value = "新增或修改饲料", httpMethod = "POST", notes = "新增或修改饲料", consumes = "application/x-www-form-urlencoded")
	public String saveMaterialFeed(HttpServletRequest request,
			@RequestBody @ApiParam(name = "饲料参数", value = "传入json格式", required = true) MaterialFeedReq req) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			String operType = materialsApiService.savaMaterialFeed(user, req);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(operType));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getMaterialFeed")
	@ApiOperation(value = "获取饲料信息", httpMethod = "POST", notes = "获取饲料信息", consumes = "application/x-www-form-urlencoded")
	public String getMaterialFeed(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			MaterialFeedResp resp = materialsApiService.getMaterialFeed(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(resp));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "/getMaterialFertilizerList")
	@ApiOperation(value = "获取化肥列表", httpMethod = "POST", notes = "获取化肥列表", consumes = "application/x-www-form-urlencoded")
	public String getMaterialFertilizerList(HttpServletRequest request, 
			@RequestParam Integer pageno,
			@RequestParam Integer pagesize) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			Page<MaterialFertilizer> page = materialsApiService.getMaterialFertilizerList(user, pageno, pagesize);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "/deleteMaterialFertilizer")
	@ApiOperation(value = "删除化肥", httpMethod = "POST", notes = "删除饲料", consumes = "application/x-www-form-urlencoded")
	public String deleteMaterialFertilizer(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			String operType = materialsApiService.deleteMaterialFertilizer(user, id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(operType));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/savaMaterialFertilizer")
	@ApiOperation(value = "新增或修改化肥", httpMethod = "POST", notes = "新增或修改化肥", consumes = "application/x-www-form-urlencoded")
	public String savaMaterialFertilizer(HttpServletRequest request,
			@RequestBody @ApiParam(name = "饲料参数", value = "传入json格式", required = true) MaterialFertilizerReq req) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			String operType = materialsApiService.savaMaterialFertilizer(user, req);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(operType));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getMaterialFertilizer")
	@ApiOperation(value = "获取化肥信息", httpMethod = "POST", notes = "获取化肥信息", consumes = "application/x-www-form-urlencoded")
	public String getMaterialFertilizer(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			MaterialFertilizerResp resp = materialsApiService.getMaterialFertilizer(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(resp));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
}
