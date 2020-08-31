package com.surekam.modules.agro.video.service;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.surekam.common.persistence.Page;
import com.surekam.common.persistence.Parameter;
import com.surekam.common.service.BaseService;
import com.surekam.common.utils.MD5Utils;
import com.surekam.common.utils.StringUtils;
import com.surekam.modules.agro.experts.entity.Experts;
import com.surekam.modules.agro.experts.service.ExpertsService;
import com.surekam.modules.agro.productionbatch.entity.ProductionBatch;
import com.surekam.modules.agro.video.dao.VideoDao;
import com.surekam.modules.agro.video.entity.Video;
import com.surekam.modules.agro.videomain.dao.VideoMainDao;
import com.surekam.modules.agro.videomain.entity.VideoMain;
import com.surekam.modules.agro.videopeopleinvolvement.dao.VideoPeopleInvolvementDao;
import com.surekam.modules.agro.videopeopleinvolvement.entity.VideoPeopleInvolvement;
import com.surekam.modules.agro.videopeopleinvolvement.service.VideoPeopleInvolvementService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.SystemService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaByProcedureRequest;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaByProcedureResponse;
import com.tencentcloudapi.vod.v20180717.models.SearchMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.SearchMediaResponse;

/**
 * 腾讯云视频接口Service
 * @author liwei
 * @version 2019-05-07
 */
@Component
@Transactional(readOnly = true)
public class VideoService extends BaseService {
	@Autowired
	private VideoDao videoDao;
	
	@Autowired
	private VideoMainDao videoMainDao;
	
	public Video get(String id) {
		return videoDao.get(id);
	}
	
	public Page<Video> find(Page<Video> page, Video video) {
		DetachedCriteria dc = videoDao.createDetachedCriteria();
		dc.add(Restrictions.eq(Video.FIELD_DEL_FLAG, Video.DEL_FLAG_NORMAL));
		return videoDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(Video video) {
		videoDao.save(video);
	}
	
	public List<Video> getUserList(String platformCode) {
		String hql =  "from Video v where v.states<>'D' and v.onlineStates='1' and v.platform like '%" + platformCode + "%' and (v.entryRoomNumber='' or v.entryRoomNumber is null) and v.type in ('专家','农户')";
		return videoDao.find(hql);
	}
	
	public List<Video> getExpertsOnlineCount(String platformCode) {
		List<Video> list = videoDao.find(" from Video v where v.states<>'D' and v.type = '专家' and v.platform like '%" + platformCode + "%'");
		return list;
	}
	
	public Video getVideoByPlatformCode(String platformCode) {
		List<Video> list = videoDao.find(" from Video v where v.states<>'D' and v.platform = :p1 and v.type = '展示'",new Parameter(platformCode));
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return new Video();
		}
	}
	
	public List<Video> getVideoList() {
		List<Video> list = videoDao.find(" from Video v where v.states<>'D' and v.type = '专家'");
		return list;
	}
	
	@Transactional(readOnly = false)
	public List<String> inviteUsersEntryRoom(String[] userCodeArray,String roomNumber) throws Exception{
		List<String> list = new ArrayList<String>();
		for (String userCode : userCodeArray) {
			Video video = videoDao.getVideoByUserCode(userCode);
			if(video!=null){
				if(StringUtils.isNotBlank(video.getEntryRoomNumber())){
					list.add(video.getUserName());
				}else{
					video.setEntryRoomNumber(roomNumber);
					videoDao.save(video);
				}
			}
		}
		return list;
	}
	
	@Transactional(readOnly = false)
	public void userExitRoom(String userCode) throws Exception{
		String sql = "UPDATE t_agro_video a SET a.entry_room_number='' WHERE a.states<>'D' AND a.user_code=:p1";
		videoDao.updateBySql(sql,new Parameter(userCode));
		videoDao.flush();
		//Video video = getVideoByUserCode(userCode);
		//video.setEntryRoomNumber("");
		//videoDao.save(video);
	}
	
	@Transactional(readOnly = false)
	public void fqrExitRoom(String roomNumber, String userCode) throws Exception{
		if(StringUtils.isNotBlank(roomNumber)){
			if(roomNumber.length() == 6){
				videoDao.update("update Video v set v.entryRoomNumber='' where v.states<>'D' and v.userCode = :p1",new Parameter(userCode));
				videoDao.flush();
			}else{
				videoDao.update("update Video v set v.entryRoomNumber='' where v.states<>'D' and v.entryRoomNumber = :p1",new Parameter(roomNumber));
				videoDao.flush();
			}
		}
	}
	
	/**
     * -  房间号是否存在的验证
     * @param code
     * @return
     */
    public List<Video> verificationRoomNumber(String code){
		DetachedCriteria dc = videoDao.createDetachedCriteria();		
		dc.add(Restrictions.eq("createRoomNumber", code));
		return videoDao.find(dc);
	}
    
    @Transactional(readOnly = false)
    public void delete(String id) {
    	videoDao.deleteByXGXTId(id);
    }
    
    @Transactional(readOnly = false)
    public void resume(String id) {
    	videoDao.resumeByXGXTId(id);
    }
    
    
    public Video getVideo(String type) {
		List<Video> list = videoDao.find(" from Video v where v.type = :p1 order by createRoomNumber desc ",new Parameter(type));
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
    
    public Video getVideoByLoginName(String loginName) {
		List<Video> list = videoDao.find(" from Video v where v.states<>'D' and v.userCode = :p1 order by createRoomNumber desc ",new Parameter(loginName));
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
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

    
    public static void main(String[] args) {
    	System.out.println(getDateFormat(getWebsiteDatetime("http://www.taobao.com")));
	}
    
}
