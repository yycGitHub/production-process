package com.surekam.modules.api.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.DateUtils;
import com.surekam.common.utils.HttpClientUtil;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.experts.entity.ExpertServiceInfo;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.entity.ExpertsGoodproblem;
import com.surekam.modules.agro.experts.service.ExpertsGoodproblemService;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.experts.service.ExpertsServiceInfoService;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.agro.video.dao.VideoDao;
import com.surekam.modules.agro.video.entity.Video;
import com.surekam.modules.agro.video.service.VideoService;
import com.surekam.modules.api.entity.TreeNode;
import com.surekam.modules.api.service.ApiService;
import com.surekam.modules.api.service.ApiUserService;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.entity.CommonUser;
import com.surekam.modules.sys.entity.CopyOfCommonUser;
import com.surekam.modules.sys.entity.Dict;
import com.surekam.modules.sys.entity.Menu;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.OfficeService;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.service.UserService;
import com.surekam.modules.sys.utils.DictUtils;
import com.surekam.modules.sys.utils.HttpXmlClient;
import com.surekam.modules.sys.utils.base64ToImage;
import com.surekam.modules.tracewxtoken.entity.TraceWxToken;
import com.surekam.modules.tracewxtoken.service.TraceWxTokenService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;

@Api
@Controller
@RequestMapping(value = "api/common")
public class CommonApiController extends BaseController {

	@Autowired
	private ApiService apiService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private SystemService systemService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private ApiUserService apiUserService;

	@Autowired
	private TraceWxTokenService traceWxTokenService;
	
	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;
	
	@Autowired
	private ExpertsGoodproblemService expertsGoodproblemService;

	@Autowired
	private ExpertsService expertsService;
	@Autowired
	private UserService userService;
	@Autowired
	private VideoDao videoDao;
	@Autowired
	private ExpertsServiceInfoService expertsServiceInfoService;

	/**
	 * 根据token获取用户信息 add by liw 2018-08-21 测试地址�?
	 * http://localhost:8080/sureserve-admin/api/common/getUserByToken token
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserByToken", method = RequestMethod.GET)
	@ApiOperation(value = "根据token获取用户信息", httpMethod = "GET", notes = "根据token获取用户信息", consumes = "application/x-www-form-urlencoded")
	public String getUserByToken(@RequestParam String token) {
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<CopyOfCommonUser> resultBean = new ResultBean<CopyOfCommonUser>();
		try {
			if (StringUtils.isNotBlank(token)) {
				CopyOfCommonUser user = userDao.findByTokenCopy(token);
				resultBean = ResultUtil.success(user);
			} else {
				resultBean = ResultUtil.error(
						ResultEnum.TOKEN_IS_NULL.getCode(),
						ResultEnum.TOKEN_IS_NULL.getMessage());
			}
			return jsonMapper.toJson(resultBean);
		} catch (Exception e) {
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),
					ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	/**
	 * 专家列表(展示平台)
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "/expertsList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "专家列表", httpMethod = "GET", notes = "专家列表", consumes = "application/x-www-form-urlencoded")
	public String expertsList(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer pageno,
			@RequestParam Integer pagesize, @RequestParam(required = false) String productLibraryId, @RequestParam String code) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<Experts> page = new Page<Experts>(pageNo, pageSize);
		page = expertsService.find(page, productLibraryId,code);
		page.setCount(page.getList().size());
		List<Experts> list = page.getList();
		if (page.getList().size() > 0) {
			for (Iterator<Experts> iterator = list.iterator(); iterator.hasNext();) {
				Experts experts = (Experts) iterator.next();
				//User user = userService.get(experts.getUserId());
				Video video = videoDao.getVideoByUserCode(experts.getUserCode());
				//experts.setUserCode(user.getLoginName());
				if(video !=null) {
					experts.setCreateRoomNumber(video.getCreateRoomNumber());
					if(StringUtils.isNotBlank(video.getEntryRoomNumber())){
						experts.setIsInRoom("1");
					}else{
						experts.setIsInRoom("0");
					}
				}
				List<ProductLibraryTree> plist = productLibraryTreeService.getList(experts.getId());
				List<ExpertsGoodproblem> glist = expertsGoodproblemService.getList(experts.getId());
				String pstr = "";
				String gstr = "";
				if(plist.size() > 0) {
					for(int i = 0; i < plist.size(); i++) {
						pstr += plist.get(i).getProductCategoryName() + ",";
					}
				}
				if(glist.size() > 0) {
					for(int i = 0; i < glist.size(); i++) {
						gstr += glist.get(i).getGoodProblem() + ",";
					}
				}
				if(StringUtils.isNotBlank(pstr)){
					experts.setProductData(pstr.substring(0,pstr.length() - 1));
				}
				if(StringUtils.isNotBlank(gstr)){
					experts.setGoodproblemData(gstr.substring(0,gstr.length() - 1));
				}
				
				experts.setPhoto(experts.getUserImg());
			}
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
	}
	

	/**
	 *专家服务列表
	 * @author 
	 * @param request
	 * @param response
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getExpertServiceInfoList",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "专家服务列表", httpMethod = "GET", notes = "专家服务列表",	consumes="application/x-www-form-urlencoded")
	public String getExpertServiceInfoList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Integer pageno,@RequestParam Integer pagesize, @RequestParam String code) {
		response.setContentType("application/json; charset=UTF-8");
		int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
		int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
		Page<ExpertServiceInfo> page = new Page<ExpertServiceInfo>(pageNo, pageSize);
		page = expertsServiceInfoService.getExpertServiceInfoList1(page, code);
		List<ExpertServiceInfo> list = page.getList();
		if (page.getList().size() > 0) {
			for (Iterator<ExpertServiceInfo> iterator = list.iterator(); iterator.hasNext();) {
				ExpertServiceInfo expertServiceInfo = (ExpertServiceInfo) iterator.next();
				expertServiceInfo.setServicePeopleName(userService.get(expertServiceInfo.getExpertId()).getName());
				//expertServiceInfo.setServicePeopleName(userService.get(expertServiceInfo.getExpertId()).getName());
				//expertServiceInfo.setCompanyName(officeService.get(expertServiceInfo.getServiceCompanyId()).getName());//公司名称
				//expertServiceInfo.setFileList(fileInfoService.find(expertServiceInfo.getId()));
			}
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(page));
	}

	/**
	 * 获取菜单 add by liw 2018-08-28 测试地址�?
	 * http://localhost:8080/sureserve-admin/api/common/getMenuData
	 * 
	 * @return
	 */
	@RequestMapping(value = "getMenuData", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取菜单", httpMethod = "GET", notes = "获取菜单", consumes = "application/x-www-form-urlencoded")
	public String getMenuData(HttpServletRequest request) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(),
							ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		CommonUser user = userDao.findByToken(token);
		List<TreeNode> list = treeMenuList(user.getMenuList(),
				"74bf19e8aa8246469af10a95b95d130f");
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
	}

	public List<TreeNode> treeMenuList(List<Menu> menuList, String pid) {
		List<TreeNode> list = new ArrayList<TreeNode>();
		for (Menu menu : menuList) {
			// 遍历出父id等于参数的id，add进子节点集合
			String id = menu.getParent() == null ? "" : menu.getParent()
					.getId();
			if (id.equals(pid)) {
				TreeNode treeNode = new TreeNode();
				treeNode.setId(menu.getId());
				treeNode.setPid(pid);
				treeNode.setName(menu.getName());
				List<TreeNode> childNode = treeMenuList(menuList, menu.getId());
				if (childNode.size() > 0) {
					treeNode.setChildNode(treeMenuList(menuList, menu.getId()));
				}
				// 递归遍历下一�?
				list.add(treeNode);
			}
		}
		return list;
	}

	/**
	 * 根据数据字典类型获取数据字典列表 add by ligm 2018-09-19 测试地址�?
	 * http://localhost:8080/sureserve-admin/api/common/getDictList
	 * 
	 * @return
	 */
	@RequestMapping(value = "getDictList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取数据字典列表", httpMethod = "GET", notes = "根据数据字典类型获取数据字典列表", consumes = "application/x-www-form-urlencoded")
	public String getDictList(HttpServletRequest request,
			@RequestParam(required = true) String type) {
		List<Dict> list = DictUtils.getDictList(type);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
	}

	/**
	 * 根据数据字典类型及�?获取标签名�? add by ligm 2018-09-19
	 * 
	 * @return
	 */
	@RequestMapping(value = "getDictLabel", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取数据字典标签�?", httpMethod = "GET", notes = "根据数据字典类型及�?获取标签名�?", consumes = "application/x-www-form-urlencoded")
	public String getDictLabel(HttpServletRequest request,
			@RequestParam(required = true) String value,
			@RequestParam(required = true) String type, String defaultValue) {
		String labelString = DictUtils.getDictLabel(value, type, defaultValue);
		return JsonMapper.nonDefaultMapper().toJson(
				ResultUtil.success(labelString));
	}

	/**
	 * 根据数据字典类型及标签名获取对应的�? add by ligm 2018-09-19
	 * 
	 * @return
	 */
	@RequestMapping(value = "getDictValue", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取数据字典标签对应�?", httpMethod = "GET", notes = "根据数据字典类型及标签名获取对应的�?", consumes = "application/x-www-form-urlencoded")
	public String getDictValue(HttpServletRequest request,
			@RequestParam(required = true) String label,
			@RequestParam(required = true) String type, String defaultLabel) {
		String labelString = DictUtils.getDictValue(label, type, defaultLabel);
		return JsonMapper.nonDefaultMapper().toJson(
				ResultUtil.success(labelString));
	}

	/**
	 * 获取用户企业列表 add by xiaowangzi 2018-09-29
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getCompanyData", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取企业列表", httpMethod = "GET", notes = "获取企业列表", consumes = "application/x-www-form-urlencoded")
	public String getCompanyData(HttpServletRequest request) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(),
							ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		CommonUser commonUser = systemService.findByToken(token);
		User user = systemService.getUserByLoginName(commonUser.getLoginName());
		List<Office> list = officeService.findOfficesByUser(user);
		List<Map<String, Object>> mapList = Lists.newArrayList();
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("name", e.getName());
			map.put("username", user.getName());
			map.put("image", user.getUserImg());// e.getOfficeLogo()
			mapList.add(map);
		}
		// 返回没有信息提示
		if (0 == mapList.size()) {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(),
							ResultEnum.DATA_NOT_EXIST.getMessage()));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.success(mapList));
		}
	}

	/**
	 * 上传base64图片
	 * 
	 * @author wangyuewen
	 * @param request
	 * @param imgStr
	 * @param extName
	 * @return
	 */
	@RequestMapping(value = "fileUpload", produces = "text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "base64图片上传", httpMethod = "POST", notes = "base64图片上传", consumes = "application/json")
	public String fileUpload(HttpServletRequest request,
			@RequestBody @ApiParam JSONObject paramJson) {
		String imgStr = "";
		String extName = "";
		if (paramJson.containsKey("imgStr")) {
			imgStr = paramJson.getString("imgStr");
		}
		if (paramJson.containsKey("extName")) {
			extName = paramJson.getString("extName");
		}
		String save_path = request.getSession().getServletContext()
				.getRealPath("");
		String relative_path = "/upload/farming/argo/"
				+ DateUtils.getDate("yyyy-MM") + "/";
		String path = save_path.substring(0,
				save_path.lastIndexOf(File.separator))
				+ relative_path;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();// 如不存在路径则创建路�?
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateStr = sdf.format(new Date());
		path = path + dateStr + "." + extName;
		relative_path = relative_path + dateStr + "." + extName;
		Boolean flag = base64ToImage.generateImage(imgStr, path);
		if (!flag) {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.OPERATION_FAILED.getCode(),
							ResultEnum.OPERATION_FAILED.getMessage()));
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("relative_path", relative_path);
		map.put("absolute_url", Global.getConfig("sy_img_url") + relative_path);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
	}

	/**
	 * 文件上传 add by liw 2018-08-21 测试地址�?
	 * http://localhost:8080/sureserve-admin/api/common/upload file 文件
	 * 
	 * @return
	 */
	@RequestMapping(value = "upload", produces = "text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "文件上传", httpMethod = "POST", notes = "文件上传", consumes = "application/x-www-form-urlencoded")
	public String upload(HttpServletRequest request,
			@RequestParam MultipartFile file) {
		String path = "";
		if (file != null && !file.getOriginalFilename().isEmpty()) {
			// 绝对路径
			String save_path = request.getSession().getServletContext()
					.getRealPath("");
			// 服务器IP 端口拼接 http://127.0.0.1:8080/
			path = save_path
					.substring(0, save_path.lastIndexOf(File.separator))
					+ "/upload/farming/images/";
			File fiel = new File(path);
			if (!fiel.exists()) {
				fiel.mkdirs();// 如不存在路径则创建路�?
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultBean<List<Map<String, Object>>> resultBean = new ResultBean<List<Map<String, Object>>>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String picUrl = apiService.saveAttach(file, path);
			if (StringUtils.isNotBlank(picUrl)) {
				map.put("relative_url", picUrl);
				String imgUrl = Global.getConfig("sy_img_url");
				map.put("absolute_url", imgUrl + picUrl);
			} else {
				map.put("relative_url", "");
				map.put("absolute_url", "");
			}
			list.add(map);
			resultBean = ResultUtil.success(list);
		} catch (Exception e) {
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),
					ResultEnum.SYSTEM_ERROR.getMessage());
		}
		return JsonMapper.nonDefaultMapper().toJson(resultBean);
	}
	
	/**
	 * 文件上传 add by liw 2018-08-21 测试地址�?
	 * http://localhost:8080/sureserve-ns/api/common/uploadAudio file 文件
	 * 
	 * @return
	 */
	@RequestMapping(value = "uploadAudio", produces = "text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "录音上传", httpMethod = "POST", notes = "录音上传", consumes = "application/x-www-form-urlencoded")
	public String uploadAudio(HttpServletRequest request, @RequestParam String len, @RequestParam String loginName) {
		String path = "";
		try {
			int l = Integer.parseInt(len);
			// 绝对路径
			String save_path = request.getSession().getServletContext().getRealPath("");
			// 服务器IP 端口拼接 http://127.0.0.1:8080/
			path = save_path.substring(0, save_path.lastIndexOf(File.separator))+ "/upload/audio/"+loginName+"/";
			File fiel = new File(path);
			if (!fiel.exists()) {
				fiel.mkdirs();// 如不存在路径则创建路�?
			}
			List<String> list = new ArrayList<String>();
			for(int i=0;i<l;i++){
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultipartFile file = multipartRequest.getFile("warArr"+i);
				String fileName = request.getParameter("filename"+i);
				String audioUrl = apiService.saveAudio(file, path,fileName,loginName);
				list.add(audioUrl);
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage()));
		}
	}
	
	
	/**
	 * 文件上传 add by  2019-07-08 测试地址�?
	 * http://localhost:8080/sureserve-admin/api/common/upload file 文件
	 * 
	 * @return
	 */
	@RequestMapping(value = "uploadFile", produces = "text/plain;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "文件上传", httpMethod = "POST", notes = "文件上传", consumes = "application/x-www-form-urlencoded")
	public String uploadFile(HttpServletRequest request,@RequestParam MultipartFile file) {
		String path = "";
		if (file != null && !file.getOriginalFilename().isEmpty()) {
			// 绝对路径
			String save_path = request.getSession().getServletContext().getRealPath("");
			// 服务器IP 端口拼接 http://127.0.0.1:8080/
			path = save_path.substring(0, save_path.lastIndexOf(File.separator))+ "/upload/farming/argo/appendix/";
			File fiel = new File(path);
			if (!fiel.exists()) {
				fiel.mkdirs();// 如不存在路径则创建路�?
			}
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultBean<List<Map<String, Object>>> resultBean = new ResultBean<List<Map<String, Object>>>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String picUrl = apiService.saveEnclosure(file, path);
			if (StringUtils.isNotBlank(picUrl)) {
				map.put("relative_url", picUrl);
				String imgUrl = Global.getConfig("sy_img_url");
				map.put("absolute_url", imgUrl + picUrl);
			} else {
				map.put("relative_url", "");
				map.put("absolute_url", "");
			}
			list.add(map);
			resultBean = ResultUtil.success(list);
		} catch (Exception e) {
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
		}
		return JsonMapper.nonDefaultMapper().toJson(resultBean);
	}

	/*************************************************************** 打印 **********************************************************************/
	/**
	 * @description 跳转到打印页面；
	 * @return
	 */
	@RequestMapping(value = "getPrintPage")
	public String getPrintPage(String applyId, String token,
			String serialNumberStart, String serialNumberEnd, Model model,
			HttpServletRequest request) {
		model.addAttribute("applyId", applyId);
		model.addAttribute("token", token);
		model.addAttribute("serialNumberStart", serialNumberStart);
		model.addAttribute("serialNumberEnd", serialNumberEnd);
		model.addAttribute("type", "1");
		return "modules/" + "common/temUploadForm";
	}

	/**
	 * @description 获取微信配置,用于获取前端定位信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getLocation")
	@ApiOperation(value = "获取微信配置,用于获取前端定位�?", httpMethod = "GET", notes = "获取微信配置,用于获取前端定位�?", consumes = "application/x-www-form-urlencoded")
	public String getLocation(String url, String code, String state,
			HttpServletRequest request) {
		if (StringUtils.isNotBlank(code)) {
			if (code.contains("code=")) {
				url += "&" + code;
			} else {
				url += "&code=" + code;
			}
		}
		if (StringUtils.isNotBlank(state)) {
			if (state.contains("state=")) {
				url += "&" + state;
			} else {
				url += "&state=" + state;
			}
		}
		System.out.println("url============================================="
				+ url);
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		String appID = Global.getConfig("appID");
		String appSecret = Global.getConfig("appSecret");
		System.out.println(appID + "                           " + appSecret);
		HttpClientUtil httpClientUtil = HttpClientUtil.getInstance();
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", appID);
		params.put("secret", appSecret);
		params.put("grant_type", "client_credential");
		String noncestr = UUID.randomUUID().toString().replaceAll("-", "")
				.substring(0, 32);
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String access_token = "";
		String jsapi_ticket = "";
		String signature = "";
		TraceWxToken traceWxToken = traceWxTokenService.findTraceWxToken("");
		if (StringUtils.isNotBlank(traceWxToken.getAccessToken())) {
			Date createTime = traceWxToken.getCreateTime();
			if (differentDaysByMillisecond(createTime, new Date()) < 1.6) {
				// String accessToken = traceWxToken.getAccessToken();
				jsapi_ticket = traceWxToken.getJsapiTicket();
				noncestr = traceWxToken.getNoncestr();
				timestamp = traceWxToken.getTimestamp();

				String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr="
						+ noncestr + "&timestamp=" + timestamp + "&url=" + url;
				System.out.println("signature========================" + str);
				// sha1加密
				signature = HttpXmlClient.SHA1(str);
			} else {
				HttpPost httpPost = HttpXmlClient.postForm(
						"https://api.weixin.qq.com/cgi-bin/token", params);
				String at = httpClientUtil.sendHttpPost(httpPost);
				JSONObject at_obj = JSONObject.fromObject(at);
				System.out.println("access_token ====== " + at_obj);
				if (StringUtils.isNotBlank(at_obj.getString("access_token"))) {
					access_token = at_obj.getString("access_token");
					System.out.println("access_token ====== " + access_token);
					// 获取ticket
					params = new HashMap<String, String>();
					params.put("access_token", access_token);
					params.put("type", "jsapi");
					httpPost = HttpXmlClient
							.postForm(
									"https://api.weixin.qq.com/cgi-bin/ticket/getticket",
									params);
					String jt = httpClientUtil.sendHttpPost(httpPost);
					System.out.println(jt);
					JSONObject jt_obj = JSONObject.fromObject(jt);
					if (StringUtils.isNotBlank(jt_obj.getString("ticket"))) {
						jsapi_ticket = jt_obj.getString("ticket");
						System.out.println("jsapi_ticket ====== "
								+ jsapi_ticket);
						// 获取签名signature
						String str = "jsapi_ticket=" + jsapi_ticket
								+ "&noncestr=" + noncestr + "&timestamp="
								+ timestamp + "&url=" + url;
						System.out.println("signature========================"
								+ str);
						// sha1加密
						signature = HttpXmlClient.SHA1(str);

						traceWxToken.setAccessToken(access_token);
						traceWxToken.setCreateTime(new Date());
						traceWxToken.setJsapiTicket(jsapi_ticket);
						traceWxToken.setTimestamp(timestamp);
						traceWxToken.setNoncestr(noncestr);
						traceWxToken.setSignature(signature);
						traceWxTokenService.save(traceWxToken);
					} else {
						resultBean = ResultUtil.error(
								ResultEnum.WX_GET_NULL.getCode(),
								ResultEnum.WX_GET_NULL.getMessage());
						return jsonMapper.toJson(resultBean);
					}
				} else {
					resultBean = ResultUtil.error(
							ResultEnum.WX_GET_NULL.getCode(),
							ResultEnum.WX_GET_NULL.getMessage());
					return jsonMapper.toJson(resultBean);
				}
			}
		} else {
			HttpPost httpPost = HttpXmlClient.postForm(
					"https://api.weixin.qq.com/cgi-bin/token", params);
			String at = httpClientUtil.sendHttpPost(httpPost);
			System.out.println(at);
			JSONObject at_obj = JSONObject.fromObject(at);
			if (StringUtils.isNotBlank(at_obj.getString("access_token"))) {
				access_token = at_obj.getString("access_token");
				System.out.println("access_token ====== " + access_token);
				// 获取ticket
				params = new HashMap<String, String>();
				params.put("access_token", access_token);
				params.put("type", "jsapi");
				httpPost = HttpXmlClient.postForm(
						"https://api.weixin.qq.com/cgi-bin/ticket/getticket",
						params);
				String jt = httpClientUtil.sendHttpPost(httpPost);
				System.out.println(jt);
				JSONObject jt_obj = JSONObject.fromObject(jt);
				if (StringUtils.isNotBlank(jt_obj.getString("ticket"))) {
					jsapi_ticket = jt_obj.getString("ticket");
					System.out.println("jsapi_ticket ====== " + jsapi_ticket);
					// 获取签名signature
					String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr="
							+ noncestr + "&timestamp=" + timestamp + "&url="
							+ url;
					System.out.println("url========================" + str);
					// sha1加密
					signature = HttpXmlClient.SHA1(str);

					TraceWxToken wxToken = new TraceWxToken();
					wxToken.setAccessToken(access_token);
					wxToken.setCreateTime(new Date());
					wxToken.setJsapiTicket(jsapi_ticket);
					wxToken.setTimestamp(timestamp);
					wxToken.setNoncestr(noncestr);
					wxToken.setSignature(signature);
					traceWxTokenService.save(wxToken);
				} else {
					resultBean = ResultUtil.error(
							ResultEnum.WX_GET_NULL.getCode(),
							ResultEnum.WX_GET_NULL.getMessage());
					return jsonMapper.toJson(resultBean);
				}
			} else {
				resultBean = ResultUtil.error(ResultEnum.WX_GET_NULL.getCode(),
						ResultEnum.WX_GET_NULL.getMessage());
				return jsonMapper.toJson(resultBean);
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("appID", appID);
		map.put("noncestr", noncestr);
		map.put("timestamp", timestamp);
		map.put("signature", signature);
		System.out.println("signature ====== " + signature);
		String message = "上传成功";
		resultBean.setCode(0);
		resultBean.setMessage(message);
		resultBean.setBodyData(map);
		return jsonMapper.toJson(resultBean);
	}

	public float differentDaysByMillisecond(Date date1, Date date2) {
		float days = (float) ((date2.getTime() - date1.getTime()) / (1000 * 3600.0));
		return days;
	}
	
	@RequestMapping(value = "getQhData", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "获取小气候数据", httpMethod = "POST", notes = "获取小气候数据", consumes = "application/x-www-form-urlencoded")
	public String getQhData(HttpServletRequest request) {
		String token = request.getHeader("X-Token");
		if (StringUtils.isBlank(token)) {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(),
							ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		CommonUser commonUser = systemService.findByToken(token);
		User user = systemService.getUserByLoginName(commonUser.getLoginName());
		List<Office> list = officeService.findOfficesByUser(user);
		List<Map<String, Object>> mapList = Lists.newArrayList();
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("name", e.getName());
			map.put("username", user.getName());
			map.put("image", user.getUserImg());// e.getOfficeLogo()
			mapList.add(map);
		}
		// 返回没有信息提示
		if (0 == mapList.size()) {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.error(ResultEnum.DATA_NOT_EXIST.getCode(),
							ResultEnum.DATA_NOT_EXIST.getMessage()));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(
					ResultUtil.success(mapList));
		}
	}
	
}
