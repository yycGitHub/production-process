package com.surekam.modules.api.utils;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import net.sf.json.JSONObject;

public class WXAuthUtil {
	 public static final String APPID="wx17d52ef0e85644e6";
	    public static final String APPSECRET ="f011f0e7b1931658af016c71cdad1ae2";
	    public static JSONObject doGetJson(String url) throws ClientProtocolException, IOException {
	        JSONObject jsonObj =null;
	        DefaultHttpClient client = new DefaultHttpClient();
	        HttpGet httpGet =new HttpGet(url);
	        HttpResponse response =  client.execute(httpGet);
	        HttpEntity entity =response.getEntity();
	        if(entity!=null)
	        {
	            //把返回的结果转换为JSON对象
	            String result =EntityUtils.toString(entity, "UTF-8");
	            jsonObj =JSONObject.fromObject(result);
	        }
	        
	        return jsonObj;
	    }

}
