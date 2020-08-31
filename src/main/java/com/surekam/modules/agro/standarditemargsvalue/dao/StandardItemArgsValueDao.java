package com.surekam.modules.agro.standarditemargsvalue.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.standarditemargs.entity.StandardItemArgs;
import com.surekam.modules.agro.standarditemargsvalue.entity.StandardItemArgsValue;
import com.surekam.modules.sys.entity.User;

/**
 * 标准作业参数多项值表DAO接口
 * 
 * @author liwei
 * @version 2019-04-26
 */
@Repository
public class StandardItemArgsValueDao extends BaseDao<StandardItemArgsValue> {
	@Transactional(readOnly = false)
	public void save(StandardItemArgs standardItemArgs, User user) {
		String hql = "from StandardItemArgsValue a where a.itemArgsId=:p1 and a.name=:p2 and a.states<>'D'";
		List<StandardItemArgsValue> list = find(hql, new Parameter(standardItemArgs.getId(), standardItemArgs.getDefaultValue()));
		if (list.size() == 0) {
			StandardItemArgsValue standardItemArgsValue = new StandardItemArgsValue();
			standardItemArgsValue.setName(standardItemArgs.getDefaultValue());
			standardItemArgsValue.setValue(standardItemArgs.getDefaultValue());
			standardItemArgsValue.setCreateUserId(user.getId());
			standardItemArgsValue.setItemArgsId(standardItemArgs.getId());
			standardItemArgsValue.setSort(Double.valueOf(getMaxSort(standardItemArgs.getId())).intValue() + "");
			save(standardItemArgsValue);
		}
	}

	private String getMaxSort(String argsId) {
		String sql = "select (ifnull(max(a.sort),0)+1) sort from t_agro_standard_item_args_value a where a.item_args_id=:p1 and a.states<>'D'";
		List<String> list = findBySql(sql, new Parameter(argsId));
		return String.valueOf(list.get(0));
	}

	public List<StandardItemArgsValue> findByItemArgsIdAndName(String itemArgsId, String name) {
		String sql = "select * from t_agro_standard_item_args_value a where a.item_args_id =:p1 and a.name =:p2 and a.states <> 'D'";
		List<StandardItemArgsValue> list = findBySql(sql, new Parameter(itemArgsId, name), StandardItemArgsValue.class);
		return list;
	}
	
	public List<StandardItemArgsValue> findByItemArgsId(String itemArgsId) {
		String sql = "select * from t_agro_standard_item_args_value a where a.item_args_id =:p1 and a.states <> 'D'";
		List<StandardItemArgsValue> list = findBySql(sql, new Parameter(itemArgsId), StandardItemArgsValue.class);
		return list;
	}

	// 通过标准id查询采收品种
	public List<Object> findBystandardsId(String standardsId) {
		String sql = "SELECT"
				+ " v.name"
				+ " FROM"
				+ " t_agro_system_enterprise_standards s"
				+ " LEFT JOIN t_agro_standard_items i ON s.id = i.system_standards_id"
				+ " LEFT JOIN t_agro_standard_item_category c ON i.item_category_id=c.id"
				+ " LEFT JOIN t_agro_standard_item_args a ON a.items_id=i.id"
				+ " LEFT JOIN t_agro_standard_item_args_value v ON a.id=v.item_args_id"
				+ " WHERE c.task_item_category_name like '采收'"
				+ " AND a.args_name like '采收类型'"
				+ " AND v.states <> 'D'"
				+ " AND s.states <> 'D'"
				+ " AND i.states <> 'D'"
				+ " AND c.states <> 'D'"
				+ " AND a.states <> 'D'"
				+ " AND s.id = :p1"
				+ " ORDER BY v.sort";
		List<Object> list = findBySql(sql, new Parameter(standardsId));
		return list;
	}
}
