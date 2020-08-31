package com.surekam.modules.agro.application.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.application.dao.ApplicationRecordVODao;
import com.surekam.modules.agro.application.entity.ApplicationRecordVO;
import com.surekam.modules.agro.application.entity.DelegationRecord;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.sys.entity.User;

/**
 * 农户申请和平台委派视图Service
 * @author xy
 * @version 2019-06-26
 */
@Component
@Transactional(readOnly = true)
public class ApplicationRecordVOService extends BaseService {

	@Autowired
	private ApplicationRecordVODao applicationRecordVODao;
	
	public ApplicationRecordVO get(String id) {
		return applicationRecordVODao.get(id);
	}
	
	public Page<ApplicationRecordVO> find(Page<ApplicationRecordVO> page, String itemId,String companyId,String expertName) {
		StringBuffer sql = new StringBuffer(800);
		sql.append(" select * from v_agro_application_recordvo where states<>'D'  ");//and (dr_states<>'D' or dr_states is NULL)
		if(StringUtils.isNotBlank(expertName)) {
			sql.append(" and expert_name LIKE '%"+ expertName +"%' ");
		}
		if(StringUtils.isNotBlank(itemId)) {
			sql.append(" and office_id='"+ itemId +"' ");
		}else {
			sql.append(" and office_id in (SELECT id FROM sys_office WHERE PARENT_IDS LIKE '%,"+ companyId +",%') ");
		}
		sql.append(" order by create_time desc");
		return applicationRecordVODao.findBySql(page, sql.toString(), new Parameter(), ApplicationRecordVO.class);
	}
	
	public Page<ApplicationRecordVO> find1(Page<ApplicationRecordVO> page, String itemId,String companyId,String expertName) {
		StringBuffer sql = new StringBuffer(800);
		sql.append(" select * from v_agro_application_recordvo where states<>'D'  ");
		
		if(StringUtils.isNotBlank(itemId)) {
			sql.append(" and office_id in (SELECT id FROM sys_office WHERE PARENT_IDS LIKE '%,"+ itemId +",%' or id ='" + itemId + "') ");
		}else {
			sql.append(" and office_id in (SELECT id FROM sys_office WHERE PARENT_IDS LIKE '%,"+ companyId +",%') ");
		}
		
		if(StringUtils.isNotBlank(expertName)) {
			sql.append(" and expert_name LIKE '%"+ expertName +"%' ");
		}
		sql.append(" order by create_time desc");
		return applicationRecordVODao.findBySql(page, sql.toString(), new Parameter(), ApplicationRecordVO.class);
	}
	
	public Page<ApplicationRecordVO> findExpertTaskList(Page<ApplicationRecordVO> page, Experts expert) {
		StringBuffer sql = new StringBuffer(800);
		sql.append(" select * from v_agro_application_recordvo where states<>'D' and dr_states<>'D' ");
		sql.append(" and expert_id = '"+expert.getId()+"' and task_status ='0' ");
		return applicationRecordVODao.findBySql(page, sql.toString(), ApplicationRecordVO.class);
	}
	
	public Page<ApplicationRecordVO> findExpertTaskList2(Page<ApplicationRecordVO> page, Experts expert) {
		StringBuffer sql = new StringBuffer(800);
		sql.append(" select * from v_agro_application_recordvo where states<>'D' and dr_states<>'D' ");
		sql.append(" and expert_id = '"+expert.getId()+"' and (task_status ='1' or task_status ='3') ");
		return applicationRecordVODao.findBySql(page, sql.toString(), ApplicationRecordVO.class);
	}
	
	public String getAllCountnot(Experts expert) {
		StringBuffer sql = new StringBuffer(800);
		sql.append(" select * from v_agro_application_recordvo where states<>'D' and dr_states<>'D' ");
		sql.append(" and expert_id = '"+expert.getId()+"' and task_status ='0' ");
		List<ApplicationRecordVO> list = applicationRecordVODao.findBySql2(sql.toString(), new Parameter(), ApplicationRecordVO.class);
		return String.valueOf(list.size()) ;
	}
	
	public String getAllCount(Experts expert) {
		StringBuffer sql = new StringBuffer(800);
		sql.append(" select * from v_agro_application_recordvo where states<>'D' and dr_states<>'D' ");
		sql.append(" and expert_id = '"+expert.getId()+"' and (task_status ='1' or task_status ='3') ");
		List<ApplicationRecordVO> list = applicationRecordVODao.findBySql2(sql.toString(), new Parameter(), ApplicationRecordVO.class);
		return String.valueOf(list.size()) ;
	}
	
	public Page<ApplicationRecordVO> getTaskById(Page<ApplicationRecordVO> page,String id) {
		DetachedCriteria dc = applicationRecordVODao.createDetachedCriteria();
		dc.add(Restrictions.ne(ApplicationRecordVO.FIELD_DEL_FLAG_XGXT, ApplicationRecordVO.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("id", id));
		dc.addOrder(Order.desc("createTime"));
		return applicationRecordVODao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ApplicationRecordVO applicationRecordVO) {
		applicationRecordVODao.save(applicationRecordVO);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		applicationRecordVODao.deleteById(id);
	}
	
	
	public String getNotCount(User user) {
		StringBuffer sql = new StringBuffer(800);
		sql.append(" select * from v_agro_application_recordvo where ");
		sql.append(" states<>'D' and create_user_id='"+ user.getId() +"' and (dr_states<>'D' or dr_states is NULL) ");
		sql.append(" and (task_status<>'1' and task_status<>'3' or task_status is NULL) ");
		List<ApplicationRecordVO> list = applicationRecordVODao.findBySql(sql.toString(),new Parameter(), ApplicationRecordVO.class);
		return String.valueOf(list.size()) ;
	}
	
	public String getCount(User user) {
		StringBuffer sql = new StringBuffer(800);
		sql.append(" select * from v_agro_application_recordvo WHERE ");
		sql.append(" states<>'D' and create_user_id ='"+ user.getId() +"' and dr_states<>'D' and ( task_status ='1' or task_status ='3' ) ");
		List<ApplicationRecordVO> list = applicationRecordVODao.findBySql2(sql.toString(), new Parameter(), ApplicationRecordVO.class);
		return String.valueOf(list.size()) ;
	}
	
	
	
	public Page<ApplicationRecordVO> getNhNotTaskList(Page<ApplicationRecordVO> page, User user) {
		StringBuffer sql = new StringBuffer(800);
		sql.append(" select * from v_agro_application_recordvo where ");
		sql.append(" states<>'D' and create_user_id='"+ user.getId() +"' and (dr_states<>'D' or dr_states is NULL) ");
		sql.append(" and (task_status<>'1' and task_status<>'3' or task_status is NULL) ");
		return applicationRecordVODao.findBySql(page, sql.toString(), ApplicationRecordVO.class);
	}
	
	public Page<ApplicationRecordVO> getNhTaskList(Page<ApplicationRecordVO> page, User user) {
		StringBuffer sql = new StringBuffer(800);
		sql.append(" select * from v_agro_application_recordvo where ");
		sql.append(" states<>'D' and create_user_id='"+ user.getId() +"' and dr_states<>'D' ");
		sql.append(" and ( task_status ='1' or task_status ='3' ) ");
		return applicationRecordVODao.findBySql(page, sql.toString(), ApplicationRecordVO.class);
	}
		
}
