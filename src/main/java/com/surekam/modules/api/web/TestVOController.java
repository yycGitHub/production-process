package com.surekam.modules.api.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.surekam.modules.agro.application.entity.ApplicationRecordVO;
import com.surekam.modules.agro.application.service.ApplicationRecordVOService;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.ApiSystemService;
import com.surekam.modules.sys.utils.StaticStringUtils;
import io.swagger.annotations.ApiOperation;

/**
 * 农户申请记录Controller
 * @author xy
 * @version 2019-06-25
 */
@Controller
@RequestMapping(value = "api/testVO")
public class TestVOController extends BaseController{
	
	@Autowired
	private ApplicationRecordVOService applicationRecordVOService;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@Autowired
	private ExpertsService expertsService;
	
	@Autowired
	private ApiSystemService apiSystemService;
	
	@RequestMapping(value = "getApplicationRecordVOList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "农户申请记录列表", notes = "农户申请记录列表", consumes = "application/json")
	public String getApplicationRecordVOList(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) String itemId,
			@RequestParam(required = false) String expertName,@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		try {
			User user = apiUserService.getUserByToken(token);
			Page<ApplicationRecordVO> page = new Page<ApplicationRecordVO>(pageNo, pageSize);
			page = applicationRecordVOService.find1(page, itemId,user.getCompany().getId(),expertName);
			System.out.println(jsonMapper.toJson(ResultUtil.success(page)));
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
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
	@RequestMapping(value = "/expertsList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "专家列表", httpMethod = "GET", notes = "专家列表", consumes = "application/x-www-form-urlencoded")
	public String expertsList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = 1 ;
		int pageSize = 100;
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
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/expertsFindById")
	@ApiOperation(value = "根据ID查询", httpMethod = "POST", notes = "根据ID查询", consumes = "application/x-www-form-urlencoded")
	public String expertsFindById(HttpServletRequest request, @RequestParam String zlid) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
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
			List<Experts> findByIdList = expertsService.expertsFindById(zlid,code);
			if (findByIdList != null && findByIdList.size() > 0) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(findByIdList));
			} else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(" "));
			}
		} catch (Exception e) {
			logger.info("获取树状下级异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/expertsFindByIdTwo")
	@ApiOperation(value = "根据ID查询", httpMethod = "POST", notes = "根据ID查询", consumes = "application/x-www-form-urlencoded")
	public String expertsFindByIdTwo(HttpServletRequest request, @RequestParam String id) {
		JsonMapper jsonMapper = JsonMapper.getInstance();
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User currentUser = apiSystemService.findUserByToken(token);
			String code = expertsService.getPlatformByUser(currentUser);
			List<Experts> findByIdList = expertsService.expertsFindById(id,code);
			if (findByIdList != null && findByIdList.size() > 0) {
				return jsonMapper.toJson(ResultUtil.success(findByIdList));
			} else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(" "));
			}
		} catch (Exception e) {
			logger.info("获取树状下级异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
}
