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
 * TODO TCF MQ�����ߣ����ӵ�MQ��Ϣ�м�������ݶ������ƻ�ȡ������Ϣ
 * @author Hasee
 *
 */
public class MQConsumer {

	public static void main(String[] args) 
	{
		//TODO TCF ��ʼ��������Netty�ͻ���
		NioEventLoopGroup workGroup=new NioEventLoopGroup();
		
		//TODO TCF ��ʼ��Netty�ͻ�����Ϣ�������ͻ��˳�ʼ���¼����󶨱���������¼�����������
		Bootstrap bootstrap=new Bootstrap();
		bootstrap.group(workGroup)
		         .channel(NioSocketChannel.class)
		         .remoteAddress(new InetSocketAddress(Const.MQBrokerInfo.HOST,Const.MQBrokerInfo.PORT))
		         .handler(new ChannelInitializer<SocketChannel>() {
		        	 
		        	 //TODO TCF ��ʼ��Netty�ͻ����¼�
		        	 @Override
		        	 protected void initChannel(SocketChannel socketChannel) throws Exception
		        	 {
		        		 //TODO TCF �󶨱���������¼�����������
		        		 socketChannel.pipeline().addLast(MarshallingCoderFactory.buildMarshallingEncoder());
		        		 socketChannel.pipeline().addLast(MarshallingCoderFactory.buildMarshallingDecoder());
		        		 
		        		 //TODO TCF ���¼�����������
		        		 socketChannel.pipeline().addLast(new MQConsumerEventHandle());
		        	 }
				});
		
		//TODO TCF ��ʼ��Nettyͨ�������ӵ�MQ��Ϣ�м����ָ����Ҫ���ѵĶ�������
		try
		{
			ChannelFuture channelFuture=bootstrap.connect().sync();
			
			System.out.println("�����������ɹ�");
			
			//TODO TCF ��Ҫ����Ķ�����Ϣ��ָ����Ҫ��ȡ����Ϣ��������
			DevilyEntity devilyEntity=new DevilyEntity(Const.MQQueueNames.QUEUE_FIRST,null,Const.NettyClientType.CONSUMER);
			
			//TODO TCF ���ӵ�MQ��Ϣ�м����������Ϣ
			channelFuture.channel().writeAndFlush(devilyEntity);
			channelFuture.channel().closeFuture().sync();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//TODO TCF �ر�Netty�߳��飬�ͷ���Դ
			workGroup.shutdownGracefully();
		}
	}
}
