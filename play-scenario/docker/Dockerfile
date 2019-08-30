FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800

ADD startup.sh /usr/local/play-scenario/
COPY lib /usr/local/play-scenario/lib

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/play-scenario/startup.sh && chmod +x /docker-entrypoint.sh

VOLUME /usr/local/play-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/play-scenario/startup.sh"]
