FROM openjdk:8u111-jdk

ENV SERVICE_CENTER_HOST=127.0.0.1 \
    PROVIDER_REST_BIND_HOST=0.0.0.0 \
    COLLECTOR_SERVER=127.0.0.1:12800

ADD servicecomb-provider.tar.gz /usr/local

COPY docker-entrypoint.sh /
COPY microservice.yaml /usr/local/servicecomb-provider/config/

RUN chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/servicecomb-provider/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/servicecomb-provider/bin/servicecomb-service.sh"]

