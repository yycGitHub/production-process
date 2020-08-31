package com.surekam.modules.api.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.basemanage.dao.BaseTreeDao;
import com.surekam.modules.agro.product.dao.ProductLibraryTreeDao;
import com.surekam.modules.agro.product.entity.ProductLibraryDetail;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.product.entity.ProductTreeList;
import com.surekam.modules.agro.product.entity.vo.ProductLibraryTreeVo;
import com.surekam.modules.agro.productlibraryrelation.dao.ProductLibraryRelationDao;
import com.surekam.modules.agro.productlibraryrelation.entity.ProductLibraryRelation;
import com.surekam.modules.api.dto.resp.ProductionBatchResp;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;

/**
 * 获取种养殖种类Service
 * 
 * @author tangjun
 * @version 2019-04-09
 */

@Component
@Transactional(readOnly = true)
public class SpeciesManageApiService {

	@Autowired
	private ProductLibraryRelationDao productLibraryRelationDao;
	
	@Autowired
	private ProductLibraryTreeDao productLibraryTreeDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	@Autowired
	private BaseTreeDao baseTreeDao;

	@Autowired
	private ProductLibraryTreeDao libraryTreeDao;
	
	/**
	 * Title: getofficeProductLibraryTreeList Description: 根据公司查询品种树列表
	 * 
	 * @param baseId
	 *            基地
	 * @param officeId
	 *            公司ID
	 * @return
	 */
	public List<ProductLibraryTreeVo> getOfficeProductLibraryTreeList(String baseId, String officeId) {
		ProductLibraryTree findByIdAndParentId = productLibraryTreeDao.findByIdAndParentId("1", "0");
		if (StringUtils.isNotBlank(baseId)) {
			Office office = officeDao.get(baseTreeDao.get(baseId).getOfficeId());
			List<ProductLibraryRelation> findByLikeOfficeId = productLibraryRelationDao.findByLikeOffice(office.getId(), office.getId());
			String idStr = "";
			for (ProductLibraryRelation productLibraryRelation : findByLikeOfficeId) {
				String[] ids = productLibraryRelation.getParentsIds().split(",");
				idStr += "'" + productLibraryRelation.getParentId() + "'" + ",";
				for (String string : ids) {
					idStr += "'" + string + "'" + ",";
				}
			}
			if (StringUtils.isNotBlank(idStr)) {
				idStr = idStr.substring(0, idStr.length() - 1);
			}
			if (StringUtils.isBlank(idStr)) {
				idStr = "''";
			}
			List<ProductLibraryTree> findProductLibraryTree = productLibraryTreeDao.findByInid(idStr);
			ProductLibraryTreeVo vo = treeList(findProductLibraryTree, findByIdAndParentId);
			List<ProductLibraryTreeVo> voList = new ArrayList<ProductLibraryTreeVo>();
			voList.add(vo);
			return voList;
		} else  if (!"1".equals(officeId) && StringUtils.isNotBlank(officeId)) {
			Office office = officeDao.get(officeId);
			List<ProductLibraryRelation> findByLikeOfficeId = productLibraryRelationDao.findByLikeOffice(office.getId(), office.getId());
			String idStr = "";
			for (ProductLibraryRelation productLibraryRelation : findByLikeOfficeId) {
				String[] ids = productLibraryRelation.getParentsIds().split(",");
				idStr += "'" + productLibraryRelation.getParentId() + "'" + ",";
				for (String string : ids) {
					idStr += "'" + string + "'" + ",";
				}
			}
			if (StringUtils.isNotBlank(idStr)) {
				idStr = idStr.substring(0, idStr.length() - 1);
			}
			if (StringUtils.isBlank(idStr)) {
				idStr = "''";
			}
			List<ProductLibraryTree> findProductLibraryTree = productLibraryTreeDao.findByInid(idStr);
			ProductLibraryTreeVo vo = treeList(findProductLibraryTree, findByIdAndParentId);
			List<ProductLibraryTreeVo> voList = new ArrayList<ProductLibraryTreeVo>();
			voList.add(vo);
			return voList;
		} else {
			List<ProductLibraryTree> findProductLibraryTree = findProductLibraryTree(findByIdAndParentId, officeId, false);
			ProductLibraryTreeVo vo = treeList(findProductLibraryTree, findByIdAndParentId);
			List<ProductLibraryTreeVo> voList = new ArrayList<ProductLibraryTreeVo>();
			voList.add(vo);
			return voList;
		}
	}
	
	/**
	 * 获取种类管理树状
	 * 
	 * @param baseId
	 * 
	 * @return
	 */
	public List<ProductLibraryTreeVo> getProductLibraryTreeList(String baseId, String itemId, boolean onlyNext) {
		ProductLibraryTree findByIdAndParentId = productLibraryTreeDao.findByIdAndParentId("1", "0");
		List<ProductLibraryTree> findProductLibraryTree = findProductLibraryTree(findByIdAndParentId, itemId, onlyNext);
		ProductLibraryTreeVo vo = treeList(findProductLibraryTree, findByIdAndParentId);
		List<ProductLibraryTreeVo> voList = new ArrayList<ProductLibraryTreeVo>();
		voList.add(vo);
		return voList;
	}

	/**
	 * 拼接下级
	 * 
	 * @param findByIdAndParentId
	 * @param onlyNext
	 * @return
	 */
	public List<ProductLibraryTree> findProductLibraryTree(ProductLibraryTree findByIdAndParentId, String itemId, boolean onlyNext) {
		DetachedCriteria dc = productLibraryTreeDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(findByIdAndParentId.getId())) {
			dc.createAlias("parent", "office");
			if (onlyNext) {
				dc.add(Restrictions.eq("parent.id", findByIdAndParentId.getId()));
			} else {
				dc.add(Restrictions.like("parentsIds", "," + findByIdAndParentId.getId() + ",", MatchMode.ANYWHERE));
			}
		}
		if ("1".equals(itemId)) {

		} else {
			dc.add(Restrictions.eq("isProductCategory", "1"));
		}
		dc.add(Restrictions.ne(ProductLibraryTree.FIELD_DEL_FLAG_XGXT, ProductLibraryTree.STATE_FLAG_DEL));
		dc.addOrder(Order.desc("createTime"));
		return productLibraryTreeDao.find(dc);
	}

	public ProductLibraryTreeVo treeList(List<ProductLibraryTree> arrayList, ProductLibraryTree agroProductLibraryTree) {
		ProductLibraryTreeVo vo = new ProductLibraryTreeVo();
		vo.setId(agroProductLibraryTree.getId());
		vo.setLabel(agroProductLibraryTree.getProductCategoryName());
		for (ProductLibraryTree agroProductLibraryTreeTemp : arrayList) {
			if (agroProductLibraryTree.getId().equals(agroProductLibraryTreeTemp.getParent().getId())) {
				ProductLibraryTreeVo agroProductLibraryTreeVoTemp = new ProductLibraryTreeVo();
				agroProductLibraryTreeVoTemp.setId(agroProductLibraryTreeTemp.getId());
				agroProductLibraryTreeVoTemp.setLabel(agroProductLibraryTreeTemp.getProductCategoryName());
				agroProductLibraryTreeVoTemp.setChildrenCount(agroProductLibraryTreeTemp.getChildList().size() + "");
				if (agroProductLibraryTreeTemp.getChildList().size() > 0) {
					agroProductLibraryTreeVoTemp = treeList(arrayList, agroProductLibraryTreeTemp);
				}
				vo.getChildren().add(agroProductLibraryTreeVoTemp);
			}
		}
		return vo;
	}
	
	/**
	 * 根据种类ID查询
	 * @param id
	 * @return
	 */
	public List<ProductionBatchResp> findById(String id) {
		List<ProductionBatchResp> respArrayList = new ArrayList<ProductionBatchResp>();
		// 查询当前种类分区
		ProductLibraryTree findByIdAndisProductCategory = productLibraryTreeDao.findByIdAndIsProductCategory(id, "1");
		if (null != findByIdAndisProductCategory) {
			List<ProductLibraryTree> arrayList = productLibraryTreeDao.findByParentsIdsAndIsProductCategory(findByIdAndisProductCategory.getId(), "2");
			for (int i = 0; i < arrayList.size(); i++) {
				ProductLibraryTree agroProductLibraryTree = arrayList.get(i);
				ProductionBatchResp productionBatchReq = new ProductionBatchResp();
				productionBatchReq.setId(agroProductLibraryTree.getId());
				productionBatchReq.setProductCategoryName(agroProductLibraryTree.getProductCategoryName());
				respArrayList.add(productionBatchReq);
			}
		}
		return respArrayList;
	}
	
	
	/**
	 * 根据公司查询种类ID
	 * @param id
	 * @return
	 */
	public List<ProductionBatchResp> findOfficeAndBbaseIdById(User user, String id) {
		List<ProductionBatchResp> respArrayList = new ArrayList<ProductionBatchResp>();
		// 查询当前种类分区
		ProductLibraryTree findByIdAndisProductCategory = productLibraryTreeDao.findByIdAndIsProductCategory(id, "1");
		if (null != findByIdAndisProductCategory) {
			List<ProductLibraryTree> arrayList = null;
			if("1".equals(user.getOffice().getId())) {
				arrayList = productLibraryTreeDao.findByParentsIdsAndIsProductCategory(findByIdAndisProductCategory.getId(), "2");
			} else {
				arrayList = productLibraryTreeDao.findByDistinct(findByIdAndisProductCategory.getId(), "2", user.getOffice().getId());
			}
			for (int i = 0; i < arrayList.size(); i++) {
				ProductLibraryTree agroProductLibraryTree = arrayList.get(i);
				ProductionBatchResp productionBatchReq = new ProductionBatchResp();
				productionBatchReq.setId(agroProductLibraryTree.getId());
				productionBatchReq.setProductCategoryName(agroProductLibraryTree.getProductCategoryName());
				respArrayList.add(productionBatchReq);
			}
		}
		return respArrayList;
	}
	
	
	public List<ProductLibraryTreeVo> getSpeciesManageList(){
		List<ProductLibraryTreeVo> respArrayList = new ArrayList<ProductLibraryTreeVo>();
		List<ProductLibraryTree> findAll = productLibraryTreeDao.findAll();
		for (ProductLibraryTree agroProductLibraryTree : findAll) {
			ProductLibraryTreeVo agroProductLibraryTreeVoTemp = new ProductLibraryTreeVo();
			agroProductLibraryTreeVoTemp.setId(agroProductLibraryTree.getId());
			agroProductLibraryTreeVoTemp.setLabel(agroProductLibraryTree.getProductCategoryName());
			respArrayList.add(agroProductLibraryTreeVoTemp);
		}
		return respArrayList;
	}

	
	
	public List<ProductLibraryTreeVo> getlist(String id){
		List<ProductLibraryTreeVo> respArrayList = new ArrayList<ProductLibraryTreeVo>();
		List<ProductLibraryTree> arrayList = productLibraryTreeDao.getlist(id);
		for (int i = 0; i < arrayList.size(); i++) {
			ProductLibraryTree agroProductLibraryTree = arrayList.get(i);
			ProductLibraryTreeVo productLibraryTreeVo = new ProductLibraryTreeVo();
			productLibraryTreeVo.setId(agroProductLibraryTree.getId());
			productLibraryTreeVo.setLabel(agroProductLibraryTree.getProductCategoryName());
			if(agroProductLibraryTree.getIsProductCategory().equals("2")) {
				productLibraryTreeVo.setChildrenCount("0");
			}
			respArrayList.add(productLibraryTreeVo);
		}
		return respArrayList;
	}

	/**
	 * Title: getofficeProductLibraryTreeList Description: 根据企业查询品种库
	 * 
	 * @param officeId
	 *            企业id
	 * @return
	 */
	public List<ProductTreeList> getofficeProductLibraryTreeList(String officeId,String officeIds) {
		ProductLibraryTree findByIdAndParentId = productLibraryTreeDao.findByIdAndParentId("1", "0");
		Set<String> strList = new HashSet<String>();
		Office office = officeDao.get(officeIds);
		String[] strs = office.getParentIds().split(",");
		if (strs.length>=3) {
			for (int i = strs.length-1;i>=2;i--) {
				List<ProductLibraryRelation> pranOfficeId = productLibraryRelationDao.findByLikeOfficeId(strs[i]);
				for (ProductLibraryRelation productLibraryRelation : pranOfficeId) {
					strList.add(productLibraryRelation.getParentId());
				}
			}
		}
		if (!"1".equals(officeId) && StringUtils.isNotBlank(officeId)) {
			List<ProductLibraryRelation> findByLikeOfficeId = productLibraryRelationDao.findByLikeOfficeId(office.getId(), office.getId());
			String idStr = "";
			for (ProductLibraryRelation productLibraryRelation : findByLikeOfficeId) {
				String[] ids = productLibraryRelation.getParentsIds().split(",");
				idStr += "'" + productLibraryRelation.getParentId() + "'" + ",";
				for (String string : ids) {
					idStr += "'" + string + "'" + ",";
				}
			}
			if (StringUtils.isNotBlank(idStr)) {
				idStr = idStr.substring(0, idStr.length() - 1);
			}
			if (StringUtils.isBlank(idStr)) {
				idStr = "''";
			}
			List<ProductLibraryTree> findProductLibraryTree = productLibraryTreeDao.findByInid(idStr);
			ProductTreeList vo = productTreeList(findProductLibraryTree, findByIdAndParentId,strList);
			List<ProductTreeList> voList = new ArrayList<ProductTreeList>();
			voList.add(vo);
			return voList;
		} else {
			List<ProductLibraryTree> findProductLibraryTree = findProductLibraryTree(findByIdAndParentId, officeId, false);
			ProductTreeList vo = productTreeList(findProductLibraryTree, findByIdAndParentId, strList);
			List<ProductTreeList> voList = new ArrayList<ProductTreeList>();
			voList.add(vo);
			return voList;
		}
	}

	/**
	 * Title: getofficeProductLibraryTreeList Description: 根据企业查询品种库
	 * 
	 * @param officeId
	 *            企业id
	 * @return
	 */
	public Set<String> getofficeProductIdList(String officeId) {
		Set<String> strList = new HashSet<String>();
		Office office = officeDao.get(officeId);
		List<ProductLibraryRelation> findByLikeOfficeId = productLibraryRelationDao.findByLikeOfficeId(office.getId());
		for (ProductLibraryRelation productLibraryRelation : findByLikeOfficeId) {
			strList.add(productLibraryRelation.getParentId());
		}
		String[] strs = office.getParentIds().split(",");
		if (strs.length >= 3) {
			for (int i = strs.length - 1; i >= 2; i--) {
				List<ProductLibraryRelation> pranOfficeId = productLibraryRelationDao.findByLikeOfficeId(strs[i]);
				for (ProductLibraryRelation productLibraryRelation : pranOfficeId) {
					strList.add(productLibraryRelation.getParentId());
				}
			}
		}
		return strList;
	}
	public ProductTreeList productTreeList(List<ProductLibraryTree> arrayList, ProductLibraryTree agroProductLibraryTree, Set<String> strList) {
		ProductTreeList vo = new ProductTreeList();
		vo.setId(agroProductLibraryTree.getId());
		vo.setIsProductCategory(agroProductLibraryTree.getIsProductCategory());
		vo.setPid(agroProductLibraryTree.getParent()==null?"0":agroProductLibraryTree.getParent().getId());
		vo.setLabel(agroProductLibraryTree.getProductCategoryName());
		for (ProductLibraryTree agroProductLibraryTreeTemp : arrayList) {
			if (agroProductLibraryTree.getId().equals(agroProductLibraryTreeTemp.getParent().getId())) {
				ProductTreeList agroProductLibraryTreeVoTemp = new ProductTreeList();
				agroProductLibraryTreeVoTemp.setId(agroProductLibraryTreeTemp.getId());
				agroProductLibraryTreeVoTemp.setIsProductCategory(agroProductLibraryTreeTemp.getIsProductCategory());
				agroProductLibraryTreeVoTemp.setPid(agroProductLibraryTreeTemp.getParent().getId());
				agroProductLibraryTreeVoTemp.setLabel(agroProductLibraryTreeTemp.getProductCategoryName());
				if (agroProductLibraryTreeTemp.getChildList().size() > 0) {
					agroProductLibraryTreeVoTemp = productTreeList(arrayList, agroProductLibraryTreeTemp, strList);
				}
				if (strList.contains(agroProductLibraryTreeVoTemp.getId())) {
					agroProductLibraryTreeVoTemp.setDisabled(true);
				}
				if ("1".equals(agroProductLibraryTreeVoTemp.getIsProductCategory())&&agroProductLibraryTreeVoTemp.getChildren().size()==0) {
					agroProductLibraryTreeVoTemp.setDisabled(true);
				}
				vo.getChildren().add(agroProductLibraryTreeVoTemp);
			}
		}
		if ("1".equals(vo.getIsProductCategory())&&vo.getChildren().size()==0) {
			vo.setDisabled(true);
		}
		return vo;
	}

	@Transactional(readOnly = false)
	public boolean saveProductRelation(List<String> productIds,String officeId,String type, User user) {
		// 新增品种与公司关系数据
		Office office = officeDao.get(officeId);
		List<ProductLibraryRelation> list = new ArrayList<ProductLibraryRelation>();
		for (String id : productIds) {
			ProductLibraryTree libraryTree = libraryTreeDao.get(id);
			if ("1".equals(libraryTree.getIsProductCategory())) {
				continue;
			}
			if ("delete".equals(type)) {
				productLibraryRelationDao.deleteByOfficeId(office.getId(),id);
			}
			if ("save".equals(type)) {
				ProductLibraryRelation pojo = new ProductLibraryRelation();
				pojo.setOfficeId(office.getId());
				pojo.setOfficeIds(office.getParentIds());
				pojo.setParentId(libraryTree.getId());
				pojo.setParentsIds(libraryTree.getParentsIds());
				list.add(pojo);
			}
		}
		if ("save".equals(type)&&list.size()>0) {
			productLibraryRelationDao.save(list);
		}
		return true;
	}
}
