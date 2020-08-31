package com.surekam.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.surekam.common.persistence.BaseEntity;
import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;
import com.surekam.modules.sys.utils.UserUtils;

import java.lang.String;
import java.util.Date;
import java.util.List;

/**
 * 项目组Entity
 * @author liuyi
 * @version 2018-03-23
 */
@Entity
@Table(name = "sys_group")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group extends BaseEntity<Group> {
	
	private static final long serialVersionUID = 1L;
	private String groupId;//组主键id
	private String name;//组名
	//private String parentId;//父id
	private String parentIds;//所有父id
	private String groupType;//组类型
/*	private String createBy;//创建者
	private String createDate;//创建时间
	private String updateBy;//更新者
	private String updateDate;//更新时间
	private String remarks;//备注
	private String delFlag;//删除标记
*/	
	private String remarks;	// 备注
	private User createBy;	// 创建者
	private Date createDate = new Date();// 创建日期
	private User updateBy;	// 更新者
	private Date updateDate;// 更新日期
	private String delFlag; // 删除标记（0：正常；1：删除；）
	
	private boolean master;// 是否是组管理
	private Group parent;	// 父级对象
	
	private List<Group> childList = Lists.newArrayList();// 拥有子组列表

	public Group() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}
	
	/*@PrePersist
	public void prePersist(){
		this.groupId = IdGen.uuid();
	}*/
		
	@Id
	public String getGroupId() {
		return this.groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Length(min=1, max=100)
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/*@Column(name="PARENT_ID")
	public String getParentId() {
		return this.parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}*/
	
	public String getParentIds() {
		return this.parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	public String getGroupType() {
		return this.groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
	
	@PrePersist
	public void prePersist(){
		this.groupId = IdGen.uuid();
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		//this.createDate = this.updateDate;
	}
	
	@PreUpdate
	public void preUpdate(){
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			this.updateBy = user;
		}
		this.updateDate = new Date();
	}
	
	@Length(min=0, max=2550)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}

	@JsonIgnore
	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	@DateBridge(resolution = Resolution.DAY)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Length(min=1, max=1)
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	
	

	/**
	 * @return the isMaster
	 */
	@Transient
	public boolean getMaster() {
		return master;
	}

	/**
	 * @param isMaster the isMaster to set
	 */
	@Transient
	public void setMaster(boolean master) {
		this.master = master;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public Group getParent() {
		return parent;
	}

	public void setParent(Group parent) {
		this.parent = parent;
	}
	
	@OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="group_id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Group> getChildList() {
		return childList;
	}

	public void setChildList(List<Group> childList) {
		this.childList = childList;
	}
	
	@Transient
	public boolean isRoot(){
		return isRoot(this.groupId);
	}
	
	@Transient
	public static boolean isRoot(String id){
		return id != null && id.equals("1");
	}
	
}


