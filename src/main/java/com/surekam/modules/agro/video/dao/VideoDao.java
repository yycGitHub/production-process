package com.surekam.modules.agro.video.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.video.entity.Video;

/**
 * 腾讯云视频接口DAO接口
 * @author liwei
 * @version 2019-05-07
 */
@Repository
public class VideoDao extends BaseDao<Video> {
	public Video getVideoByUserCode(String userCode) {
		List<Video> list = findBySql(" select v.* from t_agro_video v where v.states<>'D' and v.user_code = :p1 ",new Parameter(userCode), Video.class);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
}
