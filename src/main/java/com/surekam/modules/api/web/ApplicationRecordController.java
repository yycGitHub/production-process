package com.surekam.modules.api.web;

import java.util.Date;

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
import com.surekam.modules.agro.application.service.ApplicationRecordService;
import com.surekam.modules.agro.file.service.AgroFileInfoService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.UserVo;
import com.surekam.modules.sys.service.ApiSystemService;

import io.swagger.annotations.ApiOperation;

/**
 * 农户申请记录Controller
 * @author xy
 * @version 2019-06-25
 */
@Controller
@RequestMapping(value = "api/applicationRecord")
public class ApplicationRecordController extends BaseController{
	
	@Autowired
	private ApplicationRecordService applicationRecordService;
	
	@Autowired
	private ApiSystemService apiSystemService;
	
	@Autowired
	private AgroFileInfoService fileInfoService;
	
	@RequestMapping(value = "getApplicationRecordList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "农户申请记录列表", notes = "农户申请记录列表", consumes = "application/json")
	public String getApplicationRecordList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			
			Page<ApplicationRecord> page = new Page<ApplicationRecord>(pageNo, pageSize);
			page = applicationRecordService.find(page, new ApplicationRecord());
			System.out.println(jsonMapper.toJson(ResultUtil.success(page)));
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
		@RequestMapping(value = "saveApplicationRecord", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
		@ResponseBody
		@ApiOperation(value = "保存农户申请记录信息", httpMethod = "POST", notes = "保存农户申请记录信息", consumes = "application/json")
	    public String saveApplicationRecord(HttpServletRequest request,HttpServletResponse response,@RequestBody ApplicationRecord applicationRecord) {
	    	response.setContentType("application/json; charset=UTF-8");
			JsonMapper jsonMapper = JsonMapper.getInstance();
			ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
			String token = request.getHeader("X-Token");
			try {
				User currentUser = apiSystemService.findUserByToken(token);
				applicationRecord.setCreateTime(new Date());
				applicationRecord.setCreateUserId(currentUser.getId());
				applicationRecordService.save(applicationRecord);
				return jsonMapper.toJson(ResultUtil.success());
			} catch (Exception e) {
				logger.error("保存信息错误：" + e);
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
	    }
		
		@RequestMapping(value = "saveApplicationRecord2",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
		@ResponseBody
		@ApiOperation(value = "app-保存农户申请记录信息", httpMethod = "POST", notes = "app-保存农户申请记录信息",	consumes="application/json")
		public String saveApplicationRecord2(HttpServletRequest request,HttpServletResponse response,String phoneNumber,
				String detailedQuestions,String [] photo,String appointmentTime,String contacts,
				String address,String [] auditUrl,String batchId,String longitude,String latitude,String expertId,String expertName) {
			String token = request.getHeader("X-Token");
			if(StringUtils.isNotBlank(token)){
				User currentUser = apiSystemService.findUserByToken(token);
				applicationRecordService.saveApplicationRecord2(currentUser,detailedQuestions,appointmentTime,phoneNumber,address,contacts,photo,auditUrl,batchId,longitude,latitude,expertId,expertName);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
			} else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		}
		
		/**
		 *  申请记录附件查询
		 * @param id
		 * @return
		 */
		@RequestMapping(value = "queryFile",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
		@ResponseBody
		@ApiOperation(value = "申请记录附件查询", httpMethod = "POST", notes = "申请记录附件查询",	consumes="application/json")
		public String queryFile(HttpServletRequest request,HttpServletResponse response,String id) {
			String token = request.getHeader("X-Token");
			if(StringUtils.isNotBlank(token)){
				ApplicationRecord applicationRecord = applicationRecordService.get(id);
				applicationRecord.setAuditList(fileInfoService.find(id,"4"));
				applicationRecord.setFileList(fileInfoService.find(id,"1"));
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(applicationRecord));
			} else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		}

}
