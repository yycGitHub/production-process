package com.surekam.modules.agro.addressbook.entity;

public class Member {
	private String Member_Account;//成员（必填）
	private String Role; // 赋予该成员的身份，目前备选项只有 Admin（选填）
	private Integer JoinTime;//待导入群成员的入群时间
	private Integer UnreadMsgNum;//该成员的未读消息数（选填）
	public String getMember_Account() {
		return Member_Account;
	}
	public void setMember_Account(String member_Account) {
		Member_Account = member_Account;
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}
	public Integer getJoinTime() {
		return JoinTime;
	}
	public void setJoinTime(Integer joinTime) {
		JoinTime = joinTime;
	}
	public Integer getUnreadMsgNum() {
		return UnreadMsgNum;
	}
	public void setUnreadMsgNum(Integer unreadMsgNum) {
		UnreadMsgNum = unreadMsgNum;
	}
	
}
