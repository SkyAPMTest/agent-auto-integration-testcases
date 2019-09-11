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

package test.apache.skywalking.testcase.kafka.controller;

import java.util.Arrays;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/case")
@PropertySource("classpath:application.properties")
public class CaseController {

    private Logger logger = LogManager.getLogger(CaseController.class);

    @Value("${bootstrap.servers:127.0.0.1:9092}")
    private String bootstrapServers;

    private String topicName;

    @PostConstruct
    private void setUp() {
        topicName = "test";
    }

    @RequestMapping("/kafka-case")
    @ResponseBody
    public String kafkaCase() {
        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", bootstrapServers);
        producerProperties.put("acks", "all");
        producerProperties.put("retries", 0);
        producerProperties.put("batch.size", 16384);
        producerProperties.put("linger.ms", 1);
        producerProperties.put("buffer.memory", 33554432);
        producerProperties.put("auto.create.topics.enable", "true");
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(producerProperties);
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName, "testKey", Integer.toString(1));
        record.headers().add("TEST", "TEST".getBytes());
        producer.send(record, new Callback() {
            @Override public void onCompletion(RecordMetadata metadata, Exception exception) {
                logger.info("send success metadata={}", metadata);
            }
        });
        producer.close();
        new ConsumerThread().start();
        return "Success";
    }

    public class ConsumerThread extends Thread {
        @Override public void run() {
            Properties consumerProperties = new Properties();
            consumerProperties.put("bootstrap.servers", bootstrapServers);
            consumerProperties.put("group.id", "testGroup");
            consumerProperties.put("enable.auto.commit", "true");
            consumerProperties.put("auto.commit.interval.ms", "1000");
            consumerProperties.put("auto.offset.reset", "earliest");
            consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);
            consumer.subscribe(Arrays.asList(topicName));
            int i = 0;
            while (i++ <= 10) {
                try {
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException e) {
                }

                ConsumerRecords<String, String> records = consumer.poll(100);

                if (!records.isEmpty()) {
                    for (ConsumerRecord<String, String> record : records) {
                        logger.info("header: {}", new String(record.headers().headers("TEST").iterator().next().value()));
                        logger.info("offset = {}, key = {}, value = {}", record.offset(), record.key(), record.value());
                    }
                    break;
                }
            }

            consumer.close();
        }
    }
}
