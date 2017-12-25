package org.apache.skywalking.testcase.jettyclient.controller;

import javax.annotation.PostConstruct;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/case")
public class CaseController {

    private HttpClient client = new HttpClient();

    @PostConstruct
    public void init() throws Exception {
        client.start();
    }

    @RequestMapping("/receiveContext-0")
    @ResponseBody
    public String receiveContextService0() throws InterruptedException {
        Thread.sleep(10);
        return "receiveContext-0";
    }

    @RequestMapping("/jettyclient-case")
    @ResponseBody
    public String jettyClientScenario() throws Exception {
        client.newRequest("http://127.0.0.1:8080/jettyclient-case/case/receiveContext-0").send();
        return "Success";
    }
}
