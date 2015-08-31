package com.kindp.third_infterface.push.umeng.ios;

import com.kindp.third_infterface.push.umeng.IOSNotification;

public class IOSUnicast extends IOSNotification {
	public IOSUnicast() {
		try {
			this.setPredefinedKeyValue("type", "unicast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
