FROM openjdk:8u111-jdk

ADD sofarpc-consumer.tar.gz /usr/local

COPY docker-entrypoint.sh /

RUN chmod +x /docker-entrypoint.sh

EXPOSE 8080
VOLUME /usr/local/sofarpc-consumer/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/sofarpc-consumer/bin/sofarpc-consumer.sh"]
