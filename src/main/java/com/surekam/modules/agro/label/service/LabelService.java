package com.surekam.modules.agro.label.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.label.entity.Label;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.agro.label.dao.LabelDao;

/**
 * 标签表Service
 * @author liwei
 * @version 2019-04-27
 */
@Component
@Transactional(readOnly = true)
public class LabelService extends BaseService {

	@Autowired
	private LabelDao labelDao;
	
	public Label get(String id) {
		return labelDao.get(id);
	}
	
	public Page<Label> find(Page<Label> page, Label label) {
		DetachedCriteria dc = labelDao.createDetachedCriteria();
		dc.add(Restrictions.eq(Label.FIELD_DEL_FLAG, Label.DEL_FLAG_NORMAL));
		return labelDao.find(page, dc);
	}
	
	public List<Label> getLabelList(User user) {
		String hql = " from Label a where (a.userId = '0' or a.userId = :p1) and a.labelType='1' and a.states <> 'D'";
		return labelDao.find(hql,new Parameter(user.getId()));
	}
	
	public boolean exsitLabelContent(String reason,User user) {
		String hql = " from Label a where (a.userId = '0' or a.userId = :p1) and a.labelContent=:p2 and a.labelType='1' and a.states <> 'D'";
		List<Label> list = labelDao.find(hql,new Parameter(user.getId(),reason));
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	@Transactional(readOnly = false)
	public void save(Label label) {
		labelDao.save(label);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		labelDao.deleteById(id);
	}
	
}
