package test.apache.skywalking.testcase.cassandra.controller;

import com.datastax.driver.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/case")
public class CaseController {

    private static final String CREATE_KEYSPACE_SQL = "CREATE KEYSPACE IF NOT EXISTS demo WITH replication = " +
        "{'class': 'SimpleStrategy', 'replication_factor': 1}";
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS demo.test(id TEXT PRIMARY KEY, value TEXT)";
    private static final String INSERT_DATA_SQL = "INSERT INTO demo.test(id, value) VALUES(?,?)";
    private static final String SELECT_DATA_SQL = "SELECT * FROM demo.test WHERE id = ?";
    private static final String DELETE_DATA_SQL = "DELETE FROM demo.test WHERE id = ?";
    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS demo.test";
    private static final String DROP_KEYSPACE = "DROP KEYSPACE IF EXISTS demo";
    private Cluster cluster;
    private Session session;
    private Logger logger = LogManager.getLogger(CaseController.class);
    @Value("${cassandra.host}")
    private String host;
    @Value("${cassandra.port}")
    private int port;

    @GetMapping("/cassandra")
    public String cassandraCase() {
        logger.info("cassandra contact points: {}:{}", host, port);

        try {
            cluster = Cluster.builder().addContactPoint(host).withPort(port).build();
            session = cluster.connect();
            logger.info("cassandra connection open");

            execute(session);
            executeAsync(session);

            return "success";
        } finally {
            if (session != null) {
                session.close();
            }
            if (cluster != null) {
                cluster.close();
            }
            logger.info("cassandra connection close");
        }
    }

    private void execute(Session session) {
        logger.info("execute in sync");

        ResultSet createKeyspaceDataResultSet = session.execute(CREATE_KEYSPACE_SQL);
        logger.info("CREATE KEYSPACE result: " + createKeyspaceDataResultSet.toString());

        ResultSet createTableDataResultSet = session.execute(CREATE_TABLE_SQL);
        logger.info("CREATE TABLE result: " + createTableDataResultSet.toString());

        PreparedStatement insertDataPreparedStatement = session.prepare(INSERT_DATA_SQL);
        ResultSet insertDataResultSet = session.execute(insertDataPreparedStatement.bind("101", "foobar"));
        logger.info("INSERT result: " + insertDataResultSet.toString());

        PreparedStatement selectDataPreparedStatement = session.prepare(SELECT_DATA_SQL);
        ResultSet resultSet = session.execute(selectDataPreparedStatement.bind("101"));
        Row row = resultSet.one();
        logger.info("SELECT result: id: {}, value: {}", row.getString("id"), row.getString("value"));

        PreparedStatement deleteDataPreparedStatement = session.prepare(DELETE_DATA_SQL);
        ResultSet deleteDataResultSet = session.execute(deleteDataPreparedStatement.bind("101"));
        logger.info("DELETE result: " + deleteDataResultSet.toString());

        ResultSet dropTableDataResultSet = session.execute(DROP_TABLE_SQL);
        logger.info("DROP TABLE result: " + dropTableDataResultSet.toString());

        ResultSet dropKeyspaceDataResultSet = session.execute(DROP_KEYSPACE);
        logger.info("DROP KEYSPACE result: " + dropKeyspaceDataResultSet.toString());
    }

    private void executeAsync(Session session) {
        logger.info("execute in async");

        ResultSetFuture createKeyspaceDataResultSetFuture = session.executeAsync(CREATE_KEYSPACE_SQL);
        ResultSet createKeyspaceDataResultSet = createKeyspaceDataResultSetFuture.getUninterruptibly();
        logger.info("CREATE KEYSPACE result: " + createKeyspaceDataResultSet.toString());

        ResultSetFuture createTableDataResultSetFuture = session.executeAsync(CREATE_TABLE_SQL);
        ResultSet createTableDataResultSet = createTableDataResultSetFuture.getUninterruptibly();
        logger.info("CREATE TABLE result: " + createTableDataResultSet.toString());

        PreparedStatement insertDataPreparedStatement = session.prepare(INSERT_DATA_SQL);
        ResultSetFuture insertDataResultSetFuture = session.executeAsync(insertDataPreparedStatement.bind("101", "foobar"));
        ResultSet insertDataResultSet = insertDataResultSetFuture.getUninterruptibly();
        logger.info("INSERT result: " + insertDataResultSet.toString());

        PreparedStatement selectDataPreparedStatement = session.prepare(SELECT_DATA_SQL);
        ResultSetFuture resultSetFuture = session.executeAsync(selectDataPreparedStatement.bind("101"));
        ResultSet resultSet = resultSetFuture.getUninterruptibly();
        Row row = resultSet.one();
        logger.info("SELECT result: id: {}, value: {}", row.getString("id"), row.getString("value"));

        PreparedStatement deleteDataPreparedStatement = session.prepare(DELETE_DATA_SQL);
        ResultSetFuture deleteDataResultSetFuture = session.executeAsync(deleteDataPreparedStatement.bind("101"));
        ResultSet deleteDataResultSet = deleteDataResultSetFuture.getUninterruptibly();
        logger.info("DELETE result: " + deleteDataResultSet.toString());

        ResultSetFuture dropTableDataResultSetFuture = session.executeAsync(DROP_TABLE_SQL);
        ResultSet dropTableDataResultSet = dropTableDataResultSetFuture.getUninterruptibly();
        logger.info("DROP TABLE result: " + dropTableDataResultSet.toString());

        ResultSetFuture dropKeyspaceDataResultSetFuture = session.executeAsync(DROP_KEYSPACE);
        ResultSet dropKeyspaceDataResultSet = dropKeyspaceDataResultSetFuture.getUninterruptibly();
        logger.info("DROP KEYSPACE result: " + dropKeyspaceDataResultSet.toString());
    }
}
