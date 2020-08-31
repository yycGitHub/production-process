package com.surekam.modules.sys.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import com.surekam.common.utils.HttpClientUtil;
import com.surekam.common.utils.StringUtils;
 
public class HttpXmlClient {
	public static HttpPost postForm(String url, Map<String, String> map) {
        for (String key : map.keySet()) {  
        	if(url.contains("?")){
        		url+= "&" + key + "=" + map.get(key);
        	}else{
        		url+= "?" + key + "=" + map.get(key);
        	}
	    }  
        return new HttpPost(url);
    }
 
     /** 
     * @author：罗国辉 
     * @date： 2015年12月17日 上午9:24:43 
     * @description： SHA、SHA1加密
     * @parameter：   str：待加密字符串
     * @return：  加密串
    **/
    public static String SHA1(String str) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1"); //如果是SHA加密只需要将"SHA-1"改成"SHA"即可
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexStr = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        //获取access_token
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid","wx17d52ef0e85644e6");
        params.put("secret","f011f0e7b1931658af016c71cdad1ae2");
        params.put("grant_type","client_credential");
        
        HttpClientUtil httpClientUtil = HttpClientUtil.getInstance();
		HttpPost httpPost = postForm("https://api.weixin.qq.com/cgi-bin/token",params);
		String at = httpClientUtil.sendHttpPost(httpPost);
		JSONObject at_obj = JSONObject.fromObject(at); 
		String access_token = "";
		if(StringUtils.isNotBlank(at_obj.getString("access_token"))){
			access_token = at_obj.getString("access_token");
		}
		System.out.println("access_token=" + access_token);
		
        //获取ticket
		params = new HashMap<String, String>();
		params.put("access_token",access_token);
		params.put("type","jsapi");
		httpPost = postForm("https://api.weixin.qq.com/cgi-bin/ticket/getticket",params);
		String jt = httpClientUtil.sendHttpPost(httpPost);
		JSONObject jt_obj = JSONObject.fromObject(jt); 
		String jsapi_ticket = "";
		if(StringUtils.isNotBlank(jt_obj.getString("ticket"))){
			jsapi_ticket = jt_obj.getString("ticket");
		}
        System.out.println("jsapi_ticket=" + jsapi_ticket);
 
        //获取签名signature
        String noncestr = UUID.randomUUID().toString();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String url="http://mp.weixin.qq.com";
        String str = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + noncestr +
                "&timestamp=" + timestamp +
                "&url=" + url;
        //sha1加密
        String signature = SHA1(str);
        System.out.println("noncestr=" + noncestr);
        System.out.println("timestamp=" + timestamp);
        System.out.println("signature=" + signature);
        //最终获得调用微信js接口验证需要的三个参数noncestr、timestamp、signature
    }
}
