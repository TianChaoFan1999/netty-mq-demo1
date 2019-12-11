package com.tcf.mq.source.consumer;

import java.net.InetSocketAddress;

import com.tcf.mq.source.entity.DevilyEntity;
import com.tcf.mq.source.handle.MQConsumerEventHandle;
import com.tcf.mq.source.util.Const;
import com.tcf.mq.source.util.MarshallingCoderFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/***
 * TODO TCF MQ消费者，连接到MQ消息中间件，根据队列名称获取队列消息
 * @author Hasee
 *
 */
public class MQConsumer {

	public static void main(String[] args) 
	{
		//TODO TCF 初始化消费者Netty客户端
		NioEventLoopGroup workGroup=new NioEventLoopGroup();
		
		//TODO TCF 初始化Netty客户端信息，监听客户端初始化事件，绑定编解码器和事件驱动处理器
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.group(workGroup)
		         .channel(NioSocketChannel.class)
		         .remoteAddress(new InetSocketAddress(Const.MQBrokerInfo.HOST,Const.MQBrokerInfo.PORT))
		         .handler(new ChannelInitializer<SocketChannel>() {
		        	 
		        	 //TODO TCF 初始化Netty客户端事件
		        	 @Override
		        	 protected void initChannel(SocketChannel socketChannel) throws Exception
		        	 {
		        		 //TODO TCF 绑定编解码器和事件驱动处理器
		        		 socketChannel.pipeline().addLast(MarshallingCoderFactory.buildMarshallingEncoder());
		        		 socketChannel.pipeline().addLast(MarshallingCoderFactory.buildMarshallingDecoder());
		        		 
		        		 //TODO TCF 绑定事件驱动处理器
		        		 socketChannel.pipeline().addLast(new MQConsumerEventHandle());
		        	 }
				});
		
		//TODO TCF 初始化Netty通道，连接到MQ消息中间件，指定需要消费的队列名称
		try
		{
			ChannelFuture channelFuture=bootstrap.connect().sync();
			
			System.out.println("消费者启动成功");
			
			//TODO TCF 需要传输的对象信息，指定需要获取的消息队列名称
			DevilyEntity devilyEntity=new DevilyEntity(Const.MQQueueNames.QUEUE_FIRST,null,Const.NettyClientType.CONSUMER);
			
			//TODO TCF 连接到MQ消息中间件，发送消息
			channelFuture.channel().writeAndFlush(devilyEntity);
			channelFuture.channel().closeFuture().sync();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//TODO TCF 关闭Netty线程组，释放资源
			workGroup.shutdownGracefully();
		}
	}
}
