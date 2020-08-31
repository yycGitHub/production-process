package com.surekam.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.CacheUtils;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.api.dto.resp.DictResp;
import com.surekam.modules.sys.dao.DictDao;
import com.surekam.modules.sys.entity.Dict;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.utils.DictUtils;

/**
 * 字典Service
 * 
 * @author 腾农科技
 *
 */
@Service
@Transactional(readOnly = true)
public class ApiDictService extends BaseService {

	@Autowired
	private DictDao dictDao;

	/**
	 * 查询字典
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public Dict get(String id) {
		return dictDao.get(id);
	}

	/**
	 * 保存字典
	 * 
	 * @param dict
	 */
	@Transactional(readOnly = false)
	public void saveDict(Dict dict) {
		dictDao.save(dict);
	}

	/**
	 * 删除字典
	 */
	@Transactional(readOnly = false)
	public void delete(String id) {
		dictDao.deleteById(id);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

	/**
	 * 更新 标记:delFlag
	 * 
	 * @param id
	 * @param delFlag
	 */
	@Transactional(readOnly = false)
	public void dictChangeState(String id, String delFlag) {
		dictDao.updateDelFlag(id, delFlag);
	}

	/**
	 * 分页查询字典
	 * 
	 * @param page
	 * @param currentUser
	 * @param label
	 * @param delFlag
	 * @return
	 */
	public Page<Dict> findPageDictlist(Page<Dict> page, User currentUser, String label, String type, String delFlag) {
		DetachedCriteria dc = dictDao.createDetachedCriteria();
		if (label != null && StringUtils.isNotBlank(label)) {
			dc.add(Restrictions.like("label", "%" + label + "%"));
		}
		if (type != null && StringUtils.isNotBlank(type)) {
			dc.add(Restrictions.like("type", type));
		}
		if (!currentUser.isAdmin()) {
			dc.add(dataScopeFilter(currentUser, dc.getAlias(), ""));
		}
		if (delFlag != null && StringUtils.isNotBlank(delFlag)) {
			dc.add(Restrictions.eq(Dict.FIELD_DEL_FLAG, delFlag));
		} else {
			// dc.add(Restrictions.eq(Dict.FIELD_DEL_FLAG, Dict.DEL_FLAG_NORMAL));
		}
		return dictDao.find(page, dc);
	}

	/**
	 * Title: getDictList Description:
	 * 
	 * @param id
	 * @return
	 */
	public List<DictResp> getDictList(String id) {
		List<DictResp> respArrayList = new ArrayList<DictResp>();
		List<Dict> dictList = dictDao.findByType(id);
		for (Dict dict : dictList) {
			DictResp resp = new DictResp();
			resp.setId(dict.getValue());
			resp.setDescription(dict.getLabel());
			respArrayList.add(resp);
		}
		return respArrayList;
	}

	public List<String> findTypeList() {
		return dictDao.findTypeList();
	}

	/**
	 * Title: getSensor Description: 获取传感器
	 * 
	 * @param id
	 * @return
	 */
	public List<DictResp> getSensor(String id) {
		List<DictResp> respArrayList = new ArrayList<DictResp>();
		List<Dict> dictList = dictDao.findByType(id);
		for (Dict dict : dictList) {
			DictResp resp = new DictResp();
			resp.setId(dict.getId());
			resp.setLabel(dict.getLabel());
			resp.setDescription(dict.getDescription());
			respArrayList.add(resp);
		}
		return respArrayList;
	}

	/**
	 * Title: getDictSort Description: 更新类型获取排序
	 * 
	 * @param type
	 *            类型
	 * @return
	 */
	public String getDictMaxSort(String type) {
		List<Object> findMaxSort = dictDao.findMaxSort(type);
		String str = findMaxSort.get(0).toString();
		return str;
	}
	
	/**
	 * 查询专家所属平台使用
	 * @param value
	 * @param type
	 * @return
	 */
	public List<Dict> findDictlist(String value, String type) {
		DetachedCriteria dc = dictDao.createDetachedCriteria();
		if (value != null && StringUtils.isNotBlank(value)) {
			dc.add(Restrictions.eq("value",value));
		}
		if (type != null && StringUtils.isNotBlank(type)) {
			dc.add(Restrictions.eq("type", type));
		}
		dc.add(Restrictions.eq(Dict.FIELD_DEL_FLAG, Dict.DEL_FLAG_NORMAL));
		return dictDao.find(dc);
	}
	
	
	public List<Dict> findDictlist(String type) {
		DetachedCriteria dc = dictDao.createDetachedCriteria();
		if (type != null && StringUtils.isNotBlank(type)) {
			dc.add(Restrictions.eq("type", type));
		}
		dc.add(Restrictions.eq(Dict.FIELD_DEL_FLAG, Dict.DEL_FLAG_NORMAL));
		return dictDao.find(dc);
	}

	/**
	 * 根据公司平台id查询平台
	 * 
	 * @param page
	 * @param currentUser
	 * @param label
	 * @param delFlag
	 * @return
	 */
	public List<Dict> findDict(String officeId) {
		DetachedCriteria dc = dictDao.createDetachedCriteria();
		dc.add(Restrictions.like("description", officeId));
		dc.add(Restrictions.like("type", "platform"));
		return dictDao.find(dc);
	}
}
