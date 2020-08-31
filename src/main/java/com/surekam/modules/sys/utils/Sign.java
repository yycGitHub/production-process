package com.surekam.modules.sys.utils;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.utils.Client;

/**
 * 
 * @author wangyuewen 微信接口服务类 2015年9月16日 下午4:03:27
 */
public class Sign {

	public static String appid = Global.getConfig("weixin.appid");
	
	private String appsecret = Global.getConfig("weixin.appsecret");

	private static String access_token = "";
	private static String jsapi_ticket = "";
	private static long a_expire_time = 0;
	private static long j_expire_time = 0;

	/**
	 * 获取access_token
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getAccessToken() {
		String access_token_a = "";
		if (a_expire_time < System.currentTimeMillis()) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// 如果是企业号获取
			//String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
			//params.add(new BasicNameValuePair("grant_type", "client_credential"));
			//params.add(new BasicNameValuePair("corpid", appid));
			//params.add(new BasicNameValuePair("corpsecret", appsecret));
			//String result = Client.get(url, params, "https://qyapi.weixin.qq.com/cgi-bin/gettoken?");
			
			//公众号获取
			String url = "https://api.weixin.qq.com/cgi-bin/token";
			params.add(new BasicNameValuePair("grant_type", "client_credential"));
			params.add(new BasicNameValuePair("appid", appid));
			params.add(new BasicNameValuePair("secret", appsecret));
			String result = Client.get(url, params, "https://api.weixin.qq.com/cgi-bin/token?");
			
			JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
			Map<String, String> map = new HashMap<String, String>();
			map = (Map<String, String>) jsonMapper.fromJson(result, Object.class);
			access_token_a = map.get("access_token");
			if (access_token_a != null && !"".equals(access_token_a)) {
				a_expire_time = System.currentTimeMillis() + 7000;
				access_token = access_token_a;
			}
		} else {
			access_token_a = access_token;
		}
		return access_token_a;
	}

	/**
	 * 获取openid
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getOpenId(String code) {
		String accessToken = getAccessToken();
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", accessToken));
		params.add(new BasicNameValuePair("code", code));
		String result = Client.get(url, params, "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?");
		JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
		Map<String, String> map = new HashMap<String, String>();
		map = (Map<String, String>) jsonMapper.fromJson(result, Object.class);
		String openId = map.get("UserId");
		if(openId == null) {
			openId = map.get("OpenId");
		}
		return openId;
	}
	
	/**
	 * 获取网页授权accessToken，如果是base模式的话，还包含有openid
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getWebUserToken(String code){
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", appid));
		params.add(new BasicNameValuePair("secret", appsecret));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		String result = Client.get(url, params, "https://api.weixin.qq.com/sns/oauth2/access_token?");
		JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
		return (Map<String, String>)jsonMapper.fromJson(result, Object.class);
	}
	
	/**
	 * 网页授权获取用户信息
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getWebUserInfo(String code) {
		Map<String, String> map = getWebUserToken(code);
		String accessToken = map.get("access_token");
		String openid = map.get("openid");
		String unionUrl = "https://api.weixin.qq.com/sns/userinfo";
		List<NameValuePair> unionParams = new ArrayList<NameValuePair>();
		unionParams.add(new BasicNameValuePair("access_token", accessToken));
		unionParams.add(new BasicNameValuePair("openid", openid));
		unionParams.add(new BasicNameValuePair("lang", "zh_CN"));
		String result = Client.get(unionUrl, unionParams, "https://api.weixin.qq.com/sns/userinfo?");	
		JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
		return (Map<String, String>)jsonMapper.fromJson(result, Object.class);
	}
	
	/**
	 * 获取用户基本信息
	 * @param openid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getUserInfo(String openid) {
		getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/user/info";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", access_token));
		params.add(new BasicNameValuePair("openid", openid));
		params.add(new BasicNameValuePair("lang", "zh_CN"));
		String result = Client.get(url, params, "https://api.weixin.qq.com/cgi-bin/user/info?");	
		JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
		return (Map<String, String>)jsonMapper.fromJson(result, Object.class);
	}

	/**
	 * 获取ticket
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getJsApiTicket() {
		String jsapi_ticket_a = "";
		if (j_expire_time < System.currentTimeMillis()) {
			String accessToken = getAccessToken();
			String url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type", "jsapi"));
			params.add(new BasicNameValuePair("access_token", accessToken));
			String result = Client.get(url, params, "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?");
			JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
			Map<String, String> map = new HashMap<String, String>();
			map = (Map<String, String>) jsonMapper.fromJson(result, Object.class);
			jsapi_ticket_a = map.get("ticket");
			;
			if (jsapi_ticket_a != null && !"".equals(jsapi_ticket_a)) {
				j_expire_time = System.currentTimeMillis() + 7000;
				jsapi_ticket = jsapi_ticket_a;
			}
		} else {
			jsapi_ticket_a = jsapi_ticket;
		}

		return jsapi_ticket_a;
	}

	/**
	 * 下载微信多媒体文件
	 * 
	 * @param media_id
	 * @return
	 */
	public void getWxFile(String media_id, String savePath, String filename) {
		StringBuffer url = new StringBuffer(" https://qyapi.weixin.qq.com/cgi-bin/media/get?");
		String accessToken = getAccessToken();
		url.append("access_token=" + accessToken);
		url.append("&media_id=" + media_id);
		try {
			download(url.toString(), filename, savePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void download(String urlString, String filename, String savePath) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 设置请求超时为5s
		con.setConnectTimeout(5 * 1000);
		// 输入流
		InputStream is = con.getInputStream();
		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		File sf = new File(savePath);
		if (!sf.exists()) {
			sf.mkdirs();
		}
		String fileTempPath = sf.getPath() + File.separator + filename.replace(".mp3", ".arm");
		String filePath = sf.getPath() + File.separator + filename;
		OutputStream os = new FileOutputStream(fileTempPath);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();

		// arm转MP3
		File source = new File(fileTempPath);
		File target = new File(filePath);
		AudioAttributes audio = new AudioAttributes();
		Encoder encoder = new Encoder();
		audio.setCodec("libmp3lame");
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);
		try {
			encoder.encode(source, target, attrs);
			source.delete();// 删除临时文件
		} catch (Exception e) {
		}
	}

	/**
	 * 验证签名
	 * 
	 * @param jsapi_ticket
	 * @param url
	 * @return
	 */
	public static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";

		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		// System.out.println(string1);

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);
		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
}
