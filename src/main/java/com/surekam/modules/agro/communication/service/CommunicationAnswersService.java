package com.surekam.modules.agro.communication.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.communication.dao.CommunicationAnswersDao;
import com.surekam.modules.agro.communication.entity.CommunicationAnswers;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.file.dao.AgroFileInfoDao;
import com.surekam.modules.agro.file.entity.AgroFileInfo;
import com.surekam.modules.sys.entity.User;


/**
 * 问题解答信息Service
 * @author xy
 * @version 2019-04-09
 */
@Component
@Transactional(readOnly = true)
public class CommunicationAnswersService extends BaseService {

	@Autowired
	private CommunicationAnswersDao communicationAnswersDao;
	
	@Autowired
	private AgroFileInfoDao fileInfoDao2;
	
	public CommunicationAnswers get(String id) {
		return communicationAnswersDao.get(id);
	}
	
	/**
	 * 问题解答列表
	 * @param page
	 * @param communicationId
	 * @return
	 */
	public Page<CommunicationAnswers> find(Page<CommunicationAnswers> page, String communicationId) {
		DetachedCriteria dc = communicationAnswersDao.createDetachedCriteria();
		dc.add(Restrictions.ne(CommunicationAnswers.FIELD_DEL_FLAG_XGXT, CommunicationAnswers.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("communicationId", communicationId));
		dc.addOrder(Order.asc("createTime"));
		return communicationAnswersDao.find(page, dc);
	}
	
	public List<CommunicationAnswers> find(String communicationId) {
		DetachedCriteria dc = communicationAnswersDao.createDetachedCriteria();
		dc.add(Restrictions.ne(CommunicationAnswers.FIELD_DEL_FLAG_XGXT, CommunicationAnswers.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("communicationId", communicationId));
		dc.addOrder(Order.asc("createTime"));
		return communicationAnswersDao.find(dc);
	}
	
	/**
	 * sql优化-待做
	 * @param communicationId
	 * @return
	 */
	public List<CommunicationAnswers> find2(String communicationId) {
		//DetachedCriteria dc = communicationAnswersDao.createDetachedCriteria();
		
		DetachedCriteria dc = DetachedCriteria.forClass(CommunicationAnswers.class);
		
		
		
		
		dc.add(Restrictions.ne(CommunicationAnswers.FIELD_DEL_FLAG_XGXT, CommunicationAnswers.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("communicationId", communicationId));
		dc.addOrder(Order.asc("createTime"));
		return communicationAnswersDao.find(dc);
	}
	
	
	
	
	
	
	/**
	 * app-专家解答问题保存
	 * @param user
	 * @param communicationId
	 * @param content
	 * @param photo
	 */
	@Transactional(readOnly = false)
	public void saveCommunicationAnswers(User user,String communicationId, String content,String [] photo,String[] auditUrl) {
		CommunicationAnswers answers = new CommunicationAnswers();
		answers.setAnswerContent(content);
		answers.setCommunicationId(communicationId);
		answers.setCreateTime(new Date());
		answers.setCreateUserId(user.getId());
		answers.setExpertId(user.getId());
		answers.setExpertName(user.getName());
		answers.setType("0");
		communicationAnswersDao.save(answers);
		String imgUrl = Global.getConfig("sy_img_url");
		for(int i=0;i < photo.length;i++) {
			AgroFileInfo fileInfo = new AgroFileInfo();
			fileInfo.setAbsolutePath(imgUrl+photo[i]);
			fileInfo.setUrl(photo[i]);
			fileInfo.setType("1");
			String str = photo[i].substring(photo[i].lastIndexOf("/")+1);
			fileInfo.setFileName(str);//str.substring(0, str.indexOf(".")+1)
			fileInfo.setYwzbId(answers.getId());
			fileInfo.setYwzbType("CommunicationAnswers");
			fileInfo.setCreateUserId(user.getId());
			fileInfoDao2.save(fileInfo);
		}
		for(int j=0;j < auditUrl.length;j++) {
			AgroFileInfo fileInfo = new AgroFileInfo();
			fileInfo.setUrl(auditUrl[j]);
			fileInfo.setAbsolutePath(imgUrl + auditUrl[j]);
			fileInfo.setType("4");
			String str = auditUrl[j].substring(auditUrl[j].lastIndexOf("/")+1);
			fileInfo.setFileName(str);
			fileInfo.setYwzbId(answers.getId());
			fileInfo.setYwzbType("CommunicationAnswers");
			fileInfo.setCreateUserId(user.getId());
			fileInfoDao2.save(fileInfo);
		}
	}
	
	/**
	 * app-农户回复问题保存
	 * @param user
	 * @param communicationId
	 * @param content
	 * @param photo
	 */
	@Transactional(readOnly = false)
	public void saveCommunicationAnswersTwo(User user,String communicationId, String content,String [] photo,String[] auditUrl) {
		CommunicationAnswers answers = new CommunicationAnswers();
		answers.setAnswerContent(content);
		answers.setCommunicationId(communicationId);
		answers.setCreateTime(new Date());
		answers.setCreateUserId(user.getId());
		answers.setExpertId(user.getId());
		answers.setExpertName(user.getName());
		answers.setType("1");
		communicationAnswersDao.save(answers);
		String imgUrl = Global.getConfig("sy_img_url");
		for(int i=0;i < photo.length;i++) {
			AgroFileInfo fileInfo = new AgroFileInfo();
			fileInfo.setAbsolutePath(imgUrl+photo[i]);
			fileInfo.setUrl(photo[i]);
			fileInfo.setType("1");
			String str = photo[i].substring(photo[i].lastIndexOf("/")+1);
			fileInfo.setFileName(str);//str.substring(0, str.indexOf(".")+1)
			fileInfo.setYwzbId(answers.getId());
			fileInfo.setYwzbType("CommunicationAnswers");
			fileInfo.setCreateUserId(user.getId());
			fileInfoDao2.save(fileInfo);
		}
		for(int j=0;j < auditUrl.length;j++) {
			AgroFileInfo fileInfo = new AgroFileInfo();
			fileInfo.setUrl(auditUrl[j]);
			fileInfo.setAbsolutePath(imgUrl + auditUrl[j]);
			fileInfo.setType("4");
			String str = auditUrl[j].substring(auditUrl[j].lastIndexOf("/")+1);
			fileInfo.setFileName(str);
			fileInfo.setYwzbId(answers.getId());
			fileInfo.setYwzbType("CommunicationAnswers");
			fileInfo.setCreateUserId(user.getId());
			fileInfoDao2.save(fileInfo);
		}
	}
	
	
	@Transactional(readOnly = false)
	public void save(CommunicationAnswers communicationAnswers) {
		communicationAnswersDao.save(communicationAnswers);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id,String userId) {
		CommunicationAnswers communicationAnswers = this.getCommunicationAnswers(id,userId);
		if(communicationAnswers != null) {
			communicationAnswersDao.deleteById(id,userId);
		}
	}
	
	public  CommunicationAnswers getCommunicationAnswers(String id, String userId) {
		DetachedCriteria dc = communicationAnswersDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Experts.FIELD_DEL_FLAG_XGXT, Experts.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("id", id));
		dc.add(Restrictions.eq("creatUserid", userId));
		List<CommunicationAnswers> list = communicationAnswersDao.find(dc);
		if(list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return new CommunicationAnswers();
		}
	}
}
