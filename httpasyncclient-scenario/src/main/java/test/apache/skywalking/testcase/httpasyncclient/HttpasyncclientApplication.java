package test.apache.skywalking.testcase.httpasyncclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("test.apache.skywalking.testcase.httpasyncclient")
public class HttpasyncclientApplication {

    public static void main(String[] args) {

        Object[] sources = new Object[] {HttpasyncclientApplication.class};
        SpringApplication.run(sources, args);

    }
}
