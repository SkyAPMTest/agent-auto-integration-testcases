FROM tomcat:9.0.4-jre9-slim

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    MYSQL_INSTANCE_HOST=127.0.0.1:3306 \
    MYSQL_INSTANCE_USERNAME=root \
    MYSQL_INSTANCE_PASSWORD=root

COPY catalina.sh /usr/local/tomcat/bin/
ADD mysql-case /usr/local/tomcat/webapps/mysql-case
COPY jdbc.properties /usr/local/tomcat/webapps/mysql-case/WEB-INF/classes/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/tomcat/bin/catalina.sh && chmod +x /docker-entrypoint.sh

VOLUME /usr/local/tomcat/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["catalina.sh", "run"]
