package com.surekam.modules.agro.standarditemargsvalue.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.standarditemargs.entity.StandardItemArgs;
import com.surekam.modules.agro.standarditemargsvalue.dao.StandardItemArgsValueDao;
import com.surekam.modules.agro.standarditemargsvalue.entity.StandardItemArgsValue;
import com.surekam.modules.api.dto.req.StandardItemArgsValueReq;
import com.surekam.modules.sys.entity.User;

/**
 * 标准作业参数多项值表Service
 * 
 * @author liwei
 * @version 2019-04-26
 */
@Component
@Transactional(readOnly = true)
public class StandardItemArgsValueService extends BaseService {

	@Autowired
	private StandardItemArgsValueDao standardItemArgsValueDao;
	
	/**
	 * Title: getStandardItemArgsValueList Description: 获取标准作业参数多项 列表值
	 * 
	 * @param pageno
	 *            页数
	 * @param pagesize
	 *            条数
	 * @param itemArgsId
	 *            标准参数ID
	 * @return
	 */ 
	public Page<StandardItemArgsValue> getStandardItemArgsValueList(Integer pageno, Integer pagesize, String itemArgsId) {
		int no = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int size = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<StandardItemArgsValue> page = new Page<StandardItemArgsValue>(no, size);
		Page<StandardItemArgsValue> findPage = findPage(page, itemArgsId);
		return findPage;
	}
	
	/**
	 * Title: findPage Description: 获取标准作业参数多项 列表值 分页
	 * 
	 * @param page
	 *            分页
	 * @param itemArgsId
	 *            标准参数ID
	 * @return
	 */
	private Page<StandardItemArgsValue> findPage(Page<StandardItemArgsValue> page, String itemArgsId) {
		DetachedCriteria dc = standardItemArgsValueDao.createDetachedCriteria();
		dc.add(Restrictions.ne(StandardItemArgsValue.FIELD_DEL_FLAG_XGXT, StandardItemArgs.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("itemArgsId", itemArgsId));
		dc.addOrder(Order.asc("sort"));
		Page<StandardItemArgsValue> find = standardItemArgsValueDao.find(page, dc);
		return find;
	}


	public List<StandardItemArgsValue> getStandardItemArgsValueList(String itemArgsId) {
		DetachedCriteria dc = standardItemArgsValueDao.createDetachedCriteria();
		dc.add(Restrictions.ne(StandardItemArgsValue.FIELD_DEL_FLAG_XGXT, StandardItemArgsValue.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("itemArgsId", itemArgsId));
		dc.addOrder(Order.asc("sort"));
		return standardItemArgsValueDao.find(dc);
	}

	/**
	 * Title: getStandardItemArgsValue Description: 获取标准作业参数多项值
	 * 
	 * @param id
	 *            主键
	 * @return
	 */
	public StandardItemArgsValue getStandardItemArgsValue(String id) {
		StandardItemArgsValue pojo = standardItemArgsValueDao.get(id);
		return pojo;
	}

	/**
	 * Title: delStandardItemArgsValue Description: 删除标准作业参数多项值
	 * 
	 * @param id
	 *            主键
	 * @return
	 */
	@Transactional(readOnly = false)
	public String delStandardItemArgsValue(User user, String id) {
		StandardItemArgsValue pojo = standardItemArgsValueDao.get(id);
		pojo.setUpdateTime(new Date());
		pojo.setUpdateUserId(user.getId());
		pojo.setStates(StandardItemArgsValue.STATE_FLAG_DEL);
		standardItemArgsValueDao.save(pojo);
		return "Success";
	}

	/**
	 * Title: savaStandardItemArgsValue Description: 新增或修改标准作业参数多项值
	 * 
	 * @param user
	 *            用戶信息
	 * @param req
	 *            請求參數
	 * @return
	 */
	@Transactional(readOnly = false)
	public ResultBean<String> savaStandardItemArgsValue(User user, StandardItemArgsValueReq req) {
		if (StringUtils.isNotBlank(req.getId())) {
			StandardItemArgsValue standardItemArgsValue = standardItemArgsValueDao.get(req.getId());
			if (!standardItemArgsValue.getName().equals(req.getName())) {
				List<StandardItemArgsValue> findByItemArgsIdAndNameList = standardItemArgsValueDao.findByItemArgsIdAndName(req.getItemArgsId(), req.getName());
				if (findByItemArgsIdAndNameList != null && findByItemArgsIdAndNameList.size() > 0) {
					return ResultUtil.error(ResultEnum.PARAM_DATA_EXISTS.getCode(), ResultEnum.PARAM_DATA_EXISTS.getMessage());
				}
			}
			
			StandardItemArgsValue pojo = new StandardItemArgsValue();
			BeanUtils.copyProperties(req, pojo);

			pojo.setCreateUserId(pojo.getCreateUserId());
			pojo.setUpdateTime(new Date());
			pojo.setUpdateUserId(user.getId());
			pojo.setStates(StandardItemArgsValue.STATE_FLAG_UPDATE);
			standardItemArgsValueDao.clear();
			standardItemArgsValueDao.flush();
			standardItemArgsValueDao.save(pojo);
		}
		
		if (StringUtils.isBlank(req.getId())) {
			List<StandardItemArgsValue> findByItemArgsIdAndNameList = standardItemArgsValueDao.findByItemArgsIdAndName(req.getItemArgsId(), req.getName());
			if (findByItemArgsIdAndNameList != null && findByItemArgsIdAndNameList.size() > 0) {
				return ResultUtil.error(ResultEnum.PARAM_DATA_EXISTS.getCode(), ResultEnum.PARAM_DATA_EXISTS.getMessage());
			}
			StandardItemArgsValue pojo = new StandardItemArgsValue();
			BeanUtils.copyProperties(req, pojo);
			
			pojo.setCreateTime(new Date());
			pojo.setCreateUserId(user.getId());
			pojo.setStates(StandardItemArgsValue.STATE_FLAG_ADD);
			
			List<StandardItemArgsValue> findByItemArgsId = standardItemArgsValueDao.findByItemArgsId(req.getItemArgsId());
			int size = findByItemArgsId.size() + 1;
			pojo.setSort(String.valueOf(size));
			standardItemArgsValueDao.save(pojo);
		}
		return ResultUtil.success("Success");
	}

	// 通过标准id查询采收品种
	public List<Object> findBystandardsId(String standardsId) {
		return standardItemArgsValueDao.findBystandardsId(standardsId);
	}
}
