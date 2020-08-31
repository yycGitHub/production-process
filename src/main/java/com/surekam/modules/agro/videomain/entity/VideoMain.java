package com.surekam.modules.agro.videomain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.PrePersist;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import java.lang.String;
import java.util.Date;

/**
 * 实时音视频主表Entity
 * @author liwei
 * @version 2019-07-16
 */
@Entity
@Table(name = "t_agro_video_main")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VideoMain extends XGXTEntity<VideoMain> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String title;//主题
	private Date startTime;//开始时间
	private Date endTime;//结束时间
	private String roomNumber;//房间号
	private String type;//类型（1、多人视频会议     2、一对一专家咨询）
	private String platform;//所属平台（wangcheng-望城，tulufan-吐鲁番，rice-水稻，nanxian-南县）
	private String introduction;//简介（视频运营人员维护）

	public VideoMain() {
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
	
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Date getStartTime() {
		return this.startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return this.endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getPlatform() {
		return this.platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	public String getIntroduction() {
		return this.introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
	
}


