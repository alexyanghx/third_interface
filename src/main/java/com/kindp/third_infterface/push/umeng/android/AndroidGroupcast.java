package com.kindp.third_infterface.push.umeng.android;

import com.kindp.third_infterface.push.umeng.AndroidNotification;

public class AndroidGroupcast extends AndroidNotification {
	public AndroidGroupcast() {
		try {
			this.setPredefinedKeyValue("type", "groupcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
