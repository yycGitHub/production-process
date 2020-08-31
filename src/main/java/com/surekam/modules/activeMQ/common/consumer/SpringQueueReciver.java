package com.surekam.modules.activeMQ.common.consumer;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import com.surekam.common.mapper.JsonMapper;
import com.surekam.modules.activeMQ.syn.common.MessageData;
import com.surekam.modules.activeMQ.syn.service.SynClientServer;

@Component
public class SpringQueueReciver extends MessageListenerAdapter {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SynClientServer synClientServer;
	
	@JmsListener(destination = "${sysName}_areaSyn" , containerFactory = "jmsListenerContainerFactory")
	public void onAreaMessage(Message message, Session session) throws JMSException {		
		try {
			MessageData messageData= this.getMessageData(message);
			try {
				String jsonData = messageData.getJsonData();
				boolean flag = synClientServer.areaSyn(jsonData);
				if (flag) {
					this.rebackTextMessage(message, session, messageData.getId(),"1",null);//同步正常
				}else{
					this.rebackTextMessage(message, session, messageData.getId(),"2",null);//同步错误
				}
			} catch (Exception e) {
				this.rebackTextMessage(message, session, messageData.getId(),"2",e.getMessage());
			}
			message.acknowledge();
		} catch (Exception e) {
			logger.error("区域同步错误：",e);
		}
	}
	
	@JmsListener(destination = "${sysName}_officeSyn" , containerFactory = "jmsListenerContainerFactory")
	public void onOfficeMessage(Message message, Session session) throws JMSException {
		try {
			MessageData messageData= this.getMessageData(message);
			try {
				String jsonData = messageData.getJsonData();
				boolean flag = synClientServer.officeSyn(jsonData);
				if (flag) {
					this.rebackTextMessage(message, session, messageData.getId(),"1",null);//同步正常
				}else{
					this.rebackTextMessage(message, session, messageData.getId(),"2",null);//同步错误
				}
			} catch (Exception e) {
				this.rebackTextMessage(message, session, messageData.getId(),"2",e.getMessage());
			}
			message.acknowledge();
		} catch (Exception e) {
			logger.error("机构同步错误：",e);
		}
	}
	
	@JmsListener(destination = "${sysName}_menuSyn" , containerFactory = "jmsListenerContainerFactory")
	public void onMenuMessage(Message message, Session session) throws JMSException {
		try {
			MessageData messageData= this.getMessageData(message);
			try {
				String jsonData = messageData.getJsonData();
				boolean flag = synClientServer.menuSyn(jsonData);
				if (flag) {
					this.rebackTextMessage(message, session, messageData.getId(),"1",null);//同步正常
				}else{
					this.rebackTextMessage(message, session, messageData.getId(),"2",null);//同步错误
				}
			} catch (Exception e) {
				this.rebackTextMessage(message, session, messageData.getId(),"2",e.getMessage());
			}
			message.acknowledge();
		} catch (Exception e) {
			logger.error("菜单同步错误：",e);
		}
	}
	
	@JmsListener(destination = "${sysName}_roleSyn" , containerFactory = "jmsListenerContainerFactory")
	public void onRoleMessage(Message message, Session session) throws JMSException {
		try {
			MessageData messageData= this.getMessageData(message);
			try {
				String jsonData = messageData.getJsonData();
				boolean flag = synClientServer.roleSyn(jsonData);
				if (flag) {
					this.rebackTextMessage(message, session, messageData.getId(),"1",null);//同步正常
				}else{
					this.rebackTextMessage(message, session, messageData.getId(),"2",null);//同步错误
				}
			} catch (Exception e) {
				this.rebackTextMessage(message, session, messageData.getId(),"2",e.getMessage());
			}
			message.acknowledge();
		} catch (Exception e) {
			logger.error("角色同步错误：",e);
		}
	}
	
	@JmsListener(destination = "${sysName}_userSyn" , containerFactory = "jmsListenerContainerFactory")
	public void onMessage(Message message, Session session) throws JMSException {
		try {
			MessageData messageData= this.getMessageData(message);
			try {
				String jsonData = messageData.getJsonData();
				boolean flag = synClientServer.userSyn(jsonData);
				if (flag) {
					this.rebackTextMessage(message, session, messageData.getId(),"1",null);//同步正常
				}else{
					this.rebackTextMessage(message, session, messageData.getId(),"2",null);//同步错误
				}
			} catch (Exception e) {
				this.rebackTextMessage(message, session, messageData.getId(),"2",e.getMessage());
			}
	        message.acknowledge();
		} catch (Exception e) {
			logger.error("用户同步错误：",e);
		}
	}
	
	private MessageData getMessageData(Message message) throws Exception{
		JsonMapper jsonMapper = JsonMapper.getInstance();
		String bean = (String)getMessageConverter().fromMessage(message);
		MessageData messageData= jsonMapper.fromJson(bean.toString(), MessageData.class);
		return messageData;
	}
	
	private void rebackTextMessage(Message message, Session session ,String messageId,String status ,String error) throws Exception{
        // 创建回执消息
		Destination recall_destination = message.getJMSReplyTo();
		String json = "{\"messageId\":\""+messageId+"\",\"status\":\""+status+"\",\"error\":\""+error+"\"}";
        TextMessage textMessage = session.createTextMessage(json);
        MessageProducer producer = session.createProducer(recall_destination);  
        producer.send(textMessage);
	}
}