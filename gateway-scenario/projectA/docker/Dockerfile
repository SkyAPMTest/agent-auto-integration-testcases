FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    PROJECTB_ADDRESS=127.0.0.1:18080

ADD startup.sh /usr/local/gateway-scenario/
ADD projectA.jar /usr/local/gateway-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/gateway-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/gateway-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/gateway-scenario/startup.sh"]
