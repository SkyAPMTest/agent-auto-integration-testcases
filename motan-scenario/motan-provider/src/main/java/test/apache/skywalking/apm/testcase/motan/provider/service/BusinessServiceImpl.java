package test.apache.skywalking.apm.testcase.motan.provider.service;

import test.apache.skywalking.apm.testcase.motan.interfaces.BusinessService;

public class BusinessServiceImpl implements BusinessService {
    @Override
    public String hello(String name) {
        return "hello, " + name;
    }
}
