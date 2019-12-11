package com.tcf.mq.source.entity;

import java.io.Serializable;

/***
 * TODO TCF 发送消息信息类
 * @author Hasee
 *
 */
public class DevilyEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4668828944906705282L;

	//TODO TCF 队列名称
	private String queueName;
	
	//TODO TCF 发送的消息
	private String message;
	
	//TODO TCF 中间件类型：1 生产者 2 消费者
	private String connectionType;
	
	//TODO TCF 构造柱入
	public DevilyEntity(String queueName,String message,String connectionType)
	{
		this.queueName=queueName;
		this.message=message;
		this.connectionType=connectionType;
	}
	
	//TODO TCF 默认无参构造
	public DevilyEntity()
	{
		
	}
	
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}
	
}
