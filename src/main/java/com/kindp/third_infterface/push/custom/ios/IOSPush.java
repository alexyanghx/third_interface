package com.kindp.third_infterface.push.custom.ios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;

public class IOSPush {
	private int badge;// 图标小红圈的数值
	private String sound = "";// 铃音

	private String certificatePath;
	private String certificatePassword;// 此处注意导出的证书密码不能为空因为空密码会报错
	private PushNotificationManager pushManager;
	private boolean product;

	private static IOSPush instance;
	private AppleNotificationServer server;
	private Logger log = Logger.getLogger(this.getClass());
	
	private final Map<String,IOSPush> instances = new ConcurrentHashMap<String, IOSPush>();
	
	private IOSPush() throws Exception {

		Properties prop = new Properties();
		prop.load(this.getClass().getClassLoader()
				.getResourceAsStream("push.properties"));
		badge = Integer.parseInt(prop.getProperty("ios.badge"));
		sound = prop.getProperty("ios.sound");
		certificatePath=prop.getProperty("ios.certificatePath");
		certificatePassword=prop.getProperty("ios.certificatePassword");
		product = Boolean.parseBoolean(prop.getProperty("ios.product"));
		server = new AppleNotificationServerBasicImpl(
				this.getClass().getClassLoader()
				.getResourceAsStream(certificatePath), certificatePassword, product);
		pushManager = new PushNotificationManager();
		pushManager.initializeConnection(server);
	}
	
	private IOSPush(Integer badge,String sound,String certificatePath,String certificatePassword,boolean product) throws Exception{
		this.badge = badge;
		this.sound = sound;
		this.certificatePath = certificatePath;
		this.certificatePassword = certificatePassword;
		this.product = product;
		
		server = new AppleNotificationServerBasicImpl(
				this.getClass().getClassLoader()
				.getResourceAsStream(certificatePath), certificatePassword, product);
		pushManager = new PushNotificationManager();
		pushManager.initializeConnection(server);
	}
	
	public static IOSPush getInstance() throws Exception {
//		if (instance == null) {
			instance = new IOSPush();
//		}

		return instance;
	}
	
	public static IOSPush getInstance(Integer badge,String sound,String certificatePath,String certificatePassword,boolean product)  throws Exception {
		return new IOSPush(badge,sound,certificatePath,certificatePassword,product);
	}
	
	public int getBadge() {
		return badge;
	}




	public void setBadge(int badge) {
		this.badge = badge;
	}




	public String getSound() {
		return sound;
	}




	public void setSound(String sound) {
		this.sound = sound;
	}




	public String getCertificatePath() {
		return certificatePath;
	}




	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}




	public String getCertificatePassword() {
		return certificatePassword;
	}




	public void setCertificatePassword(String certificatePassword) {
		this.certificatePassword = certificatePassword;
	}




	public boolean isProduct() {
		return product;
	}




	public void setProduct(boolean product) {
		this.product = product;
	}


	public PushNotificationPayload getPayLoad() throws JSONException{
		PushNotificationPayload payLoad = new PushNotificationPayload();
		payLoad.addBadge(badge); // iphone应用图标上小红圈上的数值
		if (!StringUtils.isBlank(sound)) {
			payLoad.addSound(sound);// 铃音
		}
		return payLoad;
	}

	public boolean push(String msg, String deviceToken) {
		return push(msg,deviceToken,null);
	}

	public boolean push(String msg, String deviceToken,Map<String,String> extParams) {
			PushNotificationPayload payLoad;
			boolean success = true;
			try {
				payLoad = getPayLoad();
			
				payLoad.addAlert(msg); // 消息内容
				if(extParams!=null){
					for(String key:extParams.keySet()){
						payLoad.addCustomDictionary(key, extParams.get(key));
					}
				}
				// true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
				Device device = new BasicDevice();
				device.setToken(deviceToken);
				PushedNotification notification = pushManager.sendNotification(device, payLoad, false);
				success = notification.isSuccessful();
				System.out.println(String.format("ios token[%s] push result :[%s]",deviceToken,notification.isSuccessful()));
//				if(notification.getException()!=null&&notification.getException().getMessage().contains("Socket is closed")){
////					notification.getException().printStackTrace();
//					log.info("#AAAAA#ios push Socket is closed restart");
//					pushManager.restartConnection(server);
//				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				log.error("#AAAAA#ios push JSONException :");
				e.printStackTrace();
				success = false;
			} catch (CommunicationException e) {
				// TODO Auto-generated catch block
				log.error("#AAAAA#ios push CommunicationException :");
				e.printStackTrace();
				success = false;
			} finally{
				try {
					pushManager.stopConnection();
					log.info("pushManager stopConnection success!");
				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					log.info("pushManager stopConnection CommunicationException error !");
					e.printStackTrace();
				} catch (KeystoreException e) {
					// TODO Auto-generated catch block
					log.info("pushManager stopConnection KeystoreException error !");
					e.printStackTrace();
				}
			}
			
			return success;
	}
	
	public boolean multiPush(String msg, List<String> deviceTokens,
			List<PushedNotification> failedNotifications,
			List<PushedNotification> successfulNotifications,Map<String,String> extParams) {

		try {
			PushNotificationPayload payLoad = getPayLoad();
			payLoad.addAlert(msg); // 消息内容
			if(extParams!=null){
				for(String key:extParams.keySet()){
					payLoad.addCustomDictionary(key, extParams.get(key));
				}
			}
			// true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
			List<PushedNotification> notifications = new ArrayList<PushedNotification>();
			List<Device> devices = new ArrayList<Device>();
			for (String token : deviceTokens) {
				devices.add(new BasicDevice(token));
			}
			notifications = pushManager.sendNotifications(payLoad, devices);

			failedNotifications = PushedNotification
					.findFailedNotifications(notifications);
			successfulNotifications = PushedNotification
					.findSuccessfulNotifications(notifications);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
