package com.surekam.modules.sys.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 功能: 子节点的信息
 */
public class AdditionalParameters
{
     /**
     * 子节点列表
     */
     private List<Item> children = new ArrayList<Item>();
    
     /**
     * 节点的Id
     */
     private String id;
    
     /**
     * 是否有选中属性
     */
     @JsonProperty( "item-selected" )
     private boolean itemSelected ;
 
     public boolean isItemSelected()
    {
          return itemSelected ;
    }
 
     public void setItemSelected( boolean itemSelected )
    {
          this .itemSelected = itemSelected;
    }
 
     public List<Item> getChildren()
    {
          return children ;
    }
 
     public void setChildren(List<Item> children )
    {
          this .children = children;
    }
 
     public String getId()
    {
          return id ;
    }
 
     public void setId(String id )
    {
          this .id = id;
    }
}

