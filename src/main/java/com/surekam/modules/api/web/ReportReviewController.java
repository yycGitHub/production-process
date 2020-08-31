package com.surekam.modules.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.file.service.AgroFileInfoService;
import com.surekam.modules.agro.technicalreport.entity.ReportReview;
import com.surekam.modules.agro.technicalreport.service.ReportReviewService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.ApiSystemService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 汇报评审信息类Controller
 * @author xy
 * @version 2019-07-08
 */
@Controller
@RequestMapping(value = "api/reportReview")
public class ReportReviewController extends BaseController{
	
	@Autowired
	private ReportReviewService reportReviewService;
	
	@Autowired
	private ApiSystemService apiSystemService;
	
	@Autowired
	private AgroFileInfoService fileInfoService;
	
	/**
	 * web端保存评价
	 * @param request
	 * @param response
	 * @param reportReview
	 * @return
	 */
	@RequestMapping(value = "saveReportReview",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "web端保存评价", httpMethod = "POST", notes = "web端保存评价",	consumes="application/x-www-form-urlencoded")
	public String saveReportReview(HttpServletRequest request,HttpServletResponse response,
			@RequestBody @ApiParam(name="评审对象",value="传入json格式",required=true) ReportReview reportReview) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User currentUser = apiSystemService.findUserByToken(token);
			reportReviewService.saveReportReview(currentUser,reportReview);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * app手机端保存评价
	 * @param request
	 * @param response
	 * @param reportReview
	 * @return
	 */
	@RequestMapping(value = "saveReportReviewAPP",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "app手机端保存评价", httpMethod = "POST", notes = "app手机端保存评价",	consumes="application/x-www-form-urlencoded")
	public String saveReportReviewAPP(HttpServletRequest request,HttpServletResponse response,String id,String score,String review) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User currentUser = apiSystemService.findUserByToken(token);
			reportReviewService.saveReportReviewAPP(currentUser,id,score,review);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

}
