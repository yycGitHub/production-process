/**
 * 
 */
package com.surekam.modules.api.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.api.service.StatisticsAgroApiService;
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Title: StatisticsAgroApiController Description: 统计农事接口Api
 * 
 * @author tangjun
 * @date 2019年5月11日
 */

@Api
@Controller
@RequestMapping(value = "api/statisticsAgro")
public class StatisticsAgroApiController {

	@Autowired
	private StatisticsAgroApiService statisticsAgroApiService;
	
	@Autowired
	private ApiUserService apiUserService;
	
	@ResponseBody
	@RequestMapping(value = "/statisticsBatch")
	@ApiOperation(value = "统计批次信息", httpMethod = "POST", notes = "统计批次", consumes = "application/x-www-form-urlencoded")
	public String statisticsBatch(HttpServletRequest request, 
			@RequestParam String officeId,
			@RequestParam(required = false) String productId, 
			@RequestParam(required = false) String particularYear, 
			@RequestParam String operType, 
			@RequestParam Integer pageno, 
			@RequestParam Integer pagesize) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			Map<String, Object> statisticsBatch = statisticsAgroApiService.statisticsBatch(user, officeId, productId, particularYear, operType, pageno, pagesize);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(statisticsBatch));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/statisticsTask")
	@ApiOperation(value = "统计任务接口", httpMethod = "POST", notes = "统计批次", consumes = "application/x-www-form-urlencoded")
	public String statisticsTask(HttpServletRequest request, 
			@RequestParam String officeId,
			@RequestParam String particularYear, 
			@RequestParam Integer pageno, 
			@RequestParam Integer pagesize) {
		try {
			String token = request.getHeader("X-Token");
			if (StringUtils.isBlank(token)) {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
			
			User user = apiUserService.getUserByToken(token);
			Page<Map<String, String>> statisticsBatch = statisticsAgroApiService.statisticsTask(user, officeId, particularYear, pageno, pagesize);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(statisticsBatch));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
	
}
