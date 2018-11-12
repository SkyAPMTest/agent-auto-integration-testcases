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

    @Value("${url.prefixC:http://127.0.0.1:28080/projectC}")
    private String projectCURL;

    @Value("${url.prefixD:http://127.0.0.1:38080/projectD}")
    private String projectDURL;

    @GetMapping("/test")
    public String forTest() throws IOException {
        try {
            HttpClientUtil.visitURL(projectCURL + "/test");
        } catch (Exception e) {
            logger.error("Failed to visitURL", e);
        }

        try {
            HttpClientUtil.visitURL(projectDURL + "/test");
        } catch (Exception e) {
            logger.error("Failed to visitURL", e);
        }
        return "test";
    }
}
