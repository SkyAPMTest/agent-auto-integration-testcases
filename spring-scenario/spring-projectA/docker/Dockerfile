FROM tomcat:8.0.45-jre8-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    PROJECTB_URL=127.0.0.1:18080 \
    PROJECTC_URL=127.0.0.1:28080 \
    PROJECTD_URL=127.0.0.1:38080

COPY catalina.sh /usr/local/tomcat/bin/
ADD resttemplate-case /usr/local/tomcat/webapps/resttemplate-case
COPY spring-config.properties /usr/local/tomcat/webapps/resttemplate-case/WEB-INF/classes/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/tomcat/bin/catalina.sh && chmod +x /docker-entrypoint.sh

VOLUME /usr/local/tomcat/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["catalina.sh", "run"]
