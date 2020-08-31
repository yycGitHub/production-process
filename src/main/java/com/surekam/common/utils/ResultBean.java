package com.surekam.common.utils;

import java.io.Serializable;

/**
 * @author wangyuewen
 *
 * @param <T>
 */
public class ResultBean<T> implements Serializable{
	/** 序列化参数 */ 
	private static final long serialVersionUID = 1L;
	/** 错误码 */
	private Integer code;
	/** 提示信息 */
	private String message;
	/** 具体内容 */
	private T bodyData;
	/** 流程ID */
	private String procInsId;
	/** 批量处理的流程ID */
	private String procInsIds;
	
	public ResultBean() {	}

	public ResultBean(int code) {
		this.code = code;
	}
	public ResultBean(String message) {
		this.message = message;
	}
	public ResultBean(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String toString() {
		return "ResultBean [code=" + code + ", message=" + message + ", bodyData=" + bodyData + "]";
	}

	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getBodyData() {
		return bodyData;
	}
	public void setBodyData(T bodyData) {
		this.bodyData = bodyData;
		
	}

	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public String getProcInsIds() {
		return procInsIds;
	}

	public void setProcInsIds(String procInsIds) {
		this.procInsIds = procInsIds;
	}
	
	
}
