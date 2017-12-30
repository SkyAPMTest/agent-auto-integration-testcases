FROM openjdk:8-jdk

ENV COLLECTOR_SERVER=127.0.0.1:12800

ADD startup.sh /usr/local/spring-restapi-scenario/
ADD spring-projectB.jar /usr/local/spring-restapi-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/spring-restapi-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 5005
EXPOSE 8080

VOLUME /usr/local/spring-restapi-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/spring-restapi-scenario/startup.sh"]
