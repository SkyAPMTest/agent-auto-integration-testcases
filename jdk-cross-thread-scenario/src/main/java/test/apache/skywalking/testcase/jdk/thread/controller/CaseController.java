package test.apache.skywalking.testcase.jdk.thread.controller;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.skywalking.apm.toolkit.trace.CallableWrapper;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/case")
@Controller
@PropertySource("application.properties")
public class CaseController {

    private static Logger logger = LogManager.getLogger(CaseController.class);

    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @RequestMapping("/cross-thread")
    @ResponseBody
    public String jdkCrossThread() throws InterruptedException, ExecutionException, TimeoutException {
        Future<String> futrue = executorService.submit(CallableWrapper.of(new Callable<String>() {
            @Override public String call() throws Exception {
                return doGet("hello");
            }
        }));
        if (futrue.get(2, TimeUnit.SECONDS).equals("hello")) {
            return "success";
        } else {
            return "fail";
        }

    }

    @RequestMapping("/echo")
    @ResponseBody
    public String echo(String str) {
        return str;
    }

    private static String doGet(String str) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("http://localhost:8080/jdk-cross-thread-scenario/case/echo?str=" + str);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(),
                    "UTF-8");
                return content;
            }
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }
        return null;
    }
}
