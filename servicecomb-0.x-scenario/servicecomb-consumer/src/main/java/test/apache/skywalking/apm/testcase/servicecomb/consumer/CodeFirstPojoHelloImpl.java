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

package test.apache.skywalking.apm.testcase.servicecomb.consumer;

import io.servicecomb.provider.pojo.RpcReference;
import io.servicecomb.provider.pojo.RpcSchema;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import test.apache.skywalking.apm.testcase.servicecomb.schemma.Hello;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RpcSchema(schemaId = "codeFirstSpringmvcHello")
@RequestMapping(path = "/servicecomb", produces = MediaType.APPLICATION_JSON)
public class CodeFirstPojoHelloImpl {
    private static Logger logger = Logger.getLogger(CodeFirstPojoHelloImpl.class);
    @RpcReference(microserviceName = "codefirst", schemaId = "codeFirstHello")
    private static Hello hello;

    @RequestMapping(path = "/case", method = RequestMethod.GET)
    public String say() {
        String repo = " sayHi invoke filed";
        try {
            repo = hello.sayHi("Java Chassis");
        } catch (Exception e) {
            logger.error("sayHi invoke filed");
        }
        return repo;
    }
}
