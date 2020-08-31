package com.surekam.modules.api.web.sys;

import java.util.ArrayList;
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
import com.surekam.modules.api.dto.resp.DictResp;
import com.surekam.modules.sys.entity.Dict;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.DictVo;
import com.surekam.modules.sys.entity.vo.UserVo;
import com.surekam.modules.sys.service.ApiDictService;
import com.surekam.modules.sys.service.ApiSystemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 字典APIController
 * 
 * @author 腾农科技 2018-10-25
 */
@Api
@Controller
@RequestMapping(value = "api/dict/")
public class DictApiController extends BaseController {

	@Autowired
	private ApiDictService apiDictService;
	@Autowired
	private ApiSystemService apiSystemService;

	/**
	 * 根据id查询单个字典对象信息
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getDict", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取字典", httpMethod = "POST", notes = "获取字典", consumes = "application/json")
	public Dict get(@RequestParam(required = false) String id, HttpServletRequest request,
			HttpServletResponse response) {
		if (StringUtils.isNotBlank(id)) {
			return apiDictService.get(id);
		} else {
			return new Dict();
		}
	}

	/**
	 * 根据字典 标签名:label,是否删除:delFlag,分页查询字典集合
	 * 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @param label
	 * @param delFlag
	 * @return
	 */
	@RequestMapping(value = "list", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取字典对象列表", httpMethod = "POST", notes = "获取字典对象列表", consumes = "application/json")
	public String list(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) Integer pageno, @RequestParam(required = false) Integer pagesize,
			@RequestParam(required = false) String label, @RequestParam(required = false) String type,
			@RequestParam(required = false) String delFlag) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();

		try {
			int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
			int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			Page<Dict> page = new Page<Dict>(pageNo, pageSize);
			page = apiDictService.findPageDictlist(page, currentUser, label, type, delFlag);
			Page<DictVo> pageVo = new Page<DictVo>(pageNo, pageSize);
			pageVo.setCount(page.getCount());
			if (page.getList() != null && page.getList().size() > 0) {
				List<Dict> list = page.getList();
				List<DictVo> listVo = new ArrayList<DictVo>();

				for (int i = 0; i < list.size(); i++) {
					DictVo dictVo = new DictVo();
					Dict dictTemp = list.get(i);
					BeanUtils.copyProperties(dictTemp, dictVo);
					listVo.add(dictVo);
				}
				pageVo.setList(listVo);
			}
			return jsonMapper.toJson(ResultUtil.success(pageVo));
		} catch (Exception e) {
			logger.error("字典列表查询错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}

	}

	/**
	 * 保存字典对象信息
	 * 
	 * @param request
	 * @param response
	 * @param dictvo
	 * @return
	 */
	@RequestMapping(value = "save", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "保存字典", httpMethod = "POST", notes = "保存字典", consumes = "application/json")
	public String save(HttpServletRequest request, HttpServletResponse response, @RequestBody DictVo dictvo) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
		try {
			Dict dict = new Dict();
			BeanUtils.copyProperties(dictvo, dict, new String[] { "id", "createDate", "updateDate", "delFlag" });
			dict.setLabel(dictvo.getLabel());
			dict.setValue(dictvo.getValue());
			dict.setType(dictvo.getType());
			dict.setDescription(dictvo.getDescription());
			dict.setSort(dictvo.getSort());
			apiDictService.saveDict(dict);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("保存字典信息错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}

	}

	/**
	 * 更新字典信息
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @param label
	 * @param value
	 * @param type
	 * @param description
	 * @param sort
	 * @param delFlag
	 * @return
	 * 
	 */
	@RequestMapping(value = "update", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "更新字典", httpMethod = "POST", notes = "更新字典", consumes = "application/json")
	public String update(HttpServletRequest request, HttpServletResponse response, @RequestParam String id,
			@RequestParam String label, @RequestParam String value, @RequestParam String type,
			@RequestParam String description, @RequestParam Integer sort, @RequestParam String delFlag) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<DictVo> resultBean = new ResultBean<DictVo>();
		try {
			Dict dict = new Dict();
			if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(label) && StringUtils.isNotBlank(value)
					&& StringUtils.isNotBlank(type) && StringUtils.isNotBlank(description)
					&& StringUtils.isNotBlank(sort.toString()) && StringUtils.isNotBlank(delFlag)) {
				dict = apiDictService.get(id);
				dict.setLabel(label);
				dict.setValue(value);
				dict.setType(type);
				dict.setDescription(description);
				dict.setSort(sort);
				dict.setDelFlag(delFlag);
			} else {
				logger.error("更新字典信息错误：参数不正确");
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			apiDictService.saveDict(dict);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("更新字典信息错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}

	}

	/**
	 * 恢复字典信息,传入参数id
	 * 
	 * @return
	 */
	@RequestMapping(value = "enable", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "字典恢复", httpMethod = "POST", notes = "字典恢复", consumes = "application/x-www-form-urlencoded")
	public String enable(HttpServletRequest request, @RequestParam String id) {
		apiDictService.dictChangeState(id, User.DEL_FLAG_NORMAL);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
	}

	/**
	 * 删除字典信息,传入参数id
	 * 
	 * @return
	 */
	@RequestMapping(value = "delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "删除字典", httpMethod = "POST", notes = "删除字典", consumes = "application/x-www-form-urlencoded")
	public String delete(HttpServletRequest request, @RequestParam String id) {
		apiDictService.dictChangeState(id, User.DEL_FLAG_DELETE);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
	}

	@RequestMapping(value = "getDictList", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取字典", httpMethod = "POST", notes = "获取字典", consumes = "application/json")
	public String getDictList(HttpServletRequest request, HttpServletResponse response, @RequestParam String id) {
		try {
			List<DictResp> dictList = apiDictService.getDictList(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(dictList));
		} catch (Exception e) {
			logger.info("获取树状下级异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	
	@RequestMapping(value = "getSensor", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取传感器", httpMethod = "POST", notes = "获取传感器", consumes = "application/json")
	public String getSensor(HttpServletRequest request, HttpServletResponse response, @RequestParam String id) {
		try {
			List<DictResp> dictList = apiDictService.getSensor(id);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(dictList));
		} catch (Exception e) {
			logger.info("获取树状下级异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "getDictTypes", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取字典类型列表", httpMethod = "POST", notes = "获取字典类型列表", consumes = "application/json")
	public String getDictTypes() {
		try {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(apiDictService.findTypeList()));
		} catch (Exception e) {
			logger.info("获取列表异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "getDictSort", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取字典类型的排序", httpMethod = "POST", notes = "获取字典类型的排序", consumes = "application/json")
	public String getDictSort(@RequestParam String type) {
		try {
			String sort = apiDictService.getDictMaxSort(type);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(sort));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
}
