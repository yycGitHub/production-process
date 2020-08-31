package com.surekam.modules.sys.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.OfficeService;
import com.surekam.modules.sys.utils.DictUtils;
import com.surekam.modules.sys.utils.UserUtils;
import com.surekam.modules.sys.vo.OfficeVo;

/**
 * 机构Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute("office")
	public Office get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return officeService.get(id);
		} else {
			return new Office();
		}
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping({"list", ""})
	public String list(Office office, HttpServletRequest request, HttpServletResponse response, Model model) {
		String message = request.getParameter("message");
		User user = UserUtils.getUser();
		model.addAttribute("office", office);
        Page<Office> page = officeService.findOffice(new Page<Office>(request, response), office,user); 
        model.addAttribute("page", page);
        model.addAttribute("message", message);
		return "modules/sys/common/officeList";
	}
	
	@RequiresPermissions("sys:office:view")
	@RequestMapping({"listOffice"})
	public String listOffice(Office office, HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("office", office);
		return "modules/sys/common/officeList";
	}
	
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "getOfficeList", method = {RequestMethod.POST})
	@ResponseBody
	public Page<OfficeVo> getOfficeList(HttpServletRequest request, HttpServletResponse response) {
		Office office = new Office();
		String delFlag = request.getParameter("delFlag");
		if(StringUtils.isNotBlank(delFlag)) {
			office.setDelFlag(delFlag);
		}
		String id = request.getParameter("id");
		if(StringUtils.isNotBlank(id)) {
			office.setId(id);;
		}
		User user = UserUtils.getUser();
        Page<Office> page = officeService.findOffice(new Page<Office>(request, response), office,user); 
        Page<OfficeVo> pageOfficeVo = new Page<OfficeVo>();
        List<OfficeVo> officeList = new ArrayList<OfficeVo>();
        for (Iterator<Office> iterator = page.getList().iterator(); iterator.hasNext();) {
			Office officeEntity = (Office) iterator.next();
			OfficeVo officeVo = new OfficeVo();
			BeanUtils.copyProperties(officeEntity, officeVo);//复制属性
			
			officeVo.setAreaName(officeEntity.getArea().getName());
			officeVo.setTypeName(DictUtils.getDictLabel(officeEntity.getType(), "sys_office_type", ""));
		
			officeList.add(officeVo);
		}
        pageOfficeVo.setPageSize(page.getPageSize());
        pageOfficeVo.setPageNo(page.getPageNo());
        pageOfficeVo.setCount(page.getCount());
        pageOfficeVo.setList(officeList);
	    return pageOfficeVo;
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping("form")
	public String form(Office office, Model model,String rebackId) {
		if (office.getParent() == null || office.getParent().getId() == null) {
			office.setParent(officeService.get("0"));
		}else{
			//放入else分支  否则编辑顶点机构 空指针   update by ligm 2018-07-23
			office.setParent(officeService.get(office.getParent().getId()));
		}
		if (office.getArea() == null) {
			office.setArea(office.getParent().getArea());
		}
		model.addAttribute("office", office);
		model.addAttribute("rebackId", rebackId);
		return "modules/sys/common/officeForm";
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping("save")
	public String save(Office office, Model model, RedirectAttributes redirectAttributes,String rebackId) {
		Map<String,String> result = new HashMap<String,String>();
		result.put("rebackId", rebackId);
		if (Global.isDemoMode()) {
			//addMessage(redirectAttributes, "演示模式，不允许操作！");
			//return "redirect:" + Global.getAdminPath() + "/sys/office?id="+rebackId;
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DEMO_MODE_FAILED.getCode(), ResultEnum.DEMO_MODE_FAILED.getMessage(),result));
		}
		if (!beanValidator(model, office)) {
			return form(office, model,rebackId);
		}
		officeService.save(office);
		//addMessage(redirectAttributes, "保存机构'" + office.getName() + "'成功");
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(result));
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping("delete")
	public String delete(String id, RedirectAttributes redirectAttributes,String rebackId) {
		Map<String,String> result = new HashMap<String,String>();
		if (Global.isDemoMode()) {
			//addMessage(redirectAttributes, "演示模式，不允许操作！");
			//return "redirect:" + Global.getAdminPath() + "/sys/office?id="+rebackId;
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DEMO_MODE_FAILED.getCode(), ResultEnum.DEMO_MODE_FAILED.getMessage(),result));
		}
		if (Office.isRoot(id)) {
			//addMessage(redirectAttributes, "删除机构失败, 不允许删除顶级机构或编号空");
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DELETE_OFFICE_FAILED.getCode(), ResultEnum.DELETE_OFFICE_FAILED.getMessage(),result));
		} else {
			officeService.delete(id);
			//addMessage(redirectAttributes, "删除机构成功");
			result.put("rebackId", rebackId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(result));
		}
		
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping("deleteMul")
	public String deleteMul(String ids, RedirectAttributes redirectAttributes,String rebackId) {
		Map<String,String> result = new HashMap<String,String>();
		if (Global.isDemoMode()) {
			//addMessage(redirectAttributes, "演示模式，不允许操作！");
			//return "redirect:" + Global.getAdminPath() + "/sys/office?id="+rebackId;
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DEMO_MODE_FAILED.getCode(), ResultEnum.DEMO_MODE_FAILED.getMessage(),result));
		}
		for(String id:ids.split(",")){
			if(Office.isRoot(id)){
				
			}else{
				officeService.delete(id);
			}
		}
		result.put("rebackId", rebackId);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(result));
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping("rebackOffice")
	public String rebackOffice(String id, RedirectAttributes redirectAttributes,String rebackId) {
		Map<String,String> result = new HashMap<String,String>();
		if (Global.isDemoMode()) {
			//addMessage(redirectAttributes, "演示模式，不允许操作！");
			//return "redirect:" + Global.getAdminPath() + "/sys/office?id="+rebackId;
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.DEMO_MODE_FAILED.getCode(), ResultEnum.DEMO_MODE_FAILED.getMessage(),result));
		}
		else {
			officeService.rebackOffice(id);
			//addMessage(redirectAttributes, "删除机构成功");
			result.put("rebackId", rebackId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(result));
		}
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping("treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response,
			@RequestParam(required = false) String extId,
			@RequestParam(required = false) Long type,
			@RequestParam(required = false) Long grade) {
		
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findAll();
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);		
			if ((extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && Integer.parseInt(e.getType()) <= type.intValue()))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))){				
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	@RequiresPermissions("sys:office:view")
	@RequestMapping({"index"})
	public String index(Model model) {
		return "modules/sys/common/officeIndex";
	}
	
	@RequiresPermissions("sys:office:view")
	@RequestMapping({"officeTree"})
	public String officeTree(Model model) {
		model.addAttribute("url", "");
		return "modules/sys/common/officeTree";
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping("ztreeDynamicData")
	public List<Map<String, Object>> ztreeDynamicData(HttpServletResponse response,
			@RequestParam(required = false) String itemId) {		
		response.setContentType("application/json; charset=UTF-8");
		User user = UserUtils.getUser();
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findZtreeOffices(user,itemId);
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);					
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
			map.put("name", e.getName());
			if(e.getChildList().size()>0){
				map.put("isParent",true);
			}
			mapList.add(map);
		}
		return mapList;
	}
}
