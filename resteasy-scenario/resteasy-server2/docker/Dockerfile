FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    RESTEASY_PORT=8080

ADD startup.sh /usr/local/resteasy-server2/
ADD resteasy-server2.jar /usr/local/resteasy-server2/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/resteasy-server2/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/resteasy-server2/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/resteasy-server2/startup.sh"]