package com.surekam.modules.api.web.sys;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
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
import com.google.common.collect.Maps;
import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.web.BaseController;
import com.surekam.modules.sys.entity.Area;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.AreaVo;
import com.surekam.modules.sys.entity.vo.UserVo;
import com.surekam.modules.sys.service.ApiSystemService;

@Api
@Controller
@RequestMapping(value = "api/area/")
public class AreaApiController extends BaseController {
	
	@Autowired
	private ApiSystemService apiSystemService;
	
	@RequestMapping(value = "getAreas",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "获取区域列表", httpMethod = "POST", 
		notes = "获取区域列表",	consumes="application/json")
	public String getAreas(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required = false) Long type,
			@RequestParam(required = false) String itemId) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			String token = request.getHeader("X-Token");
			List<Map<String, Object>> mapList = Lists.newArrayList();
			User currentUser = apiSystemService.findUserByToken(token);
			List<Office> list = apiSystemService.findOffices(currentUser,itemId,true);
			for (int i=0; i<list.size(); i++){
				Office e = list.get(i);		
				if ((type == null || (type != null && Integer.parseInt(e.getType()) <= type.intValue()))){				
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", e.getId());
					map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
					map.put("name", e.getName());
					mapList.add(map);
				}
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(mapList));
		} catch (Exception e) {
			logger.error("获取登录用户的角色列表错误："+e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@RequestMapping(value = "getElmentTreeAreas",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "获取区域树数据", httpMethod = "POST", 
		notes = "获取区域树数据",	consumes="application/json")
	public String getElmentTreeAreas(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required = false) String itemId,
			@RequestParam(required = false) boolean onlyNext) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			if(StringUtils.isBlank(itemId)){
				itemId = "1";
			}
			Area area = apiSystemService.getArea(itemId);
			if(area == null){
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			List<Area> list = apiSystemService.findAreas(currentUser,itemId,onlyNext);			
			AreaVo vo = treeList(list,area);
			List<AreaVo> vos = new ArrayList<AreaVo>();
			vos.add(vo);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(vos));
		} catch (Exception e) {
			logger.error("获取机构树数据错误："+e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	public AreaVo treeList(List<Area> list, Area parent) {
		AreaVo areaVo = new AreaVo();
		BeanUtils.copyProperties(parent,areaVo,new String[]{"childList"});
		for (Area areaTemp : list) {
			if(parent.getId().equals(areaTemp.getParent().getId())) {
				AreaVo areaVoTemp = new AreaVo();
				BeanUtils.copyProperties(areaTemp,areaVoTemp,new String[]{"childList"});
				if(areaTemp.getChildList().size()>0){
					areaVoTemp = treeList(list, areaTemp);
				}
				areaVo.getChildList().add(areaVoTemp);
			}
		}
		return areaVo;
	}
	
	@RequestMapping(value = "list",produces="application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "区域列表查询", 
		notes = "区域列表查询",	consumes="application/json")
	public String list(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String parentId,
			@RequestParam(required = false) String delFlag,
			@RequestParam(required = false) Integer pageno,
			@RequestParam(required = false) Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			int pageNo = pageno==null?Global.DEFAULT_PAGENO:pageno;
			int pageSize = pagesize==null?Global.DEFAULT_PAGESIZE:pagesize;
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);		
			Page<Area> page = new Page<Area>(pageNo,pageSize);
			page = apiSystemService.findPageAreas(page,currentUser,name ,parentId,delFlag, false);
			Page<AreaVo> pageVo = new Page<AreaVo>(pageNo,pageSize);
			pageVo.setCount(page.getCount());
			if(page.getList()!=null && page.getList().size()>0){
				List<Area> list = page.getList(); 
				List<AreaVo> listVo = new ArrayList<AreaVo>();
				for(int i=0;i<list.size();i++){
					AreaVo areaVo = new AreaVo();
					Area areaTemp = list.get(i);
					BeanUtils.copyProperties(areaTemp, areaVo, new String[]{"childList"});
					areaVo.setParentId(areaTemp.getParent().getId());
					areaVo.setParentName(areaTemp.getParent().getName());
					listVo.add(areaVo);
				}
				pageVo.setList(listVo);
			}
			return jsonMapper.toJson(ResultUtil.success(pageVo));
		} catch (Exception e) {
			logger.error("区域列表查询错误："+e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@RequestMapping(value = "getAreaInfo",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "获取区域详细信息", httpMethod = "POST", 
		notes = "获取区域详细信息",	consumes="application/json")
	public String getAreaInfo(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required = false) String officeId) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {		
			Area area= apiSystemService.getArea(officeId);
			AreaVo areaVo = new AreaVo();
			BeanUtils.copyProperties(area,areaVo,new String[]{"childList"});
			areaVo.setParentId(area.getParent().getId());
			return jsonMapper.toJson(ResultUtil.success(areaVo));
		} catch (Exception e) {
			logger.error("获取区域详细信息错误："+e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@RequestMapping(value = "save",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "保存区域信息", httpMethod = "POST", 
		notes = "保存区域信息",	consumes="application/json")
	public String save(HttpServletRequest request,HttpServletResponse response,@RequestBody AreaVo areaVo) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
		try {			
			Area area = new Area();
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			BeanUtils.copyProperties(areaVo,area,new String[]{"id","loginIp","loginDate","createDate","updateDate","delFlag"});
			Area areaPrenat = apiSystemService.getArea(areaVo.getParentId());
			area.setParent(areaPrenat);
			area.setParentIds(areaPrenat.getParentIds()+areaPrenat.getId()+",");
			area.setCreateBy(currentUser);
			apiSystemService.saveArea(area);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("保存机构信息错误："+e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@RequestMapping(value = "update",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "更新区域信息", httpMethod = "POST", 
		notes = "更新区域信息",	consumes="application/x-www-form-urlencoded")
	public String update(HttpServletRequest request,HttpServletResponse response,
			@RequestParam String id,
			@RequestParam String parentId,
			@RequestParam(required = false) String name) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
		try {
			Area area = new Area();
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(parentId)){
				area = apiSystemService.getArea(id);
				area.setUpdateBy(currentUser);
				area.setName(name);
				if(!area.getParent().getId().equals(parentId)){
					Area areaPrenat = apiSystemService.getArea(parentId);
					area.setParent(areaPrenat);
					area.setParentIds(areaPrenat.getParentIds()+areaPrenat.getId()+",");
				}
			}else{
				logger.error("更新区域信息错误：参数不正确");
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			apiSystemService.saveArea(area);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("更新区域信息错误："+e);
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
	@ApiOperation(value = "区域恢复", httpMethod = "POST", 
	notes = "区域恢复",	consumes="application/x-www-form-urlencoded")
	public String enable(HttpServletRequest request,@RequestParam String id){
		apiSystemService.areaChangeState(id,User.DEL_FLAG_NORMAL);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
	}
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(value = "delete",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "区域删除", httpMethod = "POST", 
	notes = "区域删除",	consumes="application/x-www-form-urlencoded")
	public String delete(HttpServletRequest request,@RequestParam String id){
		apiSystemService.areaChangeState(id,User.DEL_FLAG_DELETE);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
	}
}
