package com.kindp.third_infterface.shortMsg.other;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.kindp.third_infterface.shortMsg.other.vo.SendResult;
import com.kindp.third_infterface.util.HttpClientUtil;
import com.kindp.third_infterface.util.XStreamUtil;

public class ShortMsg {
	//sendurl
	private String url;
	//账号
	private String sname;
	//密码
	private String spwd;
	//企业代码
	private String scorpid;
	//产品编号
	private String sprdid;
	
	private String tmpStr;
	
	private String expireTime;
	
	private static ShortMsg instance;
	
	private ShortMsg() throws IOException{
		Properties prop = new Properties();
		prop.load(this.getClass().getClassLoader().getResourceAsStream("config/system.properties"));
		url = prop.getProperty("shortMsg.url");
		sprdid = prop.getProperty("shortMsg.sprdid");
		sname = prop.getProperty("shortMsg.sname");
		spwd = prop.getProperty("shortMsg.spwd");
		scorpid = prop.getProperty("shortMsg.scorpid");
		
		tmpStr = prop.getProperty("shortMsg.tmpStr");
		expireTime = prop.getProperty("shortMsg.expireTime");
	}
	
	private Map<String,String> getInitParams(){
		Map<String,String> params = new HashMap<String,String>();
		params.put("sname", sname);
		params.put("spwd", spwd);
		params.put("sprdid", sprdid);
		params.put("scorpid", scorpid);
		
		return params;
	}
	
	public static synchronized ShortMsg getInstance() throws Exception {
		if (instance == null) {
			instance = new ShortMsg();
		}

		return instance;
	}
	
	public boolean send(String msg,String phone){
		
		Map<String ,String> params = getInitParams();
		params.put("smsg", String.format(tmpStr, msg,expireTime));
		params.put("sdst", phone);
		
		String result = HttpClientUtil.request(url, params);
		System.out.println(result);
		if(result==null)
			return false;
		
		return sendSuccess(result);
	}
	
	public boolean sendSuccess(String result){
		try{
			SendResult returnSms = XStreamUtil.getObjectFromXml(result,"CSubmitState", SendResult.class);
			return "0".equals(returnSms.getState());
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
}
