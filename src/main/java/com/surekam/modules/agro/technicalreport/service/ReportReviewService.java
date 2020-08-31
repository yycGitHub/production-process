package com.surekam.modules.agro.technicalreport.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.DateUtil;
import com.surekam.modules.agro.file.dao.AgroFileInfoDao;
import com.surekam.modules.agro.file.entity.AgroFileInfo;
import com.surekam.modules.agro.technicalreport.dao.ReportReviewDao;
import com.surekam.modules.agro.technicalreport.dao.ReportingDao;
import com.surekam.modules.agro.technicalreport.entity.ReportReview;
import com.surekam.modules.agro.technicalreport.entity.Reporting;
import com.surekam.modules.sys.entity.User;

/**
 * 汇报评审信息类Service
 * @author xy
 * @version 2019-07-08
 */
@Component
@Transactional(readOnly = true)
public class ReportReviewService extends BaseService {

	@Autowired
	private ReportReviewDao reportReviewDao;
	
	@Autowired
	private ReportingDao reportingDao;
	
	@Autowired
	private AgroFileInfoDao fileInfoDao2;
	
	public ReportReview get(String id) {
		return reportReviewDao.get(id);
	}
	
	public Page<ReportReview> find(Page<ReportReview> page, ReportReview reportReview) {
		DetachedCriteria dc = reportReviewDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ReportReview.FIELD_DEL_FLAG, ReportReview.DEL_FLAG_NORMAL));
		return reportReviewDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ReportReview reportReview) {
		reportReviewDao.save(reportReview);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		reportReviewDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public void saveReportReview(User user,ReportReview reportReview) {
		
		for(int i = 0; i < reportReview.getrIds().size(); i++) {
			if(reportReview.getrIds().get(i) != null) {
				DetachedCriteria dc = reportReviewDao.createDetachedCriteria();
				dc.add(Restrictions.ne(ReportReview.FIELD_DEL_FLAG_XGXT, ReportReview.STATE_FLAG_DEL));
				dc.add(Restrictions.eqOrIsNull("id", reportReview.getrIds().get(i)));
				List<ReportReview> list = reportReviewDao.find(dc);
				if(list != null && list.size() > 0) {
					for(int k=0;k < list.size(); k++) {
						reportReviewDao.deleteDataById(list.get(k).getId());
					}
				}
				ReportReview reportReview2 = new ReportReview();
				reportReview2.setReportId(reportReview.getrIds().get(i));
				reportReview2.setReportType("1");
				reportReview2.setScore(reportReview.getScore());
				reportReview2.setReview(reportReview.getReview());
				reportReview2.setReviewTime(DateUtil.getCurrDate2());
				reportReview2.setReviewUserId(user.getId());
				reportReview2.setReviewStatus("1");
				reportReview2.setCreateTime(new Date());
				reportReview2.setCreateUserId(user.getId());
				reportReviewDao.save(reportReview2);
				
				Reporting reporting = reportingDao.get(reportReview.getrIds().get(i));
				reporting.setAuditStatus("1");
				reporting.setUpdateTime(new Date());
				reporting.setUpdateUserId(user.getId());
				reportingDao.save(reporting);
			}
		}
		
	}
	
	
	@Transactional(readOnly = false)
	public void saveReportReviewAPP(User user,String id,String score,String review) {
		DetachedCriteria dc = reportReviewDao.createDetachedCriteria();
		dc.add(Restrictions.ne(ReportReview.FIELD_DEL_FLAG_XGXT, ReportReview.STATE_FLAG_DEL));
		dc.add(Restrictions.eqOrIsNull("id", id));
		List<ReportReview> list = reportReviewDao.find(dc);
		if(list != null && list.size() > 0) {
			for(int k=0;k < list.size(); k++) {
				reportReviewDao.deleteDataById(list.get(k).getId());
			}
		}
		ReportReview reportReview2 = new ReportReview();
		reportReview2.setReportId(id);
		reportReview2.setReportType("1");
		reportReview2.setScore(score);
		reportReview2.setReview(review);
		reportReview2.setReviewTime(DateUtil.getCurrDate2());
		reportReview2.setReviewUserId(user.getId());
		reportReview2.setReviewStatus("1");
		reportReview2.setCreateTime(new Date());
		reportReview2.setCreateUserId(user.getId());
		reportReviewDao.flush();
		reportReviewDao.save(reportReview2);
		
		Reporting reporting = reportingDao.get(id);
		reporting.setAuditStatus("1");
		reporting.setUpdateTime(new Date());
		reporting.setUpdateUserId(user.getId());
		reportingDao.save(reporting);
	}
}
