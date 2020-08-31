package com.surekam.modules.api.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.surekam.common.service.BaseService;
import com.surekam.modules.sys.entity.User;

/**
 * 接口管理Service
 * @author lb
 * @version 2018-03-15
 */
@Component
@Transactional(readOnly = true)
public class ApiService extends BaseService {
	
	@Transactional(readOnly = false)
	public String saveAttach(MultipartFile file,String path) throws Exception {
		String result = "";
		//图片上传
		if(file!=null && !file.getOriginalFilename().isEmpty()){
			//保存附件,生成文件名
			SimpleDateFormat sdfdate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String time = sdfdate.format(new Date());
			//文件名
			String name = file.getOriginalFilename();
			//文件后缀
			String extName =  name.substring(name.lastIndexOf(".")); 
			//变更上传文件文件名
			String new_name = time+extName;
			result = "/upload/farming/images/"+new_name;
			// 转存文件 
			file.transferTo(new File(path+new_name));
		}
		return result;
	}
	
	@Transactional(readOnly = false)
	public String saveAudio(MultipartFile file,String path,String fileName,String loginName) throws Exception {
		String result = "";
		//图片上传
		if(file!=null && !file.getOriginalFilename().isEmpty()){
			//保存附件,生成文件名
		//	SimpleDateFormat sdfdate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		//	String time = sdfdate.format(new Date());
			//文件名
		//	String name = file.getOriginalFilename();
			//文件后缀
		//	String extName =  name.substring(name.lastIndexOf(".")); 
			//变更上传文件文件名
		//	String new_name = time+extName;
			result = "/upload/audio/"+loginName+"/"+fileName;
			// 转存文件 
			file.transferTo(new File(path+fileName));
		}
		
		return result;
	}
	
	@Transactional(readOnly = false)
	public String saveEnclosure(MultipartFile file,String path) throws Exception {
		String result = "";
		//图片上传
		if(file!=null && !file.getOriginalFilename().isEmpty()){
			//保存附件,生成文件名
			SimpleDateFormat sdfdate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String time = sdfdate.format(new Date());
			//文件名
			String name = file.getOriginalFilename();
			//文件后缀
			String extName =  name.substring(name.lastIndexOf(".")); 
			//变更上传文件文件名
			String new_name = time+extName;
			result = "/upload/farming/argo/appendix/"+new_name;
			// 转存文件 
			file.transferTo(new File(path+new_name));
		}
		return result;
	}
}
