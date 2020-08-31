package com.surekam.modules.agro.sendingrecords.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.web.BaseController;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.sendingrecords.entity.SendingRecords;
import com.surekam.modules.agro.sendingrecords.service.SendingRecordsService;

/**
 * 发送记录表(短信邮件等)Controller
 * @author luoxw
 * @version 2019-07-02
 */
@Controller
@RequestMapping(value = "${adminPath}/sendingrecords/sendingRecords")
public class SendingRecordsController extends BaseController {

	@Autowired
	private SendingRecordsService sendingRecordsService;
	
}
