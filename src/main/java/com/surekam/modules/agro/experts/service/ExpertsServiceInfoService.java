package com.surekam.modules.agro.experts.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.experts.dao.ExpertsServiceInfoDao;
import com.surekam.modules.agro.experts.entity.ExpertServiceInfo;
import com.surekam.modules.agro.file.dao.AgroFileInfoDao;
import com.surekam.modules.agro.file.entity.AgroFileInfo;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;

/**
 * 专家服务信息Service
 * @author xy
 * @version 2019-04-28
 */
@Component
@Transactional(readOnly = true)
public class ExpertsServiceInfoService extends BaseService {

	@Autowired
	private ExpertsServiceInfoDao expertsServiceInfoDao;
	
	@Autowired
	private AgroFileInfoDao fileInfoDao2;
	
	@Autowired
	private OfficeDao officeDao;
	
	public ExpertServiceInfo get(String id) {
		return expertsServiceInfoDao.get(id);
	}
	/**
	 * 专家id查询单个专家的服务记录
	 * @param page
	 * @param userid
	 * @return
	 */
	public Page<ExpertServiceInfo> getExpertServiceInfoList(Page<ExpertServiceInfo> page,String userid) {
		DetachedCriteria dc = expertsServiceInfoDao.createDetachedCriteria();
		dc.add(Restrictions.ne(ExpertServiceInfo.FIELD_DEL_FLAG_XGXT, ExpertServiceInfo.STATE_FLAG_DEL));
		dc.add(Restrictions.eq("expertId",userid));
		dc.addOrder(Order.desc("createTime"));
		return expertsServiceInfoDao.find(page, dc);
	}
	/**
	 * 专家id查询单个专家的服务记录PC端用
	 * @param page
	 * @param userid
	 * @return
	 */
	public Page<ExpertServiceInfo> getPCExpertServiceInfoList(Page<ExpertServiceInfo> page,ExpertServiceInfo expertServiceInfo) {
		DetachedCriteria dc = expertsServiceInfoDao.createDetachedCriteria();
		dc.add(Restrictions.ne(ExpertServiceInfo.FIELD_DEL_FLAG_XGXT, ExpertServiceInfo.STATE_FLAG_DEL));
		if (StringUtils.isNotEmpty(expertServiceInfo.getExpertId())) {
			dc.add(Restrictions.like("expertId",expertServiceInfo.getExpertId()));
		}
		if (StringUtils.isNotEmpty(expertServiceInfo.getServiceType())) {
			dc.add(Restrictions.like("serviceType",expertServiceInfo.getServiceType()));
		}
		dc.add(Restrictions.ne("states", ExpertServiceInfo.STATE_FLAG_DEL));
		dc.addOrder(Order.desc("serviceTime"));
		return expertsServiceInfoDao.find(page, dc);
	}
	/**
	 * 专家id查询单个专家的服务记录PC端用
	 * @param page
	 * @param userid
	 * @return
	 */
	public Page<ExpertServiceInfo> getPCExpertServiceInfoList(Page<ExpertServiceInfo> page,ExpertServiceInfo expertServiceInfo,String officeId,User user) {
		DetachedCriteria dc = expertsServiceInfoDao.createDetachedCriteria();
		dc.add(Restrictions.ne(ExpertServiceInfo.FIELD_DEL_FLAG_XGXT, ExpertServiceInfo.STATE_FLAG_DEL));
		if (StringUtils.isNotEmpty(expertServiceInfo.getExpertId())) {
			dc.add(Restrictions.like("expertId",expertServiceInfo.getExpertId()));
		}
		if (StringUtils.isNotEmpty(expertServiceInfo.getServiceType())) {
			dc.add(Restrictions.like("serviceType",expertServiceInfo.getServiceType()));
		}
		if (StringUtils.isNotBlank(officeId)) {
			if(officeId.equals("1")) {
				List<Object> list = officeDao.getOfficeIdList(user.getCompany().getId());
				dc.add(Restrictions.in("serviceCompanyId", list));
			}else {
				List<Object> list = officeDao.getOfficeIdList(officeId);
				if(list != null && list.size() > 0) {
					dc.add(Restrictions.in("serviceCompanyId", list));
				}else {
					dc.add(Restrictions.eq("serviceCompanyId", officeId));
				}
				
			}
		}else {
			List<Object> list = officeDao.getOfficeIdList(user.getCompany().getId());
			dc.add(Restrictions.in("serviceCompanyId", list));
		}
		dc.addOrder(Order.desc("serviceTime"));
		return expertsServiceInfoDao.find(page, dc);
	}
	
	/**
	 * 全部专家的服务记录
	 * @param page
	 * @param userid
	 * @return
	 */
	public Page<ExpertServiceInfo> getExpertServiceInfoList(Page<ExpertServiceInfo> page) {
		DetachedCriteria dc = expertsServiceInfoDao.createDetachedCriteria();
		dc.add(Restrictions.ne(ExpertServiceInfo.FIELD_DEL_FLAG_XGXT, ExpertServiceInfo.STATE_FLAG_DEL));
		dc.addOrder(Order.desc("createTime"));
		return expertsServiceInfoDao.find(page, dc);
	}
	
	/**
	 * 全部专家的服务记录
	 * @param page
	 * @param userid
	 * @return
	 */
	public Page<ExpertServiceInfo> getExpertServiceInfoList1(Page<ExpertServiceInfo> page, String code) {
		String id = "2";
		if (code.equals("wangcheng")) {
			id = "a2bfcffa042646a98360ef86343de977";
		} else if (code.equals("tulufan")) {
			id = "433620addfd144028f9d1afaabfe8299";
		} else if (code.equals("rice")) {
			id = "f76a6516f42647699fa1c7ceba53a511";
		} else if (code.equals("nanxian")) {
			id = "ddc5d5315cb049b6875906f2d3a2915d";
		} else if (code.equals("yuanxiang")) {
			id = "2";
		}
		String sql = "SELECT b.* FROM t_agro_experts_service b WHERE b.states <> 'D' "
				+" AND b.service_company_id IN (SELECT tt.id FROM sys_office tt WHERE tt.id = '"+id+"' OR tt.PARENT_IDS LIKE '%"+id+"%')";
		return expertsServiceInfoDao.findBySql(page, sql, null, ExpertServiceInfo.class);
	}
	
	@Transactional(readOnly = false)
	public void saveExpertsServiceInfo(User user,String officeId,String servicedate,String typeId,String address,String details,String [] photo,String [] auditUrl) {
		ExpertServiceInfo expertServiceInfo = new ExpertServiceInfo();
		expertServiceInfo.setCreateTime(new Date());
		expertServiceInfo.setCreateUserId(user.getId());
		expertServiceInfo.setExpertId(user.getId());
		expertServiceInfo.setServiceCompanyId(officeId);
		expertServiceInfo.setServiceTime(servicedate);
		expertServiceInfo.setServiceType(typeId);
		expertServiceInfo.setServiceAddress(address);
		expertServiceInfo.setServiceDetails(details);
		expertsServiceInfoDao.save(expertServiceInfo);
		String imgUrl = Global.getConfig("sy_img_url");
		for(int i=0;i < photo.length;i++) {
			AgroFileInfo fileInfo = new AgroFileInfo();
			fileInfo.setAbsolutePath(imgUrl+photo[i]);
			fileInfo.setUrl(photo[i]);
			fileInfo.setType("1");
			String str = photo[i].substring(photo[i].lastIndexOf("/")+1);//2342.png
			fileInfo.setFileName(str);//str.substring(0, str.indexOf(".")+1)
			fileInfo.setYwzbId(expertServiceInfo.getId());
			fileInfo.setYwzbType("ExpertServiceInfo");
			fileInfo.setCreateUserId(user.getId());
			fileInfoDao2.save(fileInfo);
		}
		
		for(int j=0;j < auditUrl.length;j++) {
			AgroFileInfo fileInfo = new AgroFileInfo();
			fileInfo.setUrl(auditUrl[j]);
			fileInfo.setAbsolutePath(imgUrl + auditUrl[j]);
			fileInfo.setType("4");
			String str = auditUrl[j].substring(auditUrl[j].lastIndexOf("/")+1);
			fileInfo.setFileName(str);
			fileInfo.setYwzbId(expertServiceInfo.getId());
			fileInfo.setYwzbType("ExpertServiceInfo");
			fileInfo.setCreateUserId(user.getId());
			fileInfoDao2.save(fileInfo);
		}
	}
	@Transactional(readOnly = false)
	public void saveExpertsServiceInfo(User user,String officeId,String servicedate,String typeId,String address,String details,String [] photo,String [] auditUrl,String baseId) {
		ExpertServiceInfo expertServiceInfo = new ExpertServiceInfo();
		expertServiceInfo.setCreateTime(new Date());
		expertServiceInfo.setCreateUserId(user.getId());
		expertServiceInfo.setExpertId(user.getId());
		expertServiceInfo.setServiceCompanyId(officeId);
		expertServiceInfo.setServiceTime(servicedate);
		expertServiceInfo.setServiceType(typeId);
		expertServiceInfo.setServiceAddress(address);
		expertServiceInfo.setServiceDetails(details);
		expertServiceInfo.setServiceBaseId(baseId);
		expertsServiceInfoDao.save(expertServiceInfo);
		String imgUrl = Global.getConfig("sy_img_url");
		for(int i=0;i < photo.length;i++) {
			AgroFileInfo fileInfo = new AgroFileInfo();
			fileInfo.setAbsolutePath(imgUrl+photo[i]);
			fileInfo.setUrl(photo[i]);
			fileInfo.setType("1");
			String str = photo[i].substring(photo[i].lastIndexOf("/")+1);//2342.png
			fileInfo.setFileName(str);//str.substring(0, str.indexOf(".")+1)
			fileInfo.setYwzbId(expertServiceInfo.getId());
			fileInfo.setYwzbType("ExpertServiceInfo");
			fileInfo.setCreateUserId(user.getId());
			fileInfoDao2.save(fileInfo);
		}
		
		for(int j=0;j < auditUrl.length;j++) {
			AgroFileInfo fileInfo = new AgroFileInfo();
			fileInfo.setUrl(auditUrl[j]);
			fileInfo.setAbsolutePath(imgUrl + auditUrl[j]);
			fileInfo.setType("4");
			String str = auditUrl[j].substring(auditUrl[j].lastIndexOf("/")+1);
			fileInfo.setFileName(str);
			fileInfo.setYwzbId(expertServiceInfo.getId());
			fileInfo.setYwzbType("ExpertServiceInfo");
			fileInfo.setCreateUserId(user.getId());
			fileInfoDao2.save(fileInfo);
		}
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(ExpertServiceInfo expertsService) {
		expertsServiceInfoDao.save(expertsService);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		expertsServiceInfoDao.deleteById(id);
	}
	
	public Map<String,Object> getAddress(String lon,String lat) throws Exception {
		String key = Global.getConfig("gaodeKey");
		String location = lon+","+lat;
		String locationUrl = "http://restapi.amap.com/v3/geocode/regeo?key=" + key + "&location=";
		String province = "";
		String city = "";
		String area = "";
		String address = "";
		Map<String,Object> map = new HashMap<String, Object>();
		URL url = new URL(locationUrl+location);
		HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
        ucon.connect();
        InputStream in = ucon.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        String str = reader.readLine(); 
        JSONObject jsonObject = new JSONObject(str);
        System.out.println(str);
        JSONObject obj1 = jsonObject.getJSONObject("regeocode");
        JSONObject obj2 = obj1.getJSONObject("addressComponent");
        if(!obj2.get("province").getClass().toString().equals("class org.json.JSONArray")){
        	 province = obj2.getString("province");
        }
        if(!obj2.get("city").getClass().toString().equals("class org.json.JSONArray")){
        	city = obj2.getString("city");
        }
        if(!obj2.get("district").getClass().toString().equals("class org.json.JSONArray")){
        	area = obj2.getString("district");
        }
        if(!obj1.get("formatted_address").getClass().toString().equals("class org.json.JSONArray")){
        	address = obj1.getString("formatted_address");
        }
        map.put("province", province);
        map.put("city", city);
        map.put("area", area);
        map.put("address", address);
		return map;
	}

	/**
	 * Title: delExpertsServiceInfo Description: 删除服务记录
	 * 
	 * @param user
	 *            用户记录
	 * @param id
	 *            主键
	 */
	public void delExpertsServiceInfo(User user, String id) {
		ExpertServiceInfo expertServiceInfo = expertsServiceInfoDao.get(id);
		expertsServiceInfoDao.clear();
		expertsServiceInfoDao.flush();
		expertServiceInfo.setStates(ExpertServiceInfo.STATE_FLAG_DEL);
		expertServiceInfo.setUpdateTime(new Date());
		expertServiceInfo.setUpdateUserId(user.getId());
		expertsServiceInfoDao.save(expertServiceInfo);
	}
	
	
	public Page<ExpertServiceInfo> getExpertServiceListAPP(Page<ExpertServiceInfo> page,String officeId) {
		String sql = "select es.* FROM t_agro_experts_service es LEFT JOIN sys_office so on es.service_company_id = so.id WHERE es.states<>'D' "
				+ " and so.id = '"+officeId+"' or so.PARENT_ID = '"+officeId+"' ORDER BY es.service_time desc ";
		return expertsServiceInfoDao.findBySql(page, sql, null, ExpertServiceInfo.class);
	}
}
