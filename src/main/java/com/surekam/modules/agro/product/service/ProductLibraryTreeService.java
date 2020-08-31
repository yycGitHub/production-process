package com.surekam.modules.agro.product.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.Scanner;


import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import sun.swing.BakedArrayList;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.Client;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.experts.dao.ExpertsDao;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.product.dao.ProductLibraryTreeDao;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.productionbatch.dao.StatisticsProductionBatcDao;
import com.surekam.modules.agro.productionbatch.entity.StatisticsProductionBatch;
import com.surekam.modules.agro.standardtaskitemsargsvalue.dao.StandardTaskItemsArgsValueDao;
import com.surekam.modules.agro.standardtaskitemsargsvalue.entity.StandardTaskItemsArgsValue;
import com.surekam.modules.api.dto.resp.StatisticsBatchResq;
import com.surekam.modules.api.utils.SolarTerms_24;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.utils.StaticStringUtils;

/**
 * 品种分类Service
 * @author lb
 * @version 2019-04-10
 */
@Component
@Transactional(readOnly = true)
public class ProductLibraryTreeService extends BaseService {
	public static String sy_img_url = Global.getConfig("sy_img_url");
	public static String jg_api_url = Global.getConfig("jg_api_url");

	@Autowired
	private ProductLibraryTreeDao productLibraryTreeDao;
	
	@Autowired
	private StatisticsProductionBatcDao statisticsProductionBatcDao;
	
	@Autowired
	private StandardTaskItemsArgsValueDao standardTaskItemsArgsValueDao;
	
	@Autowired
	private ExpertsDao expertsDao;
		
	/**
	 * 专家id查询领域信息
	 * @param expertsId
	 * @return
	 */
	public List<ProductLibraryTree> getList(String expertsId){
		StringBuffer sql = new StringBuffer(200);
		sql.append("SELECT distinct p.* FROM t_agro_experts_professionalfield_relation e,t_agro_product_library_tree p WHERE e.product_library_id = p.id ");
		if(StringUtils.isNotBlank(expertsId)) {
			sql.append(" and e.experts_id ='"+expertsId+"' ");
		}
		sql.append(" and e.states <>'D' ");
		return productLibraryTreeDao.findBySql2(sql.toString(), new Parameter(), ProductLibraryTree.class);	
	}
	
	/**
	 * 基地id查询基地种植养殖品种信息
	 * @param expertsId
	 * @return
	 */
	public List<ProductLibraryTree> getofficeIdByList(String officeId){
		StringBuffer sql = new StringBuffer(200);
		sql.append("SELECT distinct plt.* FROM t_agro_production_batch pb,t_agro_base_tree bt,t_agro_product_library_tree plt ");
		sql.append(" WHERE pb.base_id = bt.id and plt.id = pb.product_id and bt.office_id = '" + officeId +"' ");
		return productLibraryTreeDao.findBySql2(sql.toString(), new Parameter(), ProductLibraryTree.class);	
	}

	public ProductLibraryTree get(String id) {
		return productLibraryTreeDao.get(id);
	}
	public Map getYield(String officeId, String productId, String particularYear,int order_value) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		// 返回图标的各种元素集合
		Set<Object> columns = new LinkedHashSet<Object>();
		// 要折现的元素集合
		Set<Object> showLine = new LinkedHashSet<Object>();
		columns.add("time");
		columns.add("面积");		
		// 获取符合条件的所有批次
		List<StatisticsProductionBatch> list2 = statisticsProductionBatcDao.findByBaseIdAndStates(officeId, productId);
		List<LinkedHashMap<String, Object>> listMap = new ArrayList<LinkedHashMap<String, Object>>();
		// 获取根据月份分组的品种面积分布
		List<Object> ProductArea = getAgroProductAreaByMonth(officeId,productId,particularYear);
		
		//获取品种的（种养殖）面积
		//List<Object> productArea2 = getAgroProductArea2(officeId,productId,particularYear);
		
		for (StatisticsProductionBatch statisticsProductionBatch : list2) {
			// 获取批次采收品种集合
			List<StandardTaskItemsArgsValue> staValueList = standardTaskItemsArgsValueDao.findByRegionIdAndStates(statisticsProductionBatch.getId(), "采收类型", "2");
			if (!staValueList.isEmpty()) {
				for (StandardTaskItemsArgsValue standardTaskItemsArgsValue : staValueList) {
					showLine.add(standardTaskItemsArgsValue.getTaskItemArgsValue());
					// 根据月份分组获取采收量
					List<Object> findByTaskListIdAndArgsName = standardTaskItemsArgsValueDao.getYield(standardTaskItemsArgsValue.getTaskListId(), particularYear);
					// 12个月
					for(int j = 1;j<=12;j++){
						LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
						// 如果listMap大小为12则代表已经插入过数据，需要获取已经插入的数据追加元素数据。反之直接put数据
						if(listMap.size()==12){
							map=listMap.get(j-1);
						}else{
							map.put("time", j+"月");
							map.put("面积", 0);
							if (ProductArea.size()>0){
								for (int i = 0; i < ProductArea.size(); i++) {
									Object[] object = (Object[]) ProductArea.get(i);
									// 获取j月的面积
									if (object[0].equals(j)) {
										map.put("面积", object[1]);
									}
								}
							}
						}
						map.put(standardTaskItemsArgsValue.getTaskItemArgsValue(), 0);
						if (findByTaskListIdAndArgsName.size()>0){
							for (int i = 0; i < findByTaskListIdAndArgsName.size(); i++) {
								Object[] object = (Object[]) findByTaskListIdAndArgsName.get(i);
								if (object[0].equals(j)) {
									if(map.containsKey(standardTaskItemsArgsValue.getTaskItemArgsValue())){
										map.put(standardTaskItemsArgsValue.getTaskItemArgsValue(), 
												Double.parseDouble(object[1].toString())+Double.parseDouble(map.get(standardTaskItemsArgsValue.getTaskItemArgsValue()).toString()));
									}else{
										map.put(standardTaskItemsArgsValue.getTaskItemArgsValue(), object[1]);
									}
								}
							}
						}
						if(listMap.size()==12){
							listMap.set(j-1,map);
						} else{
							listMap.add(map);
						}
					}
				}
			} else {
				// 如果没有采收品种信息，直接获取j月的面积
				for(int j = 1;j<=12;j++){
					LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
					if(listMap.size()==12){
						map=listMap.get(j-1);
					}else{
						map.put("time", j+"月");
						map.put("面积", 0);
						if (ProductArea.size()>0){
							for (int i = 0; i < ProductArea.size(); i++) {
								Object[] object = (Object[]) ProductArea.get(i);
								if (object[0].equals(j)) {
									map.put("面积", object[1]);
								}
							}
						}
					}
					if(listMap.size()==12){
						listMap.set(j-1,map);
					} else{
						listMap.add(map);
					}
				}
			}
		}
		
		//各采收类型的产量集合
		LinkedHashMap<String, Double> map1 = new LinkedHashMap<String, Double>();
		
		for(int k = 0; k < listMap.size(); k ++) {
			LinkedHashMap<String, Object> map0 = listMap.get(k);
			Set<Entry<String, Object>> set = map0.entrySet();
			Iterator<Entry<String, Object>> iterator = set.iterator();
			if(k == 0) {
				for(int m = 0; m < set.size(); m++) {
					Entry<String, Object> entry = iterator.next();
					if(m > 1) {
						double wg;
						if(entry.getValue().toString().indexOf(".") > 0) {
							wg = Double.parseDouble(entry.getValue().toString());
						}else {
							wg = Integer.parseInt(entry.getValue().toString());
						}
						map1.put(entry.getKey(), wg);
					}
				}
			}else {
				for(int m = 0; m < set.size(); m++) {
					Entry<String, Object> entry = iterator.next();
					if(m > 1) {
						double wg;
						if(entry.getValue().toString().indexOf(".") > 0) {
							wg = Double.parseDouble(entry.getValue().toString());
						}else {
							wg = Integer.parseInt(entry.getValue().toString());
						}
						if(map1.containsKey(entry.getKey())){
							map1.put(entry.getKey(), wg + map1.get(entry.getKey()));
						}else{
							map1.put(entry.getKey(), wg);
						}
					}
				}
			}
		}
		//按产量倒序（大到小）
		List<Map.Entry<String,Double>> listm = new ArrayList<Map.Entry<String,Double>>(map1.entrySet());
		Collections.sort(listm,new Comparator<Map.Entry<String, Double>>(){
			   //降序排序
			   public int compare(Entry<String,Double> o1,Entry<String ,Double> o2) {
				   return o2.getValue().compareTo(o1.getValue());
			   }
		   });
		//选出前10名的采收类型，剩余的用其他表示
		Set<Object> showLine2 = new LinkedHashSet<Object>();
		for(int n = 0; n < listm.size(); n++) {
			String str = listm.get(n).getKey();
			if(n < order_value) {
				for (Object str2 : showLine) {  
				      if(str.equals(str2)) {
				    	  showLine2.add(str);
				      }
				}  
			}else {
				for (Object str2 : showLine) {  
				      if(str.equals(str2)) {
				    	  showLine2.add("其他");
				      }
				}  
			}
		}
		//组合图表数据
		List<LinkedHashMap<String, Object>> listMap2 = new ArrayList<LinkedHashMap<String, Object>>();
		for(int p = 0; p < listMap.size(); p++) {
			LinkedHashMap<String, Object> lh = listMap.get(p);
			LinkedHashMap<String, Object> listh = new LinkedHashMap<String, Object>();
			
			Set<Entry<String, Object>> set2 = lh.entrySet();
			Iterator<Entry<String, Object>> iterator = set2.iterator();
			for(int b = 0; b < set2.size(); b++) {
				Entry<String, Object> entry2 = iterator.next();
				if(b < 2) {
					listh.put(entry2.getKey(), entry2.getValue());
				}else {
					double wg1 = 0;
					for (Object str3 : showLine2) { 
						if(entry2.getKey().equals(str3)) {
							listh.put(entry2.getKey(), entry2.getValue());
						}else {
							wg1 += Double.parseDouble(entry2.getValue().toString());
						}
					} 
					listh.put("其他", wg1);
				}
			}
			listMap2.add(listh);
		}
		columns.addAll(showLine2);
		reqMap.put("columns", columns);
		reqMap.put("rows", listMap2);
		reqMap.put("showLine", showLine2);
		return reqMap;
	}
	
	public Page<ProductLibraryTree> find(Page<ProductLibraryTree> page, ProductLibraryTree productLibraryTree) {
		DetachedCriteria dc = productLibraryTreeDao.createDetachedCriteria();
		dc.add(Restrictions.ne(ProductLibraryTree.FIELD_DEL_FLAG_XGXT, ProductLibraryTree.STATE_FLAG_DEL));
		return productLibraryTreeDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public boolean save(ProductLibraryTree productLibraryTree) {
		String oldParentIds = productLibraryTree.getParentsIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		if (StringUtils.isNotBlank(productLibraryTree.getParentId())) {
			ProductLibraryTree parent = this.get(productLibraryTree.getParentId());
			productLibraryTree.setParent(parent);
			productLibraryTree.setParentsIds(parent.getParentsIds() + parent.getId() + ",");
			List<ProductLibraryTree> findByParentIdAndProductcategoryName = productLibraryTreeDao.findByParentIdAndProductcategoryName(parent.getId(), productLibraryTree.getProductCategoryName());
			for (ProductLibraryTree pojo : findByParentIdAndProductcategoryName) {
				if (!pojo.getId().equals(productLibraryTree.getId())) {
					return false;
				}
			}
		}
		productLibraryTreeDao.flush();
		productLibraryTreeDao.clear();
		productLibraryTreeDao.save(productLibraryTree);

		// 更新子节点 parentIds
		List<ProductLibraryTree> list = productLibraryTreeDao.findByParentIdsLike("%," + productLibraryTree.getId() + ",%");
		for (ProductLibraryTree e : list) {
			e.setParentsIds(e.getParentsIds().replace(oldParentIds, productLibraryTree.getParentsIds()));
		}
		productLibraryTreeDao.save(list);
		Map<String, String> params = new HashMap<String, String>();
		params = object2Map(productLibraryTree);
		params.remove("parent");
		params.remove("childList");
		params.put("productCategoryImgUrl", productLibraryTree.getProductCategoryImgUrl() == null ? "" : sy_img_url + productLibraryTree.getProductCategoryImgUrl());
		System.out.println("params:" + params);
		String postRequest = Client.fromHttp(jg_api_url + "api/productLibrary/save", params);
		System.out.println("postRequest:" + postRequest);
		JSONObject obj = new JSONObject(postRequest);
		if (obj == null || !"0".equals(obj.get("code").toString())) {
			System.out.println("接口错误:" + postRequest);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}
	
	@Transactional(readOnly = false)
	public boolean delete(String id) {
		productLibraryTreeDao.deleteByXGXTId(id);
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		String postRequest = Client.fromHttp(jg_api_url+"api/productLibrary/delete", params);
		System.out.println("postRequest:"+postRequest);
		JSONObject obj = new JSONObject(postRequest);
		if(obj==null||!"0".equals(obj.get("code").toString())){
			System.out.println("接口错误:"+postRequest);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}
	
	@Transactional(readOnly = false)
	public boolean enable(String id) {
		productLibraryTreeDao.resumeByXGXTId(id);
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		String postRequest = Client.fromHttp(jg_api_url+"api/productLibrary/enable", params);
		System.out.println("postRequest:"+postRequest);
		JSONObject obj = new JSONObject(postRequest);
		if(obj==null||!"0".equals(obj.get("code").toString())){
			System.out.println("接口错误:"+postRequest);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}

	public List<ProductLibraryTree> findLibrarys(User currentUser, String itemId, boolean onlyNext, boolean getCategory) {
		DetachedCriteria dc = productLibraryTreeDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(itemId)) {
			dc.createAlias("parent", "parent");
			if (onlyNext) {
				dc.add(Restrictions.eq("parent.id", itemId));
			} else {
				ProductLibraryTree productLibraryTree = productLibraryTreeDao.get(itemId);
				dc.add(Restrictions.like("parentsIds", productLibraryTree.getParentsIds() + itemId + ",", MatchMode.START));
			}
		}
		if (getCategory) {
			dc.add(Restrictions.eq("isProductCategory", "1"));
		}
		dc.add(Restrictions.ne(ProductLibraryTree.FIELD_DEL_FLAG_XGXT, ProductLibraryTree.STATE_FLAG_DEL));
		dc.addOrder(Order.asc("sort"));
		return productLibraryTreeDao.find(dc);
	}

	public Page<ProductLibraryTree> findPageLibrarys(Page<ProductLibraryTree> page, User currentUser, String name,
			String parentId, String states, boolean onlyNext,boolean getProduct) {
		DetachedCriteria dc = productLibraryTreeDao.createDetachedCriteria();
		if(StringUtils.isNotBlank(name)){
			dc.add(Restrictions.like("productCategoryName",name,MatchMode.START));
		}
		if(StringUtils.isNotBlank(parentId)){
			dc.createAlias("parent", "parent");
			if(onlyNext){
				dc.add(Restrictions.eq("parent.id",parentId));
			}else{
				ProductLibraryTree library = productLibraryTreeDao.get(parentId);
				dc.add(Restrictions.like("parentsIds",library.getParentsIds()+parentId+",",MatchMode.START));
			}
		}
		if (getProduct) {
			dc.add(Restrictions.eq("isProductCategory","2"));
		}
		if(ProductLibraryTree.STATE_FLAG_DEL.equals(states)){
			dc.add(Restrictions.eq(ProductLibraryTree.FIELD_DEL_FLAG_XGXT, states));
		}
		if(ProductLibraryTree.STATE_FLAG_ADD.equals(states) || ProductLibraryTree.STATE_FLAG_UPDATE.equals(states)){
			dc.add(Restrictions.ne(ProductLibraryTree.FIELD_DEL_FLAG_XGXT, ProductLibraryTree.STATE_FLAG_DEL));
		}
		dc.addOrder(Order.asc("sort"));
		return productLibraryTreeDao.find(page, dc);
	}
	
	public Page<ProductLibraryTree> findPageLibrarys_new(Page<ProductLibraryTree> page, User currentUser, String name,
			String parentId, String states, boolean onlyNext,boolean getProduct) {
		//DetachedCriteria dc = productLibraryTreeDao.createDetachedCriteria();
		String hql = "from ProductLibraryTree a where 1=1";
		if(StringUtils.isNotBlank(name)){
			hql+= " and a.productCategoryName like '%" + name + "%'";
		}
		if(StringUtils.isNotBlank(parentId)){
			ProductLibraryTree library = productLibraryTreeDao.get(parentId);
			hql+= " and a.parentsIds like '%" + library.getParentsIds()+parentId+"," + "%'";
		}
		if (getProduct) {
			hql+= " and a.isProductCategory = '2'";
			String ptid = "";
			String parentIds = currentUser.getOffice().getParentIds();
			String officeId = currentUser.getOffice().getId();
			if(parentIds.indexOf(StaticStringUtils.AGRO_TULUFAN)>-1 || officeId.equals(StaticStringUtils.AGRO_TULUFAN)){
				ptid = StaticStringUtils.AGRO_TULUFAN;
			}else if(parentIds.indexOf(StaticStringUtils.AGRO_WANGCHENG)>-1 || officeId.equals(StaticStringUtils.AGRO_WANGCHENG)){
				ptid = StaticStringUtils.AGRO_WANGCHENG;
			}else if(parentIds.indexOf(StaticStringUtils.AGRO_LIUYANG)>-1 || officeId.equals(StaticStringUtils.AGRO_LIUYANG)){
				ptid = StaticStringUtils.AGRO_LIUYANG;
			}else if(parentIds.indexOf(StaticStringUtils.AGRO_NANXIAN)>-1 || officeId.equals(StaticStringUtils.AGRO_NANXIAN)){
				ptid = StaticStringUtils.AGRO_NANXIAN;
			}
			if(StringUtils.isNotBlank(ptid)){
				hql+= " and a.id IN (SELECT b.parentId FROM ProductLibraryRelation b WHERE b.officeId = '" + ptid + "' OR b.officeIds LIKE '%" + ptid + ",%')";
			}
		}
		if(ProductLibraryTree.STATE_FLAG_DEL.equals(states)){
			hql+= " and a.states = '" + states + "'";
		}
		if(ProductLibraryTree.STATE_FLAG_ADD.equals(states) || ProductLibraryTree.STATE_FLAG_UPDATE.equals(states)){
			hql+= " and a.states <> '" + ProductLibraryTree.STATE_FLAG_DEL + "'";
		}
		hql+=" order by a.sort";
		return productLibraryTreeDao.find(page, hql);
	}

	/**
	 * 获取品种面积
	 * @return
	 */
	public List<Object> getAgroProductArea(String id){
		String sql = "SELECT "
		 +" b.product_category_name,"
		 +"  SUM(c.acreage) "
		 +" FROM"
		 +"  t_agro_production_batch a,"
		 +"  t_agro_product_library_tree b,"
		 +"  sys_office o, "
		 +"   t_agro_base_tree c "
		 +" WHERE a.product_id = b.id "
		 +"   AND a.base_id = c.id "
		 +"   AND o.id = c.office_id "
		 +"   AND a.states <> 'D' "
		 +"   AND b.states <> 'D' "
		 +" and  (o.id LIKE '"+id+"' OR o.PARENT_IDS LIKE '%,"+id+",%')"
		 +"   GROUP BY b.id,b.product_category_name order by b.product_category_name";
		return productLibraryTreeDao.findBySql(sql);	
	}
	/**
	 * 农事首页获取品种面积
	 * @return
	 */
	public List<Object> getHomePageAgroProductArea(String id){
		String sql = "SELECT "
		 +" b.id,"
		 +" b.product_category_name,"
		 +"  SUM(c.acreage) "
		 +" FROM"
		 +"  t_agro_production_batch a,"
		 +"  t_agro_product_library_tree b,"
		 +"  sys_office o, "
		 +"   t_agro_base_tree c "
		 +" WHERE a.product_id = b.id "
		 +"   AND a.base_id = c.id "
		 +"   AND o.id = c.office_id "
		 +"   AND a.states <> 'D' "
		 +"   AND b.states <> 'D' "
		 +" and  (o.id LIKE '"+id+"' OR o.PARENT_IDS LIKE '%,"+id+",%')"
		 +"   GROUP BY b.id,b.product_category_name order by b.product_category_name";
		return productLibraryTreeDao.findBySql(sql);	
	}

	/**
	 * 获取首页品种列表
	 * @return
	 */
	public List<Object> getHomePageVarietiesList(String id){
		String sql = "SELECT "
				+ " b.product_category_name, "
				+ " d.product_imag_url,"
				+ " d.product_code, "
				+ " IFNULL(sum(IF(a. STATUS = '1', 1, 0)),0) AS 'notFinished', "
				+ " IFNULL(sum(IF(a. STATUS = '1', 0, 1)),0) AS 'complete',"
				+ " b.id,"
				+ " IFNULL(MAX(a.batch_start_date),'暂无')"
				+ " FROM "
				+ " t_agro_production_batch a,"
				+ " t_agro_product_library_tree b,"
				+ " t_agro_product_library_detail d,"
				+ " t_agro_base_tree c, sys_office o"
				+ " WHERE"
				+ " a.product_id = b.id"
				+ " AND a.base_id = c.id"
				+ " AND o.id = c.office_id"
				+ " AND b.id = d.product_library_id"
				+ " AND a.states <> 'D'"
				+ " AND b.states <> 'D'"
				+" AND  (o.id LIKE '"+id+"' OR o.PARENT_IDS LIKE '%,"+id+",%')"
				+ " GROUP BY b.product_category_name, d.product_code,d.product_imag_url, b.id "
				+ " ORDER BY b.product_category_name";
		return productLibraryTreeDao.findBySql(sql);	
	}
	/**
	 * 获取柱状图品种面积数据
	 * @return
	 */
	public List<Object> getAgroProductAreaByMonth(String officeId, String productId, String year){
		String sql = "SELECT "
				+ " MONTH(a.batch_start_date),"
				+ " IFNULL(SUM(c.acreage),0) "
				+ " FROM"
				+ " t_agro_production_batch a,"
				+ " t_agro_product_library_tree b,"
				+ " sys_office o, "
				+ " t_agro_base_tree c "
				+ " WHERE a.product_id = b.id "
				+ " AND a.base_id = c.id"
				+ " AND o.id = c.office_id "
				+ " AND a.states <> 'D' "
				+ " AND b.states <> 'D' "
				+" AND  (o.id LIKE '"+officeId+"' OR o.PARENT_IDS LIKE '%,"+officeId+",%')";
		if (StringUtils.isNotBlank(year)) {
			sql += " AND YEAR(a.batch_start_date) = '"+year+"'";
		}
		if (StringUtils.isNotBlank(productId)) {
			sql += " AND b.id = '"+productId+"'";
		}
		sql += " GROUP BY MONTH(a.batch_start_date)";
		return productLibraryTreeDao.findBySql(sql);	
	}
	
	
	
	/**
	 * 获取柱状图品种面积数据
	 * @return
	 */
	public List<Object> getAgroProductArea2(String officeId, String productId, String year){
		String sql = "SELECT "
				+ " a.product_id,b.product_category_name,IFNULL(SUM(c.acreage),0)"
				+ " FROM"
				+ " t_agro_production_batch a,"
				+ " t_agro_product_library_tree b,"
				+ " sys_office o, "
				+ " t_agro_base_tree c "
				+ " WHERE a.product_id = b.id "
				+ " AND a.base_id = c.id"
				+ " AND o.id = c.office_id "
				+ " AND a.states <> 'D' "
				+ " AND b.states <> 'D' "
				+" AND  (o.id LIKE '"+officeId+"' OR o.PARENT_IDS LIKE '%,"+officeId+",%')";
		if (StringUtils.isNotBlank(year)) {
			sql += " AND YEAR(a.batch_start_date) = '"+year+"'";
		}
		if (StringUtils.isNotBlank(productId)) {
			sql += " AND b.id = '"+productId+"'";
		}
		sql += " GROUP BY b.product_category_name,a.product_id ORDER BY IFNULL(SUM(c.acreage),0) desc " ;
		return productLibraryTreeDao.findBySql(sql);	
	}
	
	

	/**
	 * 获取品种对应的批次列表
	 * @return
	 */
	public List<Object> getBatchList(String id, String productId){
		String sql = "SELECT"
				+ " a.id,"
				+ " a.batch_code,"
				+ " a.batch_start_date,"
				+ " a.`status`,"
				+ " c.`name`,"
				+ " u.`NAME`"
				+ " FROM"
				+ " t_agro_production_batch a,"
				+ " t_agro_base_tree c,"
				+ " sys_office o,"
				+ " sys_user u"
				+ " WHERE"
				+ " a.base_id = c.id"
				+ " AND o.id = c.office_id"
				+ " AND a.user_id = u.id"
				+ " AND a.states <> 'D'"
				+" AND  (o.id LIKE '"+id+"' OR o.PARENT_IDS LIKE '%,"+id+",%')"
				+ " AND product_id = '"+productId+"'";
		return productLibraryTreeDao.findBySql(sql);	
	}
	/**
	 * 获取企业的批次各个状态的数量数据
	 * @return
	 */
	public List<Object> getBatchCount(String id){
		String sql = "SELECT"
				+ " IFNULL(sum(IF(a. STATUS = '1', 0, 1)),0) AS 'complete',"
				+ " IFNULL(sum(IF(YEAR(a.batch_plan_harvest_date)=YEAR(now()),1,0)),0) AS 'thisYearComplete',"
				+ " IFNULL(sum(IF(a. STATUS = '1', 1, 0)),0) AS 'planting',"
				+ " IFNULL(sum(IF(DATE_FORMAT(a.batch_plan_harvest_date,'%Y%m')=DATE_FORMAT(CURDATE(), '%Y%m'),1,0)),0) AS 'thisMonthHarvest'"
				+ " FROM"
				+ " t_agro_production_batch a,"
				+ " t_agro_base_tree c,"
				+ " sys_office o"
				+ " WHERE"
				+ " a.base_id = c.id"
				+ " AND o.id = c.office_id"
				+ " AND a.states <> 'D'"
				+ " AND  (o.id LIKE '"+id+"' OR o.PARENT_IDS LIKE '%,"+id+",%')";
		return productLibraryTreeDao.findBySql(sql);	
	}
	/**
	 * 获取每月新加基地数
	 * @return
	 */
	public List<Object> getMonthCompanyCount(String id){
		String sql = "SELECT b.createtime,COUNT(b.name) FROM (SELECT "
			+" a.name,DATE_FORMAT(a.create_time, '%Y-%m') createtime"
			+" FROM"
			+"   t_agro_base_tree a "
			+" WHERE a.states <> 'D' and  FIND_IN_SET (a.office_id, getChildOfficeLst2('"+id+"'))) b"
			+" GROUP BY b.createtime order by b.createtime";
		return productLibraryTreeDao.findBySql(sql);	
	}
	
	/**
	 * 获取品种对应的专家人数
	 * @return
	 */
	public List<Object> getProductExpertCount(String code){
		String sql = "SELECT d.product_category_name,COUNT(d.zjid) FROM (SELECT"
			+"   a.id zjid,a.expert_name,c.id,c.product_category_name"
			+" FROM"
			+"   t_agro_experts a,"
			+"   t_agro_experts_professionalfield_relation b,"
			+"   t_agro_product_library_tree c, "
			+"   sys_user e "
			+" WHERE a.id = b.experts_id "
			+"   AND b.product_library_id = c.id "
			+"   AND a.states <> 'D' "
			+"   AND b.states <> 'D' "
			+"   AND a.user_id=e.id AND e.DEL_FLAG='0'";
		if(StringUtils.isNotBlank(code)){
			sql+=" AND a.platform LIKE '%"+code+"%' ";
		}
		sql+=" AND c.states <> 'D' ) d GROUP BY d.id,d.product_category_name ORDER BY d.product_category_name";
		return productLibraryTreeDao.findBySql(sql);	
	}
	// 全区图形报表接口
	public Map getChartYield(String officeId, String year) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		// 返回图标的各种元素集合
		Set<Object> columns = new LinkedHashSet<Object>();
		// 要折现的元素集合
		List<LinkedHashMap<String, Object>> listMap = new ArrayList<LinkedHashMap<String, Object>>();
		columns.add("categoryName");
		columns.add("recoverySum");
		List<ProductLibraryTree> listCategory = productLibraryTreeDao.findProductCategoryList("1","1");
		for(ProductLibraryTree product:listCategory){
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			Object recoverySum = productLibraryTreeDao.getChartYield(officeId,product.getId(),year,null,null,null);
			map.put("categoryName", product.getProductCategoryName());
			map.put("recoverySum", recoverySum);
			listMap.add(map);
		}
		reqMap.put("columns", columns);
		reqMap.put("rows", listMap);
		return reqMap;
	}

	// 全区图形报表接口
	public Map getYieldQuarter(String officeId, String year) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		// 返回图标的各种元素集合
		Set<Object> columns = new LinkedHashSet<Object>();
		// 要折现的元素集合
		List<LinkedHashMap<String, Object>> listMap = new ArrayList<LinkedHashMap<String, Object>>();
		columns.add("time");
		List<ProductLibraryTree> listCategory = productLibraryTreeDao.findProductCategoryList("1","1");
		for(ProductLibraryTree product:listCategory){
			columns.add(product.getProductCategoryName());
			// 4个季度
			for(int j = 1;j<=4;j++){
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				// 如果listMap大小为12则代表已经插入过数据，需要获取已经插入的数据追加元素数据。反之直接put数据
				if(listMap.size()==4){
					map=listMap.get(j-1);
				} else {
					map.put("time", "第"+j+"季度");
				}
				Object recoverySum = productLibraryTreeDao.getChartYield(officeId,product.getId(),year,String.valueOf(j),null,null);
				map.put(product.getProductCategoryName(), recoverySum);
				if(listMap.size()==4){
					listMap.set(j-1,map);
				} else{
					listMap.add(map);
				}
			}
		}
		reqMap.put("columns", columns);
		reqMap.put("rows", listMap);
		return reqMap;
	}
	// 全区图形报表接口
	public Map getYieldMonth(String officeId, String year) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		// 返回图标的各种元素集合
		Set<Object> columns = new LinkedHashSet<Object>();
		// 要折现的元素集合
		List<LinkedHashMap<String, Object>> listMap = new ArrayList<LinkedHashMap<String, Object>>();
		columns.add("time");
		List<ProductLibraryTree> listCategory = productLibraryTreeDao.findProductCategoryList("1","1");
		for(ProductLibraryTree product:listCategory){
			columns.add(product.getProductCategoryName());
			// 12个月
			for(int j = 1;j<=12;j++){
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				// 如果listMap大小为12则代表已经插入过数据，需要获取已经插入的数据追加元素数据。反之直接put数据
				if(listMap.size()==12){
					map=listMap.get(j-1);
				} else {
					map.put("time", j+"月");
				}
				Object recoverySum = productLibraryTreeDao.getChartYield(officeId,product.getId(),year,null,String.valueOf(j),null);
				map.put(product.getProductCategoryName(), recoverySum);
				if(listMap.size()==12){
					listMap.set(j-1,map);
				} else{
					listMap.add(map);
				}
			}
		}
		reqMap.put("columns", columns);
		reqMap.put("rows", listMap);
		return reqMap;
	}

	// 全区图形报表接口
	public Map getYieldWeek(String officeId, String year) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		// 返回图标的各种元素集合
		Set<Object> columns = new LinkedHashSet<Object>();
		// 要折现的元素集合
		List<LinkedHashMap<String, Object>> listMap = new ArrayList<LinkedHashMap<String, Object>>();
		columns.add("time");
		List<ProductLibraryTree> listCategory = productLibraryTreeDao.findProductCategoryList("1","1");
		for(ProductLibraryTree product:listCategory){
			columns.add(product.getProductCategoryName());
			// 51个星期
			List<Object> recoverySum = productLibraryTreeDao.getChartWeek(officeId,product.getId(),year);
			for(int j = 1;j<=51;j++){
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				// 如果listMap大小为51则代表已经插入过数据，需要获取已经插入的数据追加元素数据。反之直接put数据
				if(listMap.size()==51){
					map=listMap.get(j-1);
				} else {
					map.put("time", j+"周");
				}
				map.put(product.getProductCategoryName(), "0");
				if(listMap.size()==51){
					listMap.set(j-1,map);
				} else{
					listMap.add(map);
				}
			}
			for(int i = 0; i<recoverySum.size();i++){
				Object[] obj = (Object[]) recoverySum.get(i);
				LinkedHashMap<String, Object> map = listMap.get(Integer.valueOf(obj[0].toString()));
				map.put(product.getProductCategoryName(), obj[1].toString());
				listMap.set(Integer.valueOf(obj[0].toString()),map);
			}
		}
		reqMap.put("columns", columns);
		reqMap.put("rows", listMap);
		return reqMap;
	}

	// 全区图形报表接口节气
	public Map getYieldSolarTerms(String officeId, String year) throws ParseException {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		// 返回图标的各种元素集合
		Set<Object> columns = new LinkedHashSet<Object>();
		// 要折现的元素集合
		List<LinkedHashMap<String, Object>> listMap = new ArrayList<LinkedHashMap<String, Object>>();
		columns.add("time");
		List<ProductLibraryTree> listCategory = productLibraryTreeDao.findProductCategoryList("1","1");
		for(ProductLibraryTree product:listCategory){
			columns.add(product.getProductCategoryName());
			// 24节气
			List<Object> recoverySum = productLibraryTreeDao.getYieldSolarTerms(officeId,product.getId(),year);
			List<String> mSolarName = SolarTerms_24.getMSolarName(Integer.valueOf(year));
			for(int j = 1;j<=mSolarName.size();j++){
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				// 如果listMap大小为51则代表已经插入过数据，需要获取已经插入的数据追加元素数据。反之直接put数据
				if(listMap.size()==mSolarName.size()){
					map=listMap.get(j-1);
				} else {
					map.put("time", mSolarName.get(j-1));
				}
				map.put(product.getProductCategoryName(), "0");
				if(listMap.size()==mSolarName.size()){
					listMap.set(j-1,map);
				} else{
					listMap.add(map);
				}
			}
			for(int i = 0; i<recoverySum.size();i++){
				int index = 0;
				Object[] obj = (Object[]) recoverySum.get(i);
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				DateFormat format2 = new SimpleDateFormat("MM");
				Date data = format1.parse(obj[0].toString());
				String solarTerms = "";
				if(StringUtils.isBlank(solarTerms)){
					for(int h=0;;h--){
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(data);
						calendar.add(calendar.DATE, h);
						String date = String.valueOf(format2.format(calendar.getTime()))+String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
						solarTerms = SolarTerms_24.getSolatName(Integer.valueOf(year), date);
						if(StringUtils.isNotBlank(solarTerms)){
							break;
						}
					}
				}
				index = mSolarName.indexOf(solarTerms);
				LinkedHashMap<String, Object> map = listMap.get(index);
				BigDecimal oldscl = new BigDecimal(map.get(product.getProductCategoryName()).toString());
				map.put(product.getProductCategoryName(), oldscl.add(new BigDecimal(obj[1].toString())));
				listMap.set(index,map);
			}
		}
		reqMap.put("columns", columns);
		reqMap.put("rows", listMap);
		return reqMap;
	}

	// 年度集合
	public List<Object> getYears(String officeId) {
		// 年度集合
		return productLibraryTreeDao.getYears(officeId);
	}
	/**
     * 实体对象转成Map
     *
     * @param obj 实体对象
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
                if(field.get(obj)!=null){
                    map.put(field.getName(), field.get(obj).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
	/**
	 * 专家id查询领域信息
	 * @param expertsId
	 * @return
	 */
	public List<Map<String,Object>> findProductLibraryList(User user){
		String code = "wangcheng";
		String sql = "select a.platform from t_agro_video a where a.user_code=:p1 and a.states<>'D'";
		List<String> list = expertsDao.findBySql(sql,new Parameter(user.getLoginName()));
		if(list!=null && list.size()>0){
			code = list.get(0);
			if(code.contains(",")){
				code = code.split(",")[0];
			}
		}
		
		sql = "SELECT aa.product_library_id,aa.product_category_name FROM ("
			+" SELECT a.expert_name,b.product_library_id,e.product_category_name,d.platform "
			+" FROM t_agro_experts a,t_agro_experts_professionalfield_relation b,sys_user c,t_agro_video d,t_agro_product_library_tree e "
			+" WHERE a.id = b.experts_id AND a.user_id=c.ID AND c.LOGIN_NAME = d.user_code AND b.product_library_id = e.id"
			+" AND a.states<>'D' AND c.DEL_FLAG='0' AND d.states<>'D' AND e.states<>'D' AND d.platform LIKE '%" + code + "%') aa"
			+" GROUP BY aa.product_library_id,aa.product_category_name ORDER BY aa.product_category_name";
		return productLibraryTreeDao.findBySql(sql.toString(), new Parameter(), Map.class);	
	}
	
}
