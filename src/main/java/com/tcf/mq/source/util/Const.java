package com.tcf.mq.source.util;

/***
 * TODO TCF ������
 * @author Hasee
 *
 */
public class Const {

	//TODO TCF MQ��Ϣ�м��-���������������Ͷ˿ں�
	public static class MQBrokerInfo
	{
		//TODO TCF ����ip
		public static final String HOST="127.0.0.1";
		
		//TODO TCF �����˿ں�
		public static final Integer PORT=8080;
	}
	
	//TODO TCF �����ߺ�����������
	public static class NettyClientType
	{
		//TODO TCF ������
		public static final String PRODUCER="1";
		
		//TODO TCF ������
		public static final String CONSUMER="2";
	}
	
	//TODO TCF MQ��������
	public static class MQQueueNames
	{
		//TODO TCF ����1
		public static final String QUEUE_FIRST="queue_first";
	}
}
