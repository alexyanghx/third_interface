package com.kindp.third_infterface.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class HttpClientUtil {
	private static final HttpClient httpClient = new HttpClient();
	
	private final static Logger log = Logger.getLogger(HttpClientUtil.class);

	public static String request(String url, Map<String, String> params) {
		return request(url, params,
				"application/x-www-form-urlencoded; charset=utf-8");

	}

	public static String request(String url, Map<String, String> params,
			String contentType) {
		
		log.info(String.format("req url:[%s],params:[%s]", url,params));
		
		PostMethod post = null;
		try {
			post = new PostMethod(url);
			if (params != null) {
				for (String key : params.keySet()) {
					post.addParameter(key, params.get(key).toString());
				}
			}

			post.setRequestHeader("Content-type", contentType);

			httpClient.executeMethod(post);

			InputStream stream = post.getResponseBodyAsStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					stream, "UTF-8"));
			StringBuffer buf = new StringBuffer();
			String line;
			while (null != (line = br.readLine())) {
				buf.append(line).append("\n");
			}
			String result = buf.toString().trim();
			log.info("send msg respone :"+result);
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			if (post != null) {
				// 释放连接
				post.releaseConnection();
			}

		}

	}

}
