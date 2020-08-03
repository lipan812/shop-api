package com.fh.rabbitmq.fanout;

import com.fh.rabbitmq.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class FanoutConsumer1 {
    //发布订阅模式   消费者
    private final static String QUEUE_NAME = "test_queue_work1"; //定义队列名称

    private final static String EXCHANGE_NAME = "test_exchange_fanout";//定义交换机名称

    public static void main(String[] argv) throws Exception {

        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //定义交换机模式  // 声明exchange     fanout广播模式    redirect 路由模式    topic 主题模式
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 声明队列  参数依次 队列名称  是否持久化  是否排它(仅此连接) 是否自动删除  其他构造参数
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 绑定队列到交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
        // 同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);
        // 定义队列的消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 监听队列，false表示手动返回完成状态，true表示自动  false表示手动返回ack
        channel.basicConsume(QUEUE_NAME, false, consumer);
        // 获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("发布订阅模式-消费者1 " + EXCHANGE_NAME+"    "+QUEUE_NAME+" ====  Received 1 '" + message + "'");
            //创建我们的监听的消息 auto Ack 默认自动签收  必须手动ack
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }
}
