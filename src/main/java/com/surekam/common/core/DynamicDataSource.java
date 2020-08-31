package com.surekam.common.core;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource{
	
	@Override  
    protected Object determineCurrentLookupKey() {
		System.out.println("----当前使用的数据源是=="+DatabaseContextHolder.getCustomerType());
        return DatabaseContextHolder.getCustomerType();   
    }  

}
