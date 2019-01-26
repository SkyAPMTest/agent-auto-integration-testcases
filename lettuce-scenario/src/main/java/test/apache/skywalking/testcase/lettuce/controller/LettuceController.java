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

package test.apache.skywalking.testcase.lettuce.controller;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/lettuce")
@PropertySource("classpath:application.properties")
public class LettuceController {

    private Logger logger = LoggerFactory.getLogger(LettuceController.class);

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder().connectTimeout(6, TimeUnit.SECONDS)
            .readTimeout(6, TimeUnit.SECONDS).writeTimeout(6, TimeUnit.SECONDS).build();

    @Value(value = "${redis.host}")
    private String address;

    @RequestMapping("/lettuce-case")
    @ResponseBody
    public String lettuceCase() throws ExecutionException, InterruptedException {
        RedisClient redisClient = RedisClient.create("redis://" + address + ":6379");
        StatefulRedisConnection<String, String> connection0 = redisClient.connect();
        RedisCommands<String, String> syncCommand = connection0.sync();
        syncCommand.get("key");

        /**
         * Increasing the asynchronous method will cause the testcase to return too much uncertain data.
         *
         * RedisAsyncCommands<String, String> asyncCommands0 = connection0.async();
         * AsyncCommand<String, String, String> future = (AsyncCommand<String, String, String>) asyncCommands0.set("key_a", "value_a");
         *
         * future.onComplete(s -> {
         *     Request.Builder builder3 = new Request.Builder().url("http://skywalking.apache.org/url");
         *     try {
         *         Response response3 = OK_HTTP_CLIENT.newCall(builder3.build()).execute();
         *         response3.close();
         *     } catch (IOException e) {
         *         logger.error("e:", e);
         *     }
         * });
         *
         * future.get();
         */

        StatefulRedisConnection<String, String> connection1 = redisClient.connect();
        RedisAsyncCommands<String, String> asyncCommands = connection1.async();
        asyncCommands.setAutoFlushCommands(false);
        List<RedisFuture<?>> futures = new ArrayList<>();
        futures.add(asyncCommands.set("key0", "value0"));
        futures.add(asyncCommands.set("key1", "value1"));
        asyncCommands.flushCommands();
        LettuceFutures.awaitAll(5, TimeUnit.SECONDS, futures.toArray(new RedisFuture[futures.size()]));

        connection0.close();
        connection1.close();
        redisClient.shutdown();
        return "Success";
    }

}
