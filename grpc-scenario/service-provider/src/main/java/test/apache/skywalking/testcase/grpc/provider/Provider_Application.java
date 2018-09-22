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

package test.apache.skywalking.testcase.grpc.provider;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.local.LocalAddress;
import java.io.IOException;
import test.apache.skywalking.testcase.grpc.provider.interceptor.ProviderInterceptor;
import test.apache.skywalking.testcase.grpc.provider.service.GreeterBlockingServiceImpl;
import test.apache.skywalking.testcase.grpc.provider.service.GreeterServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider_Application {
    private static Logger logger = LoggerFactory.getLogger(Provider_Application.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info("start the server");
        Server server = NettyServerBuilder.forAddress(LocalAddress.ANY).forPort(18080).addService(ServerInterceptors.intercept(new GreeterServiceImpl(), new ProviderInterceptor()))
            .addService(ServerInterceptors.intercept(new GreeterBlockingServiceImpl(), new ProviderInterceptor())).build();
        server.start();
        server.awaitTermination();
    }
}
