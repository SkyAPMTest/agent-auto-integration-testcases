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

package test.apache.skywalking.testcase.toolkit.trace.controller;

import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.stereotype.Component;

/**
 * @author caoyixiong
 */
@Component
public class TestService {

    @Trace
    public void testError() {
        ActiveSpan.error();
    }

    @Trace
    public void testErrorMsg() {
        ActiveSpan.error("TestErrorMsg");
    }

    @Trace
    public void testErrorThrowable() {
        ActiveSpan.error(new RuntimeException("Test-Exception"));
    }

    @Trace
    public void testDebug() {
        ActiveSpan.debug("TestDebugMsg");
    }

    @Trace
    public void testInfo() {
        ActiveSpan.info("TestInfoMsg");
    }
}
