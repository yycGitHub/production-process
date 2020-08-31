package com.surekam.modules.activeMQ.syn.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.utils.WebRTCSigApi;
import com.surekam.modules.activeMQ.syn.dto.AreaDto;
import com.surekam.modules.activeMQ.syn.dto.MenuDto;
import com.surekam.modules.activeMQ.syn.dto.OfficeDto;
import com.surekam.modules.activeMQ.syn.dto.RoleDto;
import com.surekam.modules.activeMQ.syn.dto.UserDto;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.sys.dao.AreaDao;
import com.surekam.modules.sys.dao.MenuDao;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.dao.RoleDao;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.entity.Area;
import com.surekam.modules.sys.entity.Menu;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.Role;
import com.surekam.modules.sys.entity.User;
import net.sf.json.JSONObject;

/**
 * 同步Server
 */
@Service
@Transactional(readOnly = true)
public class SynClientServer{
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private ExpertsService expertsService;
	
	/**
	 * 同步区域数据
	 * @param jsonData
	 */
	@Transactional(readOnly = false)
	public boolean areaSyn(String jsonData){		
		JsonMapper jsonMapper = JsonMapper.getInstance();
		AreaDto areaDto = jsonMapper.fromJson(jsonData, AreaDto.class);
		Area area = areaDao.getByKuid(areaDto.getKuid());
		Area parent = areaDao.getByKuid(areaDto.getParentId());				
		//父节点
		String oldParentIds = "";
		if (area == null) {
			area = new Area();				
		}else{
			oldParentIds = area.getParentIds();
		}
		if (parent == null) {
			return false;
		}
		BeanUtils.copyProperties(areaDto, area);
		area.setParent(parent);
		area.setParentIds(parent.getParentIds()+parent.getId()+",");
		areaDao.save(area);		
		if (oldParentIds != null) {
			// 更新子节点 parentIds
			List<Area> list = areaDao.findByParentIdsLike("%,"+area.getId()+",%");
			for (Area e : list){
				e.setParentIds(e.getParentIds().replace(oldParentIds, area.getParentIds()));
			}
			areaDao.save(list);
		}		
		return true;
	}
	
	/**
	 * 同步公司数据
	 * @param jsonData
	 */
	@Transactional(readOnly = false)
	public boolean officeSyn(String jsonData){		
		JsonMapper jsonMapper = JsonMapper.getInstance();
		OfficeDto officeDto = jsonMapper.fromJson(jsonData, OfficeDto.class);
		Office office = officeDao.getByKuid(officeDto.getKuid());
		Office parent = officeDao.getByKuid(officeDto.getParentId());
		Area area = areaDao.getByKuid(officeDto.getAreaId());
		//父节点
		String oldParentIds = "";
		if (office == null) {
			office = new Office();				
		}else{
			oldParentIds = office.getParentIds();
		}
		if (parent == null) {
			return false;
		}
		if (area == null) {
			return false;
		}
		BeanUtils.copyProperties(officeDto, office);
		office.setArea(area);
		office.setParent(parent);
		office.setParentIds(parent.getParentIds()+parent.getId()+",");
		officeDao.save(office);		
		if (oldParentIds != null) {
			// 更新子节点 parentIds
			List<Office> list = officeDao.findByParentIdsLike("%,"+office.getId()+",%");
			for (Office e : list){
				e.setParentIds(e.getParentIds().replace(oldParentIds, office.getParentIds()));
			}
			officeDao.save(list);
		}	
		return true;
	}
	
	/**
	 * 同步菜单数据
	 * @param jsonData
	 */
	@Transactional(readOnly = false)
	public boolean menuSyn(String jsonData){		
		JsonMapper jsonMapper = JsonMapper.getInstance();
		MenuDto menuDto = jsonMapper.fromJson(jsonData, MenuDto.class);
		Menu menu = menuDao.getByKuid(menuDto.getKuid());
		Menu parent = menuDao.getByKuid(menuDto.getParentId());
		//父节点
		String oldParentIds = null;
		if (menu == null) {
			menu = new Menu();				
		}else{
			oldParentIds = menu.getParentIds();
		}
		if (parent == null) {
			return false;
		}
		BeanUtils.copyProperties(menuDto, menu);
		menu.setParent(parent);
		menu.setParentIds(parent.getParentIds()+parent.getId()+",");
		menuDao.save(menu);		
		if (oldParentIds != null) {
			// 更新子节点 parentIds
			List<Menu> list = menuDao.findByParentIdsLike("%,"+menu.getId()+",%");
			for (Menu e : list){
				e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			}
			menuDao.save(list);
		}	
		return true;
	}

	/**
	 * 同步角色数据
	 * @param jsonData
	 */
	@Transactional(readOnly = false)
	public boolean roleSyn(String jsonData) {
		JsonMapper jsonMapper = JsonMapper.getInstance();
		RoleDto roleDto = jsonMapper.fromJson(jsonData, RoleDto.class);
		Role role = roleDao.getByKuid(roleDto.getKuid());
		Office office = officeDao.getByKuid(roleDto.getOfficeId());
		if (role == null) {
			role = new Role();				
		}
		BeanUtils.copyProperties(roleDto, role);
		role.setOffice(office);
		//角色对应的菜单
		List<String> menuIds = roleDto.getMenuIds();
		if (menuIds!=null && menuIds.size()>0) {
			List<Menu> menus = new ArrayList<Menu>();
			for (String kuid : menuIds) {
				Menu menu = menuDao.getByKuid(kuid);
				if (menu == null) {
					return false;
				}
				menus.add(menu);
			}
			role.setMenuList(menus);
		}else{
			role.setMenuList(null);
		}	
		roleDao.save(role);
		return true;
	}
	
	/**
	 * 同步用户数据
	 * @param jsonData
	 */
	@Transactional(readOnly = false)
	public boolean userSyn(String jsonData) {
		JsonMapper jsonMapper = JsonMapper.getInstance();
		UserDto userDto = jsonMapper.fromJson(jsonData, UserDto.class);
		User user = userDao.findByLoginName(userDto.getLoginName());
		Office office = officeDao.getByKuid(userDto.getOfficeId());
		String flag = "0";
		if (user == null) {
			user = new User();				
		}else{
			flag = "1";
		}
		if (office == null) {
			return false;
		}
		BeanUtils.copyProperties(userDto, user);
		if (StringUtils.isNotBlank(userDto.getUserImg())) {
			user.setUserImg(userDto.getUserImg());
		}
		user.setCompany(office);
		user.setOffice(office);
		List<String> roleIds = userDto.getRoleIds();
		if (roleIds!=null && roleIds.size()>0) {
			List<Role> roles = new ArrayList<Role>();
			for (String kuid : roleIds) {
				Role roleTemp = roleDao.getByKuid(kuid);
				if (roleTemp == null) {
					return false;
				}
				roles.add(roleTemp);
			}
			user.setRoleList(roles);
		}else{
			user.setRoleList(null);
		}
		if(flag.equals("0")){   //新增
			String privateMapKey = WebRTCSigApi.getPrivateMapKey_book(user.getLoginName());
			user.setUserToken(privateMapKey);
		}
		userDao.save(user);
		
		Experts ep = expertsService.getExpertsByUserCode(user.getLoginName());
		if(ep!=null){
			System.out.println("111111111111");
			ep.setUserId(user.getId());
			expertsService.save(ep);
		}
		
		String imageUrl = "";
		String url = Global.getConfig("wcspadmin_img_url");
		if(StringUtils.isNotBlank(user.getUserImg())){
			if(url.indexOf("http")>-1){
				imageUrl = user.getUserImg();
			}else{
				imageUrl = url + user.getUserImg();
			}
		}
		
		//上传云通信账号
		if(flag.equals("0")){   //新增
			JSONObject json = expertsService.addIMAccout(user.getLoginName(), user.getName(), imageUrl);
			if(json.getString("ActionStatus").equals("OK")){
				System.out.println("新增成功");
			}else{
				System.out.println("新增失败");
			}
		}else{    //修改
			JSONObject json = expertsService.editIMAccout(user.getLoginName(), user.getName(), imageUrl);
			if(json.getString("ActionStatus").equals("OK")){
				System.out.println("修改成功");
			}else{
				System.out.println("修改失败");
			}
		}
		
		return true;
	}
}
