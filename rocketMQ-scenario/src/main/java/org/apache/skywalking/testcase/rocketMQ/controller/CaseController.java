package org.apache.skywalking.testcase.rocketMQ.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/case")
@Controller
@PropertySource("application.properties")
public class CaseController {

    private static Logger logger = LogManager.getLogger(CaseController.class);

    private DefaultMQProducer producer;

    @Value(value = "${rocketMQ.nameSrv:localhost:9876}")
    private String nameSrv;

    @PostConstruct
    public void setUp() throws MQClientException {

    }

    @RequestMapping("/rocketMQ-Provider")
    @ResponseBody
    public String rocketMQProvider() throws UnsupportedEncodingException, MQClientException, RemotingException, InterruptedException, MQBrokerException {
        producer = new DefaultMQProducer(UUID.randomUUID().toString());
        producer.setNamesrvAddr(nameSrv);
        producer.start();
        Message msg = new Message("TopicTest", "TagA", "KEY",
            ("Hello RocketMQ ").getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult sendResult = producer.send(msg);

        logger.info("{} %n", sendResult);

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UUID.randomUUID().toString());
        consumer.setPullInterval(10);
        consumer.setNamesrvAddr(nameSrv);
        consumer.subscribe("TopicTest", "*");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                logger.info("{} Receive New Messages: {} %n", Thread.currentThread().getName(), msgs);
                return ConsumeOrderlyStatus.SUCCESS;

            }
        });

        consumer.start();

        return "success";
    }
}
