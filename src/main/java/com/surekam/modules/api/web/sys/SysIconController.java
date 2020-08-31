package com.surekam.modules.api.web.sys;

import java.util.List;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.web.BaseController;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.sys.entity.SysIcon;
import com.surekam.modules.sys.service.SysIconService;

/**
 * 系统图标表Controller
 * @author luoxw
 * @version 2019-05-16
 */
@Controller
@RequestMapping(value = "api/sysIcon")
public class SysIconController extends BaseController {

	@Autowired
	private SysIconService sysIconService;
	
	@ModelAttribute
	public SysIcon get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return sysIconService.get(id);
		}else{
			return new SysIcon();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getSysIconList")
	@ApiOperation(value = "获取图标信息列表", httpMethod = "POST", notes = "获取图标信息列表", consumes = "application/x-www-form-urlencoded")
	public String list() {
		try {
			List<SysIcon> sysIconList = sysIconService.find();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(sysIconList));
		} catch (Exception e) {
			logger.info("获取异常{}" + e.toString());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}


}
