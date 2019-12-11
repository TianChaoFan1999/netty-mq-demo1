package com.tcf.mq.source.handle;

import com.tcf.mq.source.entity.DevilyEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/***
 * TODO TCF MQ消费者-Netty客户端事件驱动处理器
 * @author Hasee
 *
 */
public class MQConsumerEventHandle extends SimpleChannelInboundHandler<DevilyEntity> {

	//TODO TCF 接收到MQ消息中间件返回的响应信息
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,DevilyEntity devilyEntity) throws Exception
	{
		if(devilyEntity!=null)
		{
			//TODO TCF 获取到的消息中间件响应信息
			String message=devilyEntity.getMessage();
			
			System.out.println("消费者接收到MQ消息中间件中的消息："+message);
		}
	}
	
}
