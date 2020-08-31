package com.surekam.modules.agro.report.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JavaType;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.Client;
import com.surekam.common.utils.DateUtils;
import com.surekam.modules.agro.basemanage.dao.BaseTreeDao;
import com.surekam.modules.agro.communication.dao.CommunicationDao;
import com.surekam.modules.agro.communication.entity.Communication;
import com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskValueVo;
import com.surekam.modules.agro.productbatchtask.entity.ResolveAndTaskVo;
import com.surekam.modules.agro.productbatchtask.service.ProductBatchTaskService;
import com.surekam.modules.agro.report.entity.DailyData;
import com.surekam.modules.agro.sensorsetup.entity.SensorData;
import com.surekam.modules.agro.sensorsetup.entity.SensorReturn;
import com.surekam.modules.agro.sensorsetup.entity.SensorTarget;
import com.surekam.modules.agro.sensorsetup.entity.WCSensorData;
import com.surekam.modules.agro.sensorsetup.entity.WCSensorTarget;
import com.surekam.modules.sys.dao.OfficeDao;


@Component
@Transactional(readOnly = true)
public class ReportService extends BaseService {

	@Autowired
	private CommunicationDao communicationDao;
	@Autowired
	private BaseTreeDao baseTreeDao;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private ProductBatchTaskService productBatchTaskService;
	
	public Map<String,DailyData<SensorData>> findSensorData(String startDate,String endDate ,
			List<String> sensorSerialNumbers,List<String> dateList) throws ParseException {
		JsonMapper jsonMapper = JsonMapper.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
		jsonMapper.setDateFormat(fmt);
		Date stDate = new Date();
		Date edDate = new Date();
		if (StringUtils.isNotBlank(startDate)) {
			stDate = DateUtils.parseDate(startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			edDate = DateUtils.parseDate(endDate);
		}
		Map<String,DailyData<SensorData>> mapSensorDatas = new HashMap<String, DailyData<SensorData>>();
		if (sensorSerialNumbers !=null && sensorSerialNumbers.size()>0) {
			for (int x = 0; x < sensorSerialNumbers.size(); x++) {
				String sensorSerialNumber = sensorSerialNumbers.get(x);
				// 如果是潜彬或者风向数据，调用他们的接口 2000000001:风向 ，2000000002潜彬
				if ("2000000001".equals(sensorSerialNumber) || "2000000002".equals(sensorSerialNumber)) {
					Map<Long, String> icon = new HashMap<Long, String>();
					icon.put(1l, "http://sensor.sureserve.cn/phone_wc/image/view/ph.png");// PH值传感器
					icon.put(2l, "http://sensor.sureserve.cn/phone_wc/image/view/o2.png");// 溶解氧传感器DO
					icon.put(3l, "http://sensor.sureserve.cn/phone_wc/image/view/nh4.png");// 氨氮传感器NH4
					icon.put(0l, "http://sensor.sureserve.cn/phone_wc/image/view/temp.png");// 水下温度传感器TEMP
					String fxqbTargeturl = "http://220.169.58.105/waterMonitor/data.action?load_sdates_by_asn";
					String fxqbDataurl = "http://220.169.58.105/waterMonitor/data.action?load_hours_data";
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("asn", sensorSerialNumber));
					String targetResult = Client.post(fxqbTargeturl, params, fxqbTargeturl);
					JavaType targetType = jsonMapper.createCollectionType(List.class, WCSensorTarget.class);
					List<WCSensorTarget> sensorTargetData = jsonMapper.fromJson(targetResult, targetType);
					if (sensorTargetData != null && sensorTargetData.size() > 0) {
						for (int i = 0; i < sensorTargetData.size(); i++) {
							WCSensorTarget wcSensorTarget = sensorTargetData.get(i);
							List<NameValuePair> dataParams = new ArrayList<NameValuePair>();
							dataParams.add(new BasicNameValuePair("ssn", wcSensorTarget.getSensorSn()));
							dataParams.add(new BasicNameValuePair("stime", DateUtils.formatDate(stDate, "yyyyMMdd00")));
							dataParams.add(new BasicNameValuePair("etime", DateUtils.formatDate(edDate, "yyyyMMdd24")));
							String dataResult = Client.post(fxqbDataurl, dataParams, fxqbDataurl);
							JavaType dataType = jsonMapper.createCollectionType(List.class, WCSensorData.class);
							List<WCSensorData> wcSensorDatas = jsonMapper.fromJson(dataResult, dataType);
							// 求最大值，最小值，平均值
							if (wcSensorDatas != null && wcSensorDatas.size() > 0) {
								for (int j = 0; j < dateList.size(); j++) {
									String date = dateList.get(j);
									List<WCSensorData> dayilySennorData = new ArrayList<WCSensorData>();
									for (WCSensorData wcSensorData : wcSensorDatas) {
										String dateTemp = wcSensorData.getMeasureTime().substring(0, 8);
										if (date.replace("-", "").equals(dateTemp)) {
											dayilySennorData.add(wcSensorData);
										}
									}
									SensorData sensorData = new SensorData();
									sensorData.setId(wcSensorTarget.getSensorSn());
									sensorData.setPathName(wcSensorTarget.getSensorSite());
									sensorData.setTargetName(wcSensorTarget.getTargetName().replace("传感器", ""));
									sensorData.setTargetUnit(wcSensorTarget.getTargetUnit());
									Long iconInt = Long.valueOf(wcSensorTarget.getSensorSn()) % 4;
									sensorData.setTargetIcon(icon.get(iconInt));
									DailyData<SensorData> dailyData =  mapSensorDatas.get(date);
									if (dailyData == null) {
										dailyData = new DailyData<SensorData>();
										dailyData.setCountNUm("0");
										dailyData.setDate(date);
										mapSensorDatas.put(date, dailyData);
									}else{//如果已经存在对象，新的传感器又没有数据，就保持原有的对象不变
										if (dayilySennorData.size()>0) {
											dailyData.setCountNUm("1");
											fixDailySennorData(dayilySennorData,sensorData);
											dailyData.getDailyDatas().add(sensorData);
											mapSensorDatas.put(date, dailyData);
										}
									}
								}
							}
						}
					}
				} else {// 其他的为我们的数据
						// 1.获取传感器指标
					String url = "http://sensor.sureserve.cn/interfaces/gateway_handler.ashx?action=gateway_target";
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("gid", sensorSerialNumber));
					String result = Client.post(url, params, url);
					JavaType javaType = jsonMapper.createCollectionType(SensorReturn.class, SensorTarget.class);
					SensorReturn<SensorTarget> sensorTargetData = jsonMapper.fromJson(result, javaType);
					if ("0".equals(sensorTargetData.getResult())) {
						List<SensorTarget> sensorTargets = sensorTargetData.getData();
						if (sensorTargets != null && sensorTargets.size() > 0) {
							for (int i = 0; i < sensorTargets.size(); i++) {
								SensorTarget sensorTarget = sensorTargets.get(i);
								String dataUrl = "http://sensor.sureserve.cn/interfaces/target_handler.ashx?action=target_day";
								List<NameValuePair> dataParams = new ArrayList<NameValuePair>();
								dataParams.add(new BasicNameValuePair("stid", sensorTarget.getId()));
								dataParams.add(new BasicNameValuePair("tid", sensorTarget.getTargetId()));
								dataParams.add(new BasicNameValuePair("stime", DateUtils.formatDate(stDate, "yyyyMMdd")));
								dataParams.add(new BasicNameValuePair("etime", DateUtils.formatDate(edDate, "yyyyMMdd")));
								String dataResult = Client.post(dataUrl, dataParams, dataUrl);
								JavaType sensorDataType = jsonMapper.createCollectionType(SensorReturn.class, SensorData.class);
								SensorReturn<SensorData> sensorDatas = jsonMapper.fromJson(dataResult, sensorDataType);
								List<SensorData> sensorDatasList = sensorDatas.getData();
								for (int j = 0; j < dateList.size(); j++) {
									String date = dateList.get(j);
									for (SensorData sensorData:sensorDatasList) {
										String dateTemp = sensorData.getMeasureTime().substring(0, 10);
										if (date.equals(dateTemp)) {
											sensorData.setTargetName(sensorTarget.getTargetName());
											sensorData.setTargetUnit(sensorTarget.getTargetUnit());
											sensorData.setTargetIcon("http://sensor.sureserve.cn" + sensorTarget.getTargetIcon());
											DailyData<SensorData> dailyData =  mapSensorDatas.get(date);
											if (dailyData == null) {
												dailyData = new DailyData<SensorData>();
												dailyData.setCountNUm("1");
												dailyData.setDate(date);
											}
											dailyData.getDailyDatas().add(sensorData);
											mapSensorDatas.put(date, dailyData);
										}
									}
								
								}
							}
						}
					}
				}
			}
		}
		return mapSensorDatas;
	}
	
	private  void fixDailySennorData(List<WCSensorData> wcSensorDatas,SensorData sensorData){
		int size = wcSensorDatas.size();
		Collections.sort(wcSensorDatas, new Comparator<WCSensorData>() {
			@Override
			public int compare(WCSensorData o1, WCSensorData o2) {
				return o1.getMeasureData().compareTo(o2.getMeasureData());
			}
		});
		sensorData.setMeasureMinData(wcSensorDatas.get(0).getMeasureData() + "");
		sensorData.setMeasureMaxData(wcSensorDatas.get(size - 1).getMeasureData() + "");
		// 平均值
		Double countData = 0d;
		for (int j = 0; j < size; j++) {
			countData = countData + wcSensorDatas.get(j).getMeasureData();
		}
		Double measureData = countData / size;
		sensorData.setMeasureData(new BigDecimal(measureData).setScale(2, RoundingMode.HALF_UP) + "");
	}

	public Map<String,DailyData<ResolveAndTaskVo>> findTaskListData(String startDate,
			String endDate, String officeId, String isOffice, String baseId ,List<String> dateList) throws ParseException {
		Map<String,DailyData<ResolveAndTaskVo>> mapTaskDatas = new HashMap<String, DailyData<ResolveAndTaskVo>>();
		String targetId = "";
		if ("0".equals(isOffice)) {
			targetId = officeId;
		}else{
			targetId = baseId;
		}
		List<ResolveAndTaskVo> resolveAndTaskVos = productBatchTaskService.fetchDailyTaskList(targetId, isOffice, startDate, endDate, "finish", null);
		if ((dateList!=null && dateList.size()>0) && (resolveAndTaskVos!=null && resolveAndTaskVos.size()>0)) {
			for (int i = 0; i <dateList.size(); i++) {
				String date = dateList.get(i);
				for (ResolveAndTaskVo resolveAndTaskVo:resolveAndTaskVos) {
					//取提交的时间数据
					List<ResolveAndTaskValueVo> resolveAndTaskValueVos = resolveAndTaskVo.getTaskValueList();
					for (ResolveAndTaskValueVo resolveAndTaskValueVo:resolveAndTaskValueVos) {
						if ("日期".equals(resolveAndTaskValueVo.getArgsName())) {
							String d = resolveAndTaskValueVo.getTaskItemArgsValue();
							if(StringUtils.isNotBlank(d)){
								Date day = DateUtils.parseDate(d);
								String dateTemp = DateUtils.formatDate(day,"yyyy-MM-dd");
								if (date.equals(dateTemp)) {
									DailyData<ResolveAndTaskVo> dailyData =  mapTaskDatas.get(date);
									if (dailyData == null) {
										dailyData = new DailyData<ResolveAndTaskVo>();
										dailyData.setCountNUm("1");
										dailyData.setDate(date);
									}
									//dailyData.getDailyDatas().add(resolveAndTaskVo);
									mapTaskDatas.put(date, dailyData);
								}
							}
						}
					}
				}
			}
		}
		return mapTaskDatas;
	}

	public Map<String, DailyData<Communication>> findCommunicationData(
			String startDate, String endDate, String officeId, String isOffice,
			String baseId, List<String> dateList) {
		Map<String,DailyData<Communication>> mapTaskDatas = new HashMap<String, DailyData<Communication>>();
		String targetId = "";
		if ("0".equals(isOffice)) {
			targetId = officeId;
		}else{
			targetId = baseTreeDao.get(baseId).getOfficeId();
		}
		//找出所选公司下的所有公司
		List<Object> ids = new ArrayList<Object>();
		ids.add(targetId);
		ids.addAll(officeDao.getOfficeIdList(targetId));
		DetachedCriteria dc = communicationDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Communication.FIELD_DEL_FLAG_XGXT, Communication.STATE_FLAG_DEL));
		dc.add(Restrictions.in("baseId", ids.toArray()));
		dc.add(Restrictions.between("createTime", DateUtils.parseDate(startDate+" 00:00:00"), DateUtils.parseDate(endDate+" 23:59:59")));
		dc.addOrder(Order.asc("communicationTop")).addOrder(Order.asc("sort"));
		List<Communication> communications = communicationDao.find(dc);
		if ((dateList!=null && dateList.size()>0) && (communications!=null && communications.size()>0)) {
			for (int i = 0; i <dateList.size(); i++) {
				String date = dateList.get(i);
				for (Communication communication:communications) {
					Date day = communication.getCreateTime();
					String dateTemp = DateUtils.formatDate(day,"yyyy-MM-dd");
					if (date.equals(dateTemp)) {
						DailyData<Communication> dailyData =  mapTaskDatas.get(date);
						if (dailyData == null) {
							dailyData = new DailyData<Communication>();
							dailyData.setCountNUm("1");
							dailyData.setDate(date);
						}
						dailyData.getDailyDatas().add(communication);
						mapTaskDatas.put(date, dailyData);
					}
			    }
			}
		}
		return mapTaskDatas;
	}

}
