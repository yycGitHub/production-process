package com.surekam.modules.cms.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.surekam.common.persistence.IdEntity;
import com.surekam.common.utils.Collections3;
import com.surekam.modules.sys.entity.Role;
import com.surekam.modules.sys.entity.User;

/**
 * 栏目管理员Entity
 * @author liuyi
 * @version 2018-01-29
 */
@Entity
@Table(name = "cms_category_role_user")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CategoryRoleUser extends IdEntity<CategoryRoleUser> {
	
	private static final long serialVersionUID = 1L;
	
	
	private Integer categoryId;//cms_category表主键
	private String roleId;//sys_role表主键
	private String userId;//sys_user表主键
	private String delFlag; // 删除标记（0：正常；1：删除；）
	
	private List<Role> roleList = Lists.newArrayList(); // 角色列表
	private List<User> userList = Lists.newArrayList(); // 版主列表
	

	public CategoryRoleUser() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}
	
	/*@PrePersist
	public void prePersist(){
		this.id = IdGen.uuid();
	}*/
	
	@Transient
	@JsonIgnore
	public List<Role> getRoleList() {
		//roleList = UserUtils.getRoleList();
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
	
	@Transient
	@JsonIgnore
	public List<User> getUserList() {
		return userList;
	}

	
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	@Transient
	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		if(StringUtils.isNotBlank(roleId)){
			String[] roleIdArr = roleId.split(",");
			for(int i = 0;i<roleIdArr.length;i++){
				roleIdList.add(roleIdArr[i]);
			}
		}
		//for (Role role : roleList) {
			//roleIdList.add(role.getId());
		//}	
		return roleIdList;
	}

	
	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	@Transient
	@JsonIgnore
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "name", ", ");
	}
		
	
	@Column(name="CATEGORY_ID")
	public Integer getCategoryId() {
		return this.categoryId;
	}
	public void setCategoryId(Integer id) {
		this.categoryId = id;
	}
	
	@Column(name="ROLE_ID")
	public String getRoleId() {
		return this.roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	@Column(name="USER_ID")
	public String getUserId() {
		return this.userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name="DEL_FLAG")
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	
}


