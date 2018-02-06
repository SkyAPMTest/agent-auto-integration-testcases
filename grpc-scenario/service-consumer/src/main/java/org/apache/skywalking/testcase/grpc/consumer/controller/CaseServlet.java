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

import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.skywalking.testcase.gpc.proto.GreeterBlockingGrpc;
import org.apache.skywalking.testcase.gpc.proto.GreeterGrpc;
import org.apache.skywalking.testcase.gpc.proto.HelloReply;
import org.apache.skywalking.testcase.gpc.proto.HelloRequest;
import org.apache.skywalking.testcase.grpc.consumer.interceptor.ConsumerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaseServlet extends HttpServlet {

    private String gprcProviderHost;
    private int grpcProviderPort;
    private Logger logger = LoggerFactory.getLogger(CaseServlet.class);

    private ManagedChannel channel;
    private GreeterGrpc.GreeterStub greeterStub;
    private GreeterBlockingGrpc.GreeterBlockingBlockingStub greeterBlockingStub;

    @Override public void init() throws ServletException {
        super.init();
        gprcProviderHost = System.getProperty("grpc.provider.host", "127.0.0.1");
        logger.info("gprcProviderHost : {}", gprcProviderHost);
        grpcProviderPort = 18080;
        channel = ManagedChannelBuilder.forAddress(gprcProviderHost, grpcProviderPort).usePlaintext(true).build();
        greeterStub = GreeterGrpc.newStub(ClientInterceptors.intercept(channel, new ConsumerInterceptor()));
        greeterBlockingStub = GreeterBlockingGrpc.newBlockingStub(ClientInterceptors.intercept(channel, new ConsumerInterceptor()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            PrintWriter printWriter = resp.getWriter();
            printWriter.write(greetService());
            printWriter.flush();
            printWriter.close();
        } catch (InterruptedException e) {
            logger.error("Failed to call grpc server", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private static List<String> names() {
        return Arrays.asList(
            "Sophia",
            "Jackson");
    }

    public String greetService() throws InterruptedException {
        ClientResponseObserver<HelloRequest, HelloReply> helloReplyStreamObserver = new ClientResponseObserver<HelloRequest, HelloReply>() {
            private ClientCallStreamObserver<HelloRequest> requestStream;

            @Override
            public void beforeStart(ClientCallStreamObserver observer) {
                this.requestStream = observer;
                this.requestStream.setOnReadyHandler(new Runnable() {
                    Iterator<String> iterator = names().iterator();

                    @Override
                    public void run() {
                        while (requestStream.isReady()) {
                            if (iterator.hasNext()) {
                                String name = iterator.next();
                                HelloRequest request = HelloRequest.newBuilder().setName(name).build();
                                requestStream.onNext(request);
                            } else {
                                requestStream.onCompleted();
                            }
                        }
                    }
                });
            }

            @Override public void onNext(HelloReply reply) {
                logger.info("Receive an message from provider. message: {}", reply.getMessage());
                requestStream.request(1);
            }

            public void onError(Throwable throwable) {
                logger.error("Failed to send data", throwable);
            }

            public void onCompleted() {
                logger.info("All Done");
            }
        };

        greeterStub.sayHello(helloReplyStreamObserver);
        return "Success";
    }
}
