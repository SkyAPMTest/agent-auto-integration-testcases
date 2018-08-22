FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    ELASTICSEARCH_HOST=127.0.0.1

ADD startup.sh /usr/local/skywalking-elasticsearch-scenario/
ADD elasticsearch-scenario.jar /usr/local/skywalking-elasticsearch-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-elasticsearch-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/skywalking-elasticsearch-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-elasticsearch-scenario/startup.sh"]
