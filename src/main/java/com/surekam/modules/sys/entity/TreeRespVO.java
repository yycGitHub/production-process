package com.surekam.modules.sys.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能: 数据返回对象
 */
public class TreeRespVO
{
     private List<Item>  data = new ArrayList<Item>();
     private String status;/** 状态码 */
     private String message;/** 提示信息 */
     
     public List<Item> getData()
    {
          return data ;
    }
 
     public void setData(List<Item> data )
    {
          this .data = data;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    
    
}

