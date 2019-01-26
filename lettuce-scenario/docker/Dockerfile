FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    LETTUCE_HOST=127.0.0.1

ADD startup.sh /usr/local/skywalking-lettuce-scenario/
ADD lettuce-scenario.jar /usr/local/skywalking-lettuce-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-lettuce-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/skywalking-lettuce-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-lettuce-scenario/startup.sh"]