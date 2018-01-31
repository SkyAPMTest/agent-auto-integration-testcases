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

package org.apache.skywalking.testcase.oracle.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/case")
@PropertySource("classpath:application.properties")
public class CaseController {

    private Logger logger = LogManager.getLogger(CaseController.class);

    @Value("${oracle.address:127.0.0.1:1521}")
    private String oracleHostAndPort;
    @Value("${oracle.username:system}")
    private String oracleUsername;
    @Value("${oracle.password:oracle}")
    private String oraclePassword;

    private String connectURL;
    private static final String CREATE_TABLE_SQL = "CREATE TABLE test_007(\n" +
        "id VARCHAR(1) PRIMARY KEY, \n" +
        "value VARCHAR(1) NOT NULL)";
    private static final String INSERT_DATA_SQL = "INSERT INTO test_007(id, value) VALUES(?,?)";
    private static final String QUERY_DATA_SQL = "SELECT id, value FROM test_007 WHERE id=?";
    private static final String DROP_TABLE_SQL = "DROP table test_007";

    @PostConstruct
    public void setUp() throws ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        connectURL = "jdbc:oracle:thin:@" + oracleHostAndPort + ":xe";
    }

    @RequestMapping("/oracle-scenario")
    @ResponseBody
    public String oracleScenario() {
        Connection connection = null;
        try {
            // create table by using statement
            connection = DriverManager.getConnection(connectURL, oracleUsername, oraclePassword);
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE_SQL);
            statement.close();

            // insert table by using PreparedStatement
            PreparedStatement insertDataPreparedStatement = connection.prepareStatement(INSERT_DATA_SQL);
            insertDataPreparedStatement.setString(1, "1");
            insertDataPreparedStatement.setString(2, "1");
            insertDataPreparedStatement.execute();
            insertDataPreparedStatement.close();

            // query data by using PreparedStatement
            PreparedStatement queryDataPreparedStatement = connection.prepareStatement(QUERY_DATA_SQL);
            queryDataPreparedStatement.setString(1, "1");
            ResultSet resultSet = queryDataPreparedStatement.executeQuery();
            resultSet.next();
            logger.info("Query id[{}]: value={}", "1", resultSet.getString(2));
            queryDataPreparedStatement.close();

            // drop table by using statement
            Statement dropTableStatement = connection.createStatement();
            dropTableStatement.execute(DROP_TABLE_SQL);
            dropTableStatement.close();

        } catch (SQLException e) {
            logger.error("Failed to execute sql", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Failed to close connection", e);
                }
            }
        }

        return "success";
    }
}
