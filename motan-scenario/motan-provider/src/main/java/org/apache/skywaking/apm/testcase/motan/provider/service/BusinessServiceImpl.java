package org.apache.skywaking.apm.testcase.motan.provider.service;

import org.apache.skywaking.apm.testcase.motan.interfaces.BusinessService;

public class BusinessServiceImpl implements BusinessService {
    @Override
    public String hello(String name) {
        return "hello, " + name;
    }
}
