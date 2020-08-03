package com.fh.rabbitmq.fanout;

import com.fh.rabbitmq.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class FanoutProducer {
    private final static String EXCHANGE_NAME = "test_exchange_fanout";

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
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 消息内容
        String message = "订阅模式!";
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println(EXCHANGE_NAME+" ==== Sent '" + message + "'");
        channel.close();
        connection.close();
    }
}
