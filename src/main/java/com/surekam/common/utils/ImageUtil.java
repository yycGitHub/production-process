package com.surekam.common.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;


import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public abstract class ImageUtil {

	/**
	 * 按给定高宽缩放图片
	 * @param String srcImgFileName 源文件
	 * @param int destWidth 图片宽
	 * @param int destHeight 图片高
	 * @param boolean isCover	是否覆盖原先的图片
	 * @throws IOException  
	 **/
	public static String zoomImageBySize(HttpServletRequest request,String srcImgFileName,int destWidth,int destHeight,boolean isCover) throws IOException {   
		// 读入文件  
		String fullPath = FileUtils.getFileAbsolutePath(request, srcImgFileName,false);
		File _file = new File(fullPath);   
		// 构造Image对象   
		BufferedImage src = javax.imageio.ImageIO.read(_file);   

		// 边长缩小为二分之一   
		BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);   
		// 绘制缩小后的图   
		tag.getGraphics().drawImage(src, 0, 0, destWidth, destHeight, null);  
		String destPath = srcImgFileName;
		String suffix = srcImgFileName.substring(srcImgFileName.lastIndexOf("."),srcImgFileName.length());
		if(!isCover){
			destPath = destPath.substring(0,destPath.lastIndexOf("/")+1)+System.currentTimeMillis()+suffix;
		}
		FileOutputStream out = new FileOutputStream(FileUtils.getFileAbsolutePath(request, destPath,false));   
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);   
		encoder.encode(tag);   
		out.close(); 
		return destPath;
	}   
	
	/**
	 * 按给定高宽缩放图片
	 * @param String srcImgFileName 源文件
	 * @param double 比例
	 * @param boolean isCover	是否覆盖原先的图片
	 * @throws IOException  
	 **/
	public static String zoomImageByRate(HttpServletRequest request,String srcImgFileName,double rate,boolean isCover) throws IOException {   
		// 读入文件   
		String fullPath = FileUtils.getFileAbsolutePath(request, srcImgFileName,false);
		File _file = new File(fullPath);   
		// 构造Image对象   
		BufferedImage src = javax.imageio.ImageIO.read(_file);   
		int width =  (int)(src.getWidth()*rate);   
	    int height = (int)(src.getHeight()*rate); 
	    return zoomImageBySize(request,srcImgFileName,width,height,isCover);
	}   

    /**  
     * 切图  
     *   
     * @param srcImageFile  
     * @throws IOException  
     */  
    public static String cut(HttpServletRequest request,String oldImageFile,String srcImageFile,String color,int oldWidth,int oldHeigh,
    						int startX,int startY,int width,int height,boolean isCover) throws IOException {   
        Image img;   
        ImageFilter cropFilter;   
        // 读取源图像   
        String fullPath = FileUtils.getFileAbsolutePath(request, srcImageFile,false);
		File _file = new File(fullPath); 
        BufferedImage src = ImageIO.read(_file); 
        
        // 四个参数分别为图像起点坐标和宽高   
        cropFilter = new CropImageFilter(startX-(oldWidth-src.getWidth())/2, startY-(oldHeigh-src.getHeight())/2, width, height); 
        img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(src.getSource(), cropFilter));   
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);   
        Graphics g = tag.getGraphics(); 
        g.drawImage(img, 0, 0,new Color(Integer.parseInt(color,16)),null); // 绘制小图   
        g.dispose();   
        // 输出为文件
        String destPath = srcImageFile;
		String suffix = srcImageFile.substring(srcImageFile.lastIndexOf("."),srcImageFile.length());
		if(!isCover){
			destPath = destPath.substring(0,destPath.lastIndexOf("/")+1)+System.currentTimeMillis()+suffix;
		}
        File f = new File(FileUtils.getFileAbsolutePath(request, destPath));   
        ImageIO.write(tag, "JPEG", f);  
        return destPath;
    } 
    
    /**  
     * 旋转图片 
     * @param srcImageFile
     * @param degree  
     * @param bgcolor
     * @throws IOException  
     */  
    public static String rotateImg(HttpServletRequest request,String srcImageFile, int degree, Color bgcolor,boolean isCover) throws IOException {  
    	String fullPath = FileUtils.getFileAbsolutePath(request, srcImageFile,false);
		File _file = new File(fullPath); 
        BufferedImage image = ImageIO.read(_file);   
        int iw = image.getWidth();//原始图象的宽度 
		int ih = image.getHeight();//原始图象的高度  
		int w = 0;
		int h = 0;
		int x = 0;
		int y = 0;
		degree = degree % 360;
		if (degree < 0)
			degree = 360 + degree;//将角度转换到0-360度之间
		double ang = Math.toRadians(degree);//将角度转为弧度

		/**
		 *确定旋转后的图象的高度和宽度
		 */

		if (degree == 180 || degree == 0 || degree == 360) {
			w = iw;
			h = ih;
		} else if (degree == 90 || degree == 270) {
			w = ih;
			h = iw;
		} else {
			int d = iw + ih;
			w = (int) (d * Math.abs(Math.cos(ang)));
			h = (int) (d * Math.abs(Math.sin(ang)));
		}

		x = (w / 2) - (iw / 2);//确定原点坐标
		y = (h / 2) - (ih / 2);
		BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
		Graphics2D gs = (Graphics2D)rotatedImage.getGraphics();
		if(bgcolor==null){
			rotatedImage  = gs.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		}else{
			gs.setColor(bgcolor);
			gs.fillRect(0, 0, w, h);//以给定颜色绘制旋转后图片的背景
		}
		
		AffineTransform at = new AffineTransform();
		at.rotate(ang, w / 2, h / 2);//旋转图象
		at.translate(x, y);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		op.filter(image, rotatedImage);
		image = rotatedImage;
        String destPath = srcImageFile;
		String suffix = srcImageFile.substring(srcImageFile.lastIndexOf("."),srcImageFile.length());
		if(!isCover){
			destPath = destPath.substring(0,destPath.lastIndexOf("/")+1)+System.currentTimeMillis()+suffix;
		}
        File f = new File(FileUtils.getFileAbsolutePath(request, destPath,false));     
        ImageIO.write(image, "JPEG", f);  
        return destPath;  
    }  
    
    
}
