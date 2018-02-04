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

package org.apache.skywalking.testcase.grpc.consumer.controller;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.StreamObserver;
import javax.annotation.PostConstruct;
import org.apache.skywalking.testcase.gpc.proto.GreeterGrpc;
import org.apache.skywalking.testcase.gpc.proto.HelloReply;
import org.apache.skywalking.testcase.gpc.proto.HelloRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/case")
@PropertySource("application.properties")
public class CaseController {

    private Logger logger = LoggerFactory.getLogger(CaseController.class);

    @Value("${grpc.provider.host:localhost}")
    private String gprcProviderHost;
    @Value("${grpc.provider.port:18080}")
    private int grpcProviderPort;

    private ManagedChannel channel;
    private GreeterGrpc.GreeterStub greeterStub;

    @PostConstruct
    public void setUp() {
        channel = ManagedChannelBuilder.forAddress(gprcProviderHost, grpcProviderPort).usePlaintext(true).build();
        greeterStub = GreeterGrpc.newStub(channel);

    }

    @RequestMapping("/grpc-scenario")
    @ResponseBody
    public String greetService() throws InterruptedException {
        StreamObserver<HelloReply> helloReplyStreamObserver = new StreamObserver<HelloReply>() {
            public void onNext(HelloReply reply) {
                logger.info("Receive an message from provider. message: {}", reply.getMessage());
            }

            public void onError(Throwable throwable) {
            }

            public void onCompleted() {
            }
        };

        ClientCallStreamObserver<HelloRequest> helloRequestStreamObserver = (ClientCallStreamObserver<HelloRequest>)greeterStub.sayHello(helloReplyStreamObserver);

        try {
            for (int i = 0; i < 2; i++) {
                helloRequestStreamObserver.onNext(HelloRequest.newBuilder().setName("Test-" + i).build());
            }
        } catch (Exception e) {
            helloRequestStreamObserver.onError(e);
        }
        helloRequestStreamObserver.onCompleted();
        return "Success";
    }
}
