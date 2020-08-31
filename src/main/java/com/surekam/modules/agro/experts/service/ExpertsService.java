package com.surekam.modules.agro.experts.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.ChineseCharToEnUtil;
import com.surekam.common.utils.Client;
import com.surekam.common.utils.MapKeyComparator;
import com.surekam.common.utils.RestAPI;
import com.surekam.common.utils.WebRTCSigApi;
import com.surekam.modules.agro.basemanage.entity.BaseTree;
import com.surekam.modules.agro.basemanage.entity.CopyOfBaseTree;
import com.surekam.modules.agro.experts.dao.ExpertsDao;
import com.surekam.modules.agro.experts.dao.ExpertsGoodproblemDao;
import com.surekam.modules.agro.experts.dao.ExpertsServiceInfoDao;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.entity.ExpertsGoodproblem;
import com.surekam.modules.agro.expertsprofessionalfieldrelation.dao.ExpertsProfessionalfieldRelationDao;
import com.surekam.modules.agro.expertsprofessionalfieldrelation.entity.ExpertsProfessionalfieldRelation;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.agro.video.entity.Video;
import com.surekam.modules.agro.video.service.VideoService;
import com.surekam.modules.api.dto.req.ExpertsReq;
import com.surekam.modules.api.dto.req.ProductLibraryTreeListReq;
import com.surekam.modules.api.dto.req.UserReq;
import com.surekam.modules.api.dto.resp.ExpertsResp;
import com.surekam.modules.api.dto.resp.ExpertsUserInfoResp;
import com.surekam.modules.api.dto.resp.ProductLibraryTreeListResp;
import com.surekam.modules.api.service.BaseManageApiService;
import com.surekam.modules.api.service.SpeciesManageApiService;
import com.surekam.modules.sys.dao.DictDao;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.entity.Dict;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.utils.StaticStringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 专家信息Service
 * 
 * @author xy
 * @version 2019-04-09
 */
@Component
@Transactional(readOnly = true)
public class ExpertsService extends BaseService {

	public static String sy_img_url = Global.getConfig("sy_img_url");
	@Autowired
	private ExpertsDao expertsDao;

	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;

	@Autowired
	private ExpertsGoodproblemService expertsGoodproblemService;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private DictDao dictDao;
	@Autowired
	private BaseManageApiService baseManageApiService;
	
	@Autowired
	private ExpertsGoodproblemDao expertsGoodproblemDao;
	
	@Autowired
	private ExpertsProfessionalfieldRelationDao agroExpertsProfessionalfieldRelationDao;

	@Autowired
	private SpeciesManageApiService speciesManageApiService;
	
	@Autowired
	private ExpertsServiceInfoDao expertsServiceInfoDao;
	
	@Autowired
	private ExpertsProfessionalfieldRelationDao expertsProfessionalfieldRelationDao;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private VideoService videoService;
	
	public Experts get(String id) {
		return expertsDao.get(id);
	}

	public Experts getExpertByUserId(String userId) {
		DetachedCriteria dc = expertsDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Experts.FIELD_DEL_FLAG_XGXT, Experts.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("userId", userId));
		List<Experts> list = expertsDao.find(dc);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return new Experts();
		}
	}
	
	/**
	 * 展示平台使用
	 * @return
	 */
	public Page<Experts> find(Page<Experts> page, String productLibraryId,String code) {
		return expertsDao.findBySql(page, getSql(productLibraryId,code), Experts.class);
	}
	/**
	 * 手机端专家列表
	 * @return
	 */
	public Page<Experts> find2(Page<Experts> page, String productLibraryId,String code) {
		return expertsDao.findBySql(page, getSql(productLibraryId,code), Experts.class);
	}
	
	/**
	 * pc巡查记录，专家下拉框
	 * @return
	 */
	public List<Experts> findList(User user) {
		String code = getPlatformByUser(user);
		DetachedCriteria dc = expertsDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Experts.FIELD_DEL_FLAG_XGXT, Experts.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("platform", code));
		return expertsDao.find(dc);
	}
	
	public List<Experts> ExpertsList() {
		String sql = " SELECT r.create_user_id, u.name as expert_name FROM t_agro_reporting r LEFT JOIN sys_user u on r.create_user_id = u.id GROUP BY r.create_user_id,u.name ";
		return expertsDao.findBySql(sql, null);
	}

	public Page<Experts> findTwo(Page<Experts> page, String productId,String platform) {
		return expertsDao.findBySql(page, getSqlTwo(productId,platform), Experts.class);
	}
	
	public Page<User> findExpertsList(Page<User> page, String name, User user) {
		/*String code = "wangcheng";
		String sql = "select a.platform from t_agro_video a where a.user_code=:p1 and a.states<>'D'";
		List<String> list = expertsDao.findBySql(sql,new Parameter(user.getLoginName()));
		if(list.size()>0){
			code = list.get(0);
			if(code.contains(",")){
				code = code.split(",")[0];
			}
		}*/
		
		String code = getPlatformByUser(user);
		String sql = "SELECT f.* FROM t_agro_experts e,sys_user f WHERE e.states <> 'D' AND e.user_id = f.id AND f.del_flag='0' AND f.login_name<>'" + user.getLoginName() + "'";
		if (StringUtils.isNotBlank(name)) {
			sql+=" and f.name like '%"+name+"%'";
		}
		if (StringUtils.isNotBlank(code)) {
			sql+=" and e.platform like '%" + code + "%'";
		}
		sql+=" order by f.name";
		return userDao.findBySql(page, sql, null, User.class);
	}
	/*
	 * 
	 * 专家问答-点击产品筛选专家时优先把点击品种的专家排在最前面，后面排其他的专家，可以避免列表为空
	 */
	public String getSqlThree(String productId,String code) {
		String sql = "";
		sql += "SELECT a.* FROM  ( ";
		
		sql += " SELECT distinct e.* FROM t_agro_experts_professionalfield_relation p,t_agro_experts e WHERE e.id = p.experts_id and e.states<>'D' and  	p.product_library_id ='"+productId+"' ";
		
		sql += " union ";
		
		sql += " SELECT distinct e.* FROM t_agro_experts_professionalfield_relation p,t_agro_experts e WHERE e.id = p.experts_id and e.states<>'D' and ";
		
		sql += " FIND_IN_SET (p.product_library_id, ( SELECT ( CASE  lt.is_product_category WHEN '1' THEN  ";
		
		sql += " (SELECT concat(l.id,',', (SELECT concat(id,',',parent_id) FROM t_agro_product_library_tree  WHERE id ='"+productId+"') ) id FROM t_agro_product_library_tree l WHERE l.parent_id ='"+productId+"' )  ";
		
		sql += " WHEN '2' THEN (SELECT concat(a.id,',',a.parent_id ,',',plt.parent_id) as id FROM t_agro_product_library_tree plt,(SELECT pl.id,pl.parent_id  FROM t_agro_product_library_tree pl  WHERE pl.id ='"+productId+"' ) a WHERE plt.id = a.parent_id) end) as ids ";
		
		sql += " FROM t_agro_product_library_tree lt WHERE  lt.id ='"+productId+"' )) ";
		
		sql +=" union SELECT distinct ae.* FROM t_agro_experts ae WHERE ae.states<>'D' ) a ";
		
		if (StringUtils.isNotBlank(code)) {
			sql +=" where a.user_id IN (SELECT bb.ID FROM t_agro_video aa,sys_user bb WHERE aa.states <> 'D' AND bb.DEL_FLAG = '0' AND aa.user_code=bb.LOGIN_NAME AND aa.platform like '%"+code+"%') ";
		}
		return sql;
	}
	
	
	/**
	 * 批次提问选择专家列表
	 * @param productId
	 * @return
	 */
	public String getSqlTwo(String productId,String platform) {
		StringBuffer sql = new StringBuffer(800);
		sql.append("SELECT a.* FROM  ( ");
		
		sql.append(" SELECT distinct e.* FROM t_agro_experts_professionalfield_relation p,t_agro_experts e WHERE e.id = p.experts_id and e.states<>'D' and p.product_library_id ='"+productId+"' and e.platform ='"+platform+"' ");
		
		sql.append(" union ");
		
		sql.append(" SELECT distinct e.* FROM t_agro_experts_professionalfield_relation p,t_agro_experts e WHERE e.id = p.experts_id and e.states<>'D' and e.platform ='"+platform+"' and ");
		
		sql.append(" FIND_IN_SET (p.product_library_id, ( SELECT ( CASE  lt.is_product_category WHEN '1' THEN  ");
		
		sql.append(" (SELECT concat(l.id,',', (SELECT concat(id,',',parent_id) FROM t_agro_product_library_tree  WHERE id ='"+productId+"') ) id FROM t_agro_product_library_tree l WHERE l.parent_id ='"+productId+"' )  ");
		
		sql.append(" WHEN '2' THEN (SELECT concat(a.id,',',a.parent_id ,',',plt.parent_id) as id FROM t_agro_product_library_tree plt,(SELECT pl.id,pl.parent_id  FROM t_agro_product_library_tree pl  WHERE pl.id ='"+productId+"' ) a WHERE plt.id = a.parent_id) end) as ids ");
		
		sql.append(" FROM t_agro_product_library_tree lt WHERE  lt.id ='"+productId+"' )) ");
		
		sql.append(" union SELECT distinct ae.* FROM t_agro_experts ae WHERE ae.states<>'D' and ae.platform ='"+platform+"' ) a ");
		return new String(sql);
	}
	
	
	/**
	 * 手机专家列表查询，传入参数为最子节点的品种id。专家领域绑定的可以是最子节点品种id也可以是最子节点的parentid
	 * @param productLibraryId
	 * @return
	 */
	public String getSql(String productLibraryId, String code) {
		StringBuffer sql = new StringBuffer(300);
		sql.append("SELECT DISTINCT a.* FROM  ( ");
		sql.append(" SELECT e.* FROM t_agro_experts_professionalfield_relation p,t_agro_experts e WHERE e.id = p.experts_id and e.states<>'D' ");
		if (StringUtils.isNotBlank(code)) {
			sql.append(" AND e.platform LIKE '%"+code+"%' ");
		}
		if (StringUtils.isNotBlank(productLibraryId) && !productLibraryId.equals("0")) {
			sql.append(" and FIND_IN_SET (p.product_library_id, (SELECT (CASE  lt.is_product_category WHEN '1' THEN lt.id WHEN '2' THEN concat(lt.id,',',lt.parent_id) end) as id FROM  t_agro_product_library_tree lt WHERE  lt.id ='"+productLibraryId+"')) ");
		}
		sql.append(" ) a order by a.create_time desc");
		return new String(sql);
	}
	
	/**
	 * 根据种类ID查询
	 * @param id
	 * @return
	 */
	public List<Experts> expertsFindById(String id,String code){
		return expertsDao.findBySql(getSql3(id,code),null, Experts.class);
	}
	
	/**
	 * 手机专家列表查询，传入参数为最子节点的品种id。专家领域绑定的可以是最子节点品种id也可以是最子节点的parentid
	 * @param productLibraryId
	 * @return
	 */
	public String getSql3(String productLibraryId, String code) {
		StringBuffer sql = new StringBuffer(300);
		sql.append("SELECT a.* FROM  ( ");
		sql.append(" SELECT e.* FROM t_agro_experts_professionalfield_relation p,t_agro_experts e WHERE e.id = p.experts_id and e.states<>'D' ");
		if (StringUtils.isNotBlank(code)) {
			sql.append(" AND e.platform LIKE '%"+code+"%' ");
		}
		if (StringUtils.isNotBlank(productLibraryId) && !productLibraryId.equals("0")) {
			sql.append(" AND p.product_library_id IN (SELECT lt.id FROM t_agro_product_library_tree lt WHERE (lt.id='"+productLibraryId+"' OR lt.parents_ids LIKE '%"+productLibraryId+",%') AND lt.states<>'D') ");
		}
		sql.append(" ) a order by a.create_time desc");
		return new String(sql);
	}

	@Transactional(readOnly = false)
	public void savaProfessionalfield(String  userid,String [] arr) {
		Experts  experts = getExpertByUserId(userid);
		List<ExpertsProfessionalfieldRelation> list = getExpertsProfessionalfieldRelationList(experts.getId());
		for(int m = 0; m < list.size(); m++) {
			expertsProfessionalfieldRelationDao.deleteByXGXTId(list.get(m).getId());
		}
		for(int i = 0; i < arr.length; i++) {
			ExpertsProfessionalfieldRelation professionalfield = new ExpertsProfessionalfieldRelation();
			professionalfield.setCreateTime(new Date());
			professionalfield.setCreateUserId(userid);
			professionalfield.setExpertsId(experts.getId());
			professionalfield.setProductLibraryId(arr[i]);
			expertsProfessionalfieldRelationDao.save(professionalfield);
		}
	
	}
	
	
	public List<ExpertsProfessionalfieldRelation> getExpertsProfessionalfieldRelationList(String expertsId) {
		DetachedCriteria dc = expertsProfessionalfieldRelationDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Experts.FIELD_DEL_FLAG_XGXT, Experts.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("expertsId", expertsId));
		return expertsProfessionalfieldRelationDao.find(dc);
	}
	
	
	@Transactional(readOnly = false)
	public void save(Experts experts) {
		expertsDao.save(experts);
	}

	@Transactional(readOnly = false)
	public void delete(String id, String userId) {
		Experts experts = this.getExperts(id, userId);
		if (experts != null) {
			expertsDao.deleteById(id);
		}

	}

	public Experts getExperts(String id, String userId) {
		DetachedCriteria dc = expertsDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Experts.FIELD_DEL_FLAG_XGXT, Experts.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("id", id));
		dc.add(Restrictions.eq("userId", userId));
		List<Experts> list = expertsDao.find(dc);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return new Experts();
		}
	}

	public Experts getExperts(String userId) {
		Page<Experts> page = new Page<Experts>();
		DetachedCriteria dc = expertsDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Experts.FIELD_DEL_FLAG_XGXT, Experts.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("userId", userId));
		page = expertsDao.find(page, dc);
		List<Experts> addressList = page.getList();
		if (addressList != null && addressList.size() > 0) {
			return addressList.get(0);
		}
		return null;
	}

	/**
	 * PC端专家页面查询
	 * 
	 * @param nameAndloginName
	 *            姓名/登录名
	 * @param goodProblem
	 *            擅长问题
	 * @param productLibraryId
	 *            擅长领域
	 * @param state
	 *            状态
	 * @param pageno
	 *            页数
	 * @param pagesize
	 *            条数
	 * @return
	 */
	public Page<Experts> getPcExpertsList(String nameAndloginName, String goodProblem, String productLibraryId, String state, Integer pageno, Integer pagesize, User u) {
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Experts> page = new Page<Experts>(pageNo, pageSize);
		Page<Experts> findPage = findPage(page, nameAndloginName, goodProblem, productLibraryId, state, u);
		List<Experts> list = findPage.getList();
		if (page.getList().size() > 0) {
			List<ExpertsUserInfoResp> respArrayList = new ArrayList<ExpertsUserInfoResp>();
			for (Iterator<Experts> iterator = list.iterator(); iterator.hasNext();) {
				Experts experts = (Experts) iterator.next();
				experts.setProductLibraryList(productLibraryTreeService.getList(experts.getId()));
				experts.setExpertsGoodproblemList(expertsGoodproblemService.getList(experts.getId()));
				if(u.isAdmin()){
					experts.setPlatformName(getPlatformNameByPlatform(experts.getPlatform()));
				}else{
					experts.setPlatformName(getPlatformNameByPlatform(getPlatformByUser(u)));
				}
				
				//User user = systemService.getUserByLoginName(experts.getUserCode());
				//if (user != null) {
					ExpertsUserInfoResp resp = new ExpertsUserInfoResp();
					resp.setName(experts.getExpertName());
					resp.setLoginName(experts.getUserCode());
					resp.setUserImg(experts.getUserImg());
					if(experts.getStates().equals("A") || experts.getStates().equals("U")){
						resp.setDelFlag("0");
					}else{
						resp.setDelFlag("1");
					}
					resp.setPhone(experts.getPhone());
					respArrayList.add(resp);
					experts.setResp(resp);
				//}
			}
		}
		return findPage;
	}
	// 专家服务统计
	public Page<Experts> getPcExpertsReportList(String name, String officeId, String time, String state, Integer pageno, Integer pagesize, User currUser) {
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Experts> page = new Page<Experts>(pageNo, pageSize);
		Page<Experts> findPage = findPageReport(page, name, state, currUser);
		List<Experts> list = findPage.getList();
		if (page.getList().size() > 0) {
			List<ExpertsUserInfoResp> respArrayList = new ArrayList<ExpertsUserInfoResp>();
			for (Iterator<Experts> iterator = list.iterator(); iterator.hasNext();) {
				Experts experts = (Experts) iterator.next();
				User user = systemService.getUserByLoginName(experts.getUserCode());
				if (user != null) {
					List<Map<String,Object>> lists = expertsServiceInfoDao.getMonthCount(user.getId(),time,officeId);
					Map map=new HashMap();
					for(int i=0;i<lists.size();i++){
						map.put("month"+lists.get(i).get("month").toString(), lists.get(i).get("count").toString());
					}
					ExpertsUserInfoResp resp = new ExpertsUserInfoResp();
					resp.setName(user.getName());
					resp.setLoginName(user.getLoginName());
					resp.setUserImg(user.getUserImg());
					resp.setDelFlag(user.getDelFlag());
					resp.setPhone(user.getPhone());
					respArrayList.add(resp);
					experts.setResp(resp);
					experts.setListMap(map);
				}
			}
		}
		return findPage;
	}
	/**
	 * 查询专业管理 分页
	 * 
	 * @param page
	 *            姓名或者登录名
	 * @param nameAndloginName
	 *            擅长问题
	 * @param goodProblem
	 *            擅长领域
	 * @param professionalfieldId
	 *            状态
	 * @param state
	 * 
	 * @return
	 */
	public Page<Experts> findPage(Page<Experts> page, String nameAndloginName, String goodProblem, String productLibraryId, String state, User user) {
		String sql = "SELECT a.* FROM t_agro_experts a where 1=1 ";
		if (StringUtils.isNotBlank(nameAndloginName)) {
			sql += " and ((a.expert_name like '%" + nameAndloginName + "%') or (a.user_code like '%" + nameAndloginName + "%'))";
		}
		
		String code = "";
		if(user.isAdmin()){
			code = "";
		}else{
			code = getPlatformByUser(user);
		}
		if (StringUtils.isNotBlank(code)) {
			sql += " and a.platform LIKE '%" + code + "%'";
		}
		
		if (StringUtils.isNotBlank(goodProblem)) {
			List<ExpertsGoodproblem> list = expertsGoodproblemDao.findByLikeGoodProblem(goodProblem);
			String str = "";
			for (ExpertsGoodproblem pojo : list) {
				str += "'" + pojo.getExpertId() + "'" + ",";
			}
			if (StringUtils.isNotBlank(str)) {
				str = str.substring(0, str.length() -1);
			} else {
				str = "''";
			}
			sql += "and a.id in (" + str + ")";
		}
		
		if (StringUtils.isNotBlank(productLibraryId) && !"1".equals(productLibraryId)) {
			// 品种集合
			List<ProductLibraryTree> pltArrayList = new ArrayList<ProductLibraryTree>();
			// 查询当前选择的品种
			ProductLibraryTree pojo = productLibraryTreeService.get(productLibraryId);
			pltArrayList.add(pojo);
			// 查询是否有品种的下级关系
			List<ProductLibraryTree> arrayList = speciesManageApiService.findProductLibraryTree(pojo, "1", false);
			for (ProductLibraryTree productLibraryTree : arrayList) {
				pltArrayList.add(productLibraryTree);
			}
			
			String pltStr = "";
			for (ProductLibraryTree productLibraryTree : pltArrayList) {
				pltStr += "'" + productLibraryTree.getId() + "'" + ",";
			}
			pltStr = pltStr.substring(0, pltStr.length() - 1);
			List<ExpertsProfessionalfieldRelation> findByLikeProductLibraryId = expertsProfessionalfieldRelationDao.findByLikeProductLibraryId("(" + pltStr + ")");
			
			String str = "";
			if (!findByLikeProductLibraryId.isEmpty()) {
				for (ExpertsProfessionalfieldRelation epr : findByLikeProductLibraryId) {
					str += "'" + epr.getExpertsId() + "'" + ",";
				}
			}
			if (StringUtils.isNotBlank(str)) {
				str = str.substring(0, str.length() - 1);
			} else {
				str = "''";
			}
			sql += "and a.id in (" + str + ")";
		}
		
		if(StringUtils.isNotBlank(state) && "0".equals(state)) {
			sql += " and a.states<>'D'";
		}
		
		if(StringUtils.isNotBlank(state) && "1".equals(state)) {
			sql += " and a.states='D'";
		}
		sql += " order by a.create_time desc";
		return expertsDao.findBySql(page, sql, Experts.class);
	}

	public Page<Experts> findPageReport(Page<Experts> page, String name, String state, User currentUser) {
		String code = "";
		String parentIds = currentUser.getOffice().getParentIds();
		String officeId = currentUser.getOffice().getId();
		if(parentIds.indexOf(StaticStringUtils.AGRO_TULUFAN)>-1 || officeId.equals(StaticStringUtils.AGRO_TULUFAN)){
			code = "tulufan";
		}else if(parentIds.indexOf(StaticStringUtils.AGRO_WANGCHENG)>-1 || officeId.equals(StaticStringUtils.AGRO_WANGCHENG)){
			code = "wangcheng";
		}else if(parentIds.indexOf(StaticStringUtils.AGRO_LIUYANG)>-1 || officeId.equals(StaticStringUtils.AGRO_LIUYANG)){
			code = "rice";
		}else if(parentIds.indexOf(StaticStringUtils.AGRO_NANXIAN)>-1 || officeId.equals(StaticStringUtils.AGRO_NANXIAN)){
			code = "nanxian";
		}
		String sql = "SELECT a.* FROM t_agro_experts a, sys_user b where a.user_code = b.LOGIN_NAME AND a.platform LIKE '%" + code + "%'";
		if (StringUtils.isNotBlank(name)) {
			sql += " and ((a.expert_name like '%" + name + "%') or (a.user_code like '%" + name + "%'))";
		}
		if(StringUtils.isNotBlank(state) && "0".equals(state)) {
			sql += " and b.del_flag = '0' and a.states <>'D'";
		}
		if(StringUtils.isNotBlank(state) && "1".equals(state)) {
			sql += " and b.del_flag = '1' and a.states = 'D'";
		}
		sql+= " ORDER BY a.create_time DESC";
		Page<Experts> find = expertsDao.findBySql(page, sql, Experts.class);
		return find;
	}

	/**
	 * 启用禁用专家状态
	 * 
	 * @param id
	 *            主键ID
	 * @param states
	 *            状态
	 * @return
	 */
	public String updateExpertsStates(String id, String operType) {
		String states = "";
		Experts experts = expertsDao.get(id);
		User user = systemService.getUserByLoginName(experts.getUserCode());
		//同步到统一管理平台
		JSONObject json = modifyUser(user.getLoginName(), user.getName(), user.getPhone(), user.getPassword(), user.getPassword(), user.getOffice().getKuid(), user.getUserImg(), user.getEmail(), operType);
		if(json==null){
			return "fail";
		}else{
			if(!json.getString("code").equals("0")){
				return "fail";
			}
		}
		if ("0".equals(operType)) {
			states = Experts.STATE_FLAG_UPDATE;
		} else if ("1".equals(operType)) {
			states = Experts.STATE_FLAG_DEL;
		}
		experts.setStates(states);
		expertsDao.save(experts);

		Video video = videoService.getVideoByLoginName(user.getLoginName());
		if(video!=null){
			video.setStates(states);
			videoService.save(video);
		}
		
		return "Success";
	}

	/**
	 * 保存
	 * @param loginUser 
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	public String savaExperts(ExpertsReq req, User loginUser) {
		String url = Global.getConfig("sy_img_url");
		String imageUrl = "";
		Experts experts = new Experts();
		BeanUtils.copyProperties(req, experts);
		String leader = "";
		if(StringUtils.isBlank(req.getLeader())) {
			leader = "0";
		} else {
			if(req.getLeader().equals("是")){
				leader = "1";
			}else if(req.getLeader().equals("否")){
				leader = "0";
			}else{
				leader = req.getLeader();
			}
		}
		if (StringUtils.isNotBlank(req.getUserReq().getUserImg())) {
			if(req.getUserReq().getUserImg().indexOf("http")>-1){
				imageUrl = req.getUserReq().getUserImg();
			}else{
				imageUrl = url + req.getUserReq().getUserImg();
			}
		}
		if(StringUtils.isBlank(req.getId())) {
			JSONObject json = checkUser(req.getUserReq().getLoginName());
			if(!json.getString("code").equals("0")){
				return "existence";
			}
			
			experts.setExpertName(req.getUserReq().getName());
			experts.setCreateTime(new Date());
			experts.setCreateUserId(loginUser.getId());
			experts.setStates(Experts.STATE_FLAG_ADD);
			experts.setLeader(leader);
			experts.setUserCode(req.getUserReq().getLoginName());
			experts.setUserImg(imageUrl);
			experts.setPhone(req.getUserReq().getPhone());
			
			if(loginUser.isAdmin()){
				if(req.getPlatform()!=null && req.getPlatform().length>0){
					experts.setPlatform(StringUtils.join(req.getPlatform(), ","));
				}
			}else{
				experts.setPlatform(getPlatformByUser(loginUser));
			}
			expertsDao.save(experts);
			
			String pwd = SystemService.entryptPassword(req.getUserReq().getPassword());
			//同步到管理平台用户表
			json = addUser(req.getUserReq().getLoginName(), req.getUserReq().getName(), req.getUserReq().getPhone(), pwd, pwd , "0", "fbbef01edb1d4392983e0ceea350a76f", imageUrl, req.getUserReq().getEmail());
			if(json==null){
				return "fail";
			}else{
				if(!json.getString("code").equals("0")){
					return "fail";
				}
			}
			
			//添加视频账号
			Video video = new Video();
			video.setUserCode(req.getUserReq().getLoginName());
			video.setUserName(req.getUserReq().getName());
			video.setOnlineStates("1");
			video.setInRoom("0");
			video.setCreateUserId(loginUser.getId());
			video.setType("专家");
			if(loginUser.isAdmin()){
				if(req.getPlatform()!=null && req.getPlatform().length>0){
					video.setPlatform(StringUtils.join(req.getPlatform(), ","));
				}
			}else{
				video.setPlatform(getPlatformByUser(loginUser));
			}
			
			Video V1 = videoService.getVideo("专家");
			int num = Integer.parseInt(V1.getCreateRoomNumber()) + 1;
			List<Video> vd  = videoService.verificationRoomNumber(String.valueOf(num));
			if(vd != null && vd.size() > 0) {
				num = num + 1;
			}
			WebRTCSigApi web = new WebRTCSigApi();
			String uToken = web.getPrivateMapKey(num , req.getUserReq().getLoginName());
			video.setCreateRoomNumber(String.valueOf(num));
			video.setUserToken(uToken);
			videoService.save(video);
			
			// 新增擅长领域
			for (ProductLibraryTreeListReq pltListReq : req.getPltListReq()) {
				ExpertsProfessionalfieldRelation pojo = new ExpertsProfessionalfieldRelation();
				pojo.setExpertsId(experts.getId());
				pojo.setProductLibraryId(pltListReq.getId());
				agroExpertsProfessionalfieldRelationDao.save(pojo);
			}
			
			// 新增擅长问题
			if(StringUtils.isNotBlank(req.getGoodProblem())) {
				String [] arr = req.getGoodProblem().split("\\s+");
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < arr.length; i++) {
					if (!list.contains(arr[i])) {
						list.add(arr[i]);
					}
				}
				for (String str : list) {
					ExpertsGoodproblem egPojo = new ExpertsGoodproblem();
					egPojo.setExpertId(experts.getId());
					egPojo.setGoodProblem(str);
					
					egPojo.setCreateTime(new Date());
					egPojo.setCreateUserId(loginUser.getId());
					egPojo.setStates(ExpertsGoodproblem.STATE_FLAG_ADD);
					expertsGoodproblemDao.save(egPojo);
				}
			}
			return "Success";
		} else if (StringUtils.isNotBlank(req.getId())) {
			// 查询专家
			Experts ep = expertsDao.findById(req.getId());
			User user = userDao.get(ep.getUserId());
			
			if (StringUtils.isNotBlank(req.getUserReq().getName())) {
				experts.setExpertName(req.getUserReq().getName());
			}
			
			if (StringUtils.isNotBlank(req.getUserReq().getUserImg())) {
				if(req.getUserReq().getUserImg().indexOf("http")>-1){
					imageUrl = req.getUserReq().getUserImg();
				}else{
					imageUrl = url + req.getUserReq().getUserImg();
				}
			}
			
			String pwd = user.getPassword();
			if(StringUtils.isNotBlank(req.getUserReq().getPassword())){
				pwd = SystemService.entryptPassword(req.getUserReq().getPassword());
			}
			//同步到统一管理平台
			JSONObject json = modifyUser(req.getUserReq().getLoginName(), req.getUserReq().getName(), req.getUserReq().getPhone(), pwd, pwd, user.getOffice().getKuid(), imageUrl, req.getUserReq().getEmail(), "0");
			if(json==null){
				return "fail";
			}else{
				if(!json.getString("code").equals("0")){
					return "fail";
				}
			}
			experts.setUserCode(req.getUserReq().getLoginName());
			experts.setCreateTime(ep.getCreateTime());
			experts.setUpdateTime(new Date());
			experts.setUpdateUserId(loginUser.getId());
			experts.setUserId(ep.getUserId());
			experts.setCreateUserId(ep.getCreateUserId());
			experts.setStates(Experts.STATE_FLAG_UPDATE);
			experts.setLeader(leader);
			experts.setUserImg(imageUrl);
			experts.setPhone(req.getUserReq().getPhone());
			if(req.getPlatform()!=null && req.getPlatform().length>0){
				experts.setPlatform(StringUtils.join(req.getPlatform(), ","));
			}
			expertsDao.clear();
			expertsDao.flush();
			expertsDao.save(experts);
			
			Video video = videoService.getVideoByLoginName(req.getUserReq().getLoginName());
			if(video!=null){
				video.setUserName(req.getUserReq().getName());
				videoService.save(video);
			}

			expertsGoodproblemDao.delete(experts.getId());
			agroExpertsProfessionalfieldRelationDao.delete(experts.getId());

			// 新增擅长领域
			for (ProductLibraryTreeListReq pltListReq : req.getPltListReq()) {
				ExpertsProfessionalfieldRelation pojo = new ExpertsProfessionalfieldRelation();
				pojo.setExpertsId(experts.getId());
				pojo.setProductLibraryId(pltListReq.getId());
				pojo.setCreateTime(new Date());
				pojo.setCreateUserId(loginUser.getId());
				pojo.setStates(ExpertsGoodproblem.STATE_FLAG_ADD);
				agroExpertsProfessionalfieldRelationDao.save(pojo);
			}

			// 新增擅长问题
			if (StringUtils.isNotBlank(req.getGoodProblem())) {
				String[] arr = req.getGoodProblem().split("\\s+");
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < arr.length; i++) {
					if (!list.contains(arr[i])) {
						list.add(arr[i]);
					}
				}
				for (String str : list) {
					ExpertsGoodproblem egPojo = new ExpertsGoodproblem();
					egPojo.setExpertId(experts.getId());
					egPojo.setGoodProblem(str);

					egPojo.setCreateTime(new Date());
					egPojo.setCreateUserId(loginUser.getId());
					egPojo.setStates(ExpertsGoodproblem.STATE_FLAG_ADD);
					expertsGoodproblemDao.save(egPojo);
				}
			}
			return "Success";
		}
		return "fail";
	}
	
	
	public ExpertsResp getExpertsInfo(String id) {
		Experts experts = expertsDao.get(id);
		ExpertsResp resp = new ExpertsResp();
		BeanUtils.copyProperties(experts, resp);
		
		User user = systemService.getUserByLoginName(experts.getUserCode());
		UserReq userReq = new UserReq();
		userReq.setName(user.getName());
		userReq.setLoginName(user.getLoginName());
		userReq.setUserImg(user.getUserImg());
		userReq.setPhone(user.getPhone());
		userReq.setEmail(user.getEmail());
		resp.setUserReq(userReq);
		resp.setLeader(experts.getLeader());
		if(StringUtils.isNotBlank(experts.getPlatform())){
			resp.setPlatform(experts.getPlatform().split(","));
		}
		List<ExpertsGoodproblem> egbList = expertsGoodproblemDao.getExpertsGoodproblemList(experts.getId());
		if (egbList != null && egbList.size() > 0) {
			String strs = "";
			for (ExpertsGoodproblem expertsGoodproblem : egbList) {
				strs += expertsGoodproblem.getGoodProblem() + " ";
			}
			String substring = strs.substring(0, strs.length() - 1);
			resp.setGoodProblem(substring);
		}
		List<ExpertsProfessionalfieldRelation> aeprList = agroExpertsProfessionalfieldRelationDao.getExpertsProfessionalfieldRelation(experts.getId());
		if (aeprList != null && aeprList.size() > 0) {
			List<ProductLibraryTreeListResp> pltListResp = new ArrayList<ProductLibraryTreeListResp>();
			for (ExpertsProfessionalfieldRelation aepfrList : aeprList) {
				ProductLibraryTree agroProductLibraryTree = productLibraryTreeService.get(aepfrList.getProductLibraryId());
				ProductLibraryTreeListResp pltResp = new  ProductLibraryTreeListResp();
				pltResp.setId(agroProductLibraryTree.getId());
				pltResp.setName(agroProductLibraryTree.getProductCategoryName());
				pltListResp.add(pltResp);
			}
			resp.setPltListResp(pltListResp);
		}
		return resp;
	}
	
	public List<Experts> getExpertsListByProductId(ProductLibraryTree productLibrary) {
		String sql="SELECT t.id FROM t_agro_product_library_tree t WHERE t.id=:p1 and t.states<>'D' UNION ALL"
			+" SELECT t.id FROM t_agro_product_library_tree t WHERE t.parents_ids=:p2 and t.states<>'D' ";
		List<String> list = expertsDao.findBySql(sql,new Parameter(productLibrary.getParent().getId(),productLibrary.getParentsIds()));
		String str = "";
		if(list.size()>0){
			String data = StringUtils.join(list, "','");
			str = "'"+data+"'";
			String expertsSql = "SELECT c.* FROM"
				+" t_agro_experts_professionalfield_relation b,"
			    +" t_agro_experts c "
			    +" WHERE b.experts_id = c.id "
			    +" AND b.states<>'D' "
			    +" AND c.states<>'D'"
			    +" AND b.product_library_id in (" + str + ")";
			return expertsDao.findBySql(expertsSql,null,Experts.class);
		}else{
			String expertsSql = "SELECT c.* FROM"
					+" t_agro_experts_professionalfield_relation b,"
				    +" t_agro_experts c "
				    +" WHERE b.experts_id = c.id "
				    +" AND b.states<>'D' "
				    +" AND c.states<>'D'";
			return expertsDao.findBySql(expertsSql,null,Experts.class);
		}
	}
	
	public int getExpertsCount(String productId) {
		String sql = " SELECT e.* FROM t_agro_experts e WHERE e.states<>'D' ";
		if (StringUtils.isNotBlank(productId) && !productId.equals("0")) {
			sql+=" AND e.id IN (SELECT p.experts_id FROM t_agro_product_library_tree lt,t_agro_experts_professionalfield_relation p WHERE lt.id = p.product_library_id AND (lt.id='"+productId+"' OR lt.parents_ids LIKE '%"+productId+",%') AND lt.states<>'D') ";
		}
		List<Experts> list = expertsDao.findBySql(sql.toString(),null,Experts.class);
		return list==null?0:list.size();
	}
	
	/**
	 * 种类id查询专家列表
	 * @param expertsId
	 * @return
	 */
	public List<Experts> findPeopleListList(String productId) {
		StringBuffer sql = new StringBuffer(300);
		sql.append(" SELECT e.* FROM t_agro_experts e WHERE e.states<>'D' ");
		if (StringUtils.isNotBlank(productId) && !productId.equals("0")) {
			sql.append(" AND e.id IN (SELECT p.experts_id FROM t_agro_product_library_tree lt,t_agro_experts_professionalfield_relation p WHERE lt.`id` = p.`product_library_id` AND (lt.id='"+productId+"' OR lt.parents_ids LIKE '%"+productId+",%') AND lt.states<>'D') ");
		}
		return expertsDao.findBySql(sql.toString(),null,Experts.class);
	}
	
	/**
	 * 根据种类ID查询
	 * @param id
	 * @return
	 */
	public Experts getExpertsByUserCode(String userCode){
		String sql = "select e.* from t_agro_experts e where e.states<>'D' and e.user_code=:p1";
		List<Experts> list = expertsDao.findBySql(sql, new Parameter(userCode), Experts.class);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 新增云通信账号
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	public JSONObject addIMAccout(String userCode,String userName,String imageUrl) {
		String url = RestAPI.restUrl("im_open_login_svc", "account_import");
		JSONObject json = new JSONObject();
		json.put("Identifier", userCode);		
		json.put("Nick", userName);			
		json.put("FaceUrl", imageUrl);
		String post = Client.post(url, json, "UTF-8");
		JSONObject at_obj = JSONObject.fromObject(post);
		System.out.println("返回数据"+at_obj);
		return at_obj;
	}
	
	/**
	 * 修改云通信账号
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	public JSONObject editIMAccout(String userCode,String userName,String imageUrl) {
		String url = RestAPI.restUrl("profile", "portrait_set");
		JSONObject json = new JSONObject();
		json.put("From_Account", userCode);		
		JSONArray jsonArr = new JSONArray();
		JSONObject js = new JSONObject();
		js.put("Tag", "Tag_Profile_IM_Nick");
		js.put("Value", userName);
		jsonArr.add(js);
		js = new JSONObject();
		js.put("Tag", "Tag_Profile_IM_Image");
		js.put("Value", imageUrl);
		jsonArr.add(js);
		json.put("ProfileItem", jsonArr);	
		String post = Client.post(url, json, "UTF-8");
		JSONObject at_obj = JSONObject.fromObject(post);
		System.out.println("返回数据"+at_obj);
		return at_obj;
	}
	
	/**
	 * 用户验证
	 * @author
	 * @param loginName
	 * @return
	 */
	public JSONObject checkUser(String loginName) {
		String url = Global.getConfig("wcspadmin_img_url") + "api/checkUser";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("loginName", loginName));
        String body = Client.get(url, params, "utf-8");
        JSONObject at_obj = JSONObject.fromObject(body);
		return at_obj;
	}
	
	/**
	 * 添加用户
	 * @author
	 * @param loginName
	 * @return
	 */
	public JSONObject addUser(String loginName, String name, String phone, String pwd, String confirmPwd, String officeId, String roleIds, String userImg, String email) {
		String url = Global.getConfig("wcspadmin_img_url") + "api/register";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("loginName", loginName));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("pwd", pwd));
        params.add(new BasicNameValuePair("confirmPwd", confirmPwd));
        params.add(new BasicNameValuePair("officeId", officeId));
        params.add(new BasicNameValuePair("roleIds", roleIds));
        if(StringUtils.isNotBlank(userImg)){
        	params.add(new BasicNameValuePair("userImg", userImg));
        }
        if(StringUtils.isNotBlank(email)){
        	params.add(new BasicNameValuePair("email", email));
        }
        String body = Client.get(url, params, "utf-8");
        JSONObject at_obj = JSONObject.fromObject(body);
		System.out.println("返回数据"+at_obj);
		return at_obj;
	}
	
	/**
	 * 修改用户
	 * @author
	 * @param loginName
	 * @return
	 */
	public JSONObject modifyUser(String loginName, String name, String phone, String pwd, String confirmPwd, String officeId, String userImg, String email, String delFlag) {
		String url = Global.getConfig("wcspadmin_img_url") + "api/modefyUserInfo";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("loginName", loginName));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("pwd", pwd));
        params.add(new BasicNameValuePair("confirmPwd", confirmPwd));
        params.add(new BasicNameValuePair("officeId", officeId));
        if(StringUtils.isNotBlank(userImg)){
        	params.add(new BasicNameValuePair("userImg", userImg));
        }
        if(StringUtils.isNotBlank(email)){
        	params.add(new BasicNameValuePair("email", email));
        }
        params.add(new BasicNameValuePair("delFlag", delFlag));
        String body = Client.get(url, params, "utf-8");
        JSONObject at_obj = JSONObject.fromObject(body);
		System.out.println("返回数据"+at_obj);
		return at_obj;
	}
	
	public String getPlatformByUser(User user) {
		String code = "";
		String sql = "SELECT d.* FROM sys_dict d WHERE d.TYPE='platform' AND d.DEL_FLAG='0' ORDER BY d.SORT";
		List<Dict> dictList = dictDao.findBySql(sql, null, Dict.class);
		for (Dict dict : dictList) {
			String officeId = dict.getDescription();
			String value = dict.getValue();
			sql = "SELECT a.* FROM sys_office a WHERE a.DEL_FLAG='0' AND (a.ID=:p1 OR a.PARENT_IDS LIKE '%:p1,%') AND a.id = :p2";
			List<Object> list = expertsDao.findBySql(sql, new Parameter(officeId, user.getOffice().getId()));	
			if(list!=null && list.size()>0){
				code = value;
				break;
			}
		}
		return code;
	}
	
	public String getPlatformNameByPlatform(String platform) {
		String platformName = "";
		String[] str = platform.split(",");
		for (String ss : str) {
			String sql = "select d.* from sys_dict d where d.type='platform' and d.del_flag='0' and d.value=:p1 order by d.sort";
			List<Dict> dictList = dictDao.findBySql(sql, new Parameter(ss), Dict.class);
			if(dictList!=null && dictList.size()>0){
				platformName+= dictList.get(0).getLabel()+",";
			}
		}
		if(StringUtils.isNotBlank(platformName)){
			platformName = platformName.substring(0, platformName.length()-1);
		}
		return platformName;
	}
	
	public void updateUserIdByUserCode(){
		String sql = "select e.* from t_agro_experts e where e.states<>'D' AND (e.user_id='' OR e.user_id IS NULL)";
		List<Experts> list = expertsDao.findBySql(sql,null,Experts.class);
		for (Experts experts : list) {
			String userCode = experts.getUserCode();
			User user = systemService.getUserByLoginName(userCode);
			if(user!=null){
				experts.setUserId(user.getId());
				expertsDao.save(experts);
			}
		}
	}

	public List<Experts> getBaseExpertsList(String platform,User u) {
		DetachedCriteria dc = expertsDao.createDetachedCriteria();
		dc.add(Restrictions.ne(Experts.FIELD_DEL_FLAG_XGXT, Experts.STATE_FLAG_DEL));
		if(StringUtils.isNotBlank(platform)){
			dc.add(Restrictions.like("platform", platform,MatchMode.ANYWHERE));
		}
		dc.addOrder(Order.asc("expertName"));
		List<Experts> list = expertsDao.find(dc);
		if (list.size() > 0) {
			List<ExpertsUserInfoResp> respArrayList = new ArrayList<ExpertsUserInfoResp>();
			for (Iterator<Experts> iterator = list.iterator(); iterator.hasNext();) {
				Experts experts = (Experts) iterator.next();
				experts.setProductLibraryList(productLibraryTreeService.getList(experts.getId()));
				//User user = systemService.getUserByLoginName(experts.getUserCode());
				//if (user != null) {
					ExpertsUserInfoResp resp = new ExpertsUserInfoResp();
					resp.setName(experts.getExpertName());
					resp.setLoginName(experts.getUserCode());
					resp.setUserImg(experts.getUserImg());
					if(experts.getStates().equals("A") || experts.getStates().equals("U")){
						resp.setDelFlag("0");
					}else{
						resp.setDelFlag("1");
					}
					resp.setPhone(experts.getPhone());
					respArrayList.add(resp);
					experts.setResp(resp);
			}
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			for(Experts experts:list){
				Map<String, Object> map = new HashMap<String, Object>();
				String szm = ChineseCharToEnUtil.getFirstLetter(experts.getExpertName());
				if (szm.length() > 0) {
					szm = szm.substring(0, 1).toUpperCase();
					map.put(szm, experts);
					listMap.add(map);
				}
			}
			Map<String, Object> rmap = new HashMap<String, Object>();
			List<Experts> rlist = new ArrayList<Experts>();
			Map<String, Object> office = new HashMap<String, Object>();
			for (Map<String, Object> map : listMap) {
				Iterator it = map.entrySet().iterator();
				Entry entry = (Entry) it.next();
				String key = (String) entry.getKey();
				Experts value = (Experts) entry.getValue();
				if (rmap.containsKey(key)) {
					List<Experts> slist = (List<Experts>) rmap.get(key);
					slist.add(value);
					rmap.put(key, slist);
				} else {
					rlist = new ArrayList<Experts>();
					rlist.add(value);
					rmap.put(key, rlist);
				}
			}
			rmap = sortMapByKey(rmap);
			List<Experts> lists = new ArrayList<Experts>();
			for (String key : rmap.keySet()) {
				List<Experts> list2= (List<Experts>)rmap.get(key);
				lists.addAll(list2);
			}
			return lists;
		}
		return list;
	}

	/**
	 * 使用 Map按key进行排序
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> sortMapByKey(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());
		sortMap.putAll(map);
		return sortMap;
	}
	public List<Experts> getBaseExpertsList(String baseId) {
		String sql = "select r.leader,e.* from t_agro_experts e LEFT JOIN t_agro_base_experts_relation r ON e.id=r.expert_id where e.states<>'D' and r.base_id=:p1 ORDER BY e.expert_name";
		List<Experts> list = expertsDao.findBySql(sql, new Parameter(baseId),Experts.class);
		if (list.size() > 0) {
			List<ExpertsUserInfoResp> respArrayList = new ArrayList<ExpertsUserInfoResp>();
			for (Iterator<Experts> iterator = list.iterator(); iterator.hasNext();) {
				Experts experts = (Experts) iterator.next();
				experts.setProductLibraryList(productLibraryTreeService.getList(experts.getId()));
				//User user = systemService.getUserByLoginName(experts.getUserCode());
				//if (user != null) {
					ExpertsUserInfoResp resp = new ExpertsUserInfoResp();
					resp.setName(experts.getExpertName());
					resp.setLoginName(experts.getUserCode());
					resp.setUserImg(experts.getUserImg());
					if(experts.getStates().equals("A") || experts.getStates().equals("U")){
						resp.setDelFlag("0");
					}else{
						resp.setDelFlag("1");
					}
					resp.setPhone(experts.getPhone());
					respArrayList.add(resp);
					experts.setResp(resp);
			}
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			for(Experts experts:list){
				Map<String, Object> map = new HashMap<String, Object>();
				String szm = ChineseCharToEnUtil.getFirstLetter(experts.getExpertName());
				if (szm.length() > 0) {
					szm = szm.substring(0, 1).toUpperCase();
					map.put(szm, experts);
					listMap.add(map);
				}
			}
			Map<String, Object> rmap = new HashMap<String, Object>();
			List<Experts> rlist = new ArrayList<Experts>();
			Map<String, Object> office = new HashMap<String, Object>();
			for (Map<String, Object> map : listMap) {
				Iterator it = map.entrySet().iterator();
				Entry entry = (Entry) it.next();
				String key = (String) entry.getKey();
				Experts value = (Experts) entry.getValue();
				if (rmap.containsKey(key)) {
					List<Experts> slist = (List<Experts>) rmap.get(key);
					slist.add(value);
					rmap.put(key, slist);
				} else {
					rlist = new ArrayList<Experts>();
					rlist.add(value);
					rmap.put(key, rlist);
				}
			}
			rmap = sortMapByKey(rmap);
			List<Experts> lists = new ArrayList<Experts>();
			for (String key : rmap.keySet()) {
				List<Experts> list2= (List<Experts>)rmap.get(key);
				lists.addAll(list2);
			}
			return lists;
		}
		return list;
	}
	public List<Experts> getPlatFormExpertsList(String platform,String officeId,String baseId) {
		String sql = "";
		if(StringUtils.isNotBlank(baseId)){
			sql = "SELECT"
					+ " a.leader, e.*"
					+ " FROM"
					+ " t_agro_experts e"
					+ " LEFT JOIN ("
					+ " SELECT"
					+ " b.expert_id,b.leader,b.base_id"
					+ " FROM"
					+ " t_agro_base_experts_relation b,"
					+ " sys_office o"
					+ " WHERE"
					+ " o.ID = b.office_id"
					+ " AND ("
					+ " o.id = '"+officeId+"'"
					+ " OR o.PARENT_IDS LIKE '%,"+officeId+",%')) a"
					+ " ON e.id = a.expert_id"
					+ " WHERE"
					+ " e.platform like '%"+platform+"%'"
					+ " AND e.states <> 'D'"
					+ " AND a.base_id = '"+baseId+"'"
					+ " ORDER BY a.leader desc";
		} else {
			sql = "SELECT"
					+ " a.leader, e.*"
					+ " FROM"
					+ " t_agro_experts e"
					+ " LEFT JOIN ("
					+ " SELECT"
					+ " b.expert_id,b.leader"
					+ " FROM"
					+ " t_agro_base_experts_relation b,"
					+ " sys_office o"
					+ " WHERE"
					+ " o.ID = b.office_id"
					+ " AND b.leader = '1'"
					+ " AND ("
					+ " o.id = '"+officeId+"'"
					+ " OR o.PARENT_IDS LIKE '%,"+officeId+",%')"
					+ " GROUP BY"
					+ " b.expert_id) a"
					+ " ON e.id = a.expert_id"
					+ " WHERE"
					+ " e.platform like '%"+platform+"%'"
					+ " AND e.states <> 'D'"
					+ " ORDER BY a.leader desc";
		}
		List<Experts> list = expertsDao.findBySql(sql,null,Experts.class);
		if (list.size() > 0) {
			List<ExpertsUserInfoResp> respArrayList = new ArrayList<ExpertsUserInfoResp>();
			for (Experts experts: list) {
				if(StringUtils.isNotBlank(experts.getUserImg())&&!experts.getUserImg().contains("http")){
					experts.setUserImg(sy_img_url+experts.getUserImg());
				}
				experts.setProductLibraryList(productLibraryTreeService.getList(experts.getId()));
				List<CopyOfBaseTree> baseTreeList= baseManageApiService.findListByExpertsId(experts.getId(),officeId);
				if(baseTreeList.size()>0){
					experts.setBaseTree(baseTreeList);
				}
				ExpertsUserInfoResp resp = new ExpertsUserInfoResp();
				resp.setName(experts.getExpertName());
				resp.setLoginName(experts.getUserCode());
				resp.setUserImg(experts.getUserImg());
				if(experts.getStates().equals("A") || experts.getStates().equals("U")){
					resp.setDelFlag("0");
				}else{
					resp.setDelFlag("1");
				}
				resp.setPhone(experts.getPhone());
				respArrayList.add(resp);
				experts.setResp(resp);
			}
		}
		return list;
	}

}
