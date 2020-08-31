package com.surekam.modules.agro.standardtaskitemsargsvalue.entity;

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
import java.util.ArrayList;
import java.util.List;

/**
 * 标准作业执行记录表Entity
 * @author liwei
 * @version 2019-04-23
 */
@Entity
@Table(name = "t_agro_standard_task_items_args_value")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StandardTaskItemsArgsValue extends XGXTEntity<StandardTaskItemsArgsValue> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String taskListId;//作业记录主表ID
	private String taskItemArgsId;//作业详细参数ID
	private String argsName;//参数名称
	private String taskItemArgsValue;//作业执行结果
	private String argsUnit;//参数单位
	private String argsType;//参数类型
	private String argsValueDescription;//执行描述
	private String sort;//排序
	private List<String> imageSrc = new ArrayList<String>(); 
	

	public StandardTaskItemsArgsValue() {
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
	
	public String getTaskListId() {
		return this.taskListId;
	}
	public void setTaskListId(String taskListId) {
		this.taskListId = taskListId;
	}
	
	public String getTaskItemArgsId() {
		return this.taskItemArgsId;
	}
	public void setTaskItemArgsId(String taskItemArgsId) {
		this.taskItemArgsId = taskItemArgsId;
	}
	
	public String getArgsName() {
		return this.argsName;
	}
	public void setArgsName(String argsName) {
		this.argsName = argsName;
	}
	
	public String getTaskItemArgsValue() {
		return this.taskItemArgsValue;
	}
	public void setTaskItemArgsValue(String taskItemArgsValue) {
		this.taskItemArgsValue = taskItemArgsValue;
	}
	
	public String getArgsUnit() {
		return this.argsUnit;
	}
	public void setArgsUnit(String argsUnit) {
		this.argsUnit = argsUnit;
	}
	
	public String getArgsType() {
		return this.argsType;
	}
	public void setArgsType(String argsType) {
		this.argsType = argsType;
	}
	
	public String getArgsValueDescription() {
		return this.argsValueDescription;
	}
	public void setArgsValueDescription(String argsValueDescription) {
		this.argsValueDescription = argsValueDescription;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Transient
	public List<String> getImageSrc() {
		return imageSrc;
	}

	public void setImageSrc(List<String> imageSrc) {
		this.imageSrc = imageSrc;
	}
	
}


