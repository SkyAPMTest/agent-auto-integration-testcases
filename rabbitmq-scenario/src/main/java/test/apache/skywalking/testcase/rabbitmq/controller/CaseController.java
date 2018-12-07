/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package test.apache.skywalking.testcase.rabbitmq.controller;


import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.io.IOException;

@Controller
@RequestMapping("/case")
@PropertySource("classpath:application.properties")
public class CaseController {

    private Logger logger = LogManager.getLogger(CaseController.class);


    private static final String USERNAME = "admin";

    private static final String PASSWORD = "admin";

    @Value(value = "${rabbitmq.host}")
    private String brokerUrl;

    private static final int PORT = 5672;

    private static final  String QUEUE_NAME = "test";

    private static final  String MESSAGE = "rabbitmq-testcase";

    ConnectionFactory factory;

    Connection connection = null;


    @RequestMapping("/rabbitmq-case")
    @ResponseBody
    public String rabbitmqCase() {

        try{
            factory = new ConnectionFactory();
            factory.setHost(brokerUrl==""?"127.0.0.1":brokerUrl);
            factory.setPort(PORT);
            factory.setUsername(USERNAME);
            factory.setPassword(PASSWORD);

            connection = factory.newConnection();

            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            AMQP.BasicProperties.Builder propsBuilder = new AMQP.BasicProperties.Builder();
            logger.info("发送消息中-------------->"+MESSAGE);
            channel.basicPublish("", QUEUE_NAME, propsBuilder.build(), MESSAGE.getBytes("UTF-8"));
            logger.info("发送消息后-------------->"+MESSAGE);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                logger.info("收到的消息是-------------->"+message);
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
            Thread.sleep(5000);
            logger.info("等待5秒-------------->");
            channel.close();
            connection.close();

        }catch (Exception ex){
            logger.error(ex.toString());
        }
        return "Success";
    }

}
