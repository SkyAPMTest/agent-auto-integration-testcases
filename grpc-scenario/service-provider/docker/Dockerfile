FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800

ADD startup.sh /usr/local/skywalking-grpc-scenario/
ADD service-provider.jar /usr/local/skywalking-grpc-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-grpc-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 18080

VOLUME /usr/local/skywalking-grpc-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-grpc-scenario/startup.sh"]