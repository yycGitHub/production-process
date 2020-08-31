package com.surekam.modules.agro.addressbook.entity;

import java.util.List;

public class ParameterClass {
	private String From_Account;//需要为该 Identifier 添加好友
	private List<FriendItem> AddFriendItem;//好友结构体对象
	private String AddType;//专家指导记录id
	private String ForceAddFlags;//农户id
	private List<Profile> ProfileItem;//待设置的用户的资料对象数组，数组中每一个对象都包含了 Tag 和 Value
	private String[] To_Account;//需要拉取这些 Identifier 的资料
	private String[] GroupName;//新增分组列表
	private String[] TagList;//需要拉取这些 Identifier 的资料
	private String[] Accounts;//用户名，单个用户名长度不超过32字节，单次最多导入100个用户名
	private String DeleteType;//删除模式，详情可参见 删除好友
	private String CheckType;//校验模式，详情可参见 校验好友
	private Integer StartIndex=null;//分页的起始位置
	private Integer MaxLimited=null;//每页最多拉取的黑名单数
	private Integer StandardSequence=null;//上次拉好友数据时返回的 StandardSequence
	private Integer CustomSequence=null;//上次拉好友数据时返回的 CustomSequence
	private Integer LastSequence=null;//上一次拉黑名单时后台返回给客户端的 Seq，初次拉取时为0
	private String Identifier;//用户名，长度不超过32字节
	private String Nick;//用户昵称
	private String FaceUrl;//用户头像 URL
	private Integer Limit=null;//本次获取的群组 ID 数量的上限，不得超过 10000。如果不填，默认为最大值 10000
	private Integer Next=null;//群太多时分页拉取标志，第一次填0，以后填上一次返回的值，返回的 Next 为0代表拉完了
	private String GroupType;//可以通过 GroupType 进行过滤
	private String Owner_Account;//群主的 UserId（选填）
	private String Type;//// 群组类型：Private/Public/ChatRoom/AVChatRoom/BChatRoom（必填）
	private String Name; // 群名称（必填）
	private String Introduction;//群简介（选填）
	private String Notification;//群公告（选填）
	private Integer MaxMemberCount=null;//最大群成员数量（选填）
	private Integer MaxMemberNum=null;// 最大群成员数量（选填）
	private String ApplyJoinOption;// 申请加群处理方式（选填）NeedPermission（需要验证），DisableApply（禁止加群），不填默认为 NeedPermission（需要验证）
	private List<Member> MemberList;// 初始群成员列表，最多500个（选填）
	private String[] GroupIdList; // 群组列表（必填）
	private String GroupId; //群组 ID（必填）
	private Integer Offset;// 从第多少个成员开始获取资料
	private String ShutUpAllMember;// 设置全员禁言（选填）:"On"开启，"Off"关闭
	private String Silence; // 是否静默加人（选填）0：非静默加人；1：静默加人。不填该字段默认为0
	private String[] MemberToDel_Account; // 要删除的群成员列表，最多500个
	private String Reason;// 踢出用户原因（选填）
	private String Role;// 成员身份，Admin/Member 分别为设置/取消管理员
	private String Member_Account;// 要操作的群成员（必填）
	private String MsgFlag;//AcceptAndNotify、Discard或者AcceptNotNotify，消息屏蔽类型
	private String NameCard;// 群名片（选填）
	private Integer ShutUpTime=null;//需禁言时间，单位为秒，0表示取消禁言
	private String[] User_Account;//表示需要查询的用户帐号，最多支持500个帐号
	private String NewOwner_Account;//新群主 ID（必填）
	private Integer CreateTime=null;//群组的创建时间
	private Integer UnreadMsgNum=null; // 该成员的未读消息数
	private String Sender_Account;//被删除消息的发送者 ID
	private String Set_Account;//设置禁言配置的帐号
	private Integer C2CmsgNospeakingTime=null;//单聊消息禁言时间，单位为秒，非负整数
	private Integer GroupmsgNospeakingTime=null;//群组消息禁言时间，单位为秒，非负整数
	private String Get_Account;//查询禁言信息的帐号
	
	private String servicename;//内部服务名
	private String command;//命令字
	
	public String getFrom_Account() {
		return From_Account;
	}
	public void setFrom_Account(String from_Account) {
		From_Account = from_Account;
	}
	public List<FriendItem> getAddFriendItem() {
		return AddFriendItem;
	}
	public void setAddFriendItem(List<FriendItem> addFriendItem) {
		AddFriendItem = addFriendItem;
	}
	public String getAddType() {
		return AddType;
	}
	public void setAddType(String addType) {
		AddType = addType;
	}
	public String getForceAddFlags() {
		return ForceAddFlags;
	}
	public void setForceAddFlags(String forceAddFlags) {
		ForceAddFlags = forceAddFlags;
	}
	public List<Profile> getProfileItem() {
		return ProfileItem;
	}
	public void setProfileItem(List<Profile> profileItem) {
		ProfileItem = profileItem;
	}
	public String[] getTo_Account() {
		return To_Account;
	}
	public void setTo_Account(String[] to_Account) {
		To_Account = to_Account;
	}
	public String[] getGroupName() {
		return GroupName;
	}
	public void setGroupName(String[] groupName) {
		GroupName = groupName;
	}
	public String[] getTagList() {
		return TagList;
	}
	public void setTagList(String[] tagList) {
		TagList = tagList;
	}
	public String[] getAccounts() {
		return Accounts;
	}
	public void setAccounts(String[] accounts) {
		Accounts = accounts;
	}
	public String getDeleteType() {
		return DeleteType;
	}
	public void setDeleteType(String deleteType) {
		DeleteType = deleteType;
	}
	public String getCheckType() {
		return CheckType;
	}
	public void setCheckType(String checkType) {
		CheckType = checkType;
	}
	public Integer getStartIndex() {
		return StartIndex;
	}
	public void setStartIndex(Integer startIndex) {
		StartIndex = startIndex;
	}
	public Integer getMaxLimited() {
		return MaxLimited;
	}
	public void setMaxLimited(Integer maxLimited) {
		MaxLimited = maxLimited;
	}
	public Integer getStandardSequence() {
		return StandardSequence;
	}
	public void setStandardSequence(Integer standardSequence) {
		StandardSequence = standardSequence;
	}
	public Integer getCustomSequence() {
		return CustomSequence;
	}
	public void setCustomSequence(Integer customSequence) {
		CustomSequence = customSequence;
	}
	public Integer getLastSequence() {
		return LastSequence;
	}
	public void setLastSequence(Integer lastSequence) {
		LastSequence = lastSequence;
	}
	public String getIdentifier() {
		return Identifier;
	}
	public void setIdentifier(String identifier) {
		Identifier = identifier;
	}
	public String getNick() {
		return Nick;
	}
	public void setNick(String nick) {
		Nick = nick;
	}
	public String getFaceUrl() {
		return FaceUrl;
	}
	public void setFaceUrl(String faceUrl) {
		FaceUrl = faceUrl;
	}
	public Integer getLimit() {
		return Limit;
	}
	public void setLimit(Integer limit) {
		Limit = limit;
	}
	public Integer getNext() {
		return Next;
	}
	public void setNext(Integer next) {
		Next = next;
	}
	public String getGroupType() {
		return GroupType;
	}
	public void setGroupType(String groupType) {
		GroupType = groupType;
	}
	public String getOwner_Account() {
		return Owner_Account;
	}
	public void setOwner_Account(String owner_Account) {
		Owner_Account = owner_Account;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getIntroduction() {
		return Introduction;
	}
	public void setIntroduction(String introduction) {
		Introduction = introduction;
	}
	public String getNotification() {
		return Notification;
	}
	public void setNotification(String notification) {
		Notification = notification;
	}
	public Integer getMaxMemberCount() {
		return MaxMemberCount;
	}
	public void setMaxMemberCount(Integer maxMemberCount) {
		MaxMemberCount = maxMemberCount;
	}
	public Integer getMaxMemberNum() {
		return MaxMemberNum;
	}
	public void setMaxMemberNum(Integer maxMemberNum) {
		MaxMemberNum = maxMemberNum;
	}
	public String getApplyJoinOption() {
		return ApplyJoinOption;
	}
	public void setApplyJoinOption(String applyJoinOption) {
		ApplyJoinOption = applyJoinOption;
	}
	public List<Member> getMemberList() {
		return MemberList;
	}
	public void setMemberList(List<Member> memberList) {
		MemberList = memberList;
	}
	public String[] getGroupIdList() {
		return GroupIdList;
	}
	public void setGroupIdList(String[] groupIdList) {
		GroupIdList = groupIdList;
	}
	public String getGroupId() {
		return GroupId;
	}
	public void setGroupId(String groupId) {
		GroupId = groupId;
	}
	public Integer getOffset() {
		return Offset;
	}
	public void setOffset(Integer offset) {
		Offset = offset;
	}
	public String getShutUpAllMember() {
		return ShutUpAllMember;
	}
	public void setShutUpAllMember(String shutUpAllMember) {
		ShutUpAllMember = shutUpAllMember;
	}
	public String getSilence() {
		return Silence;
	}
	public void setSilence(String silence) {
		Silence = silence;
	}
	public String[] getMemberToDel_Account() {
		return MemberToDel_Account;
	}
	public void setMemberToDel_Account(String[] memberToDel_Account) {
		MemberToDel_Account = memberToDel_Account;
	}
	public String getReason() {
		return Reason;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}
	public String getMember_Account() {
		return Member_Account;
	}
	public void setMember_Account(String member_Account) {
		Member_Account = member_Account;
	}
	public String getMsgFlag() {
		return MsgFlag;
	}
	public void setMsgFlag(String msgFlag) {
		MsgFlag = msgFlag;
	}
	public String getNameCard() {
		return NameCard;
	}
	public void setNameCard(String nameCard) {
		NameCard = nameCard;
	}
	public Integer getShutUpTime() {
		return ShutUpTime;
	}
	public void setShutUpTime(Integer shutUpTime) {
		ShutUpTime = shutUpTime;
	}
	public String[] getUser_Account() {
		return User_Account;
	}
	public void setUser_Account(String[] user_Account) {
		User_Account = user_Account;
	}
	public String getNewOwner_Account() {
		return NewOwner_Account;
	}
	public void setNewOwner_Account(String newOwner_Account) {
		NewOwner_Account = newOwner_Account;
	}
	public Integer getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Integer createTime) {
		CreateTime = createTime;
	}
	public Integer getUnreadMsgNum() {
		return UnreadMsgNum;
	}
	public void setUnreadMsgNum(Integer unreadMsgNum) {
		UnreadMsgNum = unreadMsgNum;
	}
	public String getSender_Account() {
		return Sender_Account;
	}
	public void setSender_Account(String sender_Account) {
		Sender_Account = sender_Account;
	}
	public String getSet_Account() {
		return Set_Account;
	}
	public void setSet_Account(String set_Account) {
		Set_Account = set_Account;
	}
	public Integer getC2CmsgNospeakingTime() {
		return C2CmsgNospeakingTime;
	}
	public void setC2CmsgNospeakingTime(Integer c2CmsgNospeakingTime) {
		C2CmsgNospeakingTime = c2CmsgNospeakingTime;
	}
	public Integer getGroupmsgNospeakingTime() {
		return GroupmsgNospeakingTime;
	}
	public void setGroupmsgNospeakingTime(Integer groupmsgNospeakingTime) {
		GroupmsgNospeakingTime = groupmsgNospeakingTime;
	}
	public String getGet_Account() {
		return Get_Account;
	}
	public void setGet_Account(String get_Account) {
		Get_Account = get_Account;
	}
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
}
