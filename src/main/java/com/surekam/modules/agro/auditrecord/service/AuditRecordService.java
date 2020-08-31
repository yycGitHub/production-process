package com.surekam.modules.agro.auditrecord.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.DateTest;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.auditrecord.entity.AuditRecord;
import com.surekam.modules.agro.experts.entity.ExpertServiceInfo;
import com.surekam.modules.agro.file.dao.AgroFileInfoDao;
import com.surekam.modules.agro.file.entity.AgroFileInfo;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.agro.auditrecord.dao.AuditRecordDao;

/**
 * 巡检记录表Service
 * @author liwei
 * @version 2019-05-29
 */
@Component
@Transactional(readOnly = true)
public class AuditRecordService extends BaseService {

	@Autowired
	private AuditRecordDao auditRecordDao;
	
	@Autowired
	private AgroFileInfoDao fileInfoDao;
	
	public AuditRecord get(String id) {
		return auditRecordDao.get(id);
	}
	
	public Page<AuditRecord> getAuditRecordList(Page<AuditRecord> page, String baseId) {
		DetachedCriteria dc = auditRecordDao.createDetachedCriteria();
		dc.add(Restrictions.ne(AuditRecord.FIELD_DEL_FLAG_XGXT, AuditRecord.STATE_FLAG_DEL));
		if(StringUtils.isNotBlank(baseId)){
			dc.add(Restrictions.eq("baseId", baseId));
		}
		dc.addOrder(Order.desc("auditTime")).addOrder(Order.desc("createTime"));
		return auditRecordDao.find(page, dc);
	}
	
	public String findAuditRecordTime(String baseId) {
		DetachedCriteria dc = auditRecordDao.createDetachedCriteria();
		dc.add(Restrictions.ne(AuditRecord.FIELD_DEL_FLAG_XGXT, AuditRecord.STATE_FLAG_DEL));
		if(StringUtils.isNotBlank(baseId)){
			dc.add(Restrictions.eq("baseId", baseId));
		}
		List<AuditRecord> list = auditRecordDao.find(dc);
		return getZJjcsj(list);
	}
	
	public String findAuditRecordCount(String baseId) {
		DetachedCriteria dc = auditRecordDao.createDetachedCriteria();
		dc.add(Restrictions.ne(AuditRecord.FIELD_DEL_FLAG_XGXT, AuditRecord.STATE_FLAG_DEL));
		if(StringUtils.isNotBlank(baseId)){
			dc.add(Restrictions.eq("baseId", baseId));
		}
		List<AuditRecord> list = auditRecordDao.find(dc);
		return list.size()+"";
	}
	
	@Transactional(readOnly = false)
	public void saveAuditRecord(User user,String baseId,String auditDate,String typeId,String details,String [] photo,String [] auditUrl) {
		AuditRecord auditRecord = new AuditRecord();
		auditRecord.setCreateTime(new Date());
		auditRecord.setCreateUserId(user.getId());
		auditRecord.setBaseId(baseId);
		auditRecord.setAuditTime(auditDate);
		auditRecord.setAuditType(typeId);
		auditRecord.setAuditDetails(details);
		auditRecordDao.save(auditRecord);
		String imgUrl = Global.getConfig("sy_img_url");
		for(int i=0;i < photo.length;i++) {
			AgroFileInfo fileInfo = new AgroFileInfo();
			fileInfo.setAbsolutePath(imgUrl+photo[i]);
			fileInfo.setUrl(photo[i]);
			fileInfo.setType("1");
			String str = photo[i].substring(photo[i].lastIndexOf("/")+1);//2342.png
			fileInfo.setFileName(str);
			fileInfo.setYwzbId(auditRecord.getId());
			fileInfo.setYwzbType("AuditRecord");
			fileInfo.setCreateUserId(user.getId());
			fileInfoDao.save(fileInfo);
		}
		
		for(int j=0;j < auditUrl.length;j++) {
			AgroFileInfo fileInfo = new AgroFileInfo();
			fileInfo.setUrl(auditUrl[j]);
			fileInfo.setAbsolutePath(imgUrl + auditUrl[j]);
			fileInfo.setType("4");
			String str = auditUrl[j].substring(auditUrl[j].lastIndexOf("/")+1);
			fileInfo.setFileName(str);
			fileInfo.setYwzbId(auditRecord.getId());
			fileInfo.setYwzbType("AuditRecord");
			fileInfo.setCreateUserId(user.getId());
			fileInfoDao.save(fileInfo);
		}
	}
	
	public String getZJjcsj(List<AuditRecord> list){
		if(list.size()>0){
			if(StringUtils.isNotBlank(list.get(0).getAuditTime())){
				int d = DateTest.compareDate(list.get(0).getAuditTime(), null, 0);
				return "上次检查时间：" + list.get(0).getAuditTime() + "(已过" + d + "天)";
			}else{
				return "上次检查时间：无";
			}
		}else{
			return "上次检查时间：无";
		}
	}
	
}
