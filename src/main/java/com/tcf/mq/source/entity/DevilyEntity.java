package com.tcf.mq.source.entity;

import java.io.Serializable;

/***
 * TODO TCF ������Ϣ��Ϣ��
 * @author Hasee
 *
 */
public class DevilyEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4668828944906705282L;

	//TODO TCF ��������
	private String queueName;
	
	//TODO TCF ���͵���Ϣ
	private String message;
	
	//TODO TCF �м�����ͣ�1 ������ 2 ������
	private String connectionType;
	
	//TODO TCF ��������
	public DevilyEntity(String queueName,String message,String connectionType)
	{
		this.queueName=queueName;
		this.message=message;
		this.connectionType=connectionType;
	}
	
	//TODO TCF Ĭ���޲ι���
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
