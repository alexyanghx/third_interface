package com.kindp.third_infterface.push.umeng;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.kindp.third_infterface.push.umeng.android.AndroidUnicast;

public class AndroidPush {
	private String appKey = null;
	private String appMasterSecret = null;
	// 必填 通知标题
	private String title;
	// 必填 通知栏提示文字
	private String ticker;
	// 必填 值可以为:
	// "go_app": 打开应用
	// "go_url": 跳转到URL
	// "go_activity": 打开特定的activity
	// "go_custom": 用户自定义内容。
	private String afterOpen;
	// 必填 消息类型，值可以为:
	private String displayType;
	// 可选 正式/测试模式。测试模式下，广播/组播只会将消息发给测试设备
	private boolean productionMode;

	private static AndroidPush instance;

	private AndroidPush() throws IOException {
		Properties prop = new Properties();
		prop.load(this.getClass().getClassLoader()
				.getResourceAsStream("push.properties"));
		appKey = prop.getProperty("umeng.android.appKey");
		appMasterSecret = prop.getProperty("umeng.android.appMasterSecret");
		title = prop.getProperty("umeng.android.title");
		ticker = prop.getProperty("umeng.android.ticker");
		afterOpen = prop.getProperty("umeng.android.after_open");
		displayType = prop.getProperty("umeng.android.display_type");
		productionMode = Boolean.parseBoolean(prop
				.getProperty("umeng.android.production_mode"));

	}

	public static AndroidPush getInstance() throws IOException {
		if (instance == null) {
			instance = new AndroidPush();
		}

		return instance;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public String getAfterOpen() {
		return afterOpen;
	}

	public void setAfterOpen(String afterOpen) {
		this.afterOpen = afterOpen;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public boolean isProductionMode() {
		return productionMode;
	}

	public void setProductionMode(boolean productionMode) {
		this.productionMode = productionMode;
	}

	// public void sendAndroidBroadcast() throws Exception {
	// AndroidBroadcast broadcast = new AndroidBroadcast();
	// broadcast.setAppMasterSecret(appMasterSecret);
	// broadcast.setPredefinedKeyValue("appkey", this.appkey);
	// broadcast.setPredefinedKeyValue("timestamp", this.timestamp);
	// broadcast.setPredefinedKeyValue("ticker", "Android broadcast ticker");
	// broadcast.setPredefinedKeyValue("title", "中文的title");
	// broadcast.setPredefinedKeyValue("text", "Android broadcast text");
	// broadcast.setPredefinedKeyValue("after_open", "go_app");
	// broadcast.setPredefinedKeyValue("display_type", "notification");
	// // TODO Set 'production_mode' to 'false' if it's a test device.
	// // For how to register a test device, please see the developer doc.
	// broadcast.setPredefinedKeyValue("production_mode", "true");
	// // Set customized fields
	// broadcast.setExtraField("test", "helloworld");
	// broadcast.send();
	// }

	public boolean sendAndroidUnicast(String msg, String token,
			Map<String, String> extra) throws Exception {
		AndroidUnicast unicast = new AndroidUnicast();
		unicast.setAppMasterSecret(appMasterSecret);
		unicast.setPredefinedKeyValue("appkey", this.appKey);

		unicast.setPredefinedKeyValue("timestamp",
				System.currentTimeMillis() / 1000);
		// TODO Set your device token
		unicast.setPredefinedKeyValue("device_tokens", token);
		unicast.setPredefinedKeyValue("ticker", ticker);
		unicast.setPredefinedKeyValue("title", title);
		unicast.setPredefinedKeyValue("text", msg);
		unicast.setPredefinedKeyValue("after_open", afterOpen);
		unicast.setPredefinedKeyValue("display_type", displayType);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		unicast.setPredefinedKeyValue("production_mode", productionMode);

		if (extra != null) {
			for (String key : extra.keySet()) {
				unicast.setExtraField(key, extra.get(key));
			}
		}

		return unicast.send();
	}

	// public void sendAndroidGroupcast() throws Exception {
	// AndroidGroupcast groupcast = new AndroidGroupcast();
	// groupcast.setAppMasterSecret(appMasterSecret);
	// groupcast.setPredefinedKeyValue("appkey", this.appkey);
	// groupcast.setPredefinedKeyValue("timestamp", this.timestamp);
	// /* TODO
	// * Construct the filter condition:
	// * "where":
	// * {
	// * "and":
	// * [
	// * {"tag":"test"},
	// * {"tag":"Test"}
	// * ]
	// * }
	// */
	// JSONObject filterJson = new JSONObject();
	// JSONObject whereJson = new JSONObject();
	// JSONArray tagArray = new JSONArray();
	// JSONObject testTag = new JSONObject();
	// JSONObject TestTag = new JSONObject();
	// testTag.put("tag", "test");
	// TestTag.put("tag", "Test");
	// tagArray.put(testTag);
	// tagArray.put(TestTag);
	// whereJson.put("and", tagArray);
	// filterJson.put("where", whereJson);
	// System.out.println(filterJson.toString());
	//
	// groupcast.setPredefinedKeyValue("filter", filterJson);
	// groupcast.setPredefinedKeyValue("ticker", "Android groupcast ticker");
	// groupcast.setPredefinedKeyValue("title", "中文的title");
	// groupcast.setPredefinedKeyValue("text", "Android groupcast text");
	// groupcast.setPredefinedKeyValue("after_open", "go_app");
	// groupcast.setPredefinedKeyValue("display_type", "notification");
	// // TODO Set 'production_mode' to 'false' if it's a test device.
	// // For how to register a test device, please see the developer doc.
	// groupcast.setPredefinedKeyValue("production_mode", "true");
	// groupcast.send();
	// }
	//
	// public void sendAndroidCustomizedcast() throws Exception {
	// AndroidCustomizedcast customizedcast = new AndroidCustomizedcast();
	// customizedcast.setAppMasterSecret(appMasterSecret);
	// customizedcast.setPredefinedKeyValue("appkey", this.appkey);
	// customizedcast.setPredefinedKeyValue("timestamp", this.timestamp);
	// // TODO Set your alias here, and use comma to split them if there are
	// multiple alias.
	// // And if you have many alias, you can also upload a file containing
	// these alias, then
	// // use file_id to send customized notification.
	// customizedcast.setPredefinedKeyValue("alias", "xx");
	// // TODO Set your alias_type here
	// customizedcast.setPredefinedKeyValue("alias_type", "xx");
	// customizedcast.setPredefinedKeyValue("ticker",
	// "Android customizedcast ticker");
	// customizedcast.setPredefinedKeyValue("title", "中文的title");
	// customizedcast.setPredefinedKeyValue("text",
	// "Android customizedcast text");
	// customizedcast.setPredefinedKeyValue("after_open", "go_app");
	// customizedcast.setPredefinedKeyValue("display_type", "notification");
	// // TODO Set 'production_mode' to 'false' if it's a test device.
	// // For how to register a test device, please see the developer doc.
	// customizedcast.setPredefinedKeyValue("production_mode", "true");
	// customizedcast.send();
	// }
	//
	// public void sendAndroidFilecast() throws Exception {
	// AndroidFilecast filecast = new AndroidFilecast();
	// filecast.setAppMasterSecret(appMasterSecret);
	// filecast.setPredefinedKeyValue("appkey", this.appkey);
	// filecast.setPredefinedKeyValue("timestamp", this.timestamp);
	// // TODO upload your device tokens, and use '\n' to split them if there
	// are multiple tokens
	// filecast.uploadContents("aa"+"\n"+"bb");
	// filecast.setPredefinedKeyValue("ticker", "Android filecast ticker");
	// filecast.setPredefinedKeyValue("title", "中文的title");
	// filecast.setPredefinedKeyValue("text", "Android filecast text");
	// filecast.setPredefinedKeyValue("after_open", "go_app");
	// filecast.setPredefinedKeyValue("display_type", "notification");
	// filecast.send();
	// }
	//
	// public void sendIOSBroadcast() throws Exception {
	// IOSBroadcast broadcast = new IOSBroadcast();
	// broadcast.setAppMasterSecret(appMasterSecret);
	// broadcast.setPredefinedKeyValue("appkey", this.appkey);
	// broadcast.setPredefinedKeyValue("timestamp", this.timestamp);
	//
	// broadcast.setPredefinedKeyValue("alert", "IOS 广播测试");
	// broadcast.setPredefinedKeyValue("badge", 0);
	// broadcast.setPredefinedKeyValue("sound", "chime");
	// // TODO set 'production_mode' to 'true' if your app is under production
	// mode
	// broadcast.setPredefinedKeyValue("production_mode", "false");
	// // Set customized fields
	// broadcast.setCustomizedField("test", "helloworld");
	// broadcast.send();
	// }
	//
	// public void sendIOSUnicast() throws Exception {
	// IOSUnicast unicast = new IOSUnicast();
	// unicast.setAppMasterSecret(appMasterSecret);
	// unicast.setPredefinedKeyValue("appkey", this.appkey);
	// unicast.setPredefinedKeyValue("timestamp", this.timestamp);
	// // TODO Set your device token
	// unicast.setPredefinedKeyValue("device_tokens", "xx");
	// unicast.setPredefinedKeyValue("alert", "IOS 单播测试");
	// unicast.setPredefinedKeyValue("badge", 0);
	// unicast.setPredefinedKeyValue("sound", "chime");
	// // TODO set 'production_mode' to 'true' if your app is under production
	// mode
	// unicast.setPredefinedKeyValue("production_mode", "false");
	// // Set customized fields
	// unicast.setCustomizedField("test", "helloworld");
	// unicast.send();
	// }
	//
	// public void sendIOSGroupcast() throws Exception {
	// IOSGroupcast groupcast = new IOSGroupcast();
	// groupcast.setAppMasterSecret(appMasterSecret);
	// groupcast.setPredefinedKeyValue("appkey", this.appkey);
	// groupcast.setPredefinedKeyValue("timestamp", this.timestamp);
	// /* TODO
	// * Construct the filter condition:
	// * "where":
	// * {
	// * "and":
	// * [
	// * {"tag":"iostest"}
	// * ]
	// * }
	// */
	// JSONObject filterJson = new JSONObject();
	// JSONObject whereJson = new JSONObject();
	// JSONArray tagArray = new JSONArray();
	// JSONObject testTag = new JSONObject();
	// testTag.put("tag", "iostest");
	// tagArray.put(testTag);
	// whereJson.put("and", tagArray);
	// filterJson.put("where", whereJson);
	// System.out.println(filterJson.toString());
	//
	// // Set filter condition into rootJson
	// groupcast.setPredefinedKeyValue("filter", filterJson);
	// groupcast.setPredefinedKeyValue("alert", "IOS 组播测试");
	// groupcast.setPredefinedKeyValue("badge", 0);
	// groupcast.setPredefinedKeyValue("sound", "chime");
	// // TODO set 'production_mode' to 'true' if your app is under production
	// mode
	// groupcast.setPredefinedKeyValue("production_mode", "false");
	// groupcast.send();
	// }
	//
	// public void sendIOSCustomizedcast() throws Exception {
	// IOSCustomizedcast customizedcast = new IOSCustomizedcast();
	// customizedcast.setAppMasterSecret(appMasterSecret);
	// customizedcast.setPredefinedKeyValue("appkey", this.appkey);
	// customizedcast.setPredefinedKeyValue("timestamp", this.timestamp);
	// // TODO Set your alias here, and use comma to split them if there are
	// multiple alias.
	// // And if you have many alias, you can also upload a file containing
	// these alias, then
	// // use file_id to send customized notification.
	// customizedcast.setPredefinedKeyValue("alias", "xx");
	// // TODO Set your alias_type here
	// customizedcast.setPredefinedKeyValue("alias_type", "xx");
	// customizedcast.setPredefinedKeyValue("alert", "IOS 个性化测试");
	// customizedcast.setPredefinedKeyValue("badge", 0);
	// customizedcast.setPredefinedKeyValue("sound", "chime");
	// // TODO set 'production_mode' to 'true' if your app is under production
	// mode
	// customizedcast.setPredefinedKeyValue("production_mode", "false");
	// customizedcast.send();
	// }
	//
	// public void sendIOSFilecast() throws Exception {
	// IOSFilecast filecast = new IOSFilecast();
	// filecast.setAppMasterSecret(appMasterSecret);
	// filecast.setPredefinedKeyValue("appkey", this.appkey);
	// filecast.setPredefinedKeyValue("timestamp", this.timestamp);
	// // TODO upload your device tokens, and use '\n' to split them if there
	// are multiple tokens
	// filecast.uploadContents("aa"+"\n"+"bb");
	// filecast.setPredefinedKeyValue("alert", "IOS 文件播测试");
	// filecast.setPredefinedKeyValue("badge", 0);
	// filecast.setPredefinedKeyValue("sound", "chime");
	// // TODO set 'production_mode' to 'true' if your app is under production
	// mode
	// filecast.setPredefinedKeyValue("production_mode", "false");
	// filecast.send();
	// }
	//
	// public static void main(String[] args) {
	// // TODO set your appkey and master secret here
	// AndroidPush demo = new AndroidPush("your appkey",
	// "the app master secret");
	// try {
	// demo.sendAndroidUnicast();
	// /* TODO these methods are all available, just fill in some fields and do
	// the test
	// * demo.sendAndroidBroadcast();
	// * demo.sendAndroidGroupcast();
	// * demo.sendAndroidCustomizedcast();
	// * demo.sendAndroidFilecast();
	// *
	// * demo.sendIOSBroadcast();
	// * demo.sendIOSUnicast();
	// * demo.sendIOSGroupcast();
	// * demo.sendIOSCustomizedcast();
	// * demo.sendIOSFilecast();
	// */
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }

}
