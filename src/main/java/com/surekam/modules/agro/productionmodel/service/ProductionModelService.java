package com.surekam.modules.agro.productionmodel.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.service.BaseService;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultUtil;
import com.surekam.modules.agro.productionmodel.dao.ProductionModelDao;
import com.surekam.modules.agro.productionmodel.entity.ProductionModel;
import com.surekam.modules.agro.systementerprisestandards.dao.SystemEnterpriseStandardsDao;
import com.surekam.modules.agro.systementerprisestandards.entity.SystemEnterpriseStandards;
import com.surekam.modules.api.dto.req.ProductionModelReq;
import com.surekam.modules.sys.entity.User;

/**
 * 生产模式Service
 * 
 * @author tangjun
 * @version 2019-05-27
 */
@Component
@Transactional(readOnly = true)
public class ProductionModelService extends BaseService {

	@Autowired
	private ProductionModelDao productionModelDao;
	
	@Autowired
	private SystemEnterpriseStandardsDao SystemEnterpriseStandardsDao;

	/**
	 * Title: savaProductionModel Description: 新增或修改生产模式
	 * 
	 * @param req
	 *            新增或修改生产模式请求参数
	 * @param user
	 *            用户信息
	 * @return
	 */
	public ResultBean<String> savaProductionModel(ProductionModelReq req, User user) {
		if (StringUtils.isBlank(req.getId())) {
			ProductionModel entity = new ProductionModel();
			BeanUtils.copyProperties(req, entity);

			entity.setCreateTime(new Date());
			entity.setCreateUserId(user.getId());
			entity.setStates(ProductionModel.STATE_FLAG_ADD);
			productionModelDao.save(entity);
		}

		if (StringUtils.isNotBlank(req.getId())) {
			ProductionModel productionModel = productionModelDao.get(req.getId());
			ProductionModel entity = new ProductionModel();
			BeanUtils.copyProperties(req, entity);

			entity.setCreateUserId(productionModel.getCreateUserId());
			entity.setUpdateTime(new Date());
			entity.setUpdateUserId(user.getId());
			entity.setStates(ProductionModel.STATE_FLAG_ADD);
			productionModelDao.clear();
			productionModelDao.flush();
			productionModelDao.save(entity);
		}
		return ResultUtil.success("Success");
	}

	/**
	 * Title: getProductionModelList Description: 获取品种下的生产模式
	 * 
	 * @param productId
	 *            品种ID
	 * @return
	 */
	public List<ProductionModel> getProductionModelList(String productId) {
		List<ProductionModel> findByProductIdList = productionModelDao.findByProductIdList(productId);
		return findByProductIdList;
	}
	
	/**
	 * Title: delete Description: 删除
	 * 
	 * @param id
	 * @param user
	 * @return
	 */  
	public boolean delete(String id, User user) {
		if (StringUtils.isBlank(id)) {
			return true;
		}
		List<SystemEnterpriseStandards> findByModelId = SystemEnterpriseStandardsDao.findByModelId(id);
		if(findByModelId.isEmpty()) {
			// ProductionModel productionModel = productionModelDao.get(id);
			// productionModel.setStates(ProductionModel.STATE_FLAG_DEL);
			// productionModelDao.save(productionModel);
			return true;
		} else {
			return false;
		}
	}

}
