FROM tomcat:8.0.45-jre8-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    REDIS_HOST=127.0.0.1 \
    REDIS_PORT=6379

COPY catalina.sh /usr/local/tomcat/bin/
ADD jedis-standalone-case /usr/local/tomcat/webapps/jedis-standalone-case
COPY jedis.properties /usr/local/tomcat/webapps/jedis-standalone-case/WEB-INF/classes/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/tomcat/bin/catalina.sh && chmod +x /docker-entrypoint.sh

VOLUME /usr/local/tomcat/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["catalina.sh", "run"]
