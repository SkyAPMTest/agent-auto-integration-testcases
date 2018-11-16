package test.apache.skywalking.apm.testcase.resttemplate.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {
    private static Logger logger = LogManager.getLogger(Config.class);
    private static String projectBURL;
    private static String projectCURL;
    private static String projectDURL;

    static {
        InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("/spring-config.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to load config", e);
        }

        projectBURL = properties.getProperty("spring.projectB.url");
        projectCURL = properties.getProperty("spring.projectC.url");
        projectDURL = properties.getProperty("spring.projectD.url");
    }

    public static String projectBURL() {
        return projectBURL;
    }

    public static String projectCURL() {
        return projectCURL;
    }

    public static String projectDURL() {
        return projectDURL;
    }

    public static void main(String[] args) {
        System.out.println(projectDURL());
    }
}
