FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    ZK_ADDRESS=127.0.0.1:2181

ADD startup.sh /usr/local/skywalking-zookeeper-scenario/
ADD zookeeper-scenario.jar /usr/local/skywalking-zookeeper-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-zookeeper-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/skywalking-zookeeper-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-zookeeper-scenario/startup.sh"]