/**
 * 
 */
package com.surekam.modules.api.dto.uploadData.req;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Title: TraceModelListReq Description: 产品模型列表
 * 
 * @author tangjun
 * @date 2019年7月16日
 */
public class TraceModelListReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 模块名称
	 */
	private String modelName;
	/**
	 * 模块显示类型，1-普通模块，2-时间轴
	 */
	private String modelShowType;
	/**
	 * 产品新列表
	 */
	private List<Map<Object, Object>> tracePropertyNewList;

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelShowType() {
		return modelShowType;
	}

	public void setModelShowType(String modelShowType) {
		this.modelShowType = modelShowType;
	}

	public List<Map<Object, Object>> getTracePropertyNewList() {
		return tracePropertyNewList;
	}

	public void setTracePropertyNewList(List<Map<Object, Object>> tracePropertyNewList) {
		this.tracePropertyNewList = tracePropertyNewList;
	}

}
