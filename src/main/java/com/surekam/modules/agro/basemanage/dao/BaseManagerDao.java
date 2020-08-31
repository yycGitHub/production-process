package com.surekam.modules.agro.basemanage.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.basemanage.entity.BaseManager;
import com.surekam.modules.agro.basemanage.entity.CopyOfBaseManager;

/**
 * 基地管理DAO接口
 * 
 * @author tangjun
 * @version 2019-04-09
 */
@Repository
public class BaseManagerDao extends BaseDao<BaseManager> {

	public List<BaseManager> getBaseManagerList(String id) {
		String sql = "select * from t_agro_base_manager a where a.t_base_id =:p1 ";
		return findBySql(sql, new Parameter(id), BaseManager.class);
	}

	public List<CopyOfBaseManager> getCopyBaseManagerList(String id) {
		String sql = "select * from t_agro_base_manager a where a.t_base_id =:p1 ";
		return findBySql(sql, new Parameter(id), CopyOfBaseManager.class);
	}

	public BaseManager getUserId(String baseId, String roleId) {
		String sql = "select * from t_agro_base_manager a where a.t_base_id =:p1 and a.role_id =:p2 ";
		List<BaseManager> findBySql = findBySql(sql, new Parameter(baseId, roleId), BaseManager.class);
		return findBySql.get(0);
	}

	public List<BaseManager> getUserIdAndBaseIdAndRoleId(String baseId, String userId, String roleId) {
		String sql = "select * from t_agro_base_manager a where a.t_base_id =:p1 and a.user_id =:p2 and a.role_id =:p3 ";
		List<BaseManager> findBySql = findBySql(sql, new Parameter(baseId, userId, roleId), BaseManager.class);
		return findBySql;
	}

	public List<BaseManager> getByUserIdList(String userId) {
		String sql = "select a.* from t_agro_base_manager a,t_agro_base_tree t where t.id = a.t_base_id and t.states <> 'D' and a.user_id =:p1 and a.role_id = '1852c8e247744ff184e8c162eff44f4c'";
		return findBySql(sql, new Parameter(userId), BaseManager.class);
	}

	public int updateBaseManager(String updateUserId, String tBaseId, String userId) {
		String sql = "update BaseManager set userId = '" + updateUserId + "' where TBaseId = '" + tBaseId + "' and userId = '" + userId + "'";
		return update(sql);
	}

	public int deleteBaseManager(String tBaseId) {
		return updateBySql("delete from t_agro_base_manager where t_base_id =:p1", new Parameter(tBaseId));
	}

	public int insert(BaseManager baseManager) {
		return updateBySql("insert into t_agro_base_manager(t_base_id, user_id, role_id) value(:p1,:p2,:p3) ", new Parameter(baseManager.getTBaseId(), baseManager.getUserId(), baseManager.getRoleId()));
	}
}
