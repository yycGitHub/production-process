package com.surekam.modules.api.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;  
import java.io.BufferedOutputStream;  
import java.util.Enumeration;  
import java.util.zip.ZipEntry;  
import java.util.zip.ZipFile;  
import com.surekam.common.utils.StringUtils;
import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

public class DeCompressUtil {
	/** 
     * 解压缩zip包 
     * @param zipFilePath zip文件的全路径 
     * @param unzipFilePath 解压后的文件保存的路径 
     * @param includeZipFileName 解压后的文件保存的路径是否包含压缩文件的文件名。true-包含；false-不包含 
     */  
    @SuppressWarnings("unchecked")  
    public static void unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws Exception  
    {  
        File zipFile = new File(zipFilePath);  
        //如果解压后的文件保存路径包含压缩文件的文件名，则追加该文件名到解压路径  
        if (includeZipFileName)  
        {  
            String fileName = zipFile.getName();  
            if (StringUtils.isNotEmpty(fileName))  
            {  
                fileName = fileName.substring(0, fileName.lastIndexOf("."));  
            }  
            unzipFilePath = unzipFilePath + File.separator + fileName;  
        }  
        //创建解压缩文件保存的路径  
        File unzipFileDir = new File(unzipFilePath);  
        if (!unzipFileDir.exists() || !unzipFileDir.isDirectory())  
        {  
            unzipFileDir.mkdirs();  
        }  
          
        //开始解压  
        ZipEntry entry = null;  
        String entryFilePath = null, entryDirPath = null;  
        File entryFile = null, entryDir = null;  
        int index = 0, count = 0, bufferSize = 1024;  
        byte[] buffer = new byte[bufferSize];  
        BufferedInputStream bis = null;  
        BufferedOutputStream bos = null;  
        ZipFile zip = new ZipFile(zipFile);  
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>)zip.entries();  
        //循环对压缩包里的每一个文件进行解压       
        while(entries.hasMoreElements())  
        {  
            entry = entries.nextElement();  
            //构建压缩包中一个文件解压后保存的文件全路径  
            entryFilePath = unzipFilePath + File.separator + entry.getName();  
            //构建解压后保存的文件夹路径  
            index = entryFilePath.lastIndexOf(File.separator);  
            if (index != -1)  
            {  
                entryDirPath = entryFilePath.substring(0, index);  
            }  
            else  
            {  
                entryDirPath = "";  
            }             
            entryDir = new File(entryDirPath);  
            //如果文件夹路径不存在，则创建文件夹  
            if (!entryDir.exists() || !entryDir.isDirectory())  
            {  
                entryDir.mkdirs();  
            }  
            
            //创建解压文件  
            entryFile = new File(entryFilePath);    
            
            if(entry.getName().contains("/") && !entry.getName().contains(".")){
            	entryFile.mkdir();
            }else{
            	//写入文件  
                bos = new BufferedOutputStream(new FileOutputStream(entryFile));  
                bis = new BufferedInputStream(zip.getInputStream(entry));  
                while ((count = bis.read(buffer, 0, bufferSize)) != -1)  
                {  
                    bos.write(buffer, 0, count);  
                }  
                bos.flush();  
                bos.close(); 
            }
        }  
    }  


	/**
	 * 解压rar格式压缩包。
	 * 对应的是java-unrar-0.3.jar，但是java-unrar-0.3.jar又会用到commons-logging-1.1.1.jar
	 */
	private static void unrar(String sourceRar, String destDir) throws Exception {
		Archive a = null;
		FileOutputStream fos = null;
		try {
			a = new Archive(new File(sourceRar));
			FileHeader fh = a.nextFileHeader();
			while (fh != null) {
				//if (!fh.isDirectory()) {
					// 1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
				String compressFileName = fh.getFileNameString().trim();
				String destFileName = "";
				String destDirName = "";
				// 非windows系统
				if (File.separator.equals("/")) {
					destFileName = destDir + compressFileName.replaceAll("\\\\", "/");
					destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
					// windows系统
				} else {
					destFileName = destDir + compressFileName.replaceAll("/", "\\\\");
					destDirName = destFileName.substring(0, destFileName.lastIndexOf("\\"));
				}
				// 2创建文件夹
				File dir = new File(destDirName);
				if (!dir.exists() || !dir.isDirectory()) {
					dir.mkdirs();
				}
				// 3解压缩文件
				if(compressFileName.contains(".")){
					fos = new FileOutputStream(new File(destFileName));
					a.extractFile(fh, fos);
					fos.flush();  
					fos.close();
				}
				//}
				fh = a.nextFileHeader();
			}
			a.close();
			a = null;
		} catch (Exception e) {
			throw e;
		} finally {
			if (fos != null) {
				try {
					fos.close();
					fos = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (a != null) {
				try {
					a.close();
					a = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 解压缩
	 */
	public static void deCompress(String sourceFile, String destDir) throws Exception {
		// 保证文件夹路径最后是"/"或者"\"
		char lastChar = destDir.charAt(destDir.length() - 1);
		if (lastChar != '/' && lastChar != '\\') {
			destDir += File.separator;
		}
		// 根据类型，进行相应的解压缩
		String type = sourceFile.substring(sourceFile.lastIndexOf(".") + 1);
		if (type.equals("zip")) {
			DeCompressUtil.unzip(sourceFile, destDir, false);
		} else if (type.equals("rar")) {
			DeCompressUtil.unrar(sourceFile, destDir);
		} else {
			throw new Exception("只支持zip和rar格式的压缩包！");
		}
	}

}
