/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sureserve/surekam">surekam</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.surekam.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.surekam.common.persistence.DataEntity;

/**
 * 生产许可资料
 * @author wangyuewen
 *
 */
@Entity
@Table(name = "sys_production_certificate")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductionCertificate extends DataEntity<ProductionCertificate> {

	private static final long serialVersionUID = 1L;
	
	private String officeId;
	private String number;
	private String socialCreditCode;
	private String address;
	private String type;
	private String foundedDate;
	private String businessTerm;
	private String image;
	
	public ProductionCertificate(){
		super();
	}
	
	public ProductionCertificate(String id){
		this();
		this.id = id;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSocialCreditCode() {
		return socialCreditCode;
	}

	public void setSocialCreditCode(String socialCreditCode) {
		this.socialCreditCode = socialCreditCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFoundedDate() {
		return foundedDate;
	}

	public void setFoundedDate(String foundedDate) {
		this.foundedDate = foundedDate;
	}

	public String getBusinessTerm() {
		return businessTerm;
	}

	public void setBusinessTerm(String businessTerm) {
		this.businessTerm = businessTerm;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	
	
	
}