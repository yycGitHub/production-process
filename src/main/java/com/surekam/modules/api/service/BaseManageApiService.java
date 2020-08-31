package com.surekam.modules.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.baseexpertsrelation.dao.BaseExpertsRelationDao;
import com.surekam.modules.agro.baseexpertsrelation.entity.BaseExpertsRelation;
import com.surekam.modules.agro.basemanage.dao.BaseManagerDao;
import com.surekam.modules.agro.basemanage.dao.BaseTreeDao;
import com.surekam.modules.agro.basemanage.entity.BaseManager;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
import com.surekam.modules.agro.basemanage.entity.CopyOfBaseTree;
import com.surekam.modules.agro.basemanage.entity.vo.BaseTreeVo;
import com.surekam.modules.agro.basesensor.dao.BaseSensorDao;
import com.surekam.modules.agro.basesensor.entity.BaseSensor;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.sensorsetup.dao.SensorSetupDao;
import com.surekam.modules.agro.sensorsetup.entity.SensorSetup;
import com.surekam.modules.api.dto.req.AgroBaseTreeReq;
import com.surekam.modules.api.dto.resp.BaseTreeResp;
import com.surekam.modules.api.dto.resp.UserRoleResp;
import com.surekam.modules.cms.entity.Category;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.OfficeVo;
import com.surekam.modules.sys.service.ApiSystemService;
import com.surekam.modules.sys.utils.StaticStringUtils;

/**
 * 基地管理Service
 * 
 * @author tangjun
 * @version 2019-04-09
 */

@Component
@Transactional(readOnly = true)
public class BaseManageApiService extends BaseService {

	@Autowired
	private BaseManagerDao baseManagerDao;

	@Autowired
	private BaseTreeDao baseTreeDao;

	@Autowired
	private OfficeDao officeDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ApiSystemService apiSystemService;
	
	@Autowired
	private SensorSetupDao sensorSetupDao;
	
	@Autowired
	private BaseSensorDao baseSensorDao;

	@Autowired
	private BaseExpertsRelationDao baseExpertsRelationDao;
	
	/**
	 * 获取树状基地
	 * 
	 * @param user
	 * @return
	 */
	public List<BaseTreeVo> getBranchList(User user) {
		List<BaseTreeVo> vos = new ArrayList<BaseTreeVo>();
		String itemId = "";
		if (user != null && user.getCompany() != null) {
			itemId = user.getCompany().getId();
		}
		if (StringUtils.isBlank(itemId)) {
			itemId = "1";
		}
		Office office = apiSystemService.getOfficeById(itemId);
		List<Office> arrayList = apiSystemService.findOffices(user, itemId, false);
		BaseTreeVo vo = treeOfficeList(user, arrayList, office);
		vos.add(vo);
		return vos;
	}

	public BaseTreeVo treeOfficeList(User user, List<Office> list, Office parent) {
		BaseTreeVo officeVo = new BaseTreeVo();
		BeanUtils.copyProperties(parent, officeVo, new String[] { "childList" });
		officeVo.setIsOffice("0");
		officeVo.setOfficeId(parent.getId());
		List<BaseTree> parentArrayList = null;
		
		boolean flg = true;
		List<String> roleIdList = user.getRoleIdList();
		for (String string : roleIdList) {
			if ("1".equals(string) || "1fe2c8e247744ff184e8c162eff44f12".equals(string)) {
				flg = true;
				break;
			}
			if ("391192d4f9634982858634f12de44275".equals(string) || "1852c8e247744ff184e8c162eff44f4c".equals(string)) {
				flg = false;
			}
		}
		if(flg == true) {
			parentArrayList = baseTreeDao.getByOfficeId(parent.getId());
		}else {
			parentArrayList = baseTreeDao.getByOfficeIdAndUserId(parent.getId(), user.getId());
		}
		if (parentArrayList != null && parentArrayList.size()>0) {
			for (BaseTree agroBaseTree : parentArrayList) {
				BaseTreeVo vo = new BaseTreeVo();
				// 返回树状对象
				if (agroBaseTree != null) {
					List<BaseTree> findAgroBaseTreesArrayList = findAgroBaseTrees(user, agroBaseTree.getId());
					if (!findAgroBaseTreesArrayList.isEmpty()) {
						BaseTreeVo agroBaseTreeVo = treeList(findAgroBaseTreesArrayList, agroBaseTree);
						officeVo.getChildList().add(agroBaseTreeVo);
					} else {
						BeanUtils.copyProperties(agroBaseTree, vo);
						vo.setIsOffice("1");
						officeVo.getChildList().add(vo);
					}
				} else {
					BeanUtils.copyProperties(agroBaseTree, vo);
					vo.setIsOffice("1");
					officeVo.getChildList().add(vo);
				}
			}
		}
		for (Office officeTemp : list) {
			if (parent.getId().equals(officeTemp.getParent().getId())) {
				List<BaseTree> officeIdArrayList = null;
				if (!officeTemp.getParent().getId().equals("1")) {
					boolean flgSte = true;
					for (String string : roleIdList) {
						if ("1".equals(string) || "1fe2c8e247744ff184e8c162eff44f12".equals(string)) {
							flgSte = true;
							break;
						}
						if ("391192d4f9634982858634f12de44275".equals(string) || "1852c8e247744ff184e8c162eff44f4c".equals(string)) {
							flgSte = false;
						}
					}
					if (flgSte == true) {
						officeIdArrayList = baseTreeDao.getByOfficeId(officeTemp.getId());
					} else {
						officeIdArrayList = baseTreeDao.getByOfficeIdAndUserId(parent.getId(), user.getId());
					}
				}
				BaseTreeVo agroBaseVo = new BaseTreeVo();
				BeanUtils.copyProperties(officeTemp, agroBaseVo, new String[] { "childList" });
				agroBaseVo.setId(officeTemp.getId());
				agroBaseVo.setParentIds("");
				agroBaseVo.setName(officeTemp.getName());
				agroBaseVo.setOfficeId(officeTemp.getId());
				agroBaseVo.setSort("0");
				agroBaseVo.setIsOffice("0");
				if (officeTemp.getChildList().size() > 0) {
					agroBaseVo = treeOfficeList(user, list, officeTemp);
				}
				if (officeIdArrayList == null) {
					officeVo.getChildList().add(agroBaseVo);
					continue;
				}
				for (BaseTree agroBaseTree : officeIdArrayList) {
					BaseTreeVo vo = new BaseTreeVo();
					// 返回树状对象
					if (agroBaseTree != null) {
						List<BaseTree> findAgroBaseTreesArrayList = findAgroBaseTrees(user, agroBaseTree.getId());
						if (!findAgroBaseTreesArrayList.isEmpty()) {
							BaseTreeVo agroBaseTreeVo = treeList(findAgroBaseTreesArrayList, agroBaseTree);
							agroBaseVo.getChildList().add(agroBaseTreeVo);
						} else {
							BeanUtils.copyProperties(agroBaseTree, vo);
							vo.setIsOffice("1");
							agroBaseVo.getChildList().add(vo);
						}
					} else {
						BeanUtils.copyProperties(agroBaseTree, vo);
						vo.setIsOffice("1");
						agroBaseVo.getChildList().add(vo);
					}
				}
				officeVo.getChildList().add(agroBaseVo);
			}
		}
		return officeVo;
	}
	
	/**
	 * 拼接树状结构
	 * 
	 * @param list
	 * @param office
	 * @return
	 */
	public BaseTreeVo treeList(List<BaseTree> list, BaseTree agroBaseTree) {
		BaseTreeVo agroBaseTreeVo = new BaseTreeVo();
		BeanUtils.copyProperties(agroBaseTree, agroBaseTreeVo, new String[] { "childList" });
		agroBaseTreeVo.setIsOffice("1");
		for (BaseTree agroBaseTreeTemp : list) {
			if (agroBaseTree.getId().equals(agroBaseTreeTemp.getParent().getId())) {
				BaseTreeVo agroBaseTreeVoTemp = new BaseTreeVo();
				BeanUtils.copyProperties(agroBaseTreeTemp, agroBaseTreeVoTemp, new String[] { "childList" });
				agroBaseTreeVoTemp.setIsOffice("1");
				if (agroBaseTreeTemp.getChildList().size() > 0) {
					agroBaseTreeVoTemp = treeList(list, agroBaseTreeTemp);
				}
				agroBaseTreeVo.getChildList().add(agroBaseTreeVoTemp);
			}
		}
		return agroBaseTreeVo;
	}
	
	public OfficeVo treeList(List<Office> list, Office parent) {
		OfficeVo officeVo = new OfficeVo();
		BeanUtils.copyProperties(parent,officeVo,new String[]{"childList"});
		for (Office officeTemp : list) {
			if(parent.getId().equals(officeTemp.getParent().getId())) {
				OfficeVo officeVoTemp = new OfficeVo();
				BeanUtils.copyProperties(officeTemp,officeVoTemp,new String[]{"childList"});
				if(officeTemp.getChildList().size()>0){
					officeVoTemp = treeList(list, officeTemp);
				}
				officeVo.getChildList().add(officeVoTemp);
			}
		}
		return officeVo;
	}
	
	/**
	 * 获取基地数据结构
	 * 
	 * @param user
	 * @param officeId
	 * @param onlyNext
	 * @return
	 */
	public List<BaseTree> findAgroBaseTrees(User user, String id) {
		DetachedCriteria dc = baseTreeDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(id)) {
			dc.createAlias("parent", "office");
			BaseTree agroBaseTree = baseTreeDao.get(id);
			dc.add(Restrictions.like("parentIds", agroBaseTree.getParentIds() + agroBaseTree.getId() + ",", MatchMode.START));
		}
		if (!user.isAdmin()) {
			dc.add(dataScopeFilter(user, dc.getAlias(), ""));
		}
		dc.addOrder(Order.asc("sort"));
		return baseTreeDao.find(dc);
	}

	/**
	 * 查询当前用户下的所有公司
	 * 
	 * @param currentUser
	 * @param officeId
	 * @param onlyNext
	 * @return
	 */
	public List<Office> findOffices(User currentUser, String officeId, boolean onlyNext) {
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(officeId)) {
			dc.createAlias("parent", "office");
			if (onlyNext) {
				dc.add(Restrictions.eq("parent.id", officeId));
			} else {
				Office office = officeDao.get(officeId);
				dc.add(Restrictions.like("parentIds", office.getParentIds() + officeId + ",", MatchMode.START));
			}
		}
		if (!currentUser.isAdmin()) {
			dc.add(dataScopeFilter(currentUser, dc.getAlias(), ""));
		}
		dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
		return officeDao.find(dc);
	}

	/**
	 * 获取公司角色
	 * 
	 * @param officeId
	 * @return
	 */
	public List<UserRoleResp> getOfficeCompanyAdmin(String officeId, String roleId) {
		List<UserRoleResp> userRoleRespArray = new ArrayList<UserRoleResp>();
		Office office = officeDao.get(officeId);
		if (office == null) {
			return null;
		}
		List<User> roleList = userDao.getRoleList(office.getId(), roleId);
		for (int i = 0; i < roleList.size(); i++) {
			UserRoleResp userRoleResp = new UserRoleResp();
			userRoleResp.setId(roleList.get(i).getId());
			userRoleResp.setName(roleList.get(i).getName());
			userRoleResp.setCompanyId(roleList.get(i).getOffice().getId());
			userRoleResp.setLoginName(roleList.get(i).getLoginName());
			userRoleRespArray.add(userRoleResp);
		}
		return userRoleRespArray;
	}

	/**
	 * 根据基地ID获取基地信息
	 * 
	 * @param id
	 * @return
	 */
	public BaseTreeResp getAgroBaseTree(String id) {
		BaseTreeResp req = new BaseTreeResp();

		BaseTree agroBaseTree = baseTreeDao.get(id);
		if (agroBaseTree == null) {
			return null;
		}
		BaseManager use1 = baseManagerDao.getUserId(id, "391192d4f9634982858634f12de44275");
		if(null != use1) {
			req.setIsAdmin(use1.getUserId());
		}
		BaseManager use2 = baseManagerDao.getUserId(id, "1852c8e247744ff184e8c162eff44f4c");
		if(null != use2) {
			req.setArtisan(use2.getUserId());
		}
		BaseTree parentName = baseTreeDao.get(agroBaseTree.getId());
		if (parentName != null) {
			req.setParentName(parentName.getName());
		} else {
			req.setParentName("");
		}
		List<BaseSensor> findByBaseIdAndOfficeId = baseSensorDao.findByBaseIdAndOfficeId(agroBaseTree.getId(), agroBaseTree.getOfficeId());
		List<SensorSetup> ssList = new ArrayList<SensorSetup>();
		if (!findByBaseIdAndOfficeId.isEmpty()) {
			for (BaseSensor bsPojo : findByBaseIdAndOfficeId) {
				ssList.add(sensorSetupDao.get(bsPojo.getSensorId()));
				req.setSsList(ssList);
			}
		}else {
			req.setSsList(ssList);
		}
		BeanUtils.copyProperties(agroBaseTree, req);
		return req;

	}

	/**
	 * 新增基地信息
	 * 
	 * @param agroBaseTreeReq
	 * @return
	 */
	@Transactional(readOnly = false)
	public ResultBean<String> savaAgroBaseTree(AgroBaseTreeReq req, User user) {
		BaseTree agroBaseTree = new BaseTree();
		// 复制页面属性信息到基地实体对象内
		BeanUtils.copyProperties(req, agroBaseTree);
		// 判断新增还是修改
		if (StringUtils.isNotBlank(req.getId())) {
			BaseTree abTree = baseTreeDao.get(req.getId());
			if (!req.getName().equals(abTree.getName())) {
				List<BaseTree> findByBranchNameQuery = baseTreeDao.findByBranchNameQuery(req.getOfficeId(), req.getName());
				if (!findByBranchNameQuery.isEmpty()) {
					return ResultUtil.error(ResultEnum.BASE_NAME_EXIST.getCode(), ResultEnum.BASE_NAME_EXIST.getMessage());
				}
			}
			// 修改操作
			agroBaseTree.setStates(Category.STATE_FLAG_UPDATE);
			agroBaseTree.setUpdateUserId(user.getId());
			agroBaseTree.setUpdateTime(new Date());
			agroBaseTree.setParentIds(abTree.getParentIds());
			
			if (abTree.getParent() != null) {
				agroBaseTree.setParent(baseTreeDao.get(abTree.getParent().getId()));
			} else {
				BaseTree pojo = new BaseTree();
				pojo.setId("0");
				agroBaseTree.setParent(pojo);
			}
			
			baseTreeDao.flush();
			baseTreeDao.clear();
			// 修改基地信息
			baseTreeDao.save(agroBaseTree);
			// 删除基地管理员信息
			baseManagerDao.deleteBaseManager(agroBaseTree.getId());
			// 把管理员和技术员放入List集合
			for (int i = 0; i <= 1; i++) {
				BaseManager baseManager = new BaseManager();
				if (i == 0) {
					baseManager.setTBaseId(agroBaseTree.getId());
					baseManager.setUserId(req.getIsAdmin());
					baseManager.setRoleId(req.getIsAdminRoleId());
				} else if (i == 1) {
					baseManager.setTBaseId(agroBaseTree.getId());
					baseManager.setUserId(req.getArtisan());
					baseManager.setRoleId(req.getArtisanRoleId());
				}
				baseManagerDao.insert(baseManager);
			}
			// 是否同步下级
			if (req.isSynchro()) {
				List<BaseTree> findAgroBaseTreesArrayList = findAgroBaseTrees(user, abTree.getId());
				for (BaseTree baseTree : findAgroBaseTreesArrayList) {
					// 删除基地管理员信息
					baseManagerDao.deleteBaseManager(baseTree.getId());
					for (int i = 0; i <= 1; i++) {
						BaseManager baseManager = new BaseManager();
						if (i == 0) {
							baseManager.setTBaseId(agroBaseTree.getId());
							baseManager.setUserId(req.getIsAdmin());
							baseManager.setRoleId(req.getIsAdminRoleId());
						} else if (i == 1) {
							baseManager.setTBaseId(agroBaseTree.getId());
							baseManager.setUserId(req.getArtisan());
							baseManager.setRoleId(req.getArtisanRoleId());
						}
						baseManagerDao.insert(baseManager);
					}
				}
			}
			// 删除传感器
			baseSensorDao.deleteBaseSensor(abTree.getId(), abTree.getOfficeId());
			if (!req.getSsList().isEmpty()) {
				List<String> ssList = req.getSsList();
				for (String string : ssList) {
					BaseSensor entity = new BaseSensor();
					entity.setBaseId(abTree.getId());
					entity.setOfficeId(abTree.getOfficeId());
					entity.setSensorId(string);
					baseSensorDao.save(entity);
				}
			}
			baseExpertsRelationDao.deleteByBaseId(agroBaseTree.getId());
			for(Experts experts:req.getExpertsList()){
				BaseExpertsRelation baseExpertsRelation = new BaseExpertsRelation();
				baseExpertsRelation.setBaseId(agroBaseTree.getId());
				baseExpertsRelation.setOfficeId(agroBaseTree.getOfficeId());
				baseExpertsRelation.setLeader(experts.getLeader());
				baseExpertsRelation.setExpertId(experts.getId());
				// 插入数据
				baseExpertsRelation.setStates("A");
				baseExpertsRelation.setCreateUserId(user.getId());
				baseExpertsRelation.setCreateTime(new Date());
				baseExpertsRelationDao.save(baseExpertsRelation);
			}
			return ResultUtil.success("Success");
		}
		
		if (StringUtils.isBlank(req.getId())) {
			List<BaseTree> findByBranchNameQuery = baseTreeDao.findByBranchNameQuery(req.getOfficeId(), req.getName());
			if (!findByBranchNameQuery.isEmpty()) {
				return ResultUtil.error(ResultEnum.BASE_NAME_EXIST.getCode(), ResultEnum.BASE_NAME_EXIST.getMessage());
			}
			// 判断是否在同一个基地下新增
			if (StringUtils.isNotBlank(req.getParentId())) {
				BaseTree findByParentAgroBase = baseTreeDao.get(req.getParentId());
				int sort = Integer.parseInt(findByParentAgroBase.getSort());
				agroBaseTree.setParentIds(findByParentAgroBase.getParentIds() + findByParentAgroBase.getId() + ",");
				agroBaseTree.setSort(String.valueOf(sort + 1));
				agroBaseTree.setParent(baseTreeDao.get(req.getParentId()));
			} else if (StringUtils.isBlank(req.getParentId())) {
				agroBaseTree.setParentIds("0,");
				agroBaseTree.setSort("0");
				BaseTree parentAgroBaseTree = new BaseTree();
				parentAgroBaseTree.setId("0");
				agroBaseTree.setParent(parentAgroBaseTree);
			}
			// 插入数据
			agroBaseTree.setCreateUserId(user.getId());
			agroBaseTree.setCreateTime(new Date());
			// 保存基地信息
			baseTreeDao.save(agroBaseTree);
			// 把管理员和技术员放入List集合
			for (int i = 0; i <= 1; i++) {
				BaseManager baseManager = new BaseManager();
				if (i == 0) {
					baseManager.setTBaseId(agroBaseTree.getId());
					baseManager.setUserId(req.getIsAdmin());
					baseManager.setRoleId(req.getIsAdminRoleId());
				} else if (i == 1) {
					baseManager.setTBaseId(agroBaseTree.getId());
					baseManager.setUserId(req.getArtisan());
					baseManager.setRoleId(req.getArtisanRoleId());
				}
				baseManagerDao.insert(baseManager);
			}
			// 是否同步下级
			if (req.isSynchro()) {
				List<BaseTree> findAgroBaseTreesArrayList = findAgroBaseTrees(user, agroBaseTree.getId());
				for (BaseTree baseTree : findAgroBaseTreesArrayList) {
					// 删除基地管理员信息
					baseManagerDao.deleteBaseManager(baseTree.getId());
					for (int i = 0; i <= 1; i++) {
						if (i == 0) {
							BaseManager baseManager = new BaseManager();
							baseManager.setTBaseId(agroBaseTree.getId());
							baseManager.setUserId(req.getIsAdmin());
							baseManager.setRoleId(req.getIsAdminRoleId());
							baseManagerDao.insert(baseManager);
						} else if (i == 1) {
							BaseManager baseManager = new BaseManager();
							baseManager.setTBaseId(agroBaseTree.getId());
							baseManager.setUserId(req.getArtisan());
							baseManager.setRoleId(req.getArtisanRoleId());
							baseManagerDao.insert(baseManager);
						}
					}
				}
			}
			if (!req.getSsList().isEmpty()) {
				List<String> ssList = req.getSsList();
				for (String string : ssList) {
					BaseSensor entity = new BaseSensor();
					entity.setBaseId(agroBaseTree.getId());
					entity.setOfficeId(agroBaseTree.getOfficeId());
					entity.setSensorId(string);
					baseSensorDao.save(entity);
				}
			}
			for(Experts experts:req.getExpertsList()){
				BaseExpertsRelation baseExpertsRelation = new BaseExpertsRelation();
				baseExpertsRelation.setBaseId(agroBaseTree.getId());
				baseExpertsRelation.setOfficeId(agroBaseTree.getOfficeId());
				baseExpertsRelation.setLeader(experts.getLeader());
				baseExpertsRelation.setExpertId(experts.getId());
				// 插入数据
				baseExpertsRelation.setStates("A");
				baseExpertsRelation.setCreateUserId(user.getId());
				baseExpertsRelation.setCreateTime(new Date());
				baseExpertsRelationDao.save(baseExpertsRelation);
			}
			return ResultUtil.success("Success");
		}
		return ResultUtil.error(ResultEnum.OPERATION_FAILED.getCode(),ResultEnum.OPERATION_FAILED.getMessage());
	}

	/**
	 * 删除基地信息
	 * 
	 * @param id
	 * @param user
	 * @return
	 */
	@Transactional(readOnly = false)
	public String deleteAgroBaseTree(String id, User user) {
		BaseTree agroBaseTree = baseTreeDao.get(id);
		agroBaseTree.setStates(Category.STATE_FLAG_DEL);
		agroBaseTree.setUpdateUserId(user.getId());
		agroBaseTree.setUpdateTime(new Date());
		baseTreeDao.save(agroBaseTree);
		return "Success";
	}

	/**
	 * 根据name查询是否存在的基地
	 * 
	 * @param name
	 * @return
	 */
	public String findByBranchNameQuery(String id, String name) {
		List<BaseTree> findByBranchNameQuery = baseTreeDao.findByBranchNameQuery(id, name);
		if (findByBranchNameQuery != null && findByBranchNameQuery.size() > 0) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 获取基地数据结构
	 * 
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> findAgroBaseList(User user, String baseId) {
		String sql = "SELECT t.* FROM t_agro_base_tree t,t_agro_base_manager m,sys_user a where t.id=m.t_base_id and t.office_id = :p1 AND m.user_id=:p2 AND m.role_id=:p3 and t.states<>'D' AND a.DEL_FLAG='0' AND m.user_id=a.ID ";
		if(StringUtils.isNotBlank(baseId)){
			sql+= " and t.id='" + baseId +"'";
		}
		sql+= "ORDER BY t.sort";
		List<Map<String,Object>> list = baseTreeDao.findBySql(sql,new Parameter(user.getOffice().getId(),user.getId(),StaticStringUtils.AGRO_FZR),Map.class);
		return list;
	}
	
	/**
	 * 获取基地数据结构
	 * 
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> findBaseList(String officeId) {
		String sql = "SELECT b.* FROM t_agro_base_tree b WHERE b.states<>'D' AND b.office_id = :p1 ORDER BY b.sort";
		List<Map<String,Object>> list = baseTreeDao.findBySql(sql,new Parameter(officeId),Map.class);
		return list;
	}
	
	/**
	 * 获取基地批次数
	 * 
	 * @param officeId
	 * @return
	 */
	public String findBaseCountByOfficeId(String officeId) {
		String sql = "SELECT b.* FROM t_agro_base_tree b WHERE b.states <>'D' AND b.office_id = :p1";
		List<BaseTree> list = baseTreeDao.findBySql(sql, new Parameter(officeId),BaseTree.class);
		if(list!=null && list.size()>0){
			return list.size() + " 个";
		}else{
			return "无";
		}
	}
	
	/**
	 * 获取基地农户姓名
	 * 
	 * @param user
	 * @return
	 */
	public String getPName(String baseId) {
		String sql = "SELECT a.name FROM t_agro_base_manager m,sys_user a WHERE m.t_base_id = :p1 AND a.DEL_FLAG = '0' AND m.user_id = a.ID AND m.role_id = :p2";
		List<String> list = baseTreeDao.findBySql(sql,new Parameter(baseId,StaticStringUtils.AGRO_NH));
		return list.get(0).toString();
	}
	
	/**
	 * 获取基地数量
	 * 
	 * @param user
	 * @return
	 */
	public String findAgroBaseCount(User user) {
		String sql = "select count(t.id) from t_agro_base_tree t,t_agro_base_manager m where t.id=m.t_base_id and t.office_id = :p1 AND m.user_id=:p2 AND m.role_id=:p3 and t.states<>'D' ";
		List<Integer> list = baseTreeDao.findBySql(sql,new Parameter(user.getOffice().getId(),user.getId(),StaticStringUtils.AGRO_FZR));
		if(list.size()>0){
			return list.get(0) + "";
		}
		return "0";
	}
	
	/**
	 * 获取基地名称
	 * 
	 * @param user
	 * @param officeId
	 * @param onlyNext
	 * @return
	 */
	public List<BaseTree> findAgroBaseOfNh(User user) {
		String sql = "select t.* from t_agro_base_tree t,t_agro_base_manager m where t.id=m.t_base_id and t.office_id = :p1 AND m.user_id=:p2 AND m.role_id=:p3 and t.states<>'D'";
		return baseTreeDao.findBySql(sql,new Parameter(user.getOffice().getId(),user.getId(),StaticStringUtils.AGRO_NH),BaseTree.class);
	}
	
	/**
	 * 获取基地名称
	 * 
	 * @param user
	 * @param officeId
	 * @param onlyNext
	 * @return
	 */
	public List<Map<String,Object>> getUserSSArea(User user) {
		String sql = "select t.* from t_agro_base_tree t,t_agro_base_manager m where t.id=m.t_base_id and t.office_id = :p1 AND m.user_id=:p2 AND m.role_id=:p3 and t.states<>'D' order by t.sort";
		return baseTreeDao.findBySql(sql,new Parameter(user.getOffice().getId(),user.getId(),StaticStringUtils.AGRO_NH),Map.class);
	}

	/**
	 * Title: getPeasantHouseholdInfo Description: 获取农户信息
	 * 
	 * @param baseId
	 *            基地ID
	 * @return
	 */
	public Map<String, String> getPeasantHouseholdInfo(String baseId) {
		Map<String, String> map = new HashMap<String, String>();
		// 获取基地农户信息
		BaseManager userId = baseManagerDao.getUserId(baseId, "1852c8e247744ff184e8c162eff44f4c");
		if (null != userId) {
			User user = userDao.get(userId.getUserId());
			if (null != user) {
				if (StringUtils.isNotBlank(user.getPhone())) {
					map.put("phone", user.getPhone());
				} else {
					map.put("phone", "");
				}
				if (StringUtils.isNotBlank(user.getEmail())) {
					map.put("email", user.getEmail());
				} else {
					map.put("email", "");
				}
			}
		}
		return map;
	}

	public boolean existCode(AgroBaseTreeReq agroBaseTreeReq) {
		List<Office> list = new ArrayList<Office>();
		// 如果id为空，则代表是新增，where条件不带id
		if(StringUtils.isBlank(agroBaseTreeReq.getId())){
			list=baseManagerDao.findBySql("select a.* from t_agro_base_tree a where a.code=:p1 and a.states <> 'D' and a.office_id=:p2",new Parameter(agroBaseTreeReq.getCode(),agroBaseTreeReq.getOfficeId()));
		}else {
			list=baseManagerDao.findBySql("select a.* from t_agro_base_tree a where a.id<>:p1 and a.code=:p2 and a.states <> 'D' and a.office_id=:p3",new Parameter(agroBaseTreeReq.getId(),agroBaseTreeReq.getCode(),agroBaseTreeReq.getOfficeId()));
		}
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public List<BaseTree> getBaseTreeList(String officeId){
		DetachedCriteria dc = baseTreeDao.createDetachedCriteria();
		dc.add(Restrictions.ne(BaseTree.FIELD_DEL_FLAG_XGXT, BaseTree.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("officeId", officeId));
		dc.addOrder(Order.asc("code"));
		return  baseTreeDao.find(dc);	
	}
	public List<BaseTree> getbaseById(String id) {
		DetachedCriteria dc = baseTreeDao.createDetachedCriteria();
		dc.add(Restrictions.ne(BaseTree.FIELD_DEL_FLAG_XGXT, BaseTree.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("id", id));
		return  baseTreeDao.find(dc);	
	}
	
	/**
	 * 获取公司数量
	 * 
	 * @param user
	 * @return
	 */
	public String findCompanyCount(User user) {
		String sql = "SELECT a.* FROM sys_office a WHERE (a.id = '" + user.getOffice().getId() + "' OR a.PARENT_IDS LIKE '%" + user.getOffice().getId() + ",%') AND a.DEL_FLAG = '0'  ";
		List<Office> list = officeDao.findBySql(sql, new Parameter(), Office.class);
		if(list.size()>0){
			return list.size() + "";
		}
		return "0";
	}
	
	public List<BaseManager> getByUserIdList(String userId){
		return baseManagerDao.getByUserIdList(userId);
	}
	
	/**
	 *  获取登录人公司及子公司下的农户
	 * 
	 * @param officeId
	 * @return
	 */
	public List<UserRoleResp> getUserListNH(String officeId, String roleId) {
		List<UserRoleResp> userRoleRespArray = new ArrayList<UserRoleResp>();
		Office office = officeDao.get(officeId);
		if (office == null) {
			return null;
		}
		List<User> roleList = userDao.getUserList(office.getId(), roleId);
		for (int i = 0; i < roleList.size(); i++) {
			UserRoleResp userRoleResp = new UserRoleResp();
			userRoleResp.setId(roleList.get(i).getId());
			userRoleResp.setName(roleList.get(i).getName());
			userRoleResp.setCompanyId(roleList.get(i).getOffice().getId());
			userRoleResp.setLoginName(roleList.get(i).getLoginName());
			userRoleRespArray.add(userRoleResp);
		}
		return userRoleRespArray;
	}

	public List<CopyOfBaseTree> findListByExpertsId(String expertsId,String officeId) {
		String sql = "SELECT"
				+ " a.*"
				+ " FROM"
				+ " t_agro_base_tree a"
				+ " WHERE"
				+ " a.id IN ("
				+ " SELECT"
				+ " r.base_id"
				+ " FROM"
				+ " t_agro_base_experts_relation r"
				+ " LEFT JOIN sys_office o ON r.office_id = o.id"
				+ " WHERE (o.id = '"+officeId+"' OR o.PARENT_IDS LIKE '%,"+officeId+",%')"
				+ " AND r.expert_id = '"+expertsId+"')";
		return baseTreeDao.findBySql(sql,null,CopyOfBaseTree.class);
		
	}

	/**
	 *  获取公司获取所有的基地
	 * 
	 * @param officeId
	 * @return
	 */
	public List<CopyOfBaseTree> getBaseLists(String officeId, String platform) {
		String sql = "SELECT"
				+ " a.*"
				+ " FROM"
				+ " t_agro_base_tree a"
				+ " LEFT JOIN sys_office o ON a.office_id = o.id"
				+ " WHERE (o.id = '"+officeId+"' OR o.PARENT_IDS LIKE '%,"+officeId+",%')";
		return baseTreeDao.findBySql(sql,null,CopyOfBaseTree.class);
	}

}
