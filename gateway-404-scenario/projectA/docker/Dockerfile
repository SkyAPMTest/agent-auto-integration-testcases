FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800

ADD startup.sh /usr/local/gateway-404-scenario/
ADD projectA.jar /usr/local/gateway-404-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/gateway-404-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/gateway-404-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/gateway-404-scenario/startup.sh"]
