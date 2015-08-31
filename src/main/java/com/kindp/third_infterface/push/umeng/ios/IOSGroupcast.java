package com.kindp.third_infterface.push.umeng.ios;

import com.kindp.third_infterface.push.umeng.IOSNotification;

public class IOSGroupcast extends IOSNotification {
	public IOSGroupcast() {
		try {
			this.setPredefinedKeyValue("type", "groupcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
