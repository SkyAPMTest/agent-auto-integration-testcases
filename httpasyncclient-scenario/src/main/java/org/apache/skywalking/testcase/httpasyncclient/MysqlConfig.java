package org.apache.skywalking.testcase.httpasyncclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@PropertySource("classpath:application.properties")
public class MysqlConfig {

    @Value(value = "${mysql.host:127.0.0.1:3306}")
    private String host;
    @Value(value = "${mysql.username:root}")
    private String userName;
    @Value(value = "${mysql.password:root}")
    private String password;

    public String getUrl() {
        String url = "jdbc:mysql://" + host + "/sky?useUnicode=true&characterEncoding=UTF-8";
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
