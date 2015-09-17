package com.kindp.third_infterface.util;

import com.thoughtworks.xstream.XStream;

public class XStreamUtil {
	
	@SuppressWarnings("unchecked")
	public static  <T> T getObjectFromXml(String xml,String name,Class<T> clazz) throws Exception{
		final XStream xstream = new XStream();
		if(xml==null)
			return null;
		
		xstream.alias(name, clazz);
		xstream.ignoreUnknownElements();
		return (T)xstream.fromXML(xml);
	} 
	
}
