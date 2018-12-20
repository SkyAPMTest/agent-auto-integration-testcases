FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    CANAL_HOST=127.0.0.1

ADD startup.sh /usr/local/skywalking-canal-scenario/
ADD canal-scenario.jar /usr/local/skywalking-canal-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-canal-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/skywalking-canal-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-canal-scenario/startup.sh"]