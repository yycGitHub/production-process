package com.surekam.modules.api.web.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
import com.google.common.collect.Maps;
import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ChineseCharToEnUtil;
import com.surekam.common.utils.MapKeyComparator;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.entity.Area;
import com.surekam.modules.sys.entity.BusinessLicense;
import com.surekam.modules.sys.entity.Dict;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.ProductionCertificate;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.Enterprise;
import com.surekam.modules.sys.entity.vo.OfficeTreeVo;
import com.surekam.modules.sys.entity.vo.OfficeVo;
import com.surekam.modules.sys.entity.vo.UserVo;
import com.surekam.modules.sys.service.ApiDictService;
import com.surekam.modules.sys.service.ApiSystemService;
import com.surekam.modules.sys.service.BusinessLicenseService;
import com.surekam.modules.sys.service.OfficeService;
import com.surekam.modules.sys.service.ProductionCertificateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@Controller
@RequestMapping(value = "api/office/")
public class OfficeApiController extends BaseController {

	@Autowired
	private ApiSystemService apiSystemService;

	@Autowired
	private BusinessLicenseService businessLicenseService;

	@Autowired
	private ProductionCertificateService productionCertificateService;

	@Autowired
	private ApiUserService apiUserService;
	@Autowired
	private OfficeService officeService;

	@Autowired
	private ExpertsService expertsService;

	@Autowired
	private ApiDictService apiDictService;

	@RequestMapping(value = "getOffices", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取公司或者部门列�?", httpMethod = "POST", notes = "获取公司或者部门列�?", consumes = "application/json")
	public String getOffices(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) Long type, @RequestParam(required = false) String itemId) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			String token = request.getHeader("X-Token");
			List<Map<String, Object>> mapList = Lists.newArrayList();
			User currentUser = apiSystemService.findUserByToken(token);
			List<Office> list = apiSystemService.findOffices(currentUser, itemId, true);
			for (int i = 0; i < list.size(); i++) {
				Office e = list.get(i);
				if (type == 2) {
					if (!"1".equals(e.getId())) {
						Map<String, Object> map = Maps.newHashMap();
						map.put("id", e.getId());
						map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
						map.put("name", e.getName());
						mapList.add(map);
					}
				} else if ((type == null || (type != null && Integer.parseInt(e.getType()) <= type.intValue()))) {
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", e.getId());
					map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
					map.put("name", e.getName());
					mapList.add(map);
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(mapList));
		} catch (Exception e) {
			logger.error("获取登录用户的角色列表错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	@RequestMapping(value = "getOfficeLists", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取公司或者部门列�?", httpMethod = "POST", notes = "获取公司或者部门列�?", consumes = "application/json")
	public String getOfficeLists(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			String token = request.getHeader("X-Token");
			List<Office> officeList = Lists.newArrayList();
			User currentUser = apiSystemService.findUserByToken(token);
			if (currentUser.isAdmin()) {
				officeList = officeService.findAll();
			} else {
				officeList = officeService.findChildrenOfficeList(currentUser.getCompany().getId());
			}
			List<Map<String, Object>> mapList = Lists.newArrayList();
			for (int i = 0; i < officeList.size(); i++) {
				Office e = officeList.get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				// map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
				map.put("name", e.getName());
				mapList.add(map);
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(mapList));
		} catch (Exception e) {
			logger.error("获取登录用户的角色列表错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	@RequestMapping(value = "getElmentTreeOffices", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取机构树数�?", httpMethod = "POST", notes = "获取机构树数�?", consumes = "application/json")
	public String getElmentTreeOffices(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String itemId, @RequestParam(required = false) boolean onlyNext) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			if (currentUser != null && currentUser.getCompany() != null) {
				itemId = currentUser.getCompany().getId();
			}
			if (StringUtils.isBlank(itemId) || currentUser.isAdmin()) {
				itemId = "1";
			}
			Office office = apiSystemService.getOfficeById(itemId);
			if (office == null) {
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			List<Office> list = apiSystemService.findOffices(currentUser, itemId, onlyNext);
			OfficeVo vo = treeList(list, office);
			List<OfficeVo> vos = new ArrayList<OfficeVo>();
			vos.add(vo);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(vos));
		} catch (Exception e) {
			logger.error("获取机构树数据错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	@RequestMapping(value = "getOfficesTree", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取公司树", httpMethod = "POST", notes = "获取机构树数�?", consumes = "application/json")
	public String getOfficesTree(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String itemId, @RequestParam(required = false) boolean onlyNext) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			if (currentUser != null && currentUser.getCompany() != null) {
				itemId = currentUser.getCompany().getId();
			}
			if (StringUtils.isBlank(itemId) || currentUser.isAdmin()) {
				itemId = "1";
			}
			Office office = apiSystemService.getOfficeById(itemId);
			if (office == null) {
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			List<Office> list = apiSystemService.findOffices(currentUser, itemId, onlyNext);
			OfficeTreeVo vo = treeListTree(list, office);
			List<OfficeTreeVo> vos = new ArrayList<OfficeTreeVo>();
			vos.add(vo);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(vos));
		} catch (Exception e) {
			logger.error("获取机构树数据错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	public OfficeTreeVo treeListTree(List<Office> list, Office parent) {
		OfficeTreeVo officeTreeVo = new OfficeTreeVo();
		BeanUtils.copyProperties(parent, officeTreeVo, new String[] { "children" });
		officeTreeVo.setLabel(parent.getName());
		for (Office officeTemp : list) {
			if (parent.getId().equals(officeTemp.getParent().getId())) {
				OfficeTreeVo officeTreeTemp = new OfficeTreeVo();
				BeanUtils.copyProperties(officeTemp, officeTreeTemp, new String[] { "children" });
				officeTreeTemp.setLabel(officeTemp.getName());
				if (officeTemp.getChildList().size() > 0) {
					officeTreeTemp = treeListTree(list, officeTemp);
				}
				officeTreeVo.getChildren().add(officeTreeTemp);
			}
		}
		return officeTreeVo;
	}

	public OfficeVo treeList(List<Office> list, Office parent) {
		OfficeVo officeVo = new OfficeVo();
		BeanUtils.copyProperties(parent, officeVo, new String[] { "childList" });
		for (Office officeTemp : list) {
			if (parent.getId().equals(officeTemp.getParent().getId())) {
				OfficeVo officeVoTemp = new OfficeVo();
				BeanUtils.copyProperties(officeTemp, officeVoTemp, new String[] { "childList" });
				if (officeTemp.getChildList().size() > 0) {
					officeVoTemp = treeList(list, officeTemp);
				}
				officeVo.getChildList().add(officeVoTemp);
			}
		}
		return officeVo;
	}

	public List<OfficeVo> officeToOfficeVoList(List<Office> list) {
		List<OfficeVo> officeVoList = new ArrayList<OfficeVo>();
		for (Office officeTemp : list) {
			OfficeVo officeVoTemp = new OfficeVo();
			BeanUtils.copyProperties(officeTemp, officeVoTemp, new String[] { "childList" });
			officeVoList.add(officeVoTemp);
		}
		return officeVoList;
	}

	@RequestMapping(value = "list", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "机构列表查询", notes = "机构列表查询", consumes = "application/json")
	public String list(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String name, @RequestParam(required = false) String parentId,
			@RequestParam(required = false) String delFlag, @RequestParam(required = false) Integer pageno,
			@RequestParam(required = false) Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
			int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			Page<Office> page = new Page<Office>(pageNo, pageSize);
			page = apiSystemService.findPageOffices(page, currentUser, name, parentId, delFlag, false);
			Page<OfficeVo> pageVo = new Page<OfficeVo>(pageNo, pageSize);
			pageVo.setCount(page.getCount());
			if (page.getList() != null && page.getList().size() > 0) {
				List<Office> list = page.getList();
				List<OfficeVo> listVo = new ArrayList<OfficeVo>();
				for (int i = 0; i < list.size(); i++) {
					OfficeVo officeVo = new OfficeVo();
					Office officeTemp = list.get(i);
					BeanUtils.copyProperties(officeTemp, officeVo, new String[] { "childList" });
					officeVo.setParentId(officeTemp.getParent().getId());
					officeVo.setParentName(officeTemp.getParent().getName());
					officeVo.setAreaId(officeTemp.getArea().getId());
					officeVo.setAreaName(officeTemp.getArea().getName());
					listVo.add(officeVo);
				}
				pageVo.setList(listVo);
			}
			return jsonMapper.toJson(ResultUtil.success(pageVo));
		} catch (Exception e) {
			logger.error("机构列表查询错误�?" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	@RequestMapping(value = "getOfficeInfo", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取机构详细信息", httpMethod = "POST", notes = "获取机构详细信息", consumes = "application/json")
	public String getOfficeInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String officeId) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			// 判断显示的用户是否在授权范围�?
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
			Office office = apiSystemService.getOfficeById(officeId);
			OfficeVo officeVo = new OfficeVo();
			BeanUtils.copyProperties(office, officeVo, new String[] { "childList" });
			officeVo.setParentId(office.getParent().getId());
			officeVo.setParentName(office.getParent().getName());
			officeVo.setAreaId(office.getArea().getId());
			officeVo.setAreaName(office.getArea().getName());
			return jsonMapper.toJson(ResultUtil.success(officeVo));
		} catch (Exception e) {
			logger.error("获取机构详细信息错误�?" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	@RequestMapping(value = "save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "保存机构信息", httpMethod = "POST", notes = "保存机构信息", consumes = "application/json")
	public String save(HttpServletRequest request, HttpServletResponse response, @RequestBody OfficeVo officeVo) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
		try {
			Office office = new Office();
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			BeanUtils.copyProperties(officeVo, office,
					new String[] { "id", "loginIp", "loginDate", "createDate", "updateDate", "delFlag" });
			Office officePrenat = apiSystemService.getOfficeById(officeVo.getParentId());
			office.setParent(officePrenat);
			office.setParentIds(officePrenat.getParentIds() + officePrenat.getId() + ",");
			office.setArea(new Area(officeVo.getAreaId()));
			office.setCreateBy(currentUser);
			apiSystemService.saveOffice(office);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("保存机构信息错误�?" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	@RequestMapping(value = "update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "更新机构信息", httpMethod = "POST", notes = "更新机构信息", consumes = "application/x-www-form-urlencoded")
	public String update(HttpServletRequest request, HttpServletResponse response, @RequestParam String id, @RequestParam String areaId, @RequestParam String parentId, @RequestParam(required = false) String name) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
		try {
			Office office = new Office();
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			if (!currentUser.isAdmin()) {
				String dataScope = apiSystemService.getDataScope(currentUser);
				if (dataScope.indexOf("office.id=") != -1) {
					String AuthorizedOfficeId = dataScope.substring(dataScope.indexOf("office.id=") + 10,
							dataScope.indexOf(" or"));
					if (!AuthorizedOfficeId.equalsIgnoreCase(id)) {
						resultBean = ResultUtil.error(ResultEnum.SYSTEM_NO_AUTH_USER.getCode(),
								ResultEnum.SYSTEM_NO_AUTH_USER.getMessage());
						return jsonMapper.toJson(resultBean);
					}
				}
			}
			if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(areaId) && StringUtils.isNotBlank(parentId)) {
				office = apiSystemService.getOfficeById(id);
				office.setName(name);
				if (!office.getParent().getId().equals(parentId)) {
					Office officePrenat = apiSystemService.getOfficeById(parentId);
					office.setParent(officePrenat);
					office.setParentIds(officePrenat.getParentIds() + officePrenat.getId() + ",");
				}
				office.setArea(new Area(areaId));
			} else {
				logger.error("更新机构信息错误：参数不正确");
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			apiSystemService.saveOffice(office);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("更新机构信息错误�?" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	@RequestMapping(value = "saveOffice", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "手机端保存机构信�?", httpMethod = "POST", notes = "手机端保存机构信�?", consumes = "application/json")
	public String saveOffice(HttpServletRequest request, HttpServletResponse response, @RequestBody OfficeVo officeVo) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			} else {
				if (apiSystemService.existSameCompanyName(officeVo)) {
					return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(
							ResultEnum.COMPANY_NAME_EXIST.getCode(), ResultEnum.COMPANY_NAME_EXIST.getMessage()));
				} else {
					apiSystemService.saveOfficeVo(officeVo);
					return jsonMapper.toJson(ResultUtil.success());
				}
			}
		} catch (Exception e) {
			logger.error("保存机构信息错误�?" + e);
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
	@ApiOperation(value = "机构恢复", httpMethod = "POST", notes = "机构恢复", consumes = "application/x-www-form-urlencoded")
	public String enable(HttpServletRequest request, @RequestParam String id) {
		apiSystemService.officeChangeState(id, Office.DEL_FLAG_NORMAL);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping(value = "delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "机构删除", httpMethod = "POST", notes = "机构删除", consumes = "application/x-www-form-urlencoded")
	public String delete(HttpServletRequest request, @RequestParam String id) {
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(
						ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			} else {
				if (officeService.findChildrenOfficeList(id).size() > 1) {
					return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DELETE_OFFICE_FAILED_SON.getCode(), ResultEnum.DELETE_OFFICE_FAILED_SON.getMessage()));
				}
				apiSystemService.officeChangeState(id, Office.DEL_FLAG_DELETE);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
			}
		} catch (Exception e) {
			logger.error("获取登录用户的角色列表错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	@RequestMapping(value = "getCurrentOffice", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取当前企业", httpMethod = "POST", notes = "获取当前企业", consumes = "application/x-www-form-urlencoded")
	public String getCurrentOffice(HttpServletRequest request) {
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			String officeId = request.getParameter("officeId");
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(
						ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			} else {
				User user = apiUserService.getUserByToken(token);
				if (user != null && user.getCompany() != null && StringUtils.isBlank(officeId)) {
					officeId = user.getCompany().getId();
				}
				List<Map> list = apiSystemService.getOfficeByOfficeId(officeId);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
			}
		} catch (Exception e) {
			logger.error("获取登录用户的角色列表错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	@RequestMapping(value = "getBusinessLicense", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取公司营业执照资料", httpMethod = "POST", notes = "获取公司营业执照资料", consumes = "application/x-www-form-urlencoded")
	public String getBusinessLicense(HttpServletRequest request) {
		String token = request.getHeader("X-Token");
		String officeId = request.getParameter("officeId");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			if (user != null && user.getCompany() != null && StringUtils.isBlank(officeId)) {
				officeId = user.getCompany().getId();
			}
			BusinessLicense businessLicense = new BusinessLicense();
			List<BusinessLicense> list = businessLicenseService.findBusinessLicense(officeId);
			if (list != null && list.size() > 0) {
				businessLicense = list.get(0);
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(businessLicense));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

	@RequestMapping(value = "saveBusinessLicense", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "保存公司营业执照资料", httpMethod = "POST", notes = "保存公司营业执照资料", consumes = "application/json")
	public String saveBusinessLicense(HttpServletRequest request, HttpServletResponse response, @RequestBody BusinessLicense businessLicense) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			String token = request.getHeader("X-Token");
			String officeId = "";
			if (token != null) {
				User user = apiUserService.getUserByToken(token);
				if (user != null && user.getCompany() != null) {
					officeId = user.getCompany().getId();
				}
				businessLicense.setOfficeId(officeId);
				businessLicenseService.save(businessLicense);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(businessLicense));
			} else {
				return JsonMapper.nonDefaultMapper().toJson(
						ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		} catch (Exception e) {
			logger.error("保存公司营业执照资料�?" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	@RequestMapping(value = "getProductionCertificate", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取生产认证资料", httpMethod = "POST", notes = "获取生产认证资料", consumes = "application/x-www-form-urlencoded")
	public String getProductionCertificate(HttpServletRequest request) {
		String token = request.getHeader("X-Token");
		String officeId = request.getParameter("officeId");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			if (user != null && user.getCompany() != null && StringUtils.isBlank(officeId)) {
				officeId = user.getCompany().getId();
			}
			ProductionCertificate productionCertificate = new ProductionCertificate();
			List<ProductionCertificate> list = productionCertificateService.findProductionCertificate(officeId);
			if (list != null && list.size() > 0) {
				productionCertificate = list.get(0);
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(productionCertificate));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

	@RequestMapping(value = "saveProductionCertificate", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "保存生产认证资料", httpMethod = "POST", notes = "保存生产认证资料", consumes = "application/json")
	public String saveProductionCertificate(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductionCertificate productionCertificate) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			String token = request.getHeader("X-Token");
			String officeId = "";
			if (token != null) {
				User user = apiUserService.getUserByToken(token);
				if (user != null && user.getCompany() != null) {
					officeId = user.getCompany().getId();
				}
				productionCertificate.setOfficeId(officeId);
				productionCertificateService.save(productionCertificate);
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(productionCertificate));
			} else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		} catch (Exception e) {
			logger.error("保存生产认证资料�?" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	@RequestMapping(value = "saveThreeInOne", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "保存企业资料", httpMethod = "POST", notes = "保存企业资料", consumes = "application/json")
	public String saveThreeInOne(HttpServletRequest request, HttpServletResponse response, @RequestBody Enterprise enterprise) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		OfficeVo officeVo = enterprise.getOfficeVo();
		BusinessLicense businessLicense = enterprise.getBusinessLicense();
		ProductionCertificate productionCertificate = enterprise.getProductionCertificate();
		try {
			String token = request.getHeader("X-Token");
			if (token != null) {
				User user = apiUserService.getUserByToken(token);
				// 不能选择所属企业，为该公司的下�?
				if (StringUtils.isNotBlank(officeVo.getId()) && apiSystemService.existLoop(officeVo)) {
					return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.UPDATE_OFFICE_PARENT_ID.getCode(),ResultEnum.UPDATE_OFFICE_PARENT_ID.getMessage()));
				}
				if (apiSystemService.existSameCompanyName(officeVo)) {
					return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(
							ResultEnum.COMPANY_NAME_EXIST.getCode(), ResultEnum.COMPANY_NAME_EXIST.getMessage()));
				} else {
					// 新增时，计算code�?
					if (StringUtils.isBlank(officeVo.getId())) {
						int code = officeService.getMaxOfficeCode() + 1;
						officeVo.setCode(String.valueOf(code));
					}
					productionCertificateService.saveEnterprise(officeVo, productionCertificate, businessLicense);
					return jsonMapper.toJson(ResultUtil.success());
				}
			} else {
				return JsonMapper.nonDefaultMapper().toJson(
						ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		} catch (Exception e) {
			logger.error("保存企业资料�?" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	/**
	 * 获取企业(下拉列表)分组 add by luoxw 2019-03-28 测试地址�?
	 * http://localhost:8080/sureserve-admin/api/office/getCompanyData
	 * 
	 * @return
	 */
	@RequestMapping(value = "getCompanyGroup", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取企业", httpMethod = "GET", notes = "获取企业", consumes = "application/x-www-form-urlencoded")
	public String getCompanyGroup(HttpServletRequest request) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		User user = apiUserService.getUserByToken(token);

		// 超级管理默认查询所有企业，其他默认查询本企�?
		if (user.isAdmin()) {
			List<Office> officeList = officeService.findAll();
			for (int i = 0; i < officeList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Office office = officeList.get(i);
				String name = office.getName();
				String szm = ChineseCharToEnUtil.getFirstLetter(name);
				if (szm.length() > 0) {
					szm = szm.substring(0, 1).toUpperCase();
					Map<String, Object> officeMap = new HashMap<String, Object>();
					if ("1".equals(office.getId())) {
						continue;
					}
					officeMap.put("id", office.getId());
					officeMap.put("name", office.getName());
					map.put(szm, officeMap);
					listMap.add(map);
				}
			}

			Map<String, Object> rmap = new HashMap<String, Object>();
			List<Map<String, Object>> rlist = new ArrayList<Map<String, Object>>();
			Map<String, Object> office = new HashMap<String, Object>();
			// office.put("id","");
			// office.put("name","所有公�?");
			// rlist.add(office);
			// rmap.put("A", rlist);

			for (Map<String, Object> map : listMap) {
				Iterator it = map.entrySet().iterator();
				Entry entry = (Entry) it.next();
				String key = (String) entry.getKey();
				Map<String, Object> value = (Map<String, Object>) entry.getValue();
				if (rmap.containsKey(key)) {
					List<Map<String, Object>> slist = (List<Map<String, Object>>) rmap.get(key);
					slist.add(value);
					rmap.put(key, slist);
				} else {
					rlist = new ArrayList<Map<String, Object>>();
					rlist.add(value);
					rmap.put(key, rlist);
				}
			}
			rmap = sortMapByKey(rmap);
			for (String key : rmap.keySet()) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("label", key);
				map.put("options", rmap.get(key));
				mapList.add(map);
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(mapList));
		} else {
			List<Office> officeList = officeService.findChildrenOfficeList(user.getCompany().getId());
			for (int i = 0; i < officeList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Office office = officeList.get(i);
				String name = office.getName();
				String szm = ChineseCharToEnUtil.getFirstLetter(name);
				if (szm.length() > 0) {
					szm = szm.substring(0, 1).toUpperCase();
					Map<String, Object> officeMap = new HashMap<String, Object>();
					if ("1".equals(office.getId())) {
						continue;
					}
					officeMap.put("id", office.getId());
					officeMap.put("name", office.getName());
					map.put(szm, officeMap);
					listMap.add(map);
				}
			}

			Map<String, Object> rmap = new HashMap<String, Object>();
			List<Map<String, Object>> rlist = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : listMap) {
				Iterator it = map.entrySet().iterator();
				Entry entry = (Entry) it.next();
				String key = (String) entry.getKey();
				Map<String, Object> value = (Map<String, Object>) entry.getValue();
				if (rmap.containsKey(key)) {
					List<Map<String, Object>> slist = (List<Map<String, Object>>) rmap.get(key);
					slist.add(value);
					rmap.put(key, slist);
				} else {
					rlist = new ArrayList<Map<String, Object>>();
					rlist.add(value);
					rmap.put(key, rlist);
				}
			}
			rmap = sortMapByKey(rmap);
			for (String key : rmap.keySet()) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("label", key);
				map.put("options", rmap.get(key));
				mapList.add(map);
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(mapList));
		}
	}

	/**
	 * 使用 Map按key进行排序
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> sortMapByKey(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());
		sortMap.putAll(map);
		return sortMap;
	}

	/**
	 * 专家新增服务获取公司下拉框列表数�?
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getOfficeListsTwo", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "专家新增服务获取公司下拉框列表数�?", httpMethod = "GET", notes = "专家新增服务获取公司下拉框列表数�?", consumes = "application/json")
	public String getOfficeListsTwo(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String token = request.getHeader("X-Token");
		if (token != null) {
			User user = apiUserService.getUserByToken(token);
			Experts experts = expertsService.getExpertByUserId(user.getId());
			List<Dict> list = apiDictService.findDictlist(experts.getPlatform(), "platform");
			String id = "%," + list.get(0).getDescription() + ",%";
			try {
				List<Office> officeList = Lists.newArrayList();
				officeList = officeService.getListOffice(id);
				List<Map<String, Object>> mapList = Lists.newArrayList();
				for (int i = 0; i < officeList.size(); i++) {
					Office e = officeList.get(i);
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", e.getId());
					map.put("name", e.getName());
					mapList.add(map);
				}
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(mapList));
			} catch (Exception e) {
				logger.error("获取登录用户的角色列表错误：" + e);
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	@RequestMapping(value = "getCurrentCompanyAndSubordinateCompany", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取当前用户的当前公司以及下级公司", httpMethod = "GET", notes = "获取当前用户的当前公司以及下级公司?", consumes = "application/json")
	public String getCurrentCompanyAndSubordinateCompany(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		List<Map<String, Object>> mapList = Lists.newArrayList();
		User user = apiUserService.getUserByToken(token);
		List<Office> list = apiSystemService.getCurrentCompanyAndSubordinateCompany(user);
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
			map.put("name", e.getName());
			mapList.add(map);
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(mapList));
	}

}
