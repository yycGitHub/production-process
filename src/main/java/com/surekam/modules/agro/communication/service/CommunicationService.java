package com.surekam.modules.agro.communication.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.communication.dao.CommunicationDao;
import com.surekam.modules.agro.communication.entity.Communication;
import com.surekam.modules.agro.experts.dao.ExpertsDao;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.file.dao.AgroFileInfoDao;
import com.surekam.modules.agro.file.entity.AgroFileInfo;
import com.surekam.modules.sys.entity.User;


/**
 * 问题信息Service
 * @author xy
 * @version 2019-04-09
 */
@Component
@Transactional(readOnly = true)
public class CommunicationService extends BaseService {

	@Autowired
	private CommunicationDao communicationDao;
	
	@Autowired
	private AgroFileInfoDao fileInfoDao2;
	
	@Autowired
	private ExpertsDao expertsDao;
	
	public Communication get(String id) {
		return communicationDao.get(id);
	}
	/**
	 * 查询我的问题列表
	 * @param page
	 * @param officeId
	 * @return
	 */
	public Page<Communication> find(Page<Communication> page, String officeId) {
		DetachedCriteria dc = communicationDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Communication.FIELD_DEL_FLAG_XGXT, Communication.STATE_FLAG_DEL));
		if(StringUtils.isNotBlank(officeId)) {
			dc.add(Restrictions.eq("baseId", officeId));
		}
		dc.addOrder(Order.desc("createTime"));
		return communicationDao.find(page, dc);
	}
	/**
	 * 查询全部问题列表
	 * @param page
	 * @param officeId
	 * @return
	 */
	public Page<Communication> findList(Page<Communication> page, String code) {
		String sql = "SELECT a.* FROM t_agro_communication a,t_agro_experts b,sys_user c, t_agro_video d "
			+" WHERE a.expert_id = b.id AND b.user_id = c.ID AND c.LOGIN_NAME = d.user_code AND a.states <> 'D' AND b.states <> 'D' AND c.DEL_FLAG = '0' AND d.states <> 'D' ";
		if(StringUtils.isNotBlank(code)) {
			sql+=" AND d.platform LIKE '%" + code + "%' ";
		}
		return communicationDao.findBySql(page, sql, Communication.class);
	}
	
	
	/**
	 * app-在线学习-问题列表
	 * @param page
	 * @return
	 */
	public Page<Communication> getOnlineLearningList(Page<Communication> page) {
		DetachedCriteria dc = communicationDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Communication.FIELD_DEL_FLAG_XGXT, Communication.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("communicationStatus", "0"));//问题状态：0-已解决，1-未解决
		dc.add(Restrictions.eq("communicationBoutique", "0"));//精品问题
		dc.addOrder(Order.asc("communicationTop"));//置顶排序
		dc.addOrder(Order.asc("sort"));//权重排序
		dc.addOrder(Order.desc("clickNum"));//点击量倒序
		dc.addOrder(Order.desc("createTime"));
		return communicationDao.find(page, dc);
	}
	
	
	@Transactional(readOnly = false)
	public void saveAPP(User user, String expertId, String title, String description, String[] photo, String batchId,String[] auditUrl) {
		Communication communication = new Communication();
		communication.setCreateUserId(user.getId());
		communication.setCreateTime(new Date());
		communication.setExpertId(expertId);
		if(StringUtils.isNotBlank(batchId)){
			communication.setProductionBatchId(batchId);
		}
		communication.setCommunicationTitle(title);
		communication.setCommunicationDescription(description);
		communication.setCommunicationTypeId("1");
		communication.setBaseId(user.getCompany().getId());
		communication.setCommunicationStatus("1");
		communication.setCommunicationBoutique("1");
		communication.setCommunicationTop("1");
		BigInteger big  = new BigInteger("0");
		communication.setClickNum(big);
		communicationDao.save(communication);
		
		String imgUrl = Global.getConfig("sy_img_url");
		for(int i=0;i < photo.length;i++) {
			AgroFileInfo fileInfo = new AgroFileInfo();
			fileInfo.setUrl(photo[i]);
			fileInfo.setAbsolutePath(imgUrl + photo[i]);
			fileInfo.setType("1");
			String str = photo[i].substring(photo[i].lastIndexOf("/")+1);
			fileInfo.setFileName(str);
			fileInfo.setYwzbId(communication.getId());
			fileInfo.setYwzbType("Communication");
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
			fileInfo.setYwzbId(communication.getId());
			fileInfo.setYwzbType("Communication");
			fileInfo.setCreateUserId(user.getId());
			fileInfoDao2.save(fileInfo);
		}
	}
	
	
	@Transactional(readOnly = false)
	public void save(Communication communication) {
		communicationDao.save(communication);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id,String userId) {
		Communication communication = this.getCommunication(id,userId);
		if(communication != null) {
			communicationDao.deleteById(id);
		}
	}
	
	public  Communication getCommunication(String id, String userId) {
		DetachedCriteria dc = communicationDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Experts.FIELD_DEL_FLAG_XGXT, Experts.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("id", id));
		dc.add(Restrictions.eq("creatUserid", userId));
		List<Communication> list = communicationDao.find(dc);
		if(list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return new Communication();
		}
	}
	
	/**
	 * 专家角色登录查询-我的已解决问题列表
	 * @param page
	 * @param officeId
	 * @return
	 */
	public Page<Communication> getResolvedCommunicationByUserId(Page<Communication> page, String userId) {
		Experts experts = getExpertByUserId(userId);
		DetachedCriteria dc = communicationDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Communication.FIELD_DEL_FLAG_XGXT, Communication.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("expertId", experts.getId()));
		dc.add(Restrictions.eq("communicationStatus", Communication.NO));
		dc.addOrder(Order.desc("createTime"));
		return communicationDao.find(page, dc);
	}
	
	/**
	 * 已解决问题数量
	 * @return
	 */
	public long getResolvedNum(String userId) {
		Experts experts = getExpertByUserId(userId);
		DetachedCriteria dc = communicationDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Communication.FIELD_DEL_FLAG_XGXT, Communication.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("expertId", experts.getId()));
		dc.add(Restrictions.eq("communicationStatus", Communication.NO));
		dc.addOrder(Order.desc("createTime"));
		return communicationDao.count(dc);
	}
	
	/**
	 * 专家角色登录查询-我的未解决问题列表
	 * @param page
	 * @param officeId
	 * @return
	 */
	public Page<Communication> getUnsolvedCommunicationByUserId(Page<Communication> page, String userId) {
		Experts experts = getExpertByUserId(userId);
		DetachedCriteria dc = communicationDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Communication.FIELD_DEL_FLAG_XGXT, Communication.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("expertId", experts.getId()));
		dc.add(Restrictions.eq("communicationStatus", Communication.YES));
		dc.addOrder(Order.desc("createTime"));
		return communicationDao.find(page, dc);
	}
	
	/**
	 * 未解决问题数量
	 * @return
	 */
	public long getUnsolvedNum(String userId) {
		Experts experts = getExpertByUserId(userId);
		DetachedCriteria dc = communicationDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Communication.FIELD_DEL_FLAG_XGXT, Communication.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("expertId", experts.getId()));
		dc.add(Restrictions.eq("communicationStatus", Communication.YES));
		dc.addOrder(Order.desc("createTime"));
		return communicationDao.count(dc);
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
	 * demo专家的已解决问题信息
	 * @param page
	 * @param expertId
	 * @return
	 */
	public Page<Communication> getDemoResolvedCommunicationByUserId(Page<Communication> page, String expertId) {
		DetachedCriteria dc = communicationDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Communication.FIELD_DEL_FLAG_XGXT, Communication.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("expertId", expertId));
		dc.add(Restrictions.eq("communicationStatus", Communication.NO));
		dc.addOrder(Order.desc("createTime"));
		return communicationDao.find(page, dc);
	}
	
	/**
	 * demo专家的未解决问题信息
	 * @param page
	 * @param expertId
	 * @return
	 */
	public Page<Communication> getDemoUnsolvedCommunicationByUserId(Page<Communication> page, String expertId) {
		DetachedCriteria dc = communicationDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Communication.FIELD_DEL_FLAG_XGXT, Communication.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("expertId", expertId));
		dc.add(Restrictions.eq("communicationStatus", Communication.YES));
		dc.addOrder(Order.desc("createTime"));
		return communicationDao.find(page, dc);
	}
	
	
	//专家工作台待办事项数量---------------------------------------------------------
	/**
	 * 专家角色登录查询-我的未解决问题列表
	 * @param page
	 * @param officeId
	 * @return
	 */
	public long getUnsolvedCommunicationCount(String userId) {
		Experts experts = getExpertByUserId(userId);
		DetachedCriteria dc = communicationDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Communication.FIELD_DEL_FLAG_XGXT, Communication.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("expertId", experts.getId()));
		dc.add(Restrictions.eq("communicationStatus", Communication.YES));
		return communicationDao.count(dc);
	}
	
}
