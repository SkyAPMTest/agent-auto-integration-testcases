package org.apache.skywalking.testcase.httpasyncclient;

import java.sql.SQLException;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SQLUtils {
    private static Logger logger = Logger.getLogger(SQLUtils.class);

    @Autowired
    private SQLExecutor sqlExecutor;

    private static SQLExecutor sqlExecute;

    @PostConstruct
    public void setUp() {
        sqlExecute = sqlExecutor;
        init();
    }

    public static void init() {
        final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS test_01(\n" +
            "id VARCHAR(1) PRIMARY KEY, \n" +
            "value VARCHAR(1) NOT NULL)";
        String INSERT_DATA_SQL = "INSERT INTO test_01(id, value) VALUES(?,?)";
        String DELETE_DATA_SQL = "DELETE FROM test_01";
        try {
            sqlExecute.createTable(CREATE_TABLE_SQL);
            sqlExecute.deleteTable(DELETE_DATA_SQL);
            sqlExecute.insertData(INSERT_DATA_SQL, "1", "1");
        } catch (SQLException e) {
            logger.error("Failed to execute sql.", e);
        } finally {
            if (sqlExecute != null) {
                try {
                    sqlExecute.closeConnection();
                } catch (SQLException e) {
                    logger.error("Failed to close connection.", e);
                }
            }
        }
    }

    public static void query() {
        String QUERY_DATA_SQL = "SELECT id, value FROM test_01 WHERE id=?";
        try {
            sqlExecute.queryData(QUERY_DATA_SQL, "1");
        } catch (SQLException e) {
            logger.error("Failed to execute sql.", e);
        } finally {
            if (sqlExecute != null) {
                try {
                    sqlExecute.closeConnection();
                } catch (SQLException e) {
                    logger.error("Failed to close connection.", e);
                }
            }
        }
    }

}
