package org.apache.skywaking.apm.testcase.dubbox.services.impl;

import org.apache.skywaking.apm.testcase.dubbox.services.GreetService;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * Created by xin on 2016/12/6.
 */
@Service
public class GreetServiceImpl implements GreetService {
    @Override
    public String doBusiness() {
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "{\"content\":\"Hello World\"}";
    }
}
