package com.surekam.modules.agro.technicalreport.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.technicalreport.entity.ReportReviewVo;
import com.surekam.modules.agro.technicalreport.entity.Reporting;
import com.surekam.modules.agro.application.entity.ApplicationRecordVO;
import com.surekam.modules.agro.technicalreport.dao.ReportReviewVoDao;

/**
 * 汇报评审视图Service
 * @author xy
 * @version 2019-07-12
 */
@Component
@Transactional(readOnly = true)
public class ReportReviewVoService extends BaseService {

	@Autowired
	private ReportReviewVoDao reportReviewVoDao;
	
	public ReportReviewVo get(String id) {
		return reportReviewVoDao.get(id);
	}
	
	public Page<ReportReviewVo> find(Page<ReportReviewVo> page, ReportReviewVo reportReviewVo) {
		DetachedCriteria dc = reportReviewVoDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ReportReviewVo.FIELD_DEL_FLAG, ReportReviewVo.DEL_FLAG_NORMAL));
		return reportReviewVoDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ReportReviewVo reportReviewVo) {
		reportReviewVoDao.save(reportReviewVo);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		reportReviewVoDao.deleteById(id);
	}
	
	public Page<ReportReviewVo> find(Page<ReportReviewVo> page, String reportingType,String startDate,String endDate,String reportUserId,String auditStatus,String userId) {
		DetachedCriteria dc = reportReviewVoDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Reporting.FIELD_DEL_FLAG_XGXT, Reporting.STATE_FLAG_DEL));
		dc.add(Restrictions.or(Restrictions.ne("wstates", Reporting.STATE_FLAG_DEL), Restrictions.isNull("wstates")));
		if(StringUtils.isNotBlank(reportingType)) {
			dc.add(Restrictions.eq("reportingType", reportingType));
		}
		if(StringUtils.isNotBlank(reportUserId)) {
			dc.add(Restrictions.eq("reportUserId", reportUserId));
		}
		if(StringUtils.isNotBlank(auditStatus)) {
			dc.add(Restrictions.eq("auditStatus", auditStatus));
		}
		if(StringUtils.isNotBlank(userId)) {
			dc.add(Restrictions.eq("createUserId", userId));
		}
		if(StringUtils.isNotBlank(startDate)&&StringUtils.isNotBlank(endDate)) {
			dc.add(Restrictions.between("reportDate", startDate, endDate));
		}
		//dc.addOrder(Order.desc("reportDate"));
		dc.addOrder(Order.asc("auditStatus"));
		dc.addOrder(Order.desc("createTime"));
		return reportReviewVoDao.find(page, dc);
	}
	
	public Page<ReportReviewVo> getReportById(Page<ReportReviewVo> page,String id) {
		DetachedCriteria dc = reportReviewVoDao.createDetachedCriteria();
		dc.add(Restrictions.ne(ApplicationRecordVO.FIELD_DEL_FLAG_XGXT, ApplicationRecordVO.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("id", id));
		dc.addOrder(Order.desc("createTime"));
		return reportReviewVoDao.find(page, dc);
	}
	
	public Page<ReportReviewVo> findMyList(Page<ReportReviewVo> page, String reportingType,String startDate,String endDate,String userId,String reportUserId,String auditStatus) {
		DetachedCriteria dc = reportReviewVoDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Reporting.FIELD_DEL_FLAG_XGXT, Reporting.STATE_FLAG_DEL));
		if(StringUtils.isNotBlank(reportingType)) {
			dc.add(Restrictions.eq("reportingType", reportingType));
		}
		if(StringUtils.isNotBlank(reportUserId)) {
			dc.add(Restrictions.eq("reportUserId", reportUserId));
		}
		if(StringUtils.isNotBlank(auditStatus)) {
			dc.add(Restrictions.eq("auditStatus", auditStatus));
		}
		if(StringUtils.isNotBlank(startDate)&&StringUtils.isNotBlank(endDate)) {
			dc.add(Restrictions.between("reportDate", startDate, endDate));
		}
		dc.add(Restrictions.eq("createUserId", userId));
		dc.addOrder(Order.asc("reportDate"));
		return reportReviewVoDao.find(page, dc);
	}
	
}
