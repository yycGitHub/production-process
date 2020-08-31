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
import com.surekam.modules.sample.dao.YXImageDao;
import com.surekam.modules.sample.entity.YXImage;


/**
 * 图片附件表Service
 * @author liw
 * @version 2018-10-31
 */
@Component
@Transactional(readOnly = true)
public class YXImageService extends BaseService {

	@Autowired
	private YXImageDao yXImageDao;
	
	public YXImage get(String id) {
		return yXImageDao.get(id);
	}
	
	public Page<YXImage> find(Page<YXImage> page, YXImage yXImage) {
		DetachedCriteria dc = yXImageDao.createDetachedCriteria();
		dc.add(Restrictions.ne(YXImage.FIELD_DEL_FLAG_XGXT, YXImage.STATE_FLAG_DEL));
		return yXImageDao.find(page, dc);
	}
	
	public List<YXImage> find(YXImage yXImage) {
		DetachedCriteria dc = yXImageDao.createDetachedCriteria();
		dc.add(Restrictions.ne(YXImage.FIELD_DEL_FLAG_XGXT, YXImage.STATE_FLAG_DEL));
		if(StringUtils.isNotBlank(yXImage.getUniqueId())){
			dc.add(Restrictions.eq("uniqueId", yXImage.getUniqueId()));
		}
		return yXImageDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(YXImage yXImage) {
		yXImageDao.save(yXImage);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		yXImageDao.deleteByXGXTId(id);
	}
	
}
