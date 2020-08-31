package com.surekam.modules.api.web;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.application.service.ApplicationRecordVOService;
import com.surekam.modules.agro.communication.entity.Communication;
import com.surekam.modules.agro.communication.entity.CommunicationAnswers;
import com.surekam.modules.agro.communication.service.CommunicationAnswersService;
import com.surekam.modules.agro.communication.service.CommunicationService;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.file.service.AgroFileInfoService;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.agro.productionbatch.serivce.ProductionBatchService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.ApiSystemService;
import com.surekam.modules.sys.service.OfficeService;
import com.surekam.modules.sys.service.UserService;
import com.surekam.modules.sys.utils.StaticStringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api
@Controller
@RequestMapping(value = "api/communication/")
public class CommunicationController {
	
	private static final int HashMap = 0;
	@Autowired
	private CommunicationService communicationService;
	@Autowired
	private ApiUserService apiUserService;
	@Autowired
	private ApiSystemService apiSystemService;
	
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
	private CommunicationAnswersService communicationAnswersService;
	
	@Autowired
	private ApplicationRecordVOService applicationRecordVOService;
	
	
	/**
	 * 我的问题列表
	 * @author 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "communicationList",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "我的问题列表", httpMethod = "GET", notes = "我的问题列表",	consumes="application/x-www-form-urlencoded")
	public String communicationList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno,@RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Communication> page = new Page<Communication>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			User currentUser = apiSystemService.findUserByToken(token);
			String officeId = currentUser.getOffice().getId();
			page = communicationService.find(page,officeId);
			
			List<Communication> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<Communication> iterator = list.iterator(); iterator.hasNext();) {
					Communication communication = (Communication) iterator.next();
					List<CommunicationAnswers> listca = new ArrayList<CommunicationAnswers>();
					listca = communicationAnswersService.find(communication.getId());
					communication.setExpertName(expertsService.get(communication.getExpertId()).getExpertName());
					communication.setBaseName(officeService.get(communication.getBaseId()).getName());//基地名称
					communication.setQuestioner(userService.get(communication.getCreateUserId()).getName());//提问人
					communication.setFileList(fileInfoService.find(communication.getId(),"1"));
					communication.setAuditList(fileInfoService.find(communication.getId(),"4"));
					communication.setReplyNum(String.valueOf(listca.size()));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					communication.setStrDate(sdf.format(communication.getCreateTime()));
					if(StringUtils.isNotBlank(communication.getProductionBatchId())) {
						ProductionBatch productionBatch = productionBatchService.get(communication.getProductionBatchId());
						if(productionBatch != null) {
							communication.setBatchCode(productionBatch.getBatchCode());
						}
					}
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	/**
	 * 全部问题列表
	 * @author 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "allCommunicationList",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "全部问题列表", httpMethod = "GET", notes = "全部问题列表",	consumes="application/x-www-form-urlencoded")
	public String allCommunicationList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno,@RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		String code = "";
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Communication> page = new Page<Communication>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			User currentUser = apiSystemService.findUserByToken(token);
			String officeId = currentUser.getOffice().getId();
			String parentIds = currentUser.getOffice().getParentIds();
			if(parentIds.indexOf(StaticStringUtils.AGRO_TULUFAN)>-1 || officeId.equals(StaticStringUtils.AGRO_TULUFAN)){
				code = "tulufan";
			}else if(parentIds.indexOf(StaticStringUtils.AGRO_WANGCHENG)>-1 || officeId.equals(StaticStringUtils.AGRO_WANGCHENG)){
				code = "wangcheng";
			}else if(parentIds.indexOf(StaticStringUtils.AGRO_LIUYANG)>-1 || officeId.equals(StaticStringUtils.AGRO_LIUYANG)){
				code = "rice";
			}
			page = communicationService.findList(page,code);
			List<Communication> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<Communication> iterator = list.iterator(); iterator.hasNext();) {
					Communication communication = (Communication) iterator.next();
					List<CommunicationAnswers> listca = new ArrayList<CommunicationAnswers>();
					listca = communicationAnswersService.find(communication.getId());
					communication.setExpertName(expertsService.get(communication.getExpertId()).getExpertName());
					communication.setBaseName(officeService.get(communication.getBaseId()).getName());//基地名称
					communication.setQuestioner(userService.get(communication.getCreateUserId()).getName());//提问人
					communication.setFileList(fileInfoService.find(communication.getId(),"1"));
					communication.setAuditList(fileInfoService.find(communication.getId(),"4"));
					communication.setReplyNum(String.valueOf(listca.size()));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					communication.setStrDate(sdf.format(communication.getCreateTime()));
					if(StringUtils.isNotBlank(communication.getProductionBatchId())) {
						ProductionBatch productionBatch = productionBatchService.get(communication.getProductionBatchId());
						if(productionBatch != null) {
							communication.setBatchCode(productionBatch.getBatchCode());
						}
					}
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * app-在线学习-问题列表
	 * @author 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getOnlineLearningList",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "app-在线学习-问题列表", httpMethod = "GET", notes = "app-在线学习-问题列表",	consumes="application/x-www-form-urlencoded")
	public String getOnlineLearningList(HttpServletRequest request,HttpServletResponse response,@RequestParam Integer pageno,@RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Communication> page = new Page<Communication>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			//User currentUser = apiSystemService.findUserByToken(token);
			//String officeId = currentUser.getOffice().getId();
			page = communicationService.getOnlineLearningList(page);
			List<Communication> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<Communication> iterator = list.iterator(); iterator.hasNext();) {
					Communication communication = (Communication) iterator.next();
					List<CommunicationAnswers> listca = new ArrayList<CommunicationAnswers>();
					listca = communicationAnswersService.find(communication.getId());
					Experts expert = expertsService.get(communication.getExpertId());
					if(expert != null) {
						communication.setExpertName(expert.getExpertName());
					}else {
						communication.setExpertName("");
					}
					communication.setBaseName(officeService.get(communication.getBaseId()).getName());//基地名称
					communication.setQuestioner(userService.get(communication.getCreateUserId()).getName());//提问人
					communication.setFileList(fileInfoService.find(communication.getId(),"1"));
					communication.setAuditList(fileInfoService.find(communication.getId(),"4"));
					communication.setReplyNum(String.valueOf(listca.size()));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					communication.setStrDate(sdf.format(communication.getCreateTime()));
					if(StringUtils.isNotBlank(communication.getProductionBatchId())) {
						ProductionBatch productionBatch = productionBatchService.get(communication.getProductionBatchId());
						if(productionBatch != null) {
							communication.setBatchCode(productionBatch.getBatchCode());
						}
					}
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 问题详情
	 * @author 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getCommunication",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "问题详情", httpMethod = "GET", notes = "问题详情",	consumes="application/x-www-form-urlencoded")
	public String getCommunication(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) String communicationId) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			Communication communication = communicationService.get(communicationId);
			communication.setBaseName(officeService.get(communication.getBaseId()).getName());//基地名称
			communication.setQuestioner(userService.get(communication.getCreateUserId()).getName());//提问人
			communication.setFileList(fileInfoService.find(communication.getId(),"1"));
			communication.setAuditList(fileInfoService.find(communication.getId(),"4"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			communication.setStrDate(sdf.format(communication.getCreateTime()));
			if(StringUtils.isNotBlank(communication.getProductionBatchId())) {
				ProductionBatch productionBatch = productionBatchService.get(communication.getProductionBatchId());
				if(productionBatch != null) {
					communication.setBatchCode(productionBatch.getBatchCode());
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(communication));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	/**
	 * @author 
	 * 新增保存问题信息 APP
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "saveAPPCommunication",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "新增保存问题信息", httpMethod = "POST", notes = "新增保存问题信息",	consumes="application/x-www-form-urlencoded")
	public String saveAPPCommunication(HttpServletRequest request,String expertId,String title,String description,String [] photo,String batchId,String[] auditUrl) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User user = apiUserService.getUserByToken(token);
			communicationService.saveAPP(user,expertId,title,description,photo,batchId,auditUrl);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	/**
	 * @author 
	 * 新增保存问题信息
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "saveCommunication",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "新增保存问题信息", httpMethod = "POST", notes = "新增保存问题信息",	consumes="application/json")
	public String saveCommunication(HttpServletRequest request,
			@RequestBody @ApiParam(name="问题对象",value="传入json格式",required=true) Communication communication) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User user = apiUserService.getUserByToken(token);
			communication.setCreateUserId(user.getId());
			communicationService.save(communication);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(communication));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * @author 
	 * 编辑保存专家信息
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "updataSaveCommunication",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "编辑保存问题信息", httpMethod = "POST", notes = "编辑保存问题信息",	consumes="application/json")
	public String updataSaveCommunication(HttpServletRequest request,
			@RequestBody @ApiParam(name="问题对象",value="传入json格式",required=true) Communication communication) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User user = apiUserService.getUserByToken(token);
			Communication communication2 = communicationService.get(communication.getId());
			if(StringUtils.isNotBlank(communication.getCommunicationTypeId())) {
				communication2.setCommunicationTypeId(communication.getCommunicationTypeId());
			}
			if(StringUtils.isNotBlank(communication.getBaseId())) {
				communication2.setBaseId(communication.getBaseId());
			}
			if(StringUtils.isNotBlank(communication.getProductionBatchId())) {
				communication2.setProductionBatchId(communication.getProductionBatchId());
			}
			if(StringUtils.isNotBlank(communication.getCommunicationTitle())) {
				communication2.setCommunicationTitle(communication.getCommunicationTitle());
			}
			if(StringUtils.isNotBlank(communication.getCommunicationDescription())) {
				communication2.setCommunicationDescription(communication.getCommunicationDescription());
			}
			if(StringUtils.isNotBlank(communication.getCommunicationStatus())) {
				communication2.setCommunicationStatus(communication.getCommunicationStatus());
			}
			communication2.setUpdateUserId(user.getId());
			communication2.setUpdateTime(new Date());	
			communicationService.save(communication2);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(communication2));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	/**
	 * @author 
	 * app-修改问题为已解决状态
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "updataCommunicationStatus",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "app-修改问题为已解决状态", httpMethod = "POST", notes = "app-修改问题为已解决状态",	consumes="application/json")
	public String updataCommunicationStatus(HttpServletRequest request,@RequestParam String id,String status) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User user = apiUserService.getUserByToken(token);
			Communication communication2 = communicationService.get(id);
			if(StringUtils.isNotBlank(status)) {
				communication2.setCommunicationStatus(status);
			}
			communication2.setUpdateUserId(user.getId());
			communication2.setUpdateTime(new Date());	
			communicationService.save(communication2);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(communication2));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * @author 
	 * app-修改问题点击量
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "updataCommunicationClickNum",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "app-修改问题点击量", httpMethod = "POST", notes = "app-修改问题点击量",	consumes="application/json")
	public String updataCommunicationClickNum(HttpServletRequest request,@RequestParam String id) {
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User user = apiUserService.getUserByToken(token);
			Communication communication2 = communicationService.get(id);
			BigInteger num = new BigInteger(communication2.getClickNum().toString());
			BigInteger m = new BigInteger("1");
			communication2.setClickNum(num.add(m));
			communication2.setUpdateUserId(user.getId());
			communication2.setUpdateTime(new Date());	
			communicationService.save(communication2);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(communication2));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 删除专家信息
	 * @author 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "deleteById",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "删除问题信息", httpMethod = "POST", notes = "删除问题信息",	consumes="application/x-www-form-urlencoded")
	public String deleteById(HttpServletRequest request,@RequestParam String id){
		String token = request.getHeader("X-Token");
		if(StringUtils.isNotBlank(token)){
			User user = apiUserService.getUserByToken(token);
			communicationService.delete(id, user.getId());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 *      专家登录首页-用户id查询此专家关联的已解决问题信息
	 * @author 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getResolvedCommunicationByUserId",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "专家登录首页-用户id查询此专家关联的问题信息", httpMethod = "GET", notes = "专家登录首页-用户id查询此专家关联的问题信息",	consumes="application/x-www-form-urlencoded")
	public String getResolvedCommunicationByUserId(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno,@RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Communication> page = new Page<Communication>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			//已解决问题
			page = communicationService.getResolvedCommunicationByUserId(page,user.getId());
			List<Communication> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<Communication> iterator = list.iterator(); iterator.hasNext();) {
					Communication communication = (Communication) iterator.next();
					//List<CommunicationAnswers> listca = new ArrayList<CommunicationAnswers>();
					//listca = communicationAnswersService.find(communication.getId());
					//communication.setExpertName(expertsService.get(communication.getExpertId()).getExpertName());
					communication.setBaseName(officeService.get(communication.getBaseId()).getName());//基地名称
					communication.setQuestioner(userService.get(communication.getCreateUserId()).getName());//提问人
					communication.setFileList(fileInfoService.find(communication.getId(),"1"));
					communication.setAuditList(fileInfoService.find(communication.getId(),"4"));
					//communication.setReplyNum(String.valueOf(listca.size()));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					communication.setStrDate(sdf.format(communication.getCreateTime()));
					if(StringUtils.isNotBlank(communication.getProductionBatchId())) {
						ProductionBatch productionBatch = productionBatchService.get(communication.getProductionBatchId());
						if(productionBatch != null) {
							communication.setBatchCode(productionBatch.getBatchCode());
						}
					}
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	/**
	 *      专家登录首页-用户id查询此专家关联的未解决问题信息
	 * @author 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getUnsolvedCommunicationByUserId",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "专家登录首页-用户id查询此专家关联的问题信息", httpMethod = "GET", notes = "专家登录首页-用户id查询此专家关联的问题信息",	consumes="application/x-www-form-urlencoded")
	public String getUnsolvedCommunicationByUserId(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno,@RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Communication> page = new Page<Communication>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			//未解决问题
			page = communicationService.getUnsolvedCommunicationByUserId(page,user.getId());
			List<Communication> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<Communication> iterator = list.iterator(); iterator.hasNext();) {
					Communication communication = (Communication) iterator.next();
					//List<CommunicationAnswers> listca = new ArrayList<CommunicationAnswers>();
					//listca = communicationAnswersService.find(communication.getId());
					//communication.setExpertName(expertsService.get(communication.getExpertId()).getExpertName());
					communication.setBaseName(officeService.get(communication.getBaseId()).getName());//基地名称
					communication.setQuestioner(userService.get(communication.getCreateUserId()).getName());//提问人
					communication.setFileList(fileInfoService.find(communication.getId(),"1"));
					communication.setAuditList(fileInfoService.find(communication.getId(),"4"));
					//communication.setReplyNum(String.valueOf(listca.size()));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					communication.setStrDate(sdf.format(communication.getCreateTime()));
					if(StringUtils.isNotBlank(communication.getProductionBatchId())) {
						ProductionBatch productionBatch = productionBatchService.get(communication.getProductionBatchId());
						if(productionBatch != null) {
							communication.setBatchCode(productionBatch.getBatchCode());
						}
					}
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 *      app-查询已解决和未解决问题数量
	 * @author 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getcountNum",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "app-查询已解决和未解决问题数量", httpMethod = "GET", notes = "app-查询已解决和未解决问题数量",	consumes="application/x-www-form-urlencoded")
	public String getcountNum(HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		long num1 = 0;
		long num2 = 0;
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			Map<String, Object> map = new java.util.HashMap<String, Object>();
			num1 = communicationService.getUnsolvedNum(user.getId());
			num2 = communicationService.getResolvedNum(user.getId());
			map.put("unsolvedNum", num1);
			map.put("resolvedNum", num2);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	//专家工作台待办事项数量---------------------------------------------------------
	
	/**
	 *      专家工作台待办事项数量
	 * @author 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getUnsolvedCommunicationCount",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "专家工作台待办事项数量", httpMethod = "GET", notes = "专家工作台待办事项数量",	consumes="application/x-www-form-urlencoded")
	public String getUnsolvedCommunicationCount(HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			long num = communicationService.getUnsolvedCommunicationCount(user.getId());
			String notFinishCount = applicationRecordVOService.getNotCount(user); 
			Map<String, Object> map = new java.util.HashMap<String, Object>();
			map.put("unsolvedNum", num);
			map.put("notFinishCount", notFinishCount);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	

}
