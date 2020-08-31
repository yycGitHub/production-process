/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.common.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.collections.iterators.UniqueFilterIterator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.surekam.modules.sys.entity.Group;
import com.surekam.modules.sys.entity.Role;
import com.surekam.modules.sys.entity.User;

/**
 * MyPredicate类
 * @author liuyi
 * @version 2018-03-26
 */
public class MyPredicate implements Predicate {  
	  
    private String property;  
  
    private Object value;  
  
    public MyPredicate(String property, Object value) {  
        this.property = property;  
        this.value = value;  
    }  
  
    public boolean evaluate(Object object) {  
        // TODO Auto-generated method stub  
        try {  
            Object beanValue;  
            if (property.indexOf(".") > 0) {  
                beanValue = PropertyUtils.getNestedProperty(object, property);  
            } else {  
                beanValue = PropertyUtils.getProperty(object, property);  
            }  
            if (beanValue == null) {  
                return false;  
            }  
            if (!value.getClass().equals(beanValue.getClass())) {  
                throw new RuntimeException("value.class!=beanValue.class");  
            }  
            return myCompare(beanValue, value);  
  
        } catch (Exception e) {  
            throw new RuntimeException(e.getMessage(), e.getCause());  
        }  
    }  
  
    private boolean myCompare(Object value, Object beanValue) {  
        if (beanValue.getClass().equals(Integer.class)) {  
            if (((Integer) beanValue).equals(value)) {  
                return true;  
            }  
        }  
        if (beanValue.getClass().equals(BigDecimal.class)) {  
            if (((BigDecimal) beanValue).compareTo((BigDecimal) value) == 0) {  
                return true;  
            }  
        }  
        if (beanValue.getClass().equals(String.class)) {  
            if (beanValue.toString().equals(value.toString())) {  
                return true;  
            }  
        }  
        return false;  
    }  
  
}  
