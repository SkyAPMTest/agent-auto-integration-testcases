package org.apache.skywalking.testcase.hystrix.controller;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/case")
public class CaseController {

    private Logger logger = LogManager.getLogger(CaseController.class);

    @PostConstruct
    public void setUp() {
        HystrixPlugins.getInstance().registerCommandExecutionHook(new HystrixCommandExecutionHook() {
            @Override public <T> void onStart(HystrixInvokable<T> commandInstance) {
                logger.info("[hookA] onStart: " + Thread.currentThread().getId());
                super.onStart(commandInstance);
            }

            @Override public <T> void onExecutionStart(HystrixInvokable<T> commandInstance) {
                logger.info("[hookA] onExecutionStart: " + Thread.currentThread().getId());
                super.onExecutionStart(commandInstance);
            }

            @Override public <T> void onExecutionSuccess(HystrixInvokable<T> commandInstance) {
                logger.info("[hookA] onExecutionSuccess: " + Thread.currentThread().getId());
                super.onExecutionSuccess(commandInstance);
            }

            @Override public <T> Exception onExecutionError(HystrixInvokable<T> commandInstance, Exception e) {
                logger.info("[hookA] onExecutionError: " + Thread.currentThread().getId());
                return super.onExecutionError(commandInstance, e);
            }

            @Override public <T> Exception onRunError(HystrixCommand<T> commandInstance, Exception e) {
                logger.info("[hookA] onRunError: " + Thread.currentThread().getId());
                return super.onRunError(commandInstance, e);
            }
        });
    }

    @RequestMapping("/hystrix-case")
    @ResponseBody
    public String hystrixCase() throws InterruptedException, ExecutionException {
        List<Future<String>> fs = new ArrayList<Future<String>>();

        fs.add(new TestBCommand("World").queue());
        logger.info(new TestACommand("World").execute());
        for (Future<String> f : fs) {
            logger.info(f.get());
        }
        return "Success";
    }
}
