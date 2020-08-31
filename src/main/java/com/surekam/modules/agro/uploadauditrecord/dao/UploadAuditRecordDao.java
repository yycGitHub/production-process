package com.surekam.modules.agro.uploadauditrecord.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.surekam.common.persistence.BaseDao;
import com.surekam.common.persistence.Parameter;
import com.surekam.modules.agro.uploadauditrecord.entity.UploadAuditRecord;

/**
 * 上传审核记录表DAO接口
 * 
 * @author tangjun
 * @version 2019-07-12
 */
@Repository
public class UploadAuditRecordDao extends BaseDao<UploadAuditRecord> {

	public List<UploadAuditRecord> getUploadAuditRecordList() {
		String sql = "select a.* from t_agro_upload_audit_record a ";
		List<UploadAuditRecord> listStr = findBySql(sql, null, UploadAuditRecord.class);
		return listStr;
	}

	public List<UploadAuditRecord> findByAuditStatus(String suditStatus) {
		String sql = "select b.* from t_agro_upload_audit_record b "
				+ "inner join (select a.standard_task_items_args_value_id,  max(a.audit_time) auditTime "
				+ "from t_agro_upload_audit_record a group by a.standard_task_items_args_value_id) v "
				+ "on v.standard_task_items_args_value_id = b.standard_task_items_args_value_id and v.auditTime = b.audit_time where b.audit_status in (" + suditStatus + ") ";
		List<UploadAuditRecord> listStr = findBySql(sql, null, UploadAuditRecord.class);
		return listStr;
	}
	
	public UploadAuditRecord findyBySyBatchCodeAndOfficeId(String syBatchCode, String officeId, String auditStatus) {
		String sql = "select a.* from t_agro_upload_audit_record a where a.sy_batch_code =:p1 and a.office_id =:p2 and a.audit_status =:p3 order by a.audit_time desc ";
		List<UploadAuditRecord> listStr = findBySql(sql, new Parameter(syBatchCode, officeId, auditStatus), UploadAuditRecord.class);
		if (!listStr.isEmpty()) {
			return listStr.get(0);
		}
		return null;
	}

	public List<UploadAuditRecord> findByStandardTaskItemsArgsValueIdAndOfficeIdAndBatchId(String officeId, String batchId, String standardTaskItemsArgsValueId) {
		String sql = "select * from t_agro_upload_audit_record a where a.office_id =:p1 and a.batch_id =:p2 and a.standard_task_items_args_value_id =:p3 ";
		List<UploadAuditRecord> listStr = findBySql(sql, new Parameter(officeId, batchId, standardTaskItemsArgsValueId), UploadAuditRecord.class);
		return listStr;
	}
	
	public List<UploadAuditRecord> findByStandardTaskItemsArgsValueId(String standardTaskItemsArgsValueId) {
		String sql = "select * from t_agro_upload_audit_record a where a.standard_task_items_args_value_id =:p1 order by a.audit_time desc";
		List<UploadAuditRecord> listStr = findBySql(sql, new Parameter(standardTaskItemsArgsValueId), UploadAuditRecord.class);
		return listStr;
	}
	
	public List<UploadAuditRecord> findByStandardTaskItemsArgsValueIdAndAuditStatus(String standardTaskItemsArgsValueId, String auditStatus) {
		String sql = "select * from t_agro_upload_audit_record a where a.standard_task_items_args_value_id =:p1 and a.audit_status =:p2 order by a.audit_time desc";
		List<UploadAuditRecord> listStr = findBySql(sql, new Parameter(standardTaskItemsArgsValueId, auditStatus),UploadAuditRecord.class);
		return listStr;
	}
}
