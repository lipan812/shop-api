package com.fh.rabbitmq.direct;

import com.fh.rabbitmq.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class DirectProducer {
    //定义交换机 路由模式   生产者
    private final static String EXCHANGE_NAME = "test_exchange_direct";

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        /*
           声明exchange
           fanout广播模式  把接收到的消息推送给所有它知道的队列
           direct 路由模式
           topic 主题模式
         */
        channel.exchangeDeclare(EXCHANGE_NAME, "direct"); //参数分别是(交换机名 , 交换机类型 , 是否持久化)
        //开启生产确认消息投递机制
        channel.confirmSelect();
        //消息内容
        String message = "路由模式123123!";
        //  routingKey="insert"   匹配路由为insert的
        if(channel.waitForConfirms()) { //生产确认消息投递  true投递成功
            channel.basicPublish(EXCHANGE_NAME, "insert", null, message.getBytes());
            System.out.println(EXCHANGE_NAME + " ==== Sent '" + message + "'");
        }
        channel.close();
        connection.close();
    }
}
