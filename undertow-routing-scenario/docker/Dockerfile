FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800

ADD startup.sh /usr/local/skywalking-undertow-routing-scenario/
ADD undertow-routing-scenario.jar /usr/local/skywalking-undertow-routing-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-undertow-routing-scenario/startup.sh && chmod +x /docker-entrypoint.sh

VOLUME /usr/local/skywalking-undertow-routing-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-undertow-routing-scenario/startup.sh"]
