package com.surekam.modules.agro.taskitemcategory.entity;

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

/**
 * 作业项类别表(包括 施肥 投料等 )Entity
 * @author liwei
 * @version 2019-04-23
 */
@Entity
@Table(name = "t_agro_task_item_category")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TaskItemCategory extends XGXTEntity<TaskItemCategory> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String taskItemCategoryName;//作业类别名称
	private String sort;//排序

	public TaskItemCategory() {
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
	
	public String getTaskItemCategoryName() {
		return this.taskItemCategoryName;
	}
	public void setTaskItemCategoryName(String taskItemCategoryName) {
		this.taskItemCategoryName = taskItemCategoryName;
	}
	
	public String getSort() {
		return this.sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
}


