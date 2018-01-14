package org.apache.skywalking.testcase.httpasyncclient;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SQLExecutor {
    private Connection connection;
    private static Logger logger = Logger.getLogger(SQLExecutor.class);
    @Autowired
    MysqlConfig mysqlConfig;

    public SQLExecutor() throws SQLException {
    }

    @PostConstruct
    public void setUp() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }

    }

    private void initConnection() throws SQLException {
        connection = DriverManager.getConnection(mysqlConfig.getUrl(), mysqlConfig.getUserName(), mysqlConfig.getPassword());
    }

    public void createTable(String sql) throws SQLException {
        initConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void insertData(String sql, String id, String name) throws SQLException {
        initConnection();
        CallableStatement preparedStatement = connection.prepareCall(sql);
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void queryData(String sql, String id) throws SQLException {
        initConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            logger.info(rs.getString(1));
        }
        preparedStatement.close();
    }

    public void deleteTable(String sql) throws SQLException {
        initConnection();
        Statement preparedStatement = connection.createStatement();
        preparedStatement.execute(sql);
        preparedStatement.close();
    }

    public void closeConnection() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }
}
