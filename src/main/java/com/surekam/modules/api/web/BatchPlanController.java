package com.surekam.modules.api.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.surekam.common.web.BaseController;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.batchplan.entity.BatchPlan;
import com.surekam.modules.agro.batchplan.service.BatchPlanService;
import com.surekam.modules.agro.breedingplan.entity.BreedingPlan;
import com.surekam.modules.agro.breedingplan.service.BreedingPlanService;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.agro.systementerprisestandards.entity.SystemEnterpriseStandards;
import com.surekam.modules.api.dto.resp.BatchCropInfoResp;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.api.service.BatchManageApiService;
import com.surekam.modules.api.service.StandardLibraryApiService;
import com.surekam.modules.sys.entity.User;

/**
 * 批次计划表Controller
 * @author luoxw
 * @version 2019-10-16
 */
@Controller
@RequestMapping(value = "api/batchplan")
public class BatchPlanController extends BaseController {

	@Autowired
	private BatchPlanService batchPlanService;
	@Autowired
	private BreedingPlanService breedingPlanService;
	@Autowired
	private ApiUserService apiUserService;
	@Autowired
	private StandardLibraryApiService standardLibraryApiService;
	@Autowired
	private BatchManageApiService batchManageApiService;
	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;

	@ResponseBody
	@RequestMapping(value = "/getList")
	@ApiOperation(value = "获取列表", httpMethod = "POST", notes = "获取列表", consumes = "application/x-www-form-urlencoded")
	public String getList(HttpServletRequest request, 
			@RequestParam(required = false) String planId,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson( ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			// 根据token获取用户信息
			User user = apiUserService.getUserByToken(token);
			int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
			int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
			Page<BatchPlan> page = new Page<BatchPlan>(pageNo, pageSize);
			page = batchPlanService.find(page, planId, user);
			List<BatchPlan> list = page.getList();
			Map<String, List<BatchCropInfoResp>> map = new HashMap<String, List<BatchCropInfoResp>>();
			for(BatchPlan batchPlan : list){
				batchPlan.setProductName("选择品种");
				if (StringUtils.isNotBlank(batchPlan.getStandardId())) {
					SystemEnterpriseStandards standard = standardLibraryApiService.getStandardLibrary(batchPlan.getStandardId());
					batchPlan.setProductId(standard.getProductId());
					batchPlan.setProductName(productLibraryTreeService.get(standard.getProductId()).getProductCategoryName());
				}
				if (StringUtils.isNotBlank(batchPlan.getProductId())) {
					if (map.containsKey(batchPlan.getProductId())) {
						batchPlan.setStandards(map.get(batchPlan.getProductId()));
					} else {
						List<BatchCropInfoResp> standardList= batchManageApiService.getCropStandardInfoList(batchPlan.getProductId());
						map.put(batchPlan.getProductId(),standardList);
						batchPlan.setStandards(standardList);
					}
				}
				List<ProductionBatch> listBatch = batchPlanService.findListByBatchPlanId(batchPlan.getId());
				if(listBatch.size()>0){
					batchPlan.setProductionBatch(listBatch.get(0));
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("获取list异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/savaBatchPlan")
	@ApiOperation(value = "新增或者修改", httpMethod = "POST", notes = "新增或者修改", consumes = "application/x-www-form-urlencoded")
	public String savaBatchPlan(HttpServletRequest request,
			@RequestBody @ApiParam(name = "新增信息", value = "传入json格式", required = true) BatchPlan batchPlan) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			if (batchPlanService.findList(batchPlan).size()>0) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_EXIST.getCode(), "批次编号已存在"));
			}
			if (StringUtils.isBlank(batchPlan.getId())) {
				batchPlan.setCreateTime(new Date());
				batchPlan.setCreateUserId(user.getId());
				batchPlan.setStates("A");
			} else {
				batchPlan.setUpdateTime(new Date());
				batchPlan.setStates("U");
				batchPlan.setUpdateUserId(user.getId());
			}
			batchPlanService.save(batchPlan);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	@ApiOperation(value = "删除", httpMethod = "POST", notes = "删除", consumes = "application/x-www-form-urlencoded")
	public String deleteBatchManage(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			batchPlanService.delete(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getBatchPlanById")
	@ApiOperation(value = "根据ID查询信息", httpMethod = "POST", notes = "根据ID查询信息", consumes = "application/x-www-form-urlencoded")
	public String getBatchPlanById(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			BatchPlan batchPlan = batchPlanService.get(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(batchPlan));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getListByBaseId")
	@ApiOperation(value = "根据BaseIdID查询信息", httpMethod = "POST", notes = "根据BaseIdID查询信息", consumes = "application/x-www-form-urlencoded")
	public String getListByBaseId(HttpServletRequest request, @RequestParam String baseId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			List<BatchPlan> list = batchPlanService.getListByBaseId(baseId);
			List<BatchPlan> batchPlanList = new ArrayList<BatchPlan>();
			for(BatchPlan batchPlan:list) {
				List<ProductionBatch> listBatch = batchPlanService.findListByBatchPlanId(batchPlan.getId());
				if(listBatch.size()==0){
					SystemEnterpriseStandards standardLibrary =standardLibraryApiService.getStandardLibrary(batchPlan.getStandardId());
					batchPlan.setStandardName(standardLibrary.getStandardName());
					batchPlanList.add(batchPlan);
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(batchPlanList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
}
