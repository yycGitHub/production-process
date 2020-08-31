package com.surekam.modules.api.web;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
import com.surekam.modules.agro.experts.entity.ExpertServiceInfo;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.experts.service.ExpertsServiceInfoService;
import com.surekam.modules.agro.file.service.AgroFileInfoService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.api.service.BaseManageApiService;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.OfficeService;
import com.surekam.modules.sys.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 专家服务
 * 
 * @author
 *
 */
@Api
@Controller
@RequestMapping(value = "api/expertServiceInfo/")
public class ExpertServiceInfoController {
	
	@Autowired
	private ExpertsServiceInfoService expertsServiceInfoService;
	@Autowired
	private ApiUserService apiUserService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private AgroFileInfoService fileInfoService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private BaseManageApiService baseManageApiService;
	
	@Autowired
	private ExpertsService expertsService;
	/**
	 * @author 
	 * app-专家服务保存
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "saveExpertsServiceInfo",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "app-专家服务保存", httpMethod = "POST", notes = "app-专家服务保存",	consumes="application/json")
	public String saveExpertsServiceInfo(HttpServletRequest request,HttpServletResponse response,
			String typeId,String officeId,String [] photo,String servicedate,String details,String address,String [] auditUrl,String baseId) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User user = apiUserService.getUserByToken(token); 
			expertsServiceInfoService.saveExpertsServiceInfo(user,officeId,servicedate,typeId,address,details,photo,auditUrl,baseId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

	/**
	 * @author 
	 * app-专家服务保存
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "savePCExpertsServiceInfo",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "PC-专家服务保存", httpMethod = "POST", notes = "app-专家服务保存",	consumes="application/json")
	public String savePCExpertsServiceInfo(HttpServletRequest request,HttpServletResponse response,
			@RequestParam String expertsId,
			@RequestParam(required = false) String typeId,
			@RequestParam(required = false) String officeId,
			@RequestParam(required = false, value = "photo[]") String [] photo,
			@RequestParam(required = false) String servicedate,
			@RequestParam(required = false) String details,
			@RequestParam(required = false) String address,
			@RequestParam(required = false) String baseId) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User user = new User();
			user.setId(expertsId);
			String [] auditUrl = {};
			if(photo==null){
				photo = auditUrl;
			}
			expertsServiceInfoService.saveExpertsServiceInfo(user,officeId,servicedate,typeId,address,details,photo,auditUrl,baseId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 *专家登录服务-用户id查询此专家关联的服务信息
	 * @author 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getExpertServiceInfoList",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "专家登录服务-用户id查询此专家关联的服务信息", httpMethod = "GET", notes = "专家登录服务-用户id查询此专家关联的服务信息",	consumes="application/x-www-form-urlencoded")
	public String getExpertServiceInfoList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno,@RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<ExpertServiceInfo> page = new Page<ExpertServiceInfo>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			page = expertsServiceInfoService.getExpertServiceInfoList(page,user.getId());
			List<ExpertServiceInfo> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<ExpertServiceInfo> iterator = list.iterator(); iterator.hasNext();) {
					ExpertServiceInfo expertServiceInfo = (ExpertServiceInfo) iterator.next();
					User user2 = userService.get(expertServiceInfo.getExpertId());
					if(user2 != null) {
						expertServiceInfo.setServicePeopleName(user2.getName());
					}else {
						expertServiceInfo.setServicePeopleName("");
					}
					Office office2 = officeService.get(expertServiceInfo.getServiceCompanyId());
					if(office2 != null) {
						expertServiceInfo.setCompanyName(office2.getName());//公司名称
					}else {
						expertServiceInfo.setCompanyName("");//公司名称
					}
					
					expertServiceInfo.setFileList(fileInfoService.find(expertServiceInfo.getId(),"1"));
					expertServiceInfo.setAuditList(fileInfoService.find(expertServiceInfo.getId(),"4"));
					String strtype ="";
					if(expertServiceInfo.getServiceType().equals("1")) {
						strtype = "收集样本";
					}else if(expertServiceInfo.getServiceType().equals("2")) {
						strtype = "咨询服务";
					}else if(expertServiceInfo.getServiceType().equals("3")) {
						strtype = "技术支持";
					}
					else if(expertServiceInfo.getServiceType().equals("4")) {
						strtype = "例行服务";
					}else if(expertServiceInfo.getServiceType().equals("5")) {
						strtype = "其他";
					}
					expertServiceInfo.setStrtype(strtype);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					expertServiceInfo.setStrDate(sdf.format(expertServiceInfo.getCreateTime()));
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getPCExpertServiceList")
	@ApiOperation(value = "获取每个专家服务记录列表PC", httpMethod = "POST", notes = "获取每个专家服务记录列表PC", consumes = "application/x-www-form-urlencoded")
	public String getBatchManageList(HttpServletRequest request,
			@RequestParam(required = false) String userId,
			@RequestParam(required = false) String serviceType,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
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
			Page<ExpertServiceInfo> page = new Page<ExpertServiceInfo>(pageNo, pageSize);
			ExpertServiceInfo expertServiceInfo = new ExpertServiceInfo();
			expertServiceInfo.setExpertId(userId);
			expertServiceInfo.setServiceType(serviceType);
			page = expertsServiceInfoService.getPCExpertServiceInfoList(page, expertServiceInfo);
			List<ExpertServiceInfo> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<ExpertServiceInfo> iterator = list.iterator(); iterator.hasNext();) {
					ExpertServiceInfo exInfo = (ExpertServiceInfo) iterator.next();
					exInfo.setExpertName(userService.get(exInfo.getExpertId()).getName());
					exInfo.setCompanyName(officeService.get(exInfo.getServiceCompanyId()).getName());//公司名称
					exInfo.setFileList(fileInfoService.find(exInfo.getId(),"1"));
				}
			}
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.success(page));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	
	/**
	 * userId传入值是专家id
	 * @param request
	 * @param userId
	 * @param officeId
	 * @param serviceType
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPCExpertServiceList2")
	@ApiOperation(value = "获取每个专家服务记录列表PC", httpMethod = "POST", notes = "获取每个专家服务记录列表PC", consumes = "application/x-www-form-urlencoded")
	public String getBatchManageList2(HttpServletRequest request,@RequestParam(required = false) String userId,@RequestParam(required = false) String officeId,
			@RequestParam(required = false) String serviceType,@RequestParam Integer pageno, @RequestParam Integer pagesize) {
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
			Page<ExpertServiceInfo> page = new Page<ExpertServiceInfo>(pageNo, pageSize);
			ExpertServiceInfo expertServiceInfo = new ExpertServiceInfo();
			if(StringUtils.isNotBlank(userId)) {
				Experts expert = expertsService.get(userId);
				expertServiceInfo.setExpertId(expert.getUserId());
			}
			expertServiceInfo.setServiceType(serviceType);
			page = expertsServiceInfoService.getPCExpertServiceInfoList(page, expertServiceInfo,officeId,user);
			List<ExpertServiceInfo> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<ExpertServiceInfo> iterator = list.iterator(); iterator.hasNext();) {
					ExpertServiceInfo exInfo = (ExpertServiceInfo) iterator.next();
					User user2 = userService.get(exInfo.getExpertId());
					if(user2 != null) {
						exInfo.setExpertName(user2.getName());
					}else {
						exInfo.setExpertName("");
					}
					Office office = officeService.get(exInfo.getServiceCompanyId());
					if(office != null ) {
						exInfo.setCompanyName(office.getName());//公司名称
					}else {
						exInfo.setCompanyName("");//公司名称
					}
					
					exInfo.setFileList(fileInfoService.find(exInfo.getId(),"1"));
					exInfo.setAuditList(fileInfoService.find(exInfo.getId(),"4"));
					if(StringUtils.isNotBlank(exInfo.getServiceBaseId())) {
						BaseTree bt = baseManageApiService.getbaseById(exInfo.getServiceBaseId()).get(0);
						if(bt != null) {
							exInfo.setBaseName(bt.getName());
						}else {
							exInfo.setBaseName("");
						}
						List<User> ul = userService.getUserList2(exInfo.getServiceBaseId());
						if(ul != null) {
							exInfo.setPersonName(ul.get(0).getName());
							exInfo.setPersonPhone(ul.get(0).getPhone());
						}else {
							exInfo.setPersonName("");
							exInfo.setPersonPhone("");
						}
						
					}
				}
			}
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.success(page));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	/**
	 *id查询单个详细信息
	 * @author 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getExpertServiceInfoById",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "id查询单个详细信息", httpMethod = "GET", notes = "id查询单个详细信息",	consumes="application/x-www-form-urlencoded")
	public String getExpertServiceInfoById(HttpServletRequest request,HttpServletResponse response,String id) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			ExpertServiceInfo expertServiceInfo = expertsServiceInfoService.get(id);
			expertServiceInfo.setServicePeopleName(userService.get(expertServiceInfo.getExpertId()).getName());
			expertServiceInfo.setCompanyName(officeService.get(expertServiceInfo.getServiceCompanyId()).getName());//公司名称
			expertServiceInfo.setFileList(fileInfoService.find(expertServiceInfo.getId(),"1"));
			expertServiceInfo.setAuditList(fileInfoService.find(expertServiceInfo.getId(),"4"));
			String strtype ="";
			if(expertServiceInfo.getServiceType().equals("1")) {
				strtype = "收集样本";
			}else if(expertServiceInfo.getServiceType().equals("2")) {
				strtype = "咨询服务";
			}else if(expertServiceInfo.getServiceType().equals("3")) {
				strtype = "技术支持";
			}
			else if(expertServiceInfo.getServiceType().equals("4")) {
				strtype = "例行服务";
			}else if(expertServiceInfo.getServiceType().equals("5")) {
				strtype = "其他";
			}
			expertServiceInfo.setStrtype(strtype);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(expertServiceInfo));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

	@RequestMapping(value = "delExpertsServiceInfo", produces = "text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "删除服务记录", httpMethod = "POST", notes = "删除服务记录", consumes = "application/json")
	public String delExpertsServiceInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam String id) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User user = apiUserService.getUserByToken(token);
			expertsServiceInfoService.delExpertsServiceInfo(user, id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/getExpertServiceListAPP",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ApiOperation(value = "平台管理员获取平台及下级公司的服务记录", httpMethod = "GET", notes = "平台管理员获取平台及下级公司的服务记录", consumes = "application/x-www-form-urlencoded")
	public String getExpertServiceListAPP(HttpServletRequest request,@RequestParam Integer pageno, @RequestParam Integer pagesize) {
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
			Page<ExpertServiceInfo> page = new Page<ExpertServiceInfo>(pageNo, pageSize);
			System.out.println("user.getCompany().getId()=="+user.getCompany().getId());
			page = expertsServiceInfoService.getExpertServiceListAPP(page,user.getCompany().getId());
			List<ExpertServiceInfo> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<ExpertServiceInfo> iterator = list.iterator(); iterator.hasNext();) {
					ExpertServiceInfo exInfo = (ExpertServiceInfo) iterator.next();
					exInfo.setExpertName(userService.get(exInfo.getExpertId()).getName());
					Office office = officeService.get(exInfo.getServiceCompanyId());
					exInfo.setCompanyName(office.getName());//公司名称
					exInfo.setFileList(fileInfoService.find(exInfo.getId(),"1"));
					exInfo.setAuditList(fileInfoService.find(exInfo.getId(),"4"));
					if(StringUtils.isNotBlank(exInfo.getServiceBaseId())) {
						exInfo.setBaseName(baseManageApiService.getbaseById(exInfo.getServiceBaseId()).get(0).getName());
						List<User> ul = userService.getUserList2(exInfo.getServiceBaseId());
						exInfo.setPersonName(ul.get(0).getName());
						exInfo.setPersonPhone(ul.get(0).getPhone());
					}
				}
			}
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.success(page));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

}
