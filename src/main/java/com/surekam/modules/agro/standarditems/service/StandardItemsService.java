package com.surekam.modules.agro.standarditems.service;

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
import com.surekam.modules.agro.standarditemargs.dao.StandardItemArgsDao;
import com.surekam.modules.agro.standarditemargs.entity.StandardItemArgs;
import com.surekam.modules.agro.standarditems.dao.StandardItemsDao;
import com.surekam.modules.agro.standarditems.entity.StandardItems;
import com.surekam.modules.api.dto.req.StandardItemsReq;
import com.surekam.modules.api.dto.resp.SavaStandardItemsInfoResp;
import com.surekam.modules.api.dto.resp.StandardItemsResp;
import com.surekam.modules.sys.entity.User;

/**
 * 标准作业项表Service
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Component
@Transactional(readOnly = true)
public class StandardItemsService extends BaseService {

	@Autowired
	private StandardItemsDao standardItemsDao;

	@Autowired
	private ProductGrowthCycleDao productGrowthCycleDao;
	
	@Autowired
	private StandardItemArgsDao StandardItemArgsDao;
	
	/**
	 * 新增或修改作业项信息
	 * 
	 * @param siReq
	 *            请求参数
	 * @param user
	 *            用户信息
	 * @return
	 */
	@Transactional(readOnly = false)
	public ResultBean<String> savaStandardItems(StandardItemsReq siReq, User user) {
		// 新增
		if (StringUtils.isBlank(siReq.getId())) {
			List<StandardItems> findBySystemStandardsIdAndItemName = standardItemsDao.findBySystemStandardsIdAndItemName(siReq.getSystemStandardsId(), siReq.getItemName());
			if (findBySystemStandardsIdAndItemName != null && findBySystemStandardsIdAndItemName.size() > 0) {
				return ResultUtil.error(ResultEnum.DATA_EXIST.getCode(), ResultEnum.DATA_EXIST.getMessage());
			}
			
			if("1".equals(siReq.getOperationType())) {
				StandardItems siPojo = new StandardItems();
				if (StringUtils.isNotBlank(siReq.getParentId())) {
					StandardItems standardItems = standardItemsDao.get(siReq.getParentId());
					int sort = Integer.parseInt(standardItems.getSort());
					siPojo.setParentIds(standardItems.getParentIds() + standardItems.getId() + ",");
					siPojo.setSort(String.valueOf(sort + 1));
					siPojo.setParent(standardItemsDao.get(siReq.getParentId()));
				} else {
					StandardItems parent = new StandardItems();
					parent.setId("0");
					siPojo.setParentIds("0,");
					siPojo.setSort(siReq.getSort());
					siPojo.setParent(parent);
				}
				siPojo.setItemName(siReq.getItemName());
				siPojo.setSort(siReq.getSort());
				siPojo.setOperationType(siReq.getOperationType());
				siPojo.setSystemStandardsId(siReq.getSystemStandardsId());
				siPojo.setGrowthCycleId(siReq.getGrowthCycleId());
				siPojo.setGrowthCycleName(siReq.getGrowthCycleName());
				siPojo.setIconUrl(siReq.getIconUrl());
				
				siPojo.setCreateTime(new Date());
				siPojo.setCreateUserId(user.getId());
				siPojo.setStates(StandardItems.STATE_FLAG_ADD);
				standardItemsDao.save(siPojo);
			}
			
			// 1：目录 2：表单 判断是否在生长之前之间
			if("2".equals(siReq.getOperationType())) {
				ProductGrowthCycle pgcPojo = productGrowthCycleDao.get(siReq.getGrowthCycleId());
				// 生长周期开始时间-结束时间
				if (StringUtils.isNotBlank(pgcPojo.getBeginDay()) && StringUtils.isNotBlank(pgcPojo.getEndDay())) {
					// 判断请求参数的开始时间—结束时间
					if (StringUtils.isNotBlank(siReq.getStartDateNumber()) && StringUtils.isNotBlank(siReq.getEndDateNumber())) {
						Integer beginDay = Integer.valueOf(pgcPojo.getBeginDay());
						Integer endDay = Integer.valueOf(pgcPojo.getEndDay());
						// 作业项开始时间-结束时间
						Integer startDateNumber = Integer.valueOf(siReq.getStartDateNumber());
						Integer endDateNumber = Integer.valueOf(siReq.getEndDateNumber());
						if (startDateNumber < beginDay ) {
							return ResultUtil.error(ResultEnum.CYCLE_BEGIN_DAY.getCode(), ResultEnum.CYCLE_BEGIN_DAY.getMessage());
						}
						if (endDateNumber > endDay) {
							return ResultUtil.error(ResultEnum.CYCLE_END_DAY.getCode(), ResultEnum.CYCLE_END_DAY.getMessage());
						}

						List<StandardItems> standardItemsList = standardItemsDao.getStandardItemsAndOperationType(pgcPojo.getId(), "2");
						if (standardItemsList != null && standardItemsList.size() > 0) {
							for (StandardItems standardItems : standardItemsList) {
								if(StringUtils.isNotBlank(standardItems.getStartDateNumber()) && StringUtils.isNotBlank(standardItems.getEndDateNumber())) {
									Integer startDate = Integer.valueOf(standardItems.getStartDateNumber());
									Integer endDate = Integer.valueOf(standardItems.getEndDateNumber());
									if(startDate < beginDay) {
										return ResultUtil.error(ResultEnum.CYCLE_BEGIN_DAY.getCode(), ResultEnum.CYCLE_BEGIN_DAY.getMessage());
									}
									if (endDate > endDay) {
										return ResultUtil.error(ResultEnum.CYCLE_END_DAY.getCode(), ResultEnum.CYCLE_END_DAY.getMessage());
									}
								}
							}
						}
					}
				}
				
				StandardItems siPojo = new StandardItems();
				BeanUtils.copyProperties(siReq, siPojo);
				
				if (StringUtils.isNotBlank(siReq.getParentId())) {
					StandardItems standardItems = standardItemsDao.get(siReq.getParentId());
					int sort = Integer.parseInt(standardItems.getSort());
					siPojo.setParentIds(standardItems.getParentIds() + standardItems.getId() + ",");
					siPojo.setSort(String.valueOf(sort + 1));
					siPojo.setParent(standardItemsDao.get(siReq.getParentId()));
				} else {
					StandardItems parent = new StandardItems();
					parent.setId("0");
					siPojo.setParentIds("0,");
					siPojo.setSort(siReq.getSort());
					siPojo.setParent(parent);
				}
				siPojo.setCreateTime(new Date());
				siPojo.setCreateUserId(user.getId());
				siPojo.setStates(StandardItems.STATE_FLAG_ADD);
				// 保存信息
				standardItemsDao.save(siPojo);
				
				List<String> parameterList = siReq.getParameterList();
				for (int i = 0; i < parameterList.size(); i++) {
					StandardItemArgs entity = new StandardItemArgs();
					String string = parameterList.get(i);
					if ("1".equals(string)) {
						entity.setArgsName("拍照"); // 参数名称
						entity.setArgsDescription("拍照"); // 参数描述
						entity.setSort("31");
						entity.setIsRequired("0"); // 是否必填
						entity.setItemsId(siPojo.getId()); // 作业项ID
						entity.setCreateTime(new Date());
						entity.setCreateUserId(user.getId());
						entity.setArgsType("6");
						entity.setStates(StandardItemArgs.STATE_FLAG_ADD);
						StandardItemArgsDao.save(entity);
					}
					if ("2".equals(string)) {
						entity.setArgsName("备注"); // 参数名称
						entity.setArgsDescription("备注"); // 参数描述
						entity.setSort("30");
						entity.setIsRequired("0"); // 是否必填
						entity.setItemsId(siPojo.getId()); // 作业项ID
						entity.setCreateTime(new Date());
						entity.setCreateUserId(user.getId());
						entity.setArgsType("7");
						entity.setStates(StandardItemArgs.STATE_FLAG_ADD);
						StandardItemArgsDao.save(entity);
					}
					if ("3".equals(string)) {
						entity.setArgsName("日期"); // 参数名称
						entity.setArgsDescription("日期"); // 参数描述
						entity.setSort("1");
						entity.setIsRequired("1"); // 是否必填
						entity.setItemsId(siPojo.getId()); // 作业项ID
						entity.setCreateTime(new Date());
						entity.setCreateUserId(user.getId());
						entity.setArgsType("3");
						entity.setStates(StandardItemArgs.STATE_FLAG_ADD);
						StandardItemArgsDao.save(entity);
					}
					if ("3".equals(string)) {
						entity.setArgsName("日期"); // 参数名称
						entity.setArgsDescription("日期"); // 参数描述
						entity.setSort("1");
						entity.setIsRequired("1"); // 是否必填
						entity.setItemsId(siPojo.getId()); // 作业项ID
						entity.setCreateTime(new Date());
						entity.setCreateUserId(user.getId());
						entity.setArgsType("3");
						entity.setStates(StandardItemArgs.STATE_FLAG_ADD);
						StandardItemArgsDao.save(entity);
					}
					if ("9".equals(string)) {
						entity.setArgsName("采收类型"); // 参数名称
						entity.setArgsDescription("采收类型"); // 参数描述
						entity.setSort("4");
						entity.setIsRequired("1"); // 是否必填
						entity.setItemsId(siPojo.getId()); // 作业项ID
						entity.setCreateTime(new Date());
						entity.setCreateUserId(user.getId());
						entity.setArgsType("9");
						entity.setStates(StandardItemArgs.STATE_FLAG_ADD);
						StandardItemArgsDao.save(entity);
					}
					if ("13".equals(string)) {
						entity.setArgsName("采收量"); // 参数名称
						entity.setArgsDescription("采收量"); // 参数描述
						entity.setSort("5");
						entity.setIsRequired("1"); // 是否必填
						entity.setItemsId(siPojo.getId()); // 作业项ID
						entity.setCreateTime(new Date());
						entity.setCreateUserId(user.getId());
						entity.setArgsType("13");
						entity.setStates(StandardItemArgs.STATE_FLAG_ADD);
						StandardItemArgsDao.save(entity);
					}
				}
			}
			return ResultUtil.success("Success");
		}
		
		// 修改
		if (StringUtils.isNotBlank(siReq.getId())) {
			// 查询标准作业项
			StandardItems standardItems = standardItemsDao.get(siReq.getId());
			if(!siReq.getItemName().equals(standardItems.getItemName())) {
				List<StandardItems> findBySystemStandardsIdAndItemName = standardItemsDao.findBySystemStandardsIdAndItemName(siReq.getSystemStandardsId(), siReq.getItemName());
				if (findBySystemStandardsIdAndItemName != null && findBySystemStandardsIdAndItemName.size() > 0) {
					return ResultUtil.error(ResultEnum.DATA_EXIST.getCode(), ResultEnum.DATA_EXIST.getMessage());
				}
			}
			
			// 判断请求参数的开始和结束时间是否为空
			if(StringUtils.isNotBlank(siReq.getStartDateNumber()) && StringUtils.isNotBlank(siReq.getEndDateNumber())) {
				if(StringUtils.isNotBlank(standardItems.getStartDateNumber()) && StringUtils.isNotBlank(standardItems.getEndDateNumber())) {
					if(!siReq.getStartDateNumber().equals(standardItems.getStartDateNumber()) || !siReq.getEndDateNumber().equals(standardItems.getEndDateNumber())) {
						ProductGrowthCycle pgcPojo = productGrowthCycleDao.get(siReq.getGrowthCycleId());
						if(StringUtils.isNotBlank(pgcPojo.getBeginDay()) && StringUtils.isNotBlank(pgcPojo.getEndDay())) {
							Integer beginDay = Integer.valueOf(pgcPojo.getBeginDay());
							Integer endDay = Integer.valueOf(pgcPojo.getEndDay());
							// 作业项开始时间-结束时间
							Integer startDateNumber = Integer.valueOf(siReq.getStartDateNumber());
							Integer endDateNumber = Integer.valueOf(siReq.getEndDateNumber());
							if (startDateNumber < beginDay ) {
								return ResultUtil.error(ResultEnum.CYCLE_BEGIN_DAY.getCode(), ResultEnum.CYCLE_BEGIN_DAY.getMessage());
							}
							if (endDateNumber > endDay) {
								return ResultUtil.error(ResultEnum.CYCLE_END_DAY.getCode(), ResultEnum.CYCLE_END_DAY.getMessage());
							}

							List<StandardItems> standardItemsList = standardItemsDao.getStandardItemsAndOperationType(pgcPojo.getId(), "2");
							if (standardItemsList != null && standardItemsList.size() > 0) {
								for (StandardItems siList : standardItemsList) {
									if(StringUtils.isNotBlank(siList.getStartDateNumber()) && StringUtils.isNotBlank(siList.getEndDateNumber())) {
										Integer startDate = Integer.valueOf(siList.getStartDateNumber());
										Integer endDate = Integer.valueOf(siList.getEndDateNumber());
										if(startDate < beginDay) {
											return ResultUtil.error(ResultEnum.CYCLE_BEGIN_DAY.getCode(), ResultEnum.CYCLE_BEGIN_DAY.getMessage());
										}
										if (endDate > endDay) {
											return ResultUtil.error(ResultEnum.CYCLE_END_DAY.getCode(), ResultEnum.CYCLE_END_DAY.getMessage());
										}
									}
								}
							}
						}
					}
				}
			}
			
			StandardItems siPojo = new StandardItems();
			BeanUtils.copyProperties(siReq, siPojo);
			
			siPojo.setCreateUserId(standardItems.getCreateUserId());
			siPojo.setUpdateTime(new Date());
			siPojo.setUpdateUserId(user.getId());
			siPojo.setStates(StandardItems.STATE_FLAG_UPDATE);
			
			if(standardItems.getParent() != null) {
				siPojo.setParent(standardItems.getParent());
			}else {
				StandardItems parent = new StandardItems();
				parent.setId("0");
				siPojo.setParent(parent);
			}
			standardItemsDao.clear();
			standardItemsDao.flush();
			// 保存信息
			standardItemsDao.save(siPojo);
			return ResultUtil.success("Success");
		}
		return ResultUtil.error(ResultEnum.OPERATION_FAILED.getCode(), ResultEnum.OPERATION_FAILED.getMessage());
	}

	/**
	 * 删除作业标准信息
	 * 
	 * @param id
	 *            主键
	 * @param user
	 *            用户信息
	 * @return
	 */
	@Transactional(readOnly = false)
	public String delStandardItems(String id, User user) {
		StandardItems pojo = standardItemsDao.get(id);
		pojo.setUpdateTime(new Date());
		pojo.setUpdateUserId(user.getId());
		pojo.setStates(StandardItems.STATE_FLAG_DEL);
		standardItemsDao.save(pojo);
		return "Success";
	}

	/**
	 * 修改时查询作业标准对象信息
	 * 
	 * @param id
	 *            主键
	 * @return
	 */
	public StandardItemsResp getStandardItems(String id) {
		StandardItems pojo = standardItemsDao.get(id);
		StandardItemsResp resp = new StandardItemsResp();
		BeanUtils.copyProperties(pojo, resp);
		return resp;
	}

	/**
	 * Title: getStandardItemsSavaInfo Description: 新增作业项时请求接口
	 * 
	 * @param productGrowthCycleId 生长周期ID
	 * @param standardItemsId 作业项ID
	 * @return
	 */
	public SavaStandardItemsInfoResp getStandardItemsSavaInfo(String productGrowthCycleId , String standardItemsId) {
		SavaStandardItemsInfoResp pojo = new SavaStandardItemsInfoResp();
		if(StringUtils.isNotBlank(productGrowthCycleId)) {
			ProductGrowthCycle pgcPojo = productGrowthCycleDao.get(productGrowthCycleId);
			List<StandardItems> stlist = standardItemsDao.getStandardItemsList(pgcPojo.getId());
			int sort = 0;
			if (stlist != null && stlist.size() > 0) {
				sort = stlist.size() + 1;
			} else {
				sort = 1;
			}
			String systemStandardsIdMaxDate = standardItemsDao.getSystemStandardsIdMaxDate(pgcPojo.getId());
			
			pojo.setSort(String.valueOf(sort));
			pojo.setBeginDay(systemStandardsIdMaxDate);
			pojo.setId(pgcPojo.getId());
			pojo.setCycleName(pgcPojo.getCycleName());
		}
		
		if(StringUtils.isNotBlank(standardItemsId)) {
			StandardItems standardItems = standardItemsDao.get(standardItemsId);
			ProductGrowthCycle pgcPojo = productGrowthCycleDao.get(standardItems.getGrowthCycleId());
			String sort = standardItemsDao.findByParentIdQueryMaxSort(standardItems.getId());
			String systemStandardsIdMaxDate = standardItemsDao.getSystemStandardsIdMaxDate(pgcPojo.getId());
			
			pojo.setSort(sort);
			pojo.setBeginDay(systemStandardsIdMaxDate);
			pojo.setId(pgcPojo.getId());
			pojo.setCycleName(pgcPojo.getCycleName());
		}
		return pojo;
	}

}
