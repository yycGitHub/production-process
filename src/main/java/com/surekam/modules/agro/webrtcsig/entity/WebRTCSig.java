package com.surekam.modules.agro.webrtcsig.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.google.common.collect.Lists;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.modules.sys.entity.Role;

import java.lang.String;
import java.util.List;

/**
 * 视频聊天人员信息Entity
 * @author xy
 * @version 2019-06-04
 */
@Entity
@Table(name = "v_agro_webRTCSig")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WebRTCSig extends XGXTEntity<WebRTCSig> {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "uid")
	private String uid;//编号
	
	@Column(name = "officeid")
	private String officeid;//编号
	
	@Column(name = "parentid")
	private String parentid;//父级编号
	
	@Column(name = "parentids")
	private String parentids;//所有父级编号
	
	@Column(name = "officename")
	private String officename;//机构名称
	
	@Column(name = "loginname")
	private String loginname;//登录名
	
	@Column(name = "name")
	private String name;//姓名
	
	@Column(name = "avid")
	private String avid;//主键
	
	@Column(name = "usertoken")
	private String usertoken;//用户TOKEN
	
	@Column(name = "roomnumber")
	private String roomnumber;//创建房间号
	
	@Column(name = "onlinestates")
	private String onlinestates;//在线状态（0-不在线，1-在线）
	
	@Column(name = "inroom")
	private String inroom;//是否在房间（0-不在，1-在）
	
	@Column(name = "type")
	private String type;//类型（展示账号，专家，农户）
	
	@Column(name = "platform")
	private String platform;//展示类型为展示平台，此处会写值
	
	private List<Role> roleList = Lists.newArrayList();

	public WebRTCSig() {
		super();
	}
	@Id
	public String getUid() {
		return this.uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getOfficeid() {
		return this.officeid;
	}
	public void setOfficeid(String officeid) {
		this.officeid = officeid;
	}
	
	public String getParentid() {
		return this.parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	
	public String getParentids() {
		return this.parentids;
	}
	public void setParentids(String parentids) {
		this.parentids = parentids;
	}
	
	public String getOfficename() {
		return this.officename;
	}
	public void setOfficename(String officename) {
		this.officename = officename;
	}
	
	public String getLoginname() {
		return this.loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAvid() {
		return this.avid;
	}
	public void setAvid(String avid) {
		this.avid = avid;
	}
	
	public String getUsertoken() {
		return this.usertoken;
	}
	public void setUsertoken(String usertoken) {
		this.usertoken = usertoken;
	}
	
	public String getRoomnumber() {
		return this.roomnumber;
	}
	public void setRoomnumber(String roomnumber) {
		this.roomnumber = roomnumber;
	}
	
	public String getOnlinestates() {
		return this.onlinestates;
	}
	public void setOnlinestates(String onlinestates) {
		this.onlinestates = onlinestates;
	}
	
	public String getInroom() {
		return this.inroom;
	}
	public void setInroom(String inroom) {
		this.inroom = inroom;
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
	@Transient
	public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
}


