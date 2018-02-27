package org.apache.skywalking.testcase.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("org.apache.skywalking.testcase.hystrix")
public class Hystrix_Application {
    public static void main(String[] args) {
        try {
            SpringApplication.run(Hystrix_Application.class, args);
        } catch (Exception e) {
            // Never do this
        }
    }
}
