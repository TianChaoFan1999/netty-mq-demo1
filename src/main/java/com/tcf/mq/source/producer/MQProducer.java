package com.tcf.mq.source.producer;

import java.net.InetSocketAddress;

import com.tcf.mq.source.entity.DevilyEntity;
import com.tcf.mq.source.util.Const;
import com.tcf.mq.source.util.MarshallingCoderFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/***
 * TODO TCF MQ消息中间件-生产者客户端，投递消息到MQ
 * @author Hasee
 *
 */
public class MQProducer {

	public static void main(String[] args) 
	{
		//TODO TCF 初始化Netty客户端
		NioEventLoopGroup workGroup=new NioEventLoopGroup();
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.group(workGroup)
		         .channel(NioSocketChannel.class)
		         .remoteAddress(new InetSocketAddress(Const.MQBrokerInfo.HOST,Const.MQBrokerInfo.PORT))
		         .handler(new ChannelInitializer<SocketChannel>() {
		        	 
		        	 @Override
		        	 protected void initChannel(SocketChannel socketChannel) throws Exception
		        	 {
		        		 //TODO TCF 绑定编码解码器
		        		 socketChannel.pipeline().addLast(MarshallingCoderFactory.buildMarshallingEncoder());
		        		 socketChannel.pipeline().addLast(MarshallingCoderFactory.buildMarshallingDecoder());
		        	 }
		        	 
				});
		
		//TODO TCF 启动Netty客户端，投递消息到MQ
		try
		{
			ChannelFuture channelFuture=bootstrap.connect().sync();
			
			System.out.println("生产者启动成功，已投递消息");
			
			//TODO TCF 生产者需要投递的消息
			String message="***测试消息***";
			
			DevilyEntity devilyEntity=new DevilyEntity(Const.MQQueueNames.QUEUE_FIRST,message,Const.NettyClientType.PRODUCER);
			
			//TODO TCF 生产者投递消息
			channelFuture.channel().writeAndFlush(devilyEntity);
			channelFuture.channel().closeFuture().sync();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//TODO TCF 关闭Netty线程组，释放占用的线程资源
			workGroup.shutdownGracefully();
		}
	}
}
