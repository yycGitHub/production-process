package com.surekam.modules.api.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import com.surekam.modules.sys.entity.User;

import io.swagger.annotations.ApiOperation;

/**
 * 报表接口Controller
 * 
 * @author liwei
 * @version 2019-05-08
 */
@Controller
@RequestMapping(value = "api/report")
public class ReportController extends BaseController {

	@Autowired
	private SpeciesManageApiService speciesManageApiService;
	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private SensorSetupService sensorSetupService;
	@Autowired
	private ApiUserService apiUserService;
	@Autowired
	private VideoService videoService;

	/**
	 * 获取农事种类及数量
	 * 
	 * @return
	 */
	@RequestMapping(value = "getAgroProductCount", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取农事种类及数量", httpMethod = "GET", notes = "获取农事种类及数量", consumes = "application/x-www-form-urlencoded")
	public String getAgroProductCount(HttpServletRequest request, @RequestParam String code) {
		List<ProductLibraryTreeVo> list = new ArrayList<ProductLibraryTreeVo>();
		List<ProductLibraryTreeVo> productLibraryTreeList = speciesManageApiService.getProductLibraryTreeList("", "1",
				false);
		List<ProductLibraryTreeVo> childrenList = productLibraryTreeList.get(0).getChildren();
		for (ProductLibraryTreeVo productLibraryTreeVo : childrenList) {
			List<ProductLibraryTreeVo> tree = new ArrayList<ProductLibraryTreeVo>();
			productLibraryTreeVo.setChildrenCount(productLibraryTreeVo.getChildren().size() + "");
			for (ProductLibraryTreeVo product : productLibraryTreeVo.getChildren()) {
				if ("tulufan".equals(code)) {
					if (product.getId().equals("7e08b67097cc4f2e8d89d7381c5f42c7")
							|| product.getId().equals("fd036e233cf044578911807a3e4ec7d1")) {
						product.setChildrenCount(product.getChildren().size() + "");
						tree.add(product);
					}
				} else if ("wangcheng".equals(code)) {
					if (product.getId().equals("df3fd18d48e84c748f78684abba93166") || product.getId().equals("5adaeed3496c45b1b9cd332272399aa6") 
							|| product.getId().equals("2597f41ea5d0492a8dbca578be76bf99")|| product.getId().equals("b5fbf9819a5e4bdbac5e0ed60d9057e6")
							|| product.getId().equals("9fcd54c50c0d443182e1189c0f9b173e")|| product.getId().equals("32667cb84d9f4f659b7dc557a29784d4")
							|| product.getId().equals("a8ee959e99474b2cbde67bf87965161e")|| product.getId().equals("5dcd283c924546059227fe70a8660114")) {
						product.setChildrenCount(product.getChildren().size() + "");
						tree.add(product);
					}
				} else if ("rice".equals(code)) {
					if (product.getId().equals("e2f7eae4997249d68e1fd83ad04da95f")) {
						product.setChildrenCount(product.getChildren().size() + "");
						tree.add(product);
					}
				} else if ("nanxian".equals(code)) {
					if (product.getId().equals("e2f7eae4997249d68e1fd83ad04da95f") || product.getId().equals("df3fd18d48e84c748f78684abba93166")) {
						product.setChildrenCount(product.getChildren().size() + "");
						tree.add(product);
					}
				} else {
					product.setChildrenCount(product.getChildren().size() + "");
					tree.add(product);
				}
			}
			productLibraryTreeVo.setChildren(tree);
			productLibraryTreeVo.setChildrenCount(tree.size() + "");
			list.add(productLibraryTreeVo);
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
	}

	/**
	 * 获取品种面积
	 * 
	 * @return
	 */
	@RequestMapping(value = "getAgroProductArea", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取品种面积", httpMethod = "GET", notes = "获取品种面积", consumes = "application/x-www-form-urlencoded")
	public String getAgroProductArea(HttpServletRequest request, @RequestParam String code) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String id = "2";
		if (code.equals("wangcheng")) {
			id = "a2bfcffa042646a98360ef86343de977";
		} else if (code.equals("tulufan")) {
			id = "433620addfd144028f9d1afaabfe8299";
		} else if (code.equals("rice")) {
			id = "f76a6516f42647699fa1c7ceba53a511";
		} else if (code.equals("nanxian")) {
			id = "ddc5d5315cb049b6875906f2d3a2915d";
		} else if (code.equals("yuanxiang")) {
			id = "2";
		}
		List<Object> productList = productLibraryTreeService.getAgroProductArea(id);
		for (int i = 0; i < productList.size(); i++) {
			Object[] obj = (Object[]) productList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("productName", obj[0].toString());
			map.put("area", obj[1].toString());
			list.add(map);
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
	}

	/**
	 * 获取每月新加基地数
	 * 
	 * @return
	 */
	@RequestMapping(value = "getMonthCompanyCount", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取每月新加基地数", httpMethod = "GET", notes = "获取每月新加基地数", consumes = "application/x-www-form-urlencoded")
	public String getMonthCompanyCount(HttpServletRequest request, String code) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<String> monthList = getInitMonthMapWithZero();
		String id = "2";
		if (code.equals("wangcheng")) {
			id = "a2bfcffa042646a98360ef86343de977";
		} else if (code.equals("tulufan")) {
			id = "433620addfd144028f9d1afaabfe8299";
		} else if (code.equals("rice")) {
			id = "f76a6516f42647699fa1c7ceba53a511";
		} else if (code.equals("nanxian")) {
			id = "ddc5d5315cb049b6875906f2d3a2915d";
		} else if (code.equals("yuanxiang")) {
			id = "2";
		}
		List<Object> baseList = productLibraryTreeService.getMonthCompanyCount(id);
		for (String month : monthList) {
			Map<String, Object> map = new HashMap<String, Object>();
			int i = 0;
			for (i = 0; i < baseList.size(); i++) {
				Object[] obj = (Object[]) baseList.get(i);
				String mon = obj[0].toString();
				String con = obj[1].toString();
				if (mon.equals(month)) {
					map.put("month", mon);
					map.put("count", con);
					break;
				}
			}
			if (i == baseList.size()) {
				map.put("month", month);
				map.put("count", "0");
			}
			list.add(map);
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
	}

	/**
	 * 获取品种对应的专家人数
	 * 
	 * @return
	 */
	@RequestMapping(value = "getProductExpertCount", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "根据品种查专家人数", httpMethod = "GET", notes = "根据品种查专家人数", consumes = "application/x-www-form-urlencoded")
	public String getProductExpertCount(HttpServletRequest request, @RequestParam String code) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Object> baseList = productLibraryTreeService.getProductExpertCount(code);
		for (int i = 0; i < baseList.size(); i++) {
			Object[] obj = (Object[]) baseList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("productName", obj[0].toString());
			map.put("expertCount", obj[1].toString());
			list.add(map);
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
	}

	/**
	 * 获取水稻的专家数及在线人数
	 * 
	 * @return
	 */
	@RequestMapping(value = "getExpertsOnlineCount", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取水稻的专家数及在线人数", httpMethod = "GET", notes = "获取水稻的专家数及在线人数", consumes = "application/x-www-form-urlencoded")
	public String getExpertsOnlineCount(HttpServletRequest request, @RequestParam String code) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Video> objList = videoService.getExpertsOnlineCount(code);
		int onlineCount = 0;
		for (int i = 0; i < objList.size(); i++) {
			Video obj = objList.get(i);
			if ("1".equals(obj.getOnlineStates())) {
				onlineCount++;
			}
		}
		map.put("expertCount", objList.size());
		map.put("onlineCount", onlineCount);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
	}

	private List<String> getInitMonthMapWithZero() {
		List<String> list = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < 12; i++) {
			int k = c.get(Calendar.YEAR);
			int j = c.get(Calendar.MONTH) + 1 - i;
			String date = "";
			if (j >= 1) {
				date = k + "-" + (j >= 10 ? "" : "0") + j;
			} else {
				int p = 11 - i;// 剩余循环次数
				int m = c.get(Calendar.YEAR) - 1;
				int n = c.get(Calendar.MONTH) + 2 + p;
				date = m + "-" + (n >= 10 ? "" : "0") + n;
			}
			list.add(date);
		}
		Collections.sort(list);
		return list;
	}

	@RequestMapping(value = "fetchSensorDailyData", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "传感数据查询", notes = "传感数据查询", consumes = "application/json")
	public String fetchSensorDailyData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String sensorSerialNumber, 
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
		jsonMapper.setDateFormat(fmt);
		try {
			List<String> sensorSerialNumbers = new ArrayList<String>();
			sensorSerialNumbers.add(sensorSerialNumber);
			Map<String, DailyData<SensorData>> rebackSensorDatas = reportService.findSensorData(startDate, endDate, sensorSerialNumbers, null);
			return jsonMapper.toJson(ResultUtil.success(rebackSensorDatas));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("传感数据查询：" + e);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TASK_LIST_ERROR.getCode(), ResultEnum.TASK_LIST_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "fetchDailyAllData", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "集合报表数据", notes = "集合报表数据", consumes = "application/json")
	public String fetchDailyAllData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String officeId, @RequestParam(required = false) String baseId,
			@RequestParam(required = false) String isOffice, @RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
		jsonMapper.setDateFormat(fmt);
		try {
			// 日期数组
			List<String> dateList = new ArrayList<String>();
			Date startD = DateUtils.parseDate(startDate);
			Date endD = DateUtils.parseDate(endDate);
			Long cal = DateUtils.calcDays(startD, endD);
			Date today = new Date();
			for (int i = 0; i <= cal; i++) {
				if ((DateUtils.addDays(startD, i).getTime() - today.getTime()) <= 0) {
					dateList.add(DateUtils.formatDate(DateUtils.addDays(startD, i), "yyyy-MM-dd"));
				}
			}
			List<Object> allDate = new ArrayList<Object>();

			String token = request.getHeader("X-Token");
			User user = apiUserService.getUserByToken(token);
			ReportData<DailyData<SensorData>> cgqdata = new ReportData<DailyData<SensorData>>();
			cgqdata.setColumnCode("1");
			cgqdata.setColumnName("传感器");
			Page<SensorSetup> page = new Page<SensorSetup>(0, 10000);
			page = sensorSetupService.fetchFind(page, baseId, isOffice, officeId, user);
			if (page.getList() != null && page.getList().size() > 0) {
				List<String> sensorNum = new ArrayList<String>();
				for (int i = 0; i < page.getList().size(); i++) {
					sensorNum.add(page.getList().get(i).getSensorSerialNumber());
				}
				Map<String, DailyData<SensorData>> rebackSensorDatas = reportService.findSensorData(startDate, endDate, sensorNum, dateList);
				cgqdata.setDatas(rebackSensorDatas);
			}
			allDate.add(cgqdata);

			// ReportData<DailyData<String>> spqdata = new ReportData<DailyData<String>>();
			// spqdata.setColumnCode("2");
			// spqdata.setColumnName("视频");
			// Map<String,DailyData<String>> dailyDataList = new HashMap<String,
			// DailyData<String>>();
			// for (int i = 0; i < dateList.size(); i++) {
			// DailyData<String> dailyData = new DailyData<String>();
			// dailyData.setDate(dateList.get(i));
			// dailyData.setCountNUm("1");
			// dailyDataList.put(dateList.get(i),dailyData);
			// }
			// spqdata.setDatas(dailyDataList);
			// allDate.add(spqdata);

			ReportData<DailyData<ResolveAndTaskVo>> nsqdata = new ReportData<DailyData<ResolveAndTaskVo>>();
			nsqdata.setColumnCode("3");
			nsqdata.setColumnName("农事记录");
			Map<String, DailyData<ResolveAndTaskVo>> taskListDatas = reportService.findTaskListData(startDate, endDate, officeId, isOffice, baseId, dateList);
			nsqdata.setDatas(taskListDatas);
			allDate.add(nsqdata);

			ReportData<DailyData<Communication>> gtqdata = new ReportData<DailyData<Communication>>();
			gtqdata.setColumnCode("4");
			gtqdata.setColumnName("沟通记录");
			Map<String, DailyData<Communication>> walkListDatas = reportService.findCommunicationData(startDate, endDate, officeId, isOffice, baseId, dateList);
			gtqdata.setDatas(walkListDatas);
			allDate.add(gtqdata);

			return jsonMapper.toJson(ResultUtil.success(allDate));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("传感数据查询：" + e);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TASK_LIST_ERROR.getCode(), ResultEnum.TASK_LIST_ERROR.getMessage()));
		}
	}

}
