package test.apache.skywalking.testcase.jdk.http.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
@RequestMapping("/case")
public class CaseController {


    @RequestMapping("/receiveContext-0")
    @ResponseBody
    public String receiveContextService0() throws InterruptedException {
        Thread.sleep(2 * 1000);
        return "receiveContext-0";
    }

    @RequestMapping("/jdk-http-case")
    @ResponseBody
    public String jdkHttp() throws IOException {
        URL url = new URL("http://127.0.0.1:8080/jdk-http-case/case/receiveContext-0");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("key","value");
        int responseCode = connection.getResponseCode();
        return "Success:" + responseCode;
    }
}
