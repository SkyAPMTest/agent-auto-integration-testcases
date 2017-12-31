FROM openjdk:8-jdk

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    PROJECTB_URL=127.0.0.1:18080 \
    PROJECTC_URL=127.0.0.1:28080

ADD startup.sh /usr/local/feign-scenario/
ADD feign-scenario.jar /usr/local/feign-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/feign-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 5005
EXPOSE 8080

VOLUME /usr/local/feign-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/feign-scenario/startup.sh"]
