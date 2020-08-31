package com.surekam.modules.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.sensorthreshold.service.SensorThresholdService;

import io.swagger.annotations.Api;

/**
 * 传感器阈值表Controller
 * 
 * @author tangjun
 * @version 2019-05-10
 */
@Api
@Controller
@RequestMapping(value = "api/sensorThreshold")
public class SensorThresholdApiController extends BaseController {

	@Autowired
	private SensorThresholdService sensorThresholdService;

}
