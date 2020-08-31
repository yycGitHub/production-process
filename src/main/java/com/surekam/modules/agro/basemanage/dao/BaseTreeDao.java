package com.surekam.modules.agro.basemanage.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.utils.StaticStringUtils;

@Repository
public class BaseTreeDao extends BaseDao<BaseTree> {

	public List<BaseTree> queryOfficeList(String id) {
		String qlString = "SELECT t.* FROM t_agro_base_tree t WHERE " + "t.office_id IN (SELECT s.id FROM sys_office s " + "WHERE (s.parent_ids LIKE '%," + id + ",%' " + "OR s.id = '" + id + "') AND s.DEL_FLAG != '1' ) AND t.states != 'D'";
		return findBySql(qlString, null, BaseTree.class);
	}
	
	public String queryLikeOffice(String id) {
		String treeStr = "";
		String sql = "SELECT t.* FROM t_agro_base_tree t WHERE t.office_id IN (SELECT s.id FROM sys_office s WHERE (s.parent_ids LIKE '%," + id + ",%' " + "OR s.id = '" + id + "') AND s.DEL_FLAG != '1' ) AND t.states != 'D'";
		List<BaseTree> findBySql = findBySql(sql, null, BaseTree.class);
		for (BaseTree baseTree : findBySql) {
			treeStr += "'" + baseTree.getId() + "'" + ",";
		}
		if(StringUtils.isNotBlank(treeStr)) {
			treeStr = treeStr.substring(0, treeStr.length() - 1);
		}
		return treeStr;
	}
	
	public List<BaseTree> queryOfficeListBatch(String officeId) {
		String sql = "select * from t_agro_base_tree a where a.office_id in (SELECT t.id FROM sys_office t WHERE (t.PARENT_IDS like '%,"+officeId+",%' OR t.id like '"+officeId+"') AND t.DEL_FLAG != '1')";
		sql += "and a.states <> 'D'";
		List<BaseTree> findBySql = findBySql(sql, null, BaseTree.class);
		return findBySql;
	}

	public List<BaseTree> findByBranchNameQuery(String id, String name) {
		return find("from BaseTree a where a.name=:p1 and a.officeId =:p2", new Parameter(name, id));
	}
	
	public List<BaseTree> getByOfficeId(String officeId) {
		String sql = "select * from t_agro_base_tree a where a.office_id ='" + officeId + "' and a.parent_id = '0' and a.states <> 'D' order by a.create_time asc";
		List<BaseTree> findBySql = findBySql(sql, null, BaseTree.class);
		return findBySql;
	}
	
	public List<BaseTree> getByOfficeIdAndUserId(String officeId, String userId) {
		String sql = "select distinct a.* from t_agro_base_tree a inner join t_agro_base_manager b on a.id = b.t_base_id where a.office_id =:p1 and b.user_id =:p2 and a.parent_id = '0' and a.states <> 'D' order by a.create_time asc";
		List<BaseTree> findBySql = findBySql(sql, new Parameter(officeId, userId), BaseTree.class);
		return findBySql;
	}
	
	public List<BaseTree> getLikeParentIds(String parentIds) {
		String sql = "SELECT * FROM t_agro_base_tree a where a.parent_ids like '" + parentIds + "%' and a.states <> 'D' ORDER BY a.create_time asc";
		List<BaseTree> findBySql = findBySql(sql, null, BaseTree.class);
		return findBySql;
	}

	public BaseTree getParentName(String parentId) {
		String sql = "select * from BaseTree a where a.parentId =:p1";
		List<BaseTree> find = find(sql, new Parameter(parentId));
		if (find.isEmpty()) {
			return null;
		} else {
			return find.get(0);
		}
	}

	public List<BaseTree> findAgroBaseList(User user) {
		String sql = "SELECT t.* FROM t_agro_base_tree t,t_agro_base_manager m where t.id=m.t_base_id and t.office_id = :p1 AND m.user_id=:p2 AND m.role_id=:p3 and t.states<>'D' AND t.parent_id = '0' ORDER BY t.sort";
		List<BaseTree> list = findBySql(sql, new Parameter(user.getOffice().getId(), user.getId(), StaticStringUtils.AGRO_FZR), BaseTree.class);
		return list;
	}

	public String findAgroBaseCount(User user) {
		String sql = "select count(t.id) from t_agro_base_tree t,t_agro_base_manager m where t.id=m.t_base_id and t.office_id = :p1 AND m.user_id=:p2 AND m.role_id=:p3 and t.states<>'D' and t.parent_id = '0'";
		List<Integer> list = findBySql(sql, new Parameter(user.getOffice().getId(), user.getId(), StaticStringUtils.AGRO_FZR));
		if (list.size() > 0) {
			return list.get(0) + "";
		}
		return "0";
	}

	public List<Object> getBaseIdList(String officeid) {
		String qlString = "SELECT t.id FROM t_agro_base_tree t WHERE " + "t.office_id IN (SELECT s.id FROM sys_office s " + "WHERE (s.parent_ids LIKE '%," + officeid + ",%' " + "OR s.id LIKE '" + officeid + "') AND s.DEL_FLAG != '1' ) AND t.states != 'D'";
		return findBySql(qlString);
	}

	public List<Object> getBaseIdByUserId(String userId) {
		String qlString = "SELECT m.t_base_id FROM t_agro_base_manager m " + "WHERE m.user_id LIKE '" + userId + "'";
		return findBySql(qlString);
	}
	
	public Page<Map<String, String>> findByOfficeId(String officeId, int no, int size) {
		Page<Map<String,String>> page = new Page<Map<String,String>>(no, size);
		String sql = "select a.id as baseId ,a.name as baseName, a.office_id as officeId from t_agro_base_tree a where a.office_id in " + officeId + " and a.states <> 'D' ";
		Page<Map<String,String>> findPage = findBySql(page, sql, Map.class);
		return findPage;
	}
	
	public List<BaseTree> findByInOfficeId(String officeId) {
		String sql = "select * from t_agro_base_tree a where a.office_id in " + officeId + " and a.states <> 'D' ";
		List<BaseTree> findBySql = findBySql(sql, null, BaseTree.class);
		return findBySql;
	}
	
	public List<BaseTree> all(){
		String sql = "select * from t_agro_base_tree a where a.states <> 'D'";
		List<BaseTree> findBySql = findBySql(sql, null, BaseTree.class);
		return findBySql;
	}
	
	public BaseTree findById(String id) {
		String sql = "select * from t_agro_base_tree a where a.id =:p1 and a.states <> 'D'";
		List<BaseTree> findBySql = findBySql(sql, new Parameter(id), BaseTree.class);
		if (!findBySql.isEmpty()) {
			return findBySql.get(0);
		}
		return null;
	}

}
