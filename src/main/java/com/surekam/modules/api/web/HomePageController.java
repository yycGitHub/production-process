package com.surekam.modules.api.web;

import io.swagger.annotations.ApiOperation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.DateUtils;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.communication.entity.Communication;
import com.surekam.modules.agro.product.entity.vo.ProductLibraryTreeVo;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskVo;
import com.surekam.modules.agro.report.entity.DailyData;
import com.surekam.modules.agro.report.entity.ReportData;
import com.surekam.modules.agro.report.service.ReportService;
import com.surekam.modules.agro.sensorsetup.entity.SensorData;
import com.surekam.modules.agro.sensorsetup.entity.SensorSetup;
import com.surekam.modules.agro.sensorsetup.service.SensorSetupService;
import com.surekam.modules.agro.video.entity.Video;
import com.surekam.modules.agro.video.service.VideoService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.api.service.SpeciesManageApiService;
import com.surekam.modules.sys.entity.Dict;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.ApiDictService;
import com.surekam.modules.sys.service.ApiSystemService;

/**
 * 首页接口Controller
 * 
 * @author luoxw
 * @version 2019-05-08
 */
@Controller
@RequestMapping(value = "api/home_page")
public class HomePageController extends BaseController {

	@Autowired
	private ApiSystemService apiSystemService;

	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;
	
	@Autowired
	private ApiDictService apiDictService;

	/**
	 * 根据公司获取品种面积占比
	 * 
	 * @return
	 */
	@RequestMapping(value = "getAgroProductArea", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取品种面积", httpMethod = "GET", notes = "获取品种面积", consumes = "application/x-www-form-urlencoded")
	public String getAgroProductArea(HttpServletRequest request,
			@RequestParam(required = false) String officeId) {
		Map<String, Object> reqMap = new HashMap();
		if (StringUtils.isBlank(officeId) || "1".equals(officeId)) {
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			officeId = currentUser.getOffice().getId();
		}
		List<Object> productList = productLibraryTreeService
				.getHomePageAgroProductArea(officeId);
		List<Object> homePageVarietiesList = productLibraryTreeService
				.getHomePageVarietiesList(officeId);
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < productList.size(); i++) {
			Object[] obj = (Object[]) productList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("productId", obj[0].toString());
			map.put("productName", obj[1].toString());
			map.put("area", obj[2].toString());
			list1.add(map);
		}
		//数组排序 --xy
		Collections.sort(list1, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> arg0, Map<String, Object> arg1) {
                  // arg0,arg1 是list中的Map,可以在其内取得值，按其排序，此例为升序，s0和s1是排序字段值
            		double s0 =  Double.parseDouble((String) arg0.get("area")) ;
            		double s1 =  Double.parseDouble((String) arg1.get("area")) ;
                  if (s0 < s1) {
                        return 1;
                  } else {
                        return -1;
                  }
            }
		});
		
		List<Dict> dlist = apiDictService.findDictlist("chart_value_type");
		int order_value = Integer.parseInt(dlist.get(0).getValue());
		List<Map<String, Object>> list0 = new ArrayList<Map<String, Object>>();
		double d1 = 0;
		for(int k = 0; k < list1.size(); k++) {
			if(k < order_value) {
				list0.add(list1.get(k));
			}else {
				d1 += Double.parseDouble((String) list1.get(k).get("area"));
			}
		}
		Map<String, Object> map0 = new HashMap<String, Object>();
		map0.put("productId", "");
		map0.put("productName", "其他");
		map0.put("area", d1);
		list0.add(map0);
		
		reqMap.put("agroProductArea",list0);
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < homePageVarietiesList.size(); i++) {
			Object[] obj = (Object[]) homePageVarietiesList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("productName", obj[0]==null?"":obj[0].toString());
			map.put("productImages", obj[1]==null?"":obj[1].toString());
			map.put("productCode", obj[2]==null?"":obj[2].toString());
			map.put("notFinished", obj[3]==null?"":obj[3].toString());
			map.put("complete", obj[4]==null?"":obj[4].toString());
			map.put("batchStartDate", obj[6]==null?"":obj[6].toString());
			String obj5 = obj[5]==null?"":obj[5].toString();
			if (StringUtils.isNotBlank(obj5)){
				List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
				List<Object> getBatchList = productLibraryTreeService.getBatchList(officeId,obj[5].toString());
				for (int j = 0; j < getBatchList.size(); j++) {
					Object[] objBatchList = (Object[]) getBatchList.get(j);
					Map<String, Object> mapBatchList = new HashMap<String, Object>();
					mapBatchList.put("id", objBatchList[0]==null?"":objBatchList[0].toString());
					mapBatchList.put("batchCode", objBatchList[1]==null?"":objBatchList[1].toString());
					mapBatchList.put("batchStartDate", objBatchList[2]==null?"":objBatchList[2].toString());
					mapBatchList.put("status", objBatchList[3]==null?"":objBatchList[3].toString());
					mapBatchList.put("baseName", objBatchList[4]==null?"":objBatchList[4].toString());
					mapBatchList.put("userName", objBatchList[5]==null?"":objBatchList[5].toString());
					list3.add(mapBatchList);
				}
				map.put("batchList", list3);
			}
			list2.add(map);
		}
		reqMap.put("homePageVarietiesList",list2);
		List<Object> batchCount = productLibraryTreeService
				.getBatchCount(officeId);
		reqMap.put("batchCount",(Object[]) batchCount.get(0));
		System.out.println("-----"+JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(reqMap)));
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(reqMap));
	}

	/**
	 * 根据公司,品种获取面积产量柱状图数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "getAreaAndYield", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取面积产量柱状图数据", httpMethod = "GET", notes = "获取品种面积产量柱状图数据", consumes = "application/x-www-form-urlencoded")
	public String getAreaAndYield(HttpServletRequest request,
			@RequestParam(required = false) String officeId,
			@RequestParam(required = false) String productId,
			@RequestParam(required = false) String particularYear) {
		Map<String, Object> reqMap = new HashMap();
		if (StringUtils.isBlank(officeId) || "1".equals(officeId)) {
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			officeId = currentUser.getOffice().getId();
		}
		try{
			List<Dict> dlist = apiDictService.findDictlist("chart_value_type");
			int order_value = Integer.parseInt(dlist.get(0).getValue());
			Map<String, Object> getYield = productLibraryTreeService.getYield(officeId,productId,particularYear,order_value);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(getYield));
		} catch (Exception e) {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INTERNAL_ERROR.getCode(), ResultEnum.INTERNAL_ERROR.getMessage()));
		}
	}
}
