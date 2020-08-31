package com.surekam.modules.standard.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.surekam.common.persistence.DataEntity;
import com.surekam.common.utils.DateUtils;

@Entity
@Table(name = "sys_standard")
@DynamicInsert 
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Standard extends DataEntity<Standard> {

	private static final long serialVersionUID = 1L;
	private Standard parent;	// 父级菜单
	private String parentIds; // 所有父级编号
	private String name; 	// 名称
	private Integer sort; 	// 排序
	private String type;
	
	private String synTime = DateUtils.getDateTime();//同步时间
	
	public String getSynTime() {
		return synTime;
	}

	public void setSynTime(String synTime) {
		this.synTime = synTime;
	}
	
	private List<Standard> childList = Lists.newArrayList();// 拥有子菜单列表
	
	public Standard(){
		super();
		this.sort = 30;
	}
	
	public Standard(String id){
		this();
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	@JsonIgnore
	public Standard getParent() {
		return parent;
	}

	public void setParent(Standard parent) {
		this.parent = parent;
	}

	@Length(min=1, max=255)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="sort") 
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	public List<Standard> getChildList() {
		return childList;
	}

	public void setChildList(List<Standard> childList) {
		this.childList = childList;
	}
	
	@Transient
	public static void sortList(List<Standard> list, List<Standard> sourcelist, String parentId){
		for (int i=0; i<sourcelist.size(); i++){
			Standard e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					Standard child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getId()!=null
							&& child.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 查找树的所有节点
	 * @param list 结果集
	 * @param sourcelist 搜索目标树
	 * @param curId 搜索开始节点
	 */
	@Transient 
	public static void searchList(List<Standard> list, List<Standard> sourcelist, String curId) {
		for(int i=0; i<sourcelist.size(); i++) {
			Standard e = sourcelist.get(i);
			if(e.getId() != null && e.getId().equals(curId)) {
				list.add(e);
				for(int j=0; j<sourcelist.size(); j++) {
					Standard child = sourcelist.get(j);
					if(child.getParent() != null && child.getParent().getId() != null && child.getParent().getId().equals(e.getId())) {
						searchList(list, sourcelist, child.getId());
					}
				}
			}
		}
	}
	
	/**
	 * 查找树的所有叶子节点
	 * @param sourcelist
	 */
	@Transient
	public static List<Standard> getLeafList(List<Standard> sourcelist) {
		List<Standard> list = new ArrayList<Standard>();
		for(int i=0; i<sourcelist.size(); i++) {
			Standard e = sourcelist.get(i);
			boolean flag = true;
			for(int j=0; j<sourcelist.size(); j++) {
				Standard child = sourcelist.get(j);
				
				if (child.getParent()!=null && child.getParent().getId()!=null
						&& child.getParent().getId().equals(e.getId())){
					flag = false;
					break;
				}
			}
			if(flag) {
				list.add(e);
			}
		}
		return list;
	}

	@Transient
	public boolean isRoot(){
		return isRoot(this.id);
	}
	
	@Transient
	public static boolean isRoot(String id){
		return id != null && id.equals("2");
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}