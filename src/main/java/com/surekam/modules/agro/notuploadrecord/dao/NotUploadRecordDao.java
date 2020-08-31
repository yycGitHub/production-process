package com.surekam.modules.agro.notuploadrecord.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.notuploadrecord.entity.NotUploadRecord;

/**
 * 不上传记录表DAO接口
 * 
 * @author tangjun
 * @version 2019-07-12
 */
@Repository
public class NotUploadRecordDao extends BaseDao<NotUploadRecord> {

	public NotUploadRecord findByBatchIdAndModularTypeIdAndDataId(String batchId, String modularTypeId, String dataId) {
		String sql = "select * from t_agro_not_upload_record a where a.batch_id =:p1 and a.modular_type_id =:p2 and a.data_id =:p3 ";
		List<NotUploadRecord> findBySql = findBySql(sql, new Parameter(batchId, modularTypeId, dataId), NotUploadRecord.class);
		if (!findBySql.isEmpty()) {
			return findBySql.get(0);
		}
		return null;
	}

	public NotUploadRecord findByBatchIdAndDataId(String batchId, String dataId) {
		String sql = "select * from t_agro_not_upload_record a where a.batch_id =:p1  and a.data_id =:p2 ";
		List<NotUploadRecord> findBySql = findBySql(sql, new Parameter(batchId, dataId), NotUploadRecord.class);
		if (!findBySql.isEmpty()) {
			return findBySql.get(0);
		}
		return null;
	}

	public boolean findByBatchIdAndDataId(String batchId) {
		String sql = "select * from t_agro_not_upload_record a where a.batch_id =:p1";
		List<NotUploadRecord> findBySql = findBySql(sql, new Parameter(batchId), NotUploadRecord.class);
		if (!findBySql.isEmpty()) {
			return true;
		}
		return false;
	}
	public NotUploadRecord findByBatchIdAndModularTypeId(String batchId, String modularTypeId) {
		String sql = "select * from t_agro_not_upload_record a where a.batch_id =:p1  and a.modular_type_id =:p2 ";
		List<NotUploadRecord> findBySql = findBySql(sql, new Parameter(batchId, modularTypeId), NotUploadRecord.class);
		if (!findBySql.isEmpty()) {
			return findBySql.get(0);
		}
		return null;
	}

	public int deleteBatchIdAndModularTypeId(String batchId, String modularTypeId) {
		return updateBySql("delete from t_agro_not_upload_record where batch_id =:p1 and modular_type_id =:p2 ", new Parameter(batchId, modularTypeId));
	}

	public int deleteBatchIdAndDataId(String batchId, String DataId) {
		return updateBySql("delete from t_agro_not_upload_record where batch_id =:p1 and data_id =:p2 ", new Parameter(batchId, DataId));
	}

	public int deleteBatchId(String batchId) {
		return updateBySql("delete from t_agro_not_upload_record where batch_id =:p1 ", new Parameter(batchId));
	}
}
