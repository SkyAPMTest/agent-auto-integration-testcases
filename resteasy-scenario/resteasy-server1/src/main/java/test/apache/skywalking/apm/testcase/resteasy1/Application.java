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

package test.apache.skywalking.apm.testcase.resteasy1;

import org.jboss.resteasy.plugins.server.netty.NettyContainer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import test.apache.skywalking.apm.testcase.resteasy1.controller.ServerController;

import java.util.Collections;

/**
 * @author yan-fucheng
 */
public class Application {

    public static void main(String[] args) throws Exception {
        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.setResources(Collections.<Object>singletonList(new ServerController()));
        NettyContainer.start("/resteasy-server1-case", null, deployment);
    }
}
