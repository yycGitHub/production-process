package com.surekam.modules.api.web.sys;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
import com.surekam.common.web.BaseController;
import com.surekam.modules.api.dto.req.RoleReq;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.Role;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.RoleVo;
import com.surekam.modules.sys.service.ApiSystemService;

@Api
@Controller
@RequestMapping(value = "api/role/")
public class RoleApiController extends BaseController {
	
	@Autowired
	private ApiSystemService apiSystemService;
	
	@RequestMapping(value = "list",produces="application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "角色列表查询", 
		notes = "角色列表查询",	consumes="application/json")
	public String list(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String delFlag,
		    @RequestParam(required = false) String orderFields,
			@RequestParam Integer pageno,
			@RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			int pageNo = pageno==null?Global.DEFAULT_PAGENO:pageno;
			int pageSize = pagesize==null?Global.DEFAULT_PAGESIZE:pagesize;
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);		
			Role role = new Role();
			role.setName(name);
			role.setDelFlag(delFlag);
			Page<Role> page = new Page<Role>(pageNo,pageSize);
			page.setOrderBy(orderFields);
			page = apiSystemService.findRole(page, role,currentUser);
			Page<RoleVo> pageVo = new Page<RoleVo>(pageNo,pageSize);
			pageVo.setCount(page.getCount());
			if(page.getList()!=null && page.getList().size()>0){
				List<Role> list = page.getList(); 
				List<RoleVo> listVo = new ArrayList<RoleVo>();
				for(int i=0;i<list.size();i++){
					RoleVo roleVo = new RoleVo();
					BeanUtils.copyProperties(list.get(i), roleVo, new String[]{});
					roleVo.getOffice().setChildList(null);
					listVo.add(roleVo);
				}
				pageVo.setList(listVo);
			}
			return jsonMapper.toJson(ResultUtil.success(pageVo));
		} catch (Exception e) {
			logger.error("角色列表查询错误："+e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}

	}
	
	@RequestMapping(value = "getInfo",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "获取角色详细信息", httpMethod = "POST", 
		notes = "获取角色详细信息",	consumes="application/json")
	public String getInfo(HttpServletResponse response,
			@RequestParam(required = false) String roleId,
			@RequestParam String token) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			Role role = apiSystemService.getRole(roleId);		
			// 判断显示的用户是否在授权范围内
			User currentUser = apiSystemService.findUserByToken(token);
			if (!currentUser.isAdmin()) {
				List<String> roleIds =currentUser.getRoleIdList();
				if (roleIds.contains(roleId)) {
					resultBean =ResultUtil.error(ResultEnum.SYSTEM_NO_AUTH_USER.getCode(), ResultEnum.SYSTEM_NO_AUTH_USER.getMessage());
					return jsonMapper.toJson(resultBean);
				}
			}	
			RoleVo roleVo = new RoleVo();
			BeanUtils.copyProperties(role,roleVo);
			return jsonMapper.toJson(ResultUtil.success(roleVo));
		} catch (Exception e) {
			logger.error("获取角色详细信息错误："+e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@RequestMapping(value = "saveOrUpdate",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "保存或修改角色信息", httpMethod = "POST", 
		notes = "保存或修改角色信息",	consumes="application/json")
	public String saveOrUpdate(HttpServletRequest request,HttpServletResponse response,
			@RequestBody @ApiParam(name = "新增基地信息", value = "传入json格式", required = true) RoleReq roleReq) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<RoleVo> resultBean = new ResultBean<RoleVo>();
		try {
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);	
			Role role = new Role();
			if(StringUtils.isNotBlank(roleReq.getId())){
				role = apiSystemService.getRole(roleReq.getId());
				if(role == null){
					logger.error("保存或更新角色信息错误：角色ID不存在！");
					resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
					return jsonMapper.toJson(resultBean);
				}
				role.setUpdateBy(currentUser);
				role.setUpdateDate(new Date());
			}else{
				role.setCreateBy(currentUser);
				role.setCreateDate(new Date());
			}
			Office office =apiSystemService.getOfficeById(roleReq.getOfficeId());
			if(office == null){
				logger.error("保存或更新角色信息错误：机构不存在！");
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			role.setOffice(office);
			role.setName(roleReq.getName());
			role.setDataScope(roleReq.getDataScope());
			if(roleReq.getCheckedMenuData() != null && roleReq.getCheckedMenuData().size()>0){
				role.setMenuIdList(roleReq.getCheckedMenuData());
			}
			if(roleReq.getCheckedOfficeData() != null && roleReq.getCheckedOfficeData().size()>0){
				role.setOfficeIdList(roleReq.getCheckedOfficeData());
			}
			apiSystemService.saveRole(role);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("保存或更新用户信息错误："+e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@RequestMapping(value = "setttingAddressBook",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "设置通讯录", httpMethod = "POST", 
		notes = "设置通讯录",	consumes="application/json")
	public String setttingAddressBook(HttpServletRequest request,HttpServletResponse response,
			@RequestBody @ApiParam(name = "新增基地信息", value = "传入json格式", required = true) RoleReq roleReq) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<RoleVo> resultBean = new ResultBean<RoleVo>();
		try {
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);	
			Role role = new Role();
			if(StringUtils.isNotBlank(roleReq.getId())){
				role = apiSystemService.getRole(roleReq.getId());
				if(role == null){
					logger.error("保存或更新角色信息错误：角色ID不存在！");
					resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
					return jsonMapper.toJson(resultBean);
				}
				role.setBookName(roleReq.getBookName());
				role.setUpdateBy(currentUser);
				role.setUpdateDate(new Date());
				apiSystemService.saveRole(role);
			}
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("保存或更新用户信息错误："+e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	/**
	 * 恢复
	 * @return
	 */
	@RequestMapping(value = "enable",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "角色恢复", httpMethod = "POST", 
	notes = "角色恢复",	consumes="application/x-www-form-urlencoded")
	public String enable(HttpServletRequest request,@RequestParam String id){
		apiSystemService.roleChangeState(id,Role.DEL_FLAG_NORMAL);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
	}
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(value = "delete",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "角色删除", httpMethod = "POST", 
	notes = "角色删除",	consumes="application/x-www-form-urlencoded")
	public String delete(HttpServletRequest request,@RequestParam String id){
		apiSystemService.roleChangeState(id,Role.DEL_FLAG_DELETE);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
	}
	
	@RequestMapping(value = "findRoles",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "获取登录用户的角色列表", httpMethod = "POST", 
		notes = "获取登录用户的角色列表",	consumes="application/json")
	public String findRoles(HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			List<Role> roles = apiSystemService.findAllRole(currentUser);
			List<RoleVo> roleVos = new ArrayList<RoleVo>();
			for(int i=0;i<roles.size();i++){
				RoleVo roleVo = new RoleVo();
				roleVo.setId(roles.get(i).getId());
				roleVo.setName(roles.get(i).getName());
				roleVos.add(roleVo);
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(roleVos));
		} catch (Exception e) {
			logger.error("获取登录用户的角色列表错误："+e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@RequestMapping(value = "getRoleByUserId",produces="application/json;charset=UTF-8",method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "userId查角色",httpMethod = "GET",  notes = "userId查角色",	consumes="application/json")
	public String getRoleByUserId(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) String userId) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			List<Role> list = new ArrayList<Role>();
			list = 	apiSystemService.getRoleByUserId(userId);	
			return jsonMapper.toJson(ResultUtil.success(list));
		} catch (Exception e) {
			logger.error("角色列表查询错误："+e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}

	}
}
