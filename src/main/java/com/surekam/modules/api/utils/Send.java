/**
 * 
 */
package com.surekam.modules.api.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.surekam.common.config.Global;
import com.surekam.common.utils.Client;

/**
 * Title: test Description:
 * 
 * @author tangjun
 * @date 2019年5月30日
 */
public class Send {
	
	public static String MAILBOX_HOST = Global.getConfig("mailboxHost");
	public static String MAILBOX_ACCOUNT = Global.getConfig("mailboxAccount");
	public static String MAILBOX_SECRET_KEY = Global.getConfig("mailboxSecretKey");
	

	/***
	 * 发送短信验证码
	 * 
	 * @param phone
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String sendMessage(String phone, String code ,String sign) {
		String url = "http://webapi.sureserve.cn/sms/send.json";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("SMSMobiles", phone));
		params.add(new BasicNameValuePair("SMSContent", code));
		params.add(new BasicNameValuePair("SMSSign", sign));
		String result = Client.post(url, params, url);
		return result;
	}

	/***
	 * 发送邮件
	 * 
	 * @param phone
	 * @return
	 */
	public static void sendEmail(String someone, String subject, String content) throws MessagingException {
		Properties props = new Properties();
		props.setProperty("mail.host", MAILBOX_HOST);
		props.setProperty("mail.smtp.auth", "true");
		Authenticator authenticator = new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(MAILBOX_ACCOUNT, MAILBOX_SECRET_KEY);
			}
		};
		Session session = Session.getDefaultInstance(props, authenticator);
		session.setDebug(true);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(MAILBOX_ACCOUNT));
		message.setRecipients(RecipientType.TO, InternetAddress.parse(someone));
		// message.setRecipients(RecipientType.TO,InternetAddress.parse("测试的接收的邮件多个以逗号隔开"));
		message.setSubject(subject);
		message.setContent(content, "text/html;charset=UTF-8");
		Transport.send(message);
	}

}
