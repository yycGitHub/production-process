package com.surekam.modules.api.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.surekam.modules.agro.communication.entity.Communication;
import com.surekam.modules.agro.communication.entity.CommunicationAnswers;
import com.surekam.modules.agro.communication.service.CommunicationAnswersService;
import com.surekam.modules.agro.communication.service.CommunicationService;
import com.surekam.modules.agro.experts.entity.ExpertServiceInfo;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsGoodproblemService;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.experts.service.ExpertsServiceInfoService;
import com.surekam.modules.agro.file.entity.AgroFileInfo;
import com.surekam.modules.agro.file.service.AgroFileInfoService;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.agro.productbatchtaskresolve.service.ProductBatchTaskResolveService;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.agro.productionbatch.serivce.ProductionBatchService;
import com.surekam.modules.api.entity.out.RebackData;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.api.service.BaseManageApiService;
import com.surekam.modules.api.service.BatchManageApiService;
import com.surekam.modules.cms.entity.Article;
import com.surekam.modules.cms.service.ArticleService;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.ApiSystemService;
import com.surekam.modules.sys.service.OfficeService;
import com.surekam.modules.sys.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 演示数据获取
 * 专家的问题列表，专家服务详情
 * @author
 *
 */
@Api
@Controller
@RequestMapping(value = "api/demo")
public class DemoController {
	
	@Autowired
	private CommunicationService communicationService;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@Autowired
	private BaseManageApiService baseManageApiService;
	
	@Autowired
	private OfficeService officeService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private AgroFileInfoService fileInfoService;

	@Autowired
	private ExpertsService expertsService;
	
	@Autowired
	private ProductionBatchService productionBatchService;
	
	@Autowired
	private ExpertsServiceInfoService expertsServiceInfoService;
	
	@Autowired
	private CommunicationAnswersService communicationAnswersService;
	
	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;
	
	@Autowired
	private ExpertsGoodproblemService expertsGoodproblemService;
	
	@Autowired
	private ProductBatchTaskResolveService productBatchTaskResolveService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BatchManageApiService batchManageApiService;
	
	@Autowired
	private ArticleService articleService;
	
	
	/**
	 * demo单个专家信息
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/getExpertById", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "demo单个专家信息", httpMethod = "GET", notes = "demo单个专家信息", consumes = "application/x-www-form-urlencoded")
	public String getExpertById(HttpServletRequest request, HttpServletResponse response,String id) {
		response.setContentType("application/json; charset=UTF-8");
		//String token = request.getHeader("X-Token");
		String token = "004c86fa-0778-4823-9f75-e7d0698b8189";
		if (token != null) {
			Experts experts = expertsService.get(id);
			experts.setProductLibraryList(productLibraryTreeService.getList(experts.getId()));
			experts.setExpertsGoodproblemList(expertsGoodproblemService.getList(experts.getId()));
			experts.setPhoto(userService.get(experts.getUserId()).getUserImg());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(experts));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 *  app-提问获取批次列表demo
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/getProductionBatchList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "app-提问获取批次列表demo", httpMethod = "GET", notes = "app-提问获取批次列表demo", consumes = "application/x-www-form-urlencoded")
	public String getProductionBatchList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		//String token = request.getHeader("X-Token");
		String token = "004c86fa-0778-4823-9f75-e7d0698b8189";
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			List<ProductionBatch> list = productionBatchService.getlist(user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * demo专家的已解决问题信息
	 * @author 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getDemoResolvedCommunicationByUserId",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "demo专家的已解决问题信息", httpMethod = "GET", notes = "demo专家的已解决问题信息",	consumes="application/x-www-form-urlencoded")
	public String getDemoResolvedCommunicationByUserId(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno,@RequestParam Integer pagesize,String expertId, @RequestParam String code) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Communication> page = new Page<Communication>(pageNo, pageSize);
		//已解决问题
		page = communicationService.getDemoResolvedCommunicationByUserId(page,expertId);
		List<Communication> list = page.getList();
		if (page.getList().size() > 0) {
			for (Iterator<Communication> iterator = list.iterator(); iterator.hasNext();) {
				Communication communication = (Communication) iterator.next();
				communication.setBaseName(officeService.get(communication.getBaseId()).getName());//基地名称
				communication.setQuestioner(userService.get(communication.getCreateUserId()).getName());//提问人
				if(StringUtils.isNotBlank(communication.getProductionBatchId())) {
					communication.setBatchCode(productionBatchService.get(communication.getProductionBatchId()).getBatchCode());
				}
			}
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
	}
	
	
	/**
	 * demo专家的未解决问题信息
	 * @author 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getDemoUnsolvedCommunicationByUserId",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "demo专家的未解决问题信息", httpMethod = "GET", notes = "demo专家的未解决问题信息",	consumes="application/x-www-form-urlencoded")
	public String getDemoUnsolvedCommunicationByUserId(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno,@RequestParam Integer pagesize,String expertId, @RequestParam String code) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Communication> page = new Page<Communication>(pageNo, pageSize); 
		page = communicationService.getDemoUnsolvedCommunicationByUserId(page,expertId);
		List<Communication> list = page.getList();
		if (page.getList().size() > 0) {
			for (Iterator<Communication> iterator = list.iterator(); iterator.hasNext();) {
				Communication communication = (Communication) iterator.next();
				communication.setBaseName(officeService.get(communication.getBaseId()).getName());//基地名称
				communication.setQuestioner(userService.get(communication.getCreateUserId()).getName());//提问人
				if(StringUtils.isNotBlank(communication.getProductionBatchId())) {
					communication.setBatchCode(productionBatchService.get(communication.getProductionBatchId()).getBatchCode());
				}
			}
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
	}
	
	
	/**
	 * demo服务详细
	 * @author 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getDemoExpertServiceInfoById",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "demo服务详细", httpMethod = "GET", notes = "demo服务详细",	consumes="application/x-www-form-urlencoded")
	public String getExpertServiceInfoById(HttpServletRequest request,HttpServletResponse response,String id) {
		response.setContentType("application/json; charset=UTF-8");
		if (StringUtils.isNotBlank(id)) {
			ExpertServiceInfo expertServiceInfo = expertsServiceInfoService.get(id);
			expertServiceInfo.setExpertName(userService.get(expertServiceInfo.getExpertId()).getName());
			expertServiceInfo.setCompanyName(officeService.get(expertServiceInfo.getServiceCompanyId()).getName());//公司名称
			expertServiceInfo.setFileList(fileInfoService.find(expertServiceInfo.getId(),"1"));
			expertServiceInfo.setAuditList(fileInfoService.find(expertServiceInfo.getId(),"4"));
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(expertServiceInfo));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.BAD_REQ_PARAM.getCode(), ResultEnum.BAD_REQ_PARAM.getMessage()));
		}
	}
	
	/**
	 * demo单个专家信息
	 * @author
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getDemoExpertById", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "demo单个专家信息", httpMethod = "GET", notes = "demo单个专家信息", consumes = "application/x-www-form-urlencoded")
	public String getDemoExpertById(HttpServletRequest request, HttpServletResponse response,String id) {
		response.setContentType("application/json; charset=UTF-8");
		if (StringUtils.isNotBlank(id)) {
			Experts experts = expertsService.get(id);
			experts.setPhoto(userService.get(experts.getUserId()).getUserImg());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(experts));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.BAD_REQ_PARAM.getCode(), ResultEnum.BAD_REQ_PARAM.getMessage()));
		}
	}
	
	/**
	 * demo问题详情
	 * @author 
	 * @param request
	 * @param response
	 * @param communicationId
	 * @return
	 */
	@RequestMapping(value = "getDemoCommunication",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "demo问题详情", httpMethod = "GET", notes = "demo问题详情",	consumes="application/x-www-form-urlencoded")
	public String getDemoCommunication(HttpServletRequest request,HttpServletResponse response,String communicationId) {
		response.setContentType("application/json; charset=UTF-8");
		if (StringUtils.isNotBlank(communicationId)) {
			Communication communication = communicationService.get(communicationId);
			communication.setBaseName(officeService.get(communication.getBaseId()).getName());//基地名称
			communication.setQuestioner(userService.get(communication.getCreateUserId()).getName());//提问人
			communication.setFileList(fileInfoService.find(communication.getId(),"1"));
			communication.setAuditList(fileInfoService.find(communication.getId(),"4"));
			if(StringUtils.isNotBlank(communication.getProductionBatchId())) {
				communication.setBatchCode(productionBatchService.get(communication.getProductionBatchId()).getBatchCode());
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(communication));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.BAD_REQ_PARAM.getCode(), ResultEnum.BAD_REQ_PARAM.getMessage()));
		}
	}
	
	
	/**
	 * demo问题解答列表
	 * @author 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @param communicationId
	 * @return
	 */
	@RequestMapping(value = "getDemoCommunicationAnswersList",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "demo问题解答列表", httpMethod = "GET", notes = "demo问题解答列表",	consumes="application/x-www-form-urlencoded")
	public String communicationAnswersList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno,@RequestParam Integer pagesize,String communicationId) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<CommunicationAnswers> page = new Page<CommunicationAnswers>(pageNo, pageSize);
		if (StringUtils.isNotBlank(communicationId)) {
			page = communicationAnswersService.find(page,communicationId);
			List<CommunicationAnswers> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<CommunicationAnswers> iterator = list.iterator(); iterator.hasNext();) {
					CommunicationAnswers communicationAnswers = (CommunicationAnswers) iterator.next();
					communicationAnswers.setFileList(fileInfoService.find(communicationAnswers.getId(),"1"));
					communicationAnswers.setAuditList(fileInfoService.find(communicationAnswers.getId(),"4"));
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.BAD_REQ_PARAM.getCode(), ResultEnum.BAD_REQ_PARAM.getMessage()));
		}
	}
	
	@RequestMapping(value = "getDemoWWCCount", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取未完成任务数量", httpMethod = "GET", notes = "获取未完成任务数量", consumes = "application/x-www-form-urlencoded")
	public String getDemoWWCCount(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String userCode = request.getParameter("userCode");
		if (userCode != null) {
			User user = userDao.findByLoginName(userCode);
			String notFinishCount = productBatchTaskResolveService.getAllCount("0",user,"");
			map.put("notFinishCount", notFinishCount);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/findDemoHomeUserData")
	@ApiOperation(value = "获取主页用户数据", httpMethod = "POST", notes = "获取主页用户数据", consumes = "application/x-www-form-urlencoded")
	public String findDemoHomeUserData(HttpServletRequest request, @RequestParam String type) {
		try {
			String userCode = request.getParameter("userCode");
			if (StringUtils.isBlank(userCode)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			User user = userDao.findByLoginName(userCode);
			user.setRoleName(user.getRoleNames().split(",")[0] + "：" + user.getName());
			if ("1".equals(type)) {
				user.setBaseNum(baseManageApiService.findAgroBaseCount(user));
			} else if ("2".equals(type)) {
				user.setBatchNum(productionBatchService.findBatchCount(user));
			}
			user.setCompanyName(user.getOffice().getName());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(user));
		} catch (Exception e) {
			//logger.info("获取主页用户数据{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getDemoNHBatchList")
	@ApiOperation(value = "根据登录用户获取批次列表", httpMethod = "POST", notes = "根据登录用户获取批次列表", consumes = "application/x-www-form-urlencoded")
	public String getDemoNHBatchList(HttpServletRequest request, @RequestParam Integer pageno, @RequestParam Integer pagesize) {
		try {
			int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
			int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
			Page<ProductionBatch> page = new Page<ProductionBatch>(pageNo, pageSize);
			String userCode = request.getParameter("userCode");
			if (StringUtils.isNotBlank(userCode)) {
				User user = userDao.findByLoginName(userCode);
				page = batchManageApiService.getNHBatchList(page, user, "");
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
			}else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
}
