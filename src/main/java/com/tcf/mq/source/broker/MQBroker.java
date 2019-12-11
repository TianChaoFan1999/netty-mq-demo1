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
 * TODO TCF 消息中间件服务器
 * @author Hasee
 *
 */
public class MQBroker {
	
	public static void main(String[] args)
	{
		//TODO TCF Netty初始化服务器
		NioEventLoopGroup bossGroup=new NioEventLoopGroup();
		NioEventLoopGroup workGroup=new NioEventLoopGroup();
		
		//TODO TCF 初始化Netty服务器
		ServerBootstrap serverBootstrap=new ServerBootstrap();
		serverBootstrap.group(bossGroup,workGroup)
		               .channel(NioServerSocketChannel.class)
		               .childHandler(new ChannelInitializer<SocketChannel>() {
		            	
		            	   @Override
		            	   protected void initChannel(SocketChannel serverSocketChannel) throws Exception
		            	   {
		            		   //TODO TCF 绑定事件驱动处理器和编码解码器
		            		   serverSocketChannel.pipeline().addLast(MarshallingCoderFactory.buildMarshallingEncoder());
		            		   serverSocketChannel.pipeline().addLast(MarshallingCoderFactory.buildMarshallingDecoder());
		            		   
		            		   //TODO TCF 绑定MQ消息中间件服务器事件驱动处理器
		            		   serverSocketChannel.pipeline().addLast(new MQBrokerEventHandle());
		            	   }
					   });
		
		//TODO TCF 初始化通道，运行MQ中间件服务器
		try
		{
			ChannelFuture channelFuture=serverBootstrap.bind(new InetSocketAddress(Const.MQBrokerInfo.HOST,Const.MQBrokerInfo.PORT));
			
			System.out.println("====MQ消息中间件服务器启动成功====");
			
			channelFuture.channel().closeFuture().sync();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//TODO TCF 关闭Netty线程组，释放线程资源
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
