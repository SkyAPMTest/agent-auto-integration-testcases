package org.apache.skywalking.apm.testcase.resttemplate.controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.skywalking.apm.testcase.resttemplate.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/case")
@PropertySource("classpath:application.properties")
public class CaseController {

    private Logger logger = LogManager.getLogger(CaseController.class);

    @Value(value = "${projectB.url}")
    private String projectBURL;

    @Value(value = "${projectC.url}")
    private String projectCURL;

    @RequestMapping("/resttemplate")
    @ResponseBody
    private String resttemplateCase() {
        // Create user
        HttpEntity<User> userEntity = new HttpEntity<>(new User(1, "a"));
        new RestTemplate().postForEntity(projectBURL + "/create/", userEntity, Void.class);

        // Find User
        new RestTemplate().getForEntity(projectBURL + "/get/{id}", User.class, 1);

        //Modify user
        HttpEntity<User> updateUserEntity = new HttpEntity<>(new User(1, "b"));
        new RestTemplate().put(projectBURL + "/update/{id}", updateUserEntity, userEntity.getBody().getId(), 1);

        //Delete user
        new RestTemplate().delete(projectBURL + "/delete/{id}", 1);

        //
        Request request = new Request.Builder().url(projectCURL + "/spring3/").build();

        try {
            Response response = new OkHttpClient().newCall(request).execute();
            logger.info(response.toString());
        } catch (IOException e) {
        }

        return "resttemplateCase";
    }
}
