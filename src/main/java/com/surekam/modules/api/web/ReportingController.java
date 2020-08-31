package com.surekam.modules.api.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.file.service.AgroFileInfoService;
import com.surekam.modules.agro.technicalreport.entity.Reporting;
import com.surekam.modules.agro.technicalreport.service.ReportingService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.ApiSystemService;
import com.surekam.modules.sys.service.UserService;

import io.swagger.annotations.ApiOperation;


/**
 * 汇报信息Controller
 * @author xy
 * @version 2019-07-09
 */
@Controller
@RequestMapping(value = "api/reporting")
public class ReportingController extends BaseController{
	
	@Autowired
	private ReportingService reportingService;
	
	@Autowired
	private ApiSystemService apiSystemService;
	
	@Autowired
	private AgroFileInfoService fileInfoService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApiUserService apiUserService;
	
	
	@RequestMapping(value = "getReportingList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "工作汇报列表", notes = "工作汇报列表", consumes = "application/json")
	public String getReportingList(HttpServletRequest request,HttpServletResponse response,String reportingType,String startDate,String endDate,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		
		response.setContentType("application/json; charset=UTF-8");
		
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		try {
			User user = apiUserService.getUserByToken(token);
			String userId = "";
			if (user.isAdmin()) {
				
			}else {
				userId = user.getId();
			}
				
				
			Page<Reporting> page = new Page<Reporting>(pageNo, pageSize);
			page = reportingService.find(page, reportingType,startDate,endDate,userId);
			List<Reporting> list = page.getList();
			
			if (page.getList().size() > 0) {
				for (Iterator<Reporting> iterator = list.iterator(); iterator.hasNext();) {
					Reporting reporting = (Reporting) iterator.next();
					/*if(reporting.getAttachments().equals("0")) {
						reporting.setFileList(fileInfoService.find(reporting.getId(),"2"));
					}
					reporting.setPhotoList(fileInfoService.find(reporting.getId(),"1"));*/
					reporting.setCreateUserName(userService.get(reporting.getCreateUserId()).getName());
				}
			}
			
			System.out.println(jsonMapper.toJson(ResultUtil.success(page)));
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@RequestMapping(value = "getWeeklyList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "工作汇报列表", notes = "工作汇报列表", consumes = "application/json")
	public String getWeeklyList(HttpServletRequest request,HttpServletResponse response,String reportingType,String startDate,String endDate,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		try {
			User user = apiUserService.getUserByToken(token);
			String userId = "";
			if (user.isAdmin()) {
			}else {
				userId = user.getId();
			}	
			Page<Reporting> page = new Page<Reporting>(pageNo, pageSize);
			page = reportingService.find(page, "2",startDate,endDate,userId);
			List<Reporting> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<Reporting> iterator = list.iterator(); iterator.hasNext();) {
					Reporting reporting = (Reporting) iterator.next();
					if(reporting.getAttachments().equals("0")) {
						reporting.setFileList(fileInfoService.find(reporting.getId(),"2"));
					}
					reporting.setPhotoList(fileInfoService.find(reporting.getId(),"1"));
					reporting.setCreateUserName(userService.get(reporting.getCreateUserId()).getName());
				}
			}
			System.out.println(jsonMapper.toJson(ResultUtil.success(page)));
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@RequestMapping(value = "saveReporting",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "app-保存汇报信息", httpMethod = "POST", notes = "app-保存汇报信息",	consumes="application/json")
	public String saveReporting(HttpServletRequest request,HttpServletResponse response,String workDone,
			String plannedWork,String otherMatters,String reportUserId,String reportDate,String address,String longitude,String latitude,String reportingType,
			String [] photo,String [] fileUrl) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User currentUser = apiSystemService.findUserByToken(token);
			reportingService.saveReporting(currentUser,workDone,plannedWork,otherMatters,reportUserId,reportDate,address,longitude,latitude,reportingType,photo,fileUrl);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@RequestMapping(value = "saveUpdataReporting",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "app-编辑保存汇报信息", httpMethod = "POST", notes = "app-编辑保存汇报信息",	consumes="application/json")
	public String saveUpdataReporting(HttpServletRequest request,HttpServletResponse response,String id,String workDone,
			String plannedWork,String otherMatters,String reportUserId,String reportDate,String address,String longitude,String latitude,String reportingType,
			String [] photo,String [] fileUrl) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User currentUser = apiSystemService.findUserByToken(token);
			reportingService.saveUpdataReporting(currentUser,id,workDone,plannedWork,otherMatters,reportUserId,reportDate,address,longitude,latitude,reportingType,photo,fileUrl);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 *  手机端，平台管理人员查询
	 * @param request
	 * @param response
	 * @param reportingType
	 * @param startDate
	 * @param endDate
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getReportingListTwo", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "工作汇报列表", notes = "工作汇报列表", consumes = "application/json")
	public String getReportingListTwo(HttpServletRequest request,HttpServletResponse response,
			String reportingType,String startDate,String endDate,String auditStatus,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		try {
			User user = apiUserService.getUserByToken(token);
			String userId = "";
			if(user != null) {
				userId = user.getId();
			}
			Page<Reporting> page = new Page<Reporting>(pageNo, pageSize);
			page = reportingService.getReportingListTwo(page, reportingType,startDate,endDate,userId,auditStatus);
			List<Reporting> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<Reporting> iterator = list.iterator(); iterator.hasNext();) {
					Reporting reporting = (Reporting) iterator.next();
					reporting.setPhotoList(fileInfoService.find(reporting.getId(),"1"));
					reporting.setCreateUserName(userService.get(reporting.getCreateUserId()).getName());
				}
			}
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("平台管理用户查询技术日志错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@RequestMapping(value = "getReportCount", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "平台管理用户获取技术日志数量", httpMethod = "GET", notes = "平台管理用户获取技术日志数量", consumes = "application/x-www-form-urlencoded")
	public String getReportCount(HttpServletRequest request, HttpServletResponse response,
			String reportingType,String startDate,String endDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			String userId = "";
			if(user != null) {
				userId = user.getId();
			}
			long notFinishCount = reportingService.getReportingListTwoCount(reportingType,startDate,endDate,userId,"0");
			long finishCount = reportingService.getReportingListTwoCount(reportingType,startDate,endDate,userId,"1");
			map.put("notFinishCount", notFinishCount);
			map.put("finishCount", finishCount);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

}
