package com.surekam.modules.api.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.web.BaseController;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.basemanage.dao.BaseTreeDao;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
import com.surekam.modules.agro.batchplan.entity.BatchPlan;
import com.surekam.modules.agro.batchplan.service.BatchPlanService;
import com.surekam.modules.agro.breedingplan.entity.BreedingPlan;
import com.surekam.modules.agro.breedingplan.service.BreedingPlanService;
import com.surekam.modules.agro.sensorsetup.entity.SensorSetup;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.api.service.BaseManageApiService;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;

/**
 * 种植计划表Controller
 * @author luoxw
 * @version 2019-10-16
 */
@Controller
@RequestMapping(value = "api/breedingplan")
public class BreedingPlanController extends BaseController {

	@Autowired
	private BreedingPlanService breedingPlanService;
	@Autowired
	private BaseTreeDao baseTreeDao;
	@Autowired
	private ApiUserService apiUserService;

	@Autowired
	private BatchPlanService batchPlanService;
	
	@ResponseBody
	@RequestMapping(value = "/getList")
	@ApiOperation(value = "获取列表", httpMethod = "POST", notes = "获取列表", consumes = "application/x-www-form-urlencoded")
	public String getList(HttpServletRequest request, 
			@RequestParam(required = false) String baseId,
			@RequestParam(required = false) String isOffice, 
			@RequestParam(required = false) String officeId, 
			@RequestParam(required = false) String year,
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
			Page<BreedingPlan> page = new Page<BreedingPlan>(pageNo, pageSize);
			page = breedingPlanService.find(page, baseId, isOffice, officeId, year, user);
			List<BreedingPlan> list = page.getList();
			for(BreedingPlan breedingPlan : list){
				breedingPlan.setBatchNumber(String.valueOf(batchPlanService.findList(breedingPlan.getId()).size()));
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("获取list异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/savaBreedingPlan")
	@ApiOperation(value = "新增或者修改", httpMethod = "POST", notes = "新增或者修改", consumes = "application/x-www-form-urlencoded")
	public String savaBreedingPlan(HttpServletRequest request,
			@RequestBody @ApiParam(name = "新增信息", value = "传入json格式", required = true) BreedingPlan breedingPlan) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			if (breedingPlanService.findList(breedingPlan.getBaseId(),breedingPlan.getPlanYear()).size()>0) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_EXIST.getCode(), "计划年份数据已存在"));
			}
			if (StringUtils.isBlank(breedingPlan.getId())) {
				breedingPlan.setCreateTime(new Date());
				breedingPlan.setCreateUserId(user.getId());
				breedingPlan.setStates("A");
			} else {
				breedingPlan.setUpdateTime(new Date());
				breedingPlan.setStates("U");
				breedingPlan.setUpdateUserId(user.getId());
			}
			Office office = new Office();
			office.setId(breedingPlan.getOfficeId());
			BaseTree base = new BaseTree();
			base.setId(breedingPlan.getBaseId());
			breedingPlan.setOffice(office);
			breedingPlan.setBaseTree(base);
			breedingPlan.setIsLock("0");
			breedingPlanService.save(breedingPlan);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	@ApiOperation(value = "删除", httpMethod = "POST", notes = "删除", consumes = "application/x-www-form-urlencoded")
	public String delete(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			breedingPlanService.delete(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getBreedingPlanById")
	@ApiOperation(value = "根据ID查询信息", httpMethod = "POST", notes = "根据ID查询信息", consumes = "application/x-www-form-urlencoded")
	public String getBreedingPlanById(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			BreedingPlan breedingPlan = breedingPlanService.get(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(breedingPlan));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getInfo")
	@ApiOperation(value = "新增时查询信息", httpMethod = "POST", notes = "新增时查询信息", consumes = "application/x-www-form-urlencoded")
	public String getInfo(HttpServletRequest request, 
			@RequestParam(required = false) String baseId,
			@RequestParam(required = false) String isOffice, 
			@RequestParam(required = false) String officeId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			BreedingPlan breedingPlan = new BreedingPlan();
			BaseTree agroBaseTree = baseTreeDao.get(baseId);
			// 设置默认值
			breedingPlan.setPlanTotal("1000");
			breedingPlan.setPlanUnit("kg");
			if(StringUtils.isNotBlank(agroBaseTree.getCultivationAbility())){
				breedingPlan.setPlanTotal(agroBaseTree.getCultivationAbility());
			}
			if(StringUtils.isNotBlank(agroBaseTree.getCultivationUnit())){
				breedingPlan.setPlanUnit(agroBaseTree.getCultivationUnit());
			}
			List<BreedingPlan> list = breedingPlanService.findList(baseId);
			if(list.size()>0){
				breedingPlan.setPlanYear(String.valueOf(Integer.parseInt(list.get(0).getPlanYear())+1));
				breedingPlan.setStartDate(breedingPlan.getPlanYear()+"-01-01");
			}else{
				SimpleDateFormat year = new SimpleDateFormat("yyyy");
				breedingPlan.setPlanYear(year.format(new Date()));
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				breedingPlan.setStartDate(sf.format(new Date()));
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(breedingPlan));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getEndDate")
	@ApiOperation(value = "根据ID查询信息", httpMethod = "POST", notes = "根据ID查询信息", consumes = "application/x-www-form-urlencoded")
	public String getEndDate(HttpServletRequest request, 
			@RequestParam String standardId, 
			@RequestParam String beginDay) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(breedingPlanService.getEndDate(standardId, beginDay)));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
}
