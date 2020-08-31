package com.surekam.modules.api.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.api.dto.req.AgroProductionBatchReq;
import com.surekam.modules.api.dto.resp.BatchCropInfoResp;
import com.surekam.modules.api.dto.resp.BatchHarvestDateResp;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.api.service.BatchManageApiService;
import com.surekam.modules.sys.entity.User;

/**
 * 批次接口API
 * 
 * @author tangjun
 * @date 2019-4-15
 */
@Api
@Controller
@RequestMapping(value = "api/batchManage")
public class BatchManageApiController extends BaseController {

	@Autowired
	private BatchManageApiService batchManageApiService;

	@Autowired
	private ApiUserService apiUserService;

	@ResponseBody
	@RequestMapping(value = "/savaBatchManage")
	@ApiOperation(value = "新增或者修改批次信息", httpMethod = "POST", notes = "新增或者修改批次信息", consumes = "application/x-www-form-urlencoded")
	public String savaBatchManage(HttpServletRequest request,
			@RequestBody @ApiParam(name = "新增基地信息", value = "传入json格式", required = true) AgroProductionBatchReq req) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			ResultBean<String> savaBatchManage = batchManageApiService.savaBatchManage(user, req);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(savaBatchManage));
		} catch (Exception e) {
			logger.info("新增或者修改批次信息异常{}" + e.toString());
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/deleteBatchManage")
	@ApiOperation(value = "删除批次信息", httpMethod = "POST", notes = "删除批次信息", consumes = "application/x-www-form-urlencoded")
	public String deleteBatchManage(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			String savaBatchManage = batchManageApiService.deleteBatchManage(user, id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(savaBatchManage));
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("删除批次信息异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getBatchManageList")
	@ApiOperation(value = "获取批次信息列表", httpMethod = "POST", notes = "获取批次信息列表", consumes = "application/x-www-form-urlencoded")
	public String getBatchManageList(HttpServletRequest request, @RequestParam String baseId,
			@RequestParam Integer pageno, @RequestParam Integer pagesize, @RequestParam String operType) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			Page<ProductionBatch> batchManageList = batchManageApiService.getBatchManageList(baseId, pageno, pagesize, operType);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(batchManageList));
		} catch (Exception e) {
			logger.info("获取树状下级异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getCropStandardInfo")
	@ApiOperation(value = "获取标准下拉框", httpMethod = "POST", notes = "获取标准下拉框", consumes = "application/x-www-form-urlencoded")
	public String getCropStandardInfo(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			List<BatchCropInfoResp> batchInfoResp = batchManageApiService.getCropStandardInfoList(id);
			if (batchInfoResp == null) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage()));
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(batchInfoResp));
		} catch (Exception e) {
			logger.info("获取标准下拉框异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}

	}

	@ResponseBody
	@RequestMapping(value = "/getStandardIdQueryInfo")
	@ApiOperation(value = "根据品种ID获取作物标准已经采收日期", httpMethod = "POST", notes = "根据品种ID获取作物标准已经采收日期", consumes = "application/x-www-form-urlencoded")
	public String getStandardIdQueryInfo(HttpServletRequest request, @RequestParam(required = false) String officeId,
			@RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			if(StringUtils.isBlank(officeId)){
				officeId = user.getOffice().getId();
			}
			BatchHarvestDateResp batchRecoveryDateResp = batchManageApiService.getStandardIdQueryInfo(officeId, id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(batchRecoveryDateResp));
		} catch (Exception e) {
			logger.info("根据品种ID获取作物标准已经采收日期异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getEndDay")
	@ApiOperation(value = "根据标准ID获取结束时间", httpMethod = "POST", notes = "根据标准ID获取结束时间", consumes = "application/x-www-form-urlencoded")
	public String getEndDay(HttpServletRequest request, @RequestParam String standardId, @RequestParam String beginDay) {
		try {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(batchManageApiService.getEndDate(standardId, beginDay)));
		} catch (Exception e) {
			logger.info("根据品种ID获取作物标准已经采收日期异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getBatchManage")
	@ApiOperation(value = "根据批次ID查询批次信息", httpMethod = "POST", notes = "根据批次ID查询批次信息", consumes = "application/x-www-form-urlencoded")
	public String getBatchManage(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			ProductionBatch batchManage = batchManageApiService.getBatchManage(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(batchManage));
		} catch (Exception e) {
			logger.info("根据批次ID查询批次信息异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getNHBatchList")
	@ApiOperation(value = "根据登录用户获取批次列表", httpMethod = "POST", notes = "根据登录用户获取批次列表", consumes = "application/x-www-form-urlencoded")
	public String getNHBatchList(HttpServletRequest request, @RequestParam Integer pageno, @RequestParam Integer pagesize, @RequestParam(required = false) String officeId) {
		try {
			int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
			int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
			Page<ProductionBatch> page = new Page<ProductionBatch>(pageNo, pageSize);
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			if (StringUtils.isNotBlank(token)) {
				User user = apiUserService.getUserByToken(token);
				page = batchManageApiService.getNHBatchList(page, user, officeId);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
			}else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getNHBatchInfo")
	@ApiOperation(value = "根据登录用户获取批次列表", httpMethod = "POST", notes = "根据登录用户获取批次列表", consumes = "application/x-www-form-urlencoded")
	public String getNHBatchInfo(HttpServletRequest request, @RequestParam String batchId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isNotBlank(token)) {
				ProductionBatch productionBatch = batchManageApiService.getBatchManage(batchId);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(productionBatch));
			}else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getOfficeQueryBatchInfoList")
	@ApiOperation(value = "根据公司或基地查询当前基地以及下级的批次信息", httpMethod = "POST", notes = "根据公司或基地查询当前基地以及下级的批次信息", consumes = "application/x-www-form-urlencoded")
	public String getOfficeQueryBatchInfoList(
			HttpServletRequest request, 
			@RequestParam(required = false) String officeId,
			@RequestParam(required = false) String baseId, 
			@RequestParam String operType, 
			@RequestParam String isOffice, 
			@RequestParam Integer pageno,
			@RequestParam Integer pagesize) {
		try {
			String token = request.getHeader("X-Token");
			User user = apiUserService.getUserByToken(token);
			Page<ProductionBatch> page = batchManageApiService.getOfficeQueryBatchInfoList(user, officeId, baseId, operType, isOffice, pageno, pagesize);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("获取树状下级异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getHasTaskListBatchList")
	@ApiOperation(value = "根据公司或基地查询当前基地以及下级的有农事作业记录的批次信息", httpMethod = "POST", notes = "根据公司或基地查询当前基地以及下级的有农事作业记录的批次信息", consumes = "application/x-www-form-urlencoded")
	public String getHasTaskListBatchList(
			HttpServletRequest request, 
			@RequestParam(required = false) String officeId,
			@RequestParam(required = false) String baseId, 
			@RequestParam String operType, 
			@RequestParam String isOffice,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		try {
			List<ProductionBatch> batchs = batchManageApiService.getHasTaskListBatchList(officeId, baseId, operType, isOffice, startDate, endDate);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(batchs));
		} catch (Exception e) {
			logger.error("根据公司或基地查询当前基地以及下级的有农事作业记录的批次信息异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/finishedBatch", produces = "text/plain;charset=UTF-8")
	@ApiOperation(value = "结束批次", httpMethod = "POST", notes = "结束批次", consumes = "application/x-www-form-urlencoded")
	public String finishedBatch(HttpServletRequest request, 
			@RequestParam(required = true) String id,
			@RequestParam(required = true) String finishStatus, 
			@RequestParam(required = false) String remark,
			@RequestParam(required = false) String finishTime) {
		try {
			String token = request.getHeader("X-Token");
			User user = apiUserService.getUserByToken(token);
			batchManageApiService.finishedBatch(id, remark, finishStatus, finishTime, user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("根据基地查询农户信息异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/findByUserInfo")
	@ApiOperation(value = "根据基地查询农户信息", httpMethod = "POST", notes = "根据基地查询农户信息", consumes = "application/x-www-form-urlencoded")
	public String findByUserInfo(HttpServletRequest request, @RequestParam String baseId, @RequestParam String operType) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			String findByUserInfo = batchManageApiService.findByUserInfo(baseId, operType);
			return JsonMapper.allPropertyMapper().toJson((ResultUtil.success(findByUserInfo)));
		} catch (Exception e) {
			logger.error("根据基地查询农户信息异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/findByOfficeAndBatchCode")
	@ApiOperation(value = "提供加工系统的批次验证", httpMethod = "POST", notes = "提供加工系统的批次验证", consumes = "application/x-www-form-urlencoded")
	public String findByOfficeAndBatchCode(HttpServletRequest request, 
			@RequestParam(required = false) String batchCode,
			@RequestParam(required = false) String kuId) {
		try {
			boolean type = batchManageApiService.findByOfficeAndBatchCode(batchCode, kuId);
			return JsonMapper.allPropertyMapper().toJson((ResultUtil.success(type)));
		} catch (Exception e) {
			logger.error("根据基地查询农户信息异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/a")
	@ApiOperation(value = "提供加工系统的批次验证", httpMethod = "POST", notes = "提供加工系统的批次验证", consumes = "application/x-www-form-urlencoded")
	public String a(HttpServletRequest request) {
		try {
			batchManageApiService.timingSensor();
			return JsonMapper.allPropertyMapper().toJson((ResultUtil.success()));
		} catch (Exception e) {
			logger.error("根据基地查询农户信息异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	

	@ResponseBody
	@RequestMapping(value = "/saveConfirmation")
	@ApiOperation(value = "保存批次认证信息", httpMethod = "POST", notes = "保存批次认证信息", consumes = "application/x-www-form-urlencoded")
	public String saveConfirmation(HttpServletRequest request,
			@RequestBody @ApiParam(name = "保存批次认证信息", value = "传入json格式", required = true) AgroProductionBatchReq req) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			batchManageApiService.save(user, req);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.info("保存批次认证信息异常{}" + e.toString());
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getStandardByModel")
	@ApiOperation(value = "批次获取标准下拉框", httpMethod = "POST", notes = "批次获取标准下拉框", consumes = "application/x-www-form-urlencoded")
	public String getStandardByModel(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			List<BatchCropInfoResp> batchInfoResp = batchManageApiService.getStandardInfoList(id);
			if (batchInfoResp == null) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage()));
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(batchInfoResp));
		} catch (Exception e) {
			logger.info("获取标准下拉框异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}

	}

}
