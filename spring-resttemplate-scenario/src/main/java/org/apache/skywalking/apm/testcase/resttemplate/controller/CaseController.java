package org.apache.skywalking.apm.testcase.resttemplate.controller;

import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletRequest;
import org.apache.skywalking.apm.testcase.resttemplate.entity.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CaseController {

    private static final String CREATE_URL = "/rest/";
    private static final String GET_URL = "/rest/{id}";
    private static final String UPDATE_URL = "/rest/{id}";
    private static final String DELETE_URL = "/rest/{id}";

    @RequestMapping(value = {"/async"})
    public ModelAndView asyncCase(HttpServletRequest request) {
        String baseURL = buildBaseURL(request);
        System.out.println("asyncCase " + Thread.currentThread().getId());
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        ListenableFuture<ResponseEntity<User>> createUserFuture = asyncRestTemplate.getForEntity(baseURL + GET_URL, User.class, 1);
        createUserFuture.addCallback(new ListenableFutureCallback<ResponseEntity<User>>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("asyncCase failure " + Thread.currentThread().getId());
                HttpEntity<User> userEntity = new HttpEntity<>(new User(1, "a"));
                AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
                ListenableFuture<ResponseEntity<Void>> createURLFuture = asyncRestTemplate.postForEntity(baseURL + CREATE_URL, userEntity, Void.class);
                try {
                    createURLFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                }

                HttpEntity<User> updateUserEntity = new HttpEntity<>(new User(1, "b"));
                AsyncRestTemplate updateRestTemplate = new AsyncRestTemplate();
                ListenableFuture<?> updateUserFuture = updateRestTemplate.put(baseURL + UPDATE_URL, updateUserEntity, userEntity.getBody().getId(), 1);
                updateUserFuture.addCallback(new SuccessCallback<Object>() {
                    @Override
                    public void onSuccess(Object result) {
                        System.out.println(" updateUserFuture success " + Thread.currentThread().getId());
                        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
                        ListenableFuture deleteFuture = asyncRestTemplate.delete(baseURL + DELETE_URL, 1);
                        try {
                            deleteFuture.get();
                        } catch (InterruptedException | ExecutionException e) {
                        }
                    }
                }, new FailureCallback() {
                    @Override
                    public void onFailure(Throwable ex) {
                        System.out.println("a");
                    }
                });
            }

            @Override
            public void onSuccess(ResponseEntity<User> result) {
            }
        });

        ModelAndView modelAndView = new ModelAndView("success");
        return modelAndView;
    }

    @RequestMapping("/sync")
    public ModelAndView syncCase(HttpServletRequest request) {
        String baseURL = buildBaseURL(request);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<User> userEntity = new HttpEntity<>(new User(1, "a"));
        restTemplate.postForEntity(baseURL + CREATE_URL, userEntity, Void.class);

        ResponseEntity<User> userResponseEntity = restTemplate.getForEntity(baseURL + GET_URL, User.class, 1);
        System.out.println(userResponseEntity.getBody().getId());

        HttpEntity<User> updateUserEntity = new HttpEntity<>(new User(1, "b"));
        restTemplate.put(baseURL + UPDATE_URL, updateUserEntity, updateUserEntity.getBody().getId());

        restTemplate.delete(baseURL + DELETE_URL, updateUserEntity.getBody().getId());
        ModelAndView modelAndView = new ModelAndView("success");
        return modelAndView;
    }

    private String buildBaseURL(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + 8080 + request.getContextPath();
    }

}
