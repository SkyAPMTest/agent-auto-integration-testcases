FROM openjdk:8-jdk

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    PROJECTB_URL=127.0.0.1:18080 \
    PROJECTC_URL=127.0.0.1:28080

ADD startup.sh /usr/local/spring-scenario/
ADD spring-projectA.jar /usr/local/spring-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/spring-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 5005
EXPOSE 8080

VOLUME /usr/local/spring-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/spring-scenario/startup.sh"]
