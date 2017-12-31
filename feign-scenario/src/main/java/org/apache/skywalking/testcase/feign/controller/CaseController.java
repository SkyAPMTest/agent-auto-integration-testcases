package org.apache.skywalking.testcase.feign.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.skywalking.testcase.feign.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/case")
public class CaseController {

    private Logger logger = LogManager.getLogger(CaseController.class);

    @ResponseBody
    @RequestMapping("/feign")
    public String feignCase() {
        RestRequest request = RestRequest.connect();
        request.createUser(1, "test");
        User user = request.getById(1);
        logger.info("find Id[{}] user. User name is {} ", user.getId(), user.getUserName());
        request.updateUser(1, "testA");
        request.deleteUser(1);
        return "success";
    }
}
