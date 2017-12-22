package org.skywalking.apm.testcase.postgresql;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostgresqlConfig {
    private static Logger logger = LogManager.getLogger(PostgresqlConfig.class);
    private static String url;
    private static String userName;
    private static String password;

    static {
        InputStream inputStream = PostgresqlConfig.class.getClassLoader().getResourceAsStream("/jdbc.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to load config", e);
        }

        url = properties.getProperty("postgres.url");
        userName = properties.getProperty("postgres.username");
        password = properties.getProperty("postgres.password");
    }

    public static String getUrl() {
        return url;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }
}
