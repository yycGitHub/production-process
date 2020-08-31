package com.surekam.modules.agro.application.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.application.dao.DelegationRecordDao;
import com.surekam.modules.agro.application.dao.FeedbackDao;
import com.surekam.modules.agro.application.dao.OperationalRecordsDao;
import com.surekam.modules.agro.application.entity.ApplicationRecord;
import com.surekam.modules.agro.application.entity.DelegationRecord;
import com.surekam.modules.agro.application.entity.Feedback;
import com.surekam.modules.agro.application.entity.GuidanceGecords;
import com.surekam.modules.agro.application.entity.OperationalRecords;
import com.surekam.modules.agro.file.dao.AgroFileInfoDao;
import com.surekam.modules.agro.file.entity.AgroFileInfo;
import com.surekam.modules.sys.entity.User;

/**
 * 反馈
 * @author xy
 * @version 2019-06-25
 */
@Component
@Transactional(readOnly = true)
public class FeedbackService extends BaseService {

	@Autowired
	private FeedbackDao feedbackDao;
	
	@Autowired
	private AgroFileInfoDao fileInfoDao2;
	
	@Autowired
	private DelegationRecordDao delegationRecordDao;
	
	@Autowired
	private OperationalRecordsDao operationalRecordsDao;
	
	public Feedback get(String id) {
		return feedbackDao.get(id);
	}
	
	public Page<Feedback> find(Page<Feedback> page, Feedback feedback) {
		DetachedCriteria dc = feedbackDao.createDetachedCriteria();
		dc.add(Restrictions.eq(Feedback.FIELD_DEL_FLAG, Feedback.DEL_FLAG_NORMAL));
		return feedbackDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(Feedback feedback) {
		feedbackDao.save(feedback);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		feedbackDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void saveFeedback(User user,String expertId,String feedbackOpinion,String continueAppoint,String guidanceId,String delegationId,String applicationId,String [] photo,String [] auditUrl) {
		Feedback feedback = new Feedback();
		feedback.setContinueAppoint(continueAppoint);
		feedback.setFeedbackOpinion(feedbackOpinion);
		feedback.setGuidanceId(guidanceId);
		feedback.setApplicationId(applicationId);
		feedback.setCreateTime(new Date());
		feedback.setCreateUserId(user.getId());
		feedback.setFarmerId(user.getId());
		feedback.setExpertId(expertId);
		feedback.setFeedbackState("0");
		feedbackDao.save(feedback);
		
		OperationalRecords operationalRecords = new OperationalRecords();
		operationalRecords.setArid(applicationId);
		operationalRecords.setHandle("农户反馈");
		operationalRecords.setYwid(feedback.getId());
		operationalRecords.setType("4");
		operationalRecords.setCreateTime(new Date());
		operationalRecords.setCreateUserId(user.getId());
		operationalRecords.setStates("A");
		operationalRecordsDao.save(operationalRecords);
		
		//专家现场指导后-修改委派任务的状态
		DelegationRecord delegationRecord = delegationRecordDao.get(delegationId);
		if(continueAppoint.equals("0")) {
			delegationRecord.setTaskStatus("0");
			delegationRecord.setStates("D");
			delegationRecord.setExplainNo("农户申请再次委派");
		}else {
			delegationRecord.setTaskStatus("3");
			delegationRecord.setStates("U");
		}
		
		delegationRecord.setUpdateTime(new Date());
		delegationRecord.setUpdateUserId(user.getId());
		
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
				fileInfo.setYwzbId(feedback.getId());
				fileInfo.setYwzbType("feedback");
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
				fileInfo.setYwzbId(feedback.getId());
				fileInfo.setYwzbType("feedback");
				fileInfo.setCreateUserId(user.getId());
				fileInfoDao2.save(fileInfo);
			}
		}	
	}
	
	
	public Page<Feedback> findlist(Page<Feedback> page,String applicationId) {
		DetachedCriteria dc = feedbackDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Feedback.FIELD_DEL_FLAG_XGXT, Feedback.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("applicationId", applicationId));
		return feedbackDao.find(page,dc);
	}
	
	public Feedback find(String applicationId) {
		String sql = "select * from t_agro_feedback where application_id ='" + applicationId + "'";
		List<Feedback> list = feedbackDao.findBySql2(sql, new Parameter(), Feedback.class);
		if(list !=null) {
			return list.get(0);
		}
		return new Feedback();
	}
	
}
