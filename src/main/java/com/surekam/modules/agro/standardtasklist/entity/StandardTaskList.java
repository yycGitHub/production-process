package com.surekam.modules.agro.standardtasklist.entity;

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
import com.surekam.modules.agro.standardtaskitemsargsvalue.entity.StandardTaskItemsArgsValue;
import java.lang.String;
import java.util.List;

/**
 * 作业记录主数据表Entity
 * @author liwei
 * @version 2019-04-23
 */
@Entity
@Table(name = "t_agro_standard_task_list")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StandardTaskList extends XGXTEntity<StandardTaskList> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String areaId;//区域主键
	private String productionBatch;//批次主键
	private String taskItemsId;//作业项ID
	private List<StandardTaskItemsArgsValue> standardTaskItemsArgsValueList;

	public StandardTaskList() {
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
	
	public String getAreaId() {
		return this.areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
	public String getProductionBatch() {
		return this.productionBatch;
	}
	public void setProductionBatch(String productionBatch) {
		this.productionBatch = productionBatch;
	}
	
	public String getTaskItemsId() {
		return this.taskItemsId;
	}
	public void setTaskItemsId(String taskItemsId) {
		this.taskItemsId = taskItemsId;
	}

	@Transient
	public List<StandardTaskItemsArgsValue> getStandardTaskItemsArgsValueList() {
		return standardTaskItemsArgsValueList;
	}

	public void setStandardTaskItemsArgsValueList(List<StandardTaskItemsArgsValue> standardTaskItemsArgsValueList) {
		this.standardTaskItemsArgsValueList = standardTaskItemsArgsValueList;
	}
	
}


