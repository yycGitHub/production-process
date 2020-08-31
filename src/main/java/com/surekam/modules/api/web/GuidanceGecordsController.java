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
import com.surekam.modules.agro.application.entity.GuidanceGecords;
import com.surekam.modules.agro.application.entity.OperationalRecords;
import com.surekam.modules.agro.application.service.GuidanceGecordsService;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
import com.surekam.modules.agro.basemanage.service.BaseTreeService;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.file.service.AgroFileInfoService;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.UserVo;
import com.surekam.modules.sys.service.ApiSystemService;
import com.surekam.modules.sys.service.OfficeService;

import io.swagger.annotations.ApiOperation;


/**
 * 专家指导记录Controller
 * @author xy
 * @version 2019-06-25
 */
@Controller
@RequestMapping(value = "api/guidance")
public class GuidanceGecordsController extends BaseController{
	
	@Autowired
	private GuidanceGecordsService guidanceGecordsService;
	
	@Autowired
	private ApiSystemService apiSystemService;
	
	@Autowired
	private ExpertsService expertsService;
	
	@Autowired
	private AgroFileInfoService fileInfoService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private BaseTreeService baseTreeService;
	
	@RequestMapping(value = "getGuidanceGecordsList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "专家指导记录列表", notes = "专家指导记录列表", consumes = "application/json")
	public String getGuidanceGecordsList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			
			Page<GuidanceGecords> page = new Page<GuidanceGecords>(pageNo, pageSize);
			page = guidanceGecordsService.find(page, new GuidanceGecords());
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	/**
	 * 申请记录id查询
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getGuidanceGecords", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "专家指导记录信息", notes = "专家指导记录信息", consumes = "application/json")
	public String getGuidanceGecords(HttpServletRequest request,HttpServletResponse response,@RequestParam String applicationId,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			Page<GuidanceGecords> page = new Page<GuidanceGecords>(pageNo, pageSize);
			page = guidanceGecordsService.getGuidanceGecords(page, applicationId);
			
			List<GuidanceGecords> list = page.getList();
			if (page.getList().size() > 0) {
				for (Iterator<GuidanceGecords> iterator = list.iterator(); iterator.hasNext();) {
					GuidanceGecords guidanceGecords = (GuidanceGecords) iterator.next();
					guidanceGecords.setFileList(fileInfoService.find(guidanceGecords.getId(),"1"));
					guidanceGecords.setAuditList(fileInfoService.find(guidanceGecords.getId(),"4"));
					guidanceGecords.setExpertName(expertsService.get(guidanceGecords.getExpertId()).getExpertName());
				}
			}
			
			GuidanceGecords guidanceGecords = page.getList().get(0);
			guidanceGecords.setAuditList(fileInfoService.find(guidanceGecords.getId(),"4"));
			guidanceGecords.setFileList(fileInfoService.find(guidanceGecords.getId(),"1"));
			guidanceGecords.setExpertName(expertsService.get(guidanceGecords.getExpertId()).getExpertName());
			return jsonMapper.toJson(ResultUtil.success(guidanceGecords));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	
		@RequestMapping(value = "saveGuidanceGecords", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
		@ResponseBody
		@ApiOperation(value = "保存专家指导记录信息", httpMethod = "POST", notes = "保存专家指导记录信息", consumes = "application/json")
	    public String saveGuidanceGecords(HttpServletRequest request,HttpServletResponse response,@RequestBody GuidanceGecords guidanceGecords) {
	    	response.setContentType("application/json; charset=UTF-8");
			JsonMapper jsonMapper = JsonMapper.getInstance();
			ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
			String token = request.getHeader("X-Token");
			try {
				User currentUser = apiSystemService.findUserByToken(token);
				guidanceGecords.setCreateTime(new Date());
				guidanceGecords.setCreateUserId(currentUser.getId());
				guidanceGecordsService.save(guidanceGecords);
				return jsonMapper.toJson(ResultUtil.success());
			} catch (Exception e) {
				logger.error("保存信息错误：" + e);
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
	    }
		
		/**
		 * app-保存专家指导记录信息
		 * @param request
		 * @param response
		 * @param address
		 * @param longitude
		 * @param latitude
		 * @param details
		 * @param guidanceTime
		 * @param delegationId
		 * @param applicationId
		 * @param photo
		 * @param auditUrl
		 * @return
		 */
		@RequestMapping(value = "saveGuidanceGecords2",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
		@ResponseBody
		@ApiOperation(value = "app-保存专家指导记录信息", httpMethod = "POST", notes = "app-保存专家指导记录信息",	consumes="application/json")
		public String saveGuidanceGecords2(HttpServletRequest request,HttpServletResponse response,String address,
				String longitude,String latitude,String details,String guidanceTime,String delegationId,
				String applicationId,String [] photo,String [] auditUrl) {
			String token = request.getHeader("X-Token");
			if(StringUtils.isNotBlank(token)){
				User currentUser = apiSystemService.findUserByToken(token);
				Experts expert = expertsService.getExpertByUserId(currentUser.getId());
				guidanceGecordsService.saveGuidanceGecords2(currentUser,expert.getId(),address,longitude,latitude,details,guidanceTime,delegationId,applicationId,photo,auditUrl);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
			} else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		}
		
		/**
		 *  专家指导记录查询
		 * @param id
		 * @return
		 */
		@RequestMapping(value = "queryGuidance",produces="text/plain;charset=UTF-8", method = {RequestMethod.POST})
		@ResponseBody
		@ApiOperation(value = "专家指导记录查询", httpMethod = "POST", notes = "专家指导记录查询",	consumes="application/json")
		public String queryGuidance(HttpServletRequest request,HttpServletResponse response,String id) {
			String token = request.getHeader("X-Token");
			if(StringUtils.isNotBlank(token)){
				GuidanceGecords guidanceGecords = guidanceGecordsService.find(id);
				if(StringUtils.isNotBlank(guidanceGecords.getExpertId())) {
					Experts expert = expertsService.get(guidanceGecords.getExpertId());
					guidanceGecords.setExpertName(expert.getExpertName());
				}
				Office office = officeService.get(guidanceGecords.getOfficeId());
				BaseTree baseTree = baseTreeService.get(guidanceGecords.getBaseId());
				guidanceGecords.setBaseName(baseTree.getName());
				guidanceGecords.setOfficeName(office.getName());
				guidanceGecords.setAuditList(fileInfoService.find(guidanceGecords.getId(),"4"));
				guidanceGecords.setFileList(fileInfoService.find(guidanceGecords.getId(),"1"));
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(guidanceGecords));
			} else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		}

}
