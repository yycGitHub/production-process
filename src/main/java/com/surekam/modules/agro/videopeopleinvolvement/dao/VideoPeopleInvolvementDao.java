package com.surekam.modules.agro.videopeopleinvolvement.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.videopeopleinvolvement.entity.VideoPeopleInvolvement;

/**
 * 实时音视频参与人员表DAO接口
 * @author liwei
 * @version 2019-07-16
 */
@Repository
public class VideoPeopleInvolvementDao extends BaseDao<VideoPeopleInvolvement> {
	
	public List<VideoPeopleInvolvement> findVideoPeopleInvolvementListByMainId(String mainId) {
		String hql = " from VideoPeopleInvolvement a where a.states<>'D' and a.mainId=:p1";
		return find(hql, new Parameter(mainId));
	}
	
	 public VideoPeopleInvolvement findVideoPeopleInvolvementByMainId(String fileId) {
		String sql = " select a.* from t_agro_video_people_involvement a where a.states<>'D' and a.file_id=:p1";
		List<VideoPeopleInvolvement> list = findBySql(sql, new Parameter(fileId),VideoPeopleInvolvement.class);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
}
