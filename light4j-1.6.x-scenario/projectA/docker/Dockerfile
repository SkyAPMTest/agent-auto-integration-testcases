FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800

ADD startup.sh /usr/local/skywalking-light4j-scenario/
ADD projectA.jar /usr/local/skywalking-light4j-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-light4j-scenario/startup.sh && chmod +x /docker-entrypoint.sh

VOLUME /usr/local/skywalking-light4j-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-light4j-scenario/startup.sh"]
