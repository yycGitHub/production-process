package com.surekam.modules.api.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.utils.MD5Utils;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.utils.WebRTCSigApi;
import com.surekam.common.web.BaseController;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.entity.ExpertsGoodproblem;
import com.surekam.modules.agro.experts.service.ExpertsGoodproblemService;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.agro.video.dao.VideoDao;
import com.surekam.modules.agro.video.entity.Video;
import com.surekam.modules.agro.video.service.VideoService;
import com.surekam.modules.agro.videomain.dao.VideoMainDao;
import com.surekam.modules.agro.videomain.entity.VideoMain;
import com.surekam.modules.agro.videomain.service.VideoMainService;
import com.surekam.modules.agro.videopeopleinvolvement.dao.VideoPeopleInvolvementDao;
import com.surekam.modules.agro.videopeopleinvolvement.entity.VideoPeopleInvolvement;
import com.surekam.modules.agro.videopeopleinvolvement.service.VideoPeopleInvolvementService;
import com.surekam.modules.agro.webrtcsig.entity.WebRTCSig;
import com.surekam.modules.agro.webrtcsig.service.WebRTCSigService;
import com.surekam.modules.api.utils.tet;
import com.surekam.modules.sys.dao.UserDao;
import com.surekam.modules.sys.entity.Dict;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.entity.vo.UserVo;
import com.surekam.modules.sys.service.ApiSystemService;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.service.UserService;
import com.surekam.modules.sys.utils.DictUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaByProcedureRequest;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaByProcedureResponse;
import com.tencentcloudapi.vod.v20180717.models.SearchMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.SearchMediaResponse;

import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 腾讯云视频接口Controller
 * @author liwei
 * @version 2019-05-07
 */
@Controller
@RequestMapping(value = "api/video")
public class VideoController extends BaseController {
	@Autowired
	private VideoService videoService;
	
	@Autowired
	private VideoDao videoDao;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private ExpertsService expertsService;
	
	@Autowired
	private WebRTCSigService webRTCSigService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ApiSystemService apiSystemService;
	
	@Autowired
	private VideoMainService videoMainService;
	
	@Autowired
	private VideoMainDao videoMainDao;
	
	@Autowired
	private VideoPeopleInvolvementDao videoPeopleInvolvementDao;
	
	@Autowired
	private VideoPeopleInvolvementService videoPeopleInvolvementService;
	
	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;
	
	@Autowired
	private ExpertsGoodproblemService expertsGoodproblemService;
	
	private String APPID = "1259166614";
	private static String SecretId = "AKIDmZBe1LUbqngSQFqMivxw4FICxiAzOCXl";
	private static String SecretKey = "DbrytfUPDpIbryvHZCMNDuQmV3T7at1y";
	
	@ModelAttribute
	public Video get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return videoService.get(id);
		}else{
			return new Video();
		}
	}
	
	/**
	 * web维护视频用户列表
	 * @param request
	 * @param response
	 * @param loginName
	 * @param name
	 * @param companyId
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "getUserVideoList2", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "web维护视频用户列表", notes = "web维护视频用户列表", consumes = "application/json")
	public String getVideoList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required = false) String loginName,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String companyId,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		if(StringUtils.isNotBlank(name)) {
			name  = tet.StringReplaceAll(name);
		}
		try {
			int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
			int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
			Page<WebRTCSig> page = new Page<WebRTCSig>(pageNo, pageSize);
			page = webRTCSigService.findUser(page, loginName, name, companyId);
			List<WebRTCSig> list = page.getList();
			if(list.size() > 0) {
				for(Iterator<WebRTCSig> iterator = list.iterator(); iterator.hasNext();) {
					WebRTCSig webRTCSig = (WebRTCSig) iterator.next();
					webRTCSig.setRoleList(apiSystemService.getRoleByUserId(webRTCSig.getUid()));
				}
			}
			System.out.println(jsonMapper.toJson(ResultUtil.success(page)));
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	/**
	 * 根据平台code获取视频用户列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "getUserVideoList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "根据平台code获取视频用户列表", httpMethod = "GET", notes = "根据平台code获取视频用户列表", consumes = "application/x-www-form-urlencoded")
	public String getUserVideoList(HttpServletRequest request, @RequestParam(required = false) String platformCode) {
		List<Video> list = new ArrayList<Video>();
		if(StringUtils.isNotBlank(platformCode)){
			List<Video> videoList = videoService.getUserList(platformCode);
			for (Video video : videoList) {
				User user = systemService.getUserByLoginName(video.getUserCode());
				if(user!=null){
					video.setCompanyName(user.getOffice().getName());
					if(video.getType().equals("专家")){
						Experts expert = expertsService.getExpertByUserId(user.getId());
						if(expert!=null){
							video.setExpertTitle(expert.getExpertTitle());
						}
					}
					if(video.getType().equals("农户")){
						video.setExpertTitle("");
					}
					list.add(video);
				}
			}
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
	}
	
	/**
	 * 根据登录账户获取该用户的视频信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "getVideoByUserCode", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "根据登录账户获取该用户的视频信息", httpMethod = "GET", notes = "根据登录账户获取该用户的视频信息", consumes = "application/x-www-form-urlencoded")
	public String getVideoByUserCode(HttpServletRequest request,@RequestParam String userCode) {
		Video video = videoDao.getVideoByUserCode(userCode);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(video));
	}
	
	/**
	 * 根据平台code获取该平台的视频信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "getVideoByPlatformCode", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "根据登录账户获取该用户的视频信息", httpMethod = "GET", notes = "根据登录账户获取该用户的视频信息", consumes = "application/x-www-form-urlencoded")
	public String getVideoByPlatformCode(HttpServletRequest request, @RequestParam String platformCode) {
		Video video = videoService.getVideoByPlatformCode(platformCode);
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(video));
	}
	
	/**
	 * 邀请用户进入房间视频
	 * 
	 * @return
	 */
	@RequestMapping(value = "inviteUsersEntryRoom", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "根据登录账户获取该用户的视频信息", httpMethod = "GET", notes = "根据登录账户获取该用户的视频信息", consumes = "application/x-www-form-urlencoded")
	public String inviteUsersEntryRoom(HttpServletRequest request,@RequestParam String[] userCodeArray, @RequestParam String roomNumber) {
		try{
			List<String> list = videoService.inviteUsersEntryRoom(userCodeArray,roomNumber);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		}catch(Exception e){
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.INVITE_ERROR.getCode(), ResultEnum.INVITE_ERROR.getMessage()));
		}
	}
	
	/**
	 * 用户退出视频房间
	 * 
	 * @return
	 */
	@RequestMapping(value = "userExitRoom", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "用户退出视频房间", httpMethod = "GET", notes = "用户退出视频房间", consumes = "application/x-www-form-urlencoded")
	public String userExitRoom(HttpServletRequest request,@RequestParam String userCode) {
		try{
			videoService.userExitRoom(userCode);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		}catch(Exception e){
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.EXIT_ERROR.getCode(), ResultEnum.EXIT_ERROR.getMessage()));
		}
	}
	
	/**
	 * 用户退出视频房间
	 * 
	 * @return
	 */
	@RequestMapping(value = "userExitRoom_new", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "用户退出视频房间", httpMethod = "GET", notes = "用户退出视频房间", consumes = "application/x-www-form-urlencoded")
	public String userExitRoom_new(HttpServletRequest request,@RequestParam String userCode) {
		try{
			userExitRoom_new(userCode);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		}catch(Exception e){
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.EXIT_ERROR.getCode(), ResultEnum.EXIT_ERROR.getMessage()));
		}
	}
	
	/**
	 * 发起人退出视频房间
	 * 
	 * @return
	 */
	@RequestMapping(value = "fqrExitRoom", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "发起人退出视频房间", httpMethod = "GET", notes = "发起人退出视频房间", consumes = "application/x-www-form-urlencoded")
	public String fqrExitRoom(HttpServletRequest request,@RequestParam String roomNumber, @RequestParam(required = false) String userCode) {
		try{
			videoService.fqrExitRoom(roomNumber, userCode);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		}catch(Exception e){
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.EXIT_ERROR.getCode(), ResultEnum.EXIT_ERROR.getMessage()));
		}
	}
	
	/**
	 * 发起人退出视频房间
	 * 
	 * @return
	 */
	@RequestMapping(value = "fqrExitRoom_new", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "发起人退出视频房间", httpMethod = "GET", notes = "发起人退出视频房间", consumes = "application/x-www-form-urlencoded")
	public String fqrExitRoom_new(HttpServletRequest request,@RequestParam String roomNumber) {
		try{
			List<Video> plist = videoDao.find("from Video v where v.states<>'D' and v.entryRoomNumber=:p1",new Parameter(roomNumber));
			if(plist!=null && plist.size()>0){
				fqrExitRoom_new(roomNumber);
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
		}catch(Exception e){
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.EXIT_ERROR.getCode(), ResultEnum.EXIT_ERROR.getMessage()));
		}
	}
	
	public void userExitRoom_new(String userCode) throws Exception{
		String time = "";
		Video video = videoDao.getVideoByUserCode(userCode);
		String roomNumber = video.getEntryRoomNumber();
		String hql = " from VideoMain a where a.roomNumber=:p1 order by a.createTime desc";
		List<VideoMain> list = videoMainDao.find(hql,new Parameter(roomNumber));
		if(list!=null && list.size()>0){
			Date startTime = list.get(0).getStartTime();
			if(startTime!=null){
				time = getDateFormat(startTime);
			}
			final String sTime = time;
	    	final String rNumber = roomNumber;
	    	final String uCode = userCode;
	    	final String mainId =  list.get(0).getId();
	    	/*ExecutorService executor = Executors.newFixedThreadPool(1);
	        Future<Integer> future = executor.submit(new Callable<Integer>() {
	            @Override
	            public Integer call() throws Exception {
	                System.out.println("===task start===");
	                firstOperation(sTime, rNumber, uCode, mainId);
	                System.out.println("===task finish===");
	                return 3;
	            }
	        });
	        System.out.println(future.get());*/
	    	firstOperation(sTime, rNumber, uCode, mainId);
		}
		String sql = "UPDATE t_agro_video a SET a.entry_room_number='' WHERE a.states<>'D' AND a.user_code=:p1";
		videoDao.updateBySql(sql,new Parameter(userCode));
		videoDao.flush();
	}
	
	public void fqrExitRoom_new(String roomNumber) throws Exception{
		String hql = " from VideoMain a where a.roomNumber=:p1 order by a.createTime desc";
		List<VideoMain> list = videoMainDao.find(hql,new Parameter(roomNumber));
		if(list!=null && list.size()>0){
			VideoMain videoMain = list.get(0);
			videoMain.setEndTime(getWebsiteDatetime("http://www.taobao.com"));
			videoMainDao.save(videoMain);
			String time = "";
			Date startTime = videoMain.getStartTime();
			if(startTime!=null){
				time = getDateFormat(startTime);
			}
			List<Video> plist = videoDao.find("from Video v where v.states<>'D' and v.entryRoomNumber=:p1",new Parameter(roomNumber));
			for (Video video : plist) {
				final String sTime = time;
		    	final String rNumber = roomNumber;
		    	final String uCode = video.getUserCode();
		    	final String mainId = videoMain.getId();
				/*ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		        cachedThreadPool.execute(new Runnable() {
		             @Override
		             public void run() {
		            	 System.out.println("task start-----");
		            	 firstOperation(sTime, rNumber, uCode, mainId);
		            	 System.out.println("task end-----");
		             }
		        });*/
		    	firstOperation(sTime, rNumber, uCode, mainId);
			}
			
			if(StringUtils.isNotBlank(roomNumber)){
				videoDao.update("update Video v set v.entryRoomNumber='' where v.states<>'D' and v.entryRoomNumber = :p1",new Parameter(roomNumber));
				videoDao.flush();
			}
		}
	}
	
    public void firstOperation(String startTime, String roomNumber, String userCode, String mainId){
    	try {
	    	String streamId = "53408_"+MD5Utils.MD5Encode(roomNumber+"_"+userCode+"_main","utf8");
			//第一步搜索媒体信息
	    	System.out.println("开始000000000000000                  "+streamId+"            "+startTime);	
			Credential cred = new Credential(SecretId, SecretKey);
	        HttpProfile httpProfile = new HttpProfile();
	        httpProfile.setEndpoint("vod.tencentcloudapi.com");
	        ClientProfile clientProfile = new ClientProfile();
	        clientProfile.setHttpProfile(httpProfile);            
	        VodClient client = new VodClient(cred, "ap-chongqing", clientProfile);
	        String params = "{\"StartTime\":\"" + startTime + "\",\"StreamId\":\"" + streamId + "\",\"Sort\":{\"Field\":\"CreateTime\",\"Order\":\"Desc\"}}";
	        SearchMediaRequest req = SearchMediaRequest.fromJsonString(params, SearchMediaRequest.class);
	        SearchMediaResponse resp = client.SearchMedia(req);
	        System.out.println("第一步开始1111111111111"+"      "+resp.getRequestId()+"_"+resp.getTotalCount());	
	        if(resp.getMediaInfoSet()!=null && resp.getMediaInfoSet().length>0){
	        	String fileId = resp.getMediaInfoSet()[0].getFileId();
	        	System.out.println("第一步结束2222222222222          "+fileId);	
	        	savePeopleInfo(userCode, mainId, fileId);
	        	secordOperation(fileId, userCode, mainId);
	        }else{
	        	Thread.sleep(10 * 1000);
	        	firstOperation(startTime, roomNumber, userCode, mainId);
	        }
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void secordOperation(String fileId, String userCode, String mainId){
    	try {
    		System.out.println("第二步开始4444444444444444"+"       "+fileId);
    		Credential cred = new Credential(SecretId, SecretKey);
	        HttpProfile httpProfile = new HttpProfile();
	        httpProfile.setEndpoint("vod.tencentcloudapi.com");
	        ClientProfile clientProfile = new ClientProfile();
	        clientProfile.setHttpProfile(httpProfile);            
	        VodClient client = new VodClient(cred, "ap-chongqing", clientProfile);
	        //第二步调用【使用任务流模板进行视频处理：https://cloud.tencent.com/document/product/266/34782】接口处理视频
	        if(StringUtils.isNotBlank(fileId)){
	            String params = "{\"FileId\":\"" + fileId + "\",\"ProcedureName\":\"TranscodeAndSnapshot\"}";
	            ProcessMediaByProcedureRequest req1 = ProcessMediaByProcedureRequest.fromJsonString(params, ProcessMediaByProcedureRequest.class);
	            ProcessMediaByProcedureResponse resp1 = client.ProcessMediaByProcedure(req1);
            	if(StringUtils.isNotBlank(resp1.getTaskId())){
            		System.out.println("第二步结束4444444444444444444" + "     "+resp1.getTaskId());
	            }
	        }
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void savePeopleInfo(String userCode, String mainId, String fileId){
    	VideoPeopleInvolvement people = videoPeopleInvolvementDao.findVideoPeopleInvolvementByMainId(fileId); 
    	if(people==null){
    		User user = systemService.getUserByLoginName(userCode);
        	//Video video = videoDao.getVideoByUserCode(userCode);
        	VideoPeopleInvolvement videoPeople = new VideoPeopleInvolvement();
        	videoPeople.setMainId(mainId);
        	videoPeople.setFileId(fileId);
        	videoPeople.setUserCode(userCode);
        	if(user!=null){
        		Experts experts = expertsService.getExpertByUserId(user.getId());
        		if(StringUtils.isNotBlank(experts.getId())){
        			videoPeople.setUserType("专家");
        		}else{
        			videoPeople.setUserType("农户");
        		}
        		videoPeople.setUserName(user.getName());
        		videoPeople.setUserId(user.getId());
        		videoPeople.setCompanyName(user.getCompany().getName());
        	}else{
        		if(userCode.equals("18800000001")){
        			videoPeople.setUserName("望城平台");
        		}
        		if(userCode.equals("18800000002")){
        			videoPeople.setUserName("吐鲁番平台");
        		}
        		if(userCode.equals("18800000003")){
        			videoPeople.setUserName("水稻平台");
        		}
        		if(userCode.equals("nxnh")){
        			videoPeople.setUserName("南县平台");
        		}
        		videoPeople.setUserType("展示");
        	}
        	videoPeopleInvolvementDao.save(videoPeople);
    	}
    }
	
	public static Date getWebsiteDatetime(String webUrl){
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            return date;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	//转换时间格式
    public static String getDateFormat(Date time){
    	Calendar c = Calendar.getInstance();	//创建表示当前时间的Calendar对象
    	c.setTime(time);
		c.add(Calendar.HOUR_OF_DAY,-8);//小时减8小时
		String year = c.get(c.YEAR)+"";				//获得当前时间的年
		String month =(c.get(c.MONTH)+1)<10?("0"+(c.get(c.MONTH)+1)):((c.get(c.MONTH)+1)+"");			//获得当前时间的月
		String date = c.get(c.DAY_OF_MONTH)<10?("0"+c.get(c.DAY_OF_MONTH)):(c.get(c.DAY_OF_MONTH)+"");		//获得当前时间的日
		String hour = c.get(c.HOUR_OF_DAY)<10?("0"+c.get(c.HOUR_OF_DAY)):(c.get(c.HOUR_OF_DAY)+"");	//获得当前时间的小时
		String minute = c.get(c.MINUTE)<10?("0"+c.get(c.MINUTE)):(c.get(c.MINUTE)+"");			//获得当前时间的分钟
		String second = c.get(c.SECOND)<10?("0"+c.get(c.SECOND)):(c.get(c.SECOND)+"");			//获得当前时间的秒
		String timeStr = year+"-"+month+"-"+date+"T"+hour+":"+minute+":"+second+"Z";
    	return timeStr;
    }
	
	/**
     * - 验证企业编号是否存在
     * @return
     */
    @RequestMapping(value = "verificationRoomNumber",produces="application/json;charset=UTF-8", method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = " 房间号是否存在的验证", httpMethod = "GET",notes = " 房间号是否存在的验证",	consumes="application/json")
    public String verificationRoomNumber(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) String code) {
    	response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			String token = request.getHeader("X-Token");
			if(token != null) {
				List<Video> video  = videoService.verificationRoomNumber(code);
				Map<String,Object> map = new HashMap<String,Object>();
				if(video != null && video.size() > 0) {
					//传入参数企业编号,查询到记录,则编号已经存在
					map.put("states", 1);
					System.out.println(JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map)));
					return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
				}else {
					map.put("states", 0);
					System.out.println(JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map)));
					return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(map));
				}
			}else {
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
			}
		} catch (Exception e) {
			logger.error("房间号是否存在的验证："+e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
    }
    
    @SuppressWarnings("static-access")
	@RequestMapping(value = "saveVideo", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "保存视频用户信息", httpMethod = "POST", notes = "保存视频用户信息", consumes = "application/json")
    public String saveVideo(HttpServletRequest request,HttpServletResponse response,@RequestBody Video video) {
    	response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
		try {
			User user = userService.get(video.getUserId());
			video.setUserCode(user.getLoginName());
			video.setUserName(user.getName());
			video.setOnlineStates("1");
			video.setInRoom("0");
			video.setCreateTime(new Date());
			video.setCreateUserId(user.getId());
			if(video.getType().equals("专家")) {
				Video V1 = videoService.getVideo("专家");
				int num = Integer.parseInt(V1.getCreateRoomNumber()) + 1;
				List<Video> vd  = videoService.verificationRoomNumber(String.valueOf(num));
				if(vd != null && vd.size() > 0) {
					num = num + 1;
				}
				WebRTCSigApi web = new WebRTCSigApi();
				String userToken = web.getPrivateMapKey(num , user.getLoginName());
				video.setCreateRoomNumber(String.valueOf(num));
				video.setUserToken(userToken);
			}else if(video.getType().equals("展示")) {
				Video V2 = videoService.getVideo("展示");
				int num = Integer.parseInt(V2.getCreateRoomNumber()) + 1;
				List<Video> vd  = videoService.verificationRoomNumber(String.valueOf(num));
				if(vd != null && vd.size() > 0) {
					num = num + 1;
				}
				WebRTCSigApi web = new WebRTCSigApi();
				String userToken = web.getPrivateMapKey(num , user.getLoginName());
				video.setCreateRoomNumber(String.valueOf(num));
				video.setUserToken(userToken);
			}else if(video.getType().equals("农户")) {
				WebRTCSigApi web = new WebRTCSigApi();
				String userToken = web.getPrivateMapKey(user.getLoginName());
				video.setUserToken(userToken);
			}
			
			videoService.save(video);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("保存信息错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
    }
    
    @RequestMapping(value = "editVideo", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "保存视频用户信息", httpMethod = "POST", notes = "保存视频用户信息", consumes = "application/json")
    public String editVideo(HttpServletRequest request,HttpServletResponse response,@RequestBody Video video) {
    	response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
		try {
			User user = userService.get(video.getUserId());
			Video video2 = videoService.get(video.getId());
			video2.setUpdateTime(new Date());
			video2.setUpdateUserId(user.getId());
			video2.setPlatform(video.getPlatform());
			videoService.save(video2);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("保存信息错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
    }
    
    
    @RequestMapping(value = "resume", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "启用", httpMethod = "POST", notes = "启用", consumes = "application/x-www-form-urlencoded")
    public String resume(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) String id) {
    	response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
		try {
			videoService.resume(id);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("保存信息错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
    }
    @RequestMapping(value = "delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "删除", httpMethod = "POST", notes = "删除", consumes = "application/x-www-form-urlencoded")
    public String delete(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) String id) {
    	response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
		try {
			videoService.delete(id);
			return jsonMapper.toJson(ResultUtil.success());
		} catch (Exception e) {
			logger.error("保存信息错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
    }
    /**
     * 视频用户角色字典查询
     * @return
     */
    @RequestMapping(value = "getVideoRole", produces = "application/json;charset=UTF-8",method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "视频用户角色字典查询",httpMethod = "GET", notes = "视频用户角色字典查询", consumes = "application/json")
    public String getVideoRole() {
    	List<Dict> list = DictUtils.getDictList("video_role");
    	return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
    	
    }
    /**
     * 视频用户平台查询
     * @return
     */
    @RequestMapping(value = "getPlatform", produces = "application/json;charset=UTF-8",method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "视频用户平台查询",httpMethod = "GET", notes = "视频用户平台查询", consumes = "application/json")
    public String getPlatform() {
    	List<Dict> list = DictUtils.getDictList("platform");
    	return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
    }
    
    /**
     * 更新用戶表中userToken（通訊IM的token）字段的值
     * @return
     */
    @RequestMapping(value = "saveUser", produces = "application/json;charset=UTF-8",method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "更新用戶表中userToken字段的值",httpMethod = "GET", notes = "更新用戶表中userToken字段的值", consumes = "application/json")
    public String saveUser() {
    	String hql = " from User a where a.delFlag='0'";
    	List<User> list = userDao.find(hql);
    	for (User user : list) {
    		String privateMapKey = WebRTCSigApi.getPrivateMapKey_book(user.getLoginName());
			userDao.updateBySql("update sys_user a SET a.user_token=:p1 where a.login_name=:p2 and a.del_flag='0'", new Parameter(privateMapKey,user.getLoginName()));
			userDao.flush();
		}
    	return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success());
    }
    
    /**
     * 保存视频主表信息
     * @return
     */
    @RequestMapping(value = "saveVideoMain", produces = "application/json;charset=UTF-8",method = {RequestMethod.GET})
	@ResponseBody
	@ApiOperation(value = "保存视频主表信息", httpMethod = "POST", notes = "保存视频主表信息", consumes = "application/x-www-form-urlencoded")
    public String saveVideoMain(VideoMain videoMain) {
    	//TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    	JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<UserVo> resultBean = new ResultBean<UserVo>();
    	try {
    		String title = "";
    		if(StringUtils.isNotBlank(videoMain.getTitle())){
    			title = URLDecoder.decode(videoMain.getTitle(), "UTF-8");
    			videoMain.setTitle(title);
    		}
    		videoMain.setStartTime(videoService.getWebsiteDatetime("http://www.taobao.com"));
        	videoMainService.save(videoMain);
        	
        	videoDao.update("update Video v set v.entryRoomNumber=:p1 where v.states<>'D' and v.createRoomNumber=:p1",new Parameter(videoMain.getRoomNumber()));
        	videoDao.flush();
        	
			return jsonMapper.toJson(ResultUtil.success(videoMain.getId()));
		} catch (Exception e) {
			logger.error("保存信息错误：" + e);
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
    }
    
    /**
     * 获取搜索媒体信息的回调
     * @return
     * @throws IOException 
     */
    @RequestMapping(value = "getProcessMedia", produces = "application/json;charset=UTF-8",method = {RequestMethod.POST})
	@ResponseBody
	@ApiOperation(value = "视频用户平台查询",httpMethod = "POST", notes = "视频用户平台查询", consumes = "application/json")
    public void getProcessMedia(HttpServletRequest request) throws IOException {
    	BufferedReader br = request.getReader();
    	String str, wholeStr = "";
    	while((str = br.readLine()) != null){
    		wholeStr += str;
    	}
    	System.out.println("回调值：             "+wholeStr);
    	JSONObject jsonobject = JSONObject.fromObject(wholeStr);
    	String eventType = jsonobject.getString("eventType");
    	if(eventType.equals("ProcedureStateChanged")){
    		JSONObject json = jsonobject.getJSONObject("data");
    		String fileId = json.getString("fileId");
    		if(json!=null && !json.equals("null")){
    			JSONArray array = json.getJSONArray("processTaskList");
    			if(array!=null && array.size()>0){
    				for (int i =0;i<array.size();i++) {
    					JSONObject js = array.getJSONObject(i);
    					String type = js.getString("taskType");
    					if(type.equals("CoverBySnapshot")){
    						JSONObject outJson = js.getJSONObject("output");
							if(outJson!=null && !outJson.equals("null")){
								String url = outJson.getString("imageUrl");
								VideoPeopleInvolvement videoPeopleInvolvement = videoPeopleInvolvementService.findVideoPeopleInvolvementByTaskId(fileId);
								if(videoPeopleInvolvement!=null){
									if(StringUtils.isBlank(videoPeopleInvolvement.getThumbnail())){
										videoPeopleInvolvement.setThumbnail(url);
					        			videoPeopleInvolvementService.save(videoPeopleInvolvement);
									}
								}
							}
    					}
					}
    			}
        	}
    	}
    }
    
    /**
	 * 视频会议分页接口（查询表为实时音视频主表）
	 * @param 开始时间（非必填）、结束时间（非必填）、关键字（非必填）、当前页数（必填）、每页数量（必填）
	 * @return
	 */
	@RequestMapping(value = "getVideoMainList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "视频会议分页接口（查询表为实时音视频主表）", httpMethod = "GET", notes = "视频会议分页接口（查询表为实时音视频主表）", consumes = "application/json")
	public String getVideoMainList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required = false) String startTime,
			@RequestParam(required = false) String endTime,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String platform,
			@RequestParam Integer pageno, @RequestParam Integer pagesize) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
			int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE: pagesize;
			Page<VideoMain> page = new Page<VideoMain>(pageNo, pageSize);
			page = videoMainService.find(page, startTime, endTime, title, platform);
			System.out.println(jsonMapper.toJson(ResultUtil.success(page)));
			return jsonMapper.toJson(ResultUtil.success(page));
		} catch (Exception e) {
			logger.error("用户列表查询错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}
	
	/**
	 * 根据平台code获取视频用户列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "getVideoPeopleInvolvementList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "根据实时音视频编号查询参与人员数据（查询表为实时音视频参与人员表）", httpMethod = "GET", notes = "根据实时音视频编号查询参与人员数据（查询表为实时音视频参与人员表）", consumes = "application/x-www-form-urlencoded")
	public String getVideoPeopleInvolvementList(HttpServletRequest request, @RequestParam String mainId) {
		List<VideoPeopleInvolvement> list = videoPeopleInvolvementService.getVideoPeopleInvolvementList(mainId);
		for (VideoPeopleInvolvement videoPeopleInvolvement : list) {
			Experts experts = expertsService.getExpertsByUserCode(videoPeopleInvolvement.getUserCode());
			if(experts!=null){
				videoPeopleInvolvement.setExpertDescription(experts.getExpertDescription());
				
				List<ProductLibraryTree> plist = productLibraryTreeService.getList(experts.getId());
				List<ExpertsGoodproblem> glist = expertsGoodproblemService.getList(experts.getId());
				String pstr = "";
				String gstr = "";
				for(int i = 0; i < plist.size(); i++) {
					pstr += plist.get(i).getProductCategoryName() + ",";
				}
				if(StringUtils.isNotBlank(pstr)){
					pstr = pstr.substring(0,pstr.length() - 1);
				}
				videoPeopleInvolvement.setProductData(pstr);
				for(int i = 0; i < glist.size(); i++) {
					gstr += glist.get(i).getGoodProblem() + ",";
				}
				if(StringUtils.isNotBlank(gstr)){
					gstr = gstr.substring(0,gstr.length() - 1);
				}
				videoPeopleInvolvement.setGoodproblemData(gstr);
				
			}
		}
		return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
	}
	
	/**
	 * 判断登录用户自己是否开通视频聊天功能
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getUserVideo", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "判断登录用户自己是否开通视频聊天功能", httpMethod = "GET", notes = "判断登录用户自己是否开通视频聊天功能", consumes = "application/x-www-form-urlencoded")
	public String getUserVideo(HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if(token != null) {
			User currentUser = apiSystemService.findUserByToken(token);
			Video video = new Video();
			video = videoService.getVideoByLoginName(currentUser.getLoginName());
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(video));
		}else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
		
	}
	
}
