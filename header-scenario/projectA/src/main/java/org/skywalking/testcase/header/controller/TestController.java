package org.skywalking.testcase.header.controller;

import java.io.IOException;
import org.skywalking.testcase.header.commons.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${url.prefix:http://127.0.0.1:18080/projectB}")
    private String projectBURL;

    @GetMapping("/test")
    public String forTest() throws IOException {
        try {
            HttpClientUtil.visitURL(projectBURL + "/test");
        } catch (Exception e) {
            logger.error("Failed to visitURL", e);
        }
        return "test";
    }
}
