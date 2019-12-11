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
 * TODO TCF MQ服务器事件驱动处理器
 * @author Hasee
 *
 */
public class MQBrokerEventHandle extends SimpleChannelInboundHandler<DevilyEntity> {

	//TODO TCF 生产者发布的消息
	private static Map<String,Queue<DevilyEntity>> queueMap=new HashMap<String,Queue<DevilyEntity>>();
	
	//TODO TCF 消息队列和订阅的消费者
	private static Map<String,List<ChannelHandlerContext>> consumerMap=new HashMap<String,List<ChannelHandlerContext>>();
	
	/***
	 * TODO TCF 接收到客户端发送的消息
	 * TODO TCF 生产者则读取消息存入缓冲区，通知消费者消费
	 * TODO TCF 消费者则直接从缓冲区读取数据
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,DevilyEntity devilyEntity) throws Exception
	{
		//TODO TCF 消息队列名称
		String queueName=devilyEntity.getQueueName();
		
		if(StringUtils.isNotEmpty(queueName))
		{
			//TODO TCF 生产者则直接把投递的消息放入队列并通知消费者消费，如果是消费者则关联MQ中间件服务器上下文并直接消费
			String connectionType=devilyEntity.getConnectionType();
			
			if(StringUtils.isNotEmpty(connectionType))
			{
				if(connectionType.equals("1"))
				{
					//TODO TCF 生产者
					//TODO TCF 根据队列名称获取队列，如果队列为空则创建队列放入集合，否则直接投递消息
					Queue<DevilyEntity> queue=queueMap.get(queueName);
					
					if(queue==null)
					{
						queue=new LinkedBlockingQueue<DevilyEntity>();
						queueMap.put(queueName,queue);
					}
					
					//TODO TCF 消息投递到MQ队列，入队列
					queue.offer(devilyEntity);
					
					System.out.println("===>生产者投递的消息："+devilyEntity.getMessage());
					
					//TODO TCF 通知消费者消费，获取消费者绑定的MQ中间件上下文
					List<ChannelHandlerContext> list=consumerMap.get(queueName);
					
					if(list!=null && list.size()>0)
					{
						//TODO TCF 投递消息的时候消费者已存在，通知消费者消费，直接出队列
						DevilyEntity data=queue.poll();
						
						System.out.println("发现"+list.size()+"个订阅了当前队列的消费者，通知消费者消费");
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
					//TODO TCF 消费者
					//TODO TCF 根据队列名称获取需要消费的队列
					Queue<DevilyEntity> queue=queueMap.get(queueName);
					
					//TODO TCF 建立消费者和消费队列之间的关联关系
					//TODO TCF 消费者订阅消息队列
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
						//TODO TCF 直接消费，出队列，返回获取到的消息队列到消费者，消费成功
						devilyEntity=queue.poll();
						
						if(devilyEntity!=null)
						{
							System.out.println("消费者获取到缓存中的消息===>"+devilyEntity.getMessage()+",队列剩余消息数量："+queue.size());
							
							//TODO TCF 把获取的到的队列消息返回给消费者
							ctx.writeAndFlush(devilyEntity);
						}
					}
					else
					{
						System.out.println("没有获取到队列名称为："+queueName+"的消息，请先使用生产者发布消息");
					}
				}
			}
		}
	}
	
}
