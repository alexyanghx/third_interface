/**
 * 
 */
/**
 * @author Administrator
 *
 */
package com.kindp.third_infterface.shortMsg.guodu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.kindp.third_infterface.shortMsg.guodu.vo.SendCode;
import com.kindp.third_infterface.shortMsg.guodu.vo.SendResult;
import com.kindp.third_infterface.util.HttpClientUtil;
import com.kindp.third_infterface.util.XStreamUtil;

/* ========================================================
 * 北京国都互联科技有限公司
 * 日 期：2012-12-18  上午10:04:19
 * 作 者：wangjialong
 * 版 本：0.1
 * =========================================================
 */
public class GuoDuShortMsg {
	
	private String username;
	private String password;
	private String url;
	private String contentType;
	private String appendId;
	private String tmpStr;
	private String expireTime;
	private String reqCharset;
	
	private static final GuoDuShortMsg instance = new GuoDuShortMsg();
	private Logger log = Logger.getLogger(this.getClass());
	
	private GuoDuShortMsg(){
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("config/system.properties"));
			username = prop.getProperty("guodu.username");
			password=prop.getProperty("guodu.password");
			url = prop.getProperty("guodu.url");
			contentType = prop.getProperty("guodu.contentType");
			appendId = prop.getProperty("guodu.appendId");
			tmpStr = prop.getProperty("guodu.tmpStr");
			expireTime = prop.getProperty("guodu.expireTime");
			reqCharset = prop.getProperty("guodu.reqCharset");
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		
	}
	
	public static GuoDuShortMsg getInstance(){
		return instance;
	}
	
	
	 public boolean sendOneMsg(String content,String desMobile,String sendTime,String validTime){
		 String response = HttpClientUtil.requestGet(getUrl(content, new String[]{desMobile}, sendTime, validTime),reqCharset);
		 if(response==null)return false;
		 
		 try {
			 SendResult result = XStreamUtil.getObjectFromXml(response, "response", SendResult.class);
			 
			 if(log.isDebugEnabled()){
				 log.debug("send result :"+result.getCode());
			 }
			 
			 return SendCode.SingleSuccess.getValue().equals(result.getCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			return false;
		}
		 
	 }

/**post方式 发送消息*/
/**
 * @param OperID   		用户名 
 * @param OperPass   	密码
 * @param Content   	发送内容文字  						长度最好不要超过500个字符。
 * @param DesMobiles[]  需要发送的手机号字符串数组   			手机号个数请不要超过200个。
 * @param AppendID    	用户自扩展的号码 。					若扩展请填写号码，若不扩展请填写"",注意！通道号+国都用户身份号+AppendID总长不能超过20位。否则将发送失败。具体号码定义，请参见国都资信通平台接口文档
 * @param SendTime   	发送时间  							如果为定时消息。请填写，格式为yyyyMMddhhmmss 若为实时消息，请填"";
 * @param ValidTime		消息有效期						应该大于SendTime，最好不要填写，国都默认消息有效期为SendTime+3。如果填写错误容易导致消息过有效期无法发送。
 * @param Content_type  内容类型 15为短短信，8为长短信             国都服务端将会自动识别短信长短，所以发送时填写8即可。若填写15 长短信将无法发送。
 * @return rec_string  	国都返回的XML格式的串				
 * @catch Exception
 */
	public String getUrl(String content,String DesMobiles[],String sendTime,String validTime)
	{
		
		/*将内容用URLEncoder编一次GBK*/
		String EncoderContent = "";
		try {
			EncoderContent = URLEncoder.encode(content, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			log.error("encode content fail,ex:"+e.getMessage());
			return null;
		}
		
		/*将手机号从数组转变成一个用逗号分开的字符串，字符串末尾有逗号不影响消息下发。*/
		String DesMobile="";
		for(int i=0;i<DesMobiles.length;i++) {
    		DesMobile = DesMobiles[i]+","+DesMobile;
    	}
		
		StringBuilder sb = new StringBuilder(url+"?OperID="+username+"&OperPass="+password+"&DesMobile="+DesMobile.trim()+"&Content="+EncoderContent+"&ContentType="+contentType);
		if(StringUtils.isNotBlank(sendTime)){
			sb.append("&SendTime="+sendTime);
		}
		
		if(StringUtils.isNotBlank(validTime)){
			sb.append("&ValidTime="+validTime);
		}
		
		if(StringUtils.isNotBlank(appendId)){
			sb.append("&AppendID="+appendId);
		}
		
		String reqUrl = sb.toString();
		if(log.isDebugEnabled()){
			log.debug(String.format("发送的内容为：%s",reqUrl));
		}
		
		
		/*返回响应*/
		return reqUrl;
	}
	
/**get方式 发送消息，与post格式完全相同，仅调用发送方法不同this.getURL(str, URL);*/
	/**
	 * @param Content   	发送内容文字  						长度最好不要超过500个字符。
	 * @param DesMobiles[]  需要发送的手机号字符串数组   			手机号个数请不要超过200个。
	 * @param AppendID    	用户自扩展的号码 。					若扩展请填写号码，若不扩展请填写"",注意！通道号+国都用户身份号+AppendID总长不能超过20位。否则将发送失败。具体号码定义，请参见国都资信通平台接口文档
	 * @param SendTime   	发送时间  							如果为定时消息。请填写，格式为yyyyMMddhhmmss 若为实时消息，请填"";
	 * @param ValidTime		消息有效期						应该大于SendTime，最好不要填写，国都默认消息有效期为SendTime+3。如果填写错误容易导致消息过有效期无法发送。
	 * @param Content_type  内容类型 15为短短信，8为长短信             国都服务端将会自动识别短信长短，所以发送时填写8即可。若填写15 长短信将无法发送。
	 * @return rec_string  	国都返回的XML格式的串				
	 * @catch Exception
	 */
	public String getSendMsg(String Content,String DesMobiles[],String AppendID,String SendTime,String ValidTime,String Content_type) 
		{
			/*将内容用URLEncoder编一次GBK*/
			String EncoderContent = "";
			try {
				EncoderContent = URLEncoder.encode(Content, "GBK");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*将手机号从数组转变成一个用逗号分开的字符串，字符串末尾有逗号不影响消息下发。*/
			String DesMobile="";
			for(int i=0;i<DesMobiles.length;i++) {
	    		DesMobile = DesMobiles[i]+","+DesMobile;
	    	}
			/*url地址*/
			
			/*消息参数*/
			String str="OperID="+username+"&OperPass="+password+"&SendTime="+SendTime+"&ValidTime="+ValidTime+"&AppendID="+AppendID+"&DesMobile="+DesMobile.trim()+"&Content="+EncoderContent+"&ContentType="+Content_type;
			
			System.out.println("发送的内容为："+str);
			/*使用get方式发送消息*/
			String response=this.getURL(str, url);
			
			/*返回响应*/
			return response;
		}

	
	/**post方式 发送url串*/
	/**
	 * @param commString   需要发送的url参数串
	 * @param address   	需要发送的url地址
	 * @return rec_string  国都返回的XML格式的串
	 * @catch Exception
	 */
	public  String postURL(String commString, String address) {
		String rec_string = "";
		URL url = null;
		HttpURLConnection urlConn = null;
		try {
			/*得到url地址的URL类*/
			url = new URL(address);
			/*获得打开需要发送的url连接*/
			urlConn = (HttpURLConnection) url.openConnection();
			/*设置连接超时时间*/
			urlConn.setConnectTimeout(30000);
			/*设置读取响应超时时间*/
			urlConn.setReadTimeout(30000);
			/*设置post发送方式*/
			urlConn.setRequestMethod("POST");
			/*发送commString*/
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			OutputStream out = urlConn.getOutputStream();
			out.write(commString.getBytes());
			out.flush();
			out.close();
			/*发送完毕 获取返回流，解析流数据*/
			BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "GBK"));
			StringBuffer sb = new StringBuffer();
			int ch;
			while ((ch = rd.read()) > -1) {
				sb.append((char) ch);
			}
			rec_string = sb.toString().trim();
			/*解析完毕关闭输入流*/
			rd.close();
		} catch (Exception e) {
			/*异常处理*/
			rec_string = "-107";
			System.out.println(e);
		} finally {
			if (urlConn != null) {
				/*关闭URL连接*/
				urlConn.disconnect();
			}
		}
		/*返回响应内容*/
		return rec_string;
	}
	
	
	/**get方式 发送url串,与post方式代码基本相同，仅发送方式不同*/
	/**
	 * @param commString   需要发送的url参数串
	 * @param address   	需要发送的url地址
	 * @return rec_string  国都返回的XML格式的串
	 * @catch Exception
	 */
	public  String getURL(String commString, String address) {
		String rec_string = "";
		URL url = null;
		HttpURLConnection urlConn = null;
		try {
			/*得到url地址的URL类*/
			url = new URL(address+"?"+commString);
			/*获得打开需要发送的url连接*/
			urlConn = (HttpURLConnection) url.openConnection();
			/*设置连接超时时间*/
			urlConn.setConnectTimeout(30000);
			/*设置读取响应超时时间*/
			urlConn.setReadTimeout(30000);
			/*设置post发送方式*/
			urlConn.setRequestMethod("GET");
			/*发送commString*/
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.connect();
			
			/*发送完毕 获取返回流，解析流数据*/
			BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "GBK"));
			StringBuffer sb = new StringBuffer();
			int ch;
			while ((ch = rd.read()) > -1) {
				sb.append((char) ch);
			}
			rec_string = sb.toString().trim();
			/*解析完毕关闭输入流*/
			rd.close();

	       
		
		} catch (Exception e) {
			/*异常处理*/
			rec_string = "-107";
			System.out.println(e);
		} finally {
			if (urlConn != null) {
				/*关闭URL连接*/
				urlConn.disconnect();
			}
		}
		/*返回响应内容*/
		return rec_string;
	}
	
}
