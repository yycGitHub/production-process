package com.surekam.modules.agro.addressbook.entity;

import java.util.List;

public class FriendItem {
	private String To_Account;//好友的 Identifier
	private String Remark;//From_Account 对 To_Account 的好友备注
	private Integer RemarkTime;
	private String[] GroupName;//From_Account 对 To_Account 的分组信息
	private String AddSource;//加好友来源字段
	private String AddWording;//From_Account 和 To_Account 形成好友关系时的附言信息，详情可参见 标配好友字段
	private List<Profile> CustomItem;//From_Account 对 To_Account 的自定义表示对象信息数组，每一个对象都包含了 Tag 和 Value
	public String getTo_Account() {
		return To_Account;
	}
	public void setTo_Account(String to_Account) {
		To_Account = to_Account;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String[] getGroupName() {
		return GroupName;
	}
	public void setGroupName(String[] groupName) {
		GroupName = groupName;
	}
	public String getAddSource() {
		return AddSource;
	}
	public void setAddSource(String addSource) {
		AddSource = addSource;
	}
	public String getAddWording() {
		return AddWording;
	}
	public void setAddWording(String addWording) {
		AddWording = addWording;
	}
	public Integer getRemarkTime() {
		return RemarkTime;
	}
	public void setRemarkTime(Integer remarkTime) {
		RemarkTime = remarkTime;
	}
	public List<Profile> getCustomItem() {
		return CustomItem;
	}
	public void setCustomItem(List<Profile> customItem) {
		CustomItem = customItem;
	}

}
