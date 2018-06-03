/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package org.apache.skywaking.apm.testcase.sofarpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @author bystander
 * @version $Id: ClientApplication.java, v 0.1 2018-05-12 2:07 PM bystander Exp $
 */
@ImportResource({"classpath:sofarpc_consumer.xml"})
@SpringBootApplication
@ComponentScan(value = {"org.apache.skywaking.apm.testcase.sofarpc"})
public class ClientApplication {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(ClientApplication.class);

        ApplicationContext applicationContext = springApplication.run(args);

        System.out.println("sofarpc client start..");
    }
}