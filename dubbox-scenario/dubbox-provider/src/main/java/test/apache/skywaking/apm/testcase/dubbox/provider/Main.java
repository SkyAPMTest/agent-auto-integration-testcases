package test.apache.skywalking.apm.testcase.dubbox.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:dubbo-provider.xml");
        context.start();
        Thread.currentThread().join();
    }
}
