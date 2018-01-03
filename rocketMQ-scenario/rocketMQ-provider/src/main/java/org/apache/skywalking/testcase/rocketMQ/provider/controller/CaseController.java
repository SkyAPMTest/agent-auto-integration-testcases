package org.apache.skywalking.testcase.rocketMQ.provider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/case")
@Controller
public class CaseController {

    @RequestMapping("/rocketMQ-Provider")
    public String rocketMQProvider() {


        return "success";
    }
}
