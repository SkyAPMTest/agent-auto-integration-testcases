package org.apache.skywalking.testcase.rocketMQ.provider.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/case")
@Controller
public class CaseController {

    private static Logger logger = LogManager.getLogger(CaseController.class);

    @RequestMapping("/rocketMQ-Provider")
    public String rocketMQProvider() throws UnsupportedEncodingException, MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("example_group_name");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        String[] tags = new String[] {"TagA", "TagB"};
        for (int i = 0; i < 3; i++) {
            int orderId = i % 10;
            Message msg = new Message("TopicTest", tags[i % tags.length], "KEY" + i,
                ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer)arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderId);

            logger.info("{} %n", sendResult);
        }

        return "success";
    }
}
