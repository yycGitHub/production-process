package com.surekam.modules.agro.application.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.application.dao.ApplicationRecordDao;
import com.surekam.modules.agro.application.dao.DelegationRecordDao;
import com.surekam.modules.agro.application.dao.GuidanceGecordsDao;
import com.surekam.modules.agro.application.dao.OperationalRecordsDao;
import com.surekam.modules.agro.application.entity.ApplicationRecord;
import com.surekam.modules.agro.application.entity.DelegationRecord;
import com.surekam.modules.agro.application.entity.GuidanceGecords;
import com.surekam.modules.agro.application.entity.OperationalRecords;
import com.surekam.modules.agro.experts.dao.ExpertsDao;
import com.surekam.modules.agro.file.dao.AgroFileInfoDao;
import com.surekam.modules.agro.file.entity.AgroFileInfo;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.sys.entity.User;

/**
 * 专家指导记录表Service
 * @author xy
 * @version 2019-06-25
 */
@Component
@Transactional(readOnly = true)
public class GuidanceGecordsService extends BaseService {

	@Autowired
	private GuidanceGecordsDao guidanceGecordsDao;
	
	@Autowired
	private ApplicationRecordDao applicationRecordDao;
	
	@Autowired
	private AgroFileInfoDao fileInfoDao2;
	
	@Autowired
	private DelegationRecordDao delegationRecordDao;
	
	@Autowired
	private OperationalRecordsDao operationalRecordsDao;
	
	public GuidanceGecords get(String id) {
		return guidanceGecordsDao.get(id);
	}
	
	public Page<GuidanceGecords> find(Page<GuidanceGecords> page, GuidanceGecords guidanceGecords) {
		DetachedCriteria dc = guidanceGecordsDao.createDetachedCriteria();
		dc.add(Restrictions.ne(GuidanceGecords.FIELD_DEL_FLAG_XGXT, GuidanceGecords.STATE_FLAG_DEL));
		return guidanceGecordsDao.find(page, dc);
	}
	
	public Page<GuidanceGecords> getGuidanceGecords(Page<GuidanceGecords> page, String  applicationId) {
		DetachedCriteria dc = guidanceGecordsDao.createDetachedCriteria();
		dc.add(Restrictions.ne(GuidanceGecords.FIELD_DEL_FLAG_XGXT, GuidanceGecords.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("applicationId", applicationId));
		dc.addOrder(Order.desc("createTime"));
		return guidanceGecordsDao.find(page, dc);
	}
	
	
	@Transactional(readOnly = false)
	public void save(GuidanceGecords guidanceGecords) {
		guidanceGecordsDao.save(guidanceGecords);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		guidanceGecordsDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void saveGuidanceGecords2(User user,String expertId,String address,String longitude,String latitude,String details,
			String guidanceTime,String delegationId,String applicationId,String [] photo,String [] auditUrl) {
		
		ApplicationRecord applicationRecord = applicationRecordDao.get(applicationId);
		
		GuidanceGecords guidanceGecords = new GuidanceGecords();
		guidanceGecords.setAddress(address);
		guidanceGecords.setDetails(details);
		guidanceGecords.setGuidanceTime(guidanceTime);
		guidanceGecords.setLatitude(latitude);
		guidanceGecords.setLongitude(longitude);
		guidanceGecords.setDelegationId(delegationId);
		guidanceGecords.setApplicationId(applicationId);
		guidanceGecords.setCreateTime(new Date());
		guidanceGecords.setCreateUserId(user.getId());
		
		guidanceGecords.setExpertId(expertId);
		guidanceGecords.setBaseId(applicationRecord.getBaseId());
		guidanceGecords.setBatchId(applicationRecord.getBatchId());
		guidanceGecords.setOfficeId(user.getOffice().getId());
		guidanceGecords.setProductId(applicationRecord.getProductId());
		
		guidanceGecordsDao.save(guidanceGecords);
		
		OperationalRecords operationalRecords = new OperationalRecords();
		operationalRecords.setArid(applicationRecord.getId());
		operationalRecords.setHandle("现场指导服务");
		operationalRecords.setYwid(guidanceGecords.getId());
		operationalRecords.setType("3");
		operationalRecords.setCreateTime(new Date());
		operationalRecords.setCreateUserId(user.getId());
		operationalRecords.setStates("A");
		operationalRecordsDao.save(operationalRecords);
		
		//专家现场指导后-修改委派任务的状态
		DelegationRecord delegationRecord = delegationRecordDao.get(delegationId);
		delegationRecord.setAcceptStatus("0");
		delegationRecord.setTaskStatus("1");
		delegationRecord.setUpdateTime(new Date());
		delegationRecord.setUpdateUserId(user.getId());
		delegationRecord.setStates("U");
		delegationRecordDao.save(delegationRecord);
		
		String imgUrl = Global.getConfig("sy_img_url");
		if(photo !=null) {
			for(int i=0;i < photo.length;i++) {
				AgroFileInfo fileInfo = new AgroFileInfo();
				fileInfo.setAbsolutePath(imgUrl+photo[i]);
				fileInfo.setUrl(photo[i]);
				fileInfo.setType("1");
				String str = photo[i].substring(photo[i].lastIndexOf("/")+1);//2342.png
				fileInfo.setFileName(str);//str.substring(0, str.indexOf(".")+1)
				fileInfo.setYwzbId(guidanceGecords.getId());
				fileInfo.setYwzbType("GuidanceGecords");
				fileInfo.setCreateUserId(user.getId());
				fileInfoDao2.save(fileInfo);
			}
		}
		
		if(auditUrl !=null) {
			for(int j=0;j < auditUrl.length;j++) {
				AgroFileInfo fileInfo = new AgroFileInfo();
				fileInfo.setUrl(auditUrl[j]);
				fileInfo.setAbsolutePath(imgUrl + auditUrl[j]);
				fileInfo.setType("4");
				String str = auditUrl[j].substring(auditUrl[j].lastIndexOf("/")+1);
				fileInfo.setFileName(str);
				fileInfo.setYwzbId(guidanceGecords.getId());
				fileInfo.setYwzbType("GuidanceGecords");
				fileInfo.setCreateUserId(user.getId());
				fileInfoDao2.save(fileInfo);
			}
		}	
	}
	
	public GuidanceGecords find(String applicationId) {
		String sql = "select * from t_agro_guidance_gecords where application_id ='" + applicationId + "'";
		List<GuidanceGecords> list = guidanceGecordsDao.findBySql2(sql, new Parameter(), GuidanceGecords.class);
		if(list !=null) {
			return list.get(0);
		}
		return new GuidanceGecords();
	}
	
}
