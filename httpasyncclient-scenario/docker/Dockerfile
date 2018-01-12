FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    MYSQL_HOST=127.0.0.1:3306 \
    MYSQL_USERNAME=root \
    MYSQL_PASSWORD=root


ADD startup.sh /usr/local/skywalking-httpasyncclient-scenario/
ADD httpasyncclient-scenario.jar /usr/local/skywalking-httpasyncclient-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-httpasyncclient-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/skywalking-httpasyncclient-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-httpasyncclient-scenario/startup.sh"]
