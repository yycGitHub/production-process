package com.surekam.modules.agro.productbatchtask.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.surekam.common.utils.DateUtils;

public class DataUploadResolveAndTaskVo {
	private String TaskId;
	private String TaskResoleId;
	private String standardItemName;
	private Date dispatchTime;
	private Date finishTime;
	private String StandardItemId;
	private String nonexecutionReason;
	private boolean taskState;

	private List<ResolveAndTaskValueVo> taskValueList = new ArrayList<ResolveAndTaskValueVo>();
	private List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();

	public DataUploadResolveAndTaskVo() {
		super();
	}

	public DataUploadResolveAndTaskVo(String taskId, String taskResoleId, String standardItemName, String dispatchTime,
			String finishTime, String standardItemId, String nonexecutionReason) {
		super();
		TaskId = taskId;
		TaskResoleId = taskResoleId;
		this.standardItemName = standardItemName;
		this.dispatchTime = DateUtils.parseDate(dispatchTime);
		this.finishTime = DateUtils.parseDate(finishTime);
		StandardItemId = standardItemId;
		this.nonexecutionReason = nonexecutionReason;
	}
	public DataUploadResolveAndTaskVo(String taskId, String taskResoleId, String standardItemName, String dispatchTime,
			String finishTime, String standardItemId) {
		super();
		TaskId = taskId;
		TaskResoleId = taskResoleId;
		this.standardItemName = standardItemName;
		this.dispatchTime = DateUtils.parseDate(dispatchTime);
		this.finishTime = DateUtils.parseDate(finishTime);
		StandardItemId = standardItemId;
	}

	public String getTaskId() {
		return TaskId;
	}

	public void setTaskId(String taskId) {
		TaskId = taskId;
	}

	public String getTaskResoleId() {
		return TaskResoleId;
	}

	public void setTaskResoleId(String taskResoleId) {
		TaskResoleId = taskResoleId;
	}

	public String getStandardItemName() {
		return standardItemName;
	}

	public void setStandardItemName(String standardItemName) {
		this.standardItemName = standardItemName;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	@JsonFormat(timezone = "GMT+8")
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public List<ResolveAndTaskValueVo> getTaskValueList() {
		return taskValueList;
	}

	public void setTaskValueList(List<ResolveAndTaskValueVo> taskValueList) {
		this.taskValueList = taskValueList;
	}

	@Transient
	public List<Map<Object, Object>> getList() {
		return list;
	}

	public void setList(List<Map<Object, Object>> list) {
		this.list = list;
	}

	public String getStandardItemId() {
		return StandardItemId;
	}

	public void setStandardItemId(String standardItemId) {
		StandardItemId = standardItemId;
	}

	public String getNonexecutionReason() {
		return nonexecutionReason;
	}

	public void setNonexecutionReason(String nonexecutionReason) {
		this.nonexecutionReason = nonexecutionReason;
	}

	public boolean isTaskState() {
		return taskState;
	}

	public void setTaskState(boolean taskState) {
		this.taskState = taskState;
	}

}
