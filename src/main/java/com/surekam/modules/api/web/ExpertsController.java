package com.surekam.modules.api.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.surekam.common.utils.ChineseCharToEnUtil;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsGoodproblemService;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.agro.productionbatch.serivce.ProductionBatchService;
import com.surekam.modules.agro.video.dao.VideoDao;
import com.surekam.modules.agro.video.entity.Video;
import com.surekam.modules.agro.video.service.VideoService;
import com.surekam.modules.api.dto.req.ExpertsReq;
import com.surekam.modules.api.dto.resp.ExpertsResp;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.Dict;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.ApiDictService;
import com.surekam.modules.sys.service.ApiSystemService;
import com.surekam.modules.sys.service.UserService;
import com.surekam.modules.sys.utils.StaticStringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 专家
 * 
 * @author
 *
 */
@Api
@Controller
@RequestMapping(value = "api/experts")
public class ExpertsController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ApiUserService apiUserService;

	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;
	
	@Autowired
	private ExpertsGoodproblemService expertsGoodproblemService;

	@Autowired
	private ExpertsService expertsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductionBatchService productionBatchService;
	
	@Autowired
	private VideoService videoService;
	
	@Autowired
	private VideoDao videoDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	@Autowired
	private ApiSystemService apiSystemService;
	
	@Autowired
	private ApiDictService apiDictService;
	
	
	/**
	 * 专家列表
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "/expertsList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "专家列表", httpMethod = "GET", notes = "专家列表", consumes = "application/x-www-form-urlencoded")
	public String expertsList(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer pageno,
			@RequestParam Integer pagesize, @RequestParam(required = false) String productLibraryId) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Experts> page = new Page<Experts>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			String code = "";
			User currentUser = apiSystemService.findUserByToken(token);
			String parentIds = currentUser.getOffice().getParentIds();
			String officeId = currentUser.getOffice().getId();
			if(parentIds.indexOf(StaticStringUtils.AGRO_TULUFAN)>-1 || officeId.equals(StaticStringUtils.AGRO_TULUFAN)){
				code = "tulufan";
			}else if(parentIds.indexOf(StaticStringUtils.AGRO_WANGCHENG)>-1 || officeId.equals(StaticStringUtils.AGRO_WANGCHENG)){
				code = "wangcheng";
			}else if(parentIds.indexOf(StaticStringUtils.AGRO_LIUYANG)>-1 || officeId.equals(StaticStringUtils.AGRO_LIUYANG)){
				code = "rice";
			}else if(parentIds.indexOf(StaticStringUtils.AGRO_NANXIAN)>-1 || officeId.equals(StaticStringUtils.AGRO_NANXIAN)){
				code = "nanxian";
			}
			page = expertsService.find2(page, productLibraryId, code);
			List<Experts> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<Experts> iterator = list.iterator(); iterator.hasNext();) {
					Experts experts = (Experts) iterator.next();
					User user = userService.get(experts.getUserId());
					Video video = videoDao.getVideoByUserCode(user.getLoginName());
					experts.setUserCode(user.getLoginName());
					if(video !=null) {
						experts.setCreateRoomNumber(video.getCreateRoomNumber());
						if(StringUtils.isNotBlank(video.getEntryRoomNumber())){
							experts.setIsInRoom("1");
						}else{
							experts.setIsInRoom("0");
						}
					}
					experts.setProductLibraryList(productLibraryTreeService.getList(experts.getId()));
					experts.setExpertsGoodproblemList(expertsGoodproblemService.getList(experts.getId()));
					experts.setPhoto(userService.get(experts.getUserId()).getUserImg());
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	
	/**
	 * 平台委派任务-查询专家列表
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "/expertsList2", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "专家列表", httpMethod = "GET", notes = "专家列表", consumes = "application/x-www-form-urlencoded")
	public String expertsList2(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = 1 ;
		int pageSize = 1000;
		Page<Experts> page = new Page<Experts>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			String code = "";
			User currentUser = apiSystemService.findUserByToken(token);
			String officeId = currentUser.getOffice().getId();
			if(officeId.equals(StaticStringUtils.AGRO_TULUFAN)){
				code = "tulufan";
			}else if(officeId.equals(StaticStringUtils.AGRO_WANGCHENG)){
				code = "wangcheng";
			}else if(officeId.equals(StaticStringUtils.AGRO_LIUYANG)){
				code = "rice";
			}else if(officeId.equals(StaticStringUtils.AGRO_NANXIAN)){
				code = "nanxian";
			}
			page = expertsService.find(page, "", code);
			/*List<Experts> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<Experts> iterator = list.iterator(); iterator.hasNext();) {
					Experts experts = (Experts) iterator.next();
					User user = userService.get(experts.getUserId());
					Video video = videoService.getVideoByUserCode(user.getLoginName());
					experts.setUserCode(user.getLoginName());
					if(video !=null) {
						experts.setCreateRoomNumber(video.getCreateRoomNumber());
						if(StringUtils.isNotBlank(video.getEntryRoomNumber())){
							experts.setIsInRoom("1");
						}else{
							experts.setIsInRoom("0");
						}
					}
					experts.setProductLibraryList(productLibraryTreeService.getList(experts.getId()));
					experts.setExpertsGoodproblemList(expertsGoodproblemService.getList(experts.getId()));
					experts.setPhoto(userService.get(experts.getUserId()).getUserImg());
				}
			}*/
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * pc巡查记录，专家下拉框
	 * @return
	 */
	@RequestMapping(value = "/expertsList3", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "专家列表", httpMethod = "POST", notes = "专家列表", consumes = "application/x-www-form-urlencoded")
	public String expertsList3(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			List<Experts> list = expertsService.findList(user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * pc工作汇报，汇报人下拉框
	 * @return
	 */
	@RequestMapping(value = "/expertsList4", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "专家列表", httpMethod = "POST", notes = "专家列表", consumes = "application/x-www-form-urlencoded")
	public String expertsList4(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			List<Experts> list = expertsService.ExpertsList();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 提问获取批次相关靠前排名的专家列表
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "/expertsListTwo", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "专家列表", httpMethod = "GET", notes = "专家列表", consumes = "application/x-www-form-urlencoded")
	public String expertsListTwo(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer pageno,
			@RequestParam Integer pagesize, @RequestParam(required = false) String batchId) {
		String platform = Global.getConfig("platform");//平台
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Experts> page = new Page<Experts>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			User tuser = apiUserService.getUserByToken(token);
			List<Dict> dlist = apiDictService.findDictlist("platform");
			String platform2 = "";
			for(int k = 0;k < dlist.size(); k++) {
				if(tuser.getCompany().getParentIds().indexOf(dlist.get(k).getDescription())>-1) {
					platform2 = dlist.get(k).getValue();
				}
			}
			//ProductLibraryTree productLibraryTree = productLibraryTreeService.
			if(batchId != null) {
				ProductionBatch productionBatch = productionBatchService.get(batchId);
				
				page = expertsService.findTwo(page, productionBatch.getProductId(),platform2);
				List<Experts> list = page.getList();
				if (page.getList().size() > 0) {
					for (Iterator<Experts> iterator = list.iterator(); iterator.hasNext();) {
						Experts experts = (Experts) iterator.next();
						User user = userService.get(experts.getUserId());
						Video video = videoDao.getVideoByUserCode(user.getLoginName());
						experts.setUserCode(user.getLoginName());
						if(video !=null) {
							experts.setCreateRoomNumber(video.getCreateRoomNumber());
							if(StringUtils.isNotBlank(video.getEntryRoomNumber())){
								experts.setIsInRoom("1");
							}else{
								experts.setIsInRoom("0");
							}
						}
						experts.setProductLibraryList(productLibraryTreeService.getList(experts.getId()));
						experts.setExpertsGoodproblemList(expertsGoodproblemService.getList(experts.getId()));
						experts.setPhoto(userService.get(experts.getUserId()).getUserImg());
					}
				}
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
			}else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	/**
	 * 单个专家信息
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getexperts", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "单个专家信息", httpMethod = "GET", notes = "单个专家信息", consumes = "application/x-www-form-urlencoded")
	public String getExperts(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			Experts experts = expertsService.getExpertByUserId(user.getId());
			experts.setProductLibraryList(productLibraryTreeService.getList(experts.getId()));
			experts.setExpertsGoodproblemList(expertsGoodproblemService.getList(experts.getId()));
			experts.setPhoto(userService.get(experts.getUserId()).getUserImg());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(experts));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 单个专家信息
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getExpertById", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "单个专家信息", httpMethod = "GET", notes = "单个专家信息", consumes = "application/x-www-form-urlencoded")
	public String getExpertById(HttpServletRequest request, HttpServletResponse response,String id) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
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
	 * @author 新增保存专家信息
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "/saveExperts", produces = "text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "新增保存专家信息", httpMethod = "POST", notes = "新增保存专家信息", consumes = "application/json")
	public String saveExperts(HttpServletRequest request,
			@RequestBody @ApiParam(name = "专家对象", value = "传入json格式", required = true) Experts experts) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User user = apiUserService.getUserByToken(token);
			experts.setCreateUserId(user.getId());
			expertsService.save(experts);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(experts));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

	/**
	 * @author 编辑保存专家信息
	 * @param request
	 * @param traceProduct
	 * @return
	 */
	@RequestMapping(value = "updataSaveExperts", produces = "text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "编辑保存专家信息", httpMethod = "POST", notes = "编辑保存专家信息", consumes = "application/json")
	public String updataSaveExperts(HttpServletRequest request,
			@RequestBody @ApiParam(name = "专家对象", value = "传入json格式", required = true) Experts experts) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User user = apiUserService.getUserByToken(token);
			Experts experts2 = expertsService.get(experts.getId());
			if (StringUtils.isNotBlank(experts.getExpertName())) {
				experts2.setExpertName(experts.getExpertName());
			}
			if (StringUtils.isNotBlank(experts.getExpertDescription())) {
				experts2.setExpertDescription(experts.getExpertDescription());
			}
			
			if (StringUtils.isNotBlank(experts.getExpertSex())) {
				experts2.setExpertSex(experts.getExpertSex());
			}

			experts2.setExpertBirthDate(experts.getExpertBirthDate());

			if (StringUtils.isNotBlank(experts.getExpertTitle())) {
				experts2.setExpertTitle(experts.getExpertTitle());
			}
			if (StringUtils.isNotBlank(experts.getProfessionalField())) {
				experts2.setProfessionalField(experts.getProfessionalField());
			}
			experts2.setUpdateUserId(user.getId());
			experts2.setUpdateTime(new Date());
			expertsService.save(experts);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(experts2));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

	/**
	 * 删除专家信息
	 * 
	 * @author
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "deleteById", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "删除专家信息", httpMethod = "POST", notes = "删除专家信息", consumes = "application/x-www-form-urlencoded")
	public String deleteById(HttpServletRequest request, @RequestParam String id) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User user = apiUserService.getUserByToken(token);
			expertsService.delete(id, user.getId());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getPcExpertsList")
	@ApiOperation(value = "PC端专家列表", httpMethod = "POST", notes = "PC端专家列表", consumes = "application/x-www-form-urlencoded")
	public String getPcExpertsList(
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam(required = false) String nameAndloginName,
			@RequestParam(required = false) String goodProblem,
			@RequestParam(required = false) String productLibraryId,
			@RequestParam(required = false) String state,
			@RequestParam Integer pageno, 
			@RequestParam Integer pagesize
			) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		User user = apiUserService.getUserByToken(token);
		Page<Experts> batchManageList = expertsService.getPcExpertsList(nameAndloginName, goodProblem, productLibraryId, state, pageno, pagesize, user);
		expertsService.updateUserIdByUserCode();
		return JsonMapper.allPropertyMapper().toJson(ResultUtil.success(batchManageList));
	}

	@ResponseBody
	@RequestMapping(value = "/getPcExpertsReportList")
	@ApiOperation(value = "PC端专家服务报表", httpMethod = "POST", notes = "PC端专家服务报表", consumes = "application/x-www-form-urlencoded")
	public String getPcExpertsReportList(
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String officeId,
			@RequestParam(required = false) String time,
			@RequestParam(required = false) String state,
			@RequestParam Integer pageno, 
			@RequestParam Integer pagesize
			) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		User user = apiUserService.getUserByToken(token);
		if(StringUtils.isBlank(state)){
			state = "0";
		}
		Page<Experts> batchManageList = expertsService.getPcExpertsReportList(name, officeId, time, state, pageno, pagesize, user);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(batchManageList));
	}
	@ResponseBody
	@RequestMapping(value = "/updateExpertsStates")
	@ApiOperation(value = "启用禁用专家用户", httpMethod = "POST", notes = "启用禁用专家用户", consumes = "application/x-www-form-urlencoded")
	public String updateExpertsStates(HttpServletRequest request, @RequestParam String id,
			@RequestParam String operType) {
		try {
			String token = request.getHeader("X-Token");
			
			if(!"0".equals(operType) && !"1".equals(operType)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMessage()));
			}
			
			String updateExpertsStates = expertsService.updateExpertsStates(id, operType);
			if("Success".equals(updateExpertsStates)){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(updateExpertsStates));
			}else{
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMessage()));
			}
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}

	}
	
	
	@ResponseBody
	@RequestMapping(value = "/savaExperts")
	@ApiOperation(value = "保存专家信息", httpMethod = "POST", notes = "保存专家信息", consumes = "application/x-www-form-urlencoded")
	public String savaExperts(HttpServletRequest request,
			@RequestBody @ApiParam(name = "保存专家信息", value = "传入json格式", required = true) ExpertsReq req) {
		try {
			String token = request.getHeader("X-Token");
			User user = apiUserService.getUserByToken(token);
			String operType = expertsService.savaExperts(req, user);
			if("existence".equals(operType)){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_EXIST.getCode(), ResultEnum.DATA_EXIST.getMessage()));
			}else if("fail".equals(operType)){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.OPERATION_FAILED.getCode(), ResultEnum.OPERATION_FAILED.getMessage()));
			}else{
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(operType));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getExperts")
	@ApiOperation(value = "获取当个专家信息", httpMethod = "POST", notes = "获取当个专家信息", consumes = "application/x-www-form-urlencoded")
	public String getExpertsInfo(HttpServletRequest request,
			@RequestParam String id) {
		try {
			String token = request.getHeader("X-Token");
			ExpertsResp resp = expertsService.getExpertsInfo(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(resp));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/savaExpertTitle")
	@ApiOperation(value = "保存专家职称", httpMethod = "POST", notes = "保存专家职称", consumes = "application/x-www-form-urlencoded")
	public String savaExpertTitle(HttpServletRequest request, HttpServletResponse response, String  expertTitle,String  userId) {
		String token = request.getHeader("X-Token");
		if (token != null) {
			try {
				Experts experts = expertsService.getExpertByUserId(userId);
				experts.setExpertTitle(expertTitle);
				expertsService.save(experts);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
			} catch (Exception e) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/savaProfessionalfield")
	@ApiOperation(value = "保存专家专业领域", httpMethod = "POST", notes = "保存专家专业领域", consumes = "application/x-www-form-urlencoded")
	public String savaProfessionalfield(HttpServletRequest request, HttpServletResponse response, String [] pfarr,String  userId) {
		String token = request.getHeader("X-Token");
		if (token != null) {
			try {
				expertsService.savaProfessionalfield(userId,pfarr);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
			} catch (Exception e) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson( ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/savaexpertDescription")
	@ApiOperation(value = "保存专家描述", httpMethod = "POST", notes = "保存专家描述", consumes = "application/x-www-form-urlencoded")
	public String savaexpertDescription(HttpServletRequest request, HttpServletResponse response, String  description,String  userId) {
		String token = request.getHeader("X-Token");
		if (token != null) {
			try {
				Experts experts = expertsService.getExpertByUserId(userId);
				experts.setExpertDescription(description);
				expertsService.save(experts);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
			} catch (Exception e) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		
	}
	
	/**
	 *  app-提问获取批次列表
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "/getProductionBatchList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "app-提问获取批次列表", httpMethod = "GET", notes = "app-提问获取批次列表", consumes = "application/x-www-form-urlencoded")
	public String getProductionBatchList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			List<ProductionBatch> list = productionBatchService.getlist(user);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	
	/**
	 * 获取专家所属平台
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getExpertsOfplatform", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取专家所属平台", httpMethod = "GET", notes = "获取专家所属平台", consumes = "application/x-www-form-urlencoded")
	public String getExpertsOfplatform(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			Experts experts = expertsService.getExpertByUserId(user.getId());
			List<Dict> list = apiDictService.findDictlist(experts.getPlatform(),"platform");
			if(list != null && list.size() > 0) {
				experts.setPlatformName(list.get(0).getLabel());
			}else{
				experts.setPlatformName("");
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(experts));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getBaseExpertsList")
	@ApiOperation(value = "PC端专家列表", httpMethod = "POST", notes = "PC端专家列表", consumes = "application/x-www-form-urlencoded")
	public String getBaseExpertsList(
			HttpServletRequest request, 
			@RequestParam String officeId,
			HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		Office office = officeDao.get(officeId);
		String platform = "";
		String[] strs = office.getParentIds().split(",");
		if(strs.length>=3){
			List<Dict> list = apiDictService.findDict(strs[2]);
			if(list.size()>0){
				platform = list.get(0).getValue();
			}else{
				platform = "无";
			}
		}
		if(strs.length==2){
			List<Dict> list = apiDictService.findDict(office.getId());
			if(list.size()>0){
				platform = list.get(0).getValue();
			}else{
				platform = "无";
			}
		}
		User user = apiUserService.getUserByToken(token);
		List<Experts> batchManageList = expertsService.getBaseExpertsList(platform,user);
		expertsService.updateUserIdByUserCode();
		return JsonMapper.allPropertyMapper().toJson(ResultUtil.success(batchManageList));
	}

	@ResponseBody
	@RequestMapping(value = "/getPlatFormExpertsList")
	@ApiOperation(value = "展示平台专家列表", httpMethod = "POST", notes = "展示平台专家列表", consumes = "application/x-www-form-urlencoded")
	public String getPlatFormExpertsList(
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam String platform,
			@RequestParam String kuId,
			@RequestParam(required = false) String baseId) {
		response.setContentType("application/json; charset=UTF-8");
		Office office = officeDao.getByKuid(kuId);
		if(office==null){
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(), ResultEnum.DATA_NOT_EXIST.getMessage()));
		}
		String officeId = office.getId();
		List<Experts> batchManageList = expertsService.getPlatFormExpertsList(platform,officeId,baseId);
		return JsonMapper.allPropertyMapper().toJson(ResultUtil.success(batchManageList));
	}
}
