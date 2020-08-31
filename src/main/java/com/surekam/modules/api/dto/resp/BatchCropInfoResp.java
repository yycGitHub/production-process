package com.surekam.modules.api.dto.resp;

import java.io.Serializable;

/**
 * Title: BatchCropInfoResp Description: 作物标准返回对象
 * 
 * @author tangjun
 * @date 2019年4月30日
 */
public class BatchCropInfoResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 作物ID
	 */
	private String id;

	/**
	 * 作物名称
	 */
	private String cropName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCropName() {
		return cropName;
	}

	public void setCropName(String cropName) {
		this.cropName = cropName;
	}

}
