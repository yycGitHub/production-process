package com.surekam.modules.agro.standarditemargs.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.modules.agro.standarditemargs.entity.StandardItemArgs;

/**
 * 标准作业详细参数表DAO接口
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Repository
public class StandardItemArgsDao extends BaseDao<StandardItemArgs> {

	public List<Object> findByDistinct() {
		String sql = "select max(a.id) as id, a.args_name as name from t_agro_standard_item_args a where a.states <> 'D' group by a.args_name ";
		List<Object> findBySql = findBySql(sql);
		return findBySql;
	}
	
	public List<BigInteger> countItemsId(String itemsId, String argsName) {
		String sql = "select count(a.items_id) as items_id from t_agro_standard_item_args a where a.items_id = '" + itemsId + "' and a.args_name = '" + argsName + "' and a.states <> 'D'";
		List<BigInteger> findBySql = findBySql(sql);
		return findBySql;
	}
	
	public List<BigInteger> findByItemsId(String itemsId) {
		String sql = "select * from t_agro_standard_item_args a where a.items_id = '" + itemsId + "' and a.states <> 'D' and a.sort <> '30' and a.sort <> '31'";
		List<BigInteger> findBySql = findBySql(sql);
		return findBySql;
	}
	
	public List<StandardItemArgs> findByItemsIdList(String itemsId) {
		String sql = "select * from t_agro_standard_item_args a where a.items_id = '" + itemsId + "' and a.states <> 'D'";
		List<StandardItemArgs> findBySql = findBySql(sql, null, StandardItemArgs.class);
		return findBySql;
	}
	
}
