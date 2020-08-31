package com.surekam.common.utils.freemarker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.surekam.common.utils.Client;
import com.surekam.modules.sample.entity.ExcelListObj;
import com.surekam.modules.sample.entity.ExcelObj;
import com.surekam.modules.sample.entity.ScoreInfo;
import com.surekam.modules.sample.entity.UserInfo;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * java生成word/excel文档工具类
 * @author liuyi
 * @version 2018-01-18
 * */
public class DocUtil {

	 // 日志处理
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private static Configuration configure=null;
    private static final String DEFAULT_ENCODING = "UTF-8";
	
	/*public DocUtil(){
        configure=new Configuration();
        configure.setDefaultEncoding("utf-8");
    }*/
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//DocUtil docUtil=new DocUtil();
		
		//DocUtil.assembleDocData();
		DocUtil.assembleExcelData();
	}
	
	/**
     * 根据xml模板生成Doc/Excel文件
     * @param dataMap 需要填入模板的数据
     * @param templateName 文件名称
     * @param suffix 文件名后缀
     * @param savePath 保存文件路径
     * ****************************************关于模板***************************************
     * 一。关于模板的制作
     * 1.先制作一个你需要的Doc/Excel文档，包含表格等格式在内
     * 2.将Doc/Excel文档内需要替换的内容（即后台读取的数据）用EL表达式的格式替换掉，例如"${name}"
     * 3.将该Doc/Excel文件另存为word2003 xml 文档格式(注意是另存为,不是直接改扩展名) 注：模板可以是xml文件，也可以是ftl文件
     * 4.将模板放入到指定目录
     * 
     * 
     * 二。关于制作模板时的循环问题
     * 在循环获取对象属性时，例如<#list userList as user> 可以使用user.name这样的写法，
     * 但是如果要嵌套循环获取user对象里的集合时，要采用user.getScoreList()的写法。
     * 
     * 三。关于模板常见问题
     * 1.如果程序在运行时出现freemarker.core.ParseException的异常，大概率是因为在写"${name}"这样的表达式时name与其前后文字样式不一致，
     * 那么在生成name时,就会有一些修饰name样式的标签,类似于${<w:color ...>name</w:color>}这样子，可以去掉${}内部的标签只留下xxx
     * 或者删掉 "${" 和 "}"然后给xxx加上el表达式都可以解决此问题。
     * 
     * 2.注意：Excel模板中ss:ExpandedRowCount后面的值对应的是模板的行数，动态加载数据时，行数一定要修改，按下面的示例修改就行了，不然生成的excel会打不开。
     * <Table ss:ExpandedColumnCount="9" ss:ExpandedRowCount="${rowList?size+4}" x:FullColumns="1" x:FullRows="1" ss:DefaultRowHeight="14.4">
     * ****************************************************************************************
     */
    public static void createDoc(Map<String,Object> dataMap,String templateName,String suffix, String savePath){
        try { 
        	
            //加载需要装填的模板
            Template template=null;
            configure=new Configuration();
            configure.setDefaultEncoding(DEFAULT_ENCODING);
            //设置模板装置方法和路径，FreeMarker支持多种模板装载方法。可以重servlet，classpath,数据库装载。
            //加载模板文件，这里用于测试直接放到了和DocUtil相同的目录下，要放到其他位置可直接修改参数
            configure.setClassForTemplateLoading(DocUtil.class, "");
            //设置异常处理器
            configure.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            //定义Template对象，注意模板类型名字与downloadType要一致
            template=configure.getTemplate(templateName+suffix);
            File outFile=new File(savePath);
            Writer out=null;
            out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), DEFAULT_ENCODING));
            long a=System.currentTimeMillis();  
            logger.info("=============开始处理word/excel文档");
            template.process(dataMap, out);
            logger.info("=============处理word/excel文档结束，执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
    
   /* public String getImageStr(String imgFile){
        InputStream in=null;
        byte[] data=null;
        try {
            in=new FileInputStream(imgFile);
            data=new byte[in.available()];
            in.read(data);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder=new BASE64Encoder();
        return encoder.encode(data);
    }*/
    
    
    /**
     * word数据组装 
     * @return void
     * *************************************关于数据的说明*******************************************
     * 大数据量的效率问题：
     * 建议所有数据都封装成一个对象放进dataMap。dataMap可以put多个参数，但是参数数量越多，freemarker解析效率越低，所以建议都封装成一个对象。
     * 当然，如果生成的doc文件较小时，可以使用put多个参数。
     * 
     * */
    public static void  assembleDocData(){
    	//DocUtil docUtil=new DocUtil();
        Map<String, Object> dataMap=new HashMap<String, Object>();
        List<UserInfo> userList = new ArrayList<UserInfo>();
        //dataMap.put("name", "Lewis");
        for (int i =0;i<1000;i++) {
			UserInfo user = new UserInfo();
			user.setName("Lewis");
			user.setIdCard("430204198305212024");
			List<ScoreInfo> scoreList = new ArrayList<ScoreInfo>();
			for(int j =0;j<10;j++){
				ScoreInfo score = new ScoreInfo();
				score.setScoreOne("100");
				score.setScoreTwo("97");
				scoreList.add(score);
			}
			user.setScoreList(scoreList);
			userList.add(user);
			
		}
        /****
        ****/
        dataMap.put("userList", userList);
        //dataMap.put("idCard", "430204198305212024");dataMap.put("name", "Lewis");
       
        /*dataMap.put("secondPic1", docUtil.getImageStr("D:\\Img\\secondPic1.png"));
        dataMap.put("secondPic2", docUtil.getImageStr("D:\\Img\\secondPic2.png"));
        dataMap.put("secondPic3", docUtil.getImageStr("D:\\Img\\secondPic3.png"));*/
        long a=System.currentTimeMillis();  
        logger.info("==============开始生成文件！");
        //注意：这里的保存文件路径仅仅是用于本机测试，实际使用时根据实际情况进行存储
        DocUtil.createDoc(dataMap, "templateDoc",".ftl", "D:\\testDoc.doc");
        logger.info("==============文件生成成功！执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
    }
    
    
    /**
     * Excel数据组装 
     * @return void
     * *************************************关于数据的说明*******************************************
     * 大数据量的效率问题：
     * 建议所有数据都封装成一个对象放进dataMap。dataMap可以put多个参数，但是参数数量越多，freemarker解析效率越低，所以建议都封装成一个对象。
     * 当然，如果生成的文件较小时，可以使用put多个参数。这里表头数据作为一个参数titleList，行数据作为一个参数rowList
     * 
     * */
    public  static void assembleExcelData(){
    	Map<String, Object> dataMap=new HashMap<String, Object>();
    	//表头数据
    	List<ExcelObj> titleList = new ArrayList<ExcelObj>();
    	for(int i = 0;i < 9; i++){
    		ExcelObj obj = new ExcelObj();
    		obj.setIndex(i+1);
    		obj.setType("String");
    		obj.setValue("表头"+(i+1));
    		titleList.add(obj);
    	}
    	
        //行数据
        List<ExcelListObj> rowList = new ArrayList<ExcelListObj>();
        for(int k = 0;k<10000;k++){
        	List<ExcelObj> row = new ArrayList<ExcelObj>();
        	 for (int i =0;i<9;i++) {
             	ExcelObj obj = new ExcelObj();
             	obj.setIndex(i+1);
             	obj.setType("String");
             	obj.setValue("属性值"+(i+1));
             	row.add(obj);
             	
     		}
        	ExcelListObj excelListObj = new ExcelListObj();
          	excelListObj.setObjList(row);
          	rowList.add(excelListObj);
        	 
        } 
        
        /****
        ****/
        dataMap.put("titleList", titleList);//表头数据
        dataMap.put("rowList", rowList);//行数据
        //dataMap.put("idCard", "430204198305212024");dataMap.put("name", "Lewis");
       
        /*dataMap.put("secondPic1", docUtil.getImageStr("D:\\Img\\secondPic1.png"));
        dataMap.put("secondPic2", docUtil.getImageStr("D:\\Img\\secondPic2.png"));
        dataMap.put("secondPic3", docUtil.getImageStr("D:\\Img\\secondPic3.png"));*/
        long a=System.currentTimeMillis();  
        logger.info("==============开始生成文件！");
        //注意：这里的保存文件路径仅仅是用于本机测试，实际使用时根据实际情况进行存储
        DocUtil.createDoc(dataMap, "templateExcel",".ftl", "D:\\testExcel.xls");
        logger.info("==============文件生成成功！执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
    }


}
