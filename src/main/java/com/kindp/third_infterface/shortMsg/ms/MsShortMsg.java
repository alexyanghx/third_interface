package com.kindp.third_infterface.shortMsg.ms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.kindp.third_infterface.util.HttpClientUtil;

public class MsShortMsg {
	
	private String url;
	
	private String userName;
	
	private String password;
	
	private String extcode;
	
	private String tempid;
	
	private static MsShortMsg instance;
	
	private MsShortMsg() throws IOException{
		Properties prop = new Properties();
		prop.load(this.getClass().getClassLoader().getResourceAsStream("config/system.properties"));
		
		url = prop.getProperty("ms.shortMsg.url");
		userName = prop.getProperty("ms.shortMsg.username");
		password = prop.getProperty("ms.shortMsg.password");
		extcode = prop.getProperty("ms.shortMsg.extcode");
		tempid = prop.getProperty("ms.shortMsg.tempid");
	}
	
	private Map<String,String> getInitParams(){
		Map<String,String> params = new HashMap<String,String>();
		params.put("username", userName);
		params.put("scode", password);
		if(StringUtils.isNotBlank(tempid)){
			params.put("tempid", tempid);
		}
		
		if(StringUtils.isNotBlank(extcode)){
			params.put("extcode", extcode);
		}
		
		return params;
	}
	
	public static synchronized MsShortMsg getInstance() throws Exception {
		if (instance == null) {
			instance = new MsShortMsg();
		}

		return instance;
	}
	/**
	 * 发送短信
	 * @param msg 消息正文
	 * @param phone 接受信息手机号
	 * @param sendtime 定时时间，格式：例如:20090504111010 代表2009年5月4日 11时10分10秒，null表示及时发送
	 * @return
	 */
	public boolean send(String msg,String phone,String sendtime){
		
		Map<String ,String> params = getInitParams();
		params.put("content", msg);
		params.put("mobile", phone);
		
		if(StringUtils.isNotBlank(sendtime)){
			params.put("sendtime", sendtime);
		}
		String result = HttpClientUtil.request(url, params);
		
		if(result==null)
			return false;
		
		return result.startsWith("0");
	}
	
	public String getBalance(String url){
		return HttpClientUtil.request(url, getInitParams());
	}
}
