package com.surekam.modules.agro.product.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.Client;
import com.surekam.modules.agro.product.dao.ProductLibraryDetailDao;
import com.surekam.modules.agro.product.dao.ProductLibraryTreeDao;
import com.surekam.modules.agro.product.entity.ProductLibraryDetail;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.productionmodel.dao.ProductionModelDao;
import com.surekam.modules.agro.productionmodel.entity.ProductionModel;
import com.surekam.modules.agro.productlibraryrelation.dao.ProductLibraryRelationDao;
import com.surekam.modules.agro.productlibraryrelation.entity.ProductLibraryRelation;
import com.surekam.modules.api.dto.req.ProductionModelReq;
import com.surekam.modules.api.dto.resp.ProductionModelResp;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;

/**
 * 品种库Service
 * 
 * @author lb
 * @version 2019-04-10
 */
@Component
@Transactional(readOnly = true)
public class ProductLibraryDetailService extends BaseService {
	public static String sy_img_url = Global.getConfig("sy_img_url");
	public static String jg_api_url = Global.getConfig("jg_api_url");

	@Autowired
	private ProductLibraryDetailDao productLibraryDetailDao;

	@Autowired
	private ProductLibraryRelationDao productLibraryRelationDao;

	@Autowired
	private ProductLibraryTreeDao libraryTreeDao;

	@Autowired
	private ProductionModelDao productionModelDao;

	@Autowired
	private OfficeDao officeDao;

	public ProductLibraryDetail get(String id) {
		return productLibraryDetailDao.get(id);
	}

	public Page<ProductLibraryDetail> find(Page<ProductLibraryDetail> page, ProductLibraryDetail productlibrary) {
		DetachedCriteria dc = productLibraryDetailDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ProductLibraryDetail.FIELD_DEL_FLAG, ProductLibraryDetail.DEL_FLAG_NORMAL));
		return productLibraryDetailDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public boolean save(ProductLibraryDetail productlibrary, User user) {
		// 创建一个同名的Library,这个lib唯一对应一个detail
		ProductLibraryTree libraryTree = new ProductLibraryTree();
		if (StringUtils.isBlank(productlibrary.getProductLibraryId())) {
			ProductLibraryTree parent = libraryTreeDao.get(productlibrary.getParentId());
			libraryTree.setIsProductCategory("2");
			libraryTree.setParent(parent);
			libraryTree.setParentsIds(parent.getParentsIds() + parent.getId() + ",");
			List<ProductLibraryTree> findByParentIdAndProductcategoryName = libraryTreeDao.findByParentIdAndProductcategoryName(parent.getId(), productlibrary.getProductName());
			if(!findByParentIdAndProductcategoryName.isEmpty()) {
				return false;
			}
		} else {
			libraryTree = libraryTreeDao.get(productlibrary.getProductLibraryId());
			if (!libraryTree.getParent().getId().equals(productlibrary.getParentId())) {
				ProductLibraryTree parent = libraryTreeDao.get(productlibrary.getParentId());
				libraryTree.setParent(parent);
				libraryTree.setParentsIds(parent.getParentsIds() + parent.getId() + ",");
				List<ProductLibraryTree> findByParentIdAndProductcategoryName = libraryTreeDao.findByParentIdAndProductcategoryName(parent.getId(), productlibrary.getProductName());
				if (!findByParentIdAndProductcategoryName.isEmpty()) {
					return false;
				}
			}
		}
		libraryTree.setProductCategoryImgUrl(productlibrary.getProductImagUrl());
		libraryTree.setProductCategoryName(productlibrary.getProductName());
		
		libraryTreeDao.save(libraryTree);
		productlibrary.setProductLibraryId(libraryTree.getId());
		productLibraryDetailDao.save(productlibrary);

		List<ProductionModelReq> pmReq = productlibrary.getPmReq();
		if (!pmReq.isEmpty()) {
			List<ProductionModel> findByProductIdList = productionModelDao.findByProductIdList(libraryTree.getId());
			for (ProductionModelReq productionModelReq : pmReq) {
				if (StringUtils.isBlank(productionModelReq.getId())) {
					ProductionModel entity = new ProductionModel();
					BeanUtils.copyProperties(productionModelReq, entity);
					entity.setProductId(libraryTree.getId());
					entity.setCreateTime(new Date());
					entity.setCreateUserId(user.getId());
					entity.setStates(ProductionModel.STATE_FLAG_ADD);
					productionModelDao.save(entity);
				} else {
					for (int i = 0; i < findByProductIdList.size(); i++) {
						ProductionModel productionModel = findByProductIdList.get(i);
						for (ProductionModelReq pojoReq : pmReq) {
							ProductionModel entity = new ProductionModel();
							BeanUtils.copyProperties(pojoReq, entity);
							
							if (productionModel.getId().equals(entity.getId())) {
								entity.setProductId(libraryTree.getId());
								entity.setCreateTime(new Date());
								entity.setCreateUserId(user.getId());
								entity.setStates(ProductionModel.STATE_FLAG_UPDATE);
								productionModelDao.flush();
								productionModelDao.clear();
								productionModelDao.save(entity);
								findByProductIdList.remove(i);
							}
						}
					}
				}
			}
			for (ProductionModel productionModel : findByProductIdList) {
				productionModel.setStates(ProductionModel.STATE_FLAG_DEL);
				productionModelDao.save(productionModel);
			}
		} else {
			List<ProductionModel> findByProductIdList = productionModelDao.findByProductIdList(libraryTree.getId());
			for (ProductionModel pojo : findByProductIdList) {
				productionModelDao.flush();
				productionModelDao.clear();
				pojo.setStates(ProductionModel.STATE_FLAG_DEL);
				productionModelDao.save(pojo);
			}
			ProductionModel entity = new ProductionModel();
			entity.setProductId(libraryTree.getId());
			entity.setCreateTime(new Date());
			entity.setCreateUserId(user.getId());
			entity.setStates(ProductionModel.STATE_FLAG_ADD);
			entity.setProductionName("默认生产模式");
			entity.setProductionCode("001");
			entity.setProductionModelInfo("默认生产模式");
			productionModelDao.save(entity);
		}
		Map<String, String> params = new HashMap<String, String>();
		String kIdStr = "";
		List<Map<String, String>> libraryList = productlibrary.getFieldTags();
		for (Map<String, String> map : libraryList) {
			Office office = officeDao.get(map.get("id").toString());
			if (StringUtils.isNotBlank(office.getKuid())) {
				kIdStr += office.getKuid() + ",";
			}
		}
		params = object2Map(productlibrary);
		params.put("kIdStr", kIdStr);
		params.put("productImagUrl", productlibrary.getProductImagUrl() == null ? "" : sy_img_url + productlibrary.getProductImagUrl());
		String postRequest = Client.fromHttp(jg_api_url + "api/productLibraryDetail/saveByNs", params);
		System.out.println("params:" + params);
		System.out.println("postRequest:" + postRequest);
		JSONObject obj = new JSONObject(postRequest);
		if (obj == null || !"0".equals(obj.get("code").toString())) {
			System.out.println("接口错误:" + postRequest);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		// 新增品种与公司关系数据
		productLibraryRelationDao.delete(libraryTree.getId());
		List<Map<String, String>> fieldTags = productlibrary.getFieldTags();
		for (Map<String, String> map : fieldTags) {
			Office office = officeDao.get(map.get("id").toString());
			ProductLibraryRelation pojo = new ProductLibraryRelation();
			pojo.setOfficeId(office.getId());
			pojo.setOfficeIds(office.getParentIds());
			pojo.setParentId(libraryTree.getId());
			pojo.setParentsIds(libraryTree.getParentsIds());
			productLibraryRelationDao.flush();
			productLibraryRelationDao.clear();
			productLibraryRelationDao.save(pojo);
		}
		return true;
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		productLibraryDetailDao.deleteById(id);
	}

	public ProductLibraryDetail getLibraryId(String libraryId) {
		List<ProductionModelResp> pmResp = new ArrayList<ProductionModelResp>();

		String qlString = "from ProductLibraryDetail where productLibraryId=:p1";
		ProductLibraryDetail byHql = productLibraryDetailDao.getByHql(qlString, new Parameter(libraryId));

		List<ProductionModel> findByProductIdList = productionModelDao.findByProductIdList(byHql.getProductLibraryId());
		for (ProductionModel productionModel : findByProductIdList) {
			ProductionModelResp resp = new ProductionModelResp();
			BeanUtils.copyProperties(productionModel, resp);
			pmResp.add(resp);
		}
		byHql.setPmResp(pmResp);
		ProductLibraryTree productLibraryTree = libraryTreeDao.get(byHql.getProductLibraryId());
		byHql.setParentId(productLibraryTree.getParent().getId());
		byHql.setParentName(productLibraryTree.getParent().getProductCategoryName());
		List<Map<String, String>> fieldTags = productLibraryRelationDao.findByParentId(libraryId);
		byHql.setFieldTags(fieldTags);
		return byHql;
	}

	@Transactional(readOnly = false)
	public void defaultProductionMode(User user) {
		List<Object> list = productionModelDao.findProductIdList();
		for (Object obj : list) {
			ProductionModel entity = new ProductionModel();
			entity.setProductId(obj.toString());
			entity.setCreateTime(new Date());
			entity.setCreateUserId(user.getId());
			entity.setStates(ProductionModel.STATE_FLAG_ADD);
			entity.setProductionName("默认生产模式");
			entity.setProductionCode("001");
			entity.setProductionModelInfo("默认生产模式");
			productionModelDao.save(entity);
		}
	}

	/**
	 * 实体对象转成Map
	 *
	 * @param obj
	 *            实体对象
	 * @return
	 */
	public static Map<String, String> object2Map(Object obj) {
		Map<String, String> map = new HashMap<String, String>();
		if (obj == null) {
			return map;
		}
		Class clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.get(obj) != null) {
					map.put(field.getName(), field.get(obj).toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
