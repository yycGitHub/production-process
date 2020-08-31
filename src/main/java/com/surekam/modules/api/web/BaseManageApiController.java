package com.surekam.modules.api.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.auditrecord.service.AuditRecordService;
import com.surekam.modules.agro.basemanage.entity.BaseManager;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
//import com.surekam.modules.agro.auditrecord.service.AuditRecordService;
import com.surekam.modules.agro.basemanage.entity.vo.BaseTreeVo;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.productbatchtaskresolve.service.ProductBatchTaskResolveService;
import com.surekam.modules.agro.productionbatch.serivce.ProductionBatchService;
import com.surekam.modules.api.dto.req.AgroBaseTreeReq;
import com.surekam.modules.api.dto.resp.BaseTreeResp;
import com.surekam.modules.api.dto.resp.UserRoleResp;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.api.service.BaseManageApiService;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.OfficeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 基地管理API
 * 
 * @author tangjun
 * @date 2019-4-8
 */

@Api
@Controller
@RequestMapping(value = "api/baseManage")
public class BaseManageApiController extends BaseController {
	@Autowired
	private BaseManageApiService baseManageApiService;


	@Autowired
	private ExpertsService expertsService;
	@Autowired
	private AuditRecordService auditRecordService;
	
	@Autowired
	private ProductionBatchService productionBatchService;

	@Autowired
	private ApiUserService apiUserService;
	
	@Autowired
	private OfficeService officeService;
	@Autowired
	private OfficeDao officeDao;
	
	@Autowired
	private ProductBatchTaskResolveService productBatchTaskResolveService;

	@ResponseBody
	@RequestMapping(value = "/savaAgroBaseTree")
	@ApiOperation(value = "新增基地信息", httpMethod = "POST", notes = "新增基地信息", consumes = "application/x-www-form-urlencoded")
	public String savaAgroBaseTree(HttpServletRequest request,
			@RequestBody @ApiParam(name = "新增基地信息", value = "传入json格式", required = true) AgroBaseTreeReq agroBaseTreeReq) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			if (baseManageApiService.existCode(agroBaseTreeReq)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_EXIST.getCode(), "编码重复"));
			}
			User user = apiUserService.getUserByToken(token);
			ResultBean<String> savaAgroBaseTree = baseManageApiService.savaAgroBaseTree(agroBaseTreeReq, user);
			return JsonMapper.nonDefaultMapper().toJson(savaAgroBaseTree);
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/deleteAgroBaseTree")
	@ApiOperation(value = "删除基地信息", httpMethod = "POST", notes = "删除基地信息", consumes = "application/x-www-form-urlencoded")
	public String deleteAgroBaseTree(HttpServletRequest request, @RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			String deleteAgroBaseTree = baseManageApiService.deleteAgroBaseTree(id, user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(deleteAgroBaseTree));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getAgroBaseTree")
	@ApiOperation(value = "获取当前基地信息", httpMethod = "POST", notes = "获取当前基地信息", consumes = "application/x-www-form-urlencoded")
	public String getAgroBaseTree(HttpServletRequest request, @RequestParam String id) {
		try {
			BaseTreeResp agroBaseTreeResp = baseManageApiService.getAgroBaseTree(id);
			List<Experts> batchManageList = expertsService.getBaseExpertsList(id);
			agroBaseTreeResp.setExpertsList(batchManageList);
			return JsonMapper.allPropertyMapper().toJson(ResultUtil.success(agroBaseTreeResp));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getBranchList")
	@ApiOperation(value = "获取基地树状", httpMethod = "POST", notes = "获取基地树状", consumes = "application/x-www-form-urlencoded")
	public String getBranchList(HttpServletRequest request) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			List<BaseTreeVo> branchList = baseManageApiService.getBranchList(user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(branchList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getOfficeCompanyAdmin")
	@ApiOperation(value = "获取公司管理员", httpMethod = "POST", notes = "获取公司管理员", consumes = "application/x-www-form-urlencoded")
	public String getOfficeCompanyAdmin(HttpServletRequest request, @RequestParam String officeId,
			@RequestParam String roleId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			List<UserRoleResp> userRoleRespList = baseManageApiService.getOfficeCompanyAdmin(officeId, roleId);
			if (userRoleRespList.isEmpty()) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(" "));
			}
			
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(userRoleRespList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/findByBranchNameQuery")
	@ApiOperation(value = "查询重复的基地名称", httpMethod = "POST", notes = "查询重复的基地名称", consumes = "application/x-www-form-urlencoded")
	public String findByBranchNameQuery(HttpServletRequest request, @RequestParam String id,
			@RequestParam String name) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			String findByBranchNameQuery = baseManageApiService.findByBranchNameQuery(id, name);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(findByBranchNameQuery));
		} catch (Exception e) {
			logger.info("获取树状下级异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/findHomeUserData")
	@ApiOperation(value = "获取主页用户数据", httpMethod = "POST", notes = "获取主页用户数据", consumes = "application/x-www-form-urlencoded")
	public String findHomeUserData(HttpServletRequest request, @RequestParam String type, @RequestParam(required = false) String officeId, 
			@RequestParam(required = false) String baseId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			user.setCompanyName(user.getOffice().getName());
			if ("1".equals(type)) {
				user.setBaseNum(baseManageApiService.findAgroBaseCount(user));
			} else if ("2".equals(type)) {
				user.setBatchNum(productionBatchService.findBatchCount(user));
			} else if ("3".equals(type)) {
				user.setCompanyNum(baseManageApiService.findCompanyCount(user));
			} else if ("4".equals(type)) {
				user.setCompanyName(officeService.get(officeId).getName());
				user.setBaseNum(baseManageApiService.findBaseCountByOfficeId(officeId));
			}  else if ("5".equals(type)) {
				user.setCompanyName(officeService.get(officeId).getName());
				user.setBatchNum(productionBatchService.findBatchCountByBaseId(officeId));
			}  
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(user));
		} catch (Exception e) {
			logger.info("获取主页用户数据{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/findHomeBaseData")
	@ApiOperation(value = "获取主页基地列表数据", httpMethod = "POST", notes = "获取主页基地列表数据", consumes = "application/x-www-form-urlencoded")
	public String findHomeBaseData(HttpServletRequest request, @RequestParam(required = false) String baseId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			List<Map<String,Object>> list = baseManageApiService.findAgroBaseList(user,baseId);
			List<Map<String,Object>> rlist = new ArrayList<Map<String,Object>>();
			for (Map<String,Object> map : list) {
				map.put("pname", baseManageApiService.getPName(map.get("id").toString()));
				map.put("auditTime", auditRecordService.findAuditRecordTime(map.get("id").toString()));
				map.put("batchCount", productionBatchService.findBatchCountByBaseId(map.get("id").toString()));
				map.put("auditCount", auditRecordService.findAuditRecordCount(map.get("id").toString()));
				map.put("batchData", productionBatchService.findBatchDataByBaseId(map.get("id").toString()));
				map.put("collectData", productBatchTaskResolveService.getCollectionTaskCount2(baseId));//已确认数
				map.put("noCollectData", productBatchTaskResolveService.getCollectionTaskCount3(baseId));//未认数
				rlist.add(map);
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(rlist));
		} catch (Exception e) {
			logger.info("获取主页基地列表数据{}" + e.toString());
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/findBaseData")
	@ApiOperation(value = "获取主页基地列表数据", httpMethod = "POST", notes = "获取主页基地列表数据", consumes = "application/x-www-form-urlencoded")
	public String findBaseData(HttpServletRequest request, @RequestParam String officeId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			List<Map<String,Object>> list = baseManageApiService.findBaseList(officeId);
			List<Map<String,Object>> rlist = new ArrayList<Map<String,Object>>();
			for (Map<String,Object> map : list) {
				map.put("pname", baseManageApiService.getPName(map.get("id").toString()));
				//map.put("auditTime", auditRecordService.findAuditRecordTime(map.get("id").toString()));
				map.put("batchCount", productionBatchService.findBatchCountByBaseId(map.get("id").toString()));
				//map.put("auditCount", auditRecordService.findAuditRecordCount(map.get("id").toString()));
				map.put("batchData", productionBatchService.findBatchDataByBaseId(map.get("id").toString()));
				//map.put("collectData", productBatchTaskResolveService.getCollectionTaskCount(baseId));
				rlist.add(map);
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(rlist));
		} catch (Exception e) {
			logger.info("获取主页基地列表数据{}" + e.toString());
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getPeasantHouseholdInfo")
	@ApiOperation(value = "获取农户信息", httpMethod = "POST", notes = "获取农户信息", consumes = "application/x-www-form-urlencoded")
	public String getPeasantHouseholdInfo(HttpServletRequest request, @RequestParam String baseId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}

			Map<String, String> map = baseManageApiService.getPeasantHouseholdInfo(baseId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
		} catch (Exception e) {
			logger.info("获取树状下级异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	/**
	 * 专家新增服务获取公司下拉框列表数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getBaseList", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "专家新增服务获取公司下拉框列表数据", httpMethod = "GET", notes = "专家新增服务获取公司下拉框列表数据", consumes = "application/json")
	public String getBaseList(HttpServletRequest request, HttpServletResponse response,String officeId) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			try {
				List<BaseTree> baseTree = Lists.newArrayList();
				baseTree = baseManageApiService.getBaseTreeList(officeId);
				List<Map<String, Object>> mapList = Lists.newArrayList();
				for (int i = 0; i < baseTree.size(); i++) {
					BaseTree e = baseTree.get(i);
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", e.getId());
					map.put("name", e.getName());
					mapList.add(map);
				}
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(mapList));
			} catch (Exception e) {
				logger.error("获取登录用户的角色列表错误：" + e);
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	//----------------------------------------------------平台管理员接口------------------------------------------------------//
	@ResponseBody
	@RequestMapping(value = "/findHomeCompanyData")
	@ApiOperation(value = "获取公司列表数据", httpMethod = "POST", notes = "获取公司列表数据", consumes = "application/x-www-form-urlencoded")
	public String findHomeCompanyData(HttpServletRequest request) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = apiUserService.getUserByToken(token);
			List<Map<String,Object>> list = productionBatchService.findCompanyList(user);
			List<Map<String,Object>> rlist = new ArrayList<Map<String,Object>>();
			for (Map<String,Object> map : list) {
				String id = map.get("id").toString();
				map.put("wcNum", productionBatchService.findWCCountByOfficeId(id));
				map.put("wwcNum", productionBatchService.findWWCCountByOfficeId(id));
				rlist.add(map);
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(rlist));
		} catch (Exception e) {
			logger.info("获取主页基地列表数据{}" + e.toString());
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	/**
	 * 获取用户关联的基地数量
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserIdByBase")
	@ApiOperation(value = "获取用户关联的基地数量", httpMethod = "GET", notes = "获取用户关联的基地数量", consumes = "application/x-www-form-urlencoded")
	public String getByUserIdList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			try {
				User user = apiUserService.getUserByToken(token);
				List<BaseManager> list  = baseManageApiService.getByUserIdList(user.getId());
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
			} catch (Exception e) {
				logger.error("获取用户关联的基地数量错误：" + e);
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	/**
	 * web个人任务人员查询
	 * @param request
	 * @param officeId
	 * @param roleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserListNH")
	@ApiOperation(value = "web个人任务人员查询", httpMethod = "POST", notes = "web个人任务人员查询", consumes = "application/x-www-form-urlencoded")
	public String getUserListNH(HttpServletRequest request, @RequestParam String officeId,
			@RequestParam String roleId) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			List<UserRoleResp> userRoleRespList = baseManageApiService.getUserListNH(officeId, roleId);
			if (userRoleRespList.isEmpty()) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(" "));
			}
			
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(userRoleRespList));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	
	@ResponseBody
	@RequestMapping(value = "/getBaseLists")
	@ApiOperation(value = "获取平台下所有的基地列表", httpMethod = "POST", notes = "获取平台下所有的基地列表", consumes = "application/x-www-form-urlencoded")
	public String getBaseLists(HttpServletRequest request, 
			@RequestParam String kuId,
			@RequestParam String platform) {
		try {
			Office office = officeDao.getByKuid(kuId);
			if(office==null){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), ResultEnum.DATA_NOT_EXIST.getMessage()));
			}
			String officeId = office.getId();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(baseManageApiService.getBaseLists(officeId,platform)));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
}