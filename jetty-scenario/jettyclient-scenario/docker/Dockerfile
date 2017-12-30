FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    JETTY_SERVER_HOST=127.0.0.1

ADD startup.sh /usr/local/skywalking-jettyclient-scenario/
ADD jettyclient-scenario.jar /usr/local/skywalking-jettyclient-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-jettyclient-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/skywalking-jettyclient-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-jettyclient-scenario/startup.sh"]
