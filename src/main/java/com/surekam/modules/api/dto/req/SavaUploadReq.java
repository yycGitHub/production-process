/**
 * 
 */
package com.surekam.modules.api.dto.req;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Title: SavaUploadReq Description: 保存上传
 * 
 * @author tangjun
 * @date 2019年7月19日
 */
public class SavaUploadReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 批次号
	 */
	private String batchId;

	/**
	 * 基本信息
	 */
	private List<QuickReq> quickList = new ArrayList<QuickReq>();

	/**
	 * 生产过程
	 */
	private List<String> taskList = new ArrayList<String>();

	/**
	 * 认证信息
	 */
	private List<String> rzList = new ArrayList<String>();

	/**
	 * 质检信息
	 */
	private List<String> zjList = new ArrayList<String>();

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public List<String> getRzList() {
		return rzList;
	}

	public void setRzList(List<String> rzList) {
		this.rzList = rzList;
	}

	public List<String> getZjList() {
		return zjList;
	}

	public void setZjList(List<String> zjList) {
		this.zjList = zjList;
	}

	public List<String> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<String> taskList) {
		this.taskList = taskList;
	}

	public List<QuickReq> getQuickList() {
		return quickList;
	}

	public void setQuickList(List<QuickReq> quickList) {
		this.quickList = quickList;
	}

}
