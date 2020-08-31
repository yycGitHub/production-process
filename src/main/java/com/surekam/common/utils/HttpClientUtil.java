package com.surekam.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 *  httpclient4.5
 * <p>Title:HttpClientUtil</p>
 * <p>Description:</p>
 * <p>Company:sureserve</p>
 * @date:2017-9-15 上午11:11:36
 * @author xuyx
 */
public class HttpClientUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	private RequestConfig requestConfig = RequestConfig.custom()
	.setSocketTimeout(10*1000)
	.setConnectTimeout(10*1000)
	.setConnectionRequestTimeout(10*1000)
	.build();
	
	private static HttpClientUtil getInstance = null;
	
	private PoolingHttpClientConnectionManager poolConnManager;
	
	private final int maxTotalPool = 200;
	
	private final int maxConPerRoute = 200;

	private HttpClientUtil(){
		init();
	}
	
	public static HttpClientUtil getInstance(){
		if(getInstance == null){
			getInstance = new HttpClientUtil();
		}
		return getInstance;
	}
	
	public void init(){  
		LayeredConnectionSocketFactory sslsf = null;
		try {
			sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			logger.error("创建SSL连接失败");
		}
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
			.register("https", sslsf)
			.register("http", new PlainConnectionSocketFactory())
			.build();
	    poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
	    // Increase max total connection to 200  
	    poolConnManager.setMaxTotal(maxTotalPool);  
	    // Increase default max connection per route to 20  
	    poolConnManager.setDefaultMaxPerRoute(maxConPerRoute);  
	    logger.info("新建单点登录链接池");
	}
	
	public CloseableHttpClient getConnection(){
	    CloseableHttpClient httpClient = HttpClients.custom()
	                .setConnectionManager(poolConnManager)
	                .setConnectionManagerShared(true)
	                .setDefaultRequestConfig(requestConfig)
	                .build();  
	    if(poolConnManager!=null&&poolConnManager.getTotalStats()!=null){
	    	logger.info("now client pool "+poolConnManager.getTotalStats().toString());
	    }  
	    return httpClient;  
	} 

	
	/**
	 * 发送Post请求
	 * @param httpUrl
	 * @param params 参数xml格式
	 * @return
	 * @throws Exception 
	 */
	public String sendHttpPost(String httpUrl,String params) throws Exception{
		HttpPost httpPost = new HttpPost(httpUrl);
		StringEntity stringEntity = new StringEntity(params,"UTF-8");
		httpPost.setHeader("Content-Type","text/xml; charset=utf-8");
		httpPost.setEntity(stringEntity);
		return sendHttpPost(httpPost);
	}
	
	/**
	 * 发送Post请求
	 * @param httpPost
	 * @return
	 */
	public String sendHttpPost(HttpPost httpPost){
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			//httpClient = HttpClients.createDefault();
			//httpPost.setConfig(requestConfig);
			httpClient = getConnection();
			response = httpClient.execute(httpPost);
			logger.info("返回结果状态："+response.getStatusLine().getStatusCode());
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity,"UTF-8");
			EntityUtils.consume(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(response!=null){
					response.close();
				}
//				if(httpClient != null){
//					httpClient.close();
//				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
	
	public static void main(String[] args) {
		//登录连接（获取token）
		HttpPost httpPost = new HttpPost("http://wlyk.husin.cn/api/publics/login.html?mobile=13975897422&password=a12345678");
		String lianjie = new HttpClientUtil().sendHttpPost(httpPost);
		System.out.println(lianjie);
		JSONObject jsonObj = JSONObject.fromObject(lianjie); 
		if(jsonObj.getString("code").equals("1")){
			String json = jsonObj.getString("data");
			JSONObject jsonObj1 = JSONObject.fromObject(json); 
			String token = jsonObj1.getString("token");
			//设备列表查看(获取设备ID和客户端ID)
			httpPost = new HttpPost("http://wlyk.husin.cn/api/user/getDevicesList.html?token="+token);
			String device = new HttpClientUtil().sendHttpPost(httpPost);
			JSONObject jsonObj2 = JSONObject.fromObject(device); 
			System.out.println(jsonObj2);
			if(jsonObj2.getString("code").equals("1")){
				String json1 = jsonObj2.getString("data");
				JSONObject jsonObj4 = JSONObject.fromObject(json1); 
				String liststr = jsonObj4.getString("list").replace("[", "").replace("]", "");
				JSONObject listObj = JSONObject.fromObject(liststr);
				String devices_id = listObj.getString("devices_id");
				String client_id = listObj.getString("client_id");
				System.out.println(devices_id+"     "+client_id);
				
				httpPost = new HttpPost("http://wlyk.husin.cn/api/user/allOpenOrClose.html?token="+token+"&devices_id=569&type=1");
				String qukaiguan = new HttpClientUtil().sendHttpPost(httpPost);
				JSONObject jsonObj5 = JSONObject.fromObject(qukaiguan); 
				System.out.println(jsonObj5);
			}else{
				System.out.println("查询设备列表连接失败");
			}
		}else{
			System.out.println("登录失败");
		}
		
	}

}
