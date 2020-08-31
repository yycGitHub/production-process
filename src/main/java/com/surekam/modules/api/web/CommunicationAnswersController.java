package com.surekam.modules.api.web;

import java.text.SimpleDateFormat;
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
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.communication.entity.CommunicationAnswers;
import com.surekam.modules.agro.communication.service.CommunicationAnswersService;
import com.surekam.modules.agro.file.service.AgroFileInfoService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api
@Controller
@RequestMapping(value = "api/communicationAnswers")
public class CommunicationAnswersController {
	
	@Autowired
	private CommunicationAnswersService communicationAnswersService;
	@Autowired
	private ApiUserService apiUserService;
	
	@Autowired
	private AgroFileInfoService fileInfoService;
	@Autowired
	private UserService userService;

	/**
	 * 问题解答列表
	 * @author 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "communicationAnswersList",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "问题解答列表", httpMethod = "GET", notes = "问题解答列表",	consumes="application/x-www-form-urlencoded")
	public String communicationAnswersList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno,@RequestParam Integer pagesize,@RequestParam(required = false) String communicationId) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<CommunicationAnswers> page = new Page<CommunicationAnswers>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			page = communicationAnswersService.find(page,communicationId);
			List<CommunicationAnswers> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<CommunicationAnswers> iterator = list.iterator(); iterator.hasNext();) {
					CommunicationAnswers communicationAnswers = (CommunicationAnswers) iterator.next();
					communicationAnswers.setExpertName(userService.get(communicationAnswers.getExpertId()).getName());
					communicationAnswers.setFileList(fileInfoService.find(communicationAnswers.getId(),"1"));
					communicationAnswers.setAuditList(fileInfoService.find(communicationAnswers.getId(),"4"));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					communicationAnswers.setStrDate(sdf.format(communicationAnswers.getCreateTime()));
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	/**
	 * @author 
	 * app-专家解答问题保存
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "saveCommunicationAnswers",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "app-专家解答问题保存", httpMethod = "POST", notes = "app-专家解答问题保存",	consumes="application/json")
	public String saveCommunicationAnswers(HttpServletRequest request,HttpServletResponse response,
			String communicationId,String content,String [] photo,String [] auditUrl) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User user = apiUserService.getUserByToken(token);
			communicationAnswersService.saveCommunicationAnswers(user,communicationId,content,photo,auditUrl);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	/**
	 * @author 
	 * app-农户回复问题保存
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "saveCommunicationAnswersTwo",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "app-农户回复问题保存", httpMethod = "POST", notes = "app-农户回复问题保存",	consumes="application/json")
	public String saveCommunicationAnswersTwo(HttpServletRequest request,HttpServletResponse response,
			String communicationId,String content,String [] photo,String [] auditUrl) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User user = apiUserService.getUserByToken(token);
			communicationAnswersService.saveCommunicationAnswersTwo(user,communicationId,content,photo,auditUrl);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * @author 
	 * 编辑保存问题解答信息
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "updataSaveCommunicationAnswers",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "编辑保存问题解答信息", httpMethod = "POST", notes = "编辑保存问题解答信息",	consumes="application/json")
	public String updataSaveCommunicationAnswers(HttpServletRequest request,
			@RequestBody @ApiParam(name="问题解答对象",value="传入json格式",required=true) CommunicationAnswers communicationAnswers) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User user = apiUserService.getUserByToken(token);
			CommunicationAnswers communicationAnswers2 = communicationAnswersService.get(communicationAnswers.getId());
			if(StringUtils.isNotBlank(communicationAnswers.getCommunicationId())) {
				communicationAnswers2.setCommunicationId(communicationAnswers.getCommunicationId());
			}
			if(StringUtils.isNotBlank(communicationAnswers.getAnswerContent())) {
				communicationAnswers2.setAnswerContent(communicationAnswers.getAnswerContent());
			}
			if(StringUtils.isNotBlank(communicationAnswers.getExpertId())) {
				communicationAnswers2.setExpertId(communicationAnswers.getExpertId());
			}
			if(StringUtils.isNotBlank(communicationAnswers.getExpertName())) {
				communicationAnswers2.setExpertName(communicationAnswers.getExpertName());
			}
			communicationAnswers2.setUpdateUserId(user.getId());
			communicationAnswers2.setUpdateTime(new Date());	
			communicationAnswersService.save(communicationAnswers2);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(communicationAnswers2));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 删除问题解答信息
	 * @author 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "deleteById",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "删除问题解答信息", httpMethod = "POST", notes = "删除问题解答信息",	consumes="application/x-www-form-urlencoded")
	public String deleteById(HttpServletRequest request,@RequestParam String id){
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User user = apiUserService.getUserByToken(token);
			communicationAnswersService.delete(id, user.getId());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}


}
