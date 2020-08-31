/**
 * 
 */
package com.surekam.modules.api.dto.req;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Title: UploadSettingReq Description: 保存上传设置
 * 
 * @author tangjun
 * @date 2019年7月18日
 */
public class UploadSettingReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String officeId;

	private String standardId;

	private List<String> uploadSettingIdList = new ArrayList<String>();

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getStandardId() {
		return standardId;
	}

	public void setStandardId(String standardId) {
		this.standardId = standardId;
	}

	public List<String> getUploadSettingIdList() {
		return uploadSettingIdList;
	}

	public void setUploadSettingIdList(List<String> uploadSettingIdList) {
		this.uploadSettingIdList = uploadSettingIdList;
	}

}
