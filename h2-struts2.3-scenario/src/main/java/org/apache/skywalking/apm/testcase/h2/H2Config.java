package org.apache.skywalking.apm.testcase.h2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class H2Config {
    private static Logger logger = LogManager.getLogger(H2Config.class);
    private static String url;
    private static String userName;
    private static String password;

    static {
        InputStream inputStream = H2Config.class.getClassLoader().getResourceAsStream("/jdbc.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to load config", e);
        }

        url = properties.getProperty("h2.url");
        userName = properties.getProperty("h2.username");
        password = properties.getProperty("h2.password");
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
