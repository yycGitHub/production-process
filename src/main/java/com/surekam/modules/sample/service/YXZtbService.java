package com.surekam.modules.sample.service;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.sample.dao.YXZtbDao;
import com.surekam.modules.sample.entity.YXZtb;


/**
 * 状态表Service
 * @author liw
 * @version 2018-10-31
 */
@Component
@Transactional(readOnly = true)
public class YXZtbService extends BaseService {

	@Autowired
	private YXZtbDao yXZtbDao;
	
	public YXZtb get(String id) {
		return yXZtbDao.get(id);
	}
	
	public Page<YXZtb> find(Page<YXZtb> page, YXZtb yXZtb) {
		DetachedCriteria dc = yXZtbDao.createDetachedCriteria();
		dc.add(Restrictions.ne(YXZtb.FIELD_DEL_FLAG_XGXT, YXZtb.STATE_FLAG_DEL));
		return yXZtbDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(YXZtb yXZtb) {
		if(StringUtils.isNotBlank(yXZtb.getZtbId())){
			yXZtb.setStates("U");
		}else{
			yXZtb.setStates("A");
		}
		yXZtbDao.save(yXZtb);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		yXZtbDao.deleteByXGXTId(id);
	}
	
	public YXZtb getYXZtbByUserid(String userid) {
		DetachedCriteria dc = yXZtbDao.createDetachedCriteria();
		dc.add(Restrictions.ne(YXZtb.FIELD_DEL_FLAG_XGXT, YXZtb.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("userid", userid));
		List<YXZtb> list = yXZtbDao.find(dc);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
}
