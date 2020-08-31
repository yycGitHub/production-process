package com.surekam.modules.sample.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.service.BaseService;
import com.surekam.common.utils.freemarker.DocUtil;
import com.surekam.modules.sample.entity.ScoreInfo;
import com.surekam.modules.sample.entity.UserInfo;

/**
 * java生成doc文件Service
 * @author liuyi
 * @version 2018-01-18
 */
@Component
@Transactional(readOnly = true)
public class DocUtilService extends BaseService {


	
	/**
	 * @param docName 文件名
	 * @param docPath 文件保存路径
	 * @return
	 */
	public String createDoc(String docName ,String docPath) {
		String result="sucssus";
		 Map<String, Object> dataMap=new HashMap<String, Object>();
	        List<UserInfo> userList = new ArrayList<UserInfo>();
	        //dataMap.put("name", "Lewis");
	        /**
	         * 数据组装 
	         * *************************************关于数据的说明*******************************************
	         * 大数据量的效率问题：
	         * 建议所有数据都封装成一个对象放进dataMap。dataMap可以put多个参数，但是参数数量越多，freemarker解析效率越低，所以建议都封装成一个对象。
	         * 当然，如果生成的doc文件较小时，可以使用put多个参数。
	         * 
	         * */
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
	        dataMap.put("userList", userList);
	        //dataMap.put("idCard", "430204198305212024");dataMap.put("name", "Lewis");
	        try{
	        	long a=System.currentTimeMillis();  
		        System.out.println("==============开始生成文件！");
		        DocUtil.createDoc(dataMap, docName,".tfl", docPath);
		        System.out.println("==============文件生成成功！执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
	        }catch(Exception e) {
	        	result = "error";
	        }
	        
		return result;
	}
	
	
	
}
