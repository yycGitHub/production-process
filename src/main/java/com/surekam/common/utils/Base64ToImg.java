package com.surekam.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;

public class Base64ToImg {
	//base64字符串转化成图片  
	    public static boolean GenerateImage(String imgStr ,String imgFilePath ,String fileName)  
	    {   //对字节数组字符串进行Base64解码并生成图片  
	        if (imgStr == null) //图像数据为空  
	            return false;  
	        BASE64Decoder decoder = new BASE64Decoder();  
	        try   
	        {  
	            //Base64解码  
	            byte[] b = decoder.decodeBuffer(imgStr);  
	            for(int i=0;i<b.length;++i)  
	            {  
	                if(b[i]<0)  
	                {//调整异常数据  
	                    b[i]+=256;  
	                }  
	            }
	            File dirFile = new File(imgFilePath);
	            if (!dirFile.exists()) {
	                dirFile.mkdirs();
	            }
	            OutputStream out = new FileOutputStream(imgFilePath + File.separator + fileName);      
	            out.write(b);  
	            out.flush();  
	            out.close();  
	            return true;  
	        }   
	        catch (Exception e)   
	        {  
	        	e.printStackTrace();
	            return false;  
	        }  
	    }  
}

