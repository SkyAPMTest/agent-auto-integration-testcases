package org.apache.skywaking.apm.testcase.sofarpc.consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.skywaking.apm.testcase.sofarpc.interfaces.SofaRpcDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sofarpc-consumer/case")
public class CaseController {

    private Logger logger = LogManager.getLogger(CaseController.class);

    @Autowired
    private SofaRpcDemoService sofaRpcDemoService;

    @RequestMapping("/sofarpc-case")
    @ResponseBody
    public String sofaRpcCase() {

        String result = sofaRpcDemoService.hello("world");
        return result;
    }
}
