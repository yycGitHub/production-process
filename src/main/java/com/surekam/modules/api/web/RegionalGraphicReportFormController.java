package com.surekam.modules.api.web;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;


import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatchReport;
import com.surekam.modules.agro.productlibraryquick.service.ProductLibraryQuickService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.api.service.BatchManageApiService;
import com.surekam.modules.api.utils.SolarTerms_24;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.ApiSystemService;

/**
 * 全区图形报表接口API
 * 
 * @author lxw
 * @date 2019-07-10
 */
@Api
@Controller
@RequestMapping(value = "api/chart")
public class RegionalGraphicReportFormController extends BaseController {

	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;

	@Autowired
	private ApiSystemService apiSystemService;

	@Autowired
	private BatchManageApiService batchManageApiService;
	
	@Autowired
	private ProductLibraryQuickService productLibraryQuickService;
	@Autowired
	private OfficeDao officeDao;

	/**
	 * 根据公司产量柱状图数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "getYield", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取产量柱状图数据", httpMethod = "POST", notes = "获取品种产量柱状图数据", consumes = "application/x-www-form-urlencoded")
	public String getAreaAndYield(HttpServletRequest request,
			@RequestParam(required = false) String officeId,
			@RequestParam(required = false) String year,
			@RequestParam(required = false) String fromCenter) {
		String token = request.getHeader("X-Token");
		User currentUser = apiSystemService.findUserByToken(token);
		if (currentUser==null) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_INVALID.getCode(), ResultEnum.TOKEN_IS_INVALID.getMessage()));
		}
		if(StringUtils.isNotBlank(fromCenter)&&"1".equals(fromCenter)){
			Office office = officeDao.getByKuid(officeId);
			if(office==null){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), ResultEnum.DATA_NOT_EXIST.getMessage()));
			}
			officeId = office.getId();
		}
		Map<String, Object> getYield = productLibraryTreeService
				.getChartYield(officeId,year);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(getYield));
	}

	/**
	 * 根据公司产量柱状图数据季度
	 * 
	 * @return
	 */
	@RequestMapping(value = "getYieldQuarter", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取产量柱状图数据", httpMethod = "POST", notes = "获取品种产量柱状图数据", consumes = "application/x-www-form-urlencoded")
	public String getYieldQuarter(HttpServletRequest request,
			@RequestParam(required = false) String officeId,
			@RequestParam(required = false) String fromCenter,
			@RequestParam(required = false) String year) {
		String token = request.getHeader("X-Token");
		User currentUser = apiSystemService.findUserByToken(token);
		if (currentUser==null) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_INVALID.getCode(), ResultEnum.TOKEN_IS_INVALID.getMessage()));
		}
		if(StringUtils.isNotBlank(fromCenter)&&"1".equals(fromCenter)){
			Office office = officeDao.getByKuid(officeId);
			if(office==null){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), ResultEnum.DATA_NOT_EXIST.getMessage()));
			}
			officeId = office.getId();
		}
		Map<String, Object> getYield = productLibraryTreeService
				.getYieldQuarter(officeId,year);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(getYield));
	}

	/**
	 * 根据公司产量柱状图数据月份
	 * 
	 * @return
	 */
	@RequestMapping(value = "getYieldMonth", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取产量柱状图数据", httpMethod = "POST", notes = "获取品种产量柱状图数据", consumes = "application/x-www-form-urlencoded")
	public String getYieldMonth(HttpServletRequest request,
			@RequestParam(required = false) String officeId,
			@RequestParam(required = false) String fromCenter,
			@RequestParam(required = false) String year) {
		String token = request.getHeader("X-Token");
		User currentUser = apiSystemService.findUserByToken(token);
		if (currentUser==null) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_INVALID.getCode(), ResultEnum.TOKEN_IS_INVALID.getMessage()));
		}
		if(StringUtils.isNotBlank(fromCenter)&&"1".equals(fromCenter)){
			Office office = officeDao.getByKuid(officeId);
			if(office==null){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), ResultEnum.DATA_NOT_EXIST.getMessage()));
			}
			officeId = office.getId();
		}
		Map<String, Object> getYield = productLibraryTreeService
				.getYieldMonth(officeId,year);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(getYield));
	}

	/**
	 * 根据公司产量柱状图数据周
	 * 
	 * @return
	 */
	@RequestMapping(value = "getYieldWeek", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取产量柱状图数据", httpMethod = "POST", notes = "获取品种产量柱状图数据", consumes = "application/x-www-form-urlencoded")
	public String getYieldWeek(HttpServletRequest request,
			@RequestParam(required = false) String officeId,
			@RequestParam(required = false) String fromCenter,
			@RequestParam(required = false) String year) {
		String token = request.getHeader("X-Token");
		User currentUser = apiSystemService.findUserByToken(token);
		if (currentUser==null) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_INVALID.getCode(), ResultEnum.TOKEN_IS_INVALID.getMessage()));
		}
		if(StringUtils.isNotBlank(fromCenter)&&"1".equals(fromCenter)){
			Office office = officeDao.getByKuid(officeId);
			if(office==null){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), ResultEnum.DATA_NOT_EXIST.getMessage()));
			}
			officeId = office.getId();
		}
		Map<String, Object> getYield = productLibraryTreeService
				.getYieldWeek(officeId,year);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(getYield));
	}
	/**
	 * 根据公司产量柱状图数据节气
	 * 
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "getYieldSolarTerms", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取产量柱状图数据", httpMethod = "POST", notes = "获取品种产量柱状图数据", consumes = "application/x-www-form-urlencoded")
	public String getYieldSolarTerms(HttpServletRequest request,
			@RequestParam(required = false) String officeId,
			@RequestParam(required = false) String fromCenter,
			@RequestParam(required = false) String year) throws ParseException {
		String token = request.getHeader("X-Token");
		User currentUser = apiSystemService.findUserByToken(token);
		if (currentUser==null) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_INVALID.getCode(), ResultEnum.TOKEN_IS_INVALID.getMessage()));
		}
		if(StringUtils.isNotBlank(fromCenter)&&"1".equals(fromCenter)){
			Office office = officeDao.getByKuid(officeId);
			if(office==null){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), ResultEnum.DATA_NOT_EXIST.getMessage()));
			}
			officeId = office.getId();
		}
		Map<String, Object> getYield = productLibraryTreeService
				.getYieldSolarTerms(officeId,year);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(getYield));
	}

	/**
	 * 获取年度下拉框
	 * 
	 * @return
	 */
	@RequestMapping(value = "getYears", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取年度下拉框", httpMethod = "POST", notes = "获取年度下拉框", consumes = "application/x-www-form-urlencoded")
	public String getYears(HttpServletRequest request,
			@RequestParam(required = false) String officeId,
			@RequestParam(required = false) String fromCenter,
			@RequestParam(required = false) String year) {
		String token = request.getHeader("X-Token");
		User currentUser = apiSystemService.findUserByToken(token);
		if (currentUser==null) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_INVALID.getCode(), ResultEnum.TOKEN_IS_INVALID.getMessage()));
		}
		if(StringUtils.isNotBlank(fromCenter)&&"1".equals(fromCenter)){
			Office office = officeDao.getByKuid(officeId);
			if(office==null){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), ResultEnum.DATA_NOT_EXIST.getMessage()));
			}
			officeId = office.getId();
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(productLibraryTreeService.getYears(officeId)));
	}

	@ResponseBody
	@RequestMapping(value = "/getBatchReport")
	@ApiOperation(value = "根据公司或基地查询当前基地以及下级的批次信息", httpMethod = "POST", notes = "根据公司或基地查询当前基地以及下级的批次信息", consumes = "application/x-www-form-urlencoded")
	public String getBatchReport(
			HttpServletRequest request, 
			@RequestParam(required = false) String officeId,
			@RequestParam(required = false) String baseId, 
			@RequestParam(required = false) String batchCode, 
			@RequestParam(required = false) String operType, 
			@RequestParam String isOffice, 
			@RequestParam Integer pageno,
			@RequestParam Integer pagesize) {
		try {
			Page<ProductionBatchReport> page = batchManageApiService.getBatchReport(officeId, baseId,batchCode, operType, isOffice, pageno, pagesize);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取树状下级异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	/**
	 * 根据公司生产的详细品种分布情况统计分析
	 * 
	 * @return
	 */
	@RequestMapping(value = "getVarietyDistribution", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "生产的详细品种分布情况统计分析", httpMethod = "POST", notes = "生产的详细品种分布情况统计分析", consumes = "application/x-www-form-urlencoded")
	public String getVarietyDistribution(HttpServletRequest request,
			@RequestParam(required = false) String fromCenter,
			@RequestParam String officeId) {
		String token = request.getHeader("X-Token");
		User currentUser = apiSystemService.findUserByToken(token);
		if (currentUser==null) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_INVALID.getCode(), ResultEnum.TOKEN_IS_INVALID.getMessage()));
		}
		if(StringUtils.isNotBlank(fromCenter)&&"1".equals(fromCenter)){
			Office office = officeDao.getByKuid(officeId);
			if(office==null){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), ResultEnum.DATA_NOT_EXIST.getMessage()));
			}
			officeId = office.getId();
		}
		Map<String, Object> getYield = productLibraryQuickService
				.getVarietyDistribution(officeId);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(getYield));
	}
}
