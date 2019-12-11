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
 * TODO TCF MQ��Ϣ�м��-�����߿ͻ��ˣ�Ͷ����Ϣ��MQ
 * @author Hasee
 *
 */
public class MQProducer {

	public static void main(String[] args) 
	{
		//TODO TCF ��ʼ��Netty�ͻ���
		NioEventLoopGroup workGroup=new NioEventLoopGroup();
		
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.group(workGroup)
		         .channel(NioSocketChannel.class)
		         .remoteAddress(new InetSocketAddress(Const.MQBrokerInfo.HOST,Const.MQBrokerInfo.PORT))
		         .handler(new ChannelInitializer<SocketChannel>() {
		        	 
		        	 @Override
		        	 protected void initChannel(SocketChannel socketChannel) throws Exception
		        	 {
		        		 //TODO TCF �󶨱��������
		        		 socketChannel.pipeline().addLast(MarshallingCoderFactory.buildMarshallingEncoder());
		        		 socketChannel.pipeline().addLast(MarshallingCoderFactory.buildMarshallingDecoder());
		        	 }
		        	 
				});
		
		//TODO TCF ����Netty�ͻ��ˣ�Ͷ����Ϣ��MQ
		try
		{
			ChannelFuture channelFuture=bootstrap.connect().sync();
			
			System.out.println("�����������ɹ�����Ͷ����Ϣ");
			
			//TODO TCF ��������ҪͶ�ݵ���Ϣ
			String message="***������Ϣ***";
			
			DevilyEntity devilyEntity=new DevilyEntity(Const.MQQueueNames.QUEUE_FIRST,message,Const.NettyClientType.PRODUCER);
			
			//TODO TCF ������Ͷ����Ϣ
			channelFuture.channel().writeAndFlush(devilyEntity);
			channelFuture.channel().closeFuture().sync();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//TODO TCF �ر�Netty�߳��飬�ͷ�ռ�õ��߳���Դ
			workGroup.shutdownGracefully();
		}
	}
}
