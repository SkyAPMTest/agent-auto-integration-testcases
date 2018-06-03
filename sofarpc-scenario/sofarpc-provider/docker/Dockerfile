FROM openjdk:8u111-jdk

ENV ZK_ADDRESS=127.0.0.1:2181 \
    COLLECTOR_SERVER=127.0.0.1:12800

ADD sofarpc-provider.tar.gz /usr/local

COPY docker-entrypoint.sh /
COPY sofarpc_provider.xml /usr/local/sofarpc-provider/config/

RUN chmod +x /docker-entrypoint.sh

EXPOSE 12200
EXPOSE 5005
VOLUME /usr/local/sofarpc-provider/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/sofarpc-provider/bin/sofarpc-service.sh"]
