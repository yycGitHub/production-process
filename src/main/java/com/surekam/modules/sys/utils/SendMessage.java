package com.surekam.modules.sys.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.surekam.common.config.Global;
import com.surekam.common.utils.Client;

/**
 * 短信接口
 * @author wangyuewen
 *
 */
public class SendMessage {

	public static String SMSSign = Global.getConfig("SMSSign");
	
	/***
	 * 发送短信验证码
	 * @param phone
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String sendValidationMessage(String phone, String validationCode) {
		String url = "http://webapi.sureserve.cn/sms/send.json";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("SMSContent", "验证码：" + validationCode+"，请在10分钟内完成验证。"));
		params.add(new BasicNameValuePair("SMSMobiles", phone));
		params.add(new BasicNameValuePair("SMSSign", SMSSign));
		String result = Client.post(url, params, url);
		return result;
	}
	
	public String getValidationCode() {
		String code = "";
		Random random = new Random();
		for(int i=0; i<6; i++) {
			code += random.nextInt(9);
		}
		return code;
	}
}
