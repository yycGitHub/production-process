package com.surekam.modules.agro.application.service;

import java.util.Date;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.application.entity.ApplicationRecord;
import com.surekam.modules.agro.application.entity.DelegationRecord;
import com.surekam.modules.agro.application.entity.OperationalRecords;
import com.surekam.modules.agro.file.dao.AgroFileInfoDao;
import com.surekam.modules.agro.file.entity.AgroFileInfo;
import com.surekam.modules.agro.productionbatch.dao.ProductionBatchDao;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.agro.application.dao.ApplicationRecordDao;
import com.surekam.modules.agro.application.dao.DelegationRecordDao;
import com.surekam.modules.agro.application.dao.OperationalRecordsDao;

/**
 * 农户申请记录表Service
 * @author xy
 * @version 2019-06-25
 */
@Component
@Transactional(readOnly = true)
public class ApplicationRecordService extends BaseService {

	@Autowired
	private ApplicationRecordDao applicationRecordDao;
	
	@Autowired
	private AgroFileInfoDao fileInfoDao2;
	
	@Autowired
	private ProductionBatchDao productionBatchDao;
	
	@Autowired
	private DelegationRecordDao delegationRecordDao;
	
	@Autowired
	private OperationalRecordsDao operationalRecordsDao;
	
	public ApplicationRecord get(String id) {
		return applicationRecordDao.get(id);
	}
	
	public Page<ApplicationRecord> find(Page<ApplicationRecord> page, ApplicationRecord applicationRecord) {
		DetachedCriteria dc = applicationRecordDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ApplicationRecord.FIELD_DEL_FLAG, ApplicationRecord.DEL_FLAG_NORMAL));
		return applicationRecordDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ApplicationRecord applicationRecord) {
		applicationRecordDao.save(applicationRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		applicationRecordDao.deleteById(id);
	}
	
	/**
	 * APP申请现场指导保存（农户-批次-现场指导，农户-专家服务-专家信息-现场指导）
	 */
	@Transactional(readOnly = false)
	public void saveApplicationRecord2(User user,String detailedQuestions,String appointmentTime,
			String phoneNumber,String address,String contacts,String [] photo,String [] auditUrl,
			String batchId,String longitude,String latitude,String expertId,String expertName) {
		
		ProductionBatch productionBatch = productionBatchDao.get(batchId);
		
		ApplicationRecord applicationRecord = new ApplicationRecord();
		applicationRecord.setPhoneNumber(phoneNumber);
		applicationRecord.setContacts(contacts);
		applicationRecord.setAddress(address);
		applicationRecord.setDetailedQuestions(detailedQuestions);
		applicationRecord.setAppointmentTime(appointmentTime);
		applicationRecord.setLatitude(latitude);
		applicationRecord.setLongitude(longitude);
		applicationRecord.setCreateTime(new Date());
		applicationRecord.setCreateUserId(user.getId());
		
		applicationRecord.setBaseId(productionBatch.getBaseId());
		applicationRecord.setBatchId(productionBatch.getId());
		applicationRecord.setOfficeId(user.getOffice().getId());
		applicationRecord.setProductId(productionBatch.getProductId());
		
		applicationRecordDao.save(applicationRecord);
		
		OperationalRecords operationalRecords = new OperationalRecords();
		operationalRecords.setArid(applicationRecord.getId());
		operationalRecords.setHandle("申请");
		operationalRecords.setYwid(applicationRecord.getId());
		operationalRecords.setType("1");
		operationalRecords.setCreateTime(new Date());
		operationalRecords.setCreateUserId(user.getId());
		operationalRecords.setStates("A");
		operationalRecordsDao.save(operationalRecords);
		
		String imgUrl = Global.getConfig("sy_img_url");
		if(photo!=null) {
			for(int i=0;i < photo.length;i++) {
				AgroFileInfo fileInfo = new AgroFileInfo();
				fileInfo.setAbsolutePath(imgUrl+photo[i]);
				fileInfo.setUrl(photo[i]);
				fileInfo.setType("1");
				String str = photo[i].substring(photo[i].lastIndexOf("/")+1);//2342.png
				fileInfo.setFileName(str);//str.substring(0, str.indexOf(".")+1)
				fileInfo.setYwzbId(applicationRecord.getId());
				fileInfo.setYwzbType("ApplicationRecord");
				fileInfo.setCreateUserId(user.getId());
				fileInfoDao2.save(fileInfo);
			}
		}
		
		if(auditUrl!=null) {
			for(int j=0;j < auditUrl.length;j++) {
				if(auditUrl[j].equals("undefined")) {
					
				}else {
					AgroFileInfo fileInfo = new AgroFileInfo();
					System.out.println("auditUrl[j]="+auditUrl[j]);
					fileInfo.setUrl(auditUrl[j]);
					fileInfo.setAbsolutePath(imgUrl + auditUrl[j]);
					fileInfo.setType("4");
					String str = auditUrl[j].substring(auditUrl[j].lastIndexOf("/")+1);
					fileInfo.setFileName(str);
					fileInfo.setYwzbId(applicationRecord.getId());
					fileInfo.setYwzbType("ApplicationRecord");
					fileInfo.setCreateUserId(user.getId());
					fileInfoDao2.save(fileInfo);
				}
			}
		}
		
		if(StringUtils.isNotBlank(expertId)) {//农户指定申请的专家
			DelegationRecord delegationRecord = new DelegationRecord();
			delegationRecord.setApplicationId(applicationRecord.getId());
			delegationRecord.setExpertId(expertId);
			delegationRecord.setRemark("农户指定申请专家");
			delegationRecord.setCreateTime(new Date());
			delegationRecord.setCreateUserId(user.getId());
			delegationRecord.setTaskStatus("0");
			delegationRecordDao.save(delegationRecord);
			
			OperationalRecords operationalRecordsTwo = new OperationalRecords();
			operationalRecordsTwo.setArid(applicationRecord.getId());
			operationalRecordsTwo.setHandle("委派专家");
			operationalRecordsTwo.setYwid(delegationRecord.getId());
			operationalRecordsTwo.setType("2");
			operationalRecordsTwo.setCreateTime(new Date());
			operationalRecordsTwo.setCreateUserId(user.getId());
			operationalRecordsTwo.setStates("A");
			operationalRecordsDao.save(operationalRecordsTwo);
		}
	}
	
}
