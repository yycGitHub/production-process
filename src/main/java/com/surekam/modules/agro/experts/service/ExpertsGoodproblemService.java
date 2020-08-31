package com.surekam.modules.agro.experts.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.experts.dao.ExpertsDao;
import com.surekam.modules.agro.experts.dao.ExpertsGoodproblemDao;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.entity.ExpertsGoodproblem;


/**
 * 专家擅长问题Service
 * 
 * @author xy
 * @version 2019-04-16
 */
@Component
@Transactional(readOnly = true)
public class ExpertsGoodproblemService extends BaseService {

	@Autowired
	private ExpertsGoodproblemDao expertsGoodproblemDao;

	@Autowired
	private ExpertsDao expertsDao;

	public ExpertsGoodproblem get(String id) {
		return expertsGoodproblemDao.get(id);
	}

	public Page<ExpertsGoodproblem> find(Page<ExpertsGoodproblem> page, ExpertsGoodproblem expertsGoodproblem) {
		DetachedCriteria dc = expertsGoodproblemDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ExpertsGoodproblem.FIELD_DEL_FLAG, ExpertsGoodproblem.DEL_FLAG_NORMAL));
		return expertsGoodproblemDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(ExpertsGoodproblem expertsGoodproblem) {
		expertsGoodproblemDao.save(expertsGoodproblem);
	}
	
	public Experts getExpertByUserId(String userId) {
		DetachedCriteria dc = expertsDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Experts.FIELD_DEL_FLAG_XGXT, Experts.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("userId", userId));
		List<Experts> list = expertsDao.find(dc);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return new Experts();
		}
	}
	
	/**
	 * app 新增保存擅长问题
	 * @param userid
	 * @param content
	 */
	@Transactional(readOnly = false)
	public void savaGoodAtProblems(String userid,String  content) {
		Experts experts = getExpertByUserId(userid);
		ExpertsGoodproblem expertsGoodproblem = new ExpertsGoodproblem();
		expertsGoodproblem.setExpertId(experts.getId());
		expertsGoodproblem.setGoodProblem(content);
		expertsGoodproblem.setCreateTime(new Date());
		expertsGoodproblem.setCreateUserId(userid);
		expertsGoodproblemDao.save(expertsGoodproblem);
	}
	
	/**
	 * app 编辑保存擅长问题
	 * @param id
	 * @param content
	 */
	@Transactional(readOnly = false)
	public void editSavaGoodAtProblems(String id,String  content) {
		ExpertsGoodproblem expertsGoodproblem = expertsGoodproblemDao.get(id);
		expertsGoodproblem.setGoodProblem(content);
		expertsGoodproblem.setUpdateTime(new Date());
		expertsGoodproblem.setUpdateUserId(expertsGoodproblem.getCreateUserId());
		expertsGoodproblemDao.save(expertsGoodproblem);
	}
	
	/**
	 * app 删除擅长问题
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void delGoodAtProblems(String id) {
		expertsGoodproblemDao.deleteByXGXTId(id);
	}

	@Transactional(readOnly = false)
	public void delete(String id) {
		expertsGoodproblemDao.deleteById(id);
	}

	/**
	 * 专家id查询擅长问题信息
	 * 
	 * @param expertsId
	 * @return
	 */
	public List<ExpertsGoodproblem> getList(String expertsId) {
		StringBuffer sql = new StringBuffer(200);
		sql.append("SELECT distinct g.* FROM t_agro_experts_goodproblem g,t_agro_experts e WHERE g.expert_id = e.id ");
		if (StringUtils.isNotBlank(expertsId)) {
			sql.append(" and e.id ='" + expertsId + "' ");
		}
		sql.append(" and g.states <>'D' ");
		return expertsGoodproblemDao.findBySql2(sql.toString(), new Parameter(), ExpertsGoodproblem.class);
	}

	public List<ExpertsGoodproblem> getList() {
		List<ExpertsGoodproblem> findAll = expertsGoodproblemDao.findAll();
		return findAll;
	}

	public List<String> getExpertsGoodAtProblemsList() {
		List<String> arratList = new ArrayList<String>();
		List<Experts> expertsList = expertsDao.findAll();
		for (Experts experts : expertsList) {
			List<ExpertsGoodproblem> expertsGoodproblemList = expertsGoodproblemDao.getExpertsGoodproblemList(experts.getId());
			if(expertsGoodproblemList !=null && expertsGoodproblemList.size() >0) {
				String strs = "";
				for (ExpertsGoodproblem expertsGoodproblem : expertsGoodproblemList) {
					strs += expertsGoodproblem.getGoodProblem() + " ";
				}
				String substring = strs.substring(0, strs.length() - 1);
				arratList.add(substring);
			}
		}
		return arratList;
	}
	
	/**
	 * app 查询专家擅长问题列表
	 * @param userid
	 * @return
	 */
	public List<ExpertsGoodproblem> getListExpertsGoodproblem(String userid) {
		Experts experts = getExpertByUserId(userid);
		DetachedCriteria dc = expertsGoodproblemDao.createDetachedCriteria();
		dc.add(Restrictions.ne(ExpertsGoodproblem.FIELD_DEL_FLAG_XGXT, ExpertsGoodproblem.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("expertId", experts.getId()));
		dc.addOrder(Order.desc("createTime"));
		return expertsGoodproblemDao.find(dc);
	}

}
