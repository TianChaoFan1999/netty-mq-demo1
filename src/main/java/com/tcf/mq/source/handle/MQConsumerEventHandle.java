package com.tcf.mq.source.handle;

import com.tcf.mq.source.entity.DevilyEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/***
 * TODO TCF MQ������-Netty�ͻ����¼�����������
 * @author Hasee
 *
 */
public class MQConsumerEventHandle extends SimpleChannelInboundHandler<DevilyEntity> {

	//TODO TCF ���յ�MQ��Ϣ�м�����ص���Ӧ��Ϣ
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,DevilyEntity devilyEntity) throws Exception
	{
		if(devilyEntity!=null)
		{
			//TODO TCF ��ȡ������Ϣ�м����Ӧ��Ϣ
			String message=devilyEntity.getMessage();
			
			System.out.println("�����߽��յ�MQ��Ϣ�м���е���Ϣ��"+message);
		}
	}
	
}
