package com.surekam.modules.agro.uploadsetting.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.uploadsetting.entity.UploadSetting;

/**
 * 上传审核记录表DAO接口
 * 
 * @author tangjun
 * @version 2019-07-12
 */
@Repository
public class UploadSettingDao extends BaseDao<UploadSetting> {

	public UploadSetting findByOfficeidAndStandardIdAndStandardIdAndItemsId(String officeId, String standardId, String standardItemsid) {
		String sql = "select * from t_agro_upload_setting a where a.office_id =:p1 and a.standard_id =:p2 and a.standard_items_id =:p3 ";
		List<UploadSetting> findBySql = findBySql(sql, new Parameter(officeId, standardId, standardItemsid), UploadSetting.class);
		if (!findBySql.isEmpty()) {
			return findBySql.get(0);
		}
		return null;
	}
	
	public int delete(String officeId, String standardId) {
		return updateBySql("delete from t_agro_upload_setting where office_id =:p1 and standard_id =:p2 ", new Parameter(officeId, standardId));
	}
}
