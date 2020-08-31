package com.surekam.modules.api.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.api.entity.Token;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.Area;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.Role;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.ApiSystemService;
import com.surekam.modules.sys.service.OfficeService;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.utils.SendMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@Controller
@RequestMapping(value = "api")
public class ApiController extends BaseController {
	
	@Autowired
	private ApiUserService apiUserService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private ApiSystemService apiSystemService;

	@ResponseBody
	@RequestMapping(value = "/login",method=RequestMethod.POST)
	@ApiOperation(value = "系统登录", httpMethod = "POST", notes = "系统登录",
				consumes="application/x-www-form-urlencoded")
	public String gettoken(HttpServletResponse response,
			@RequestParam String loginName,
			@RequestParam String pwd) {
		response.setContentType("application/json; charset=UTF-8");
		try {
			if(StringUtils.isNotBlank(loginName) && StringUtils.isNotBlank(pwd)){
				String token =apiUserService.login(loginName,pwd);
				//apiUserService.saveSYToken(loginName,token);
				if("1".equals(token)){
					return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.LOGIN_FAILED.getCode(), ResultEnum.LOGIN_FAILED.getMessage()));
				}else{
					User user = apiUserService.getUserByToken(token);
					Map<String,Object> map = new HashMap<String, Object>();
					if (user.getOffice() != null) {
						Office office = apiSystemService.getOfficeById(user.getOffice().getId());
						if (office.getId().equals("1")
								|| StringUtils.isBlank(office.getName())) {
							map.put("officeName","农科院信息所农事标准化管理平台");
						} else {
							map.put("officeName",office.getName());
						}
						map.put("officeLog",office.getOfficeLogo());
					}
					map.put("token", token);
					map.put("userToken", user.getUserToken());
					map.put("roleList", user.getRoleList());
					return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
				}
			}else{
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.LOGIN_FAILED.getCode(), ResultEnum.LOGIN_FAILED.getMessage()));
			}
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping(value = "/skipLogin",method=RequestMethod.POST)
	@ApiOperation(value = "系统演示免登", httpMethod = "POST", notes = "系统演示免登",
				consumes="application/x-www-form-urlencoded")
	public String skipLogin(HttpServletResponse response,
			@RequestParam String userId) {
		response.setContentType("application/json; charset=UTF-8");
		try {
			if(StringUtils.isNotBlank(userId)){
				String token =apiUserService.skipLogin(userId);
				if("1".equals(token)){
					return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.BAD_REQ_PARAM.getCode(), ResultEnum.BAD_REQ_PARAM.getMessage()));
				}else{
					User user = apiUserService.getUserByToken(token);
					Map<String,Object> map = new HashMap<String, Object>();
					if (user.getOffice() != null) {
						Office office = apiSystemService.getOfficeById(user
								.getOffice().getId());
						if (office.getId().equals("1")
								|| StringUtils.isBlank(office.getName())) {
							map.put("officeName","农科院信息所农事标准化管理平台");
						} else {
							map.put("officeName",office.getName());
						}
						map.put("officeLog",office.getOfficeLogo());
					}
					map.put("token", token);
					map.put("roleList", user.getRoleList());
					return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
				}
			}else{
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.BAD_REQ_PARAM.getCode(), ResultEnum.BAD_REQ_PARAM.getMessage()));
			}
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage()));
		}
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/skipLogin2",method=RequestMethod.POST)
	@ApiOperation(value = "展示系统免登录", httpMethod = "POST", notes = "系统登录",consumes="application/x-www-form-urlencoded")
	public String skipLogin2(HttpServletResponse response,
			@RequestParam String loginName) {
		response.setContentType("application/json; charset=UTF-8");
		try {
			if(StringUtils.isNotBlank(loginName)){
				String token =apiUserService.login(loginName);
				if("1".equals(token)){
					return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.LOGIN_FAILED.getCode(), ResultEnum.LOGIN_FAILED.getMessage()));
				}else{
					User user = apiUserService.getUserByToken(token);
					Map<String,Object> map = new HashMap<String, Object>();
					if (user.getOffice() != null) {
						Office office = apiSystemService.getOfficeById(user.getOffice().getId());
						if (office.getId().equals("1")
								|| StringUtils.isBlank(office.getName())) {
							map.put("officeName","农科院信息所农事标准化管理平台");
						} else {
							map.put("officeName",office.getName());
						}
						map.put("officeLog",office.getOfficeLogo());
					}
					map.put("token", token);
					map.put("userToken", user.getUserToken());
					map.put("roleList", user.getRoleList());
					return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
				}
			}else{
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.LOGIN_FAILED.getCode(), ResultEnum.LOGIN_FAILED.getMessage()));
			}
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage()));
		}
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/register",method=RequestMethod.POST)
	@ApiOperation(value = "系统注册", httpMethod = "POST", notes = "系统注册",
				consumes="application/x-www-form-urlencoded")
	public String register(HttpServletResponse response,
			HttpServletRequest request,
			@RequestParam String companyName,
			@RequestParam String phone,
			@RequestParam String pwd,
			@RequestParam String confirmPwd,
			@RequestParam String validateCode) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Token> resultBean = new ResultBean<Token>();
		try {
			if(phone != null && pwd != null && confirmPwd != null && validateCode != null) {
				if(pwd.equals(confirmPwd)) {
					User tempUser = systemService.getUserByLoginName(phone);
					if(tempUser == null || StringUtils.isBlank(tempUser.getId())) {
						String validationCode = (String) request.getSession().getAttribute(phone);
						if(validationCode != null && validateCode.equals(validationCode)) {
							
							List<Office> list = new ArrayList<Office>();
							list = officeService.findBycompanyName(companyName);
							Office office = new Office();
							if(list == null || list.size() == 0) {
								//添加公司
								office.setName(companyName);
								office.setParent(new Office("1"));
								office.setParentIds(office.getParent().getId()+",");
								office.setArea(new Area("1"));
								office.setType("1");
								office.setGrade("1");
								int code = officeService.getMaxOfficeCode() + 1;
								office.setCode(String.valueOf(code));
								officeService.save(office);
								//添加用户
								User user = new User();
								user.setCompany(office);
								user.setOffice(office);
								user.setLoginName(phone);
								user.setPhone(phone);
								user.setName(phone);
								user.setPassword(SystemService.entryptPassword(pwd));
								Role role = new Role("1852c8e247744ff184e8c162eff44f4c", "公司管理角色");
								List<Role> roleList = new ArrayList<Role>();
								roleList.add(role);
								user.setRoleList(roleList);
								systemService.saveUser(user);
							}else {
								office = list.get(0);
								//添加用户
								User user = new User();
								user.setCompany(office);
								user.setOffice(office);
								user.setLoginName(phone);
								user.setPhone(phone);
								user.setName(phone);
								user.setPassword(SystemService.entryptPassword(pwd));
								user.setDelFlag("1");
								Role role = new Role("b3c5cff1bb574c66bcec902829e54680", "普�?管理角色");
								List<Role> roleList = new ArrayList<Role>();
								roleList.add(role);
								user.setRoleList(roleList);
								systemService.saveUser(user);
							}
							
							
							/*Office office = new Office();
							List<Office> list = new ArrayList<Office>();
							list = officeService.findByinvitationCode(invitationCode);
							if(list == null || list.size() == 0) {
								resultBean = ResultUtil.error(ResultEnum.INVITATIONCODE_IS_INVALID.getCode(),ResultEnum.INVITATIONCODE_IS_INVALID.getMessage());
								return jsonMapper.toJson(resultBean);
							}else {
								office = list.get(0);
							}*/
						
							//登录
							String token =apiUserService.login(phone,pwd);
							if("1".equals(token)){
								resultBean = ResultUtil.error(ResultEnum.LOGIN_AUDIT.getCode(),ResultEnum.LOGIN_AUDIT.getMessage());
							}else{
								resultBean = ResultUtil.success(new Token(token));
							}
							return jsonMapper.toJson(resultBean);
						} else {
							resultBean = ResultUtil.error(ResultEnum.REGISTER_VALIDATION_CODE_ERROR.getCode(),ResultEnum.REGISTER_VALIDATION_CODE_ERROR.getMessage());
							return jsonMapper.toJson(resultBean);
						}
					} else {
						resultBean = ResultUtil.error(ResultEnum.REGISTER_PHONE_EXSIST.getCode(),ResultEnum.REGISTER_PHONE_EXSIST.getMessage());
						return jsonMapper.toJson(resultBean);
					}
				} else {
					resultBean = ResultUtil.error(ResultEnum.REGISTER_CONFIRM_PASSWORD_ERROR.getCode(),ResultEnum.REGISTER_CONFIRM_PASSWORD_ERROR.getMessage());
					return jsonMapper.toJson(resultBean);
				}
			} else {
				resultBean = ResultUtil.error(ResultEnum.BAD_REQ_PARAM.getCode(),ResultEnum.BAD_REQ_PARAM.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		} catch (Exception e) {
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/resetPass",method=RequestMethod.POST)
	@ApiOperation(value = "重置密码", httpMethod = "POST", notes = "重置密码", consumes="application/x-www-form-urlencoded")
	public String resetPass(HttpServletResponse response, HttpServletRequest request, @RequestParam String phone,
			@RequestParam String resetPwd, @RequestParam String validateCode) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Token> resultBean = new ResultBean<Token>();
		try {
			if (phone != null && resetPwd != null && validateCode != null) {
				User user = systemService.getUserByLoginName(phone);
				if (null != user && StringUtils.isNotBlank(user.getId())) {
					String validationCode = (String) request.getSession().getAttribute(phone);
					if (validationCode != null && validateCode.equals(validationCode)) {
						// 添加用户
						user.setPassword(SystemService.entryptPassword(resetPwd));
						systemService.saveUser(user);
						// 登录
						String token = apiUserService.login(phone, resetPwd);
						if ("1".equals(token)) {
							resultBean = ResultUtil.error(ResultEnum.LOGIN_FAILED.getCode(), ResultEnum.LOGIN_FAILED.getMessage());
						} else {
							resultBean = ResultUtil.success(new Token(token));
						}
						return jsonMapper.toJson(resultBean);
					} else {
						resultBean = ResultUtil.error(ResultEnum.REGISTER_VALIDATION_CODE_ERROR.getCode(), ResultEnum.REGISTER_VALIDATION_CODE_ERROR.getMessage());
						return jsonMapper.toJson(resultBean);
					}
				} else {
					resultBean = ResultUtil.error(ResultEnum.REGISTER_PHONE_NOT_EXSIST.getCode(), ResultEnum.REGISTER_PHONE_NOT_EXSIST.getMessage());
					return jsonMapper.toJson(resultBean);
				}
			} else {
				resultBean = ResultUtil.error(ResultEnum.BAD_REQ_PARAM.getCode(), ResultEnum.BAD_REQ_PARAM.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		} catch (Exception e) {
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/resetPass2", method = RequestMethod.POST)
	@ApiOperation(value = "重置密码PC", httpMethod = "POST", notes = "重置密码PC", consumes = "application/x-www-form-urlencoded")
	public String resetPassPC(HttpServletResponse response, HttpServletRequest request, @RequestParam String loginName, @RequestParam String resetPwd) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Token> resultBean = new ResultBean<Token>();
		try {
			if (loginName != null && resetPwd != null) {
				User user = systemService.getUserByLoginName(loginName);
				if (null != user && StringUtils.isNotBlank(user.getId())) {
					// 添加用户
					user.setPassword(SystemService.entryptPassword(resetPwd));
					systemService.saveUser(user);
					// 登录
					String token = apiUserService.login(loginName, resetPwd);
					if ("1".equals(token)) {
						resultBean = ResultUtil.error(ResultEnum.LOGIN_FAILED.getCode(), ResultEnum.LOGIN_FAILED.getMessage());
					} else {
						resultBean = ResultUtil.success(new Token(token));
					}
					return jsonMapper.toJson(resultBean);
				} else {
					resultBean = ResultUtil.error(ResultEnum.REGISTER_PHONE_NOT_EXSIST.getCode(), ResultEnum.REGISTER_PHONE_NOT_EXSIST.getMessage());
					return jsonMapper.toJson(resultBean);
				}
			} else {
				resultBean = ResultUtil.error(ResultEnum.BAD_REQ_PARAM.getCode(), ResultEnum.BAD_REQ_PARAM.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		} catch (Exception e) {
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/sendMessage",method=RequestMethod.POST)
	@ApiOperation(value = "发�?短信验证", httpMethod = "POST", notes = "发�?短信验证",
				consumes="application/x-www-form-urlencoded")
	public String sendMessage(HttpServletResponse response,HttpServletRequest request,
			@RequestParam String phone) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			if(phone != null) {
				SendMessage sendMessage = new SendMessage();
				String validationCode = sendMessage.getValidationCode();
				String result = sendMessage.sendValidationMessage(phone, validationCode);
				if(StringUtils.isBlank(result)){
					resultBean = ResultUtil.error(ResultEnum.GET_VALIDATION_CODE_ERROR.getCode(),ResultEnum.GET_VALIDATION_CODE_ERROR.getMessage());
					return jsonMapper.toJson(resultBean);
				}
				Map<String, String> map = new HashMap<String, String>();
				map = (Map<String, String>) jsonMapper.fromJson(result, Object.class);
				String code = map.get("code");
				String message = map.get("msg");
				String data = map.get("result");
				resultBean.setCode(Integer.parseInt(code));
				resultBean.setMessage(message);
				resultBean.setBodyData(data);
				//10分钟有效�?
				request.getSession().setMaxInactiveInterval(60 * 10);
				request.getSession().setAttribute(phone, validationCode);
				
				//User user = systemService.getUserByLoginName(phone);
				//user.setValidationCode(validationCode);
				//systemService.saveUser(user);
  				return jsonMapper.toJson(resultBean);
			} else {
				resultBean = ResultUtil.error(ResultEnum.BAD_REQ_PARAM.getCode(),ResultEnum.BAD_REQ_PARAM.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		} catch (Exception e) { 
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
}
