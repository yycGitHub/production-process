package com.surekam.modules.agro.product.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;

/**
 * 品种分类DAO接口
 * @author lb
 * @version 2019-04-10
 */
@Repository
public class ProductLibraryTreeDao extends BaseDao<ProductLibraryTree> {
	public ProductLibraryTree findByIdAndParentId(String id, String parentId) {
		String sql = "select * from t_agro_product_library_tree a where a.id ='" + id + "' and a.parent_id = '" + parentId + "' and a.states !='D' ";
		List<Object> findBySql = findBySql(sql, null, ProductLibraryTree.class);
		return (ProductLibraryTree) findBySql.get(0);
	}

	public ProductLibraryTree findByIdAndIsProductCategory(String id, String isProductCategory) {
		String sql = "select * from t_agro_product_library_tree a where a.id ='" + id + "' and a.is_product_category = '" + isProductCategory + "' and a.states !='D' ";
		List<Object> findBySql = findBySql(sql, null, ProductLibraryTree.class);
		if (null != findBySql && findBySql.size() > 0) {
			return (ProductLibraryTree) findBySql.get(0);
		} else {
			return null;
		}
	}

	public List<ProductLibraryTree> findByParentsIdsAndIsProductCategory(String parentsIds, String isProductCategory) {
		String sql = "select * from t_agro_product_library_tree a where a.parents_ids like '%," + parentsIds + ",%' and a.is_product_category = '" + isProductCategory + "' and a.states !='D' ";
		List<ProductLibraryTree> findBySql = findBySql(sql, null, ProductLibraryTree.class);
		return findBySql;
	}
	
	public List<ProductLibraryTree> findByParentsIdsAndIsProductCategoryAndOfficeId(String parentsIds, String isProductCategory, String officeId) {
		String sql = "select * from t_agro_product_library_tree a inner join t_agro_product_library_relation b on a.id = b.parent_id where a.is_product_category = '"
				+ isProductCategory + "' and a.parents_ids like '%," + parentsIds + ",%' and (b.office_id ='" + officeId + "' or b.office_ids like '%," + officeId + ",%') and a.states !='D' ";
		List<ProductLibraryTree> findBySql = findBySql(sql, null, ProductLibraryTree.class);
		return findBySql;
	}
	
	public List<ProductLibraryTree> findByDistinct(String parentsIds, String isProductCategory, String officeId) {
		String sql = "select distinct a.* from t_agro_product_library_tree a inner join t_agro_product_library_relation b on a.id = b.parent_id where a.is_product_category = '"
				+ isProductCategory + "' and a.parents_ids like '%," + parentsIds + ",%' and (b.office_id ='" + officeId + "' or b.office_ids like '%," + officeId + ",%') and a.states !='D' ";
		List<ProductLibraryTree> findBySql = findBySql(sql, null, ProductLibraryTree.class);
		return findBySql;
	}

	public List<ProductLibraryTree> findProductCategoryList(String parentsId, String isProductCategory) {
		String sql = "select * from t_agro_product_library_tree where parent_id like '" + parentsId + "' and is_product_category = '" + isProductCategory + "' and states !='D' ";
		List<ProductLibraryTree> findBySql = findBySql(sql, null, ProductLibraryTree.class);
		return findBySql;
	}
	
	public List<ProductLibraryTree> findByParentIdsLike(String parentsId) {
		return find("from ProductLibraryTree where parentsIds like :p1", new Parameter(parentsId));
	}
	
	public List<ProductLibraryTree> findByParent(String parentsId, String parentsIds, String officeId, String officeIds) {
		String sql = "select distinct a.* from t_agro_product_library_tree a inner join t_agro_product_library_relation b on a.id = b.parent_id "
				+ "where a.states <> 'D' and (b.parent_id ='" + parentsId + "' or b.parents_ids like '%" + parentsIds
				+ "%' ) and (b.office_id ='" + officeId + "' or b.office_ids like '%" + officeIds + "%')";
		List<ProductLibraryTree> findBySql = findBySql(sql, null, ProductLibraryTree.class);
		return findBySql;
	}
	
	
	public List<ProductLibraryTree> findByParent(String officeId, String officeIds) {
		String sql = "select distinct a.* from t_agro_product_library_tree a inner join t_agro_product_library_relation b on a.id = b.parent_id "
				+ "where a.states <> 'D' and (b.office_id ='" + officeId + "' or b.office_ids like '%" + officeIds + "%' )";
		List<ProductLibraryTree> findBySql = findBySql(sql, null, ProductLibraryTree.class);
		return findBySql;
	}

	public Object getChartYield(String officeId,String productId, String year,String quarter,String month,String week) {
		String sql = "SELECT"
				+ " IFNULL(SUM(dd1.task_item_args_value),0) 'csl'"
				+ " FROM"
				+ " ("
				+ " SELECT"
				+ " task_list_id,"
				+ " task_item_args_value"
				+ " FROM"
				+ " t_agro_standard_task_items_args_value"
				+ " WHERE"
				+ " args_name = '采收量'"
				+ " ) dd1"
				+ " LEFT JOIN ("
				+ " SELECT"
				+ " task_list_id,"
				+ " str_to_date(REPLACE (REPLACE (REPLACE (task_item_args_value,'年','-'),'月','-'),'日',''),'%Y-%m-%d') 'time'"
				+ " FROM"
				+ " t_agro_standard_task_items_args_value"
				+ " WHERE"
				+ " args_name = '日期'"
				+ " ) dd2 ON dd1.task_list_id = dd2.task_list_id"
				+ " LEFT JOIN t_agro_standard_task_list t ON dd1.task_list_id=t.id"
				+ " LEFT JOIN t_agro_production_batch b ON t.production_batch=b.id"
				+ " LEFT JOIN t_agro_product_library_tree lt ON lt.id=b.product_id"
				+ " LEFT JOIN t_agro_base_tree m ON b.base_id=m.id"
				+ " LEFT JOIN sys_office o ON m.office_id=o.id"
				+ " WHERE lt.parents_ids LIKE '%,"+productId+",%'";
		if(StringUtils.isNotBlank(officeId)){
			sql+=" AND (o.parent_ids like '%,"+officeId+",%' or o.id='"+officeId+"')";
		}
		if(StringUtils.isNotBlank(year)){
			sql+=" AND year(dd2.time) = '"+year+"'";
		}
		if(StringUtils.isNotBlank(quarter)){
			sql+=" AND QUARTER(dd2.time) = '"+quarter+"'";
		}
		if(StringUtils.isNotBlank(month)){
			sql+=" AND month(dd2.time) = '"+month+"'";
		}
		if(StringUtils.isNotBlank(week)){
			sql+=" AND week(dd2.time) = '"+week+"'";
		}
		List<Object> findBySql = findBySql(sql, null);
		return findBySql.get(0);
	}
	

	public List<Object> getChartWeek(String officeId,String productId, String year) {
		String sql = "SELECT"
				+ " week(dd2.time),"
				+ " IFNULL(SUM(dd1.task_item_args_value),0) 'csl'"
				+ " FROM"
				+ " ("
				+ " SELECT"
				+ " task_list_id,"
				+ " task_item_args_value"
				+ " FROM"
				+ " t_agro_standard_task_items_args_value"
				+ " WHERE"
				+ " args_name = '采收量'"
				+ " ) dd1"
				+ " LEFT JOIN ("
				+ " SELECT"
				+ " task_list_id,"
				+ " str_to_date(REPLACE (REPLACE (REPLACE (task_item_args_value,'年','-'),'月','-'),'日',''),'%Y-%m-%d') 'time'"
				+ " FROM"
				+ " t_agro_standard_task_items_args_value"
				+ " WHERE"
				+ " args_name = '日期'"
				+ " ) dd2 ON dd1.task_list_id = dd2.task_list_id"
				+ " LEFT JOIN t_agro_standard_task_list t ON dd1.task_list_id=t.id"
				+ " LEFT JOIN t_agro_production_batch b ON t.production_batch=b.id"
				+ " LEFT JOIN t_agro_product_library_tree lt ON lt.id=b.product_id"
				+ " LEFT JOIN t_agro_base_tree m ON b.base_id=m.id"
				+ " LEFT JOIN sys_office o ON m.office_id=o.id"
				+ " WHERE lt.parents_ids LIKE '%,"+productId+",%'";
		if(StringUtils.isNotBlank(officeId)){
			sql+=" AND (o.parent_ids like '%,"+officeId+",%' or o.id='"+officeId+"')";
		}
		if(StringUtils.isNotBlank(year)){
			sql+=" AND year(dd2.time) = '"+year+"'";
		}
		sql+=" GROUP BY week(dd2.time)";
		return findBySql(sql, null);
	}

	public List<Object> getYieldSolarTerms(String officeId,String productId, String year) {
		String sql = "SELECT"
				+ " dd2.time,"
				+ " IFNULL(SUM(dd1.task_item_args_value),0) 'csl'"
				+ " FROM"
				+ " ("
				+ " SELECT"
				+ " task_list_id,"
				+ " task_item_args_value"
				+ " FROM"
				+ " t_agro_standard_task_items_args_value"
				+ " WHERE"
				+ " args_name = '采收量'"
				+ " ) dd1"
				+ " LEFT JOIN ("
				+ " SELECT"
				+ " task_list_id,"
				+ " str_to_date(REPLACE (REPLACE (REPLACE (task_item_args_value,'年','-'),'月','-'),'日',''),'%Y-%m-%d') 'time'"
				+ " FROM"
				+ " t_agro_standard_task_items_args_value"
				+ " WHERE"
				+ " args_name = '日期'"
				+ " ) dd2 ON dd1.task_list_id = dd2.task_list_id"
				+ " LEFT JOIN t_agro_standard_task_list t ON dd1.task_list_id=t.id"
				+ " LEFT JOIN t_agro_production_batch b ON t.production_batch=b.id"
				+ " LEFT JOIN t_agro_product_library_tree lt ON lt.id=b.product_id"
				+ " LEFT JOIN t_agro_base_tree m ON b.base_id=m.id"
				+ " LEFT JOIN sys_office o ON m.office_id=o.id"
				+ " WHERE lt.parents_ids LIKE '%,"+productId+",%'";
		if(StringUtils.isNotBlank(officeId)){
			sql+=" AND (o.parent_ids like '%,"+officeId+",%' or o.id='"+officeId+"')";
		}
		if(StringUtils.isNotBlank(year)){
			sql+=" AND year(dd2.time) = '"+year+"'";
		}
		sql+=" GROUP BY dd2.time";
		return findBySql(sql, null);
	}

	public List<Object> getYears(String officeId) {
		String sql = "SELECT"
				+ " year(dd2.time)"
				+ " FROM"
				+ " ("
				+ " SELECT"
				+ " task_list_id,"
				+ " task_item_args_value"
				+ " FROM"
				+ " t_agro_standard_task_items_args_value"
				+ " WHERE"
				+ " args_name = '采收量'"
				+ " ) dd1"
				+ " LEFT JOIN ("
				+ " SELECT"
				+ " task_list_id,"
				+ " str_to_date(REPLACE (REPLACE (REPLACE (task_item_args_value,'年','-'),'月','-'),'日',''),'%Y-%m-%d') 'time'"
				+ " FROM"
				+ " t_agro_standard_task_items_args_value"
				+ " WHERE"
				+ " args_name = '日期'"
				+ " ) dd2 ON dd1.task_list_id = dd2.task_list_id"
				+ " LEFT JOIN t_agro_standard_task_list t ON dd1.task_list_id=t.id"
				+ " LEFT JOIN t_agro_production_batch b ON t.production_batch=b.id"
				+ " LEFT JOIN t_agro_product_library_tree lt ON lt.id=b.product_id"
				+ " LEFT JOIN t_agro_base_tree m ON b.base_id=m.id"
				+ " LEFT JOIN sys_office o ON m.office_id=o.id"
				+ " WHERE 1=1";
		if(StringUtils.isNotBlank(officeId)){
			sql+=" AND (o.parent_ids like '%,"+officeId+",%' or o.id='"+officeId+"')";
		}
		sql+=" GROUP BY year(dd2.time)";
		return findBySql(sql, null);
	}
	
	public List<ProductLibraryTree> findByParentIdAndProductcategoryName(String parentId, String productcategoryName) {
		String sql = "select * from t_agro_product_library_tree a where a.parent_id =:p1 and a.product_category_name =:p2 and states !='D' ";
		List<ProductLibraryTree> findBySql = findBySql(sql, new Parameter(parentId, productcategoryName), ProductLibraryTree.class);
		return findBySql;
	}
	
	public List<ProductLibraryTree> getlist(String id) {
		String sql = "select * from t_agro_product_library_tree a where a.parent_id like '%" + id + "%'  and a.states !='D' ";
		List<ProductLibraryTree> findBySql = findBySql(sql, null, ProductLibraryTree.class);
		return findBySql;
	}
	
	public List<ProductLibraryTree> findByInid(String id) {
		String sql = "select * from t_agro_product_library_tree a where a.id in (" + id + ") and a.id != '1' and a.states <> 'D' ";
		List<ProductLibraryTree> findBySql = findBySql(sql, null, ProductLibraryTree.class);
		return findBySql;
	}
	
	public List<ProductLibraryTree> findByInId(String id) {
		String sql = "select * from t_agro_product_library_tree a where a.id in (" + id + ") and a.states <> 'D' ";
		List<ProductLibraryTree> findBySql = findBySql(sql, null, ProductLibraryTree.class);
		return findBySql;
	}
}
