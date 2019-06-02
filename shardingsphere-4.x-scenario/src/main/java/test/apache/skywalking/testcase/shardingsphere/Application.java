/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package test.apache.skywalking.testcase.shardingsphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import test.apache.skywalking.testcase.shardingsphere.service.api.service.CommonService;
import test.apache.skywalking.testcase.shardingsphere.service.config.ShardingDatabasesAndTablesConfigurationPrecise;
import test.apache.skywalking.testcase.shardingsphere.service.repository.jdbc.JDBCOrderItemRepositoryImpl;
import test.apache.skywalking.testcase.shardingsphere.service.repository.jdbc.JDBCOrderRepositoryImpl;
import test.apache.skywalking.testcase.shardingsphere.service.repository.service.RawPojoService;
import test.apache.skywalking.testcase.shardingsphere.service.utility.config.DataSourceUtil;

import javax.sql.DataSource;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("test.apache.skywalking.testcase.shardingsphere")
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Application.class, args);
            DataSourceUtil.createDataSource("");
            DataSourceUtil.createSchema("demo_ds_0");
            DataSourceUtil.createSchema("demo_ds_1");
            DataSourceUtil.createDataSource("demo_ds_0");
            DataSourceUtil.createDataSource("demo_ds_1");
            DataSource dataSource = new ShardingDatabasesAndTablesConfigurationPrecise().createDataSource();
            CommonService commonService = new RawPojoService(new JDBCOrderRepositoryImpl(dataSource), new JDBCOrderItemRepositoryImpl(dataSource));
            commonService.initEnvironment();
        } catch (Exception e) {
            // Never do this
        }
    }
}
