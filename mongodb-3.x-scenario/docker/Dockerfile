FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    MONGO_HOST=127.0.0.1

ADD startup.sh /usr/local/skywalking-mongodb-scenario/
ADD mongodb-scenario.jar /usr/local/skywalking-mongodb-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-mongodb-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/skywalking-mongodb-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-mongodb-scenario/startup.sh"]
