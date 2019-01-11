FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    REDISSON_HOST=127.0.0.1

ADD startup.sh /usr/local/skywalking-redisson-scenario/
ADD redisson-scenario.jar /usr/local/skywalking-redisson-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-redisson-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/skywalking-redisson-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-redisson-scenario/startup.sh"]