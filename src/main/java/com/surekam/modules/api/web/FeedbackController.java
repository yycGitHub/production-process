package com.surekam.modules.api.web;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import com.surekam.modules.agro.application.entity.DelegationRecord;
import com.surekam.modules.agro.application.entity.Feedback;
import com.surekam.modules.agro.application.service.DelegationRecordService;
import com.surekam.modules.agro.application.service.FeedbackService;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.file.service.AgroFileInfoService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.UserVo;
import com.surekam.modules.sys.service.ApiSystemService;
import com.surekam.modules.sys.service.UserService;

import io.swagger.annotations.ApiOperation;

/**
 * 反馈记录表Controller
 * @author xy
 * @version 2019-06-25
 */
@Controller
@RequestMapping(value = "api/feedback")
public class FeedbackController extends BaseController{
	
	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private ApiSystemService apiSystemService;
	
	@Autowired
	private ExpertsService expertsService;
	
	@Autowired
	private AgroFileInfoService fileInfoService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DelegationRecordService delegationRecordService;
	
	@RequestMapping(value = "getFeedbackList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "反馈记录列表", notes = "反馈记录列表", consumes = "application/json")
	public String getFeedbackList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			
			Page<Feedback> page = new Page<Feedback>(pageNo, pageSize);
			page = feedbackService.find(page, new Feedback());
			System.out.println(jsonMapper.toJson(ResultUtil.success(page)));
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
		@RequestMapping(value = "saveFeedback", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
		@ResponseBody
		@ApiOperation(value = "保存反馈记录信息", httpMethod = "POST", notes = "保存反馈记录信息", consumes = "application/json")
	    public String saveFeedback(HttpServletRequest request,HttpServletResponse response,@RequestBody Feedback feedback) {
	    	response.setContentType("application/json; charset=UTF-8");
			JsonMapper jsonMapper = JsonMapper.getInstance();
			ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
			String token = request.getHeader("X-Token");
			try {
				User currentUser = apiSystemService.findUserByToken(token);
				feedback.setCreateTime(new Date());
				feedback.setCreateUserId(currentUser.getId());
				feedbackService.save(feedback);
				return jsonMapper.toJson(ResultUtil.success());
			} catch (Exception e) {
				logger.error("保存信息错误：" + e);
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
	    }
		
		
		@RequestMapping(value = "saveFeedback2",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
		@ResponseBody
		@ApiOperation(value = "app-保存反馈记录信息", httpMethod = "POST", notes = "app-保存反馈记录信息",	consumes="application/json")
		public String saveFeedback(HttpServletRequest request,HttpServletResponse response,String feedbackOpinion,String continueAppoint,
				String guidanceId,String delegationId,String applicationId,String [] photo,String [] auditUrl) {
			String token = request.getHeader("X-Token");
			if(StringUtils.isNotBlank(token)){
				User currentUser = apiSystemService.findUserByToken(token);
				DelegationRecord delegationRecord = delegationRecordService.get(delegationId);
				feedbackService.saveFeedback(currentUser,delegationRecord.getExpertId(),feedbackOpinion,continueAppoint,guidanceId,delegationId,applicationId,photo,auditUrl);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
			} else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		}
		
		@RequestMapping(value = "getFeedback", produces = "application/json;charset=UTF-8")
		@ResponseBody
		@ApiOperation(value = "反馈记录列表", notes = "反馈记录列表", consumes = "application/json")
		public String getFeedback(HttpServletRequest request,HttpServletResponse response,String applicationId) {
			response.setContentType("application/json; charset=UTF-8");
			JsonMapper jsonMapper = JsonMapper.getInstance();
			ResultBean<Object> resultBean = new ResultBean<Object>();
			int pageNo = 1;
			int pageSize = 20;
			try {
				Page<Feedback> page = new Page<Feedback>(pageNo, pageSize);
				page = feedbackService.findlist(page,applicationId);
				List<Feedback> list = page.getList();
				for(Iterator<Feedback> iterator = list.iterator(); iterator.hasNext();) {
					Feedback feedback = (Feedback) iterator.next();
					feedback.setAuditList(fileInfoService.find(feedback.getId(),"4"));
					feedback.setFileList(fileInfoService.find(feedback.getId(),"1"));
				}

				return jsonMapper.toJson(ResultUtil.success(list));
			} catch (Exception e) {
				logger.error("用户列表查询错误：" + e);
				e.printStackTrace();
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		}
		
		
		/**
		 *  反馈记录查询
		 * @param id
		 * @return
		 */
		@RequestMapping(value = "queryFeedback",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
		@ResponseBody
		@ApiOperation(value = "反馈记录查询", httpMethod = "POST", notes = "反馈记录查询",	consumes="application/json")
		public String queryFeedback(HttpServletRequest request,HttpServletResponse response,String id) {
			String token = request.getHeader("X-Token");
			if(StringUtils.isNotBlank(token)){
				Feedback feedback = feedbackService.find(id);
				if(StringUtils.isNotBlank(feedback.getExpertId())) {
					Experts expert = expertsService.get(feedback.getExpertId());
					feedback.setExpertName(expert.getExpertName());
				}
				if(StringUtils.isNotBlank(feedback.getFarmerId())) {
					User user = userService.get(feedback.getFarmerId());
					feedback.setUserName(user.getName());
				}
				feedback.setAuditList(fileInfoService.find(feedback.getId(),"4"));
				feedback.setFileList(fileInfoService.find(feedback.getId(),"1"));
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(feedback));
			} else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		}

}
