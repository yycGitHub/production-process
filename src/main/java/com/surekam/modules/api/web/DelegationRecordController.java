package com.surekam.modules.api.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.application.entity.ApplicationRecord;
import com.surekam.modules.agro.application.entity.DelegationRecord;
import com.surekam.modules.agro.application.entity.OperationalRecords;
import com.surekam.modules.agro.application.service.ApplicationRecordService;
import com.surekam.modules.agro.application.service.DelegationRecordService;
import com.surekam.modules.agro.application.service.OperationalRecordsService;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.ApiSystemService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 平台委派记录Controller
 * @author xy
 * @version 2019-06-25
 */
@Controller
@RequestMapping(value = "api/delegation")
public class DelegationRecordController extends BaseController{
	
	@Autowired
	private DelegationRecordService delegationRecordService;
	
	@Autowired
	private ApiSystemService apiSystemService;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@Autowired
	private ExpertsService expertsService;
	
	@Autowired
	private ApplicationRecordService applicationRecordService;
	
	@Autowired
	private OperationalRecordsService operationalRecordsService;
	
	
	@RequestMapping(value = "getDelegationRecordRecordList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "平台委派记录列表", notes = "平台委派记录列表", consumes = "application/json")
	public String getDelegationRecordRecordList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			
			Page<DelegationRecord> page = new Page<DelegationRecord>(pageNo, pageSize);
			page = delegationRecordService.find(page,"");
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
		@RequestMapping(value = "saveDelegationRecordRecord", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
		@ResponseBody
		@ApiOperation(value = "保存平台委派记录信息", httpMethod = "POST", notes = "保存平台委派记录信息", consumes = "application/json")
	    public String saveDelegationRecordRecord(HttpServletRequest request,HttpServletResponse response,@RequestBody DelegationRecord delegationRecord) {
	    	response.setContentType("application/json; charset=UTF-8");
			JsonMapper jsonMapper = JsonMapper.getInstance();
			ResultBean<DelegationRecord> resultBean = new ResultBean<DelegationRecord>();
			String token = request.getHeader("X-Token");
			try {
				User currentUser = apiSystemService.findUserByToken(token);
				if(StringUtils.isNotBlank(delegationRecord.getId())) {//id不为空则是重新委派专家，需要删除之前的记录
					DelegationRecord delegationRecord3 = delegationRecordService.get(delegationRecord.getId());
					delegationRecord3.setStates("D");
					delegationRecord3.setAcceptStatus("1");
					delegationRecord3.setExplainNo("平台更换委派专家");
					delegationRecordService.save(delegationRecord3);
					
					DelegationRecord delegationRecord2 = new  DelegationRecord();
					delegationRecord2.setRemark(delegationRecord.getRemark());
					delegationRecord2.setExpertId(delegationRecord.getExpertId());
					delegationRecord2.setApplicationId(delegationRecord.getApplicationId());
					//delegationRecord2.setExplainNo("平台重新委派专家");
					delegationRecord2.setCreateTime(new Date());
					delegationRecord2.setCreateUserId(currentUser.getId());
					delegationRecord2.setAcceptStatus("0");
					delegationRecord2.setTaskStatus("0");
					delegationRecordService.save(delegationRecord2);
					
					OperationalRecords operationalRecords = new OperationalRecords();
					operationalRecords.setArid(delegationRecord.getApplicationId());
					operationalRecords.setHandle("委派专家");
					operationalRecords.setYwid(delegationRecord2.getId());
					operationalRecords.setType("2");
					operationalRecords.setCreateTime(new Date());
					operationalRecords.setCreateUserId(currentUser.getId());
					operationalRecords.setStates("A");
					operationalRecordsService.save(operationalRecords);
				
					if(StringUtils.isNotBlank(delegationRecord.getAppointmentTime())) {
						ApplicationRecord applicationRecord =  applicationRecordService.get(delegationRecord2.getApplicationId());
						applicationRecord.setAppointmentTime(delegationRecord.getAppointmentTime());
						applicationRecord.setUpdateTime(new Date());
						applicationRecord.setUpdateUserId(currentUser.getId());
						applicationRecordService.save(applicationRecord);
					}
				}else {//id为空则为第一次委派
					delegationRecord.setCreateTime(new Date());
					delegationRecord.setCreateUserId(currentUser.getId());
					delegationRecord.setAcceptStatus("0");
					delegationRecord.setTaskStatus("0");
					delegationRecordService.save(delegationRecord);
					
					OperationalRecords operationalRecords = new OperationalRecords();
					operationalRecords.setArid(delegationRecord.getApplicationId());
					operationalRecords.setHandle("委派专家");
					operationalRecords.setYwid(delegationRecord.getId());
					operationalRecords.setType("2");
					operationalRecords.setCreateTime(new Date());
					operationalRecords.setCreateUserId(currentUser.getId());
					operationalRecords.setStates("A");
					operationalRecordsService.save(operationalRecords);
					
					if(StringUtils.isNotBlank(delegationRecord.getAppointmentTime())) {
						ApplicationRecord applicationRecord =  applicationRecordService.get(delegationRecord.getApplicationId());
						applicationRecord.setAppointmentTime(delegationRecord.getAppointmentTime());
						applicationRecord.setUpdateTime(new Date());
						applicationRecord.setUpdateUserId(currentUser.getId());
						applicationRecordService.save(applicationRecord);
					}
				}
				return jsonMapper.toJson(ResultUtil.success());
			} catch (Exception e) {
				logger.error("保存信息错误：" + e);
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
	    }
		
		
		@RequestMapping(value = "refusalOfTasks",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
		@ResponseBody
		@ApiOperation(value = "app-拒绝委派任务", httpMethod = "POST", notes = "app-拒绝委派任务",	consumes="application/json")
		public String refusalOfTasks(HttpServletRequest request,HttpServletResponse response,String delegationId,String explainNo) {
			response.setContentType("application/json; charset=UTF-8");
			String token = request.getHeader("X-Token");
			JsonMapper jsonMapper = JsonMapper.getInstance();
			if(StringUtils.isNotBlank(token)){
				User currentUser = apiSystemService.findUserByToken(token);
				DelegationRecord delegationRecord = delegationRecordService.get(delegationId);
				delegationRecord.setExplainNo(explainNo);
				delegationRecord.setUpdateTime(new Date());
				delegationRecord.setUpdateUserId(currentUser.getId());
				delegationRecord.setAcceptStatus("1");
				delegationRecord.setTaskStatus("2");
				delegationRecord.setStates("D");
				delegationRecordService.save(delegationRecord);
				//TODO 拒绝操作暂不加入记录
				return jsonMapper.toJson(ResultUtil.success());
			} else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		}
		
		
		
		@RequestMapping(value = "getCount", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
		@ResponseBody
		@ApiOperation(value = "获取专家的委派任务数量", httpMethod = "GET", notes = "获取专家的委派任务数量", consumes = "application/x-www-form-urlencoded")
		public String getCount(HttpServletRequest request, HttpServletResponse response) {
			Map<String, Object> map = new HashMap<String, Object>();
			String token = request.getHeader("X-Token");
			if (token != null) {
				User user = apiUserService.getUserByToken(token);
				Experts expert = expertsService.getExpertByUserId(user.getId());
				String notFinishCount = delegationRecordService.getAllCount("0",expert);
				map.put("notFinishCount", notFinishCount);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
			} else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		}	

		/**
		 * 申请记录id查询委派记录
		 * @param request
		 * @param response
		 * @param applicationId
		 * @param pageno
		 * @param pagesize
		 * @return
		 */
		@RequestMapping(value = "getListById", produces = "application/json;charset=UTF-8")
		@ResponseBody
		@ApiOperation(value = "平台委派记录列表", notes = "平台委派记录列表", consumes = "application/json")
		public String getListById(HttpServletRequest request,HttpServletResponse response,String applicationId,@RequestParam Integer pageno, @RequestParam Integer pagesize) {
			response.setContentType("application/json; charset=UTF-8");
			int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
			int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
			JsonMapper jsonMapper = JsonMapper.getInstance();
			ResultBean<Object> resultBean = new ResultBean<Object>();
			try {
				
				Page<DelegationRecord> page = new Page<DelegationRecord>(pageNo, pageSize);
				page = delegationRecordService.find(page, applicationId);
				
				List<DelegationRecord> list = page.getList();
				if (page.getList().size() > 0) {
					for (Iterator<DelegationRecord> iterator = list.iterator(); iterator.hasNext();) {
						DelegationRecord delegationRecord = (DelegationRecord) iterator.next();
						Experts expert = expertsService.get(delegationRecord.getExpertId());
						if(expert != null) {
							delegationRecord.setExpertName(expert.getExpertName());
						}
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
		
		
		
		@RequestMapping(value = "saveDelegationRecordRecord2", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
		@ResponseBody
		@ApiOperation(value = "保存平台委派记录信息", httpMethod = "POST", notes = "保存平台委派记录信息", consumes = "application/x-www-form-urlencoded")
		public String saveDelegationRecordRecord2(HttpServletRequest request,HttpServletResponse response,
				@RequestBody @ApiParam(name = "新增基地信息", value = "传入json格式", required = true) DelegationRecord delegationRecord) {
			response.setContentType("application/json; charset=UTF-8");
			JsonMapper jsonMapper = JsonMapper.getInstance();
			ResultBean<DelegationRecord> resultBean = new ResultBean<DelegationRecord>();
			String token = request.getHeader("X-Token");
			try {
				User currentUser = apiSystemService.findUserByToken(token);
				if(StringUtils.isNotBlank(delegationRecord.getId())) {//id不为空则是重新委派专家，需要删除之前的记录
					DelegationRecord delegationRecord3 = delegationRecordService.get(delegationRecord.getId());
					delegationRecord3.setStates("D");
					delegationRecord3.setAcceptStatus("1");
					delegationRecord3.setExplainNo("平台更换委派专家");
					delegationRecordService.save(delegationRecord3);
					
					DelegationRecord delegationRecord2 = new  DelegationRecord();
					delegationRecord2.setRemark(delegationRecord.getRemark());
					delegationRecord2.setExpertId(delegationRecord.getExpertId());
					delegationRecord2.setApplicationId(delegationRecord.getApplicationId());
					//delegationRecord2.setExplainNo("平台重新委派专家");
					delegationRecord2.setCreateTime(new Date());
					delegationRecord2.setCreateUserId(currentUser.getId());
					delegationRecord2.setAcceptStatus("0");
					delegationRecord2.setTaskStatus("0");
					delegationRecordService.save(delegationRecord2);
					
					OperationalRecords operationalRecords = new OperationalRecords();
					operationalRecords.setArid(delegationRecord.getApplicationId());
					operationalRecords.setHandle("委派专家");
					operationalRecords.setYwid(delegationRecord2.getId());
					operationalRecords.setType("2");
					operationalRecords.setCreateTime(new Date());
					operationalRecords.setCreateUserId(currentUser.getId());
					operationalRecords.setStates("A");
					operationalRecordsService.save(operationalRecords);
				
					if(StringUtils.isNotBlank(delegationRecord.getAppointmentTime())) {
						ApplicationRecord applicationRecord =  applicationRecordService.get(delegationRecord2.getApplicationId());
						applicationRecord.setAppointmentTime(delegationRecord.getAppointmentTime());
						applicationRecord.setUpdateTime(new Date());
						applicationRecord.setUpdateUserId(currentUser.getId());
						applicationRecordService.save(applicationRecord);
					}
				}else {//id为空则为第一次委派
					delegationRecord.setCreateTime(new Date());
					delegationRecord.setCreateUserId(currentUser.getId());
					delegationRecord.setAcceptStatus("0");
					delegationRecord.setTaskStatus("0");
					delegationRecordService.save(delegationRecord);
					
					OperationalRecords operationalRecords = new OperationalRecords();
					operationalRecords.setArid(delegationRecord.getApplicationId());
					operationalRecords.setHandle("委派专家");
					operationalRecords.setYwid(delegationRecord.getId());
					operationalRecords.setType("2");
					operationalRecords.setCreateTime(new Date());
					operationalRecords.setCreateUserId(currentUser.getId());
					operationalRecords.setStates("A");
					operationalRecordsService.save(operationalRecords);
					
					if(StringUtils.isNotBlank(delegationRecord.getAppointmentTime())) {
						ApplicationRecord applicationRecord =  applicationRecordService.get(delegationRecord.getApplicationId());
						applicationRecord.setAppointmentTime(delegationRecord.getAppointmentTime());
						applicationRecord.setUpdateTime(new Date());
						applicationRecord.setUpdateUserId(currentUser.getId());
						applicationRecordService.save(applicationRecord);
					}
				}
				return jsonMapper.toJson(ResultUtil.success(" "));
			} catch (Exception e) {
				logger.error("保存信息错误：" + e);
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		}
}
