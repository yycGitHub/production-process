/**
 * 
 */
package com.surekam.modules.api.dto.resp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Title: TaskValueResp Description: 上传任务返回对象
 * 
 * @author tangjun
 * @date 2019年7月18日
 */
public class TaskValueResp implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String taskVal;

	private List<String> url = new ArrayList<String>();

	private boolean taskState;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskVal() {
		return taskVal;
	}

	public void setTaskVal(String taskVal) {
		this.taskVal = taskVal;
	}

	public boolean isTaskState() {
		return taskState;
	}

	public void setTaskState(boolean taskState) {
		this.taskState = taskState;
	}

	public List<String> getUrl() {
		return url;
	}

	public void setUrl(List<String> url) {
		this.url = url;
	}

}
