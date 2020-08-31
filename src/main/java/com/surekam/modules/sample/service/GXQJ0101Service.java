package com.surekam.modules.sample.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.common.dao.FileInfoDao;
import com.surekam.modules.common.entity.FileInfo;
import com.surekam.modules.sample.dao.GXQJ0101Dao;
import com.surekam.modules.sample.entity.GXQJ0101;

/**
 * 请假单Service
 * @author ligm
 * @version 2017-11-20
 */
@Component
@Transactional(readOnly = true)
public class GXQJ0101Service extends BaseService {

	@Autowired
	private GXQJ0101Dao gXQJ0101Dao;
	
	@Autowired
	private FileInfoDao fileInfoDao;
	
	public GXQJ0101 get(String id) {
		return gXQJ0101Dao.get(id);
	}
	
	public Page<GXQJ0101> find(Page<GXQJ0101> page, GXQJ0101 gXQJ0101) {
		DetachedCriteria dc = gXQJ0101Dao.createDetachedCriteria();
		dc.add(Restrictions.ne(GXQJ0101.FIELD_DEL_FLAG, GXQJ0101.DEL_FLAG_NORMAL));
		return gXQJ0101Dao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(GXQJ0101 gXQJ0101) {
		gXQJ0101Dao.save(gXQJ0101);
	}
	
	/**
	 * 保存请假单以及对应的附件
	 * @param gXQJ0101
	 * @param fileIds
	 */
	@Transactional(readOnly = false)
	public void saveGXQJ0101AndFileInfos(GXQJ0101 gXQJ0101,String[] fileIds) {
		//特殊处理时间
		if(StringUtils.isNotBlank(gXQJ0101.getQjkssj()) && gXQJ0101.getQjkssj().contains(":")){
			String sj = gXQJ0101.getQjkssj().replaceAll(":", "");
			gXQJ0101.setQjkssj(sj + "00");
		}
		//特殊处理时间
		if(StringUtils.isNotBlank(gXQJ0101.getQjjssj()) && gXQJ0101.getQjjssj().contains(":")){
			String sj = gXQJ0101.getQjjssj().replaceAll(":", "");
			gXQJ0101.setQjjssj(sj + "00");
		}
		gXQJ0101Dao.save(gXQJ0101);
		//保存附件关联关系
		if(null != fileIds && fileIds.length != 0){
			for (int i = 0; i < fileIds.length; i++) {
				FileInfo info = fileInfoDao.get(fileIds[i]);
				info.setYwzbId(gXQJ0101.getGxqj0101Id());
				info.setYwzbType(FileInfo.YWZB_TYPE_QJD);
				fileInfoDao.save(info);
			}
		}
		
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		gXQJ0101Dao.deleteByXGXTId(id);
	}
	
}
