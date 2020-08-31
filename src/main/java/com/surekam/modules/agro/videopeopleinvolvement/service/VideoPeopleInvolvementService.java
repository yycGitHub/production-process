package com.surekam.modules.agro.videopeopleinvolvement.service;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.modules.agro.videopeopleinvolvement.entity.VideoPeopleInvolvement;
import com.surekam.modules.agro.videopeopleinvolvement.dao.VideoPeopleInvolvementDao;

/**
 * 实时音视频参与人员表Service
 * @author liwei
 * @version 2019-07-16
 */
@Component
@Transactional(readOnly = true)
public class VideoPeopleInvolvementService extends BaseService {

	@Autowired
	private VideoPeopleInvolvementDao videoPeopleInvolvementDao;
	
	public VideoPeopleInvolvement get(String id) {
		return videoPeopleInvolvementDao.get(id);
	}
	
	public Page<VideoPeopleInvolvement> find(Page<VideoPeopleInvolvement> page, VideoPeopleInvolvement videoPeopleInvolvement) {
		DetachedCriteria dc = videoPeopleInvolvementDao.createDetachedCriteria();
		dc.add(Restrictions.eq(VideoPeopleInvolvement.FIELD_DEL_FLAG, VideoPeopleInvolvement.DEL_FLAG_NORMAL));
		return videoPeopleInvolvementDao.find(page, dc);
	}
	
	public VideoPeopleInvolvement findVideoPeopleInvolvementByTaskId(String fileId) {
		String sql = "select a.* from t_agro_video_people_involvement a where a.states<>'D' and a.file_id=:p1";
		List<VideoPeopleInvolvement> list = videoPeopleInvolvementDao.findBySql(sql,new Parameter(fileId),VideoPeopleInvolvement.class);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public List<VideoPeopleInvolvement> getVideoPeopleInvolvementList(String mainId) {
		String hql = " from VideoPeopleInvolvement a where a.states<>'D' and a.mainId=:p1";
		List<VideoPeopleInvolvement> list = videoPeopleInvolvementDao.find(hql,new Parameter(mainId));
		return list;
	}
	
	@Transactional(readOnly = false)
	public void updateVideoPeopleFileId(String mainId, String userCode, String fileId) {
		String hql = " update t_agro_video_people_involvement a set a.id=:p1 where a.states<>'D' and a.main_id=:p2 and a.user_code=:p3";
		videoPeopleInvolvementDao.updateBySql(hql,new Parameter(fileId, mainId, userCode));
		videoPeopleInvolvementDao.flush();
	}
	
	@Transactional(readOnly = false)
	public void updatevideoPeopleTaskId(String mainId, String userCode, String taskId) {
		String hql = " update t_agro_video_people_involvement a set a.task_id=:p1 where a.states<>'D' and a.main_id=:p2 and a.user_code=:p3";
		videoPeopleInvolvementDao.updateBySql(hql,new Parameter(taskId, mainId, userCode));
		videoPeopleInvolvementDao.flush();
	}
	
	@Transactional(readOnly = false)
	public void save(VideoPeopleInvolvement videoPeopleInvolvement) {
		videoPeopleInvolvementDao.save(videoPeopleInvolvement);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		videoPeopleInvolvementDao.deleteById(id);
	}
	
}
