package com.surekam.modules.agro.standarditemcategory.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.service.BaseService;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.modules.agro.standarditemcategory.dao.StandardItemCategoryDao;
import com.surekam.modules.agro.standarditemcategory.entity.StandardItemCategory;
import com.surekam.modules.api.dto.resp.StandardItemCategoryResp;
import com.surekam.modules.sys.entity.User;

/**
 * 作业项类别表(包括 施肥 投料等 )Service
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Component
@Transactional(readOnly = true)
public class StandardItemCategoryService extends BaseService {

	@Autowired
	private StandardItemCategoryDao standardItemCategoryDao;

	/**
	 * 获取作业项类别
	 * 
	 * @return
	 */
	public List<StandardItemCategoryResp> getStandardItemCategoryList() {
		List<StandardItemCategoryResp> respArrayList = new ArrayList<StandardItemCategoryResp>();

		List<StandardItemCategory> findAll = standardItemCategoryDao.findBy();
		for (StandardItemCategory pojo : findAll) {
			StandardItemCategoryResp resp = new StandardItemCategoryResp();
			resp.setId(pojo.getId());
			resp.setName(pojo.getTaskItemCategoryName());
			respArrayList.add(resp);
		}
		return respArrayList;
	}

	/**
	 * Title: savaStandardItemCategory Description: 保存作业项类别
	 * @param user 
	 * 
	 * @param taskItemCategoryName
	 * @return
	 * 
	 */
	@Transactional(readOnly = false)
	public ResultBean<String> savaStandardItemCategory(User user, String taskItemCategoryName) {
		List<StandardItemCategory> findByTaskItemCategoryName = standardItemCategoryDao.findByTaskItemCategoryNameList(taskItemCategoryName);
		if (findByTaskItemCategoryName != null && findByTaskItemCategoryName.size() > 0) {
			return ResultUtil.error(ResultEnum.DATA_EXIST.getCode(), ResultEnum.DATA_EXIST.getMessage());
		}

		List<StandardItemCategory> findAll = standardItemCategoryDao.findAll();
		int size = 0;
		if (findAll != null && findAll.size() > 0) {
			size = findAll.size();
		}
		StandardItemCategory pojo = new StandardItemCategory();
		pojo.setTaskItemCategoryName(taskItemCategoryName);

		String sort = String.valueOf(size + 1);
		pojo.setSort(sort);

		pojo.setCreateTime(new Date());
		pojo.setCreateUserId(user.getId());
		pojo.setStates(StandardItemCategory.STATE_FLAG_ADD);
		standardItemCategoryDao.save(pojo);
		return ResultUtil.success("Success");
	}

}
