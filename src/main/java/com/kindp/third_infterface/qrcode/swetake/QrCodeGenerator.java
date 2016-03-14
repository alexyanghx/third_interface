package com.kindp.third_infterface.qrcode.swetake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

public class QrCodeGenerator {
	
	private QrCodeGenerator(){}
	
	private static class LazyHolder{
		private static final Qrcode instance = new Qrcode();
		
		static {
			//错误修正容量   
	        //L水平   7%的字码可被修正  
	        //M水平   15%的字码可被修正  
	        //Q水平   25%的字码可被修正  
	        //H水平   30%的字码可被修正  
	        //QR码有容错能力，QR码图形如果有破损，仍然可以被机器读取内容，最高可以到7%~30%面积破损仍可被读取。  
	        //相对而言，容错率愈高，QR码图形面积愈大。所以一般折衷使用15%容错能力。
			instance.setQrcodeErrorCorrect('H');/* L','M','Q','H' */  
			instance.setQrcodeEncodeMode('B');/* "N","A" or other */  
			instance.setQrcodeVersion(3);/* 0-20 */  
		}
	} 
	
	public static boolean createImage(String content,int unitWidth,String output){
		int DEFAULT_WIDTH = 0;
	    int UNIT_WIDTH = unitWidth;
	    
		Qrcode qrcode=LazyHolder.instance;
  
        byte[] buff = null;  
        try {  
            buff = content.getBytes("utf-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        boolean[][] bRect = qrcode.calQrcode(buff);  
        DEFAULT_WIDTH = bRect.length * UNIT_WIDTH;  
  
        BufferedImage bi = new BufferedImage(DEFAULT_WIDTH, DEFAULT_WIDTH, BufferedImage.TYPE_INT_RGB);  
//        int unitWidth = DEFAULT_WIDTH / bRect.length;  
  
// createGraphics  
        Graphics2D g = bi.createGraphics();  
  
// set background  
        g.setBackground(Color.WHITE);  
        g.clearRect(0, 0, DEFAULT_WIDTH, DEFAULT_WIDTH);  
        g.setColor(Color.BLACK);  
  
        if (buff.length>0 && buff.length <123){  
  
            for (int i=0;i<bRect.length;i++){  
  
                for (int j=0;j<bRect.length;j++){
                    if (bRect[j][i]) {  
                        g.fillRect(j*UNIT_WIDTH, i*UNIT_WIDTH, UNIT_WIDTH, UNIT_WIDTH);  
                    }  
                }  
  
            }  
        }  
  
        g.dispose();  
        bi.flush();  
  
        File f = new File(output);
  
        if(!f.getParentFile().exists()){
        	f.getParentFile().mkdirs();
        }
        
        try {  
            ImageIO.write(bi, "png", f);  
        } catch (IOException e) {  
            e.printStackTrace();
            return false;
        }  
        System.out.println("Create QRCode finished!"); 
        
        return true;
	}
	
}
