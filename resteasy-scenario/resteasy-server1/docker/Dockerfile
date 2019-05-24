FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    RESTEASY_PORT=18080

ADD startup.sh /usr/local/resteasy-server1/
ADD resteasy-server1.jar /usr/local/resteasy-server1/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/resteasy-server1/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 18080

VOLUME /usr/local/resteasy-server1/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/resteasy-server1/startup.sh"]