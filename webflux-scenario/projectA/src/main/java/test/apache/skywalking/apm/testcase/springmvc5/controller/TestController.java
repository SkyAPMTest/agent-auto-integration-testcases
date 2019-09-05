/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package test.apache.skywalking.apm.testcase.springmvc5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.apache.skywalking.apm.testcase.springmvc5.utils.HttpUtils;

import java.io.IOException;

@RestController
public class TestController {

    @Value("${projectB.host:localhost:18080}")
    private String hostBAddress;

    @Autowired
    private HttpUtils httpUtils;

    @RequestMapping("/testcase")
    public String testcase() throws IOException {
            visit("http://" + hostBAddress + "/testcase/annotation/success");
            visit("http://" + hostBAddress + "/testcase/annotation/error");
            visit("http://" + hostBAddress + "/testcase/route/success");
            visit("http://" + hostBAddress + "/testcase/route/error");
            visit("http://" + hostBAddress + "/notFound");
            visit("http://" + hostBAddress + "/testcase/annotation/mono/hello");
            return "test";
    }

    private void visit(String path){
        try {
            httpUtils.visit(path);
        } catch (Exception i) {

        }
    }
}
