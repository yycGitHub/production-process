package com.surekam.modules.common.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.surekam.common.beanvalidator.BeanValidators;
import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.utils.DateUtils;
import com.surekam.common.utils.FileUtils;
import com.surekam.common.utils.excel.ExportExcel;
import com.surekam.common.utils.excel.ImportExcel;
import com.surekam.common.web.BaseController;
import com.surekam.modules.common.entity.FileInfo;
import com.surekam.modules.common.service.FileInfoService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.utils.UserUtils;

/**
 * 公共文件相关操作Controller  包括下载模板、导入、附件上传、下载、删除
 * 下载模板和导入具体数据的业务代码需要写入此文件  界面上以type定义具体业务类别  
 * 附件上传与业务表单分离，先上传附件，传递附件ID给业务表单，保存表单时存入主业务表主键和类型至附件表对应实体中
 * @author ligm
 * @version 2017-11-16
 */
@Controller
@RequestMapping(value = "${adminPath}/common")
public class CommonController extends BaseController{

	private static Logger logger = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	private FileInfoService fileInfoService;
	
	@Autowired
	private SystemService systemService;

	/**
	 * 文件上传
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String uploadFile(MultipartHttpServletRequest request) {
		try {
			JsonMapper jsonMapper = JsonMapper.getInstance();
			String moduleFilePath = request.getParameter("moduleFilePath");
			String fieldMark = request.getParameter("fieldMark");
			String path = Global.getCkBaseDir();
			if(StringUtils.isNotBlank(moduleFilePath)){
				path += moduleFilePath+File.separator+DateUtils.formatDate(new Date(), "yyyyMMdd")+File.separator;
			}else{
				path += "base"+File.separator+DateUtils.formatDate(new Date(), "yyyyMMdd")+File.separator;
			}
			String absolutePath= FileUtils.getFileAbsolutePath(request, path);
	    	String serverURL = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	        MultipartFile file = request.getFile("Filedata");
	        String originalFileName = file.getOriginalFilename();
            String contentType = FileUtils.getFileMimeType(originalFileName);
            String targetFileName = FileUtils.getTargetFileName(originalFileName);
            String url = serverURL+"/"+path.replace("\\", "/")+targetFileName;
            FileUtils.uplaodFile(file,absolutePath,targetFileName);
            
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(originalFileName);
            fileInfo.setNewFileName(targetFileName);
            fileInfo.setType(contentType);
            fileInfo.setUrl(url);
            fileInfo.setAbsolutePath(path+targetFileName);
            fileInfo.setFieldMark(fieldMark);
            fileInfoService.save(fileInfo);
	
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		jsonMapper.setDateFormat(fmt);
			return jsonMapper.toJson(fileInfo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "failed";
		}
	}
	
	/**
	 * 下载附件
	 * @param id
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = {"downloadFile"})
	public void downloadFile(String id,HttpServletResponse response,HttpServletRequest request) throws Exception {
		String path =Global.getCkBaseDir()+"vacationfiles";//文件路径
		String realpath= FileUtils.getFileAbsolutePath(request, path);
		
		// 下载本地文件
		FileInfo fileInfo = fileInfoService.get(id);
		realpath = FileUtils.getFileAbsolutePath(request,"")+fileInfo.getAbsolutePath();
        // 读到流中
        InputStream inStream = new FileInputStream(realpath);// 文件的存放路径      
        // 设置输出的格式        
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + new String( fileInfo.getFileName().getBytes("gb2312"), "ISO8859-1")  + "\"");
        // 循环取出流中的数据    
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	inStream.close();
        }
    }
	
	/**
	 * 删除附件
	 * @param id
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(String id, RedirectAttributes redirectAttributes, 
			HttpServletRequest request) {
		try {
			String path =Global.getCkBaseDir();//文件路径
			String realpath= FileUtils.getFileAbsolutePath(request, path);
		
			fileInfoService.delete(realpath,id);
			return "success";
		} catch (Exception e) {
			return "failed";
		}
	}
	
	/**
	 * @description Excel导入模板生成；
	 * @return
	 */
	@RequestMapping(value="downLoadTemp")
	@ResponseBody
	public String downLoadTemp(String type, HttpServletRequest request, HttpServletResponse response, Model model) {  
	    try {
	    	//type为1 表示为助学贷款模板下载     2表示入伍代偿模板下载   3基层就业代偿模板下载
	    	if(StringUtils.isNotBlank(type) && "1".equals(type)){
	    		String fileName = "用户数据导入模板.xlsx";
				List<User> list = Lists.newArrayList();
				list.add(UserUtils.getUser());
				new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
				return null;
	    	}/*else if(StringUtils.isNotBlank(type) && "2".equals(type)){
	    		String fileName = "入伍代偿数据导入模板.xlsx";  
		        List<GXXS1012> list = new ArrayList<GXXS1012>(); 
		        // 第三个参数设置为“2”表示输出为导入模板（1:导出数据；2：导入模板）  
		        new ExportExcel("入伍代偿信息", GXXS1012.class, 2).setDataList(list).write(response, fileName).dispose();  
		        return null;  
	    	}*/
	    	return null;
	    } catch (Exception e) {  
	        return "生成导入模板失败！";
	    }  
	} 
	
	
	
	/**
	 * @description Excel导入数据；
	 * @return String 导入数据入库的情况；
	 */
	@RequestMapping(value="importFile")
	@ResponseBody
	public String importFile(String type ,MultipartFile file,HttpServletRequest request, HttpServletResponse response) {  
		//设置导入成功和失败记录数的计数器，并初始化；
        int successNum = 0;
        int failureNum = 0;
        List<String> errorList = new ArrayList<String>();
		try {  
	        ImportExcel ei = new ImportExcel(file,1,0);
	        //type=1 表示为用户数据导入
	        if(StringUtils.isNotBlank(type) && "1".equals(type)){
				List<User> list = ei.getDataList(User.class);
				if(null != list && 0 != list.size()){
					//设置导入成功和失败记录数的计数器，并初始化；
					int totalNum = list.size(); 
					
					for (User user : list){
						float progressFloat = ((float)successNum + (float)failureNum)/(float)totalNum*100;
						UserUtils.putCache("progress", (int)progressFloat);
						
						try{
							if (user.getLoginName() != null && systemService.getUserByLoginName(user.getLoginName()) == null){
								user.setPassword(SystemService.entryptPassword("123456"));
								BeanValidators.validateWithException(validator, user);
								systemService.saveUser(user);
								successNum++;
							}else{
								errorList.add("【登录名为" + user.getLoginName() + "用户已存在，导入失败】");
								failureNum++;
							}
						}catch(ConstraintViolationException ex){
							String failureMsg = "【登录名为" + user.getLoginName() + "数据未通过验证，验证详情为：";
							List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
							for (String message : messageList){
								failureMsg += message+"; ";
							}
							failureMsg += "】";
							errorList.add(failureMsg);
							failureNum++;
						}catch (Exception ex) {
							errorList.add("【登录名 " + user.getLoginName() + " 导入失败：" + ex.getMessage() + "】");
							failureNum++;
						}
					}
	        	
		        }else{
		        	errorList.add("【文件中没有有效数据】");
		        }
		        
	        }/*else if(StringUtils.isNotBlank(type) && "2".equals(type)){
	        //导入入伍代偿数据    代码暂只注释  供加入导入功能的人参考     需要到数据库中验证的数据 建议放到service中统一处理 返回数据  能一定程度上提速
	        	List<GXXS1012> gBList = ei.getDataList(GXXS1012.class);
		        if(null != gBList && 0 != gBList.size()){
		        	if(StringUtils.isNotBlank(testDate) && "1".equals(testDate)){
		        		gXXS1012Service.saveGXXS1012And1013ImportMultTestData(gBList);
		        	}else{
				        Map<String,Object> resultMap = gXXS1012Service.saveGXXS1012And1013ImportMult(gBList);
				        successNum = (Integer)resultMap.get("successNum");
				        failureNum = (Integer)resultMap.get("failureNum");
				        errorList = (List<String>)resultMap.get("errorList");
		        	}
		        }else{
		        	errorList.add("【文件中没有有效数据】");
		        }
			        
	        }*/
	        else{
	        	return "参数传递错误，导入失败。";
	        }
	    } catch (Exception e) { 
	    	e.printStackTrace();
	    	return "导入失败,请检查数据格式是否正确！";
	    } 
		//若导入结果提醒条数超过10条则存入文件供下载  否则直接显示结果详情
		if(null != errorList && errorList.size() > 10){
			errorList.add("");
			errorList.add("成功导入"+successNum+"条记录！ 失败 "+failureNum+"条记录！");
			
            String path =Global.getCkBaseDir()+"importresultfiles";//文件路径
			String realpath= FileUtils.getFileAbsolutePath(request, path);
			String newFileName = String.valueOf(System.currentTimeMillis())
	                    + "result.txt";
			File realdir = new File(realpath);
			File newfile =new File(realpath,newFileName);
			if(!realdir.exists() && !realdir .isDirectory()){
				realdir.mkdirs();
			}
			
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(newfile));
				for(String str : errorList){
					// 写文件
					bw.write(str, 0, str.length());
					bw.write("\r\n");
					// 刷新流
					bw.flush();
				}
				// 用于下载的文件名称
	            return newFileName;
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					// 关闭文件流
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			StringBuilder resultMessage = new StringBuilder();
			for (Iterator iterator = errorList.iterator(); iterator.hasNext();) {
				String mess = (String) iterator.next();
				resultMessage.append(mess + "<br/>");
			}
			if(resultMessage.length() == 0){
				return "成功导入"+successNum+"条记录！ 失败 "+failureNum+"条记录！";
			}
			return "成功导入"+successNum+"条记录！ 失败 "+failureNum+"条记录！" + "导入详情为：<br/>" + resultMessage.toString();
		}
		return null;
	}  
	
	/**
	 * @description 跳转到导入数据文件上传的页面；
	 * @return
	 */
	@RequestMapping(value="uploadData")
	public String uploadData(String type, Model model, HttpServletRequest request){
		model.addAttribute("type", type);
		return "modules/" + "common/temUploadForm";
	}
	
	/**
	 * @description 获取实时进度
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getProgress")
	public int getProgress(){
		if(null != UserUtils.getCache("progress")){
			Integer progress = (Integer)UserUtils.getCache("progress");
			return progress;
		}
		return 0;
	}
	
	/**
	 * 下载导入结果
	 * @param id
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = {"download"})
	public void download(String fileName,HttpServletResponse response,HttpServletRequest request) throws Exception {
		String path =Global.getCkBaseDir()+"importresultfiles";//文件路径
		String realpath= FileUtils.getFileAbsolutePath(request, path);
		
        // 读到流中
        InputStream inStream = new FileInputStream(realpath + fileName);// 文件的存放路径      
        // 设置输出的格式        
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + new String( fileName.getBytes("gb2312"), "ISO8859-1")  + "\"");
        // 循环取出流中的数据    
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	inStream.close();
        }
    }
	
}
