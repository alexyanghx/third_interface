package com.kindp.third_infterface.push.jpush.android;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

public class AndroidPush {
    protected static final Logger LOG = Logger.getLogger(AndroidPush.class);

    // demo App defined in resources/jpush-api.conf 
	
    
    private String appKey = null;
	private String appMasterSecret = null;
	
	public String alert;
	// 必填 通知标题
	private String title;

	private static AndroidPush instance;
	private final JPushClient jpushClient;
	private static ReadWriteLock lock = new ReentrantReadWriteLock();
	private final static Map<String,AndroidPush> instances = new ConcurrentHashMap<String, AndroidPush>();
	private AndroidPush() throws IOException {
		Properties prop = new Properties();
		prop.load(this.getClass().getClassLoader()
				.getResourceAsStream("push.properties"));
		appKey = prop.getProperty("jpush.appKey");
		appMasterSecret = prop.getProperty("jpush.appMasterSecret");
		title = prop.getProperty("jpush.title");
		jpushClient = new JPushClient(appMasterSecret, appKey);

	}
	
	private AndroidPush(String appKey, String appMasterSecret) {
		this.appKey = appKey;
		this.appMasterSecret = appMasterSecret;
		jpushClient = new JPushClient(appMasterSecret, appKey);
	}

	public static AndroidPush getInstance() throws IOException {
		lock.readLock().lock();
		if (instance == null) {
			lock.readLock().unlock();
			lock.writeLock().lock();
			if(instance==null){
				instance = new AndroidPush();
			}
			lock.writeLock().unlock();
			lock.readLock().lock();
		}
		lock.readLock().unlock();
		return instance;
	}
	
	public static AndroidPush getInstance(String appId,String appKey,String appMasterSecret) throws IOException {
		AndroidPush inst = instances.get(appId);
		if (inst == null) {
			synchronized (instances) {
				if((inst=instances.get(appId))==null){
					inst = new AndroidPush(appKey,appMasterSecret);
					instances.put(appId, inst);
				}
			}
			
		}
		return inst;
	}
	
	public boolean pushAndroid(String msg,String devId,Map<String,String> extra) {
	    // HttpProxy proxy = new HttpProxy("localhost", 3128);
	    // Can use this https proxy: https://github.com/Exa-Networks/exaproxy
		PushPayload payload = buildAndroidPayload(msg, devId, extra);
//		PushPayload payload = buildPushObject_all_all_alert(msg);
        
        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);
            
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            return false;
            
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            return false;
        }
        return true;
	}
	
	public PushPayload buildAndroidPayload(String msg,String devId,Map<String,String> extra){
		Message.Builder msgBuilder = Message.newBuilder().setMsgContent(msg);
		if(extra!=null)msgBuilder.addExtras(extra);
		
		return PushPayload.newBuilder()
        .setPlatform(Platform.android())
        .setAudience(Audience.registrationId(devId))
        //.setNotification(Notification.android(msg, msg, extra))
        .setMessage(msgBuilder.build())
        .build();
	}
	
	public PushPayload buildPushObject_all_all_alert(String msg) {
	    return PushPayload.alertAll(msg);
	}
//	
//    public static PushPayload buildPushObject_all_alias_alert() {
//        return PushPayload.newBuilder()
//                .setPlatform(Platform.all())
//                .setAudience(Audience.alias("alias1"))
//                .setNotification(Notification.alert(ALERT))
//                .build();
//    }
//    
//    public static PushPayload buildPushObject_android_tag_alertWithTitle() {
//        return PushPayload.newBuilder()
//                .setPlatform(Platform.android())
//                .setAudience(Audience.tag("tag1"))
//                .setNotification(Notification.android(ALERT, TITLE, null))
//                .build();
//    }
//    
//    public static PushPayload buildPushObject_android_and_ios() {
//        return PushPayload.newBuilder()
//                .setPlatform(Platform.android_ios())
//                .setAudience(Audience.tag("tag1"))
//                .setNotification(Notification.newBuilder()
//                		.setAlert("alert content")
//                		.addPlatformNotification(AndroidNotification.newBuilder()
//                				.setTitle("Android Title").build())
//                		.addPlatformNotification(IosNotification.newBuilder()
//                				.incrBadge(1)
//                				.addExtra("extra_key", "extra_value").build())
//                		.build())
//                .build();
//    }
//    
//    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {
//        return PushPayload.newBuilder()
//                .setPlatform(Platform.ios())
//                .setAudience(Audience.tag_and("tag1", "tag_all"))
//                .setNotification(Notification.newBuilder()
//                        .addPlatformNotification(IosNotification.newBuilder()
//                                .setAlert(ALERT)
//                                .setBadge(5)
//                                .setSound("happy")
//                                .addExtra("from", "JPush")
//                                .build())
//                        .build())
//                 .setMessage(Message.content(MSG_CONTENT))
//                 .setOptions(Options.newBuilder()
//                         .setApnsProduction(true)
//                         .build())
//                 .build();
//    }
//    
//    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras() {
//        return PushPayload.newBuilder()
//                .setPlatform(Platform.android_ios())
//                .setAudience(Audience.newBuilder()
//                        .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
//                        .addAudienceTarget(AudienceTarget.alias("alias1", "alias2"))
//                        .build())
//                .setMessage(Message.newBuilder()
//                        .setMsgContent(MSG_CONTENT)
//                        .addExtra("from", "JPush")
//                        .build())
//                .build();
//    }
//
//    public static void testSendPushWithCustomConfig() {
//        ClientConfig config = ClientConfig.getInstance();
//        // Setup the custom hostname
//        config.setPushHostName("https://api.jpush.cn");
//
//        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3, null, config);
//
//        // For push, all you need do is to build PushPayload object.
//        PushPayload payload = buildPushObject_all_all_alert();
//
//        try {
//            PushResult result = jpushClient.sendPush(payload);
//            LOG.info("Got result - " + result);
//
//        } catch (APIConnectionException e) {
//            LOG.error("Connection error. Should retry later. ", e);
//
//        } catch (APIRequestException e) {
//            LOG.error("Error response from JPush server. Should review and fix it. ", e);
//            LOG.info("HTTP Status: " + e.getStatus());
//            LOG.info("Error Code: " + e.getErrorCode());
//            LOG.info("Error Message: " + e.getErrorMessage());
//            LOG.info("Msg ID: " + e.getMsgId());
//        }
//    }

//    public static void testSendIosAlert() {
//        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
//
//        IosAlert alert = IosAlert.newBuilder()
//                .setTitleAndBody("test alert", "test ios alert json")
//                .setActionLocKey("PLAY")
//                .build();
//        try {
//            PushResult result = jpushClient.sendIosNotificationWithAlias(alert, new HashMap<String, String>(), "alias1");
//            LOG.info("Got result - " + result);
//        } catch (APIConnectionException e) {
//            LOG.error("Connection error. Should retry later. ", e);
//        } catch (APIRequestException e) {
//            LOG.error("Error response from JPush server. Should review and fix it. ", e);
//            LOG.info("HTTP Status: " + e.getStatus());
//            LOG.info("Error Code: " + e.getErrorCode());
//            LOG.info("Error Message: " + e.getErrorMessage());
//        }
//    }
//
//    public static void testSendWithSMS() {
//        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
//        try {
//            SMS sms = SMS.content("Test SMS", 10);
//            PushResult result = jpushClient.sendAndroidMessageWithAlias("Test SMS", "test sms", sms, "alias1");
//            LOG.info("Got result - " + result);
//        } catch (APIConnectionException e) {
//            LOG.error("Connection error. Should retry later. ", e);
//        } catch (APIRequestException e) {
//            LOG.error("Error response from JPush server. Should review and fix it. ", e);
//            LOG.info("HTTP Status: " + e.getStatus());
//            LOG.info("Error Code: " + e.getErrorCode());
//            LOG.info("Error Message: " + e.getErrorMessage());
//        }
//    }

}
