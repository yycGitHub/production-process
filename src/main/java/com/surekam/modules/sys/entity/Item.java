package com.surekam.modules.sys.entity;

/**
 * 功能: 节点的信息
 */
public class Item
{
     /**
     * 节点的名字
     */
     private String text ;
    
     /**
     * 节点的类型："item":文件  "folder":目录
     */
     private String type ;
     
     /**
      * 节点的Id
      */
      private String id;
    
     /**
     * 子节点的信息
     */
     private AdditionalParameters additionalParameters ;
 
     public String getText()
    {
          return text ;
    }
 
     public void setText(String text )
    {
          this .text = text;
    }
 
     public String getType()
    {
          return type ;
    }
 
     public void setType(String type )
    {
          this .type = type;
    }
 
     public AdditionalParameters getAdditionalParameters()
    {
          return additionalParameters ;
    }
 
     public void setAdditionalParameters(AdditionalParameters additionalParameters )
    {
          this .additionalParameters = additionalParameters ;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
     
     
}
