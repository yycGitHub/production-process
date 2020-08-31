package com.surekam.modules.agro.productbatchtask.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

public class ResolveAndTaskValueVo {
	private String id;// 主键
	private String taskResolveId;
	private String taskListId;// 作业记录主表ID
	private String taskItemArgsId;// 作业详细参数ID
	private String argsName;// 参数名称
	private String taskItemArgsValue;// 作业执行结果
	private String argsUnit;// 参数单位
	private String argsType;// 参数类型
	private String argsValueDescription;// 执行描述
	private String sort;// 排序
	private List<String> imageSrc = new ArrayList<String>();

	public ResolveAndTaskValueVo() {
		super();
	}

	public ResolveAndTaskValueVo(String id, String taskResolveId, String taskListId, String taskItemArgsId,
			String argsName, String taskItemArgsValue, String argsUnit, String argsType, String argsValueDescription,
			String sort) {
		super();
		this.id = id;
		this.taskResolveId = taskResolveId;
		this.taskListId = taskListId;
		this.taskItemArgsId = taskItemArgsId;
		this.argsName = argsName;
		this.taskItemArgsValue = taskItemArgsValue;
		this.argsUnit = argsUnit;
		this.argsType = argsType;
		this.argsValueDescription = argsValueDescription;
		this.sort = sort;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskResolveId() {
		return taskResolveId;
	}

	public void setTaskResolveId(String taskResolveId) {
		this.taskResolveId = taskResolveId;
	}

	public String getTaskListId() {
		return taskListId;
	}

	public void setTaskListId(String taskListId) {
		this.taskListId = taskListId;
	}

	public String getTaskItemArgsId() {
		return taskItemArgsId;
	}

	public void setTaskItemArgsId(String taskItemArgsId) {
		this.taskItemArgsId = taskItemArgsId;
	}

	public String getArgsName() {
		return argsName;
	}

	public void setArgsName(String argsName) {
		this.argsName = argsName;
	}

	public String getTaskItemArgsValue() {
		return taskItemArgsValue;
	}

	public void setTaskItemArgsValue(String taskItemArgsValue) {
		this.taskItemArgsValue = taskItemArgsValue;
	}

	public String getArgsUnit() {
		return argsUnit;
	}

	public void setArgsUnit(String argsUnit) {
		this.argsUnit = argsUnit;
	}

	public String getArgsType() {
		return argsType;
	}

	public void setArgsType(String argsType) {
		this.argsType = argsType;
	}

	public String getArgsValueDescription() {
		return argsValueDescription;
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

	public List<String> getImageSrc() {
		if (StringUtils.isNotBlank(this.taskItemArgsValue)) {
			String[] imgs = this.taskItemArgsValue.split(",");
			if (imgs != null) {
				for (int i = 0; i < imgs.length; i++) {
					this.imageSrc.add(imgs[i]);
				}
			}
			return this.imageSrc;
		}
		return null;
	}

	public void setImageSrc(List<String> imageSrc) {
		this.imageSrc = imageSrc;
	}

}
