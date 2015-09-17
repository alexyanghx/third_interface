package com.kindp.third_infterface.shortMsg.chanzor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.kindp.third_infterface.shortMsg.chanzor.vo.BalanceResult;
import com.kindp.third_infterface.shortMsg.chanzor.vo.SendResult;
import com.kindp.third_infterface.util.HttpClientUtil;
import com.kindp.third_infterface.util.XStreamUtil;

public class ChanzorShortMsg {
	
	private String url;
	
	private String userId;
	
	private String account;
	
	private String password;
	
	private String extno;
	
	private String tmpStr;
	
	private String expireTime;
	
	private static ChanzorShortMsg instance;
	
	private ChanzorShortMsg() throws IOException{
		Properties prop = new Properties();
		prop.load(this.getClass().getClassLoader().getResourceAsStream("config/system.properties"));
		
		url = prop.getProperty("chanzor.shortMsg.url");
		userId = prop.getProperty("chanzor.shortMsg.userid");
		account = prop.getProperty("chanzor.shortMsg.account");
		password = prop.getProperty("chanzor.shortMsg.password");
		extno = prop.getProperty("chanzor.shortMsg.extno");
		tmpStr = prop.getProperty("chanzor.shortMsg.tmpStr");
		expireTime = prop.getProperty("chanzor.shortMsg.expireTime");
	}
	
	private Map<String,String> getInitParams(){
		Map<String,String> params = new HashMap<String,String>();
		params.put("account", account);
		params.put("password", password);
		if(StringUtils.isNotBlank(extno)){
			params.put("extno", extno);
		}
		
		if(StringUtils.isNotBlank(userId)){
			params.put("userid", userId);
		}
		
		return params;
	}
	
	public static synchronized ChanzorShortMsg getInstance() throws Exception {
		if (instance == null) {
			instance = new ChanzorShortMsg();
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
		params.put("content", String.format(tmpStr, msg,expireTime));
		params.put("mobile", phone);
		params.put("action", "send");
		
		if(StringUtils.isNotBlank(sendtime)){
			params.put("sendTime", sendtime);
		}
		String result = HttpClientUtil.request(url, params);
		
		if(result==null)
			return false;
		
		return sendSuccess(result);
	}
	
	public BalanceResult getBalance(String url){
		Map<String,String> params = getInitParams();
		params.put("action", "overage");
		String result = HttpClientUtil.request(url,params );
		BalanceResult bResult = null;
		try {
			bResult = XStreamUtil.getObjectFromXml(result, "returnsms", BalanceResult.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bResult;
	}
	
	public boolean sendSuccess(String result){
		try{
			SendResult returnSms = XStreamUtil.getObjectFromXml(result,"returnsms", SendResult.class);
			return "Success".equalsIgnoreCase(returnSms.getReturnstatus()!=null?returnSms.getReturnstatus().trim():null);
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
}
