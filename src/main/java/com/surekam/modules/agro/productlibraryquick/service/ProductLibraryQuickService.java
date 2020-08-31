package com.surekam.modules.agro.productlibraryquick.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.basemanage.dao.BaseTreeDao;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
import com.surekam.modules.agro.product.dao.ProductLibraryTreeDao;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.productlibraryquick.dao.ProductLibraryQuickDao;
import com.surekam.modules.agro.productlibraryquick.dao.ProductLibraryQuickValueDao;
import com.surekam.modules.agro.productlibraryquick.entity.ProductLibraryQuick;
import com.surekam.modules.agro.productlibraryquick.entity.ProductLibraryQuickValue;
import com.surekam.modules.agro.productlibraryrelation.dao.ProductLibraryRelationDao;
import com.surekam.modules.agro.productlibraryrelation.entity.ProductLibraryRelation;
import com.surekam.modules.api.dto.resp.ProductLibraryTreeResp;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;

/**
 * 品种快捷表Service
 * 
 * @author tangjun
 * @version 2019-06-03
 */
@Component
@Transactional(readOnly = true)
public class ProductLibraryQuickService extends BaseService {
	
	@Autowired
	private OfficeDao officeDao;
	
	@Autowired
	private BaseTreeDao baseTreeDao;

	@Autowired
	private ProductLibraryQuickDao productLibraryQuickDao;
	
	@Autowired
	private ProductLibraryRelationDao productLibraryRelationDao;
	
	@Autowired
	private ProductLibraryQuickValueDao productLibraryQuickValueDao;
	
	@Autowired
	private ProductLibraryTreeDao productLibraryTreeDao;
	
	/**
	 * Title: getProductLibraryQuickList Description: 查询公司下的快捷品种
	 * 
	 * @param baseId
	 *            基地ID
	 * @return
	 */
	public List<ProductLibraryTreeResp> findByOfficeQueryList(String baseId) {
		List<ProductLibraryTreeResp> resp = new ArrayList<ProductLibraryTreeResp>();
		
		BaseTree baseTree = baseTreeDao.get(baseId);
		if (null != baseTree) {
			String idStr = "";
			Office office = officeDao.get(baseTree.getOfficeId());
			String[] parentIds = office.getParentIds().split(",");
			String offStr = "'" + office.getId() + "'" + ",";
			for (String string : parentIds) {
				if (!"0".equals(string) && !"1".equals(string)) {
					offStr += "'" + string + "'" + ",";
				}
			}
			List<ProductLibraryRelation> findByLikeOfficeId = productLibraryRelationDao.findByInOfficeId(offStr.substring(0, offStr.length() - 1));
			for (ProductLibraryRelation productLibraryRelation : findByLikeOfficeId) {
				idStr += "'" + productLibraryRelation.getParentId() + "'" + ",";
			}
			if (StringUtils.isNotBlank(idStr)) {
				List<ProductLibraryTree> findByInId = productLibraryTreeDao.findByInId(idStr.substring(0, idStr.length() - 1));
				for (ProductLibraryTree productLibraryTree : findByInId) {
					ProductLibraryTreeResp pojo = new ProductLibraryTreeResp();
					pojo.setId(productLibraryTree.getId());
					pojo.setClassificationId(productLibraryTree.getParent().getId());
					pojo.setOfficeId(office.getId());
					pojo.setOfficeName(office.getName());
					pojo.setProductId(productLibraryTree.getId());
					pojo.setProductName(productLibraryTree.getParent().getProductCategoryName());
					pojo.setStandardsName(productLibraryTree.getProductCategoryName());
					resp.add(pojo);
				}
			}
		}
		return resp;
	}

	public ProductLibraryQuick get(String id) {
		ProductLibraryQuick productLibraryQuick= productLibraryQuickDao.get(id);
		if (productLibraryQuick!=null){
			List<ProductLibraryQuickValue> list = productLibraryQuickValueDao.findByQuickId(id);
			productLibraryQuick.setList(list);
		}
		return productLibraryQuick;
	}

	public Page<ProductLibraryQuick> find(Page<ProductLibraryQuick> page, String officeId, User user) {
		DetachedCriteria dc = productLibraryQuickDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.add(Restrictions.or(
				// 本企业的信息
				Restrictions.eq("office.id", officeId),
				// 所有下级企业的信息
				Restrictions.like("office.parentIds", "%," + officeId + ",%")));
		dc.add(Restrictions.ne(ProductLibraryQuick.FIELD_DEL_FLAG_XGXT, ProductLibraryQuick.STATE_FLAG_DEL));
		dc.addOrder(Order.desc("createTime"));
		Page<ProductLibraryQuick> pageSensorSetup = productLibraryQuickDao.find(page, dc);
		List<ProductLibraryQuick> list = pageSensorSetup.getList();
		for(ProductLibraryQuick productLibraryQuick:list){
			List<ProductLibraryQuickValue> listValue = productLibraryQuickValueDao.findByQuickId(productLibraryQuick.getId());
			productLibraryQuick.setList(listValue);
		}
		return pageSensorSetup;
	}

	@Transactional(readOnly = false)
	public void save(ProductLibraryQuick productLibraryQuick) {
		if(StringUtils.isNotBlank(productLibraryQuick.getId())){
			productLibraryQuickDao.clear();
			productLibraryQuickDao.flush();
			productLibraryQuickValueDao.clear();
			productLibraryQuickValueDao.flush();
		}
		productLibraryQuickDao.save(productLibraryQuick);
		List<ProductLibraryQuickValue> list = productLibraryQuick.getList();
		for (ProductLibraryQuickValue value : list) {
			value.setProductQuickId(productLibraryQuick.getId());
			if ("A".equals(productLibraryQuick.getStates())) {
				value.setCreateTime(new Date());
				value.setCreateUserId(productLibraryQuick.getCreateUserId());
				value.setStates("A");
			} else {
				value.setUpdateTime(new Date());
				value.setStates("U");
				value.setUpdateUserId(productLibraryQuick.getUpdateUserId());
			}
		}
		productLibraryQuickValueDao.save(list);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		productLibraryQuickDao.deleteByXGXTId(id);
	}
	// 全区图形报表接口
	public Map getVarietyDistribution(String officeId) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		// 返回图标的各种元素集合
		Set<Object> columns = new LinkedHashSet<Object>();
		// 要折现的元素集合
		List<LinkedHashMap<String, Object>> listMap = new ArrayList<LinkedHashMap<String, Object>>();
		columns.add("品种");
		columns.add("企业总数");
		List<Object> list = productLibraryQuickDao.getVarietyDistribution(officeId);
		for(int i = 0; i<list.size();i++){
			Object[] obj = (Object[]) list.get(i);
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("品种", obj[0].toString());
			map.put("企业总数", obj[1]);
			listMap.add(map);
		}
		reqMap.put("columns", columns);
		reqMap.put("rows", listMap);
		return reqMap;
	}

	public List<ProductLibraryQuick> findByOfficeIdAndClassificationIdAndSpeciesId(String officeId, String speciesId) {
		List<ProductLibraryQuick> findByOfficeIdAndClassificationIdAndSpeciesId = productLibraryQuickDao.findByOfficeIdAndClassificationIdAndSpeciesId(officeId, speciesId);
		return findByOfficeIdAndClassificationIdAndSpeciesId;
	}
	public boolean exist(ProductLibraryQuick pro) {
		List<ProductLibraryQuick> findBySql = new ArrayList<ProductLibraryQuick>();
		if(StringUtils.isBlank(pro.getId())){
			String sql = "select * from t_agro_product_library_quick a where a.office_id =:p1 and a.product_id =:p2 and a.system_standards_id = :p3 and a.states!='D'";
			findBySql = productLibraryQuickDao.findBySql(sql,new Parameter(pro.getOfficeId(), pro.getProductId(),pro.getStandardsId()), ProductLibraryQuick.class);
		}else{
			String sql = "select * from t_agro_product_library_quick a where a.office_id =:p1 and a.product_id =:p2 and a.system_standards_id = :p3 and a.id <> :p4 and a.states!='D'";
			findBySql = productLibraryQuickDao.findBySql(sql,new Parameter(pro.getOfficeId(), pro.getProductId(),pro.getStandardsId(),pro.getId()), ProductLibraryQuick.class);
		}
		if(findBySql.size()>0){
			return true;
		}else{
			return false;
		}
	}

}
