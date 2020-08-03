package com.fh.rabbitmq.direct;

import com.fh.rabbitmq.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class DirectConsumer {

    private final static String QUEUE_NAME = "test_exchange_direct_2"; //定义队列名称

    private final static String EXCHANGE_NAME = "test_exchange_direct";//定义交换机名称

    public static void main(String[] argv) throws Exception {

        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //定义交换机模式  // 声明exchange     fanout广播模式    redirect 路由模式    topic 主题模式
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 绑定队列到交换机  路由为update  和 insert
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "update");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "insert"); //******
        // 同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);
        // 定义队列的消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 监听队列，手动返回完成
        channel.basicConsume(QUEUE_NAME, false, consumer);
        // 获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("路由模式-消费者1  " + EXCHANGE_NAME+"    "+QUEUE_NAME+" ====  Received 2 '" + message + "'");
            //创建我们的监听的消息 auto Ack 默认自动签收  必须手动ack
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }
}
