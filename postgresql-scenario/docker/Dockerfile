FROM tomcat:8.0.45-jre8-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    POSTGRES_INSTANCE_HOST=127.0.0.1:5432 \
    POSTGRES_INSTANCE_USERNAME=root \
    POSTGRES_INSTANCE_PASSWORD=root

COPY catalina.sh /usr/local/tomcat/bin/
ADD postgresql-scenario /usr/local/tomcat/webapps/postgresql-scenario
COPY jdbc.properties /usr/local/tomcat/webapps/postgresql-scenario/WEB-INF/classes/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/tomcat/bin/catalina.sh && chmod +x /docker-entrypoint.sh

VOLUME /usr/local/tomcat/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["catalina.sh", "run"]
