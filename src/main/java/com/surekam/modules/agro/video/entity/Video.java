package com.surekam.modules.agro.video.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.PrePersist;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import java.lang.String;

/**
 * 腾讯云视频接口Entity
 * @author liwei
 * @version 2019-05-07
 */
@Entity
@Table(name = "t_agro_video")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Video extends XGXTEntity<Video> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String userCode;//用户账号
	private String userName;//用户姓名
	private String userToken;//用户TOKEN
	private String createRoomNumber;//创建房间号
	private String entryRoomNumber;//邀请进入房间号
	private String onlineStates;//在线状态（0-不在线，1-在线）
	private String inRoom;//是否在房间（0-不在，1-在）
	private String type;//类型（1-展示账号，2-专家账号，3-用户账号）
	private String platform;//平台
	private String companyName;//公司名称
	private String expertTitle;//专家职称

	private String userId;//专家职称
	
	public Video() {
		super();
	}
	
	@PrePersist
	public void prePersist(){
		this.id = IdGen.uuid();
	}
		
	@Id
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserCode() {
		return this.userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	public String getUserName() {
		return this.userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserToken() {
		return this.userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	
	public String getCreateRoomNumber() {
		return this.createRoomNumber;
	}
	public void setCreateRoomNumber(String createRoomNumber) {
		this.createRoomNumber = createRoomNumber;
	}
	
	public String getEntryRoomNumber() {
		return this.entryRoomNumber;
	}
	public void setEntryRoomNumber(String entryRoomNumber) {
		this.entryRoomNumber = entryRoomNumber;
	}
	
	public String getOnlineStates() {
		return this.onlineStates;
	}
	public void setOnlineStates(String onlineStates) {
		this.onlineStates = onlineStates;
	}
	
	public String getInRoom() {
		return this.inRoom;
	}
	public void setInRoom(String inRoom) {
		this.inRoom = inRoom;
	}
	
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@Transient
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Transient
	public String getExpertTitle() {
		return expertTitle;
	}

	public void setExpertTitle(String expertTitle) {
		this.expertTitle = expertTitle;
	}

	@Transient
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}


