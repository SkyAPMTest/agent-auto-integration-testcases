package test.apache.skywalking.apm.testcase.sofarpc.provider.service;

import test.apache.skywalking.apm.testcase.sofarpc.interfaces.SofaRpcDemoService;

public class SofaRpcDemoServiceImpl implements SofaRpcDemoService {
    @Override
    public String hello(String name) {
        return "hello, " + name;
    }
}
