package test.apache.skywalking.testcase.feign.controller;

import feign.Body;
import feign.Feign;
import feign.Headers;
import feign.Logger;
import feign.Param;
import feign.RequestLine;
import feign.codec.Decoder;
import feign.gson.GsonDecoder;
import test.apache.skywalking.testcase.feign.entity.User;

public interface RestRequest {

    @RequestLine("GET /get/{id}")
    User getById(@Param("id") int id);

    @RequestLine("POST /create/")
    @Headers("Content-Type: application/json")
    @Body("%7B\"id\": \"{id}\", \"userName\": \"{userName}\"%7D")
    void createUser(@Param("id") int id, @Param("userName") String userName);

    @RequestLine("PUT /update/{id}")
    @Headers("Content-Type: application/json")
    @Body("%7B\"id\": \"{id}\", \"userName\": \"{userName}\"%7D")
    User updateUser(@Param("id") int id, @Param("userName") String userName);

    @RequestLine("DELETE /delete/{id}")
    void deleteUser(@Param("id") int id);

    static RestRequest connect() {
        Decoder decoder = new GsonDecoder();
        return Feign.builder()
            .decoder(decoder)
            .logger(new Logger.ErrorLogger())
            .logLevel(Logger.Level.BASIC)
            .target(RestRequest.class, "http://localhost:8080/feign-case");
    }
}
