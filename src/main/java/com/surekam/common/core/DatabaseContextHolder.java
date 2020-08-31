package com.surekam.common.core;

public class DatabaseContextHolder {
	
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();  
	//设置要使用的数据源  
	public static void setCustomerType(String customerType) {  
	     contextHolder.set(customerType);  
	}  
	//获取数据源  
	public static String getCustomerType() {  
	     return contextHolder.get();  
	}  
	//清除数据源，使用默认的数据源  
	public static void clearCustomerType() {  
	     contextHolder.remove();  
	}  

}
