package com.surekam.modules.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.modules.agro.materials.dao.MaterialAnimalHealthProductsDao;
import com.surekam.modules.agro.materials.dao.MaterialFeedDao;
import com.surekam.modules.agro.materials.dao.MaterialFertilizerDao;
import com.surekam.modules.agro.materials.dao.MaterialPesticideDao;
import com.surekam.modules.agro.materials.dao.MaterialRelationDao;
import com.surekam.modules.agro.materials.entity.MaterialAnimalHealthProducts;
import com.surekam.modules.agro.materials.entity.MaterialFeed;
import com.surekam.modules.agro.materials.entity.MaterialFertilizer;
import com.surekam.modules.agro.materials.entity.MaterialPesticide;
import com.surekam.modules.agro.materials.entity.MaterialRelation;
import com.surekam.modules.agro.product.dao.ProductLibraryTreeDao;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.api.dto.req.MaterialAnimalHealthProductsReq;
import com.surekam.modules.api.dto.req.MaterialFeedReq;
import com.surekam.modules.api.dto.req.MaterialFertilizerReq;
import com.surekam.modules.api.dto.req.MaterialPesticideReq;
import com.surekam.modules.api.dto.req.ProductLibraryTreeListReq;
import com.surekam.modules.api.dto.resp.MaterialAnimalHealthProductsResp;
import com.surekam.modules.api.dto.resp.MaterialFeedResp;
import com.surekam.modules.api.dto.resp.MaterialFertilizerResp;
import com.surekam.modules.api.dto.resp.MaterialPesticideResp;
import com.surekam.modules.api.dto.resp.ProductLibraryTreeListResp;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;

/**
 * 农事农资Service
 * 
 * @author dell
 *
 */
@Component
public class MaterialsApiService {

	/**
	 * 动保产品
	 */
	@Autowired
	private MaterialAnimalHealthProductsDao materialAnimalHealthProductsDao;

	/**
	 * 农药管理
	 */
	@Autowired
	private MaterialPesticideDao materialPesticideDao;
	
	/**
	 * 饲料表
	 */
	@Autowired
	private MaterialFeedDao materialFeedDao;
	
	/**
	 * 化肥表
	 */
	@Autowired
	private MaterialFertilizerDao materialFertilizerDao;

	/**
	 * 农资与农产品类型关系表
	 */
	@Autowired
	private MaterialRelationDao materialRelationDao;
	
	/**
	 * 种类表
	 */
	@Autowired
	private ProductLibraryTreeDao productLibraryTreeDao;
	
	
	@Autowired
	private OfficeDao officeDao;
	
	/**
	 * 查询动保列表
	 * 
	 * @param user
	 *            用户信息
	 * @param pageNo
	 *            页数
	 * @param pageSize
	 *            条数
	 * @return
	 */
	public Page<MaterialAnimalHealthProducts> getMaterialAnimalHealthProductsList(User user, Integer pageNo, Integer pageSize) {
		int no = pageNo == null ? Global.DEFAULT_PAGENO : pageNo;
		int size = pageSize == null ? Global.DEFAULT_PAGESIZE : pageSize;
		Page<MaterialAnimalHealthProducts> page = new Page<MaterialAnimalHealthProducts>(no, size);
		page = getMaterialAnimalHealthProductsPage(page, user);
		return page;
	}

	public Page<MaterialAnimalHealthProducts> getMaterialAnimalHealthProductsPage(Page<MaterialAnimalHealthProducts> page, User user) {
		DetachedCriteria dc = materialAnimalHealthProductsDao.createDetachedCriteria();
		if (user != null && !user.isAdmin()) {
			dc.add(Restrictions.eq("officeId", user.getOffice().getId()));
		}
		dc.add(Restrictions.ne(MaterialAnimalHealthProducts.FIELD_DEL_FLAG_XGXT, MaterialAnimalHealthProducts.STATE_FLAG_DEL));
		dc.addOrder(Order.desc("createTime"));
		Page<MaterialAnimalHealthProducts> find = materialAnimalHealthProductsDao.find(page, dc);
		List<MaterialAnimalHealthProducts> list = find.getList();
		for (MaterialAnimalHealthProducts mahp : list) {
			if (StringUtils.isNotBlank(mahp.getOfficeId())) {
				// 获取公司的中文名称
				Office office = officeDao.get(mahp.getOfficeId());
				if(office != null) {
					mahp.setOfficeName(office.getName());
				}else {
					mahp.setOfficeName("");
				}
				// 查询农事与农业产品关系表
				List<MaterialRelation> mrList = materialRelationDao.getMaterialRelationList(mahp.getId());
				if (list != null && list.size() > 0) {
					List<ProductLibraryTreeListResp> pltResp = new ArrayList<ProductLibraryTreeListResp>();
					for (MaterialRelation materialRelation : mrList) {
						ProductLibraryTreeListResp pltlResp = new ProductLibraryTreeListResp();
						// 查询种类信息
						ProductLibraryTree productLibraryTree = productLibraryTreeDao.get(materialRelation.getProductLibraryId());
						pltlResp.setId(productLibraryTree.getId());
						pltlResp.setName(productLibraryTree.getProductCategoryName());
						pltResp.add(pltlResp);
					}
					mahp.setPltResp(pltResp);
				}
			}
		}
		return find;
	}

	/**
	 * 删除动保产品
	 * 
	 * @param user
	 *            用户信息
	 * @param id
	 *            主键
	 * @return
	 */
	@Transactional(readOnly = false)
	public String deleteMaterialAnimalHealthProducts(User user, String id) {
		MaterialAnimalHealthProducts mapPojo = materialAnimalHealthProductsDao.get(id);
		mapPojo.setUpdateTime(new Date());
		mapPojo.setUpdateUserId(user.getId());
		mapPojo.setStates(MaterialAnimalHealthProducts.STATE_FLAG_DEL);
		materialAnimalHealthProductsDao.save(mapPojo);
		return "Success";
	}

	/**
	 * 保存或者修改动保产品
	 * 
	 * @param user
	 *            用戶信息
	 * @param req
	 *            请求对象
	 * @return
	 */
	@Transactional(readOnly = false)
	public String savaMaterialAnimalHealthProducts(User user, MaterialAnimalHealthProductsReq req) {
		MaterialAnimalHealthProducts mahpPojo = new MaterialAnimalHealthProducts();
		// 复制页面属性信息到基地实体对象内
		BeanUtils.copyProperties(req, mahpPojo);
		// 农资类型
		String sysDict = "db";
		// 判断新怎还是修改不
		if (StringUtils.isNotBlank(req.getId())) {
			MaterialAnimalHealthProducts pojo = materialAnimalHealthProductsDao.get(req.getId());
			mahpPojo.setCreateUserId(pojo.getCreateUserId());
			mahpPojo.setUpdateTime(new Date());
			mahpPojo.setUpdateUserId(user.getId());
			mahpPojo.setStates(MaterialAnimalHealthProducts.STATE_FLAG_UPDATE);
			materialAnimalHealthProductsDao.clear();
			materialAnimalHealthProductsDao.flush();
			materialAnimalHealthProductsDao.save(mahpPojo);
			
			materialRelationDao.delete(mahpPojo.getId());
			if(!req.getPltReq().isEmpty()) {
				for (ProductLibraryTreeListReq mrReq : req.getPltReq()) {
					MaterialRelation raterialRelation = new MaterialRelation();
					raterialRelation.setMaterialId(mahpPojo.getId());
					raterialRelation.setProductLibraryId(mrReq.getId());
					raterialRelation.setSysDict(sysDict);
					materialRelationDao.save(raterialRelation);
				}
			}
		}
		if (StringUtils.isBlank(req.getId())) {
			if (StringUtils.isBlank(req.getOfficeId())) {
				mahpPojo.setOfficeId(user.getOffice().getId());
			}
			mahpPojo.setCreateTime(new Date());
			mahpPojo.setCreateUserId(user.getId());
			mahpPojo.setStates(MaterialAnimalHealthProducts.STATE_FLAG_ADD);
			materialAnimalHealthProductsDao.save(mahpPojo);
			if (!req.getPltReq().isEmpty()) {
				for (ProductLibraryTreeListReq mrReq : req.getPltReq()) {
					MaterialRelation raterialRelation = new MaterialRelation();
					raterialRelation.setMaterialId(mahpPojo.getId());
					raterialRelation.setProductLibraryId(mrReq.getId());
					raterialRelation.setSysDict(sysDict);
					materialRelationDao.save(raterialRelation);
				}
			}
		}
		return "Success";
	}
	
	/**
	 * 修改查看对象信息
	 * 
	 * @param id
	 *            主键
	 * @return
	 */
	public MaterialAnimalHealthProductsResp getMaterialAnimalHealthProducts(String id) {
		MaterialAnimalHealthProductsResp resp = new MaterialAnimalHealthProductsResp();
		// 查询动保产品对象
		MaterialAnimalHealthProducts pojo = materialAnimalHealthProductsDao.get(id);
		// 复制动保产品表参数
		BeanUtils.copyProperties(pojo, resp);
		// 查询农事与农业产品关系表
		List<MaterialRelation> list = materialRelationDao.getMaterialRelationList(pojo.getId());
		if (list != null && list.size() > 0) {
			List<ProductLibraryTreeListResp> pltResp = new ArrayList<ProductLibraryTreeListResp>();
			for (MaterialRelation materialRelation : list) {
				ProductLibraryTreeListResp pltlResp = new ProductLibraryTreeListResp();
				// 查询种类信息
				ProductLibraryTree productLibraryTree = productLibraryTreeDao.get(materialRelation.getProductLibraryId());
				pltlResp.setId(productLibraryTree.getId());
				pltlResp.setName(productLibraryTree.getProductCategoryName());
				pltResp.add(pltlResp);
			}
			resp.setPltResp(pltResp);
		}
		return resp;
	}
	
	/**
	 * 查询农药管理列表
	 * 
	 * @param user
	 *            用户
	 * @param pageNo
	 *            页数
	 * @param pageSize
	 *            条数
	 * @return
	 */
	public Page<MaterialPesticide> getMaterialPesticideList(User user, Integer pageNo, Integer pageSize) {
		int no = pageNo == null ? Global.DEFAULT_PAGENO : pageNo;
		int size = pageSize == null ? Global.DEFAULT_PAGESIZE : pageSize;
		Page<MaterialPesticide> page = new Page<MaterialPesticide>(no, size);
		page = getMaterialPesticidePage(page, user);
		return page;
	}
	
	public Page<MaterialPesticide> getMaterialPesticidePage(Page<MaterialPesticide> page, User user) {
		DetachedCriteria dc = materialPesticideDao.createDetachedCriteria();
		if (user != null && !user.isAdmin()) {
			dc.add(Restrictions.eq("officeId", user.getOffice().getId()));
		}
		dc.add(Restrictions.ne(MaterialPesticide.FIELD_DEL_FLAG_XGXT, MaterialPesticide.STATE_FLAG_DEL));
		dc.addOrder(Order.desc("createTime"));
		Page<MaterialPesticide> find = materialPesticideDao.find(page, dc);
		List<MaterialPesticide> list = find.getList();
		for (MaterialPesticide pojo : list) {
			// 获取公司的中文名称
			Office office = officeDao.get(pojo.getOfficeId());
			if(office != null) {
				pojo.setOfficeName(office.getName());
			}else {
				pojo.setOfficeName("");
			}
			// 查询农事与农业产品关系表
			List<MaterialRelation> mrList = materialRelationDao.getMaterialRelationList(pojo.getId());
			if (list != null && list.size() > 0) {
				List<ProductLibraryTreeListResp> pltResp = new ArrayList<ProductLibraryTreeListResp>();
				for (MaterialRelation materialRelation : mrList) {
					ProductLibraryTreeListResp pltlResp = new ProductLibraryTreeListResp();
					// 查询种类信息
					ProductLibraryTree productLibraryTree = productLibraryTreeDao.get(materialRelation.getProductLibraryId());
					pltlResp.setId(productLibraryTree.getId());
					pltlResp.setName(productLibraryTree.getProductCategoryName());
					pltResp.add(pltlResp);
				}
				pojo.setPltResp(pltResp);
			}
		}
		return find;
	}

	/**
	 * 刪除农药
	 * 
	 * @param user
	 *            用户信息
	 * @param id
	 *            主键
	 * @return
	 */
	@Transactional(readOnly = false)
	public String deleteMaterialPesticide(User user, String id) {
		MaterialPesticide ampPojo = materialPesticideDao.get(id);
		ampPojo.setUpdateTime(new Date());
		ampPojo.setUpdateUserId(user.getUnionId());
		ampPojo.setStates(MaterialPesticide.STATE_FLAG_DEL);
		materialPesticideDao.save(ampPojo);
		return "Success";
	}
	
	/**
	 * 保存或者修改农药
	 * 
	 * @param user
	 *            用戶信息
	 * @param req
	 *            请求对象
	 * @return
	 */
	@Transactional(readOnly = false)
	public String savaMaterialPesticide(User user, MaterialPesticideReq req) {
		MaterialPesticide mpPojo = new MaterialPesticide();
		// 复制页面属性信息到基地实体对象内
		BeanUtils.copyProperties(req, mpPojo);
		// 农资类型
		String sysDict = "ny";
		// 判断新怎还是修改不
		if (StringUtils.isNotBlank(req.getId())) {
			MaterialPesticide pojo = materialPesticideDao.get(req.getId());
			
			mpPojo.setCreateUserId(pojo.getCreateUserId());
			mpPojo.setUpdateTime(new Date());
			mpPojo.setUpdateUserId(user.getId());
			mpPojo.setStates(MaterialAnimalHealthProducts.STATE_FLAG_UPDATE);
			materialPesticideDao.clear();
			materialPesticideDao.flush();
			materialPesticideDao.save(mpPojo);
			
			materialRelationDao.delete(mpPojo.getId());
			if(!req.getPltReq().isEmpty()) {
				for (ProductLibraryTreeListReq mrReq : req.getPltReq()) {
					MaterialRelation raterialRelation = new MaterialRelation();
					raterialRelation.setMaterialId(mpPojo.getId());
					raterialRelation.setProductLibraryId(mrReq.getId());
					raterialRelation.setSysDict(sysDict);
					materialRelationDao.save(raterialRelation);
				}
			}
		}
		
		if (StringUtils.isBlank(req.getId())) {
			if (StringUtils.isBlank(req.getOfficeId())) {
				mpPojo.setOfficeId(user.getOffice().getId());
			}
			mpPojo.setCreateTime(new Date());
			mpPojo.setCreateUserId(user.getId());
			mpPojo.setStates(MaterialAnimalHealthProducts.STATE_FLAG_ADD);
			materialPesticideDao.save(mpPojo);

			if (!req.getPltReq().isEmpty()) {
				for (ProductLibraryTreeListReq mrReq : req.getPltReq()) {
					MaterialRelation raterialRelation = new MaterialRelation();
					raterialRelation.setMaterialId(mpPojo.getId());
					raterialRelation.setProductLibraryId(mrReq.getId());
					raterialRelation.setSysDict(sysDict);
					materialRelationDao.save(raterialRelation);
				}
			}
		}
		return "Success";
	}
	
	/**
	 * 修改查看对象信息
	 * 
	 * @param id
	 *            主键
	 * @return
	 */
	public MaterialPesticideResp getMaterialPesticide(String id) {
		MaterialPesticideResp resp = new MaterialPesticideResp();
		// 查询农药对象
		MaterialPesticide pojo = materialPesticideDao.get(id);
		// 复制农药表参数
		BeanUtils.copyProperties(pojo, resp);
		// 查询农事与农业产品关系表
		List<MaterialRelation> list = materialRelationDao.getMaterialRelationList(pojo.getId());
		if (list != null && list.size() > 0) {
			List<ProductLibraryTreeListResp> pltResp = new ArrayList<ProductLibraryTreeListResp>();
			for (MaterialRelation materialRelation : list) {
				ProductLibraryTreeListResp pltlResp = new ProductLibraryTreeListResp();
				// 查询种类信息
				ProductLibraryTree productLibraryTree = productLibraryTreeDao.get(materialRelation.getProductLibraryId());
				pltlResp.setId(productLibraryTree.getId());
				pltlResp.setName(productLibraryTree.getProductCategoryName());
				pltResp.add(pltlResp);
			}
			resp.setPltResp(pltResp);
		}
		return resp;
	}
	
	/**
	 * 查询饲料列表
	 * 
	 * @param user
	 *            用户信息
	 * @param pageNo
	 *            页数
	 * @param pageSize
	 *            条数
	 * @return
	 */
	public Page<MaterialFeed> getMaterialFeedList(User user, Integer pageNo, Integer pageSize) {
		int no = pageNo == null ? Global.DEFAULT_PAGENO : pageNo;
		int size = pageSize == null ? Global.DEFAULT_PAGESIZE : pageSize;
		Page<MaterialFeed> page = new Page<MaterialFeed>(no, size);
		page = getMaterialFeedPage(page, user);
		return page;
	}

	public Page<MaterialFeed> getMaterialFeedPage(Page<MaterialFeed> page, User user) {
		DetachedCriteria dc = materialFeedDao.createDetachedCriteria();
		if (user != null && !user.isAdmin()) {
			dc.add(Restrictions.eq("officeId", user.getOffice().getId()));
		}
		dc.add(Restrictions.ne(MaterialFeed.FIELD_DEL_FLAG_XGXT, MaterialFeed.STATE_FLAG_DEL));
		dc.addOrder(Order.desc("createTime"));
		Page<MaterialFeed> find = materialFeedDao.find(page, dc);
		List<MaterialFeed> list = find.getList();
		for (MaterialFeed pojo : list) {
			// 获取公司的中文名称
			Office office = officeDao.get(pojo.getOfficeId());
			if(office != null) {
				pojo.setOfficeName(office.getName());
			}else {
				pojo.setOfficeName("");
			}
			// 查询农事与农业产品关系表
			List<MaterialRelation> mrList = materialRelationDao.getMaterialRelationList(pojo.getId());
			if (list != null && list.size() > 0) {
				List<ProductLibraryTreeListResp> pltResp = new ArrayList<ProductLibraryTreeListResp>();
				for (MaterialRelation materialRelation : mrList) {
					ProductLibraryTreeListResp pltlResp = new ProductLibraryTreeListResp();
					// 查询种类信息
					ProductLibraryTree productLibraryTree = productLibraryTreeDao.get(materialRelation.getProductLibraryId());
					pltlResp.setId(productLibraryTree.getId());
					pltlResp.setName(productLibraryTree.getProductCategoryName());
					pltResp.add(pltlResp);
				}
				pojo.setPltResp(pltResp);
			}
		}
		return find;
	}

	/**
	 * 刪除饲料
	 * 
	 * @param user
	 *            用户信息
	 * @param id
	 *            主键
	 * @return
	 */
	@Transactional(readOnly = false)
	public String deleteMaterialFeed(User user, String id) {
		MaterialFeed materialFeed = materialFeedDao.get(id);
		materialFeed.setUpdateTime(new Date());
		materialFeed.setUpdateUserId(user.getUnionId());
		materialFeed.setStates(MaterialPesticide.STATE_FLAG_DEL);
		materialFeedDao.save(materialFeed);
		return "Success";
	}
	
	/**
	 * 保存或者修改饲料
	 * 
	 * @param user
	 *            用戶信息
	 * @param req
	 *            请求对象
	 * @return
	 */
	@Transactional(readOnly = false)
	public String savaMaterialFeed(User user, MaterialFeedReq req) {
		MaterialFeed mfPojo = new MaterialFeed();
		// 复制页面属性信息到基地实体对象内
		BeanUtils.copyProperties(req, mfPojo);
		// 农资类型
		String sysDict = "sl";
		// 判断新怎还是修改不
		if (StringUtils.isNotBlank(req.getId())) {
			MaterialFeed pojo = materialFeedDao.get(req.getId());

			mfPojo.setCreateUserId(pojo.getCreateUserId());
			mfPojo.setUpdateTime(new Date());
			mfPojo.setUpdateUserId(user.getId());
			mfPojo.setStates(MaterialAnimalHealthProducts.STATE_FLAG_UPDATE);
			materialFeedDao.clear();
			materialFeedDao.flush();
			materialFeedDao.save(mfPojo);

			materialRelationDao.delete(mfPojo.getId());
			if (!req.getPltReq().isEmpty()) {
				for (ProductLibraryTreeListReq mrReq : req.getPltReq()) {
					MaterialRelation raterialRelation = new MaterialRelation();
					raterialRelation.setMaterialId(mfPojo.getId());
					raterialRelation.setProductLibraryId(mrReq.getId());
					raterialRelation.setSysDict(sysDict);
					materialRelationDao.save(raterialRelation);
				}
			}
		}
		if (StringUtils.isBlank(req.getId())) {
			if (StringUtils.isBlank(req.getOfficeId())) {
				mfPojo.setOfficeId(user.getOffice().getId());
			}
			mfPojo.setCreateTime(new Date());
			mfPojo.setCreateUserId(user.getId());
			mfPojo.setStates(MaterialAnimalHealthProducts.STATE_FLAG_ADD);
			materialFeedDao.save(mfPojo);

			if (!req.getPltReq().isEmpty()) {
				for (ProductLibraryTreeListReq mrReq : req.getPltReq()) {
					MaterialRelation raterialRelation = new MaterialRelation();
					raterialRelation.setMaterialId(mfPojo.getId());
					raterialRelation.setProductLibraryId(mrReq.getId());
					raterialRelation.setSysDict(sysDict);
					materialRelationDao.save(raterialRelation);
				}
			}
		}
		return "Success";
	}
	
	/**
	 * 修改查看对象信息
	 * 
	 * @param id
	 *            主键
	 * @return
	 */
	public MaterialFeedResp getMaterialFeed(String id) {
		MaterialFeedResp resp = new MaterialFeedResp();
		// 查询饲料对象
		MaterialFeed pojo = materialFeedDao.get(id);
		// 复制饲料表参数
		BeanUtils.copyProperties(pojo, resp);
		// 查询农事与农业产品关系表
		List<MaterialRelation> list = materialRelationDao.getMaterialRelationList(pojo.getId());
		if (list != null && list.size() > 0) {
			List<ProductLibraryTreeListResp> pltResp = new ArrayList<ProductLibraryTreeListResp>();
			for (MaterialRelation materialRelation : list) {
				ProductLibraryTreeListResp pltlResp = new ProductLibraryTreeListResp();
				// 查询种类信息
				ProductLibraryTree productLibraryTree = productLibraryTreeDao.get(materialRelation.getProductLibraryId());
				pltlResp.setId(productLibraryTree.getId());
				pltlResp.setName(productLibraryTree.getProductCategoryName());
				pltResp.add(pltlResp);
			}
			resp.setPltResp(pltResp);
		}
		return resp;
	}
	
	/**
	 * 查询化肥列表
	 * 
	 * @param user
	 *            用户信息
	 * @param pageNo
	 *            页数
	 * @param pageSize
	 *            条数
	 * @return
	 */
	public Page<MaterialFertilizer> getMaterialFertilizerList(User user, Integer pageNo, Integer pageSize) {
		int no = pageNo == null ? Global.DEFAULT_PAGENO : pageNo;
		int size = pageSize == null ? Global.DEFAULT_PAGESIZE : pageSize;
		Page<MaterialFertilizer> page = new Page<MaterialFertilizer>(no, size);
		page = getMaterialFertilizerPage(page, user);
		return page;
	}

	public Page<MaterialFertilizer> getMaterialFertilizerPage(Page<MaterialFertilizer> page, User user) {
		DetachedCriteria dc = materialFertilizerDao.createDetachedCriteria();
		if (user != null && !user.isAdmin()) {
			dc.add(Restrictions.eq("officeId", user.getOffice().getId()));
		}
		dc.add(Restrictions.ne(MaterialFertilizer.FIELD_DEL_FLAG_XGXT, MaterialFertilizer.STATE_FLAG_DEL));
		dc.addOrder(Order.desc("createTime"));
		Page<MaterialFertilizer> find = materialFertilizerDao.find(page, dc);
		List<MaterialFertilizer> list = find.getList();
		for (MaterialFertilizer pojo : list) {
			// 获取公司的中文名称
			Office office = officeDao.get(pojo.getOfficeId());
			if(office != null) {
				pojo.setOfficeName(office.getName());
			}else {
				pojo.setOfficeName("");
			}
			// 查询农事与农业产品关系表
			List<MaterialRelation> mrList = materialRelationDao.getMaterialRelationList(pojo.getId());
			if (list != null && list.size() > 0) {
				List<ProductLibraryTreeListResp> pltResp = new ArrayList<ProductLibraryTreeListResp>();
				for (MaterialRelation materialRelation : mrList) {
					ProductLibraryTreeListResp pltlResp = new ProductLibraryTreeListResp();
					// 查询种类信息
					ProductLibraryTree productLibraryTree = productLibraryTreeDao.get(materialRelation.getProductLibraryId());
					pltlResp.setId(productLibraryTree.getId());
					pltlResp.setName(productLibraryTree.getProductCategoryName());
					pltResp.add(pltlResp);
				}
				pojo.setPltResp(pltResp);
			}
		}
		return find;
	}

	/**
	 * 刪除化肥
	 * 
	 * @param user
	 *            用户信息
	 * @param id
	 *            主键
	 * @return
	 */
	@Transactional(readOnly = false)
	public String deleteMaterialFertilizer(User user, String id) {
		MaterialFertilizer materialFertilizer = materialFertilizerDao.get(id);
		materialFertilizer.setUpdateTime(new Date());
		materialFertilizer.setUpdateUserId(user.getUnionId());
		materialFertilizer.setStates(MaterialPesticide.STATE_FLAG_DEL);
		materialFertilizerDao.save(materialFertilizer);
		return "Success";
	}
	
	/**
	 * 保存或者修改饲料
	 * 
	 * @param user
	 *            用戶信息
	 * @param req
	 *            请求对象
	 * @return
	 */
	@Transactional(readOnly = false)
	public String savaMaterialFertilizer(User user, MaterialFertilizerReq req) {
		MaterialFertilizer mfPojo = new MaterialFertilizer();
		// 复制页面属性信息到基地实体对象内
		BeanUtils.copyProperties(req, mfPojo);
		// 农资类型
		String sysDict = "hf";
		// 判断新怎还是修改不
		if (StringUtils.isNotBlank(req.getId())) {
			MaterialFertilizer pojo = materialFertilizerDao.get(req.getId());

			mfPojo.setCreateUserId(pojo.getCreateUserId());
			mfPojo.setUpdateTime(new Date());
			mfPojo.setUpdateUserId(user.getId());
			mfPojo.setStates(MaterialAnimalHealthProducts.STATE_FLAG_UPDATE);
			materialFertilizerDao.clear();
			materialFertilizerDao.flush();
			materialFertilizerDao.save(mfPojo);

			materialRelationDao.delete(mfPojo.getId());
			if (!req.getPltReq().isEmpty()) {
				for (ProductLibraryTreeListReq mrReq : req.getPltReq()) {
					MaterialRelation raterialRelation = new MaterialRelation();
					raterialRelation.setMaterialId(mfPojo.getId());
					raterialRelation.setProductLibraryId(mrReq.getId());
					raterialRelation.setSysDict(sysDict);
					materialRelationDao.save(raterialRelation);
				}
			}
		}
		if (StringUtils.isBlank(req.getId())) {
			if (StringUtils.isBlank(req.getOfficeId())) {
				mfPojo.setOfficeId(user.getOffice().getId());
			}
			mfPojo.setCreateTime(new Date());
			mfPojo.setCreateUserId(user.getId());
			mfPojo.setStates(MaterialAnimalHealthProducts.STATE_FLAG_ADD);
			materialFertilizerDao.save(mfPojo);

			if (!req.getPltReq().isEmpty()) {
				for (ProductLibraryTreeListReq mrReq : req.getPltReq()) {
					MaterialRelation raterialRelation = new MaterialRelation();
					raterialRelation.setMaterialId(mfPojo.getId());
					raterialRelation.setProductLibraryId(mrReq.getId());
					raterialRelation.setSysDict(sysDict);
					materialRelationDao.save(raterialRelation);
				}
			}
		}
		return "Success";
	}
	
	
	/**
	 * 修改查看对象信息
	 * 
	 * @param id
	 *            主键
	 * @return
	 */
	public MaterialFertilizerResp getMaterialFertilizer(String id) {
		MaterialFertilizerResp resp = new MaterialFertilizerResp();
		// 查询化肥对象
		MaterialFertilizer pojo = materialFertilizerDao.get(id);
		// 复制化肥表参数
		BeanUtils.copyProperties(pojo, resp);
		// 查询农事与农业产品关系表
		List<MaterialRelation> list = materialRelationDao.getMaterialRelationList(pojo.getId());
		if (list != null && list.size() > 0) {
			List<ProductLibraryTreeListResp> pltResp = new ArrayList<ProductLibraryTreeListResp>();
			for (MaterialRelation materialRelation : list) {
				ProductLibraryTreeListResp pltlResp = new ProductLibraryTreeListResp();
				// 查询种类信息
				ProductLibraryTree productLibraryTree = productLibraryTreeDao.get(materialRelation.getProductLibraryId());
				pltlResp.setId(productLibraryTree.getId());
				pltlResp.setName(productLibraryTree.getProductCategoryName());
				pltResp.add(pltlResp);
			}
			resp.setPltResp(pltResp);
		}
		return resp;
	}
	
}
