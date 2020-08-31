package com.surekam.modules.api.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.Client;
import com.surekam.common.utils.RestAPI;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.addressbook.entity.ParameterClass;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.agro.video.dao.VideoDao;
import com.surekam.modules.agro.video.entity.Video;
import com.surekam.modules.agro.video.service.VideoService;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.ApiSystemService;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 通讯录（接口）
 * @author liw
 */
@Api
@Controller
@RequestMapping(value = "api/address_book")
public class AddressBookController {
	@Autowired
	private SystemService systemService;

	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;
	
	@Autowired
	private ExpertsService expertsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private VideoDao videoDao;
	
	@Autowired
	private ApiSystemService apiSystemService;
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * 专家列表
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/expertsList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "专家列表", httpMethod = "GET", notes = "专家列表", consumes = "application/x-www-form-urlencoded")
	public String expertsList(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer pageno,
			@RequestParam Integer pagesize, @RequestParam(required = false) String name) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<User> page = new Page<User>(pageNo, pageSize);
		String token = request.getHeader("X-Token");
		if (token != null) {
			User currentUser = apiSystemService.findUserByToken(token);
			List<User> rlist = new ArrayList<User>();
			page = expertsService.findExpertsList(page, name,currentUser);
			List<String> userArr = new ArrayList<String>();
			for (Iterator<User> iterator = page.getList().iterator(); iterator.hasNext();) {
				User user = (User) iterator.next();
				Experts experts = expertsService.getExpertByUserId(user.getId());
				if(StringUtils.isNotBlank(experts.getExpertTitle())){
					user.setExpertTitle(experts.getExpertTitle());
				}else{
					Video video = videoDao.getVideoByUserCode(user.getLoginName());
					user.setExpertTitle(video.getType());
				}
				userArr.add(user.getLoginName());
				rlist.add(user);
			}
			
			List<User> slist = new ArrayList<User>();
			if(rlist.size()>0){
				String url = RestAPI.restUrl("sns", "friend_check");
				JSONObject json = new JSONObject();
				json.put("From_Account", currentUser.getLoginName());		
				json.put("To_Account", userArr.toArray());			
				json.put("CheckType", "CheckResult_Type_Single");		
				String post = Client.post(url, json, "UTF-8");
				JSONObject at_obj = JSONObject.fromObject(post);
				JSONArray arr = at_obj.getJSONArray("InfoItem");
				for (int i=0;i<arr.size();i++) {
					JSONObject obj = arr.getJSONObject(i);
					String To_Account = obj.getString("To_Account");
					String relation = obj.getString("Relation");
					if(relation!=null && relation.equals("CheckResult_Type_NoRelation")){
						for(int j=0;j<rlist.size();j++){
							User u = rlist.get(j);
							if(u.getLoginName().equals(To_Account)){
								slist.add(u);
							}
						}
					}
				}
			}
			page.setList(slist);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 根据用户code获取专家个人信息
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getExpertsByUserCode", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "根据用户code获取专家个人信息", httpMethod = "GET", notes = "根据用户code获取专家个人信息", consumes = "application/x-www-form-urlencoded")
	public String getExpertsByUserCode(HttpServletRequest request, HttpServletResponse response, String userCode) {
		response.setContentType("application/json; charset=UTF-8");
		User user = systemService.getUserByLoginName(userCode);
		if (user!=null) {
			Experts experts = expertsService.getExpertByUserId(user.getId());
			if(StringUtils.isNotBlank(experts.getExpertTitle())){
				user.setExpertTitle(experts.getExpertTitle());
			}else{
				Video video = videoDao.getVideoByUserCode(user.getLoginName());
				user.setExpertTitle(video.getType());
			}
			user.setExpertDescription(experts.getExpertDescription());
			user.setExpertSex(experts.getExpertSex());
			user.setExpertBirthDate(experts.getExpertBirthDate());
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(user));
	}
	
	/**
	 * 种类列表
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/productList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "种类列表", httpMethod = "GET", notes = "种类列表", consumes = "application/x-www-form-urlencoded")
	public String productList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			User currentUser = apiSystemService.findUserByToken(token);
			List<Map<String,Object>> rlist = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> list = productLibraryTreeService.findProductLibraryList(currentUser);
			for (Map<String, Object> map : list) {
				String id = map.get("product_library_id").toString();
				map.put("count", expertsService.getExpertsCount(id));
				rlist.add(map);
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(rlist));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}
	
	/**
	 * 种类对应的人员列表
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/peopleList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "种类列表", httpMethod = "GET", notes = "种类列表", consumes = "application/x-www-form-urlencoded")
	public String peopleList(HttpServletRequest request, HttpServletResponse response, String productId) {
		response.setContentType("application/json; charset=UTF-8");
		List<Experts> rlist = new ArrayList<Experts>();
		List<Experts> list = expertsService.findPeopleListList(productId);
		for (Experts experts : list) {
			User user = userService.get(experts.getUserId());
			experts.setPhoto(user.getUserImg());
			experts.setUserToken(user.getUserToken());
			experts.setUserCode(user.getLoginName());
			rlist.add(experts);
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(rlist));
	}
	
	/**
	 * 腾讯云通信操作接口
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/restAPIInterface", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "腾讯云通信操作接口", httpMethod = "POST", notes = "腾讯云通信操作接口", consumes = "application/x-www-form-urlencoded")
	public JSONObject restAPIInterface(ParameterClass parameterClass) {
		String url = RestAPI.restUrl(parameterClass.getServicename(), parameterClass.getCommand());
		JSONObject json = RestAPI.objToJson(parameterClass);
		String post = Client.post(url, json, "UTF-8");
		JSONObject at_obj = JSONObject.fromObject(post);
		System.out.println("返回数据"+at_obj);
		return at_obj;
	}
	
	/**
	 * 新增云通信账号
	 * @author
	 * @param userCode, userName, imageUrl
	 * @return
	 */
	@RequestMapping(value = "/addIMAccount", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "新增云通信账号", httpMethod = "POST", notes = "新增云通信账号", consumes = "application/x-www-form-urlencoded")
	public JSONObject addIMAccount(String userCode,String userName,String imageUrl) {
		return expertsService.addIMAccout(userCode, userName, imageUrl);
	}
	
	/**
	 * 修改云通信账号
	 * @author
	 * @param userCode, userName, imageUrl
	 * @return
	 */
	@RequestMapping(value = "/editIMAccount", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "修改云通信账号", httpMethod = "POST", notes = "修改云通信账号", consumes = "application/x-www-form-urlencoded")
	public JSONObject editIMAccount(String userCode,String userName,String imageUrl) {
		return expertsService.editIMAccout(userCode, userName, imageUrl);
	}
	
	/**
	 * 批量提交用户账号
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/submitAttachAccount", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "批量提交用户账号", httpMethod = "POST", notes = "批量提交用户账号", consumes = "application/x-www-form-urlencoded")
	public JSONObject submitAttachAccount(ParameterClass parameterClass) {
		String url = RestAPI.restUrl(parameterClass.getServicename(), parameterClass.getCommand());
		List<String> account = new ArrayList<String>();
		//List<User> list = userDao.findUserList();
		//for (User user : list) {
		//	account.add(user.getLoginName());
		//}
		account.add("huayuannh");
		account.add("ysstnh");
		JSONObject json = new JSONObject();
		json.put("Accounts", account.toArray());		
		
		String post = Client.post(url, json, "UTF-8");
		JSONObject at_obj = JSONObject.fromObject(post);
		System.out.println("批量提交用户账号返回数据"+at_obj);
		return at_obj;
	}
	
	/**
	 * 设置所有的用户的资料
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/setAttachAccount", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "设置所有的用户的资料", httpMethod = "POST", notes = "设置所有的用户的资料", consumes = "application/x-www-form-urlencoded")
	public void setAttachAccount(ParameterClass parameterClass) {
//		List<User> list = userDao.findUserList();
//		for (User user : list) {
//			String imageUrl = "";
//			String url = Global.getConfig("sy_img_url");
//			if(StringUtils.isNotBlank(user.getUserImg())){
//				if(user.getUserImg().indexOf("http")==-1){
//					imageUrl = url + user.getUserImg();
//				}else{
//					imageUrl = user.getUserImg();
//				}
//			}
//			expertsService.editIMAccout(user.getLoginName(), user.getName(), imageUrl);
//		}
		expertsService.editIMAccout("huayuannh", "花园", "/upload/farming/product_model/20190424112418479.png");
		expertsService.editIMAccout("ysstnh", "耘升", "/upload/farming/product_model/20190424112418479.png");
	}
	
	/**
	 * 删除用户
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteAttachAccount", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "设置所有的用户的资料", httpMethod = "POST", notes = "设置所有的用户的资料", consumes = "application/x-www-form-urlencoded")
	public void deleteAttachAccount(ParameterClass parameterClass) {
		String url = RestAPI.restUrl(parameterClass.getServicename(), parameterClass.getCommand());
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("UserID","test1");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("UserID","test2");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("UserID","test3");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("UserID","test4");
		list.add(map);
		
		JSONObject json = new JSONObject();
		json.put("DeleteItem", list.toArray());	
		String post = Client.post(url, json, "UTF-8");
		JSONObject at_obj = JSONObject.fromObject(post);
		System.out.println("批量删除用户账号返回数据"+at_obj);
	}
	
}
