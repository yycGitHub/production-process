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
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.application.entity.ApplicationRecordVO;
import com.surekam.modules.agro.application.service.ApplicationRecordVOService;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.file.service.AgroFileInfoService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.ApiOperation;

/**
 * 农户申请记录Controller
 * @author xy
 * @version 2019-06-25
 */
@Controller
@RequestMapping(value = "api/applicationRecordVO")
public class ApplicationRecordVOController extends BaseController{
	
	@Autowired
	private ApplicationRecordVOService applicationRecordVOService;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@Autowired
	private ExpertsService expertsService;
	
	@Autowired
	private AgroFileInfoService fileInfoService;
	
	@RequestMapping(value = "getApplicationRecordVOList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "农户申请记录列表", notes = "农户申请记录列表", consumes = "application/json")
	public String getApplicationRecordVOList(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) String itemId,
			@RequestParam(required = false) String expertName,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		try {
			User user = apiUserService.getUserByToken(token);
			Page<ApplicationRecordVO> page = new Page<ApplicationRecordVO>(pageNo, pageSize);
			page = applicationRecordVOService.find(page, itemId,user.getCompany().getId(),expertName);
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	
	@RequestMapping(value = "getExpertTaskList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "专家待办任务列表", notes = "专家待办任务列表", consumes = "application/json")
	public String getExpertTaskList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			Experts expert = expertsService.getExpertByUserId(user.getId());
			try {
				
				Page<ApplicationRecordVO> page = new Page<ApplicationRecordVO>(pageNo, pageSize);
				page = applicationRecordVOService.findExpertTaskList(page ,expert);
				return jsonMapper.toJson(ResultUtil.success(page));
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
	
	@RequestMapping(value = "getExpertTaskList2", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "专家已完成任务列表", notes = "专家已完成任务列表", consumes = "application/json")
	public String getExpertTaskList2(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			Experts expert = expertsService.getExpertByUserId(user.getId());
			try {
				
				Page<ApplicationRecordVO> page = new Page<ApplicationRecordVO>(pageNo, pageSize);
				page = applicationRecordVOService.findExpertTaskList2(page, expert);
				return jsonMapper.toJson(ResultUtil.success(page));
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
	

	
	@RequestMapping(value = "getTaskById", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询单个任务详细", notes = "查询单个任务详细", consumes = "application/json")
	public String getTaskById(HttpServletRequest request,HttpServletResponse response,@RequestParam String id,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			try {
				Page<ApplicationRecordVO> page = new Page<ApplicationRecordVO>(pageNo, pageSize);
				page = applicationRecordVOService.getTaskById(page,id);
				List<ApplicationRecordVO> list = page.getList();
				if (page.getList().size() > 0) {
					for (Iterator<ApplicationRecordVO> iterator = list.iterator(); iterator.hasNext();) {
						ApplicationRecordVO applicationRecordVO = (ApplicationRecordVO) iterator.next();
						applicationRecordVO.setFileList(fileInfoService.find(applicationRecordVO.getId(),"1"));
						applicationRecordVO.setAuditList(fileInfoService.find(applicationRecordVO.getId(),"4"));
					}
				}
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
	
	
	@RequestMapping(value = "getAllCountNH", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取任务数量", httpMethod = "GET", notes = "获取任务数量", consumes = "application/x-www-form-urlencoded")
	public String getAllCountNH(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String baseId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			String notFinishCount = applicationRecordVOService.getNotCount(user);
			String finishCount = applicationRecordVOService.getCount(user);
			map.put("notFinishCount", notFinishCount);
			map.put("finishCount", finishCount);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	@RequestMapping(value = "getNhNotTaskList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "农户申请未处理列表", notes = "农户申请未处理列表", consumes = "application/json")
	public String getNhNotTaskList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			try {
				Page<ApplicationRecordVO> page = new Page<ApplicationRecordVO>(pageNo, pageSize);
				page = applicationRecordVOService.getNhNotTaskList(page, user);
				return jsonMapper.toJson(ResultUtil.success(page));
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
	
	
	@RequestMapping(value = "getNhTaskList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "农户申请已处理列表", notes = "农户申请已处理列表", consumes = "application/json")
	public String getNhTaskList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			try {
				Page<ApplicationRecordVO> page = new Page<ApplicationRecordVO>(pageNo, pageSize);
				page = applicationRecordVOService.getNhTaskList(page, user);
				return jsonMapper.toJson(ResultUtil.success(page));
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
	
	
	@RequestMapping(value = "getAllCount", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取任务数量", httpMethod = "GET", notes = "获取任务数量", consumes = "application/x-www-form-urlencoded")
	public String getAllCount(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String baseId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			Experts expert = expertsService.getExpertByUserId(user.getId());
			String notFinishCount = applicationRecordVOService.getAllCountnot(expert);
			String finishCount = applicationRecordVOService.getAllCount(expert);
			map.put("notFinishCount", notFinishCount);
			map.put("finishCount", finishCount);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	
	@RequestMapping(value = "getListAPP", produces="application/json;charset=UTF-8",method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "app-平台管理员查询申请记录",httpMethod = "GET", notes = "app-平台管理员查询申请记录", consumes = "application/x-www-form-urlencoded")
	public String getListAPP(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) String itemId,
			@RequestParam(required = false) String expertName,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		try {
			User user = apiUserService.getUserByToken(token);
			Page<ApplicationRecordVO> page = new Page<ApplicationRecordVO>(pageNo, pageSize);
			page = applicationRecordVOService.find(page, itemId,user.getCompany().getId(),expertName);
			List<ApplicationRecordVO> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<ApplicationRecordVO> iterator = list.iterator(); iterator.hasNext();) {
					ApplicationRecordVO applicationRecordVO = (ApplicationRecordVO) iterator.next();
					applicationRecordVO.setFileList(fileInfoService.find(applicationRecordVO.getId(),"1"));
				}
			}
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
}
