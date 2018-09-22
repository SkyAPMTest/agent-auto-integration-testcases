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

package test.apache.skywalking.testcase.activemq.controller;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.*;

@Controller
@RequestMapping("/case")
@PropertySource("classpath:application.properties")
public class CaseController {

    private Logger logger = LogManager.getLogger(CaseController.class);


    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;

    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;

    private static final String BROKEN_URL = ActiveMQConnection.DEFAULT_BROKER_URL;


    ConnectionFactory factory;

    Connection connection = null;

    Session session;

    Destination destination;

    MessageProducer messageProducer;

    MessageConsumer messageConsumer;


    @RequestMapping("/activemq-case")
    @ResponseBody
    public String activemqCase() {

        try{
            factory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEN_URL);
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("test");
            messageProducer = session.createProducer(destination);
            TextMessage message = session.createTextMessage("test");
            messageProducer.send(message);
            session.commit();
            session.close();
            connection.close();
        }catch (Exception ex){
            logger.info(ex.toString());
        }
        new ConsumerThread().start();
        return "Success";
    }

    public class ConsumerThread extends Thread {
        @Override public void run() {
            try{
                factory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEN_URL);
                connection = factory.createConnection();
                connection.start();
                session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
                destination = session.createQueue("test");
                messageConsumer = session.createConsumer(destination);
                messageConsumer.receive();
                session.close();
                connection.close();
            }catch (Exception ex){
                logger.info(ex.toString());
            }

        }
    }
}
