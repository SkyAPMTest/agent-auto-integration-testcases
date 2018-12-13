FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    PROJECTB_ADDRESS=127.0.0.1:18080

ADD startup.sh /usr/local/webflux-scenario/
ADD projectA.jar /usr/local/webflux-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/webflux-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/webflux-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/webflux-scenario/startup.sh"]
