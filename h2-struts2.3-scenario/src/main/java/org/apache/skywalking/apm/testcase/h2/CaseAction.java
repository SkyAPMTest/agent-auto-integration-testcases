package org.apache.skywalking.apm.testcase.h2;

import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CaseAction {
    private static Logger logger = LogManager.getLogger(CaseAction.class);
    private static final String CREATE_TABLE_SQL = "CREATE TABLE test_007(\n" +
        "id VARCHAR(1) PRIMARY KEY, \n" +
        "value VARCHAR(1) NOT NULL)";
    private static final String INSERT_DATA_SQL = "INSERT INTO test_007(id, value) VALUES(?,?)";
    private static final String DROP_TABLE_SQL = "DROP table test_007";

    public String execute() throws InterruptedException {
        SQLExecutor sqlExecute = null;
        try {
            sqlExecute = new SQLExecutor();
            sqlExecute.createTable(CREATE_TABLE_SQL);
            sqlExecute.insertData(INSERT_DATA_SQL, "1", "1");
            sqlExecute.dropTable(DROP_TABLE_SQL);
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
        return "SUCCESS";
    }
}
