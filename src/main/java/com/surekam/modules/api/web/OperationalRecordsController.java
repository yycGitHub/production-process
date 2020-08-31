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

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.application.entity.ApplicationRecord;
import com.surekam.modules.agro.application.entity.DelegationRecord;
import com.surekam.modules.agro.application.entity.Feedback;
import com.surekam.modules.agro.application.entity.GuidanceGecords;
import com.surekam.modules.agro.application.entity.OperationalRecords;
import com.surekam.modules.agro.application.service.ApplicationRecordService;
import com.surekam.modules.agro.application.service.DelegationRecordService;
import com.surekam.modules.agro.application.service.FeedbackService;
import com.surekam.modules.agro.application.service.GuidanceGecordsService;
import com.surekam.modules.agro.application.service.OperationalRecordsService;
import com.surekam.modules.agro.basemanage.service.BaseTreeService;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.file.entity.AgroFileInfo;
import com.surekam.modules.agro.file.service.AgroFileInfoService;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.api.service.BatchManageApiService;
import com.surekam.modules.sys.service.OfficeService;
import com.surekam.modules.sys.service.UserService;

import io.swagger.annotations.ApiOperation;


/**
 * 现场指导系列操作记录
 * @author xy
 * @version 2019-08-19
 */
@Controller
@RequestMapping(value = "api/operationalRecords")
public class OperationalRecordsController extends BaseController{
	
	@Autowired
	private OperationalRecordsService operationalRecordsService;
	
	@Autowired
	private ApplicationRecordService applicationRecordService;
	
	@Autowired
	private DelegationRecordService delegationRecordService;
	
	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private GuidanceGecordsService guidanceGecordsService;
	
	@Autowired
	private AgroFileInfoService fileInfoService;
	
	@Autowired
	private BaseTreeService baseTreeService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private BatchManageApiService batchManageApiService;
	
	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;

	@Autowired
	private ExpertsService expertsService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "getOperationalRecordsList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "现场指导操作记录", httpMethod = "GET", notes = "现场指导操作记录", consumes = "application/x-www-form-urlencoded")
	public String getOperationalRecordsList(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) String id) {
		response.setContentType("application/json; charset=UTF-8");
		
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			try {
				List<OperationalRecords> list = new ArrayList<OperationalRecords>();
				list = operationalRecordsService.getList(id);
				if (list != null | list.size() > 0) {
					for (Iterator<OperationalRecords> iterator = list.iterator(); iterator.hasNext();) {
						OperationalRecords operationalRecords = (OperationalRecords) iterator.next();
						if(operationalRecords.getType().equals("1")) {
							ApplicationRecord applicationRecord= applicationRecordService.get(operationalRecords.getYwid());
							List<AgroFileInfo> fiList = fileInfoService.find(id,"1");
							List<String> url = new ArrayList<String>();
							for(int i = 0;i < fiList.size(); i++) {
								url.add(fiList.get(i).getAbsolutePath());
							}
							applicationRecord.setFileUrlList(url);
							applicationRecord.setFileList(fiList);
							applicationRecord.setAuditList(fileInfoService.find(id,"4"));
							applicationRecord.setBaseName(baseTreeService.get(applicationRecord.getBaseId()).getName());
							applicationRecord.setOfficeName(officeService.get(applicationRecord.getOfficeId()).getName());
							applicationRecord.setBatchCode(batchManageApiService.getBatchManage(applicationRecord.getBatchId()).getBatchCode());
							applicationRecord.setProductName(productLibraryTreeService.get(applicationRecord.getProductId()).getProductCategoryName());
							operationalRecords.setApplicationRecord(applicationRecord);
						}else if(operationalRecords.getType().equals("2")) {
							DelegationRecord delegationRecord = delegationRecordService.get(operationalRecords.getYwid());
							delegationRecord.setExpertName(expertsService.get(delegationRecord.getExpertId()).getExpertName());
							operationalRecords.setDelegationRecord(delegationRecord);
						}else if(operationalRecords.getType().equals("3")) {
							GuidanceGecords guidanceGecords = guidanceGecordsService.get(operationalRecords.getYwid());
							List<AgroFileInfo> fiList = fileInfoService.find(id,"1");
							List<String> url = new ArrayList<String>();
							for(int i = 0;i < fiList.size(); i++) {
								url.add(fiList.get(i).getAbsolutePath());
							}
							guidanceGecords.setFileUrlList(url);
							guidanceGecords.setFileList(fiList);
							guidanceGecords.setAuditList(fileInfoService.find(guidanceGecords.getId(),"4"));
							guidanceGecords.setExpertName(expertsService.get(guidanceGecords.getExpertId()).getExpertName());
							operationalRecords.setGuidanceGecords(guidanceGecords);
						}else if(operationalRecords.getType().equals("4")) {
							Feedback feedback = feedbackService.get(operationalRecords.getYwid());
							List<AgroFileInfo> fiList = fileInfoService.find(id,"1");
							List<String> url = new ArrayList<String>();
							for(int i = 0;i < fiList.size(); i++) {
								url.add(fiList.get(i).getAbsolutePath());
							}
							feedback.setFileUrlList(url);
							feedback.setFileList(fiList);
							feedback.setAuditList(fileInfoService.find(feedback.getId(),"4"));
							feedback.setUserName(userService.get(feedback.getCreateUserId()).getName());
							operationalRecords.setFeedback(feedback);
						}
					}
				}
				return jsonMapper.toJson(ResultUtil.success(list));
			} catch (Exception e) {
				logger.error("现场指导操作记录查询错误：" + e);
				e.printStackTrace();
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

}
