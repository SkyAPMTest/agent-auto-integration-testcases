FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    CASSANDRA_HOST=127.0.0.1 \
    CASSANDRA_PORT=9042

ADD startup.sh /usr/local/skywalking-cassandra-scenario/
ADD cassandra-scenario.jar /usr/local/skywalking-cassandra-scenario/cassandra-scenario.jar

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-cassandra-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/skywalking-cassandra-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-cassandra-scenario/startup.sh"]
