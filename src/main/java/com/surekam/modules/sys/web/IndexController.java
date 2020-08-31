package com.surekam.modules.sys.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "${adminPath}/sys/index")
public class IndexController {
	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping(value = "")
	public String index(){
		return "modules/sys/user/profile";
	}

}
