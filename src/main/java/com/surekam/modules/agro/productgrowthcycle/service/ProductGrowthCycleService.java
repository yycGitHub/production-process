package com.surekam.modules.agro.productgrowthcycle.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.service.BaseService;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.productgrowthcycle.dao.ProductGrowthCycleDao;
import com.surekam.modules.agro.productgrowthcycle.entity.ProductGrowthCycle;
import com.surekam.modules.agro.sensorthreshold.dao.SensorThresholdDao;
import com.surekam.modules.agro.sensorthreshold.entity.SensorThreshold;
import com.surekam.modules.agro.sensorthreshold.service.SensorThresholdService;
import com.surekam.modules.agro.standarditems.dao.StandardItemsDao;
import com.surekam.modules.agro.standarditems.entity.StandardItems;
import com.surekam.modules.api.dto.req.ProductGrowthCycleReq;
import com.surekam.modules.api.dto.req.SensorThresholdReq;
import com.surekam.modules.api.dto.resp.ProductGrowthCycleResp;
import com.surekam.modules.api.dto.resp.SensorThresholdRresp;
import com.surekam.modules.sys.entity.User;

/**
 * 生长周期阶段表Service
 * 
 * @author tangjun
 * @version 2019-04-26
 */
@Component
@Transactional(readOnly = true)
public class ProductGrowthCycleService extends BaseService {

	@Autowired
	private ProductGrowthCycleDao productGrowthCycleDao;
	
	@Autowired
	private StandardItemsDao standardItemsDao;
	
	@Autowired
	private SensorThresholdDao sensorThresholdDao;
	
	@Autowired
	private SensorThresholdService sensorThresholdService;
	

	/**
	 * 新增或修改生长周期
	 * 
	 * @param pgcReq
	 *            请求参数
	 * @param user
	 *            用户信息
	 * @return
	 */
	@Transactional(readOnly = false)
	public ResultBean<String> savaProductGrowthCycle(ProductGrowthCycleReq pgcReq, User user) {
		if(StringUtils.isBlank(pgcReq.getId())) {
			ProductGrowthCycle pgcPojo = new ProductGrowthCycle();
			BeanUtils.copyProperties(pgcReq, pgcPojo);
			
			List<ProductGrowthCycle> findBySysEntStandardIdAndCycleName = productGrowthCycleDao.findBySysEntStandardIdAndCycleName(pgcPojo.getSysEntStandardId(), pgcPojo.getCycleName());
			if(!findBySysEntStandardIdAndCycleName.isEmpty()) {
				return ResultUtil.error(ResultEnum.CYCLE_NAME.getCode(), ResultEnum.CYCLE_NAME.getMessage());
			}
			
			List<ProductGrowthCycle> pgcArrayList = productGrowthCycleDao.getProductGrowthCycleList(pgcPojo.getSysEntStandardId());
			if (StringUtils.isNotBlank(pgcPojo.getBeginDay()) && StringUtils.isNotBlank(pgcPojo.getEndDay())) {
				Integer pgcReqBeginDay = Integer.valueOf(pgcPojo.getBeginDay());
				for (ProductGrowthCycle pojo : pgcArrayList) {
					if(StringUtils.isNotBlank(pojo.getEndDay()) && StringUtils.isNotBlank(pojo.getBeginDay())) {
						Integer endDay = Integer.valueOf(pojo.getEndDay());
						Integer beginDay = Integer.valueOf(pojo.getBeginDay());
						if ((endDay >= pgcReqBeginDay) && (beginDay <= pgcReqBeginDay)) {
							return ResultUtil.error(ResultEnum.CYCLE_BEGIN_DAY.getCode(), ResultEnum.CYCLE_BEGIN_DAY.getMessage());
						}
					}
				}

				Integer pgcReqEndDay = Integer.valueOf(pgcPojo.getEndDay());
				for (ProductGrowthCycle pojo : pgcArrayList) {
					if(StringUtils.isNotBlank(pojo.getEndDay()) && StringUtils.isNotBlank(pojo.getBeginDay())) {
						Integer endDay = Integer.valueOf(pojo.getEndDay());
						Integer beginDay = Integer.valueOf(pojo.getBeginDay());
						if ((endDay >= pgcReqEndDay) && (beginDay <= pgcReqEndDay)) {
							return ResultUtil.error(ResultEnum.CYCLE_END_DAY.getCode(), ResultEnum.CYCLE_END_DAY.getMessage());
						}
					}
				}
			}
			pgcPojo.setCreateTime(new Date());
			pgcPojo.setCreateUserId(user.getId());
			pgcPojo.setStates(ProductGrowthCycle.STATE_FLAG_ADD);
			productGrowthCycleDao.save(pgcPojo);
			
			// 新增传感器
			List<SensorThresholdReq> stReq = pgcReq.getStReq();
			if(stReq != null) {
				for (SensorThresholdReq sensorThresholdReq : stReq) {
					sensorThresholdService.savaSensorThreshold(sensorThresholdReq, user, pgcPojo.getId());
				}
			}
			return ResultUtil.success("Success");
		}
		
		if(StringUtils.isNotBlank(pgcReq.getId())) {
			ProductGrowthCycle pgcPojo = new ProductGrowthCycle();
			BeanUtils.copyProperties(pgcReq, pgcPojo);
			
			ProductGrowthCycle productGrowthCycle = productGrowthCycleDao.get(pgcPojo.getId());
			if(!pgcPojo.getCycleName().equals(productGrowthCycle.getCycleName())) {
				List<ProductGrowthCycle> findBySysEntStandardIdAndCycleName = productGrowthCycleDao.findBySysEntStandardIdAndCycleName(pgcPojo.getSysEntStandardId(), pgcPojo.getCycleName());
				if(!findBySysEntStandardIdAndCycleName.isEmpty()) {
					return ResultUtil.error(ResultEnum.CYCLE_NAME.getCode(), ResultEnum.CYCLE_NAME.getMessage());
				}
			}
			
			if (StringUtils.isNotBlank(pgcPojo.getBeginDay()) && StringUtils.isNotBlank(pgcPojo.getEndDay())) {
				if ((!pgcPojo.getBeginDay().equals(productGrowthCycle.getBeginDay())) || (!pgcPojo.getEndDay().equals(productGrowthCycle.getEndDay()))) {
					List<ProductGrowthCycle> pgcArrayList = productGrowthCycleDao.getProductGrowthCycleList(pgcPojo.getSysEntStandardId());
					if (StringUtils.isNotBlank(pgcPojo.getBeginDay()) && StringUtils.isNotBlank(pgcPojo.getEndDay())) {
						Integer pgcReqBeginDay = Integer.valueOf(pgcPojo.getBeginDay());
						for (ProductGrowthCycle pojo : pgcArrayList) {
							if (!pgcPojo.getId().equals(pojo.getId())) {
								if(StringUtils.isNotBlank(pojo.getEndDay()) && StringUtils.isNotBlank(pojo.getBeginDay())) {
									Integer endDay = Integer.valueOf(pojo.getEndDay());
									Integer beginDay = Integer.valueOf(pojo.getBeginDay());
									if ((endDay >= pgcReqBeginDay) && (beginDay <= pgcReqBeginDay)) {
										return ResultUtil.error(ResultEnum.CYCLE_BEGIN_DAY.getCode(), ResultEnum.CYCLE_BEGIN_DAY.getMessage());
									}
								}
								
							}
						}
						Integer pgcReqEndDay = Integer.valueOf(pgcPojo.getEndDay());
						for (ProductGrowthCycle pojo : pgcArrayList) {
							if (!pgcPojo.getId().equals(pojo.getId())) {
								if (StringUtils.isNotBlank(pojo.getEndDay()) && StringUtils.isNotBlank(pojo.getBeginDay())) {
									Integer endDay = Integer.valueOf(pojo.getEndDay());
									Integer beginDay = Integer.valueOf(pojo.getBeginDay());
									if ((endDay >= pgcReqEndDay) && (beginDay <= pgcReqEndDay)) {
										return ResultUtil.error(ResultEnum.CYCLE_END_DAY.getCode(), ResultEnum.CYCLE_END_DAY.getMessage());
									}
								}
							}
						}
					}
				}
			}
			pgcPojo.setCreateUserId(productGrowthCycle.getCreateUserId());
			pgcPojo.setUpdateTime(new Date());
			pgcPojo.setUpdateUserId(user.getId());
			pgcPojo.setStates(ProductGrowthCycle.STATE_FLAG_UPDATE);
			productGrowthCycleDao.clear();
			productGrowthCycleDao.flush();
			productGrowthCycleDao.save(pgcPojo);
			
			// 修改传感器
			List<SensorThresholdReq> stReq = pgcReq.getStReq();
			if ((stReq != null) && (stReq != null && stReq.size() > 0)) {
				for (SensorThresholdReq sensorThresholdReq : stReq) {
					sensorThresholdService.savaSensorThreshold(sensorThresholdReq, user, pgcPojo.getId());
				}
			} else {
				sensorThresholdDao.delete(pgcPojo.getId());
			}
			return ResultUtil.success("Success");
		}
		return ResultUtil.error(ResultEnum.OPERATION_FAILED.getCode(), ResultEnum.OPERATION_FAILED.getMessage());
	}

	/**
	 * 删除生长周期
	 * 
	 * @param user
	 *            用户信息
	 * @param id
	 *            生长周期主键
	 * @return
	 */
	@Transactional(readOnly = false)
	public ResultBean<String> delProductGrowthCycle(User user, String id) {
		ProductGrowthCycle pojo = productGrowthCycleDao.get(id);
		List<StandardItems> stlist = standardItemsDao.getStandardItemsList(pojo.getId());
		if (stlist != null && stlist.size() > 0) {
			return ResultUtil.error(ResultEnum.TASK_DATA_EXISTS.getCode(), ResultEnum.TASK_DATA_EXISTS.getMessage());
		}
		pojo.setUpdateTime(new Date());
		pojo.setUpdateUserId(user.getId());
		pojo.setStates(ProductGrowthCycle.STATE_FLAG_DEL);
		productGrowthCycleDao.save(pojo);
		return ResultUtil.success("Success");
	}

	
	/**
	 * 获取开始日期
	 * 
	 * @param user
	 * @param id
	 * @return
	 */
	public String getBeginDate(User user, String id) {
		String str = productGrowthCycleDao.findBySysEntStandardIdMaxDate(id);
		return str;
	}

	/**
	 * 
	 * Title: getProductGrowthCycleResp Description: 修改时获取生长周期对象信息
	 * 
	 * @param id
	 *            主键
	 * @return
	 */
	public ProductGrowthCycleResp getProductGrowthCycleResp(String id) {
		// 查询生长周期阶段信息
		ProductGrowthCycle productGrowthCycle = productGrowthCycleDao.get(id);

		ProductGrowthCycleResp pgcResp = new ProductGrowthCycleResp();
		BeanUtils.copyProperties(productGrowthCycle, pgcResp);

		// 查詢传感器信息
		List<SensorThresholdRresp> siRespArrayList = new ArrayList<SensorThresholdRresp>();
		List<SensorThreshold> findByGrowthCycleId = sensorThresholdService.findByGrowthCycleId(productGrowthCycle.getId());
		if (findByGrowthCycleId != null && findByGrowthCycleId.size() > 0) {
			for (SensorThreshold sensorThreshold : findByGrowthCycleId) {
				SensorThresholdRresp stResp = new SensorThresholdRresp();
				BeanUtils.copyProperties(sensorThreshold, stResp);
				siRespArrayList.add(stResp);
			}
		} else {
			SensorThresholdRresp stResp = new SensorThresholdRresp();
			siRespArrayList.add(stResp);
		}
		pgcResp.setStReq(siRespArrayList);
		return pgcResp;
	}
}
