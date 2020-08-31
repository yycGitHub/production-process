package com.surekam.modules.api.web;

import java.util.ArrayList;
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
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.application.entity.ApplicationRecordVO;
import com.surekam.modules.agro.file.service.AgroFileInfoService;
import com.surekam.modules.agro.technicalreport.entity.ReportReviewVo;
import com.surekam.modules.agro.technicalreport.entity.ReportReviewVoReq;
import com.surekam.modules.agro.technicalreport.entity.Reporting;
import com.surekam.modules.agro.technicalreport.service.ReportReviewVoService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.UserService;

import io.swagger.annotations.ApiOperation;

/**
 * 汇报评审视图Controller
 * @author xy
 * @version 2019-07-12
 */
@Controller
@RequestMapping(value = "api/reportReviewVo")
public class ReportReviewVoController extends BaseController{
	
	@Autowired
	private ReportReviewVoService reportReviewVoService;
	
	@Autowired
	private AgroFileInfoService fileInfoService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApiUserService apiUserService;
	
	/**
	 * web端查询汇报记录
	 * @param request
	 * @param response
	 * @param reportingType
	 * @param startDate
	 * @param endDate
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getReportReviewVoList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "工作汇报列表", notes = "工作汇报列表", consumes = "application/json")
	public String getReportReviewVoList(HttpServletRequest request,HttpServletResponse response,String reportingType,String startDate,String endDate,
			String auditStatus,String userId,@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		try {
			User user = apiUserService.getUserByToken(token);
			String reportUserId = "";
			if (user.isAdmin()) {
				
			}else {
				reportUserId = user.getId();
			}
			Page<ReportReviewVo> page = new Page<ReportReviewVo>(pageNo, pageSize);
			page = reportReviewVoService.find(page, reportingType,startDate,endDate,reportUserId,auditStatus,userId);
			List<ReportReviewVo> list = page.getList();
			Page<ReportReviewVoReq> page2 = new Page<ReportReviewVoReq>(pageNo, pageSize);
			page2.setCount(page.getCount());
			if (page.getList().size() > 0) {
				List<ReportReviewVoReq> list2 = new ArrayList<ReportReviewVoReq>();
				for (Iterator<ReportReviewVo> iterator = list.iterator(); iterator.hasNext();) {
					ReportReviewVo reportReviewVo = (ReportReviewVo) iterator.next();
					ReportReviewVoReq reportReviewVoReq = new ReportReviewVoReq();
					
					reportReviewVoReq.setId(reportReviewVo.getId());
					reportReviewVoReq.setWorkDone(reportReviewVo.getWorkDone());
					reportReviewVoReq.setReportUserId(reportReviewVo.getReportUserId());
					reportReviewVoReq.setReportDate(reportReviewVo.getReportDate());
					reportReviewVoReq.setOfficeId(reportReviewVo.getOfficeId());
					reportReviewVoReq.setBaseId(reportReviewVo.getBaseId());
					reportReviewVoReq.setReportingType(reportReviewVo.getReportingType());
					reportReviewVoReq.setAttachments(reportReviewVo.getAttachments());
					reportReviewVoReq.setAuditStatus(reportReviewVo.getAuditStatus());
					reportReviewVoReq.setWstates(reportReviewVo.getWstates());
					reportReviewVoReq.setWid(reportReviewVo.getWid());
					reportReviewVoReq.setWreview(reportReviewVo.getWreview());
					reportReviewVoReq.setWreviewTime(reportReviewVo.getWreviewTime());
					reportReviewVoReq.setWreportUserId(reportReviewVo.getWreportUserId());
					reportReviewVoReq.setWreviewStatus(reportReviewVo.getWreviewStatus());
					reportReviewVoReq.setWofficeId(reportReviewVo.getWofficeId());
					reportReviewVoReq.setWbaseId(reportReviewVo.getWbaseId());
					reportReviewVoReq.setCreateUserName(reportReviewVo.getCreateUserName());
					reportReviewVoReq.setPhotoList(fileInfoService.find(reportReviewVo.getId(),"1"));
					reportReviewVoReq.setReportUserName(userService.get(reportReviewVo.getReportUserId()).getName());
					if(reportReviewVo.getReportingType().equals("4")) {
						reportReviewVoReq.setPlannedWork(" ");
						reportReviewVoReq.setOtherMatters(" ");
					}else {
						reportReviewVoReq.setPlannedWork(reportReviewVo.getPlannedWork());
						reportReviewVoReq.setOtherMatters(reportReviewVo.getOtherMatters());
					}
					if(reportReviewVo.getWscore() == null) {
						reportReviewVoReq.setWscore(" ");
					}else {
						reportReviewVoReq.setWscore(reportReviewVo.getWscore());
					}
					list2.add(reportReviewVoReq);
				}
				page2.setList(list2);
			}
			System.out.println(jsonMapper.toJson(ResultUtil.success(page2)));
			return jsonMapper.toJson(ResultUtil.success(page2));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	/**
	 * web端查询周报记录列表
	 * @param request
	 * @param response
	 * @param reportingType
	 * @param startDate
	 * @param endDate
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getWeeklyList2", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "工作汇报列表", notes = "工作汇报列表", consumes = "application/json")
	public String getWeeklyList(HttpServletRequest request,HttpServletResponse response,String reportingType,String startDate,String endDate,
			String auditStatus,String userId,@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		try {
			User user = apiUserService.getUserByToken(token);
			String reportUserId = "";
			if (user.isAdmin()) {
			}else {
				reportUserId = user.getId();
			}	
			Page<ReportReviewVo> page = new Page<ReportReviewVo>(pageNo, pageSize);
			page = reportReviewVoService.find(page, "2",startDate,endDate,reportUserId,auditStatus,userId);
			List<ReportReviewVo> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<ReportReviewVo> iterator = list.iterator(); iterator.hasNext();) {
					ReportReviewVo reportReviewVo = (ReportReviewVo) iterator.next();
					reportReviewVo.setPhotoList(fileInfoService.find(reportReviewVo.getId(),"1"));
					reportReviewVo.setReportUserName(userService.get(reportReviewVo.getReportUserId()).getName());
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
	
	/**
	 * web端查看详细
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getReportById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询单个汇报信息详细", notes = "查询单个汇报信息详细", consumes = "application/json")
	public String getReportById(HttpServletRequest request,HttpServletResponse response,@RequestParam String id) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = 1;
		int pageSize = 10;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			try {
				Page<ReportReviewVo> page = new Page<ReportReviewVo>(pageNo, pageSize);
				page = reportReviewVoService.getReportById(page,id);
				List<ReportReviewVo> list = page.getList();
				if (page.getList().size() > 0) {
					for (Iterator<ReportReviewVo> iterator = list.iterator(); iterator.hasNext();) {
						ReportReviewVo reportReviewVo = (ReportReviewVo) iterator.next();
						reportReviewVo.setPhotoList(fileInfoService.find(reportReviewVo.getId(),"1"));
						reportReviewVo.setFileList(fileInfoService.find(reportReviewVo.getId(),"2"));
						reportReviewVo.setVideoList(fileInfoService.find(reportReviewVo.getId(),"3"));
						reportReviewVo.setAuditList(fileInfoService.find(reportReviewVo.getId(),"4"));
						reportReviewVo.setReportUserName(userService.get(reportReviewVo.getReportUserId()).getName());
					}
				}
				System.out.println(jsonMapper.toJson(ResultUtil.success(page)));
				if(page.getList().size() > 0) {
					return jsonMapper.toJson(ResultUtil.success(page.getList().get(0)));
				}else {
					return jsonMapper.toJson(ResultUtil.success(new ApplicationRecordVO()));
				}
			} catch (Exception e) {
				logger.error("用户列表查询错误：" + e);
				e.printStackTrace();
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	/**
	 * 手机端专家查看自己的汇报记录列表
	 * @param request
	 * @param response
	 * @param reportingType
	 * @param startDate
	 * @param endDate
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getMyReportList",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "自己的工作汇报列表", httpMethod = "GET", notes = "自己的工作汇报列表",	consumes="application/x-www-form-urlencoded")
	public String getMyReportList(HttpServletRequest request,HttpServletResponse response,String reportingType,String startDate,String endDate,
			String reportUserId,String auditStatus,@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		try {
			User user = apiUserService.getUserByToken(token);
			
			Page<ReportReviewVo> page = new Page<ReportReviewVo>(pageNo, pageSize);
			page = reportReviewVoService.findMyList(page, reportingType,startDate,endDate,user.getId(),reportUserId,auditStatus);
			System.out.println(jsonMapper.toJson(ResultUtil.success(page)));
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

}
