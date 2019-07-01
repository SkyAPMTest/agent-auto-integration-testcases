FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    SPRING_ASYNC_HOST=127.0.0.1

ADD startup.sh /usr/local/skywalking-spring-async-scenario/
ADD spring-async-scenario.jar /usr/local/skywalking-spring-async-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-spring-async-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/skywalking-spring-async-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-spring-async-scenario/startup.sh"]