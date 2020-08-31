package com.surekam.modules.agro.technicalreport.service;

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
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.technicalreport.entity.Reporting;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.agro.file.dao.AgroFileInfoDao;
import com.surekam.modules.agro.file.entity.AgroFileInfo;
import com.surekam.modules.agro.technicalreport.dao.ReportReviewDao;
import com.surekam.modules.agro.technicalreport.dao.ReportingDao;

/**
 * 汇报信息Service
 * @author xy
 * @version 2019-07-09
 */
@Component
@Transactional(readOnly = true)
public class ReportingService extends BaseService {

	@Autowired
	private ReportingDao reportingDao;
	
	@Autowired
	private AgroFileInfoDao fileInfoDao2;
	
	@Autowired
	private ReportReviewDao reportReviewDao;
	
	public Reporting get(String id) {
		return reportingDao.get(id);
	}
	
	public Page<Reporting> find(Page<Reporting> page, String reportingType,String startDate,String endDate,String userId) {
		DetachedCriteria dc = reportingDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Reporting.FIELD_DEL_FLAG_XGXT, Reporting.STATE_FLAG_DEL));
		if(StringUtils.isNotBlank(reportingType)) {
			dc.add(Restrictions.eq("reportingType", reportingType));
		}
		if(StringUtils.isNotBlank(userId)) {
			dc.add(Restrictions.eq("reportUserId", userId));
		}
		if(StringUtils.isNotBlank(startDate)&&StringUtils.isNotBlank(endDate)) {
			dc.add(Restrictions.between("reportDate", startDate, endDate));
		}
		dc.addOrder(Order.asc("reportDate"));
		return reportingDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(Reporting reporting) {
		reportingDao.save(reporting);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		reportingDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void saveReporting(User user,String workDone,String plannedWork,String otherMatters,String reportUserId,String reportDate,
			String address,String longitude,String latitude,String reportingType,String [] photo,String [] fileUrl) {
		
		Reporting reporting = new Reporting();
		if(reportingType.equals("4")) {
			reporting.setPlannedWork(" ");
			reporting.setOtherMatters(" ");
		}else {
			reporting.setPlannedWork(plannedWork);
			reporting.setOtherMatters(otherMatters);
		}
		reporting.setWorkDone(workDone);
		reporting.setReportUserId(reportUserId);
		reporting.setReportDate(reportDate);
		reporting.setAddress(address);
		reporting.setLatitude(latitude);
		reporting.setLongitude(longitude);
		reporting.setReportingType(reportingType);
		reporting.setAuditStatus("0");
		reporting.setCreateTime(new Date());
		reporting.setCreateUserId(user.getId());
		if(fileUrl!=null) {
			reporting.setAttachments("0");
		}
		reportingDao.save(reporting);
		
		String imgUrl = Global.getConfig("sy_img_url");
		if(photo!=null) {
			for(int i=0;i < photo.length;i++) {
				AgroFileInfo fileInfo = new AgroFileInfo();
				fileInfo.setAbsolutePath(imgUrl+photo[i]);
				fileInfo.setUrl(photo[i]);
				fileInfo.setType("1");
				String str = photo[i].substring(photo[i].lastIndexOf("/")+1);
				fileInfo.setFileName(str);
				fileInfo.setYwzbId(reporting.getId());
				fileInfo.setYwzbType("DailyReport");
				fileInfo.setCreateUserId(user.getId());
				fileInfoDao2.save(fileInfo);
			}
		}
		
		if(fileUrl!=null) {
			for(int j=0;j < fileUrl.length;j++) {
				AgroFileInfo fileInfo = new AgroFileInfo();
				fileInfo.setUrl(fileUrl[j]);
				fileInfo.setAbsolutePath(imgUrl + fileUrl[j]);
				String str = fileUrl[j].substring(fileUrl[j].lastIndexOf("/")+1);
				String type = getType(str);
				fileInfo.setType(type);
				fileInfo.setFileName(str);
				fileInfo.setYwzbId(reporting.getId());
				fileInfo.setYwzbType("DailyReport");
				fileInfo.setCreateUserId(user.getId());
				fileInfoDao2.save(fileInfo);
			}
		}
	}
	
	public String getType(String name) {
		String str = name.substring(name.lastIndexOf(".")+1);
		str = str.toUpperCase();
		if(str.equals("BMP") | str.equals("JPG") | str.equals("JPEG") | str.equals("PNG") | str.equals("GIF")) {
			return "1";
		}else if (str.equals("AVI") | str.equals("MOV") | str.equals("RMVB") | str.equals("RM") 
				| str.equals("FLV") | str.equals("MP4") | str.equals("3GP")) {
			return "3";
		}else if (str.equals("CDA") | str.equals("WAV") | str.equals("MP3") | str.equals("WMA") | str.equals("FLAC") 
				| str.equals("RA") | str.equals("MIDI") | str.equals("OGG") | str.equals("APE") | str.equals("AAC")) {
			return "4";
		}else {
			return "2";
		}
	}
	
	
	@Transactional(readOnly = false)
	public void saveUpdataReporting(User user,String id,String workDone,String plannedWork,String otherMatters,String reportUserId,String reportDate,
			String address,String longitude,String latitude,String reportingType,String [] photo,String [] fileUrl) {
		
		Reporting reporting = reportingDao.get(id);
		reporting.setWorkDone(workDone);
		reporting.setPlannedWork(plannedWork);
		reporting.setOtherMatters(otherMatters);
		reporting.setReportUserId(reportUserId);
		reporting.setReportDate(reportDate);
		reporting.setAddress(address);
		reporting.setLatitude(latitude);
		reporting.setLongitude(longitude);
		reporting.setReportingType(reportingType);
		reporting.setUpdateTime(new Date());
		reporting.setUpdateUserId(user.getId());
		if(fileUrl!=null) {
			reporting.setAttachments("0");
		}
		reportingDao.save(reporting);
		
		List<AgroFileInfo> list = findAgroFileInfo(id,"");
		if(list != null) {
			for(int i=0;i < list.size(); i++) {
				fileInfoDao2.deleteDataById(list.get(i).getId());
			}
		}
		
		String imgUrl = Global.getConfig("sy_img_url");
		if(photo!=null) {
			for(int i=0;i < photo.length;i++) {
				AgroFileInfo fileInfo = new AgroFileInfo();
				fileInfo.setAbsolutePath(imgUrl+photo[i]);
				fileInfo.setUrl(photo[i]);
				fileInfo.setType("1");
				String str = photo[i].substring(photo[i].lastIndexOf("/")+1);
				fileInfo.setFileName(str);
				fileInfo.setYwzbId(reporting.getId());
				fileInfo.setYwzbType("DailyReport");
				fileInfo.setCreateUserId(user.getId());
				fileInfoDao2.save(fileInfo);
			}
		}
		
		if(fileUrl!=null) {
			for(int j=0;j < fileUrl.length;j++) {
				AgroFileInfo fileInfo = new AgroFileInfo();
				fileInfo.setUrl(fileUrl[j]);
				fileInfo.setAbsolutePath(imgUrl + fileUrl[j]);
				String str = fileUrl[j].substring(fileUrl[j].lastIndexOf("/")+1);
				String type = getType(str);
				fileInfo.setType(type);
				fileInfo.setFileName(str);
				fileInfo.setYwzbId(reporting.getId());
				fileInfo.setYwzbType("DailyReport");
				fileInfo.setCreateUserId(user.getId());
				fileInfoDao2.save(fileInfo);
			}
		}
	}
	
	public List<AgroFileInfo> findAgroFileInfo(String ywzbId,String type) {
		DetachedCriteria dc = fileInfoDao2.createDetachedCriteria();
		dc.add(Restrictions.ne(AgroFileInfo.FIELD_DEL_FLAG_XGXT, AgroFileInfo.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("ywzbId", ywzbId));
		if(StringUtils.isNotBlank(type)) {
			dc.add(Restrictions.eq("type", type));
		}
		return fileInfoDao2.find(dc);
	}
	
	
	public Page<Reporting> getReportingListTwo(Page<Reporting> page, String reportingType,String startDate,String endDate,String userId,String auditStatus) {
		DetachedCriteria dc = reportingDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Reporting.FIELD_DEL_FLAG_XGXT, Reporting.STATE_FLAG_DEL));
		if(StringUtils.isNotBlank(reportingType)) {
			dc.add(Restrictions.eq("reportingType", reportingType));
		}
		if(StringUtils.isNotBlank(userId)) {
			dc.add(Restrictions.eq("reportUserId", userId));
		}
		if(StringUtils.isNotBlank(auditStatus)) {
			dc.add(Restrictions.eq("auditStatus", auditStatus));
		}
		if(StringUtils.isNotBlank(startDate)&&StringUtils.isNotBlank(endDate)) {
			dc.add(Restrictions.between("reportDate", startDate, endDate));
		}
		dc.addOrder(Order.asc("reportDate"));
		return reportingDao.find(page, dc);
	}
	
	public long getReportingListTwoCount(String reportingType,String startDate,String endDate,String userId,String auditStatus) {
		DetachedCriteria dc = reportingDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Reporting.FIELD_DEL_FLAG_XGXT, Reporting.STATE_FLAG_DEL));
		if(StringUtils.isNotBlank(reportingType)) {
			dc.add(Restrictions.eq("reportingType", reportingType));
		}
		if(StringUtils.isNotBlank(userId)) {
			dc.add(Restrictions.eq("reportUserId", userId));
		}
		if(StringUtils.isNotBlank(auditStatus)) {
			dc.add(Restrictions.eq("auditStatus", auditStatus));
		}
		if(StringUtils.isNotBlank(startDate)&&StringUtils.isNotBlank(endDate)) {
			dc.add(Restrictions.between("reportDate", startDate, endDate));
		}
		return reportingDao.count(dc);
	}
}
