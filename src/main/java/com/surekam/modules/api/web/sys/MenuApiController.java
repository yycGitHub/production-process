package com.surekam.modules.api.web.sys;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

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

import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.web.BaseController;
import com.surekam.modules.sys.entity.Menu;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.MenuVo;
import com.surekam.modules.sys.entity.vo.UserVo;
import com.surekam.modules.sys.service.ApiSystemService;

@Api
@Controller
@RequestMapping(value = "api/menu/")
public class MenuApiController extends BaseController {
	
	@Autowired
	private ApiSystemService apiSystemService;
	
	@RequestMapping(value = "getElmentTreeMenus",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "获取菜单树数据", httpMethod = "POST", 
		notes = "获取菜单树数据",	consumes="application/json")
	public String getElmentTreeMenus(HttpServletRequest request,HttpServletResponse response,
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
			Menu menu = apiSystemService.getMenu(itemId);
			if(menu == null){
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			List<Menu> list = apiSystemService.findAllMenu(currentUser,itemId,onlyNext);			
			MenuVo vo = treeList(list,menu);
			List<MenuVo> vos = new ArrayList<MenuVo>();
			vos.add(vo);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(vos));
		} catch (Exception e) {
			logger.error("获取菜单树数据错误："+e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	public MenuVo treeList(List<Menu> list, Menu parent) {
		MenuVo menuVo = new MenuVo();
		BeanUtils.copyProperties(parent,menuVo,new String[]{"childList"});
		for (Menu menuTemp : list) {
			if(parent.getId().equals(menuTemp.getParent().getId())) {
				MenuVo menuVoTemp = new MenuVo();
				BeanUtils.copyProperties(menuTemp,menuVoTemp,new String[]{"childList"});
				menuVoTemp.setParentId(menuTemp.getParent().getId());
				menuVoTemp.setParentName(menuTemp.getParent().getName());
				if(menuTemp.getChildList().size()>0){
					menuVoTemp = treeList(list, menuTemp);
				}
				menuVo.getChildList().add(menuVoTemp);
			}
		}
		return menuVo;
	}
	
	@RequestMapping(value = "list",produces="application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "菜单列表查询", 
		notes = "菜单列表查询",	consumes="application/json")
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
			Page<Menu> page = new Page<Menu>(pageNo,pageSize);
			page = apiSystemService.findPageMenus(page,currentUser,name ,parentId,delFlag,false);
			Page<MenuVo> pageVo = new Page<MenuVo>(pageNo,pageSize);
			pageVo.setCount(page.getCount());
			if(page.getList()!=null && page.getList().size()>0){
				List<Menu> list = page.getList(); 
				List<MenuVo> listVo = new ArrayList<MenuVo>();
				for(int i=0;i<list.size();i++){
					MenuVo menuVo = new MenuVo();
					Menu menuTemp = list.get(i);
					BeanUtils.copyProperties(menuTemp, menuVo, new String[]{"childList"});
					menuVo.setParentId(menuTemp.getParent().getId());
					menuVo.setParentName(menuTemp.getParent().getName());
					listVo.add(menuVo);
				}
				pageVo.setList(listVo);
			}
			return jsonMapper.toJson(ResultUtil.success(pageVo));
		} catch (Exception e) {
			logger.error("菜单列表查询错误："+e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@RequestMapping(value = "getInfo",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "获取菜单详细信息", httpMethod = "POST", 
		notes = "获取菜单详细信息",	consumes="application/json")
	public String getInfo(HttpServletRequest request,HttpServletResponse response,
			@RequestParam String officeId) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			MenuVo menuVo = new MenuVo();
			Menu menu = apiSystemService.getMenu(officeId);
			if(menu == null){
				resultBean =ResultUtil.error(ResultEnum.SYSTEM_NO_AUTH_USER.getCode(), ResultEnum.SYSTEM_NO_AUTH_USER.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			// 判断显示的用户是否在授权范围内
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			if (!currentUser.isAdmin()) {
				List<Menu> list = apiSystemService.findAllMenu(currentUser,"1",false);
				if (list!=null && list.size()>0) {
					boolean flag = true;
					for(Menu menuTemp:list){
						if(menuTemp.getId().equals(officeId)){
							flag = false;
						}
					}
					if(flag){
						resultBean =ResultUtil.error(ResultEnum.SYSTEM_NO_AUTH_USER.getCode(), ResultEnum.SYSTEM_NO_AUTH_USER.getMessage());
						return jsonMapper.toJson(resultBean);
					}
				}else{
					resultBean =ResultUtil.error(ResultEnum.SYSTEM_NO_AUTH_USER.getCode(), ResultEnum.SYSTEM_NO_AUTH_USER.getMessage());
					return jsonMapper.toJson(resultBean);
				}
			}
			BeanUtils.copyProperties(menu,menuVo,new String[]{"childList"});
			menuVo.setParentId(menu.getParent().getId());
			menuVo.setParentName(menu.getParent().getName());
			return jsonMapper.toJson(ResultUtil.success(menuVo));
		} catch (Exception e) {
			logger.error("获取机构详细信息错误："+e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@RequestMapping(value = "save",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "保存菜单信息", httpMethod = "POST", 
		notes = "保存菜单信息",	consumes="application/json")
	public String save(HttpServletRequest request,HttpServletResponse response,@RequestBody MenuVo menuVo) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
		try {			
			Menu menu = new Menu();
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			BeanUtils.copyProperties(menuVo,menu,new String[]{"id","loginIp","loginDate","createDate","updateDate","delFlag"});
			Menu menuPrenat = apiSystemService.getMenu(menuVo.getParentId());
			menu.setParent(menuPrenat);
			menu.setParentIds(menuPrenat.getParentIds()+menuPrenat.getId()+",");
			menu.setCreateBy(currentUser);
			apiSystemService.saveMenu(menu);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("保存菜单信息错误："+e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	@RequestMapping(value = "update",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "更新菜单信息", httpMethod = "POST", 
		notes = "更新菜单信息",	consumes="application/x-www-form-urlencoded")
	public String update(HttpServletRequest request,HttpServletResponse response,
			@RequestParam String id,
			@RequestParam String parentId,
			@RequestParam(required = false) String href,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) Integer sort,
			@RequestParam(required = false) String permission,
			@RequestParam(required = false) String isShow
			) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<MenuVo> resultBean = new ResultBean<MenuVo>();
		try {
			Menu menu = new Menu();
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			if (!currentUser.isAdmin()) {
				String dataScope = apiSystemService.getDataScope(currentUser);
				if (dataScope.indexOf("office.id=") != -1) {
					String AuthorizedOfficeId = dataScope.substring(dataScope.indexOf("office.id=") + 10, dataScope.indexOf(" or"));
					if (!AuthorizedOfficeId.equalsIgnoreCase(id)) {
						resultBean =ResultUtil.error(ResultEnum.SYSTEM_NO_AUTH_USER.getCode(), ResultEnum.SYSTEM_NO_AUTH_USER.getMessage());
						return jsonMapper.toJson(resultBean);
					}
				}
			}
			if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(parentId)){
				menu = apiSystemService.getMenu(id);
				menu.setName(name);
				if(!menu.getParent().getId().equals(parentId)){
					Menu menuPrenat = apiSystemService.getMenu(parentId);
					menu.setParent(menuPrenat);
					menu.setParentIds(menuPrenat.getParentIds()+menuPrenat.getId()+",");
				}
				menu.setHref(href);
				menu.setSort(sort);
				menu.setPermission(permission);
				menu.setIsShow(isShow);
			}else{
				logger.error("更新菜单信息错误：参数不正确");
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			apiSystemService.saveMenu(menu);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("更新机构信息错误："+e);
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
	@ApiOperation(value = "菜单恢复", httpMethod = "POST", 
	notes = "菜单恢复",	consumes="application/x-www-form-urlencoded")
	public String enable(HttpServletRequest request,@RequestParam String id){
		apiSystemService.menuChangeState(id,Office.DEL_FLAG_NORMAL);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
	}
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(value = "delete",produces="application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "菜单删除", httpMethod = "POST", 
	notes = "菜单删除",	consumes="application/x-www-form-urlencoded")
	public String delete(HttpServletRequest request,@RequestParam String id){
		apiSystemService.menuChangeState(id,Office.DEL_FLAG_DELETE);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
	}
}
