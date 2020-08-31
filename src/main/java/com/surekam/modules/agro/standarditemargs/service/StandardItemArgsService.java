package com.surekam.modules.agro.standarditemargs.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.surekam.modules.agro.standarditemargs.dao.StandardItemArgsDao;
import com.surekam.modules.agro.standarditemargs.entity.StandardItemArgs;
import com.surekam.modules.api.dto.req.StandardItemArgsReq;
import com.surekam.modules.sys.dao.DictDao;
import com.surekam.modules.sys.entity.Dict;
import com.surekam.modules.sys.entity.User;

/**
 * 标准作业详细参数表Service
 * 
 * @author liwei
 * @version 2019-04-25
 */
@Component
@Transactional(readOnly = true)
public class StandardItemArgsService extends BaseService {

	@Autowired
	private StandardItemArgsDao standardItemArgsDao;
	
	@Autowired
	private DictDao dictDao;

	/**
	 * Title: getStandardItemArgsList Description: 参数列表分页
	 * 
	 * @param user
	 *            用户信息
	 * @param pageno
	 *            页数
	 * @param pagesize
	 *            条数
	 * @param itemsId
	 *            标准作业详细参数表ID
	 * @return
	 */
	public Page<StandardItemArgs> getStandardItemArgsList(User user, Integer pageno, Integer pagesize, String itemsId) {
		int no = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int size = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<StandardItemArgs> page = new Page<StandardItemArgs>(no, size);
		Page<StandardItemArgs> findPage = findPage(page, itemsId);
		List<StandardItemArgs> list = findPage.getList();
		for (StandardItemArgs standardItemArgs : list) {
			if(StringUtils.isNotBlank(standardItemArgs.getArgsType())) {
				Dict dict = dictDao.findByDescriptionAndValue(standardItemArgs.getArgsType(), "sys_standard");
				if (dict != null) {
					standardItemArgs.setArgsTypeName(dict.getDescription());
				}
			}
		}
		return findPage;
	}

	private Page<StandardItemArgs> findPage(Page<StandardItemArgs> page, String itemsId) {
		DetachedCriteria dc = standardItemArgsDao.createDetachedCriteria();
		dc.add(Restrictions.ne(StandardItemArgs.FIELD_DEL_FLAG_XGXT, StandardItemArgs.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("itemsId", itemsId));
		dc.addOrder(Order.asc("sort"));
		Page<StandardItemArgs> find = standardItemArgsDao.find(page, dc);
		return find;
	}

	public List<StandardItemArgs> getStandardItemArgsInfo(String itemId) {
		DetachedCriteria dc = standardItemArgsDao.createDetachedCriteria();
		dc.add(Restrictions.ne(StandardItemArgs.FIELD_DEL_FLAG_XGXT, StandardItemArgs.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("itemsId", itemId));
		dc.addOrder(Order.asc("sort"));
		return standardItemArgsDao.find(dc);
	}

	/**
	 * Title: getDeWeighting Description: 获取复制参数列表
	 * @return 
	 */
	public List<Map<String, Object>> getDeWeighting() {
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

		List<Object> findByDistinct = standardItemArgsDao.findByDistinct();
		for (int i = 0; i < findByDistinct.size(); i++) {
			Object[] object = (Object[]) findByDistinct.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", object[0]);
			map.put("name", object[1]);
			listMap.add(map);
		}
		return listMap;
	}

	/**
	 * Title: delStandardItemArgs Description: 删除参数列表
	 * 
	 * @param user
	 * 
	 * @param id
	 *            主键
	 * @return
	 */
	@Transactional(readOnly = false)
	public String delStandardItemArgs(User user, String id) {
		StandardItemArgs pojo = standardItemArgsDao.get(id);
		pojo.setUpdateTime(new Date());
		pojo.setUpdateUserId(user.getId());
		pojo.setStates(StandardItemArgs.STATE_FLAG_DEL);
		standardItemArgsDao.save(pojo);
		return "Success";
	}

	/**
	 * Title: getStandardItemArgs Description: 获取参数信息
	 * 
	 * @param id
	 *            主键
	 * @return 
	 */
	public StandardItemArgs getStandardItemArgs(String id) {
		StandardItemArgs pojo = standardItemArgsDao.get(id);
		return pojo;
	}

	/**
	 * Title: savaStandardItemArgs Description: 新增或修改
	 * 
	 * @param user
	 *            用户信息
	 * @param req
	 *            请求参数
	 * @return
	 */
	@Transactional(readOnly = false)
	public ResultBean<String> savaStandardItemArgs(User user, StandardItemArgsReq req) {
		if (StringUtils.isNotBlank(req.getId())) {
			StandardItemArgs pojo = standardItemArgsDao.get(req.getId());
			if (!pojo.getArgsName().equals(req.getArgsName())) {
				List<BigInteger> countItemsId = standardItemArgsDao.countItemsId(req.getItemsId(), req.getArgsName());
				for (int i = 0; i < countItemsId.size(); i++) {
					String string = countItemsId.get(i).toString();
					if (!"0".equals(string)) {
						return ResultUtil.error(ResultEnum.PARAM_DATA_EXISTS.getCode(), ResultEnum.PARAM_DATA_EXISTS.getMessage());
					}
				}
			}
			StandardItemArgs copyPojo = new StandardItemArgs();
			BeanUtils.copyProperties(req, copyPojo);

			copyPojo.setCreateUserId(pojo.getCreateUserId());
			copyPojo.setUpdateTime(new Date());
			copyPojo.setUpdateUserId(user.getId());
			copyPojo.setStates(StandardItemArgs.STATE_FLAG_UPDATE);
			standardItemArgsDao.clear();
			standardItemArgsDao.flush();
			standardItemArgsDao.save(copyPojo);
		}
		
		if (StringUtils.isBlank(req.getId())) {
			StandardItemArgs copyPojo = new StandardItemArgs();
			BeanUtils.copyProperties(req, copyPojo);

			List<BigInteger> countItemsId = standardItemArgsDao.countItemsId(req.getItemsId(), req.getArgsName());
			for (int i = 0; i < countItemsId.size(); i++) {
				String string = countItemsId.get(i).toString();
				if (!"0".equals(string)) {
					return ResultUtil.error(ResultEnum.PARAM_DATA_EXISTS.getCode(), ResultEnum.PARAM_DATA_EXISTS.getMessage());
				}
			}
			copyPojo.setCreateTime(new Date());
			copyPojo.setCreateUserId(user.getId());
			copyPojo.setStates(StandardItemArgs.STATE_FLAG_ADD);
			standardItemArgsDao.save(copyPojo);
		}
		return ResultUtil.success("Success");
	}

	/**
	 * Title: getStandardItemArgsSort Description: 获取标准库分页信息排序
	 * 
	 * @param itemsId
	 * @return
	 */
	public int getStandardItemArgsSort(String itemsId) {
		List<BigInteger> findByItemsId = standardItemArgsDao.findByItemsId(itemsId);
		int size = findByItemsId.size() + 1;
		return size;
	}

}
