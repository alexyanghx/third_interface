package com.kindp.third_infterface;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;

import com.kindp.third_infterface.shortMsg.chanzor.vo.SendResult;
import com.kindp.third_infterface.util.XStreamUtil;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    
    @Test
    public void testXStreamAnaylze(){
    	String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><returnsms><returnstatus>status</returnstatus><message>message</message><remainpoint> remainpoint</remainpoint><taskID>taskID</taskID><successCounts>successCounts</successCounts></returnsms>";
    	
    	SendResult result = null;
		try {
			result = XStreamUtil.getObjectFromXml(xml, "returnsms", SendResult.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Assert.assertNotNull(result);
    	System.out.println(result.getReturnstatus());
    	
    }
    @Test
    public void channelFile() throws IOException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
    	 	String m = "hello,world";
    	    String v = new String(m.substring(0,2));
    	 
    	    Field f = m.getClass().getDeclaredField("value");
    	    f.setAccessible(true);
    	    char[] cs = (char[]) f.get(m);
    	    cs[0] = 'H';
    	    v.intern();
    	    String p = "Hello,world";
    	    Assert.assertEquals(p.substring(0,2), m.substring(0,2));
    	    Assert.assertEquals(p.substring(0,2), v);
    }
}
