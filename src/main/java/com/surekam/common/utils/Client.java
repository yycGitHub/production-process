package com.surekam.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * Client 数据接口客户端
 */
public class Client {
	// 日志处理
	private static Logger logger = LoggerFactory.getLogger(Client.class);

	private static HttpClient hc = new DefaultHttpClient(new PoolingClientConnectionManager());

	private static final int REQUEST_SUCCESS = 200;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://localhost:80/gtcms/sd.action";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("OrigAppId", "0001"));
		String body = get(url, params, "http://localhost:80/gtcms/sd.action?");
		System.out.println("Body = " + body);
		System.exit(0);
	}

	/**
	 * Get请求
	 * 
	 * @param url
	 *            ---host
	 * @param params
	 * @return
	 */
	public static String get(String url, List<NameValuePair> params, String address) {
		String body = null;
		// HttpClient hc = new DefaultHttpClient();
		// Get请求
		HttpGet httpget = null;
		try {
			httpget = new HttpGet(url);
			// 设置参数
			String str = EntityUtils.toString(new UrlEncodedFormEntity(params, "UTF-8"), "UTF-8");
			httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
			// 发送请求
			HttpResponse httpresponse = hc.execute(httpget);
			// 判断是否正常返回
			if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 获取返回数据
				HttpEntity entity = httpresponse.getEntity();
				body = EntityUtils.toString(entity, "UTF-8");
				if (entity != null) {
					EntityUtils.consume(entity);
				}
				logger.info("获取到的对方信息 ： " + body);
			} else {
				logger.info("接口地址：" + address + " 返回状态码：" + httpresponse.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			logger.error("接口地址：" + address);
			body = null;
		} finally {
			try {
				httpget.releaseConnection();
			} catch (Exception e) {
				logger.error("释放连接出现异常！");
				body = null;
			}
			hc.getConnectionManager().closeExpiredConnections();
		}
		return body;
	}

	/**
	 * Post请求
	 * 
	 * @param url
	 *            --- host
	 * @param params
	 * @return
	 */
	public static String post(String url, List<NameValuePair> params, String address) {
		String body = null;
		// HttpClient hc = new DefaultHttpClient();
		// Post请求
		HttpPost httppost = null;
		try {
			httppost = new HttpPost(url);
			// 设置参数
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			// 发送请求
			HttpResponse httpresponse = hc.execute(httppost);
			if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 获取返回数据
				HttpEntity entity = httpresponse.getEntity();
				body = EntityUtils.toString(entity, "UTF-8");
				if (entity != null) {
					EntityUtils.consume(entity);
				}
				logger.info("获取到的对方信息 ： " + body);
			} else {
				logger.info("接口地址：" + address + " 返回状态码：" + httpresponse.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			logger.error("接口地址：" + address);
			body = null;
		} finally {
			try {
				httppost.releaseConnection();
			} catch (Exception e) {
				logger.error("释放连接出现异常！");
				body = null;
			}
			hc.getConnectionManager().closeExpiredConnections();
		}
		return body;
	}

	public static String fromHttp(String url, Map<String, String> params) {
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Entry<String, String> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
				sb.append("&");
			}
			sb.substring(0, sb.length() - 1);
		}
		// 尝试发送请求
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			//// POST 只能为大写，严格限制，post会不识别
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			osw.write(sb.toString());
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		// 读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
			// 一定要有返回值，否则无法把请求发送给server端。
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	public static String fromMapHttp(String url, Map<String, Object> params) {
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Entry<String, Object> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
				sb.append("&");
			}
			sb.substring(0, sb.length() - 1);
		}
		// 尝试发送请求
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			//// POST 只能为大写，严格限制，post会不识别
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			osw.write(sb.toString());
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		// 读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
			// 一定要有返回值，否则无法把请求发送给server端。
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public static String post(String url, JSONObject jsonObject, String encoding) {
		String body = "";
		// 创建httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建post方式请求对象
		HttpPost httpPost = new HttpPost(url);

		// 装填参数
		StringEntity s = new StringEntity(jsonObject.toString(), "utf-8");
		s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		// 设置参数到请求对象中
		httpPost.setEntity(s);
		System.out.println("请求地址：" + url);
		// System.out.println("请求参数："+nvps.toString());

		// 设置header信息
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpPost);
		} catch (ClientProtocolException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// 获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// 按指定编码转换结果实体为String类型
			try {
				body = EntityUtils.toString(entity, encoding);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			EntityUtils.consume(entity);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 释放链接
		try {
			response.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return body;
	}

	public static String doPost(String url, Map<String, Object> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (String key : param.keySet()) {
					Object value = param.get(key);
					if (value != null) {
						paramList.add(new BasicNameValuePair(key, value + ""));
					}
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "UTF-8");
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(resultString);
		return resultString;
	}

}
