package org.apache.skywaking.apm.testcase.sofarpc.provider.service;

import org.apache.skywaking.apm.testcase.sofarpc.interfaces.SofaRpcDemoService;

public class SofaRpcDemoServiceImpl implements SofaRpcDemoService {
    @Override
    public String hello(String name) {
        return "hello, " + name;
    }
}
