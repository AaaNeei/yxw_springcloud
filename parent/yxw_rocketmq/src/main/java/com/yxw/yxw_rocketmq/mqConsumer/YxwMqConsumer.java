package com.yxw.yxw_rocketmq.mqConsumer;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:阿倪
 * @Date: 2019/4/15 16:07
 * @Description:
 * @return:
 * @throws:
 */
@Service
public class YxwMqConsumer {

    private static final String CONSUMER="consumer";



    public void consumerOne(String producerGroup, String namesrvAddr, String topic, String tags) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(producerGroup);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setInstanceName(CONSUMER);
        consumer.subscribe(topic, tags);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {

                    String keys = msg.getKeys();
                    System.out.println(msg.getMsgId()+"---"+new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("Consumer Started.");

    }




}
