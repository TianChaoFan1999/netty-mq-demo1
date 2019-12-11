package com.tcf.mq.source.handle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.lang3.StringUtils;
import com.tcf.mq.source.entity.DevilyEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/***
 * TODO TCF MQ�������¼�����������
 * @author Hasee
 *
 */
public class MQBrokerEventHandle extends SimpleChannelInboundHandler<DevilyEntity> {

	//TODO TCF �����߷�������Ϣ
	private static Map<String,Queue<DevilyEntity>> queueMap=new HashMap<String,Queue<DevilyEntity>>();
	
	//TODO TCF ��Ϣ���кͶ��ĵ�������
	private static Map<String,List<ChannelHandlerContext>> consumerMap=new HashMap<String,List<ChannelHandlerContext>>();
	
	/***
	 * TODO TCF ���յ��ͻ��˷��͵���Ϣ
	 * TODO TCF ���������ȡ��Ϣ���뻺������֪ͨ����������
	 * TODO TCF ��������ֱ�Ӵӻ�������ȡ����
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,DevilyEntity devilyEntity) throws Exception
	{
		//TODO TCF ��Ϣ��������
		String queueName=devilyEntity.getQueueName();
		
		if(StringUtils.isNotEmpty(queueName))
		{
			//TODO TCF ��������ֱ�Ӱ�Ͷ�ݵ���Ϣ������в�֪ͨ���������ѣ�����������������MQ�м�������������Ĳ�ֱ������
			String connectionType=devilyEntity.getConnectionType();
			
			if(StringUtils.isNotEmpty(connectionType))
			{
				if(connectionType.equals("1"))
				{
					//TODO TCF ������
					//TODO TCF ���ݶ������ƻ�ȡ���У��������Ϊ���򴴽����з��뼯�ϣ�����ֱ��Ͷ����Ϣ
					Queue<DevilyEntity> queue=queueMap.get(queueName);
					
					if(queue==null)
					{
						queue=new LinkedBlockingQueue<DevilyEntity>();
						queueMap.put(queueName,queue);
					}
					
					//TODO TCF ��ϢͶ�ݵ�MQ���У������
					queue.offer(devilyEntity);
					
					System.out.println("===>������Ͷ�ݵ���Ϣ��"+devilyEntity.getMessage());
					
					//TODO TCF ֪ͨ���������ѣ���ȡ�����߰󶨵�MQ�м��������
					List<ChannelHandlerContext> list=consumerMap.get(queueName);
					
					if(list!=null && list.size()>0)
					{
						//TODO TCF Ͷ����Ϣ��ʱ���������Ѵ��ڣ�֪ͨ���������ѣ�ֱ�ӳ�����
						DevilyEntity data=queue.poll();
						
						System.out.println("����"+list.size()+"�������˵�ǰ���е������ߣ�֪ͨ����������");
						for(ChannelHandlerContext context:list)
						{
							if(context!=null)
							{
								context.writeAndFlush(data);
							}
						}
					}
				}
				else
				{
					//TODO TCF ������
					//TODO TCF ���ݶ������ƻ�ȡ��Ҫ���ѵĶ���
					Queue<DevilyEntity> queue=queueMap.get(queueName);
					
					//TODO TCF ���������ߺ����Ѷ���֮��Ĺ�����ϵ
					//TODO TCF �����߶�����Ϣ����
					if(consumerMap.containsKey(queueName))
					{
						consumerMap.get(queueName).add(ctx);
					}
					else
					{
						List<ChannelHandlerContext> list=new ArrayList<ChannelHandlerContext>();
						list.add(ctx);
						consumerMap.put(queueName,list);
					}
					
					if(queue!=null && queue.size()>0)
					{
						//TODO TCF ֱ�����ѣ������У����ػ�ȡ������Ϣ���е������ߣ����ѳɹ�
						devilyEntity=queue.poll();
						
						if(devilyEntity!=null)
						{
							System.out.println("�����߻�ȡ�������е���Ϣ===>"+devilyEntity.getMessage()+",����ʣ����Ϣ������"+queue.size());
							
							//TODO TCF �ѻ�ȡ�ĵ��Ķ�����Ϣ���ظ�������
							ctx.writeAndFlush(devilyEntity);
						}
					}
					else
					{
						System.out.println("û�л�ȡ����������Ϊ��"+queueName+"����Ϣ������ʹ�������߷�����Ϣ");
					}
				}
			}
		}
	}
	
}
