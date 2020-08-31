package com.surekam.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.surekam.modules.agro.addressbook.entity.ParameterClass;

import net.sf.json.JSONObject;

public class RestAPI {
	private static String APPID = "1400230157";
	private static String IDENTIFIER = "admin";
	private static String USERSIG = "eJxNj19PgzAUxb*K6euMacufhSU*sIlCZAPBTffUVFqa6koJ6xoW43eXEZbsPv7Oufee8wve0-KBVpU*NYaYc8vBAkBwP2LJeGNkLXk3QMqUbCaBtq1khBridOzGf2Q-ZJQGhlwIsQORN59E3rey44TWZjyHcODDy1x3pRjoOtquklg8J9vcy-Z9XVSJ0uveirjLM1vOqoPa86eXndO4X0uLglBG4TcUgkVBpm2SL8Vch6dCFR-xIXtjr6XWm*z8uVGrNJ1h93F6ZqS69ESejwPkI-*a0fLuKHUDFncAD9HRUGCM*PcPZt9YKQ__";
	

	public static String restUrl(String servicename, String command) {
		String url = "https://console.tim.qq.com/v4/";
		String int32 = getInt32();
		url+=servicename+"/"+command+"?sdkappid="+APPID+"&identifier="+IDENTIFIER+"&usersig="+USERSIG+"&random="+int32+"&contenttype=json";
		System.out.println(url);
		return url;
	}
	
	public static String getInt32(){
		Random rand = new Random();
		StringBuffer sb=new StringBuffer();
		for (int i=1;i<=32;i++){
		    int randNum = rand.nextInt(9)+1;
		    String num=randNum+"";
		    sb=sb.append(num);
		}
		return String.valueOf(sb);
	}
	
	public static void main(String[] args) {
		String url = "https://console.tim.qq.com/v4/";
		String int32 = getInt32();
		url+="im_open_login_svc/account_import?sdkappid="+APPID+"&identifier="+IDENTIFIER+"&usersig="+USERSIG+"&random="+int32+"&contenttype=json";
		System.out.println(url);
		Map<String, String> params = new HashMap<String, String>();
		params.put("Identifier", "test10");
		params.put("Nick", "test10");
		params.put("FaceUrl", "http://www.qq.com");
		JSONObject json = new JSONObject().fromObject(params);
		String post = Client.post(url, json, "UTF-8");
		JSONObject at_obj = JSONObject.fromObject(post);
		System.out.println(at_obj);
		
	}
	
	public static JSONObject objToJson(ParameterClass parameterClass){
		JSONObject json = new JSONObject();
		if(StringUtils.isNotBlank(parameterClass.getFrom_Account())){
			json.put("From_Account",parameterClass.getFrom_Account());
		}
		if(StringUtils.isNotBlank(parameterClass.getAddType())){
			json.put("AddType",parameterClass.getAddType());
		}
		if(StringUtils.isNotBlank(parameterClass.getForceAddFlags())){
			json.put("ForceAddFlags",parameterClass.getForceAddFlags());
		}
		if(parameterClass.getAddFriendItem()!=null && parameterClass.getAddFriendItem().size()>0){
			json.put("AddFriendItem",parameterClass.getAddFriendItem());
		}
		if(parameterClass.getProfileItem()!=null && parameterClass.getProfileItem().size()>0){
			json.put("ProfileItem",parameterClass.getProfileItem());
		}
		if(parameterClass.getTo_Account()!=null && parameterClass.getTo_Account().length>0){
			json.put("To_Account",parameterClass.getTo_Account());
		}
		if(parameterClass.getGroupName()!=null && parameterClass.getGroupName().length>0){
			json.put("GroupName",parameterClass.getGroupName());
		}
		if(parameterClass.getTagList()!=null && parameterClass.getTagList().length>0){
			json.put("TagList",parameterClass.getTagList());
		}
		if(parameterClass.getAccounts()!=null && parameterClass.getAccounts().length>0){
			json.put("Accounts",parameterClass.getAccounts());
		}
		if(StringUtils.isNotBlank(parameterClass.getDeleteType())){
			json.put("DeleteType",parameterClass.getDeleteType());
		}
		if(StringUtils.isNotBlank(parameterClass.getCheckType())){
			json.put("CheckType",parameterClass.getCheckType());
		}
		if(parameterClass.getStartIndex()!=null){
			json.put("StartIndex",parameterClass.getStartIndex());
		}
		if(parameterClass.getMaxLimited()!=null){
			json.put("MaxLimited",parameterClass.getMaxLimited());
		}
		if(parameterClass.getStandardSequence()!=null){
			json.put("StandardSequence",parameterClass.getStandardSequence());
		}
		if(parameterClass.getCustomSequence()!=null){
			json.put("CustomSequence",parameterClass.getCustomSequence());
		}
		if(parameterClass.getLastSequence()!=null){
			json.put("LastSequence",parameterClass.getLastSequence());
		}
		if(parameterClass.getLimit()!=null){
			json.put("Limit",parameterClass.getLimit());
		}
		if(parameterClass.getNext()!=null){
			json.put("Next",parameterClass.getNext());
		}
		if(parameterClass.getMaxMemberCount()!=null){
			json.put("MaxMemberCount",parameterClass.getMaxMemberCount());
		}
		if(parameterClass.getMaxMemberNum()!=null){
			json.put("MaxMemberNum",parameterClass.getMaxMemberNum());
		}
		if(parameterClass.getOffset()!=null){
			json.put("Offset",parameterClass.getOffset());
		}
		if(parameterClass.getShutUpTime()!=null){
			json.put("ShutUpTime",parameterClass.getShutUpTime());
		}
		if(parameterClass.getUnreadMsgNum()!=null){
			json.put("UnreadMsgNum",parameterClass.getUnreadMsgNum());
		}
		if(parameterClass.getC2CmsgNospeakingTime()!=null){
			json.put("C2CmsgNospeakingTime",parameterClass.getC2CmsgNospeakingTime());
		}
		if(parameterClass.getGroupmsgNospeakingTime()!=null){
			json.put("GroupmsgNospeakingTime",parameterClass.getGroupmsgNospeakingTime());
		}
		
		if(StringUtils.isNotBlank(parameterClass.getIdentifier())){
			json.put("Identifier",parameterClass.getIdentifier());
		}
		if(StringUtils.isNotBlank(parameterClass.getNick())){
			json.put("Nick",parameterClass.getNick());
		}
		if(StringUtils.isNotBlank(parameterClass.getFaceUrl())){
			json.put("FaceUrl",parameterClass.getFaceUrl());
		}
		if(StringUtils.isNotBlank(parameterClass.getGroupType())){
			json.put("GroupType",parameterClass.getGroupType());
		}
		if(StringUtils.isNotBlank(parameterClass.getOwner_Account())){
			json.put("Owner_Account",parameterClass.getOwner_Account());
		}
		if(StringUtils.isNotBlank(parameterClass.getType())){
			json.put("Type",parameterClass.getType());
		}
		if(StringUtils.isNotBlank(parameterClass.getName())){
			json.put("Name",parameterClass.getName());
		}
		if(StringUtils.isNotBlank(parameterClass.getIntroduction())){
			json.put("Introduction",parameterClass.getIntroduction());
		}
		if(StringUtils.isNotBlank(parameterClass.getNotification())){
			json.put("Notification",parameterClass.getNotification());
		}
		if(StringUtils.isNotBlank(parameterClass.getGroupId())){
			json.put("GroupId",parameterClass.getGroupId());
		}
		if(StringUtils.isNotBlank(parameterClass.getShutUpAllMember())){
			json.put("ShutUpAllMember",parameterClass.getShutUpAllMember());
		}
		if(StringUtils.isNotBlank(parameterClass.getSilence())){
			json.put("Silence",parameterClass.getSilence());
		}
		if(StringUtils.isNotBlank(parameterClass.getReason())){
			json.put("Reason",parameterClass.getReason());
		}
		if(StringUtils.isNotBlank(parameterClass.getRole())){
			json.put("Role",parameterClass.getRole());
		}
		if(StringUtils.isNotBlank(parameterClass.getMember_Account())){
			json.put("Member_Account",parameterClass.getMember_Account());
		}
		if(StringUtils.isNotBlank(parameterClass.getMsgFlag())){
			json.put("MsgFlag",parameterClass.getMsgFlag());
		}
		if(StringUtils.isNotBlank(parameterClass.getNameCard())){
			json.put("NameCard",parameterClass.getNameCard());
		}
		if(StringUtils.isNotBlank(parameterClass.getNewOwner_Account())){
			json.put("NewOwner_Account",parameterClass.getNewOwner_Account());
		}
		if(StringUtils.isNotBlank(parameterClass.getMember_Account())){
			json.put("Member_Account",parameterClass.getMember_Account());
		}
		if(StringUtils.isNotBlank(parameterClass.getMsgFlag())){
			json.put("MsgFlag",parameterClass.getMsgFlag());
		}
		if(StringUtils.isNotBlank(parameterClass.getNameCard())){
			json.put("NameCard",parameterClass.getNameCard());
		}
		if(StringUtils.isNotBlank(parameterClass.getNewOwner_Account())){
			json.put("NewOwner_Account",parameterClass.getNewOwner_Account());
		}
		if(parameterClass.getCreateTime()!=null){
			json.put("CreateTime",parameterClass.getCreateTime());
		}
		
		if(StringUtils.isNotBlank(parameterClass.getSet_Account())){
			json.put("Set_Account",parameterClass.getSet_Account());
		}
		if(StringUtils.isNotBlank(parameterClass.getSender_Account())){
			json.put("Sender_Account",parameterClass.getSender_Account());
		}
		if(StringUtils.isNotBlank(parameterClass.getGet_Account())){
			json.put("Get_Account",parameterClass.getGet_Account());
		}
		
		if(parameterClass.getTagList()!=null && parameterClass.getUser_Account().length>0){
			json.put("User_Account",parameterClass.getUser_Account());
		}
		if(parameterClass.getAccounts()!=null && parameterClass.getGroupIdList().length>0){
			json.put("GroupIdList",parameterClass.getGroupIdList());
		}
		if(parameterClass.getTagList()!=null && parameterClass.getMemberToDel_Account().length>0){
			json.put("MemberToDel_Account",parameterClass.getMemberToDel_Account());
		}
		if(parameterClass.getMemberList()!=null && parameterClass.getMemberList().size()>0){
			json.put("MemberList",parameterClass.getAccounts());
		}
		return json;
	}
	
}
