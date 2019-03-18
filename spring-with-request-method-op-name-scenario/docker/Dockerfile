FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800

ADD startup.sh /usr/local/skywalking-spring-with-request-method-op-name-scenario/

ADD spring-with-request-method-op-name-scenario.jar /usr/local/skywalking-spring-with-request-method-op-name-scenario/
ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-spring-with-request-method-op-name-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/skywalking-spring-with-request-method-op-name-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-spring-with-request-method-op-name-scenario/startup.sh"]