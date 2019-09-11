package test.apache.skywalking.apm.testcase.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestFilterConfig {

    @Bean
    public Test1Filter test1Filter(){
        return new Test1Filter();
    }

    @Bean
    public Test2Filter test2Filter(){
        return new Test2Filter();
    }
}
