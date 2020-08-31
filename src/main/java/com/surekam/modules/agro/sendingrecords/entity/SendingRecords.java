package com.surekam.modules.agro.sendingrecords.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

import java.lang.String;

/**
 * 发送记录表(短信邮件等)Entity
 * @author luoxw
 * @version 2019-07-02
 */
@Entity
@Table(name = "sending_records")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SendingRecords extends XGXTEntity<SendingRecords> {
	
	private static final long serialVersionUID = 1L;
	private String id;//主键
	private String sendUserId;//发送用户id
	private String sendAddress;//发送地址
	private String sendType;//发送类型
	private String messageType;//内容类型
	private String sendMessage;//内容
	private String result;//回执

	public SendingRecords() {
		super();
	}
	
	@PrePersist
	public void prePersist(){
		this.id = IdGen.uuid();
	}
		
	@Id
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSendUserId() {
		return this.sendUserId;
	}
	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}
	
	public String getSendAddress() {
		return this.sendAddress;
	}
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	
	public String getSendType() {
		return this.sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	
	public String getMessageType() {
		return this.messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	public String getSendMessage() {
		return this.sendMessage;
	}
	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}
	
	public String getResult() {
		return this.result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}


