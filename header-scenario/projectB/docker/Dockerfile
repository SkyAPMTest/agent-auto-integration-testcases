FROM openjdk:8

ENV COLLECTOR_SERVER=127.0.0.1:19876 \
    PROJECTC_ADDRESS=127.0.0.1:28080 \
    PROJECTD_ADDRESS=127.0.0.1:38080

WORKDIR /usr/local/header-scenario

ADD projectB.jar /usr/local/header-scenario/projectB.jar
ADD start.sh /usr/local/header-scenario/start.sh
ADD docker-entrypoint.sh /

RUN chmod +x /usr/local/header-scenario/start.sh && chmod +x /docker-entrypoint.sh

VOLUME /usr/local/header-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/header-scenario/start.sh"]