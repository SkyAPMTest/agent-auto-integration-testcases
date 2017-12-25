FROM openjdk:8-jdk

ENV COLLECTOR_SERVER=127.0.0.1:12800

ADD startup.sh /usr/local/spring-resttemplate-scenario/
ADD spring-resttemplate.jar /usr/local/spring-resttemplate-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/spring-resttemplate-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 5005
EXPOSE 8080

VOLUME /usr/local/spring-resttemplate-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/spring-resttemplate-scenario/startup.sh"]
