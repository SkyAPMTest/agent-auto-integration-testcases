FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    ORACLE_ADDRESS=127.0.0.1:1521

ADD startup.sh /usr/local/skywalking-oracle-scenario/
ADD oracle-scenario.jar /usr/local/skywalking-oracle-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-oracle-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/skywalking-oracle-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-oracle-scenario/startup.sh"]
