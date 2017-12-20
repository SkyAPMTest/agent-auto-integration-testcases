package org.skywalking.apm.testcase.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MysqlConfig {
    private static Logger logger = LogManager.getLogger(MysqlConfig.class);
    private static String url;
    private static String userName;
    private static String password;

    static {
        InputStream inputStream = MysqlConfig.class.getClassLoader().getResourceAsStream("/jdbc.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to load config", e);
        }

        url = properties.getProperty("mysql.url");
        userName = properties.getProperty("mysql.username");
        password = properties.getProperty("mysql.password");
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
