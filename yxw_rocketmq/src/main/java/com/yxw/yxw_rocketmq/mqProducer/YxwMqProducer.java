package com.yxw.yxw_rocketmq.mqProducer;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:阿倪
 * @Date: 2019/4/15 15:48
 * @Description:
 * @return:
 * @throws:
 */
@Service
public class YxwMqProducer {
    private static final String PRODUCER = "producer";
    private static final String COLONY_FLAG = ";";

    /**
     * @param producerGroup
     * @param namesrvAddr
     * @param topic
     * @param tags
     * @param body
     * @throws MQClientException 单mq
     */
    public static void producerOne(String keys, String producerGroup, String namesrvAddr, String topic, String tags, String body) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setInstanceName(PRODUCER);
        producer.start();
        try {
            Message msg = new Message(
                    // topic 主题名称
                    topic,
                    // tag 临时值
                    tags,
                    // body 内容
                    (body).getBytes()
            );
            //解决幂等性的业务唯一标识
            msg.setKeys(keys);
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.shutdown();

    }

    /**
     * mq集群情况
     *
     * @param producerGroup
     * @param namesrvAddr
     * @throws MQClientException
     */
    public static void producerColony(String producerGroup, List<String> namesrvAddr, String topic, String tags, String body) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        StringBuffer bufferNamesrvAddr = new StringBuffer();
        for (String addr : namesrvAddr) {
            bufferNamesrvAddr.append(addr);
            bufferNamesrvAddr.append(COLONY_FLAG);
        }
        String string = bufferNamesrvAddr.deleteCharAt(bufferNamesrvAddr.length() - 1).toString();
        producer.setNamesrvAddr(string);
        producer.setInstanceName(PRODUCER);
        producer.start();
        try {

            Message msg = new Message(
                    // topic 主题名称
                    topic,
                    // tag 临时值
                    tags,
                    // body 内容
                    (body).getBytes()
            );
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.shutdown();
        }

    }
}
