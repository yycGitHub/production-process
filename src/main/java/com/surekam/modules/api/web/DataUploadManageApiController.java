/**
 * 
 */
package com.surekam.modules.api.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.standarditems.entity.StandardItems;
import com.surekam.modules.agro.uploadauditrecord.entity.UploadAuditRecord;
import com.surekam.modules.agro.uploadauditrecord.service.UploadAuditRecordService;
import com.surekam.modules.api.dto.req.SavaUploadReq;
import com.surekam.modules.api.dto.req.UploadAuditRecordReq;
import com.surekam.modules.api.dto.req.UploadSettingReq;
import com.surekam.modules.api.service.DataUploadManageApiService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Title: DataUploadManage Description: 数据上传管理Api
 * 
 * @author tangjun
 * @date 2019年7月12日
 */
@Controller
@RequestMapping(value = "api/dataUploadManage/")
public class DataUploadManageApiController {

	@Autowired
	private DataUploadManageApiService dataUploadManageApiService;
	
	@Autowired
	private UploadAuditRecordService uploadAuditRecordService;
	
	@ResponseBody
	@RequestMapping(value = "notUpload")
	@ApiOperation(value = "获取未上传列表信息", httpMethod = "POST", notes = "获取未上传列表信息", consumes = "application/x-www-form-urlencoded")
	public String notUpload(
			HttpServletRequest request, 
			@RequestParam String officeId, 
			@RequestParam Integer pageno,
			@RequestParam Integer pagesize) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		Page<Map<String, String>> page = dataUploadManageApiService.notUpload(officeId, pageno, pagesize);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
	}
	
	@ResponseBody
	@RequestMapping(value = "alreadyUpload")
	@ApiOperation(value = "获取已上传列表信息", httpMethod = "POST", notes = "获取已上传列表信息", consumes = "application/x-www-form-urlencoded")
	public String alreadyUpload(
			HttpServletRequest request, 
			@RequestParam String officeId, 
			@RequestParam Integer pageno,
			@RequestParam Integer pagesize) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		Page<Map<String, String>> page = dataUploadManageApiService.alreadyUpload(officeId, pageno, pagesize);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
	}
	
	@ResponseBody
	@RequestMapping(value = "auditFailed")
	@ApiOperation(value = "获取审核未通过列表信息", httpMethod = "POST", notes = "获取审核未通过列表信息", consumes = "application/x-www-form-urlencoded")
	public String auditFailed(
			HttpServletRequest request, 
			@RequestParam String officeId, 
			@RequestParam Integer pageno,
			@RequestParam Integer pagesize) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		Page<Map<String, String>> page = dataUploadManageApiService.auditFailed(officeId, pageno, pagesize);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
	}
	
	@ResponseBody
	@RequestMapping(value = "aboutOfficeQueryProductLibrary")
	@ApiOperation(value = "通过公司查询品种", httpMethod = "POST", notes = "通过公司查询品种", consumes = "application/x-www-form-urlencoded")
	public String aboutOfficeQueryProductLibrary(HttpServletRequest request, @RequestParam String officeId) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		List<Map<String, String>> list = dataUploadManageApiService.aboutOfficeQueryProductLibrary(officeId);
		return JsonMapper.allPropertyMapper().toJson(ResultUtil.success(list));
	}
	
	@ResponseBody
	@RequestMapping(value = "aboutStandardQueryItems")
	@ApiOperation(value = "通过标准查询作业项", httpMethod = "POST", notes = "通过标准查询作业项", consumes = "application/x-www-form-urlencoded")
	public String aboutStandardQueryItems(HttpServletRequest request, @RequestParam String officeId ,@RequestParam String standardId) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		List<StandardItems> aboutStandardQueryItems = dataUploadManageApiService.aboutStandardQueryItems(officeId, standardId);
		return JsonMapper.allPropertyMapper().toJson(ResultUtil.success(aboutStandardQueryItems));
	}
	
	@RequestMapping(value = "uploadSetting", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "保存上传设置", notes = "保存上传设置", consumes = "application/json")
	public String uploadSetting(HttpServletRequest request,
			@RequestBody @ApiParam(name = "保存上传设置", value = "传入json格式", required = true) UploadSettingReq req) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		String state = dataUploadManageApiService.uploadSetting(req.getOfficeId(), req.getStandardId(), req.getUploadSettingIdList());
		return JsonMapper.allPropertyMapper().toJson(ResultUtil.success(state));
	}
	
	@ResponseBody
	@RequestMapping(value = "getBatchData")
	@ApiOperation(value = "上传页面数据", httpMethod = "POST", notes = "上传页面数据", consumes = "application/x-www-form-urlencoded")
	public String getBatchData(HttpServletRequest request, @RequestParam String id, @RequestParam String batchId) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		ResultBean<Map<Object, Object>> batchData = dataUploadManageApiService.getBatchData(id, batchId);
		return JsonMapper.allPropertyMapper().toJson(ResultUtil.success(batchData));
	}
	
	@RequestMapping(value = "savaUpload", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "保存上传", notes = "对外审核状态", consumes = "application/json")
	public String savaUpload(HttpServletRequest request,
			@RequestBody @ApiParam(name = "保存上传", value = "传入json格式", required = true) SavaUploadReq req) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		String status = dataUploadManageApiService.savaUpload(req);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(status));
	}
	
	@RequestMapping(value = "batchUpload", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "批量上传", notes = "批量上传", consumes = "application/json")
	public String batchUpload(HttpServletRequest request, @RequestBody @RequestParam List<String> standardTaskItemsArgsValueId) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		ResultBean<String> batchUpload = dataUploadManageApiService.batchUpload(standardTaskItemsArgsValueId);
		return JsonMapper.nonDefaultMapper().toJson(batchUpload);
	}
	
	@RequestMapping(value = "autoUpload", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "自动上传", notes = "自动上传", consumes = "application/json")
	public String autoUpload(HttpServletRequest request, @RequestBody @RequestParam String officeId) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		ResultBean<String> autoUpload = dataUploadManageApiService.autoUpload(officeId);
		return JsonMapper.nonDefaultMapper().toJson(autoUpload);
	}
	
	@RequestMapping(value = "updateAuditStatus", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "对外审核状态", notes = "对外审核状态", consumes = "application/json")
	public String updateAuditStatus(
			@RequestBody @ApiParam(name = "修改审核记录参数", value = "传入json格式", required = true) UploadAuditRecordReq req) {
		String status = dataUploadManageApiService.updateAuditStatus(req);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(status));
	}
	
	@ResponseBody
	@RequestMapping(value = "getAgroProductionInfo")
	@ApiOperation(value = "获取农事生产过程信息", httpMethod = "POST", notes = "获取农事生产过程信息", consumes = "application/x-www-form-urlencoded")
	public String getAgroProductionInfo(@RequestParam String officeId, @RequestParam String batchCode) {
		List<Map<Object, Object>> listMap = dataUploadManageApiService.getAgroProductionInfo(officeId, batchCode);
		if(!listMap.isEmpty()) {
			return JsonMapper.allPropertyMapper().toJson(ResultUtil.success(listMap));
		}else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.BAD_REQ_PARAM.getCode(), ResultEnum.BAD_REQ_PARAM.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "getUploadAuditRecord")
	@ApiOperation(value = "获取农事生产过程信息", httpMethod = "POST", notes = "获取农事生产过程信息", consumes = "application/x-www-form-urlencoded")
	public String getUploadAuditRecord(HttpServletRequest request, @RequestParam String id) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		List<UploadAuditRecord> list = uploadAuditRecordService.findByStandardTaskItemsArgsValueId(id);
		if(!list.isEmpty()) {
			return JsonMapper.allPropertyMapper().toJson(ResultUtil.success(list));
		}else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.BAD_REQ_PARAM.getCode(), ResultEnum.BAD_REQ_PARAM.getMessage()));
		}
	}
}
