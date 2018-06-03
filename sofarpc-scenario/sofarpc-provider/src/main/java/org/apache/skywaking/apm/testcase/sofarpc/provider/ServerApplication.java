package org.apache.skywaking.apm.testcase.sofarpc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@ImportResource({"classpath:sofarpc_provider.xml"})
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {

        System.setProperty("server.port", "8081");

        SpringApplication springApplication = new SpringApplication(ServerApplication.class);
        ApplicationContext applicationContext = springApplication.run(args);
        System.out.println("sofarpc server start..");
    }
}
