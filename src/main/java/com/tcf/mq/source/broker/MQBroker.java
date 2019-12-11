package com.tcf.mq.source.broker;

import java.net.InetSocketAddress;

import com.tcf.mq.source.handle.MQBrokerEventHandle;
import com.tcf.mq.source.util.Const;
import com.tcf.mq.source.util.MarshallingCoderFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/***
 * TODO TCF ��Ϣ�м��������
 * @author Hasee
 *
 */
public class MQBroker {
	
	public static void main(String[] args)
	{
		//TODO TCF Netty��ʼ��������
		NioEventLoopGroup bossGroup=new NioEventLoopGroup();
		NioEventLoopGroup workGroup=new NioEventLoopGroup();
		
		//TODO TCF ��ʼ��Netty������
		ServerBootstrap serverBootstrap=new ServerBootstrap();
		serverBootstrap.group(bossGroup,workGroup)
		               .channel(NioServerSocketChannel.class)
		               .childHandler(new ChannelInitializer<SocketChannel>() {
		            	
		            	   @Override
		            	   protected void initChannel(SocketChannel serverSocketChannel) throws Exception
		            	   {
		            		   //TODO TCF ���¼������������ͱ��������
		            		   serverSocketChannel.pipeline().addLast(MarshallingCoderFactory.buildMarshallingEncoder());
		            		   serverSocketChannel.pipeline().addLast(MarshallingCoderFactory.buildMarshallingDecoder());
		            		   
		            		   //TODO TCF ��MQ��Ϣ�м���������¼�����������
		            		   serverSocketChannel.pipeline().addLast(new MQBrokerEventHandle());
		            	   }
					   });
		
		//TODO TCF ��ʼ��ͨ��������MQ�м��������
		try
		{
			ChannelFuture channelFuture=serverBootstrap.bind(new InetSocketAddress(Const.MQBrokerInfo.HOST,Const.MQBrokerInfo.PORT));
			
			System.out.println("====MQ��Ϣ�м�������������ɹ�====");
			
			channelFuture.channel().closeFuture().sync();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//TODO TCF �ر�Netty�߳��飬�ͷ��߳���Դ
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
