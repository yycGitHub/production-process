package com.surekam.modules.sample.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.modules.sample.dao.YXLsxxDao;
import com.surekam.modules.sample.entity.YXLsxx;


/**
 * 历史信息表Service
 * @author liw
 * @version 2018-10-31
 */
@Component
@Transactional(readOnly = true)
public class YXLsxxService extends BaseService {

	@Autowired
	private YXLsxxDao yXLsxxDao;
	
	public YXLsxx get(String id) {
		return yXLsxxDao.get(id);
	}
	
	public Page<YXLsxx> find(Page<YXLsxx> page, YXLsxx yXLsxx) {
		DetachedCriteria dc = yXLsxxDao.createDetachedCriteria();
		dc.add(Restrictions.ne(YXLsxx.FIELD_DEL_FLAG_XGXT, YXLsxx.STATE_FLAG_DEL));
		return yXLsxxDao.find(page, dc);
	}
	
	public String find(String userId) {
		List<YXLsxx> list = yXLsxxDao.find("from YXLsxx a where a.userid=:p1 and a.states<>'D' order by a.creatTime desc",new Parameter(userId));
		if(list.size()>0){
			return list.get(0).getUniqueId();
		}else{
			return "";
		}
	}
	
	@Transactional(readOnly = false)
	public void save(YXLsxx yXLsxx) {
		yXLsxxDao.save(yXLsxx);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		yXLsxxDao.deleteByXGXTId(id);
	}
	
}
