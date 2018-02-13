FROM openjdk:8u111-jdk

ENV SERVICE_CENTER_HOST=127.0.0.1 \
    CONSUMER_REST_BIND_HOST=127.0.0.1 \
    COLLECTOR_SERVER=127.0.0.1:12800

ADD servicecomb-consumer.tar.gz /usr/local

COPY docker-entrypoint.sh /
COPY microservice.yaml /usr/local/servicecomb-consumer/config/

RUN chmod +x /docker-entrypoint.sh

EXPOSE 9090

VOLUME /usr/local/servicecomb-consumer/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/servicecomb-consumer/bin/servicecomb-service.sh"]

