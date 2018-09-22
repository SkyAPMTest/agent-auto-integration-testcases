package test.apache.skywalking.apm.testcase.jedis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RedisConfig {
    private static Logger logger = LogManager.getLogger(RedisConfig.class);
    private static String redisHost;
    private static int redisPort;

    static {
        InputStream inputStream = RedisConfig.class.getClassLoader().getResourceAsStream("/jedis.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to load config.", e);
        }

        redisHost = properties.getProperty("redis.host");
        redisPort = Integer.parseInt(properties.getProperty("redis.port"));
    }

    public static String getRedisHost() {
        return redisHost;
    }

    public static int getRedisPort() {
        return redisPort;
    }
}
