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

package test.apache.skywalking.testcase.redisson.controller;

import org.redisson.Redisson;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/redisson")
@PropertySource("classpath:application.properties")
public class RedissonController {

    @Value(value = "${redis.host}")
    private String address;

    @RequestMapping("/redisson-case")
    @ResponseBody
    public String redissonCase() {
        Config config = new Config();

        config.useSingleServer()
                .setAddress("redis://" + address + ":6379");
        RedissonClient client = Redisson.create(config);

        RBucket<String> bucket = client.getBucket("key_a");
        bucket.set("value_a");

        RBatch batch = client.createBatch();
        batch.getBucket("batch_k_a").setAsync("batch_v_a");
        batch.getBucket("batch_k_b").setAsync("batch_v_b");
        batch.getBucket("batch_k_b").expireAsync(20, TimeUnit.SECONDS);
        batch.execute();
        return "Success";
    }

}
