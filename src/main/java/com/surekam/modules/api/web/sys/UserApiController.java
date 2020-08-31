package com.surekam.modules.api.web.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.WebRTCSigApi;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsGoodproblemService;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.api.dto.req.UserQuery;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.Role;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.UserVo;
import com.surekam.modules.sys.service.ApiSystemService;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@Controller
@RequestMapping(value = "api/user/")
public class UserApiController extends BaseController {

	@Autowired
	private ApiSystemService apiSystemService;
	@Autowired
	private UserService userService;
	@Autowired
	private ApiUserService apiUserService;
	@Autowired
	private ExpertsService expertsService;
	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;

	@Autowired
	private ExpertsGoodproblemService expertsGoodproblemService;

	@RequestMapping(value = "list", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "用户列表查询", notes = "用户列表查询", consumes = "application/json")
	public String list(HttpServletRequest request, HttpServletResponse response,
			@RequestBody UserQuery userQuery) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			int pageNo = userQuery.getPageno() == null ? Global.DEFAULT_PAGENO : userQuery.getPageno();
			int pageSize = userQuery.getPagesize() == null ? Global.DEFAULT_PAGESIZE : userQuery.getPagesize();
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			User user = new User();
			user.setLoginName(userQuery.getLoginName());
			user.setName(userQuery.getName());
			if (StringUtils.isBlank(userQuery.getCompanyId())) {
				userQuery.setCompanyId(currentUser.getCompany().getId());
			}
			user.setCompany(new Office(userQuery.getCompanyId()));
			user.setOffice(new Office(userQuery.getOfficeId()));
			user.setDelFlag(userQuery.getDelFlag());
			Page<User> page = new Page<User>(pageNo, pageSize);
			page.setOrderBy(userQuery.getOrderFields());
			page = apiSystemService.findUser(page, user, currentUser);
			Page<UserVo> pageVo = new Page<UserVo>(pageNo, pageSize);
			pageVo.setCount(page.getCount());
			if (page.getList() != null && page.getList().size() > 0) {
				List<User> list = page.getList();
				List<UserVo> listVo = new ArrayList<UserVo>();
				for (int i = 0; i < list.size(); i++) {
					UserVo userVo = new UserVo();
					User userTemp = list.get(i);
					BeanUtils.copyProperties(userTemp, userVo, new String[] { "password" });
					if (userTemp.getCompany() != null) {
						userVo.setCompanyId(userTemp.getCompany().getId());
					}
					if (userTemp.getOffice() != null) {
						userVo.setOfficeId(userTemp.getOffice().getId());
					}
					listVo.add(userVo);
				}
				pageVo.setList(listVo);
			}
			return jsonMapper.toJson(ResultUtil.success(pageVo));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}

	}

	@RequestMapping(value = "getUserInfo", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取用户详细信息", httpMethod = "POST", notes = "获取用户详细信息", consumes = "application/json")
	public String getUserInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String userId) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			User user = apiSystemService.getUser(userId);
			// 判断显示的用户是否在授权范围内
			String officeId = user.getOffice().getId();
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			if (!currentUser.isAdmin()) {
				String dataScope = apiSystemService.getDataScope(currentUser);
				if (dataScope.indexOf("office.id=") != -1) {
					String AuthorizedOfficeId = dataScope.substring(dataScope.indexOf("office.id=") + 10,
							dataScope.indexOf(" or"));
					if (!AuthorizedOfficeId.equalsIgnoreCase(officeId)) {
						resultBean = ResultUtil.error(ResultEnum.SYSTEM_NO_AUTH_USER.getCode(),
								ResultEnum.SYSTEM_NO_AUTH_USER.getMessage());
						return jsonMapper.toJson(resultBean);
					}
				}
			}
			UserVo userVo = new UserVo();
			BeanUtils.copyProperties(user, userVo);
			userVo.setCompanyId(user.getCompany().getId());
			userVo.setOfficeId(user.getOffice().getId());
			return jsonMapper.toJson(ResultUtil.success(userVo));
		} catch (Exception e) {
			logger.error("获取用户详细信息错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	@RequestMapping(value = "save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "保存用户信息", httpMethod = "POST", notes = "保存用户信息", consumes = "application/json")
	public String save(HttpServletRequest request, HttpServletResponse response, @RequestBody UserVo userVo) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
		try {
			User user = new User();
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			BeanUtils.copyProperties(userVo, user, new String[] { "id", "loginIp", "loginDate", "createDate", "updateDate", "delFlag" });
			user.setCompany(new Office(userVo.getCompanyId()));
			user.setOffice(new Office(userVo.getCompanyId()));
			// 登录后台时密码必须6位数，所以新增时加上这个判断
			if (userVo.getPassword().length() < 6) {
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_USER_PASSWORD_LENGTH.getCode(), ResultEnum.SYSTEM_USER_PASSWORD_LENGTH.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			user.setPassword(SystemService.entryptPassword(userVo.getPassword()));
			if (StringUtils.isNotBlank(userVo.getUserImg())) {
				user.setUserImg(userVo.getUserImg());
			}
			if (userVo.getRoleListIds().size() == 0) {
				// 如果没有选择角色，用户登录不上后台
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_USER_ROLE.getCode(), ResultEnum.SYSTEM_USER_ROLE.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			// 筛选掉不在授权范围内的角色，避免被非法授权
			List<String> roleIds = userVo.getRoleListIds();
			List<Role> roleList = Lists.newArrayList();
			if (roleIds != null) {
				for (Role r : apiSystemService.findAllRole(currentUser)) {
					if (roleIds.contains(r.getId())) {
						roleList.add(r);
					}
				}
			}
			user.setRoleList(roleList);
			user.setCreateBy(currentUser);
			if (apiSystemService.existUserName(user)) {
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_USER_EXIST.getCode(), ResultEnum.SYSTEM_USER_EXIST.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			if (StringUtils.isNotBlank(userVo.getPhone())) {
				user.setPhone(userVo.getPhone());
			}
			if (StringUtils.isNotBlank(userVo.getEmail())) {
				user.setEmail(userVo.getEmail());
			}
			
			String privateMapKey = WebRTCSigApi.getPrivateMapKey_book(user.getLoginName());
			user.setUserToken(privateMapKey);
			apiSystemService.saveUser(user);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("保存用户信息错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	@RequestMapping(value = "update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "更新用户信息", httpMethod = "POST", notes = "更新用户信息", consumes = "application/json")
	public String update(HttpServletRequest request, HttpServletResponse response, @RequestBody UserVo userVo) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
		try {
			User user = new User();
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			if (StringUtils.isNotBlank(userVo.getId()) && StringUtils.isNotBlank(userVo.getCompanyId()) && StringUtils.isNotBlank(userVo.getOfficeId())) {
				user = apiSystemService.getUser(userVo.getId());
				user.setName(userVo.getName());
				if (StringUtils.isNotBlank(userVo.getPassword())) {
					if (userVo.getPassword().length() < 6) {
						resultBean = ResultUtil.error(ResultEnum.SYSTEM_USER_PASSWORD_LENGTH.getCode(), ResultEnum.SYSTEM_USER_PASSWORD_LENGTH.getMessage());
						return jsonMapper.toJson(resultBean);
					}
					user.setPassword(SystemService.entryptPassword(userVo.getPassword()));
				}
				if (StringUtils.isNotBlank(userVo.getUserImg())) {
					user.setUserImg(userVo.getUserImg());
				}
				user.setCompany(new Office(userVo.getCompanyId()));
				user.setOffice(new Office(userVo.getCompanyId()));
			} else {
				logger.error("更新用户信息错误：参数不正确");
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			// 筛选掉不在授权范围内的角色，避免被非法授权
			List<Role> roleList = Lists.newArrayList();
			if (userVo.getRoleListIds() != null) {
				for (Role r : apiSystemService.findAllRole(currentUser)) {
					for (String rid : userVo.getRoleListIds()) {
						if (r.getId().equals(rid)) {
							roleList.add(r);
						}
					}
				}
			}
			if (roleList.size() == 0) {
				// 如果没有选择角色，用户登录不上后台
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_USER_ROLE.getCode(),
						ResultEnum.SYSTEM_USER_ROLE.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			user.setRoleList(roleList);
			if (StringUtils.isNotBlank(userVo.getPhone())) {
				user.setPhone(userVo.getPhone());
			}
			if (StringUtils.isNotBlank(userVo.getEmail())) {
				user.setEmail(userVo.getEmail());
			}
			if(StringUtils.isBlank(user.getUserToken())){
				String privateMapKey = WebRTCSigApi.getPrivateMapKey_book(user.getLoginName());
				user.setUserToken(privateMapKey);
			}
			apiSystemService.saveUser(user);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("更新用户信息错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	/**
	 * 恢复
	 * 
	 * @return
	 */
	@RequestMapping(value = "enable", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "用户恢复", httpMethod = "POST", notes = "用户恢复", consumes = "application/x-www-form-urlencoded")
	public String enable(HttpServletRequest request, @RequestParam String id) {
		apiSystemService.userChangeState(id, User.DEL_FLAG_NORMAL);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping(value = "delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "用户删除", httpMethod = "POST", notes = "用户删除", consumes = "application/x-www-form-urlencoded")
	public String delete(HttpServletRequest request, @RequestParam String id) {
		apiSystemService.userChangeState(id, User.DEL_FLAG_DELETE);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
	}

	/**
	 * 手机端获取个人信息
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "getUserInfo2", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取用户详细信息", httpMethod = "POST", notes = "获取用户详细信息", consumes = "application/json")
	public String getUserInfo2(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String userId) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			// 判断显示的用户是否在授权范围内
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			String officeId = currentUser.getOffice().getId();
			if (!currentUser.isAdmin()) {
				String dataScope = apiSystemService.getDataScope(currentUser);
				if (dataScope.indexOf("office.id=") != -1) {
					String AuthorizedOfficeId = dataScope.substring(dataScope.indexOf("office.id=") + 10,
							dataScope.indexOf(" or"));
					if (!AuthorizedOfficeId.equalsIgnoreCase(officeId)) {
						resultBean = ResultUtil.error(ResultEnum.SYSTEM_NO_AUTH_USER.getCode(),
								ResultEnum.SYSTEM_NO_AUTH_USER.getMessage());
						return jsonMapper.toJson(resultBean);
					}
				}
			}
			UserVo userVo = new UserVo();
			BeanUtils.copyProperties(currentUser, userVo);
			userVo.setCompanyId(currentUser.getCompany().getId());
			userVo.setOfficeId(currentUser.getOffice().getId());
			// 判断角色是否是专家
			if (userVo.getRoleList().get(0).getId().equals("7b18d965552a4a2286a35dd94fed0b2a")) {
				Experts experts = expertsService.getExperts(userVo.getId());
				experts.setProductLibraryList(productLibraryTreeService.getList(experts.getId()));
				experts.setExpertsGoodproblemList(expertsGoodproblemService.getList(experts.getId()));
				userVo.setExperts(experts);
			}
			return jsonMapper.toJson(ResultUtil.success(userVo));
		} catch (Exception e) {
			logger.error("获取用户详细信息错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	/**
	 * 手机端--个人信息--编辑姓名
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "editName", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "手机端--个人信息--编辑姓名", httpMethod = "POST", notes = "手机端--个人信息--编辑姓名", consumes = "application/x-www-form-urlencoded")
	public String editName(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String userId, @RequestParam(required = false) String userName) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		// 判断显示的用户是否在授权范围内
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User user2 = apiUserService.getUserByToken(token);
			try {
				User user = new User();
				if (StringUtils.isNotBlank(userId)) {
					user = userService.get(userId);
				}
				Experts experts = expertsService.getExperts(userId);
				if (StringUtils.isNotBlank(userName)) {
					user.setName(userName);
					user.setUpdateDate(new Date());
					user.setUpdateBy(user2);
					userService.save(user);
					if (experts != null) {
						experts.setExpertName(userName);
						experts.setUpdateTime(new Date());
						experts.setUpdateUserId(user.getId());
						expertsService.save(experts);
					}
				}
				return jsonMapper.toJson(ResultUtil.success(user));
			} catch (Exception e) {
				logger.error("编辑姓名错误：" + e);
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

	/**
	 * 手机端--个人信息--编辑照片
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "editPhone", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "手机端--个人信息--编辑手机号", httpMethod = "POST", notes = "手机端--个人信息--编辑手机号", consumes = "application/x-www-form-urlencoded")
	public String editPhone(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String userId, @RequestParam(required = false) String userPhone) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		// 判断显示的用户是否在授权范围内
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User user2 = apiUserService.getUserByToken(token);
			try {
				User user = new User();
				if (StringUtils.isNotBlank(userId)) {
					user = userService.get(userId);
				}
				if (StringUtils.isNotBlank(userPhone)) {
					user.setPhone(userPhone);
					user.setUpdateDate(new Date());
					user.setUpdateBy(user2);
					userService.save(user);
				}
				return jsonMapper.toJson(ResultUtil.success(user));
			} catch (Exception e) {
				logger.error("编辑手机号错误：" + e);
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

	/**
	 * 手机端--个人信息--编辑照片
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "editPhoto", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "手机端--个人信息--编辑照片", httpMethod = "POST", notes = "手机端--个人信息--编辑照片", consumes = "application/x-www-form-urlencoded")
	public String editPhoto(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String userId, @RequestParam(required = false) String userImg) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		// 判断显示的用户是否在授权范围内
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User user2 = apiUserService.getUserByToken(token);
			try {
				User user = new User();
				if (StringUtils.isNotBlank(userId)) {
					user = userService.get(userId);
				}
				if (StringUtils.isNotBlank(userImg)) {
					user.setUserImg(userImg);
					user.setUpdateDate(new Date());
					user.setUpdateBy(user2);
					userService.save(user);
				}
				return jsonMapper.toJson(ResultUtil.success(user));
			} catch (Exception e) {
				logger.error("上传照片错误：" + e);
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@RequestMapping(value = "getUser", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取用户详细信息", httpMethod = "POST", notes = "获取用户详细信息", consumes = "application/json")
	public String getUser(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User currentUser = apiSystemService.findUserByToken(token);
			return jsonMapper.toJson(ResultUtil.success(currentUser));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@RequestMapping(value = "getReportUser",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "查询接收汇报的用户", httpMethod = "GET", notes = "查询接收汇报的用户",	consumes="application/x-www-form-urlencoded")
	public String getReportUser(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			List<User> list  = userService.getUserList();
			return jsonMapper.toJson(ResultUtil.success(list));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		
	}
	
	@RequestMapping(value = "getUser2", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取用户的OfficeId", httpMethod = "POST", notes = "获取用户的OfficeId", consumes = "application/json")
	public String getUser2(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (StringUtils.isNotBlank(token)) {
			User currentUser = apiSystemService.findUserByToken(token);
			return JsonMapper.toJsonString(ResultUtil.success(currentUser.getOffice().getId()));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
}
