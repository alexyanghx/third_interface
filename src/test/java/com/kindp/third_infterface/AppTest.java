package com.kindp.third_infterface;

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
}
