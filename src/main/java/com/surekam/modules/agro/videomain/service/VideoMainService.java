package com.surekam.modules.agro.videomain.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.videomain.entity.VideoMain;
import com.surekam.modules.agro.videomain.dao.VideoMainDao;

/**
 * 实时音视频主表Service
 * @author liwei
 * @version 2019-07-16
 */
@Component
@Transactional(readOnly = true)
public class VideoMainService extends BaseService {

	@Autowired
	private VideoMainDao videoMainDao;
	
	public VideoMain get(String id) {
		return videoMainDao.get(id);
	}
	
	public Page<VideoMain> find(Page<VideoMain> page, String startTime, String endTime, String title, String platform) {
		String sql = "select a.* from t_agro_video_main a where a.states<>'D' and a.end_time!='' and a.end_time is not null";
		if(StringUtils.isNotBlank(startTime)){
			sql+=" and DATE_FORMAT(a.start_time,'%Y-%m-%d')>=" + startTime;
		}
		if(StringUtils.isNotBlank(endTime)){
			sql+=" and DATE_FORMAT(a.end_time,'%Y-%m-%d')<=" + endTime;
		}
		if(StringUtils.isNotBlank(title)){
			sql+=" AND a.title LIKE '%" + title + "%'";
		}
		if(StringUtils.isNotBlank(platform)){
			sql+=" AND a.platform = '" + platform + "'";
		}
		sql+=" order by a.create_time desc";
		return videoMainDao.findBySql(page, sql, null, VideoMain.class);
	}
	
	@Transactional(readOnly = false)
	public void save(VideoMain videoMain) {
		videoMainDao.save(videoMain);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		videoMainDao.deleteById(id);
	}
	
}
