package test.apache.skywalking.apm.testcase.h2;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLExecutor {
    private Connection connection;

    public SQLExecutor() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            //
        }
        connection = DriverManager.getConnection(H2Config.getUrl(), H2Config.getUserName(), H2Config.getPassword());
    }

    public void createTable(String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void insertData(String sql, String id, String value) throws SQLException {
        CallableStatement preparedStatement = connection.prepareCall(sql);
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, value);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void dropTable(String sql) throws SQLException {
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
