FROM openjdk:8u111-jdk

ENV ZK_ADDRESS=127.0.0.1:2181 \
    COLLECTOR_SERVER=127.0.0.1:12800

ADD dubbo-provider.tar.gz /usr/local

COPY docker-entrypoint.sh /
COPY dubbo-provider.xml /usr/local/dubbo-provider/config/

RUN chmod +x /docker-entrypoint.sh

EXPOSE 20880
EXPOSE 5005
VOLUME /usr/local/dubbo-provider/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/dubbo-provider/bin/dubbo-service.sh"]
