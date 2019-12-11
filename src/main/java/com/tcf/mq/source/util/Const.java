package com.tcf.mq.source.util;

/***
 * TODO TCF 常量类
 * @author Hasee
 *
 */
public class Const {

	//TODO TCF MQ消息中间件-服务器运行主机和端口号
	public static class MQBrokerInfo
	{
		//TODO TCF 主机ip
		public static final String HOST="127.0.0.1";
		
		//TODO TCF 主机端口号
		public static final Integer PORT=8080;
	}
	
	//TODO TCF 生产者和消费者类型
	public static class NettyClientType
	{
		//TODO TCF 生产者
		public static final String PRODUCER="1";
		
		//TODO TCF 消费者
		public static final String CONSUMER="2";
	}
	
	//TODO TCF MQ队列名称
	public static class MQQueueNames
	{
		//TODO TCF 队列1
		public static final String QUEUE_FIRST="queue_first";
	}
}
