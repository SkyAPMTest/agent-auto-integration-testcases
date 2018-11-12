FROM openjdk:8

ENV COLLECTOR_SERVER=127.0.0.1:19876 \
    PROJECTB_URL=127.0.0.1:18080
WORKDIR /usr/local/header-scenario

RUN wget http://mirror.bit.edu.cn/apache/incubator/skywalking/5.0.0-GA/apache-skywalking-apm-incubating-5.0.0-GA.tar.gz && \
    tar -xvf apache-skywalking-apm-incubating-5.0.0-GA.tar.gz

ADD projectA.jar /usr/local/header-scenario/projectA.jar
ADD start.sh /usr/local/header-scenario/start.sh
ADD docker-entrypoint.sh /

RUN chmod +x /usr/local/header-scenario/start.sh && chmod +x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/header-scenario/start.sh"]